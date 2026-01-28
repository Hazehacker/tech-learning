# 事务管理

> **如果某一个service层的方法上有<u>不止一次</u>数据库操作，就进行事务控制**
> 避免数据不完整，不一致的情况



> 问题场景：
>
> ![image-20250912151500566](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912151500566.png)
>
> 比如因为网络原因导致只有员工的基本信息保存成功、而工作经历保存失败了



![image-20250912151641705](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912151641705.png)

![image-20250912152551553](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912152551553.png)









# spring事务管理

* spring已经对事务管理的功能进行了封装

* 

![image-20250912153228966](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912153228966.png)





![image-20250912153344556](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912153344556.png)

* 类上面：类里面的全部方法都开启事务管理
* 接口上面：这个接口的所有实现都开启事务管理

> 将spring事务管理的底层日志设成debug、获取更多信息（整体日志级别一帮设置成Info）

![image-20250912155435048](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912155435048.png)









```
@Transactional
```

pom.xml：

```xml

logging:
	level:
		org.springframework.jdbc.support.jdbcTransactionManager:debug

```

> #### 控制台插件[Grep Console]
>
> （用于搜索控制台中的信息）
> 快速过滤出想要的日志
>
> 

### 进阶

**rollbackFor**

![image-20250912161106567](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912161106567.png)

> transactional注解默认出现运行时异常(RuntimeException)才会回滚

> RuntimeException包括
> 	ArithmeticException



* 用rollbackFor声明出现哪些异常要回滚

  > ```
  > @Transactional(rollback = {Exception.class})//全部异常都回滚
  > ```
  >
  > 



**propagation属性**



* 控制事务的传播行为

![image-20250912161406290](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912161406290.png)



> 主要是前两个



























# springAOP



**是什么**

**面向特定方法编程**， 

![image-20250912144429548](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912144429548.png)

* AOP是一种思想，在Spring框架中对这种思想进行的实现就叫做SpringAOP

> 比如：记录原始代码原始耗时
> ![image-20250912143858560](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912143858560.png)
>
> 原始方式需要修改几百个业务方法
> 借助AOP简化操作
>
>
> ![image-20250912144349635](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912144349635.png)

* @Around注解用于指定当前AOP程序是针对哪些方法的  

  “

  ```
  @Around("execution(* com.itheima.service.*.*(..))")
  ```

  ”——针对业务层的所有方法



![image-20250916091556540](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916091556540.png)

> 事务管理的底层就是基于AOP实现的，只不过直接封装成了一个注解



> #### 涉及的核心概念
>
> ![image-20250916092402483](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916092402483.png)
>
> > 切入点：实际被AOP容器控制的方法（切入点一定是连接点，连接点不一定是切入点）
>
> > 通知+切入点=切面
>
> > 应用于切入点、切入点这个类产生的对象就是目标对象
>
> **执行流程**
>
> （**底层基于动态代理技术**）为目标对象生成一个代理对象，代理对象中的方法的逻辑就是
> 我们在AOP中设置过的逻辑（比如先记录初始时间、<u>执行逻辑</u>、记录结束时间、打印总时间）
>
> 接着将代理对象交给Spring的IOC容器管理（controller层进行依赖注入的时候，注入的其实是这个代理对象；使用这个对象调用A()方法，调用也是代理对象中的A()方法）
>
> ![image-20250916095125189](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916095125189.png)
>
> 
> 





#### 【实战】

