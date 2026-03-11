



#### @Async



`@Async` 是 **Spring 框架提供的注解**（位于 `org.springframework.scheduling.annotation`），用于**将方法标记为异步执行**。调用该方法时，主线程会立即返回，实际逻辑在独立线程中执行，避免阻塞主流程。

**核心作用**

- **解耦耗时操作**：如发送邮件、日志记录、调用外部 API、文件处理等，提升系统响应速度。
- **简化异步编程**：无需手动创建/管理线程，Spring 自动通过线程池调度。
- **提升吞吐量**：合理利用 CPU 和 I/O 资源，尤其适合高并发场景。

**关键使用要点**

1. **启用异步支持**
   配置类需添加 `@EnableAsync`（Spring Boot 也需显式声明）：

   ```java
   @Configuration
   @EnableAsync
   public class AsyncConfig { ... }
   ```

2. **方法要求**  

   - ✅ 支持返回 `void`、`Future`、`CompletableFuture`、`ListenableFuture`
     （返回 `Future` 可获取结果/异常；`void` 需配合异常处理器）
   - ❌ **不能是 `private`/`final`/`static`**（AOP 代理限制）
   - ❌ **同类内直接调用无效**（需通过 Spring 代理调用，如拆分类或注入自身）

3. **线程池配置（强烈推荐）**
   默认使用 `SimpleAsyncTaskExecutor`（**不复用线程**，生产环境禁用！）
   ✅ 自定义示例：

   ```java
   @Bean(name = "customExecutor")
   public Executor asyncExecutor() {
       ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
       executor.setCorePoolSize(5);
       executor.setMaxPoolSize(10);
       executor.setQueueCapacity(100);
       executor.setThreadNamePrefix("Async-");
       executor.initialize();
       return executor;
   }
   ```

   方法指定线程池：`@Async("customExecutor")`

4. **异常处理**  

   - 返回 `Future`：异常封装在 `Future.get()` 中抛出  
   - 返回 `void`：需实现 `AsyncUncaughtExceptionHandler` 捕获未处理异常



**使用示例**

```java
@Service
public class EmailService {
    @Async("customExecutor") // 指定线程池
    public CompletableFuture<Void> sendEmail(String to) {
        // 模拟耗时操作
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("邮件已发送至: " + to);
        return CompletableFuture.completedFuture(null);
    }
}

// 调用方（主线程不阻塞）
@Autowired private EmailService emailService;
public void trigger() {
    emailService.sendEmail("user@example.com"); // 立即返回
    // 继续执行其他逻辑...
}
```

```java
// 使用异步发送，避免阻塞主流程
@Async
public void asyncSendDeleteMsg(String key) {
    try {
        mqProducer.sendDeleteCacheMsg(key);
    } catch (Exception e) {
        log.error("发送缓存删除消息失败", e);
        alarmService.alert("MQ发送失败", key);
    }
}
```







常见陷阱

| 问题             | 原因                     | 解决方案                                             |
| ---------------- | ------------------------ | ---------------------------------------------------- |
| 同类内调用不异步 | AOP 代理失效             | 通过 `ApplicationContext` 获取代理对象调用，或拆分类 |
| 线程池耗尽       | 未配置合理参数           | 显式定义 `ThreadPoolTaskExecutor` 并监控队列         |
| 异常被吞掉       | 未处理 `void` 方法异常   | 实现 `AsyncUncaughtExceptionHandler`                 |
| 事务失效         | 异步方法脱离原事务上下文 | 需在异步方法内显式开启新事务（`@Transactional`）     |



优势总结

- **代码简洁**：声明式异步，专注业务逻辑  
- **灵活可控**：支持多线程池、返回值、异常定制  
- **生态集成**：与 Spring Boot、Spring MVC 无缝协作

> 💡 **提示**：在 Spring Boot 中，若仅添加 `@EnableAsync` 且无自定义 `Executor`，Boot 会自动配置一个 `ThreadPoolTaskExecutor`（基于 `application.properties` 中的 `spring.task.execution.*` 配置），但仍建议显式定义以精准控制资源。