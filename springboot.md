1.介绍

约定>配置
纯注解零配置开发方式

* spring——>springboot
  变化：在spring基础上集成了很多框架，方便调用
  提供了默认配置，简化了初始搭建和开发过程
  集成了大量常用的第三方库配置，比如Redis/MongoDB/Dubbo/kafka/Es等
  这些配置都可以开箱即用【使开发者能够更加专注于业务逻辑】
  另外通过集成大量的框架使得依赖包的版本冲突，以及引用的不稳定性等问题得到了解决



> 避免springMVC、xml等繁琐的配置

![image-20250629200310183](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250629200310183.png)

* 2——不需要单独安装tomcat服务器
* 支持各种微服务主键
* SSM->Springboot

架构发展历程——单体应用（所有模块一个工程）——单体应用水平复制出来（负载均衡）——微服务架构（拆分各个模块出来，有利于负载、合作等）—[这就出现了一些问题，需要微服务的解决方案，比如Netflix]





> 小案例
> ![image-20250629200512268](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250629200512268.png)
>
> 
>
> 
>
> 
>
> 









## 1.项目结构

* src
  * main
    * java——书写运行时的java代码
      * controller
      * service
      * xxxApplication.java文件——项目唯一入口类，用于启动项目
    * resources——书写运行时使用的配置文件
      * application.properties/application.yml文件——【核心配置文件】用来写springboot的全局配置
        比如数据库地址
  * test
    * test/java文件夹——用来书写测试时的java代码
    * test/resources——用来书写测试时使用的配置文件
* 控制层
* 逻辑层
* 数据访问层dao/mapper/reposiry



### 2.配置文件

> ```java
> serve.port = 8080;
> 
> spring.datasource.url = jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
> spring.datasource.usrname = root;
> spring.datasource.password = 1234;
> spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
> 
>     
> spring.jpa.hibernate.ddl-auto=update
> ```
>
> * springboot_xushu是数据库的名字
>   
>
> * ```java
>   spring.jpa.show-sql=true
>   ```
>
>   在控制台显示sql语句
>
> * ```java
>   spring.jpa.properties.hibernate.format_sql=true
>   ```
>
>   格式化sql语句，整洁的把sql语句打印在控制台，方便调试
>
> * 设置启动时自动更新数据库表结构
>
>   ```java
>   spring.jpa.hibernate.ddl-auto=update
>   ```
>
> * ```
>     
>   ```
>
>   
>
> 

### 3.类映射成表格

![image-20250702094843448](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702094843448.png)

* ```java
  @Id
  private Integer userId;
  ```

  设置成主键

### 4.controller类制造接口



* ```java
  @RestController
  ```

  允许接口的方法返回对象，并转换成json格式文本，方便处理客户端数据

* ```
  @RequestMapping("/user")
  ```

  定义客户端的访问路径
  (localhost:8080/user/**)

* ![image-20250702113336738](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702113336738.png)

  ```java
  @PostMapping
  @GetMapping
  ```

  rest风格注解

  自动识别前端请求类型然后匹配对应函数

* ![image-20250702115133579](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702115133579.png)

  

  
  

  可以用UserDto来接收客户端请求的参数
  调用@RequestBody注解将传进来的文本转成对象
  ![image-20250702100503181](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702100503181.png)

  ![image-20250702100450520](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702100450520.png)

* ![image-20250702103044504](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702103044504.png)
  在接口里面调用业务逻辑中的IUserService接口里面的<u>*addUser()方法*</u>，（这个方法在IUserService中定义，UserService中被具体实现)





> 面向接口，对于业务拓展性很有帮助

### 5.Service包



* ```
  @service
  ```

  这个注解会把xxService配置成spring的一个bean9
  标志成spring bean之后就能在xxController中自动装配进去
  ![image-20250702101402580](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702101402580.png)

* ![image-20250702110551135](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702110551135.png)
  [service实现了addUser方法，在里面调用xxRepository.fanfa()操作数据库]

  save方法会自动判别增加还是修改（有id就增加，没有id就修改）
  Dto类型对象不能直接传进去save（因为userRepository绑定的是User类）
  这里使用

  ```
  User userPojo = new User();
  BeanUtils.copuProperties(user,userPojo);
  ```


  拷贝成一个User对象再传进去









### 6.repository包



* ```
  
  ```

  继承CrudRepository之后就不用自己实现增删查改的方法
  第一个泛型指定“增删查改”要用来操作哪个类
  第二个泛型指定主键的类型

* 
  这里的注解换成@Service也能实现一样的功能，但是使用@Repository更有区分度、职责更明确



### 7.pom.xml

![image-20250702111338854](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702111338854.png)

* 用法
  ![image-20250702111640819](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702111640819.png)

  在需要验证参数的地方要加上“@Validated”注解
  ![image-20250702111740864](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702111740864.png)

---

![image-20250703143824136](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703143824136.png)

---

![image-20250703153607950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703153607950.png)



* 用于打包

---







![image-20250703144104704](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703144104704.png)

![image-20250703144137860](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703144137860.png)

* 官方文档这个位置里面包含了一堆starters

https://docs.spring.io/spring-boot/reference/using/build-systems.html#





![image-20250703145258102](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703145258102.png)

*   安全官方文档这个位置包含了所以可配置的项

https://docs.spring.io/spring-boot/appendix/application-properties/index.html





----

1. 开发web的场景启动器
   ![用](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703144328254.png)

   > 不用声明version，因为版本已经被管理好了
   > ![image-20250703144658914](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250703144658914.png)

2. 









### 8.exception包

![image-20250702113131177](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702113131177.png)

![image-20250702113118948](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702113118948.png)

* ![image-20250702112718081](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702112718081.png)
  用来注明给什么异常做统一处理
  Exception.class表示所有异常的父类

  

## 2.配置和自动配置原理

































