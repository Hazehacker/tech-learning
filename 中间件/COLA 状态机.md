
[Cola-StateMachine状态机的实战使用-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/2350454)
[保姆式教程！如何使用Cola-statemachine构建高可靠性的状态机 ？ cola-statemachine可以帮 - 掘金](https://juejin.cn/post/7237996261214814263)


**Cola-StateMachine** 是阿里巴巴开源的一个轻量级、高性能的状态机框架，专注于解决复杂业务场景中的状态流转问题。它基于无状态设计，使用纯 Java 实现，支持事件驱动和分布式事务，适用于高并发场景。


# 核心概念

Cola-StateMachine 的核心包括以下几个部分：

- **State（状态）**：描述系统的某一特定状态。
    
- **Event（事件）**：触发状态转换的动作。
    
- **Transition（流转）**：定义从一个状态到另一个状态的逻辑。
    
- **Condition（条件）**：判断是否允许状态转换。
    
- **Action（动作）**：状态转换时执行的操作。
    
- **StateMachine（状态机）**：包含所有状态、事件和流转规则的整体结构。


# 使用步骤


* **引入依赖**：在项目中添加 Cola-StateMachine 的 Maven 依赖
```xml
<dependency>
   <groupId>com.alibaba.cola</groupId>
   <artifactId>cola-component-statemachine</artifactId>
   <version>4.4.0</version>
</dependency>
```

* **定义状态和事件**：使用枚举类定义系统的状态和触发事件。

```java
public enum OrderStatusEnum {
   INITIAL, PAY_PENDING, PAY_SUCCESS, CANCELED
}
public enum OrderEventEnum {
   CREATE_ORDER, PAY_SUCCESS, CANCEL_ORDER
}
```


* **定义上下文**：创建上下文类，用于在状态转换时传递额外数据。
```java
public class OrderContext {
   private String orderId;
   private Integer payAmount;
}
```


* **配置状态机**：通过 _StateMachineBuilder_ 定义状态流转规则

```java
StateMachineBuilder<OrderStatusEnum, OrderEventEnum, OrderContext> builder = StateMachineBuilderFactory.create();
builder.externalTransition()
      .from(OrderStatusEnum.INITIAL)
      .to(OrderStatusEnum.PAY_PENDING)
      .on(OrderEventEnum.CREATE_ORDER)
      .when(context -> context.getPayAmount() > 0)
      .perform((from, to, event, context) -> System.out.println("创建订单成功"));
builder.build("Order_StateMachine");

```


* **触发事件**：通过 _fireEvent_ 方法触发状态转换
```java
StateMachine<OrderStatusEnum, OrderEventEnum, OrderContext> stateMachine = StateMachineFactory.get("Order_StateMachine");
OrderContext context = new OrderContext();
context.setPayAmount(100);
stateMachine.fireEvent(OrderStatusEnum.INITIAL, OrderEventEnum.CREATE_ORDER, context);
```

# 优势与应用场景
Cola-StateMachine 的主要优势在于其轻量级、高性能和语义清晰。它适用于电商订单管理、支付状态流转等场景，能够显著提升代码的**可维护性**，同时避免复杂的 if-else 逻辑。通过 Cola-StateMachine，开发者可以轻松实现状态的定义、事件的触发以及状态流转的控制，从而**简化复杂业务逻辑的实现**。

Deep thinking
```The user wants me to analyze whether it's necessary to use COLA state machine in the current project. Let me think about this carefully.

First, let me understand the business context:
- This is a purchase note (采购单) audit scenario
- States: PENDING (0=未审核) and AUDITED (1=已审核)
- Events: DO_AUDIT and UNDO_AUDIT
- Only 2 states, 2 transitions

Let me think about the pros and cons of using COLA state machine here:

**Arguments FOR COLA state machine:**
1. Clean separation of concerns - state transition logic is in one place


2. Extensible - easy to add more states (e.g., partial audit, rejected state)
3. Industry pattern - recognizable pattern for other developers
4. Condition/Guard validation is structured
5. Good for resume/showcase purposes

**Arguments AGAINST COLA state machine:**
1. **Over-engineering for 2 states**: COLA state machine is most valuable when you have many states (5+) with complex transitions. For just PENDING↔AUDITED, a simple if-else is equivalent in complexity.
2. **Dependency cost**: Adds `cola-component-statemachine` as a new dependency
3. **Learning curve**: New team members need to understand the framework
4. **The state machine doesn't eliminate the need for Redisson lock** - the lock is still the primary protection mechanism
5. **Transaction boundary complexity**: Moving DB logic into the state machine Action creates an awkward layering - actions are called from within a `@Transactional` method, which blurs the transaction boundary
6. **The real protection is Redisson lock**: If you have the distributed lock, the state check (current == target) achieves the same idempotency guarantee without a state machine
7. **COLA state machine is stateless (doesn't store state)**: It just orchestrates the transition logic. The "state" is still stored in the DB and loaded manually. So it's really just a more formal way of writing if-else chains.

**Key analysis for this project:**

The purchase note has these states across different fields:
- `examine`: 0=

未审核, 1=已审核 (2 states)
- `nucleus`: 0=未核销, 1=部分核销, 2=已核销 (3 states)
- `cse`: 0=未结算, 1=部分结算, 2=已结算, 3=无需结算 (4 states)
- `invoice`: 0=未开票, 1=部分开票, 2=已开票, 3=无需开具 (4 states)
- `check`: 0=未核对, 1=已核对 (2 states)

Looking at the full state landscape, if we were modeling all these fields and their interactions as a compound state machine, COLA would provide real value. But for a single boolean-like field like examine, it's introducing unnecessary complexity.

For just the `examine` field, COLA isn't technically necessary—the Redisson lock already handles concurrent access protection, and a simple state check with optimistic locking is sufficient. That said, from a career perspective, demonstrating familiarity with enterprise frameworks like COLA can be a useful talking point.

COLA really shines when dealing with 5+ states and intricate transition rules, when you need declarative graphs for documentation, when multiple teams coordinate around state transitions, or when audit trails become critical. For simpler scenarios, it's overkill.

The more pragmatic alternatives here are either a straightforward if-else with Redisson lock plus optimistic locking using a DB version field, or leveraging atomic CAS updates with an `UPDATE ... WHERE examine=0` pattern. Actually, that WHERE clause approach is more robust than COLA for this use case because it relies on database-level atomicity rather than application-level state management.
```
需要从业务复杂度、技术收益、成本三个维度客观分析。核心问题是：当前采购单审核的状态图是否足够复杂，值得引入 COLA 这个框架级依赖。


### 什么情况下 COLA 才真正值得引入

| 判断标准 | 当前项目 |
|---|---|
| 状态数 ≥ 5 | ✗ 只有 2 个 |
| 迁移规则复杂、有多个并行约束 | ✗ 线性双向 |
| 多人维护、需要状态图作为契约文档 | 不确定 |
| 后续计划增加"部分审核"、"撤回"、"驳回"等中间状态 | **这种情况才值得** |







# 细化应用场景与 COLA 必要性证明

## 场景一：电商订单全生命周期管理

### 业务背景
一个完整的订单涉及：创建 → 支付中 → 已支付 → 发货中 → 已发货 → 确认收货 → 完成，其中任意环节都可能因为超时、取消、退款等操作产生分支。

### 不用 COLA 的问题

假设用 if-else 实现：

```java
public void handleOrderEvent(OrderEventEnum event, Order order) {
    if (event == CREATE_ORDER) {
        if (order.getStatus() == INITIAL) {
            order.setStatus(PAY_PENDING);
        }
    } else if (event == PAY_SUCCESS) {
        if (order.getStatus() == PAY_PENDING) {
            order.setStatus(PAID);
        } else if (order.getStatus() == CANCELED) {
            return;
        }
    } else if (event == CANCEL) {
        if (order.getStatus() == PAY_PENDING) {
            order.setStatus(CANCELED);
        } else if (order.getStatus() == PAID) {
            order.setStatus(REFUNDING);
        } else if (order.getStatus() == SHIPPED) {
            throw new BusinessException("商品已发货，请拒收后申请退款");
        }
    }
}
```

**问题分析**：
- **状态-事件耦合爆炸**：每加一种状态或事件，需要在多个分支中新增逻辑，遗漏即为 Bug
- **无法保证状态合法转换**：if-else 无法约束哪些转换是合法的，代码中散落的校验随时可能被绕过
- **并发安全问题**：库存扣减、余额操作等在高并发下，多个 if 分支可能同时通过
- **测试困难**：所有分支排列组合，单元测试难以覆盖所有路径

### 使用 COLA 后的效果

```java
builder.externalTransition()
    .from(OrderStatus.INITIAL)
    .to(OrderStatus.PAY_PENDING)
    .on(OrderEvent.CREATE_ORDER)
    .when(checkOrderValid())
    .perform(doCreateOrder());

builder.externalTransition()
    .from(OrderStatus.PAY_PENDING)
    .to(OrderStatus.CANCELED)
    .on(OrderEvent.CANCEL)
    .when(checkNotExpired())
    .perform(doCancelWithRestock());

builder.externalTransition()
    .from(OrderStatus.PAID)
    .to(OrderStatus.REFUNDING)
    .on(OrderEvent.APPLY_REFUND)
    .when(checkRefundAmount())
    .perform(doInitRefund());

StateMachine<OrderStatus, OrderEvent, OrderContext> sm = StateMachineFactory.get("order");
TransitionResult result = sm.fireEvent(currentStatus, event, context);
```

**必要性证明**：

| 维度 | if-else 实现 | COLA 实现 |
|------|-------------|-----------|
| 新增状态 | 需修改所有相关 if 分支，O(n) | 仅需新增一条 Transition 配置，O(1) |
| 状态转换合法性 | 依赖人工 code review | 框架强制约束 |
| 并发安全 | 需手动加锁 | Transition 粒度可控制 |
| 可视化 | 需额外文档 | 配置即文档 |

---

## 场景二：支付回调幂等性处理

### 业务背景
支付渠道（微信/支付宝）回调存在重复回调、异步乱序的问题，同一个支付结果可能推送多次。状态机需要保证：**即使回调重复到达，最终状态也是正确的**。

### 不用 COLA 的问题

```java
public void handlePayCallback(PayCallbackRequest request) {
    Order order = orderService.getById(request.getOrderId());
    if (order.getPayStatus() == PAID) {
        log.info("订单已支付，直接返回");
        return;
    }
    if (order.getPayStatus() == CANCELED) {
        log.warn("订单已取消，退款处理");
        refundService.refund(order.getId());
        return;
    }
    order.setPayStatus(PAID);
    orderService.update(order);
    orderCompleted(order.getId());
}
```

**问题**：
- 状态判断和业务动作混在一起，逻辑分支极多
- 新增一种状态（如"Frozen"）时，所有判断处都要加分支
- **判断和更新之间存在时间窗口**，并发时可能被绕过

### 使用 COLA 后的效果

```java
builder.externalTransition()
    .from(OrderStatus.PAY_PENDING)
    .to(OrderStatus.PAID)
    .on(PayEvent.PAY_SUCCESS)
    .when(checkSignature())
    .when(checkAmountMatch())
    .perform(doUpdatePayInfo())
    .perform(doTriggerDelivery());

// 重复回调：状态已是 PAID，PAY_SUCCESS 事件无对应 Transition，框架自动拒绝
// 订单取消后回调：状态是 CANCELED，框架拒绝，无任何副作用
```

**必要性证明**：
- **幂等天然保证**：非法的状态+事件组合，框架直接拒绝，无需业务层判断
- **回调乱序无害**：先发超时再发成功，成功事件在超时之后因为状态不对而被拒绝
- **事务一致性好**：Condition→Action 链作为原子执行单元，不存在判断和执行之间的空隙

---

## 场景三：分布式事务下的状态补偿

### 业务背景
订单支付成功后，需要调用库存服务扣减库存、调用物流服务创建物流单。如果其中任意一步失败，需要回滚之前的状态。

### 不用 COLA 的问题

```java
@KafkaListener(topics = "order-paid")
public void onOrderPaid(OrderPaidEvent event) {
    try {
        inventoryService.decrease(event.getSkuId(), event.getQuantity());
    } catch (Exception e) {
        orderService.updateStatus(event.getOrderId(), "INVENTORY_FAILED");
        retryService.schedule("inventory", event.getOrderId());
    }
    try {
        logisticsService.createShipping(event.getOrderId());
    } catch (Exception e) {
        // 物流失败呢？扣减的库存要不要回滚？
    }
}
```

**问题**：
- 失败补偿的逻辑散落在各处，没有统一的状态视图
- 人工介入时，无法快速判断当前处于哪个阶段
- 新增一个下游服务，整个补偿链路都要改

### 使用 COLA 后的效果

```java
builder.externalTransition()
    .from(OrderStatus.PAID)
    .to(OrderStatus.DELIVERING)
    .on(OrderEvent.START_DELIVERY)
    .when(checkInventoryAvailable())
    .perform(doReserveInventory())
    .perform(doCreateShippingOrder());

builder.externalTransition()
    .from(OrderStatus.DELIVERING)
    .to(OrderStatus.INVENTORY_RETRY)
    .on(OrderEvent.INVENTORY_FAILED)
    .when(checkRetryCountLessThan(3))
    .perform(doScheduleRetry());

builder.externalTransition()
    .from(OrderStatus.DELIVERING)
    .to(OrderStatus.SHIPPING_FAILED)
    .on(OrderEvent.SHIPPING_FAILED)
    .perform(doNotifyOps());
```

**必要性证明**：
- **状态即文档**：看到 `INVENTORY_RETRY` 状态，立刻知道问题在哪
- **补偿逻辑集中**：状态流转规则一处定义，补偿路径清晰
- **与 Saga/补偿事务天然配合**：状态机的每个状态对应 Saga 的一个步骤，失败后的流转就是补偿路径

---

## 场景四：运营后台——订单状态人工干预

### 业务背景
运营人员经常需要手动修改订单状态（如强制取消、补录物流）。如果不对操作加以限制，可能导致财务风险。

### 不用 COLA 的问题

```java
@PostMapping("/admin/updateStatus")
public void updateStatus(Long orderId, String targetStatus) {
    // 没有校验：运营可以从小程序直接改成"已发货"（跳过支付）
    orderService.updateStatus(orderId, targetStatus);
}
```

**问题**：任意状态可跳转到任意状态，完全绕过业务规则，风险极高。

### 使用 COLA 后的效果

```java
builder.externalTransition()
    .from(OrderStatus.PAY_PENDING)
    .to(OrderStatus.CANCELED)
    .on(OrderEvent.ADMIN_CANCEL)
    .when(checkPermission("ORDER_CANCEL"))
    .when(checkOperatorAuditLog())
    .perform(doCancelWithReason());

builder.externalTransition()
    .from(OrderStatus.DELIVERING)
    .to(OrderStatus.SHIPPED)
    .on(OrderEvent.ADMIN_CONFIRM_SHIP)
    .when(checkTrackingNoProvided())
    .perform(doUpdateShippingInfo());

// 其他非法转换（如 ADMIN_CANCEL 从 PAID 跳到 SHIPPED）根本不存在
```

**必要性证明**：
- **操作白名单化**：管理员只能做被允许的操作，无法越界
- **操作必须带审计**：每个转换的 Action 中记录操作人、时间、原因
- **配置即权限**：新增/下线管理员权限，改配置即可，不改代码

---

## 场景五：审批工作流（工单/合同/采购）

### 业务背景
一个采购单的审批流程：提交 → 部门经理审批 →(通过)→ 财务审批 →(通过)→ CEO审批 →(通过)→ 采购执行。中间任意节点可拒绝，拒绝后打回提交人。

### 不用 COLA 的问题

```java
public void handleApprove(Long workflowId, ApproveRequest request) {
    Workflow w = workflowService.getById(workflowId);
    if (w.getCurrentStep() == 1 && request.isApproved()) {
        w.setCurrentStep(2);
    } else if (w.getCurrentStep() == 1 && !request.isApproved()) {
        w.setStatus(REJECTED);
    } else if (w.getCurrentStep() == 2 && request.isApproved()) {
        w.setCurrentStep(3);
    } else if (w.getCurrentStep() == 2 && !request.isApproved()) {
        w.setStatus(REJECTED);
    }
    workflowService.update(w);
}
```

**问题**：
- 审批节点变化需要改代码
- 无法支持条件分支（如金额>10万才需要CEO审批）
- 无法支持会签、或签等复杂逻辑

### 使用 COLA 后的效果

```java
builder.externalTransition()
    .from(WorkflowStatus.STEP_1_APPROVING)
    .to(WorkflowStatus.STEP_2_APPROVING)
    .on(WorkflowEvent.APPROVE)
    .when(checkAmountBelow(100000))
    .perform(doNotifyNextApprover());

builder.externalTransition()
    .from(WorkflowStatus.STEP_1_APPROVING)
    .to(WorkflowStatus.STEP_2_APPROVING)
    .on(WorkflowEvent.APPROVE)
    .when(checkAmountAbove(100000))
    .perform(doNotifyCEO());

builder.externalTransition()
    .fromAny(WorkflowStatus.class)
    .to(WorkflowStatus.REJECTED)
    .on(WorkflowEvent.REJECT)
    .perform(doNotifySubmitter())
    .perform(doClearPendingApprovals());
```

**必要性证明**：
- **条件分支内嵌**：不同金额走不同审批路径，配置层面即可控制
- **拒绝统一处理**：`fromAny + REJECT` 一次定义，所有审批节点拒绝统一打回
- **节点增删改**：调整流程只需修改流转配置，不改 handler 代码