1. 引入pom依赖

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-aop</artifactId>
   </dependency>
   ```

   //如果当前项目是基于springboot官方骨架的项目，那么parent中就统一管理了依赖的版本，就不用制定版本号

   > **推荐使用 `spring-boot-starter-aop`**，而不是直接引入原生 AspectJ
   >
   > ```
   > <dependency>
   >             <groupId>org.aspectj</groupId>
   >             <artifactId>aspectjrt</artifactId>
   >         </dependency>
   >         <dependency>
   >             <groupId>org.aspectj</groupId>
   >             <artifactId>aspectjweaver</artifactId>
   >         </dependency>
   > ```
   >
   > 

2. 编写AOP程序，针对特定方法，根据业务需要进行编程

   ![image-20250916090340659](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916090340659.png)


   **【实战：记录方法的运行时间】**

   ```java
   @Aspect
   @Component
   public class RecordTimeAspect{
       
       @Around("execution(* com.itheima.service.impl.*.*(..))")
       public Object recordTime(ProceedingJoinPoint pjp) throws Throwable{
           long beginTime = System.currentTimeMillis();
           
           //调用原始方法
           Object result = pjp.proceed(
           
           
           long endTime = System.currentTimeMillis();
           log.info("方法 {} 执行耗时：{}ms",pjp.getSignature(),endTime-beginTime);
           
           return result;
       }
       
       
       
       
   }
   ```

   **【实战：将增删改相关接口的操作日志记录到数据库表中】**
   ![image-20250916133159070](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916133159070.png)

   > 提前建表
   >
   > 
   >
   > ![image-20250916140255674](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916140255674.png)

   OperationLogAspect.java（AOP层）;然后给那些增删改方法加上 @Log 注解

   ```java
   @Slf4j
   @Aspect
   @Commponent
   public class OperationLogAspect{
       @Autowired
       private OperateLogMapper operateLogMapper;
   
       @Around("@annotation(com.itheima.anno.log)")
   	public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable{
   		
   		Object result = joinPoint.proceed();
   		
   		long endTime = System.currentTimeMillis();
   		long costTime = endTime-startTime;
   		
   		//构建日志实体
   		OperateLog olog = new OperateLog();
   		olog.setOperateEmpId(getCurrentUserId);
   		olog.setOperateTime(LocalDateTime.now());
   		olog.setClassName(joinPoint.getTarget().getClass().getName());
   		olog.setMethodName(joinPoint.getSignature.getName);
   		olog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
   		olog.setReturnValue(result != null ? result.toString() : "void");
   		olog.setCostTime(costTime);
   		log.info("记录操作日志: {}",log);
   		
   		
   		return result;
   		
   	}
       
       
       
       
       
   }
   ```

   > ![image-20250916131650499](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916131650499.png)
   >
   > 
   > 

> #### AOP进阶
>
> ##### 通知类型
>
> ![image-20250916100207112](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916100207112.png)
> **@Around——重点**
>
> ```java
> @Slf4j
> @Aspect
> @Component
> public class MyAspect{
> 	@Before("execution(* com.itheima.service.impl.*.*(..))")
> 	public void before(){
> 		log.info("before...")
> 	}
>     
>     @Around("execution(* com.itheima.service.impl.*.*(..))")
>     public Object around(ProceedingJoinPoint pjp){
>         log.info("1");
>         Object res = pjp.proceed();
>         log.info("2");
>         return res;
>     }
>     
>     @After("execution(* com.itheima.service.impl.*.*(..))")
> 	public void after(){
> 		log.info("after...")
> 	}
>     
>     //返回后通知-目标方法运行之后运行，如果出现异常不会运行
>     @AfterReturning("execution(* com.itheima.service.impl.*.*(..))")
> 	public void afterreturning(){
> 		log.info("afterreturning...")
> 	}
>     
>     
>     //返回后通知-目标方法运行之后运行，只有出现异常才会运行
>     @AfterThrowing("execution(* com.itheima.service.impl.*.*(..))")
> 	public void afterthrowing(){
> 		log.info("afterthrowing...")
> 	}
>     
>     
> }
> 
> 
> 
> ```
>
> > ![image-20250916102238454](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916102238454.png)
> >
> > 
>
> ##### 通知顺序
>
> 受类名字母排序影响，或者人为加上Order注解
>
> ![image-20250916103022378](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916103022378.png)
>
> 
>
> 
>
> 
>
> 
>
> #### 切入点表达式
>
> ![image-20250916104913072](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916104913072.png)
>
> ```
> @Around("execution(* com.itheima.service.impl.*.*(*))")
> ```
>
> ![image-20250916112913925](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916112913925.png)
>
> * ```
>   "execution(* 包名.类名.*.(*))"
>   "execution(* 包名.*.*.(*))"
>   ```
>
>   可以使用逻辑运算符连接多个execution
>   如果有多个形参，括号里面用的是".."，如果只有一个，可以用"*"
>
> ![image-20250916113648038](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916113648038.png)
>
> 
>
> ![image-20250916115756696](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916115756696.png)
>
> 
>
> 
>
> 1. 设置一个注解类
>    ![image-20250916114800673](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916114800673.png)
>
>    ```java
>    @Target(ElementType.METHOD)//Target注解用来指定这个注解在哪生效（元注解）;ElementType.METHOD表示作用在方法上
>    @Retention(RetentionPolicy.RUNTIME)//Target注解用来指定这个注解什么时候生效（元注解）；RetentionPolicy.RUNT、IME表示运行的时候生效
>    public @interface LogOperation{
>    }
>    //这个注解中不需要定义其他东西，它起到一个标识的作用
>    
>    
>    
>    ```
>
> 2. 通知方法上的execution的位置换成@annotation注解
>
>    ```
>    @Around("@annotation(com.itheima.anno.LogOperation)")//()里面填那个自定义的注解的全类名
>    public void RecordTimeAspect(){
>    	//省略
>    }
>    ```
>
>    ![image-20250916115440038](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916115440038.png)
>
> 3. 在需要匹配的方法上面加上自定义的注解
>
>    ```
>    public class DeptServiceImpl implements DeptService{
>    	@Autowired
>    	private DeptMapper deptMapper;
>    	
>    	public List<Dept> list(){
>    		List<Dept> deptList = deptMapper.list();
>    		return deptList;
>    	}
>    
>    }
>    
>    
>    ```
>
>    
>
> ##### 【优先使用execution，如果execution不方便，考虑使用annotation】
>
> 
>
> #### 连接点
>
> 可以用于获得方法执行时的相关信息
>
> ![image-20250916125626610](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916125626610.png)
>
> ![image-20250916130149012](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916130149012.png)
>
> 
>
>  
>
> 







# SpringBoot原理



#### 配置优先级

> tomcat默认端口号8080

![image-20250916144437360](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916144437360.png)

优先级：.properties>.yml>.yaml

> ![image-20250916144611140](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916144611140.png)

![image-20250916150702496](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916150702496.png)

![image-20250916151459734](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916151459734.png)





#### Bean管理

> Bean是被Spring IoC容器管理的Java对象，它遵循特定的约定，如无参构造方法、可序列化、有标准的getter和setter方法。Bean通过XML配置文件或注解（如@Component、@Service等）定义，由IoC容器负责创建、初始化和销毁，支持依赖注入（DI），实现控制反转（IoC），使对象之间的依赖关系由容器管理，而非程序员直接控制，从而实现代码的松耦合和模块化设计。

##### bean作用域

![image-20250916152139346](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916152139346.png)
![image-20250916205110973](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916205110973.png)

> 声明的bean默认就是单例；
> ![image-20250916204309575](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916204309575.png)
>
> ![image-20250916152630584](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916152630584.png)
>
> 每次拿到的DeptController对象(bean)都是一个，输出1000次都是一样的
>
> ![image-20250916204916137](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250916204916137.png)
>
> > 使用注解"@Scope()"可以指定其他作用域
> >  

单例：项目启动时实例化，然后之后就会从IOC容器中获取这个对象



![image-20250917082050458](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917082050458.png)

* 如果一个bean是无状态的（不保存数据），我们可以默认单例
  即便有多个线程访问这个bean，也不会有共享数据的情况、不会有线程安全问题
  * 优点：节约资源、效率高（能减少创建和销毁的次数）
* 如果一个bean是有状态的（不保存数据），我们设置成多例( prototype )



![image-20250917082411900](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917082411900.png)

> @Lazy能延迟到第一次使用的时候再初始化



##### 如何将第三方bean交给IOC容器管理

 

启动类定义一个方法并加上bean注解，启动时就会自动调用这个方法并把返回值交给IOC容器管理，声明为bean对象

* 把这个方法的返回值指定为我们要声明的bean的类型
* 在方法中构造这个对象，并返回即可

![image-20250917084011449](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917084011449.png)



-![image-20250917091803353](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917091803353.png)

![image-20250917091851650](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917091851650.png)

![image-20250917091922119](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917091922119.png)



















#### SpringBoot原理





![image-20250917092139630](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917092139630.png)

![image-20250917092156189](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917092156189.png)

##### 起步依赖

![image-20250917092350277](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917092350277.png)

![image-20250917092416327](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917092416327.png)







##### 自动配置【宜重听】





![image-20250917093533332](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917093533332.png)

* 省去了繁琐的配置第三方bean的操作，简化了开发

> ```java
> class SpringbootWebTest{
>     @Autowired
>     private Gson gson;
>     @Test
>     public void testJson(){
>         System.out.prinln(gson.toJson(Result.success("HEllo Gson")));
>     }
> }
> 
> 
> ```
>
> toJson可以将一个对象直接转化成json格式的数据
> Gson的依赖在引入阿里云Oss时顺带引入了,
> springboot为我们自动配置了Gson的对象，所以可以直接注入并使用







* 如果想用第三方工具包
  ![image-20250917100728721](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917100728721.png)
  * pom文件引入第三方依赖
  * 启动类中用@ComponentScan手动指定扫描的范围

* ![image-20250917101943810](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917101943810.png)

  * 通过@import注解直接导入配置类

    ![image-20250917102333082](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917102333082.png)

  * 更优方案：启动类上加第三方自定义的注解

**总结**

![image-20250917102524997](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917102524997.png)







* **源码跟踪【宜重听】**

利用源码根据看看springboot底层采用什么方式实现自动配置

![image-20250917103523123](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917103523123.png)

> SpringBootApplication注解
> 具备扫描功能，但是默认扫描的是自动类所在的包及其子包

* @EnableAutoConfiguration底层封装了一个Import注解

![image-20250917103715013](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917103715013.png)

> 源码跟踪
>
>
> ![image-20250917103807919](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917103807919.png)
>
> ![image-20250917105758104](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917105758104.png)
>
> > * 这个重写的方法设置了要自动配置哪些类、哪些类要排除出去
>
> ![image-20250917103824633](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917103824633.png)





> 怎么看源码
>
> ![image-20250917110247954](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917110247954.png)
>
> 



![image-20250917111455614](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917111455614.png)

![image-20250917111428135](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917111428135.png)



> AutoConfigurationImportSelector实现了String[] selectImports()方法
> 这个方法返回值封装了 需要通过import注解批量导入的类的类名集合（这个方法加载一个配置源文件“META-INF/spring.factories(2.7.0版本之前是在这个文件)”(这份文件中定义了需要配置的所有类的类名)）
>
> 这些类都会通过Import注解直接导入到IOC容器中，声明为IOC容器中的bean对象
>
> > ![image-20250917112600167](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917112600167.png)
> >
> > ![image-20250917112407264](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917112407264.png)
> >
> > 这个注解会自动判断当前环境，满足一定条件就会把需要的对象注册到IOC容器
>
> 所以IOC容器就有了这些bean，在项目中就可以直接注入，直接使用





* **ConditionalXXX注解**

![ ](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917113011285.png)

>    ![image-20250917113747116](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917113747116.png)

![image-20250917114133451](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917114133451.png)



**自定义starter-【后面用到了再仔细听】**



> 是什么
>
> ![image-20250917121203885](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917121203885.png)
>
> 包含：起步依赖、自动配置
>
> ![image-20250917121243388](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917121243388.png)
>
> 自动配置功能在这里实现





![image-20250917121906650](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917121906650.png)

> spring-boot在前面：springboot官方提供
> 其他在前：其他技术提供
>
> 引入依赖只用引入starter，其他依赖会传递进来







> ![image-20250917123030272](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917123030272.png)
>
> 和springboot项目保持一致
> 







![image-20250918083859741](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918083859741.png)



> springMVC是spring框架的一部分，spring框架中对于web开发的支持部分叫做MVC
>
> ![image-20250918122601452](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918122601452.png)
>
> 现在企业进行SSM开发都是基于SpringBoot的底座进行构建的





## 其他







#### @Autowired和@Resource注解的区别





> * 都能实现依赖注入
>
> `@Autowired`和`@Resource`都是用于依赖注入的注解，但存在一些区别：
>
> 1. 来源不同：`@Autowired`是Spring框架的注解，而`@Resource`是Java标准（JSR-250规范）的注解。
>
> 2. 注入方式：`@Autowired`默认按类型注入，需要配合`@Qualifier`注解按名称注入；`@Resource`默认按名称注入，名称找不到则按类型注入。
>
> 3. 使用范围：`@Autowired`只能在Spring环境下使用；`@Resource`在支持JSR-250规范的Java容器（如Tomcat）中都能使用，Spring从3.0版本开始也支持`@Resource`。
>
> 4. 功能灵活性：`@Autowired`提供了`required`属性，可指定依赖是否必须；`@Resource`可通过`name`属性指定Bean名称。
>
> 5. 实现原理：`@Autowired`基于Spring的IoC容器实现；`@Resource`基于Java反射机制实现。
>
> 在实际开发中，可根据项目需求和团队规范选择使用。如果项目已引入Spring，通常推荐使用`@Autowired`，因为它与Spring的其他功能更紧密集成，提供了更多灵活性。





#### @Controller和@RestController的区别

> 好的，`@Controller` 和 `@RestController` 是 Spring Framework（尤其是 Spring Boot）中用于创建 Web 控制器的两个核心注解。它们的主要区别在于**如何处理返回值**以及**是否默认开启 REST 风格**。
>
> ---
>
> ### 🧩 核心一句话总结：
>
> > - `@Controller`：用于传统的 MVC 控制器，**返回视图名**（如 JSP、Thymeleaf 模板），需要配合 `@ResponseBody` 才能返回 JSON 数据。
> > - `@RestController`：是 `@Controller` 和 `@ResponseBody` 的**组合注解**，专为构建 RESTful API 设计，**所有方法默认返回数据**（如 JSON/XML），而不是视图。
>
> ---
>
> ### 🔍 详细对比
>
> | 特性               | `@Controller`                    | `@RestController`                            |
> | ------------------ | -------------------------------- | -------------------------------------------- |
> | **本质**           | 普通控制器                       | `@Controller + @ResponseBody`                |
> | **返回值用途**     | 返回视图名称（如 `"user/list"`） | 直接返回数据（如 `User` 对象、JSON）         |
> | **是否自动序列化** | ❌ 不会，需手动加 `@ResponseBody` | ✅ 所有方法默认序列化为 JSON/XML              |
> | **典型用途**       | 页面跳转、服务端渲染（HTML）     | 构建前后端分离的 REST API                    |
> | **能否返回视图？** | ✅ 可以                           | ❌ 不能（除非使用 `ModelAndView` 等特殊方式） |
>
> ---
>
> ### 💡 举个例子说明区别
>
> #### 场景1：使用 `@Controller`（传统 MVC）
>
> ```java
> @Controller
> public class UserController {
> 
>     // 返回一个视图名称，Spring 会去模板目录找 user/list.html
>     @GetMapping("/users")
>     public String listUsers(Model model) {
>         model.addAttribute("users", userService.getAllUsers());
>         return "user/list"; // 返回视图名
>     }
> 
>     // 如果想返回 JSON，必须加上 @ResponseBody
>     @GetMapping("/api/users")
>     @ResponseBody
>     public List<User> getUsers() {
>         return userService.getAllUsers(); // 返回 JSON 数据
>     }
> }
> ```
>
> > ⚠️ 注意：第二个方法必须加 `@ResponseBody`，否则 Spring 会把 `List<User>` 当作视图名去查找，导致 404 错误！
>
> ---
>
> #### 场景2：使用 `@RestController`（REST API）
>
> ```java
> @RestController
> public class ApiUserController {
> 
>     // 默认就是 @ResponseBody，直接返回 JSON
>     @GetMapping("/api/users")
>     public List<User> getUsers() {
>         return userService.getAllUsers(); // 自动序列化为 JSON
>     }
> 
>     // 所有方法都返回数据
>     @PostMapping("/api/users")
>     public ResponseEntity<String> createUser(@RequestBody User user) {
>         userService.save(user);
>         return ResponseEntity.ok("User created");
>     }
> }
> ```
>
> > ✅ 不需要加 `@ResponseBody`，所有方法的返回值都会被转换为 JSON 或 XML 并写入响应体。
>
> ---
>
> ### 🔄 等价关系
>
> ```java
> @RestController
> public class MyController { ... }
> ```
>
> 等价于：
>
> ```java
> @Controller
> @ResponseBody
> public class MyController { ... }
> ```
>
> 也就是说，`@RestController` = `@Controller` + 类级别 `@ResponseBody`
>
> ---
>
> ### 📌 使用建议
>
> | 项目类型                               | 推荐使用                                   |
> | -------------------------------------- | ------------------------------------------ |
> | 前后端不分离（服务端渲染 HTML）        | `@Controller`                              |
> | 前后端分离（前端用 Vue/React/Angular） | `@RestController`                          |
> | 同一个控制器中既有页面又有 API         | 混用或只用 `@Controller` + `@ResponseBody` |
>
> ---
>
> ### ⚠️ 常见误区
>
> - ❌ 认为 `@RestController` 不能返回字符串：它可以，但字符串会被当作数据返回，而不是视图名。
> - ❌ 在 `@RestController` 中试图返回 `"redirect:/xxx"`：虽然可以，但更推荐用 `ResponseEntity` 或 `RedirectView`。
> - ✅ 如果你在 `@RestController` 中某个方法想返回视图，可以使用 `ModelAndView`，但这违背了 REST 设计原则，不推荐。
>
> ---
>
> ### ✅ 总结
>
> | 你想做什么                     | 用哪个？                                           |
> | ------------------------------ | -------------------------------------------------- |
> | 写一个返回 JSON 的 API 接口    | ✅ `@RestController`                                |
> | 写一个跳转到 HTML 页面的方法   | ✅ `@Controller`                                    |
> | 整个项目都是 REST API          | 全部用 `@RestController`                           |
> | 混合模式（部分页面、部分 API） | 可以混用，或统一用 `@Controller` + `@ResponseBody` |
>
> > 💡 简单记：  
> > - 要返回 **页面** → 用 `@Controller`  
> > - 要返回 **数据**（JSON）→ 用 `@RestController`
>
> 





















