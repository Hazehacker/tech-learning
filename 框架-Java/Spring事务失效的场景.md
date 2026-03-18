https://blog.csdn.net/qq_66345100/article/details/129322325

---

#### 前置知识

* **事务**：指访问并可能更新数据库中各种数据项的一个程序执行单元(unit)。可以理解事务就是一段代码块或者一行SQL，这段代码或这行SQL会更新数据库，事务具有基本的ACID特性，以此保障数据的安全性。
* **事务管理**：由**事务管理器、恢复管理器、锁管理器、死锁管理器、缓存管理器**构成
  * 事务管理器：负责产生事务并为其分配事务标识 [↩︎](https://blog.csdn.net/qq_66345100/article/details/129322325#fnref1)
  * 恢复管理器：子事务提交时，负责将子事务的日志链接到父事务的日志上，确保事务的一致性原则 [↩︎](https://blog.csdn.net/qq_66345100/article/details/129322325#fnref2)
  * 锁管理器：事务申请锁时，负责判断锁的相容性 [↩︎](https://blog.csdn.net/qq_66345100/article/details/129322325#fnref3)
  * 死锁管理器：负责检测死锁 [↩︎](https://blog.csdn.net/qq_66345100/article/details/129322325#fnref4)
  * 缓存管理器：提供对事务标识的缓存 [↩︎](https://blog.csdn.net/qq_66345100/article/details/129322325#fnref5)
* **事务管理的作用**：管理事务相关的资源；更容易处理复杂的事务；简化事务相关的操作，让程序员更关注业务
* **Spring中提供了两种事务管理机制**：
  * **编程式事务**：是指在代码中手动的管理事务的提交、回滚等操作，代码侵入性比较强。（Spring提供了TransactionTemplate模板，利用该模板我们可以通过编程的方式实现事务管理，而无需关注资源获取、复用、释放、事务同步及异常处理等操作。相对于声明式事务来说，这种方式相对麻烦一些，但是好在更为灵活，我们可以将事务管理的范围控制的更为精确）
  * **声明式事务**：基于AOP面向切面的，它将具体业务与事务处理部分解耦，代码侵入性很低。而声明式事务有两种方式实现，方式一是基于@Transaction注解实现，方式二是基于XML实现。（Spring事务管理的亮点在于声明式事务管理，它允许我们通过声明的方式，在IoC配置中指定事务的边界和事务属性，Spring会自动在指定的事务边界上应用事务属性。相对于编程式事务来说，这种方式十分的方便，只需要在需要做事务管理的方法上，增加@Transactional注解，以声明事务特征即可）

> 大多数 Spring 框架的用户选择声明式事务管理，因为它对应用代码的影响最小， 因此更符合一个无侵入的轻量级容器的思想。声明式事务管理要优于编程式事务 管理，虽然比编程式事务管理（这种方式允许你通过代码控制事务）少了一点灵 活性。

* **@Transactional**：可以作用在接口、类、方法

  * **作用于接口**：不推荐这种使用方法，因为一旦标注在Interface上并且配置了Spring AOP 使用CGLib动态代理，将会导致@Transactional注解失效
  * **作用于类**：当把@Transactional 注解放在类上时，代表这个类所有公共（public）非静态（static）的方法都将启用事务功能，且都会被 Spring 的事务管理器进行管理
  * **作用于方法**：当把@Transactional配置在方法上，该方法被当成一个独立的事务，且被事务管理器管理。当类配置了@Transactional，方法也配置了@Transactional，此时方法的事务会覆盖类的事务配置信息

* **@Transactional的属性**

  ![img](https://i-blog.csdnimg.cn/blog_migrate/77d681da53e192d6cc5b9d982eada6be.png)

  * **`propagation`属性**

    `propagation` 代表事务的传播行为，默认值为 `Propagation.REQUIRED`，其他的属性信息如下：
    ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6b8e87c95acce586927c2ef08835c7dc.png)

    * `Propagation.REQUIRED`：如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。 也就是说如果A方法和B方法都添加了注解，在默认传播模式下，A方法内部调用B方法，会把两个方法的事务合并为一个事务
    * `Propagation.SUPPORTS`：如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行
    * `Propagation.MANDATORY`：如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常
    * `Propagation.REQUIRES_NEW`：重新创建一个新的事务，如果当前存在事务，暂停当前的事务。( 当类A中的 a 方法用默认Propagation.REQUIRED模式，类B中的 b方法加上采用 Propagation.REQUIRES_NEW模式，然后在 a 方法中调用 b方法操作数据库，然而 a方法抛出异常后，b方法并没有进行回滚，因为Propagation.REQUIRES_NEW会暂停 a方法的事务 )
    * `Propagation.NOT_SUPPORTED`：以非事务的方式运行，如果当前存在事务，暂停当前的事务
    * `Propagation.NEVER`：以非事务的方式运行，如果当前存在事务，则抛出异常
    * `Propagation.NESTED` ：和 Propagation.REQUIRED 效果一样

  * **`isolation`属性**

    事务的隔离级别，默认值为 Isolation.DEFAULT

    * `Isolation.DEFAULT`：使用底层数据库默认的隔离级别。
    * `Isolation.READ_UNCOMMITTED`：读取未提交数据(会出现脏读, 不可重复读) 基本不使用
    * `Isolation.READ_COMMITTED`：读取已提交数据(会出现不可重复读和幻读)
    * `Isolation.REPEATABLE_READ`：可重复读(会出现幻读)
    * `Isolation.SERIALIZABLE`：串行化

  * **`timeout`属性**：事务的超时时间，默认值为 -1。如果超过该时间限制但事务还没有完成，则自动回滚事务

  * **`readOnly`属性**：指定事务是否为只读事务，默认值为 false；为了忽略那些不需要事务的方法，比如读取数据，可以设置 readonly 为 true

  * **`rollbackFor`属性**：用于指定能够触发事务回滚的异常类型，可以指定多个异常类型

  * **`noRollbackFor`属性**：抛出指定的异常类型，不回滚事务，也可以指定多个异常类型



---


在 Spring 框架中，`@Transactional` 注解是声明式事务管理的核心，但在实际开发中，由于对 AOP（面向切面编程）和代理机制理解不足，经常会出现注解“失效”的情况。

以下是导致 `@Transactional` 事务失效的**所有常见情况及解决方案**，按发生频率和原理分类整理：

### 一、代理机制导致的失效（最常见）

Spring 的事务管理基于 **AOP 动态代理**。只有当方法通过**代理对象**被调用时，事务拦截器才能工作。

#### 1. 同类内部方法调用（自调用）

- **现象**：在同一个类中，<u>一个非事务方法 `A()`</u> 调用了<u>另一个带有 `@Transactional` 的方法</u> `B()`。

- **原因**：`A()` 调用 `B()` 时，使用的是 `this.B()`，即直接调用目标对象本身的方法，**绕过了代理对象**。因此，事务切面无法拦截到 `B()` 的执行。

- **解决方案**：
    - **推荐**：将带事务的方法移到另一个 Service 类中，通过注入该 Service 来调用。
    
    - **替代**：在类内部注入自身（`@Autowired private SelfService self;`），然后使用 `self.methodB()` 调用（需注意循环依赖问题）。
    
    - **高级**：使用 `AopContext.currentProxy()` 获取当前代理对象进行调用（需开启 `exposeProxy=true`）。
    
      > ①引入AOP依赖，动态代理是AOP的常见实现之一
      >
      > ```xml
      > <!--aspectj-->
      > <dependency>
      >     <groupId>org.aspectj</groupId>
      >     <artifactId>aspectjweaver</artifactId>
      > </dependency>
      > ```
      >
      > ②暴露动态代理对象，默认是关闭的 (启动类上面加注解)
      >
      > ```java
      > @EnableAspectJAutoProxy(exposeProxy = true)
      > ```
      >
      > ③使用代理对象
      >
      > ```java
      > // 示例：
      > UserServiceImpl proxy = (UserServiceImpl) AopContext.currentProxy();
      > proxy.method2(args);// method 带有 @Transactional注解
      > ```
      >
      > 
    
      

#### 2. 目标类未被 Spring 容器管理

- **现象**：在一个普通 Java 类（非 Bean）上添加了 `@Transactional`，或者该类虽然加了 `@Service`/`@Component` 但包扫描路径未覆盖。
- **原因**：<u>Spring 只能为容器管理的 Bean 创建代理</u>。如果对象是手动 `new` 出来的，Spring 完全不知道它的存在，自然无法代理。
- **解决方案**：确保类上有 `@Service`, `@Component`, `@Controller` 等注解，且位于组件扫描范围内，并通过 `@Autowired` 注入使用，严禁 `new`。

#### 3. 方法访问修饰符不是 `public`

- **现象**：`@Transactional` 加在了 `protected`, `private` 或默认（package-private）方法上。
- **原因**：<u>Spring 的默认代理机制（无论是 JDK 动态代理还是 CGLIB）通常只拦截 `public` 方法。非 public 方法上的注解会被忽略</u>
- **解决方案**：将方法修饰符改为 `public`

---

### 二、异常处理导致的失效

事务回滚的前提是捕获到了特定的异常。

#### 4. 异常被 `try-catch` 吞掉

- **现象**：在事务方法内部使用了 `try-catch` 捕获了异常，但没有重新抛出。
- **原因**：事务拦截器只有在方法抛出异常时才会触发回滚。如果异常被“吃掉”了，拦截器认为方法成功执行，从而提交事务。
- **解决方案**：
    - 在 `catch` 块中手动抛出异常：`throw new RuntimeException(e);`
    - 或者在 `catch` 块中手动设置回滚状态：`TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();`

#### 5. 抛出的异常类型不匹配

- **现象**：默认情况下，Spring 只对 `RuntimeException` 和 `Error` 进行回滚。如果代码抛出了受检异常（Checked Exception，如 `IOException`, `SQLException`），事务不会回滚。
- **原因**：`@Transactional` 的默认 `rollbackFor` 属性仅包含运行时异常。
- **解决方案**：显式指定回滚异常类型：
  
    ```java
    @Transactional(rollbackFor = Exception.class) // 推荐对所有 Exception 回滚
    // 或者
    @Transactional(rollbackFor = {IOException.class, SQLException.class})
    ```
    

---

### 三、配置与传播行为导致的失效

#### 6. 事务传播行为设置不当

- **现象**：设置了 `propagation = Propagation.NOT_SUPPORTED` 或 `Propagation.NEVER`。
- **原因**：
    - `NOT_SUPPORTED`：以非事务方式运行，如果当前有事务则挂起。
    - `NEVER`：以非事务方式运行，如果当前有事务则抛出异常。
    - 这两种模式下，方法本身不会开启新事务，也不会加入现有事务。
- **解决方案**：根据业务需求选择正确的传播行为，通常使用默认的 `REQUIRED`。

#### 7. 多线程调用

- **现象**：在主线程开启事务，然后在子线程（新启动的 Thread 或线程池任务）中执行数据库操作。
- **原因**：**Spring 事务是基于 `ThreadLocal` 绑定数据库连接的**。<u>子线程无法继承主线程的 `ThreadLocal` 变量，因此子线程中的操作不在同一个事务上下文中</u>，甚至可能没有事务。
- **解决方案**：
    
    - 避免在事务中进行多线程操作。
    - 如果必须异步，需重新设计事务边界，让异步方法自己管理事务（在其内部加 `@Transactional`），但这会导致主从事务分离，无法保证原子性。
    - 将代理对象设为成员变量，并在主线程中初始化
    
      ```java
      // 示例：
      /**
       * 由于任务是在子线程中异步消费，子线程无法从 ThreadLocal 中获取到代理对象，所以这里要将代理对象赋值给成员变量
       */
      private VoucherOrderServiceImpl proxy;
      
      // ...
      // 主线程中初始化
      proxy = (UserServiceImpl) AopContext.currentProxy();
      // ...
      
      private BlockingQueue<VoucherOrder> voucherOrderTasks = new ArrayBlockingQueue<>(1024 * 1024);
      private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();
      
      /**
       * 使用 @PostConstruct 注解，在加载这个类的时候就启动这个方法
       */
      @PostConstruct
      public void init(){
          SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
      }
      public class VoucherOrderHandler implements Runnable {
          @Override
          public void run() {
              // 不断从队列中获取订单信息
              while (true) {
      
                  // 1. 从队列中获取订单信息
                  VoucherOrder voucherOrder = null;
                  try {
                      voucherOrder = voucherOrderTasks.take();
                  } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                  }
                  // 2. 创建订单，扣减库存
                  proxy.createVoucherOrder2(voucherOrder);
              }
          }
      }
      
      ```
    
      

#### 8. 数据库引擎不支持事务

- **现象**：代码逻辑正确，但数据库表使用的是 MyISAM 引擎（MySQL）。
- **原因**：MyISAM 不支持事务，即使 Spring 发出了提交/回滚指令，数据库层面也无法执行。
- **解决方案**：将数据库表引擎修改为支持事务的引擎（如 InnoDB）。

#### 9. 多个数据源未正确配置

- **现象**：项目配置了多数据源，但 `@Transactional` 没有指定具体数据源，或者事务管理器配置错误。
- **原因**：Spring 默认只有一个事务管理器。如果存在多个，需要明确指定使用哪个 `transactionManager`。
- **解决方案**：
  
    ```java
    @Transactional(transactionManager = "orderTransactionManager")
    ```
    

---

### 四、其他特殊情况

#### 10. 嵌套事务中的部分回滚误解

> 外层方法 A 调用了内层方法 B。
>
> - B 加了 `@Transactional(propagation = REQUIRES_NEW)` —— 意思是“我要开一个新事务，跟外面没关系”。
> - B 执行时出错了，抛了异常 → **B 的事务确实回滚了！**
> - 但 A 用 `try-catch` 把这个异常“吃掉”了，没再往上抛 → **A 的事务认为“一切正常”，于是提交了！**
>
> 结果：**B 的数据回滚了，A 的其他操作提交了。**
> → 开发者一看：“咦？B 明明失败了，怎么整体没回滚？” → 误以为“事务失效”。
>
> 其实不是失效！是**设计如此**——因为 `REQUIRES_NEW` 就是让内外事务独立

- **现象**：外层事务捕获了内层事务抛出的异常，导致外层提交，开发者误以为内层没回滚。
- **原因**：
  如果内层事务传播行为是 `REQUIRES_NEW`，它独立于外层。如果内层异常被外层 catch 住且未设 rollbackOnly，外层会提交（此时内层事务回滚， 外层事务提交，**两者互不影响**）
- **注意**：如果内层是 `REQUIRED`（默认，内外层一起成功和失败），异常抛出会导致整个大事务标记为 rollbackOnly。（即使外层用 `try-catch` 吃掉了内层抛出的异常，最后也会回滚） → **只要事务被标记为 rollbackOnly，最终一定会回滚**）

#### 11. 方法重载问题

- **现象**：类中有两个同名方法，一个有 `@Transactional`，一个没有。调用时参数匹配到了没有注解的那个。
- **原因**：简单的调用错配。
- **解决方案**：检查调用的方法签名是否正确。



### 总结自查清单

如果发现事务没生效，请按以下顺序排查：

1. **是不是自己调自己？** (同类内部调用)
2. **是不是 try-catch 把异常吃了？**
3. **是不是抛出了 Checked Exception 却没配 `rollbackFor`？**
4. **方法是不是 `public` 的？**
5. **这个类是不是 Spring Bean？** (有没有被 `new`)
6. **数据库引擎支持事务吗？** (InnoDB?)
7. **是不是在多线程里跑的？**
8. **传播行为是不是设成了 `NOT_SUPPORTED`？**

掌握以上几点，基本可以解决 99% 的 Spring 事务失效问题。