# Exception的阅读



* 。
* 从下到上，越靠上，离直接报错的位置越近
  ![image-20250911151235368](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250911151235368.png)

> #### 如果类中有这种属性值，建议直接初始化，以防报错
>
>
> ![image-20250911185035707](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250911185035707.png)





# 终端运行系列

### 1.![image-20250630113429974](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250630113429974.png)

* ai回答：

> 你遇到的错误信息表明 PowerShell 正在阻止运行 `npm.ps1` 脚本，因为当前系统的执行策略不允许运行脚本。这是 Windows 的安全特性之一，用于防止恶意脚本的执行。
>
> ### 解决方法
>
> #### 1. **临时更改执行策略**
> 你可以临时更改 PowerShell 的执行策略来允许运行脚本。打开一个新的 PowerShell 窗口（以管理员身份），然后输入以下命令：
>
> ```powershell
> Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
> ```
>
> 这将仅对当前会话更改执行策略，不会影响其他 PowerShell 会话或系统设置。
>
> #### 2. **永久更改执行策略**
> 如果你希望永久更改执行策略，可以使用以下命令：
>
> ```powershell
> Set-ExecutionPolicy -Scope LocalMachine -ExecutionPolicy RemoteSigned
> ```
>
> 或者更宽松的策略：
>
> ```powershell
> Set-ExecutionPolicy -Scope LocalMachine -ExecutionPolicy Unrestricted
> ```
>
> **注意：** 更改执行策略可能会降低系统的安全性，请谨慎操作。`RemoteSigned` 是一个较为平衡的选择，它允许本地脚本无限制地运行，但要求从互联网下载的脚本必须有可信发布者的签名。
>
> #### 3. **绕过执行策略**
> 如果你只是想一次性运行某个脚本而不更改执行策略，可以在运行命令时添加 `-ExecutionPolicy Bypass` 参数：
>
> ```powershell
> powershell -ExecutionPolicy Bypass -Command "npm install"
> ```
>
> ### 验证更改
> 更改执行策略后，再次尝试运行 `npm install` 命令，看看问题是否解决。
>
> ### 其他注意事项
> - 如果你在使用 Node.js 和 npm 进行开发，建议使用命令提示符（CMD）而不是 PowerShell，因为 CMD 不受执行策略的影响。
> - 如果你经常需要在 PowerShell 中运行脚本，考虑创建一个专门的开发环境，并为该环境设置适当的执行策略。
>
> 通过以上步骤，你应该能够解决由于执行策略导致的脚本运行问题。
>









# 方法论

##### 1. **确定/定位错误的源头**

首先，找到报错信息的最核心部分。在 Spring Boot 的错误日志中，通常最下面的 `Caused by` 部分是问题的根源。

> ![image-20250714100403916](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714100403916.png)
>
> 为了获取更详细的错误信息，可以在启动应用程序时启用 debug 模式。

##### 2. **分析错误的原因**

接下来，分析为什么会出现这个错误。错误信息中提到的 `jdbcUrl` 是

##### 3.报错信息结构

- **最上面的部分**：通常是 Spring Boot 的启动失败信息，例如：

  ```
  Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
  ```

  这部分告诉你应用程序启动失败了，并建议你启用调试模式来获取更多信息。

- **中间的堆栈跟踪**：这部分详细记录了错误发生的具体位置和调用链。例如：

  ```
  org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' ...
  ```

  这里告诉你是在创建 `entityManagerFactory` 这个 Bean 时出现了问题。

- **最下面的 `Caused by` 部分**：这是问题的根源，通常是最关键的信息。例如：

  ```
  Caused by: java.lang.RuntimeException: Driver com.mysql.cj.jdbc.Driver claims to not accept jdbcUrl, jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
  ```

  这里直接指出了问题的原因

#### 分析网络请求：排查问题的关键手段

* 在“网络”中选中请求，就能在预览中查看返回过来的对象、在“响应”中能查看json格式的返回数据

  ![image-20250902115603210](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902115603210.png)

  当你发送请求的时候携带了参数，可以在“载荷”中查看
  ![image-20250902115811299](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902115811299.png)

  > Fetch/XHR里面的都是ajax请求

  ![image-20250910131014946](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250910131014946.png)

#### 断点调试+控制台

1. 断点调试，一步步放行，找到抛出异常的地方

2. 在“线程与变量”中查看具体的异常信息，判断出错原因

   ![image-20250922143325140](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250922143325140.png)

#### 针对出现问题的地方开启详细日志

* ![image-20250923082056200](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250923082056200.png)


  ![image-20250923082116501](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250923082116501.png)



#### 排除法找到产生错误的原因

* 难以确定为什么运行不了（报错信息不全、造成错误的位置模糊）时

  > ![image-20250925145738280](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250925145738280.png)
  >
  > 比如运行到这里的时候报错
  >
  > ![image-20250925145755632](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250925145755632.png)
  >
  > ![image-20250925145804789](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250925145804789.png)
  >
  >
  > 并且报错信息也很简单，我们难以定位错误的位置和原因，不知道是sql的错误还是自定义注解的错误
  >
  > 

  * 就可以采用排除法，将通用字段填充的方式改成手动代码填充（注释掉自定义注解），看下是不是注解的原因

### try-catch获取详细的异常信息

> 有些会出现异常的地方在你编写代码的时候不会提示你加上try catch或throw，但是仍然有可能抛出异常，此时手动加上try catch帮助获取更详细的异常信息
>
> 

* 案例

  > 
  >
  > ![image-20250930151414137](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250930151414137.png)
  >
  > ![image-20250930151928374](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250930151928374.png)
  > 

* 











---

---















### 1.数据库连接失败

多个bean相互循环依赖，会报错

> 好的，接下来我将逐步引导你如何分析这段报错信息，以便你能够独立看懂类似的错误。
>
> ### 1. **确定错误的源头**
> 首先，找到报错信息的最核心部分。在 Spring Boot 的错误日志中，通常最下面的 `Caused by` 部分是问题的根源。
>
> 在这段日志中，最核心的错误是：
> ```
> Caused by: java.lang.RuntimeException: Driver com.mysql.cj.jdbc.Driver claims to not accept jdbcUrl, jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
> ```
>
> 这表明问题出在数据库连接的配置上，具体是 `Driver com.mysql.cj.jdbc.Driver` 不接受提供的 `jdbcUrl`。
>
> ### 2. **分析错误的原因**
> 接下来，分析为什么会出现这个错误。错误信息中提到的 `jdbcUrl` 是：
> ```
> jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
> ```
>
> 这里有两个明显的问题：
> - **多余的冒号**：`jdbc::mysql://` 中多了一个冒号，正确的应该是 `jdbc:mysql://`。
> - **参数格式问题**：`serverTimezone = UTC` 中间有空格，正确的应该是 `serverTimezone=UTC`。
>
> ### 3. **定位配置文件中的问题**
> 根据错误信息，我们知道问题出在数据库连接的配置上。通常，这些配置位于 `application.properties` 或 `application.yml` 文件中。检查你的配置文件，找到相关的配置项：
> ```properties
> spring.datasource.url = jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
> ```
>
> ### 4. **修正配置**
> 根据分析，修正配置文件中的错误：
> ```properties
> spring.datasource.url=jdbc:mysql://localhost:3306/springboot_xushu?serverTimezone=UTC
> ```
>
> ### 5. **理解错误日志的结构**
> 为了能够独立看懂类似的错误，你需要理解错误日志的结构：
> - **最上面的部分**：通常是 Spring Boot 的启动失败信息，例如：
>   ```
>   Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
>   ```
>   这部分告诉你应用程序启动失败了，并建议你启用调试模式来获取更多信息。
>
> - **中间的堆栈跟踪**：这部分详细记录了错误发生的具体位置和调用链。例如：
>   ```
>   org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' ...
>   ```
>   这里告诉你是在创建 `entityManagerFactory` 这个 Bean 时出现了问题。
>
> - **最下面的 `Caused by` 部分**：这是问题的根源，通常是最关键的信息。例如：
>   
>   ```
>   Caused by: java.lang.RuntimeException: Driver com.mysql.cj.jdbc.Driver claims to not accept jdbcUrl, jdbc::mysql://localhost:3306/springboot_xushu?serverTimezone = UTC
>   ```
>   这里直接指出了问题的原因。
>
> ### 6. **逐步排查问题**
> - **检查配置文件**：确保所有配置项都是正确的。
> - **检查依赖**：确保项目中包含了正确的数据库驱动依赖。例如，如果你使用的是 MySQL，应该有类似以下的依赖：
>   ```xml
>   <dependency>
>       <groupId>mysql</groupId>
>       <artifactId>mysql-connector-java</artifactId>
>       <scope>runtime</scope>
>   </dependency>
>   ```
> - **检查数据库服务**：确保数据库服务正在运行，并且可以通过配置的 URL 访问。
>
> ### 7. **总结**
> 通过以上步骤，你可以逐步分析和解决错误。关键是要找到错误的根源（`Caused by` 部分），然后根据错误信息逐步排查问题。
>
> 希望这些步骤能帮助你独立看懂类似的错误日志。如果还有其他问题，欢迎继续提问！



* **参数不要写错**
  **如：username不要写成usname**
* ![image-20250702092321218](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250702092321218.png)
  **特定格式中的等号两边不要有空格，会报错**
* **application.properties里面的等号两边不要有空格**
* **application.yml的冒号后面要有空格**
  ![image-20250714194057653](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250714194057653.png)



### 2.访问失败

![image-20250715143121477](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250715143121477.png)

> 从你提供的错误截图来看，这是 Spring Boot 应用程序出现 Whitelabel 错误页面的情况，以下是一些可能的解决方法：
>
> ### 1\. 检查配置文件
>
>   * **查看 application.properties 或 application.yml 文件** ：确保其中的配置正确无误。例如，数据源配置（如数据库 URL、用户名、密码等）、服务器端口等配置是否正确。
>   * **检查是否有未配置的属性** ：某些情况下，如果应用中使用了某些功能模块，但没有在配置文件中设置相应的属性，也可能导致错误。例如，使用 Spring Security 时，没有配置相关的安全策略等。
>
> ### 2\. 检查代码逻辑
>
>   * **检查控制器层** ：确认控制器中的请求映射是否正确，是否有拼写错误或者路径不匹配的情况。例如，`@RequestMapping`、`@GetMapping` 等注解的路径是否与前端请求的路径一致。
>   * **检查服务层和数据访问层** ：查看服务层的业务逻辑是否有错误，如数据的增删改查操作是否正确执行。对于数据访问层，检查 SQL 语句是否正确，是否与数据库表结构匹配，是否有未处理的异常等。
>   * **检查异常处理逻辑** ：如果你在代码中有自定义的异常处理机制，确保其能够正确处理各种异常情况，并返回合适的响应。如果没有自定义异常处理，则可以考虑添加一个全局异常处理器，来捕获和处理应用中的异常。
>
> ### 3\. 检查依赖
>
>   * **查看 pom.xml 或 build.gradle 文件** ：确保项目中引入的依赖正确且版本兼容。例如，Spring Boot 各个版本之间可能存在一些兼容性问题，或者某些依赖库的版本与 Spring Boot 版本不匹配。可以尝试更新依赖到最新的稳定版本，或者查看 Spring 官方的依赖兼容性文档，来选择合适的版本组合。
>   * **检查依赖冲突** ：使用 Maven 或 Gradle 的相关命令来检查项目中是否存在依赖冲突。对于 Maven，可以使用`mvn dependency:tree`命令来查看依赖树，找出冲突的依赖并进行排除。
>
> ### 4\. 检查服务器环境
>
>   * **检查服务器是否正常运行** ：确保应用所部署的服务器（如 Tomcat、Jetty 等）正常启动，并且没有出现其他故障。可以查看服务器的日志文件来获取更多信息。
>   * **检查资源限制** ：确认服务器是否有足够的内存、CPU 等资源来运行应用程序。如果资源不足，可能会导致应用出现异常。可以通过服务器的监控工具来查看资源使用情况。
>
> ### 5\. 启用详细错误信息（开发环境下）
>
>   * **在 application.properties 文件中添加以下配置** ：`server.error.include-stacktrace=always`，这样在出现错误时，页面上会显示更详细的错误堆栈信息，有助于你快速定位问题。
>
> 如果按照以上方法仍然无法解决问题，你可以将更详细的错误日志、相关代码片段等信息提供给我，我会进一步帮你分析问题所在。

### 3.spring官方脚手架连接不上

> ![image-20250808092234758](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250808092234758.png)
>



# 【跨域错误】



* 在springboot中设置统一跨域处理（允许跨域）
  ![image-20250902103821009](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902103821009.png)

  ![image-20250902104136378](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250902104136378.png)

```java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");//1. 设置访问源地址
        corsConfiguration.addAllowedHeader("*");//2. 设置访问源请求头
        corsConfiguration.addAllowedMethod("*");//3. 设置访问源请求方法
        source.registerCorsConfiguration("/**",corsConfiguration);//4. 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
```

> 这段 `CorsConfig` 代码是 Spring Boot 项目中用来**全局解决跨域（CORS）问题**的配置类。它的核心原理是：**在服务器端主动声明允许来自其他源（域名、端口、协议）的请求访问资源**。
>
> 下面我来用通俗易懂的方式，分步解释它是如何解决跨域问题的：
>
> ---
>
> #### 🔍 什么是“跨域”？
> 浏览器出于安全考虑，有一个 **同源策略（Same-Origin Policy）**：
> - 只有当 **协议（http/https）+ 域名 + 端口** 完全相同时，才允许前端页面向后端发送请求。
> - 比如前端运行在 `http://localhost:5173`，而后端在 `http://localhost:9000`，虽然域名一样，但端口不同 → 跨域。
>
> 此时浏览器会拦截请求，除非后端明确说：“我允许你来访问”。
>
> ---
>
> #### ✅ 这段代码做了什么？
>
> #### 1. `@Configuration` 和 `@Bean`
> - 表示这是一个配置类，Spring 启动时会加载它。
> - `@Bean` 注解的方法会把返回的对象注册到 Spring 容器中，这里注册的是一个 `CorsFilter`（跨域过滤器）。
>
> #### 2. 创建 `CorsConfiguration` 对象
> 这个对象用来**定义跨域规则**，就像一张“通行证”：
>
> ```java
> corsConfiguration.addAllowedOrigin("*");
> ```
> > 允许任何来源的请求（比如 `http://localhost:8080`、`https://example.com`）。  
> > ⚠️ 生产环境建议写具体域名，如 `http://localhost:8080`，而不是 `"*"`。
>
> ```java
> corsConfiguration.addAllowedHeader("*");
> ```
> > 允许任何请求头（比如 `Content-Type`、`Authorization` 等）。
>
> ```java
> corsConfiguration.addAllowedMethod("*");
> ```
> > 允许任何 HTTP 方法（GET、POST、PUT、DELETE 等）。
>
> #### 3. 将配置绑定到请求路径
> ```java
> source.registerCorsConfiguration("/**", corsConfiguration);
> ```
> > 把上面的跨域规则应用到所有接口路径（`/**` 表示拦截所有请求）。
>
> #### 4. 创建并返回 `CorsFilter`
> ```java
> return new CorsFilter(source);
> ```
> > 这个过滤器会在每次请求到达之前进行检查，并自动添加必要的 **CORS 响应头**。
>
> ---
>
> #### 🌐 浏览器是如何被“说服”的？
> 当浏览器发起一个跨域请求时，比如前端调用 `fetch('http://localhost:9000/api/user')`，Spring 后端会通过这个 `CorsFilter` 在响应中自动加上类似这样的头信息：
>
> ```http
> Access-Control-Allow-Origin: *
> Access-Control-Allow-Methods: GET, POST, PUT, DELETE, ...
> Access-Control-Allow-Headers: *
> ```
>
> 浏览器看到这些响应头后，发现：
> - “哦，这个服务器允许我这个来源访问”
> - → 放行请求，不报错 ✅
>
> ---
>
> #### 🧠 总结：原理一句话
> > **服务器通过 `CorsFilter` 在响应头中添加 CORS 相关字段，告诉浏览器“我允许跨域访问”，从而绕过浏览器的同源策略限制。**
>
> ---
>
> ### 🛡️ 小提示（生产环境建议）
> ```java
> // 不推荐
> corsConfiguration.addAllowedOrigin("*");
> 
> // 推荐：只允许指定的前端地址
> corsConfiguration.addAllowedOrigin("http://localhost:8080");
> corsConfiguration.addAllowedOrigin("https://yourdomain.com");
> ```
>
> 这样更安全，避免恶意网站随意调用你的接口。
>
> ---
>
> ✅ 所以，加上这段代码后，你的 Spring Boot 项目就能被其他域名/端口的前端（如 Vue、React 应用）安全调用了。







# 前置准备

* 启动好redis，















# SQL报错

* #### **<u>对于比较复杂的查询sql，最好在数据库里面调试好、确保当前sql能够查询到想要的结果，再拷贝到xml文件中</u>**







* ![image-20250922150151357](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250922150151357.png)

* 本次错误原因为update语句<if></if>里面没有逗号，语法错误

  ```
  <update id="update">
      update employee
      <set>
          <if test="name != null">name = #{name},</if>
          <if test="username != null">username = #{username},</if>
          <if test="password != null">password = #{password},</if>
          <if test="phone != null">phone = #{phone},</if>
          <if test="sex != null">sex = #{sex},</if>
          <if test="idNumber != null">id_number = #{idNumber},</if>
          <if test="updateUser != null">update_user = #{updateUser},</if>
          <if test="status != null">status = #{status},</if>
          update_time = #{updateTime}
      </set>
      where id = #{id}
  </update>
  ```



---

---



* insert语句末尾没有 id = #{id}  !!!

---

---

* 语法报错异常，且没有报错信息

  ![image-20250927074935812](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250927074935812.png)

  * 解决过程、解决方案

    自己看一遍、查询ai是否有语法错误
    提示信息太少，询问ai怎么获取更多提示信息（开启mybatis日志）

    > #### 如何开启mybatis日志
    >
    > ![image-20250927075529644](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250927075529644.png)
    >
    > ```
    > logging:
    >  level:
    >    com.example.mapper: DEBUG
    > ```
    >
    > ```
    > mybatis:
    >  configuration:
    >    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    > ```

    发现已经开过了
    确实很难找出错在哪里，打算全部逻辑都截图发给ai
    发现第二种的mybatis日志没开，打开之后发现这种日志信息更详细
    发现第二种日志打开了也没什么用
    又去问通义哪里错了，也没找出来
    又调试了两遍，发现应该不是badsqlGrammarexception的问题，跟踪之后怀疑是InvocationTargetException异常

  * 错误原因

    1. sql语句里面的
       <if test="categoryId != null"></if>
       里面的test写成了"category_id != null"

---

---

* ![image-20251007103832924](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251007103832924.png)

  

  > 这段SQL存在几个问题：
  >
  > 1. **字段选择错误**：
  >    - `order_detail.* as orderDetailList` 语法不正确。不能直接将多个字段（`*`）赋值给一个别名 [orderDetailList](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-pojo\src\main\java\com\sky\vo\OrderVO.java#L19-L19)。这会导致MyBatis无法正确映射结果。
  >
  > 2. **关联查询结果映射问题**：
  >    - 当使用 `orders.*` 和 `order_detail.*` 时，如果两个表中有相同名称的字段，会导致字段覆盖或映射混乱。
  >
  > 3. **缺少明确的字段映射**：
  >    - 没有指定具体需要从 `order_detail` 表中获取哪些字段，也没有正确地将这些字段映射到 [OrderVO](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-pojo\src\main\java\com\sky\vo\OrderVO.java#L10-L21) 对象中的 [orderDetailList](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-pojo\src\main\java\com\sky\vo\OrderVO.java#L19-L19) 属性。
  >
  > 建议修改为：
  >
  > ```xml
  > <select id="pageQuery" resultType="com.sky.vo.OrderVO">
  >     SELECT 
  >         orders.*,
  >         order_detail.id as orderDetailId,
  >         order_detail.name as orderDetailName,
  >         order_detail.image as orderDetailImage,
  >         order_detail.order_id as orderId,
  >         order_detail.dish_id as dishId,
  >         order_detail.setmeal_id as setmealId,
  >         order_detail.dish_flavor as dishFlavor,
  >         order_detail.number as orderDetailNumber,
  >         order_detail.amount as orderDetailAmount
  >     FROM orders LEFT JOIN order_detail ON orders.id = order_detail.order_id
  >     <where>
  >         <if test="status != null">AND status = #{status}</if>
  >         <if test="userId != null">AND user_id = #{userId}</if>
  >     </where>
  > </select>
  > ```
  >
  >
  > 或者更好的方式是使用 `resultMap` 来处理一对多的关系映射，这样可以直接将 `order_detail` 的数据映射到 [OrderVO](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-pojo\src\main\java\com\sky\vo\OrderVO.java#L10-L21) 的 [orderDetailList](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-pojo\src\main\java\com\sky\vo\OrderVO.java#L19-L19) 集合属性中。

---

---

* 不是所有的表里面表示创建时间的字段都叫 create_time ，比如orders表里面可能叫order_time

  ![image-20251008161049293](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251008161049293.png)

* 这种where与多个条件搭配的动态sql不要忘了加and

  ![image-20251008170543171](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251008170543171.png)

---

---

#### 复杂sql错题

1. 

   > ![image-20251009171228934](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251009171228934.png)
   >
   > ![image-20251009171326301](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251009171326301.png)
   >
   > ![image-20251009171235308](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251009171235308.png)
   >
   > 
   > 
   >
   > ![image-20251009171341208](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251009171341208.png)
   >
   > 
   > 

* ai

  > 思考
  >
  > > 刚拿到这个问题时，我第一反应是去仔细检查SQL语句的语法以及逻辑是否满足需求。因为这是一个`SELECT`查询语句，涉及到子查询和别名，我首先尝试从语法和功能实现两个层面去验证它是否正确。
  > >
  > > 好，现在开始看这个SQL：
  > >
  > > sql
  > >
  > > 复制
  > >
  > > ```sql
  > > <select id="pageQuery" resultType="com.sky.vo.OrderVO">
  > >     select o.id, o.number, o.status, o.user_id, o.address_book_id, o.order_time,
  > >            o.checkout_time, o.pay_method, o.pay_status, o.amount, o.remark, o.user_name,
  > >            o.phone, o.address, o.consignee, o.cancel_reason, o.rejection_reason, o.cancel_time,
  > >            o.estimated_delivery_time, o.delivery_status, o.delivery_time, o.pack_amount,
  > >            o.tableware_number, o.tableware_status,
  > >     (select * from order_detail where order_id = o.id) as orderDetailList from orders
  > > </select>
  > > ```
  > >
  > > 第一眼看去，这里有一个明显的可疑点，就是子查询部分：
  > >
  > > sql
  > >
  > > 复制
  > >
  > > ```sql
  > > (select * from order_detail where order_id = o.id) as orderDetailList
  > > ```
  > >
  > > 这让我停顿了一下，因为直觉告诉我，子查询返回的是一个结果集，而我们试图把它作为一个普通的字段（`orderDetailList`）赋值给结果。这种写法在SQL中并不常见，而且会引发问题：SQL不允许将多行多列的结果直接作为单个字段返回。
  > >
  > > 于是我立刻决定从逻辑上再细想一下这段代码的用意。既然是用MyBatis，而`resultType`指定的是`OrderVO`，那么子查询应该是想把`order_detail`表的内容和主表`orders`联合查询出来，尤其是`order_detail`表的多行结果应该映射到`OrderVO`的`orderDetailList`字段。显然，这用`SELECT *`是做不到的，因为它不符合常见的MyBatis映射规则。
  > >
  > > 接下来，我决定尝试对这个问题进行分解，先验证几个关键点：
  > >
  > > 1. **子查询部分是否有效**：
  > >
  > >    - 我先尝试手动执行一个简单的子查询：
  > >
  > >      sql
  > >
  > >      复制
  > >
  > >      ```sql
  > >      select * from order_detail where order_id = 1;
  > >      ```
  > >
  > >      如果子查询本身能正确返回数据，那么问题就在它试图嵌入`SELECT`主查询时。
  > >
  > > 2. **字段映射是否合理**：
  > >
  > >    - 看了看`OrderVO`和`Orders`的代码，`orderDetailList`是一个`List<OrderDetail>`类型，而直接用`SELECT *`作为它的来源，显然和MyBatis的映射规则不匹配。这里应该需要一个嵌套的`<association>`或`<collection>`映射。
  > >
  > > 3. **SQL语句改进方向**：
  > >
  > >    - 我需要将查询改写为可以正确支持一对多关系的形式，主表（`orders`）和从表（`order_detail`）通过关联查询加载。
  > >
  > > ### 开始尝试
  > >
  > > 首先，我尝试验证子查询单独运行是否能有结果。在MySQL控制台中，我写了一个简单的查询：
  > >
  > > sql
  > >
  > > 复制
  > >
  > > ```sql
  > > select * from order_detail where order_id = 1;
  > > ```
  > >
  > > 幸运的是，我得到了预期的返回结果，这说明子查询本身语法没问题，问题出在它被嵌套到主查询中的写法。
  > >
  > > 接下来，我尝试改写SQL让它返回一个主键为`id`的字段，而不是直接`SELECT *`：
  > >
  > > sql
  > >
  > > 复制
  > >
  > > ```sql
  > > (select json_arrayagg(od) from order_detail od where od.order_id = o.id) as orderDetailList
  > > ```
  > >
  > > 修改后的查询会尝试用聚合的方式将多个`order_detail`记录嵌套到主查询的结果集中。但这里又出现问题：MyBatis默认并不能直接支持JSON格式的映射。
  > >
  > > ### 错误后的调整
  > >
  > > 意识到依赖`JSON`聚合的方法对MyBatis来说不直观后，我决定走一个更简单的路径：使用`LEFT JOIN`的方式，把`orders`和`order_detail`关联起来，返回所有需要的字段，然后再依赖MyBatis的`<resultMap>`来处理一对多关系：
  > >
  > > sql
  > >
  > > 复制
  > >
  > > ```sql
  > > SELECT 
  > >     o.id, o.number, o.status, o.user_id, o.address_book_id, o.order_time,
  > >     o.checkout_time, o.pay_method, o.pay_status, o.amount, o.remark, o.user_name,
  > >     o.phone, o.address, o.consignee, o.cancel_reason, o.rejection_reason, o.cancel_time,
  > >     o.estimated_delivery_time, o.delivery_status, o.delivery_time, o.pack_amount,
  > >     o.tableware_number, o.tableware_status,
  > >     od.id AS detail_id, od.order_id, od.dish_id, od.dish_name, od.setmeal_id, od.setmeal_name, od.dish_flavor
  > > FROM orders o
  > > LEFT JOIN order_detail od ON o.id = od.order_id
  > > ```
  > >
  > > ### 验证结果
  > >
  > > 改写后，我重新检查SQL语义是否合理。`LEFT JOIN`可以正确地把主表和从表的数据结合在一起，同时用别名`detail_id`区分主表和从表的`id`字段，避免字段冲突。
  > >
  > > 在MyBatis中，我需要为`OrderVO`设置一个`<resultMap>`，用来映射一对多的关系：
  > >
  > > xml
  > >
  > > 复制
  > >
  > > ```xml
  > >     <resultMap id="OrderVOResultMap" type="com.sky.vo.OrderVO">
  > >         <id property="id" column="id" />
  > >         <result property="number" column="number" />
  > >         <result property="status" column="status" />
  > >         <result property="userId" column="user_id" />
  > >         <result property="addressBookId" column="address_book_id" />
  > >         <result property="orderTime" column="order_time" />
  > >         <result property="checkoutTime" column="checkout_time" />
  > >         <result property="payMethod" column="pay_method" />
  > >         <result property="payStatus" column="pay_status" />
  > >         <result property="amount" column="amount" />
  > >         <result property="remark" column="remark" />
  > >         <result property="userName" column="user_name" />
  > >         <result property="phone" column="phone" />
  > >         <result property="address" column="address" />
  > >         <result property="consignee" column="consignee" />
  > >         <result property="cancelReason" column="cancel_reason" />
  > >         <result property="rejectionReason" column="rejection_reason" />
  > >         <result property="cancelTime" column="cancel_time" />
  > >         <result property="estimatedDeliveryTime" column="estimated_delivery_time" />
  > >         <result property="deliveryStatus" column="delivery_status" />
  > >         <result property="deliveryTime" column="delivery_time" />
  > >         <result property="packAmount" column="pack_amount" />
  > >         <result property="tablewareNumber" column="tableware_number" />
  > >         <result property="tablewareStatus" column="tableware_status" />
  > >         <collection property="orderDetailList" ofType="com.sky.entity.OrderDetail">
  > >             <id property="id" column="detail_id" />
  > >             <result property="name" column="dish_name" />
  > >             <result property="orderId" column="order_id" />
  > >             <result property="dishId" column="dish_id" />
  > >             <result property="setmealId" column="setmeal_id" />
  > >             <result property="dishFlavor" column="dish_flavor" />
  > >             <result property="number" column="number" />
  > >             <result property="amount" column="amount" />
  > >             <result property="image" column="image" />
  > >         </collection>
  > >     </resultMap>
  > > 
  > > 
  > >     <select id="pageQuery" resultType="com.sky.vo.OrderVO">
  > >         SELECT
  > >             o.id, o.number, o.status, o.user_id, o.address_book_id, o.order_time,
  > >             o.checkout_time, o.pay_method, o.pay_status, o.amount, o.remark, o.user_name,
  > >             o.phone, o.address, o.consignee, o.cancel_reason, o.rejection_reason, o.cancel_time,
  > >             o.estimated_delivery_time, o.delivery_status, o.delivery_time, o.pack_amount,
  > >             o.tableware_number, o.tableware_status,
  > >             od.id AS detail_id, od.order_id, od.dish_id, od.name AS dish_name,od.image, od.setmeal_id,od.number as number, od.amount, od.dish_flavor
  > >         FROM orders o
  > >                  LEFT JOIN order_detail od ON o.id = od.order_id
  > >     </select>
  > > ```
  > >
  > > ### 验证效果
  > >
  > > 我运行了这个改写后的SQL和MyBatis配置，结果能够正确返回`OrderVO`对象，并且`orderDetailList`字段成功加载了关联的`order_detail`数据。
  > >
  > > 通过这次调试过程，我意识到，相比用嵌套子查询的方式，`LEFT JOIN`结合MyBatis的`<resultMap>`是一对多场景下的最佳实践。
  > >
  > > 
  >
  > 这个SQL语句不正确。子查询`(select * from order_detail where order_id = o.id) as orderDetailList`试图将多行多列的结果作为单个字段返回，这在SQL中是不允许的。此外，MyBatis默认不支持将JSON格式的聚合结果直接映射到对象属性。
  >
  > 正确的做法是使用`LEFT JOIN`将`orders`表和`order_detail`表关联起来，然后在MyBatis中使用`<resultMap>`处理一对多的关系。以下是改写后的SQL和MyBatis配置示例：
  >
  > ```sql
  > SELECT 
  >     o.id, o.number, o.status, o.user_id, o.address_book_id, o.order_time,
  >     o.checkout_time, o.pay_method, o.pay_status, o.amount, o.remark, o.user_name,
  >     o.phone, o.address, o.consignee, o.cancel_reason, o.rejection_reason, o.cancel_time,
  >     o.estimated_delivery_time, o.delivery_status, o.delivery_time, o.pack_amount,
  >     o.tableware_number, o.tableware_status,
  >     od.id AS detail_id, od.order_id, od.dish_id, od.dish_name, od.setmeal_id, od.setmeal_name, od.dish_flavor
  > FROM orders o
  > LEFT JOIN order_detail od ON o.id = od.order_id
  > ```
  >
  > 在MyBatis中，为`OrderVO`设置一个`<resultMap>`来映射查询结果：
  >
  > ```xml
  >     <resultMap id="OrderVOResultMap" type="com.sky.vo.OrderVO">
  >         <id property="id" column="id" />
  >         <result property="number" column="number" />
  >         <result property="status" column="status" />
  >         <result property="userId" column="user_id" />
  >         <result property="addressBookId" column="address_book_id" />
  >         <result property="orderTime" column="order_time" />
  >         <result property="checkoutTime" column="checkout_time" />
  >         <result property="payMethod" column="pay_method" />
  >         <result property="payStatus" column="pay_status" />
  >         <result property="amount" column="amount" />
  >         <result property="remark" column="remark" />
  >         <result property="userName" column="user_name" />
  >         <result property="phone" column="phone" />
  >         <result property="address" column="address" />
  >         <result property="consignee" column="consignee" />
  >         <result property="cancelReason" column="cancel_reason" />
  >         <result property="rejectionReason" column="rejection_reason" />
  >         <result property="cancelTime" column="cancel_time" />
  >         <result property="estimatedDeliveryTime" column="estimated_delivery_time" />
  >         <result property="deliveryStatus" column="delivery_status" />
  >         <result property="deliveryTime" column="delivery_time" />
  >         <result property="packAmount" column="pack_amount" />
  >         <result property="tablewareNumber" column="tableware_number" />
  >         <result property="tablewareStatus" column="tableware_status" />
  >         <collection property="orderDetailList" ofType="com.sky.entity.OrderDetail">
  >             <id property="id" column="detail_id" />
  >             <result property="name" column="dish_name" />
  >             <result property="orderId" column="order_id" />
  >             <result property="dishId" column="dish_id" />
  >             <result property="setmealId" column="setmeal_id" />
  >             <result property="dishFlavor" column="dish_flavor" />
  >             <result property="number" column="number" />
  >             <result property="amount" column="amount" />
  >             <result property="image" column="image" />
  >         </collection>
  >     </resultMap>
  > 
  > 
  >     <select id="pageQuery" resultType="com.sky.vo.OrderVO">
  >         SELECT
  >             o.id, o.number, o.status, o.user_id, o.address_book_id, o.order_time,
  >             o.checkout_time, o.pay_method, o.pay_status, o.amount, o.remark, o.user_name,
  >             o.phone, o.address, o.consignee, o.cancel_reason, o.rejection_reason, o.cancel_time,
  >             o.estimated_delivery_time, o.delivery_status, o.delivery_time, o.pack_amount,
  >             o.tableware_number, o.tableware_status,
  >             od.id AS detail_id, od.order_id, od.dish_id, od.name AS dish_name,od.image, od.setmeal_id,od.number as number, od.amount, od.dish_flavor
  >         FROM orders o
  >                  LEFT JOIN order_detail od ON o.id = od.order_id
  >     </select>
  > ```
  >
  > 这样可以正确地将`orders`表和`order_detail`表的数据关联起来，并映射到`OrderVO`对象及其`orderDetailList`属性。
  >
  > 
  >
  > **注意：实际上不可行**
  >
  > 

  **对于返回的对象中包含一个对象数组的情况，分开写sql语句**

  /**
       * 用户端订单分页查询
       *
       * @param pageNum
       * @param pageSize
       * @param status
       * @return
       */
      public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
          // 设置分页
          PageHelper.startPage(pageNum, pageSize);
          OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
          ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
          ordersPageQueryDTO.setStatus(status);

          // 分页条件查询
          Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
      
          List<OrderVO> list = new ArrayList();
      
          // 查询出订单明细，并封装入OrderVO进行响应
          if (page != null && page.getTotal() > 0) {
              for (Orders orders : page) {
                  Long orderId = orders.getId();// 订单id
      
                  // 查询订单明细
                  List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
      
                  OrderVO orderVO = new OrderVO();
                  BeanUtils.copyProperties(orders, orderVO);
                  orderVO.setOrderDetailList(orderDetails);
      
                  list.add(orderVO);
              }
          }
          return new PageResult(page.getTotal(), list);
      }

  > **数目用page.getTotal()获得，具体数据用list返回**
  >
  > 

  

#### And和OR的处理错误

![image-20260404214428151](assets/image-20260404214428151.png)





* 处理方式

  错误: 无法确定参数 $2 的数据类型

  

  问题很明显！SQL 的 WHERE 子句中括号的位置有问题：

  当前写法：
  ```sql
  WHERE l.owner_id = #{ownerId}
  AND (l.name LIKE CONCAT('%', #{keyword}, '%')) OR (l.description LIKE CONCAT('%', #{keyword}, '%'))
  ```

  这个 SQL 的逻辑是：
  ```
  (owner_id = ? AND name LIKE ?) OR description LIKE ?
  ```

  但是 `#{keyword}` 参数只传了一次，却在两个地方使用了。PostgreSQL 无法确定第二个参数的类型。

  正确的写法应该是：
  ```sql
  WHERE l.owner_id = #{ownerId}
  AND ((l.name LIKE CONCAT('%', #{keyword}, '%')) OR (l.description LIKE CONCAT('%', #{keyword}, '%')))
  ```

  或者更简洁：


  ```sql
  WHERE l.owner_id = #{ownerId}
  AND (l.name LIKE CONCAT('%', #{keyword}, '%') OR l.description LIKE CONCAT('%', #{keyword}, '%'))
  ```

  关键是要传两次 `#{keyword}` 参数。我需要修复这个 SQL 语句的括号逻辑，确保参数正确绑定。

  当前 SQL实际解析为：

  ```sql
  (owner_id = ? AND name LIKE ?) OR description LIKE ?
  ```


  这样 `keyword` 参数只绑定了一次，但 SQL 需要两个参数，导致 PostgreSQL 无法确定第二个参数类型。

  

  修复后的 SQL：
  ```sql
  WHERE l.owner_id = #{ownerId}
  AND (l.name LIKE ... OR l.description LIKE ...)
  ```

  现在整个 `OR` 条件被包含在一个括号内，作为 `AND` 的一个整体条件，MyBatis 会正确地将两个 `#{keyword}` 绑定为同一个参数值。

  整个 `OR` 条件作为一个整体，两个 `#{keyword}` 都会被正确绑定为同一个参数值。

* 依旧会报错：

  原因分析：
  CONCAT('%', #{keyword}, '%') 中，PostgreSQL 无法推断 #{keyword} 参数的类型
  使用 PostgreSQL 原生的 || 字符串连接操作符时，类型推断是明确的

  ```
  -- 修改前
  LIKE CONCAT('%', #{keyword}, '%')
  
  -- 修改后  
  LIKE '%' || #{keyword} || '%'
  ```

  ![image-20260404215039115](assets/image-20260404215039115.png)









# vue3系列

### 1.不能正确显示出页面

* 路径不匹配导致

![image-20250709122049301](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709122049301.png)

* 对应页面的文件语法错误导致（比如这里如果没给ref导包，就不会正常显示页面）
  ![image-20250709122631574](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250709122631574.png)









# 常见浏览器错误

> #### 4开头——客户端错误
>
> 
>
> 







## 405

请求类型不匹配

* 例：使用put请求请求delete类的接口
* 











## 新增数据失败

* 原因：<u>前端在请求中传给后端的数据没有覆盖除了id之外的其他所有不能为null的属性（departmentId）</u>，补全之后新增成功
  * ![image-20250903092213223](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903092213223.png)
    （忘记了自己在设计表的时候把两个属性值都勾选“不是null”了)



### 前后端请求系列bug

* 查看控制台

  * 直接定位是源代码哪里出了问题（比较简单的bug）
    ![image-20250903210224483](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903210224483.png)

  * 
    

* 查看后端因为请求产生的日志
  定位后端报错原因(位置)

* 也可考虑使用postman测试后端接口，判断是否是后端的问题

* 考虑使用后端断点调试、同时查看不同阶段局部变量的值是否符合预期，进而定位bug

* 考虑在发送请求前在控制台打印form表单
  (console.log(data.form))





---

---



![image-20250903170238053](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903170238053.png)

* 原因：没有导入request.js文件到当前文件，导致没办法正常使用创建的request实例
  * ![image-20250903170343834](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903170343834.png)
    虽然此处代码正常，也写了request，但是没有导入request.js、不能正常运作（可以看到这里的request下面有标线)
  * 



---

---



![image-20250903185043170](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903185043170.png)

#### 【404】未找到接口

* 报错原因：
  * 增加login接口之后没有重启后台，导致访问不到这个接口
    **更新完后端记得要重新启动**
  * 接口写错

---

---



![image-20250903190525243](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903190525243.png)

![image-20250903190533988](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903190533988.png)

* 报错原因：后台数据库中虽然给数据设置了密码，但是没保存，导致后端依照用户名查到的对象的密码是null，进而出现equals()函数调用出错
  **数据库修改完数据记得要保存**

---

---



![image-20250903204432329](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903204432329.png)

* 网络请求失败
  * 

---

---

![img](https://www.kimi.com/apiv2-files/sign-obj/kimi-fs%2Ffiles%2Fblob%2F7b9fce90d4f17af7c5bf1f376d06be70a15909810a9015a21fbfa4fc4954a4f4/image.png?sig=7mbW0sSIYS88o9uKaOGunqFKT0vWZaG6qtT6wxuOhCc=&t=t)

![img](https://www.kimi.com/apiv2-files/sign-obj/kimi-fs%2Ffiles%2Fblob%2F334d5441af6c2e22c9dc3f7a9760f48999bd089392ad60f6e9c39f4e260ec6df/image.png?sig=pv3J48oG4UPmqxZxSBZ21P9qytKsQwBVCzo6FfhU5ic=&t=t)



问题：前端data.form数据正常，传到后端时account里面newPassword属性却变成了null

* 本次错误原因：后端中的newPassword打成了newPasssword，导致后端实体类缺失对应的字段

![image-20250904200702986](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250904200702986.png)

---

---

![image-20250905205320198](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905205320198.png)

问题：404，未找到接口

* 有时候出现404就单纯是因为过程/路径中个别单词拼写错误了
  ![image-20250905205358747](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250905205358747.png)







# 前端bug系列









* 部分数据只在特定情况下存在，如果直接作为一个元素展示并且在浏览器直接访问这个页面就会报错

  * 比如这里的data.user属性


    ![image-20250903211641622](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903211641622.png)![image-20250903211553074](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903211553074.png)
    此时如果不加v-if、不加以判断这个元素是否存在，直接展示这个元素就会报错
    ![image-20250903211732620](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250903211732620.png)



# IDEA bug

#### 文件历史版本 changes 对比失败

手动检查与加入当前仓库

![image-20260402175012047](assets/image-20260402175012047.png)

指定git.exe的位置

![image-20260402175043259](assets/image-20260402175043259.png)





# 后端bug



#### jdk版本不匹配





* 解决办法  ↓

  ![image-20251108150254628](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251108150254628.png)







![image-20250907082325202](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250907082325202.png)

* 本次报错原因：sql语句中的department单词拼写错误

---

---



* @RequestBody注解要加（如果需要将json数据转换成字符串），否则会导致一些不能为null的字段为null，在执行sql语句的时候发生异常



* 如果出现方法返回值报错，注意看看xxService接口里面的这个方法和xxServiceImpl中这个方法的返回值是否一致

---

---

* bug：knife4j测试接口的时候发现控制台只有拦截器的日志，没有controll层及后面两层的日志，操作执行失败

---

----

* 方法末尾的括号爆红
* 这个类中的{、}括号没有匹配上





---

---



* **“为什么我依赖注入AliOssProperties对象时@Autowired注解会报错”**

* 出错原因：

  没有给  被依赖注入的那个类  配置注解@Component，spring扫描不到
  Spring扫描到的类必须是配置为Bean的，而如果`AliOssUtil`不是Bean，那么Spring会认为它是一个普通的Java类，那`@Autowired`自然不会起作用。

  > 首先，我看到你描述的问题是：使用`@Autowired`注解注入`AliOssProperties`对象时会报错。从代码结构上看，`AliOssProperties`已经被标注为`@Component`，按照Spring的规则，至少它应该被Spring容器识别并实例化成一个Bean。乍一看，这似乎有点矛盾，因为它明明应该是能被注入的。
  >
  > 那我第一步的思路是先假设问题可能出在Spring对`AliOssProperties`的识别上，比如说Spring上下文有没有成功扫描到这个类？嗯，这确实是一个常见的问题。不过再看了一下代码，`AliOssProperties`是被`@Component`注解标记的，这意味着它应该是默认被扫描的。如果Spring的包扫描路径中没有包含这个类的包，那可能问题就出在这里。
  >
  > 接下来，我尝试换一个角度思考：会不会问题的根源不是扫描路径，而是别处？比如`AliOssProperties`这个类本身可能有一些Spring不支持的特征，导致它无法被注册为一个Bean？或者，有没有可能它的字段或者方法配置有问题？
  >
  > 好，我再仔细看了一下代码，嗯，`AliOssProperties`的代码看起来很清晰，字段声明都很正常，也用了`@Data`注解，理论上是支持Getters和Setters的，配置文件是以`prefix = "sky.alioss"`作为前缀的。到这里，我还看不出什么问题。
  >
  > 这时我觉得应该拓宽一下思考路径，去想一想，有没有可能问题不在`AliOssProperties`这个类，而是在调用`@Autowired`注入它的地方。如果是`AliOssUtil`类本身的问题，比如这个类是不是没有标注`@Component`或者是否位于Spring未扫描的位置，那`AliOssProperties`就无法被注入。这个思路让我有些担心，因为`AliOssUtil`是直接依赖注入的类。
  >
  > 我检查了一下`AliOssUtil`类，上面并没有使用`@Component`注解。啊！这可能就是问题的关键了。Spring扫描到的类必须是配置为Bean的，而如果`AliOssUtil`不是Bean，那么Spring会认为它是一个普通的Java类，那`@Autowired`自然不会起作用。



* 第二遍运行增强for循环的时候报错

  ![image-20250926204610794](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250926204610794.png)

* **错误原因，使用增强for循环的同时，在内部对这个数组调用了remove函数**

  > ```
  > for(Long id :ids){
  >     Dish dish = dishMapper.getById(id);
  >     if(dish.getStatus() == StatusConstant.ENABLE){
  >         ids.remove(id);
  >         isExceptional = true;
  >     }else{
  >         //* <u>被套餐关联的菜品不能删除</u>
  >         Long dishSetCount = setmealDishMapper.getSetmealCountByDishId(id);
  >         if(dishSetCount>0){
  >             ids.remove(id);
  >             isRelevantToSet = true;
  >         }
  > 
  >     }
  > }
  > 
  > ```
  >
  > **这个for循环，如果ids里面只有一个元素、第二遍的时候不会自动跳出吗**
  >
  > > 你好！关于你提到的这段代码，让我来分析一下这个for循环的行为。
  > >
  > > 
  > >
  > >
  > > **已查看文件** *DishServiceImpl.java*
  > >
  > >
  > > 你提出的问题非常好。让我来详细解释一下这个for循环的行为：
  > >
  > > ```java
  > > for(Long id :ids){
  > >     Dish dish = dishMapper.getById(id);
  > >     if(dish.getStatus() == StatusConstant.ENABLE){
  > >         ids.remove(id);
  > >         isExceptional = true;
  > >     }else{
  > >         //* <u>被套餐关联的菜品不能删除</u>
  > >         Long dishSetCount = setmealDishMapper.getSetmealCountByDishId(id);
  > >         if(dishSetCount>0){
  > >             ids.remove(id);
  > >             isRelevantToSet = true;
  > >         }
  > > 
  > >     }
  > > }
  > > ```
  > >
  > >
  > > 关于你的问题："如果ids里面只有一个元素、第二遍的时候不会自动跳出吗？"
  > >
  > > 实际上，这段代码存在一个严重的问题。在遍历List的同时修改List（通过[ids.remove(id)](file:///D:/ProjectOfBZH/sky_Project/sky-take-out/sky-server/src/main/java/com/sky/service/impl/DishServiceImpl.java#L104-L127)）会导致`ConcurrentModificationException`异常，在大多数情况下程序甚至无法正常运行到第二遍循环。
  > >
  > > 具体来说：
  > > 1. 当使用增强for循环(`for(Long id :ids)`)时，Java会在内部创建一个Iterator来遍历集合
  > > 2. 如果在遍历过程中直接调用集合的[remove()](file:///D:/ProjectOfBZH/sky_Project/sky-take-out/sky-common/src/main/java/com/sky/utils/AliOssUtil.java#L49-L56)方法修改集合，会导致Iterator的状态与集合状态不一致
  > > 3. 下一次调用Iterator的[next()](file:///D:/ProjectOfBZH/sky_Project/sky-take-out/sky-common/src/main/java/com/sky/utils/AliOssUtil.java#L58-L63)方法时会抛出`ConcurrentModificationException`异常
  > >
  > > 所以回答你的问题：即使只有一个元素，程序也不会正常执行到第二遍循环，而是会在尝试进行下一次迭代时抛出异常。
  > >
  > > 这段代码需要修改为使用Iterator或者先收集需要删除的元素，然后再进行删除操作。

  * 改成普通的for循环仍然会报错

    ```
    for(int i = 0;i<ids.size();i++){
    	Long id == ids.get(i);
        Dish dish = dishMapper.getById(id);
        if(dish.getStatus() == StatusConstant.ENABLE){
            ids.remove(id);
            isExceptional = true;
        }else{
            //* <u>被套餐关联的菜品不能删除</u>
            Long dishSetCount = setmealDishMapper.getSetmealCountByDishId(id);
            if(dishSetCount>0){
                ids.remove(id);
                isRelevantToSet = true;
            }
    
        }
    }
    ```

    > 你提出的修改方案仍然存在问题。让我来分析一下：
    > 这种写法虽然避免了增强for循环（for-each）的问题，但仍然存在另一个严重问题：在循环过程中修改列表大小会导致索引错误。
    > 让我详细解释：
    > 假设ids列表中有3个元素：[1, 2, 3]
    > 当i=0时，删除了元素1，列表变成[2, 3]
    > 但循环继续，i变为1，此时访问的是新列表中的索引1，即元素3
    > 这样就跳过了元素2，导致逻辑错误
    > 更严重的是，如果在循环中删除元素，可能会导致IndexOutOfBoundsException，因为循环的边界条件（ids.size()）在变化，而索引i在递增。
    > 正确的做法应该是使用Iterator或者先收集需要处理的元素，再进行批量操作。

    

* **<u>InvocationTargetException</u>**
  当被调用的方法的内部抛出了异常而没有被捕获时，将由此异常接收

---

---

* ![image-20250928102310913](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250928102310913.png)
  Caused by: org.springframework.context.annotation.ConflictingBeanDefinitionException: Annotation-specified bean name 'shopController' for bean class [com.sky.controller.user.ShopController] conflicts with existing, non-compatible bean definition of same name and class [com.sky.controller.admin.ShopController]

  

* 错误原因：
  程序中存在两个同名的交给IOC管理的controller类，导致bean对象重复
  ![image-20250928102622919](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250928102622919.png)

* 解决办法：
  @RestController("userShopController")后面手动给bean指定一个名字、避免重名

* 

---

---

* ![image-20250930162025044](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250930162025044.png)

  > 分析
  > 这个异常是Java中常见的类型不匹配错误。让我为您详细解释：
  >
  > ## 异常分析
  >
  > - **异常类型**: `java.lang.IllegalArgumentException`
  > - **具体原因**: `invalid comparison: java.time.LocalDateTime and java.lang.String` (或类似的基本类型)
  >
  > ## 可能的原因
  >
  > 1. **日期比较错误**: <u>在代码中尝试将 `LocalDateTime` 对象与字符串或其他基本类型进行比较</u>
  > 2. **数据库查询**: 在SQL查询或MyBatis映射中，对时间字段进行了不当的类型比较
  > 3. **条件判断**: 在业务逻辑中错误地比较了不同类型的对象
  >
  > ## 解决方案
  >
  > ```java
  > // 错误示例 - 不要这样做
  > if (localDateTimeObject.equals(stringValue)) { ... }
  > 
  > // 正确做法 - 确保类型一致
  > if (localDateTimeObject.isEqual(anotherLocalDateTime)) { ... }
  > // 或者转换为相同类型后再比较
  > if (localDateTimeObject.toString().equals(stringValue)) { ... }
  > ```
  >
  >
  > ## 建议检查点
  >
  > 基于您提供的 [DishServiceImpl.java](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-server\src\main\java\com\sky\service\impl\DishServiceImpl.java) 文件，建议重点检查以下位置：
  >
  > 1. 注释掉的时间相关字段设置代码：
  >    ```java
  >    // dish.setCreateTime(LocalDateTime.now());
  >    // dish.setUpdateTime(LocalDateTime.now());
  >    ```
  >
  >
  > 2. Mapper XML 文件中的 SQL 查询条件，特别是涉及时间字段的比较部分。
  >
  > 3. 任何涉及时间字段的条件判断逻辑。
  >
  > 需要查看完整的堆栈跟踪信息才能准确定位具体出错位置。
  >
  > 
  > 

* 错误原因

  在代码中尝试将 `LocalDateTime` 对象与字符串或其他基本类型进行比较
  ![image-20250930162434743](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250930162434743.png)
  updateTime属性是LocalDateTime类型，不能和  ‘ ’  这样的空字符串（String类型)比对

  

---

---

* service层方法执行没报错，但是执行完毕发现insert语句没有被执行

* 错误原因：


  ![image-20251003085605423](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251003085605423.png)

   Spring 没有正确注入bean，对象名写错，写成了类名

  ![image-20251003085854089](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251003085854089.png)
  另外可能误触了快捷键导致mapper层方法变成这样

---

---



* sql语法错误

  ![image-20251003161006083](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251003161006083.png)

  ![image-20251003161107722](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251003161107722.png)

* 错误原因：

  表名是sql关键字，没加反引号

  

  > 你提供的这个错误日志非常典型，是 **MyBatis + MySQL 开发中常见的 SQL 语法错误**。我们来一步步分析，找出**最关键的问题**。
  >
  > ---
  >
  > ## 🔍 一、核心错误信息（最关键）
  >
  > ```text
  > Caused by: java.sql.SQLSyntaxErrorException: 
  > You have an error in your SQL syntax; 
  > check the manual that corresponds to your MySQL server version for the right syntax to use near 'order (number,status,user_id,...)' at line 1
  > ```
  >
  > ### ✅ 关键点：
  > > **`near 'order (number, ...)'`**
  >
  > 这说明：**MySQL 在解析 `order` 这个表名时出错了！**
  >
  > ---
  >
  > ## 🚫 二、根本原因：`order` 是 MySQL 的保留关键字！
  >
  > 在 MySQL 中，`ORDER` 是一个 **保留关键字**（用于 `ORDER BY` 语句），**不能直接作为表名使用**，除非你用反引号 `` ` `` 包裹。
  >
  > 你写的 SQL 是：
  >
  > ```sql
  > insert into order (number, status, ...) values (...)
  > ```
  >
  > ❌ 错误：`order` 没有加反引号
  >
  > ✅ 正确写法应该是：
  >
  > ```sql
  > insert into `order` (number, status, ...) values (...)
  > ```
  >
  > ---
  >
  > ## 📌 三、为什么报错位置是 `'order (number, ...)'`？
  >
  > 因为 MySQL 看到 `insert into order` 时，会认为你是想写：
  >
  > ```sql
  > SELECT * FROM user ORDER BY id
  > ```
  >
  > 但它发现后面不是 `BY`，而是一个括号 `(number, ...)`，语法不匹配，所以报错：
  >
  > > **"near 'order (number, ...)"** —— “在 `order (number, ...)` 附近语法错误”
  >
  > 这就是典型的 **关键字冲突** 导致的语法错误。
  >
  > ---
  >
  > ## ✅ 四、解决方案
  >
  > ### ✅ 方法1：给表名加上反引号 `` ` ``
  >
  > 修改你的 MyBatis XML：
  >
  > ```xml
  > <insert id="insert" useGeneratedKeys="true" keyProperty="id">
  >     insert into `order` 
  >     (number, status, user_id, address_book_id, order_time, checkout_time, ...)
  >     values 
  >     (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, ...)
  > </insert>
  > ```
  >
  > > ⚠️ 注意：是 **反引号 `` ` ``**，不是单引号 `'`！
  >
  > ---
  >
  > ### ✅ 方法2：（推荐）改表名，避免使用保留字
  >
  > 更好的做法是：**不要用 `order` 作为表名**，改为：
  >
  > - `orders`
  > - `t_order`
  > - `order_info`
  >
  > 这样就彻底避免关键字冲突。
  >
  > 例如：
  >
  > ```sql
  > create table orders (
  >     id bigint primary key auto_increment,
  >     number varchar(50),
  >     ...
  > );
  > ```
  >
  > 然后 SQL 就可以正常写：
  >
  > ```sql
  > insert into orders (...) values (...)
  > ```
  >
  > ---
  >
  > ## 🛠 五、其他建议
  >
  > ### 1. 开启 MyBatis 日志，查看真实 SQL
  > 在 `application.yml` 中添加：
  >
  > ```yaml
  > mybatis:
  >   configuration:
  >     log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  > ```
  >
  > 这样控制台会打印出实际执行的 SQL，方便调试。
  >
  > ### 2. 使用 `@TableName`（MyBatis-Plus）
  > 如果你用的是 MyBatis-Plus，可以在实体类上加：
  >
  > ```java
  > @TableName("`order`")
  > public class Order {
  >     // ...
  > }
  > ```
  >
  > 或者：
  >
  > ```java
  > @TableName("orders")
  > public class Order {
  >     // ...
  > }
  > ```
  >
  > ---
  >
  > ## ✅ 总结
  >
  > | 问题           | 原因                        | 解决方案                     |
  > | -------------- | --------------------------- | ---------------------------- |
  > | ❌ SQL 语法错误 | `order` 是 MySQL 保留关键字 | ✅ 加反引号 `` `order` ``     |
  > | ❌ 容易出错     | 使用保留字作为表名          | ✅ 改为 `orders` 或 `t_order` |
  >
  > ---
  >
  > ### ✅ 最终修复建议（二选一）：
  >
  > #### ✅ 推荐方案（改表名）：
  > ```sql
  > -- 表名改为 orders
  > insert into orders (number, status, ...) values (...)
  > ```
  >
  > #### ✅ 快速修复（加反引号）：
  > ```sql
  > insert into `order` (number, status, ...) values (...)
  > ```
  >
  > > ⚠️ 注意：反引号是键盘左上角 `Esc` 下面那个键 `` ` ``
  >
  > ---
  >
  > 🔧 修复后，重启应用，应该就能正常插入数据了。
  >
  > 如果你还有其他字段映射问题或报错，欢迎继续贴日志，我可以帮你继续排查。

  

  ---

  ---

  * 
    ![image-20251007174110343](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251007174110343.png)

  * 错误原因：

    端口被占用

    > 根据您提供的完整错误日志，问题已经很明确了：
    >
    > ## 错误分析
    >
    > - **错误类型**: 端口占用冲突
    > - **具体原因**: Web服务器启动失败，因为端口 `8080` 已经被其他进程占用
    > - **影响组件**: [ReportController](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-server\src\main\java\com\sky\controller\admin\ReportController.java#L17-L38) 作为REST控制器需要Web服务器支持，但由于端口冲突无法启动
    >
    > ## 解决方案
    >
    > ### 方案一：终止占用端口的进程
    > ```bash
    > # Windows系统
    > netstat -ano | findstr :8080
    > taskkill /PID <占用端口的进程ID> /F
    > 
    > # Linux/Mac系统
    > lsof -i :8080
    > kill -9 <占用端口的进程ID>
    > ```
    >
    >
    > ### 方案二：修改应用端口
    > 在 [application.yml](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-server\target\classes\application.yml) 或 `application.properties` 中配置：
    > ```yaml
    > server:
    >   port: 8081  # 或其他可用端口
    > ```
    >
    >
    > 或者通过命令行参数启动：
    > ```bash
    > java -jar your-application.jar --server.port=8081
    > ```
    >
    >
    > ## 验证步骤
    >
    > 1. 确认端口是否可用
    > 2. 重新启动应用
    > 3. 检查 [ReportController](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-server\src\main\java\com\sky\controller\admin\ReportController.java#L17-L38) 是否正常注册到Spring容器中
    >
    > 您的 [ReportController](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-server\src\main\java\com\sky\controller\admin\ReportController.java#L17-L38) 代码本身没有问题，主要是运行环境的端口冲突导致整个应用无法启动。

    ![image-20251007174156790](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251007174156790.png)

---

---

* ```
  java.lang.NullPointerException: Cannot invoke "com.sky.properties.BaiduGeoProperties.getGeoCodingUrl()" because "com.sky.utils.BaiduGeoUtil.baiduGeoProperties" is null
  ```

  ![image-20251010194103776](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251010194103776.png)

  baiduGeoProperties对象为null，导致方法调用异常

* 错误原因

  > 根据你提供的代码，[baiduGeoProperties](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-common\src\main\java\com\sky\utils\BaiduGeoUtil.java#L28-L29) 对象为 `null` 的原因如下：
  >
  > 1. **静态字段无法注入**
  >    - 你在 [BaiduGeoUtil](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-common\src\main\java\com\sky\utils\BaiduGeoUtil.java#L23-L133) 类中使用了 `@Autowired` 注解来注入 [baiduGeoProperties](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-common\src\main\java\com\sky\utils\BaiduGeoUtil.java#L28-L29)，但该字段被声明为 `static`。
  >    - **Spring 无法将依赖注入到静态字段中，因为静态字段属于类而不是实例**，而 Spring 的依赖注入是基于实例的。
  >
  > 2. **解决方案**
  >    - 移除 `static` 关键字，让 Spring 正常注入依赖：
  >
  > ```java
  > @Autowired
  > private BaiduGeoProperties baiduGeoProperties; // 移除 static 关键字
  > ```
  >
  >
  > 3. **相关方法调整**
  >    - 同时需要将使用该属性的两个方法改为非静态方法：
  >
  > ```java
  > public String requestGeoCodingAPI(String detailAddress) throws Exception {
  >     // 方法实现保持不变，只需移除 static 关键字
  > }
  > 
  > public String requestRoutePlanApi(double lnt, double lat) throws IOException {
  >     // 方法实现保持不变，只需移除 static 关键字
  > }
  > ```
  >
  >
  > 4. **使用方式**
  >    - 在其他类中使用时，**需要通过注入 [BaiduGeoUtil](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-common\src\main\java\com\sky\utils\BaiduGeoUtil.java#L23-L133) 实例来调用这些方法，而不是直接调用静态方法。**
  >    
  >    - 此外，**确保你在使用 BaiduGeoUtil 的地方是通过Spring注入的，而不是使用 new 关键字创建实例**
  >    
  >      > 这个问题涉及Spring框架的核心概念：
  >      >
  >      > ## 为什么必须通过Spring注入而不是使用`new`关键字
  >      >
  >      > 1. **依赖注入机制**
  >      >    - Spring框架通过`@Autowired`等注解自动管理对象的生命周期和依赖关系
  >      >    - 当你使用`new`关键字创建实例时，绕过了Spring容器的管理机制
  >      >
  >      > 2. **注解失效问题**
  >      >    ```java
  >      >    // 错误方式 - 绕过Spring容器
  >      >    BaiduGeoUtil util = new BaiduGeoUtil();  // @Autowired不会生效
  >      >                                                    
  >      >    // 正确方式 - 通过Spring容器管理
  >      >    @Autowired
  >      >    private BaiduGeoUtil baiduGeoUtil;  // Spring会自动注入依赖
  >      >    ```
  >      >
  >      >
  >      > 3. **具体到你的代码**
  >      >    - `BaiduGeoUtil`类上的`@Component`注解使其成为Spring管理的Bean
  >      >    - `baiduGeoUtil`字段上的`@Autowired`注解需要Spring容器来解析和注入
  >      >    - 在`OrderServiceImpl`中你正确地使用了`@Autowired`注入`baiduGeoUtil`
  >      >
  >      > 4. **根本原因**
  >      >    - 只有Spring管理的Bean才能享受依赖注入、AOP代理等Spring特性
  >      >    - 手动创建的实例不会触发Spring的初始化过程，导致`@Autowired`字段为`null`
  >      >
  >      > 因此，通过Spring注入而非`new`关键字创建实例是保证依赖注入正常工作的关键。
  >      >
  >      > 
  >      > 
  >
  > 这样修改后，[baiduGeoProperties](file://D:\ProjectOfBZH\sky_Project\sky-take-out\sky-common\src\main\java\com\sky\utils\BaiduGeoUtil.java#L28-L29) 就能被正确注入，不再为 `null`。

---

---

* knife4j没办法正常启动

* 错误原因：扫描的包没写对，访问路径写错

  ![image-20251015142632596](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251015142632596.png)

  当前服务运行在9090端口（application.yml文件中定义）

  ![image-20251015144012880](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251015144012880.png)

  

---

---

* **<u>情况：没有办法正常插入数据</u>**
* 考虑数据库建表错误，也可能导致没办法正常插入数据



---

---

* 情况：测试的时候前端发送的请求体没办法被后端DTO接受

* 原因：

  这个DTO类没有加上改加的注解，导致没有getter、setter方法

  这样即使controller层有@RequestBody注解也没办法正确封装

  ![image-20251019195007834](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251019195007834.png)

---

---

* **SpringBoot注解突然大面积爆红**

* 出错原因

  本次出错原因：

  ![image-20251022221532354](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251022221532354.png)

  > 当 SpringBoot 项目中的注解突然全部爆红时，通常是由于 Maven 配置、IDEA 缓存或依赖加载问题导致的。以下是常见原因及解决方法。
  >
  > 示例问题
  >
  > ```
  > @RestController
  > public class DemoController {
  >    @GetMapping("/demo") // 此处注解报红
  >    public String demo() {
  >        return "Hello, SpringBoot!";
  >    }
  > }
  > ```
  >
  > ![复制](data:image/svg+xml,%EF%BB%BF%3Csvg%20width%3D%2218%22%20height%3D%2219%22%20viewBox%3D%220%200%2018%2019%22%20fill%3D%22none%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%0D%0A%20%20%20%20%3Cpath%20d%3D%22M4.11363%203.54174L4.11153%205.16645V13.2054C4.11153%2014.579%205.19925%2015.6926%206.54102%2015.6926L12.982%2015.6929C12.751%2016.3615%2012.1281%2016.8406%2011.3958%2016.8406H6.54102C4.57998%2016.8406%202.99023%2015.2131%202.99023%2013.2054V5.16645C2.99023%204.41591%203.45927%203.77752%204.11363%203.54174ZM13.2688%201.53125C14.1977%201.53125%2014.9508%202.30219%2014.9508%203.25319V13.2022C14.9508%2014.1531%2014.1977%2014.9241%2013.2688%2014.9241H6.54102C5.6121%2014.9241%204.85907%2014.1531%204.85907%2013.2022V3.25319C4.85907%202.30219%205.6121%201.53125%206.54102%201.53125H13.2688ZM13.2688%202.67921H6.54102C6.23138%202.67921%205.98037%202.93619%205.98037%203.25319V13.2022C5.98037%2013.5192%206.23138%2013.7761%206.54102%2013.7761H13.2688C13.5784%2013.7761%2013.8295%2013.5192%2013.8295%2013.2022V3.25319C13.8295%202.93619%2013.5784%202.67921%2013.2688%202.67921Z%22%20fill%3D%22%23767676%22%20%2F%3E%0D%0A%3C%2Fsvg%3E)
  >
  > 1. **检查 Maven 配置和依赖**
  >
  > 确保 Maven 的 *settings.xml* 配置正确，并尝试更新依赖。
  >
  > ```
  > mvn clean install
  > ```
  >
  > ![复制](data:image/svg+xml,%EF%BB%BF%3Csvg%20width%3D%2218%22%20height%3D%2219%22%20viewBox%3D%220%200%2018%2019%22%20fill%3D%22none%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%0D%0A%20%20%20%20%3Cpath%20d%3D%22M4.11363%203.54174L4.11153%205.16645V13.2054C4.11153%2014.579%205.19925%2015.6926%206.54102%2015.6926L12.982%2015.6929C12.751%2016.3615%2012.1281%2016.8406%2011.3958%2016.8406H6.54102C4.57998%2016.8406%202.99023%2015.2131%202.99023%2013.2054V5.16645C2.99023%204.41591%203.45927%203.77752%204.11363%203.54174ZM13.2688%201.53125C14.1977%201.53125%2014.9508%202.30219%2014.9508%203.25319V13.2022C14.9508%2014.1531%2014.1977%2014.9241%2013.2688%2014.9241H6.54102C5.6121%2014.9241%204.85907%2014.1531%204.85907%2013.2022V3.25319C4.85907%202.30219%205.6121%201.53125%206.54102%201.53125H13.2688ZM13.2688%202.67921H6.54102C6.23138%202.67921%205.98037%202.93619%205.98037%203.25319V13.2022C5.98037%2013.5192%206.23138%2013.7761%206.54102%2013.7761H13.2688C13.5784%2013.7761%2013.8295%2013.5192%2013.8295%2013.2022V3.25319C13.8295%202.93619%2013.5784%202.67921%2013.2688%202.67921Z%22%20fill%3D%22%23767676%22%20%2F%3E%0D%0A%3C%2Fsvg%3E)
  >
  > 如果依赖仍未加载，检查本地仓库是否缺少相关 JAR 包，必要时手动添加依赖并重新下载。
  >
  > ```
  > <dependency>
  >    <groupId>org.springframework.boot</groupId>
  >    <artifactId>spring-boot-starter-web</artifactId>
  >    <version>2.5.4</version>
  > </dependency>
  > ```
  >
  > 
  >
  > 2. **清理 IDEA 缓存**
  >
  > IDEA 缓存可能导致注解显示错误。尝试以下步骤：
  >
  > - 删除 *.idea* 文件夹和 *.iml* 文件。
  > - 在 IDEA 中执行 **File > Invalidate Caches / Restart**，然后重启项目。
  >
  > 3. **检查 JDK 和 SpringBoot 版本兼容性**
  >
  > 如果项目升级了 JDK 或 SpringBoot 版本，可能会引发兼容性问题。确保使用的 JDK 和 SpringBoot 版本匹配。例如：
  >
  > - JDK 8 对应 SpringBoot 2.1.x。
  > - JDK 11 对应 SpringBoot 2.3.x 或更高版本。
  >
  > 4. **修复注解高亮问题**
  >
  > 如果注解仍然报红，可以尝试以下操作：
  >
  > - 将鼠标悬停在报红的注解上，按 *Alt + Enter*，选择 **Add dependency** 或 **Cancel Highlighting**。
  > - 确保 IDEA 的 External Libraries 中正确加载了相关依赖。
  >
  > 通过以上方法，可以快速定位并解决 SpringBoot 项目中注解爆红的问题。

  > 根据你提供的信息和代码，出现`String`、注解（如`@Data`）以及JDK内置类爆红的问题，通常是由于IDE或项目配置层面的错误导致的。以下是可能的原因和解决办法：
  >
  > ### 1. **JDK配置问题**
  > - **现象**：`String` 等JDK核心类无法识别。
  > - **原因**：项目未正确配置JDK，或者JDK损坏。
  > - **解决办法**：
  >   - 检查IDE中的JDK配置是否正确：
  >     - 在IntelliJ IDEA中：`File -> Project Structure -> SDKs`，确认JDK路径是否正确。
  >     - 确保项目的`Project SDK`和`Module SDK`都设置为有效的JDK版本（如JDK 8、11或17）。
  >
  > ### 2. **Lombok插件未启用或配置错误**
  > - **现象**：Lombok注解（如 `@Data`, `@Builder`）爆红。
  > - **原因**：IDE未启用Lombok插件，或者注解处理未开启。
  > - **解决办法**：
  >   - 确保已安装并启用Lombok插件：
  >     - 在IntelliJ IDEA中：`File -> Settings -> Plugins`，搜索Lombok并安装/启用。
  >   - 启用注解处理：
  >     - `File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors`，勾选“Enable annotation processing”。
  >
  > ### 3. **Maven/Gradle依赖问题**
  > - **现象**：依赖类（如Lombok）无法识别。
  > - **原因**：项目依赖未正确加载或版本冲突。
  > - **解决办法**：
  >   - Maven项目：
  >     ```bash
  >     mvn clean install
  >     ```
  >
  >     或在IDE中重新导入Maven项目（右侧Maven工具栏 -> Refresh）。
  >   - Gradle项目：
  >     ```bash
  >     ./gradlew clean build
  >     ```
  >
  >     或在Gradle工具栏中刷新项目。
  >
  > ### 4. **IDE缓存问题**
  > - **现象**：所有类都爆红，但实际代码无误。
  > - **原因**：IDE缓存损坏。
  > - **解决办法**：
  >   - 清除IDE缓存并重启：
  >     - IntelliJ IDEA：`File -> Invalidate Caches / Restart -> Invalidate and Restart`。
  >
  > ### 5. **项目结构或模块配置错误**
  > - **现象**：部分模块或类路径无法识别。
  > - **原因**：模块未正确标记为`Sources Root`或依赖未正确添加。
  > - **解决办法**：
  >   - 检查项目结构：
  >     - `File -> Project Structure -> Modules`，确认源码目录（如`src/main/java`）已标记为`Sources`。
  >   - 确认依赖模块是否正确引入。
  >
  > ### 6. **编码或字符集问题**
  > - **现象**：部分符号或注释显示异常。
  > - **原因**：文件编码与IDE设置不一致。
  > - **解决办法**：
  >   - 设置统一编码格式为UTF-8：
  >     - `File -> Settings -> Editor -> File Encodings`，将`Global Encoding`和`Project Encoding`设置为UTF-8。
  >
  > ---
  >
  > 如果以上方法都无法解决问题，请提供更详细的错误信息（如IDE提示的具体错误内容），以便进一步排查。

---

---

* 为什么我在knife4j发送请求之后idea没有打印日志（并且没进入controller层），浏览器就显示返回失败

* 错误原因：

  

  > 根据你提供的信息，你在使用 knife4j 发送请求时，IDEA 没有打印日志，且浏览器显示返回失败。以下是可能的原因和排查建议：
  >
  > ## 可能原因及解决方案
  >
  > 1. **路径冲突**
  >    - **你定义了两个相似的 `@GetMapping` 接口：**
  >      
  >      - `@GetMapping("/{id}")` 使用 `Long id`
  >      - `@GetMapping("/{slug}")` 使用 `String slug`
  >    - Spring 无法区分这两个路径，可能导致请求没有正确映射到 [getArticleDetail](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-server\src\main\java\top\hazenix\service\ArticleService.java#L15-L15) 方法。
  >    - **建议**：为这两个接口定义不同的路径，例如：
  >      ```java
  >      @GetMapping("/{id}")
  >      public Result getArticleDetail(@PathVariable Long id) { ... }
  >                                                             
  >      @GetMapping("/slug/{slug}")
  >      public Result getArticleDetail(@PathVariable String slug) { ... }
  >      ```
  >
  >
  > 2. **日志配置问题**
  >    - 确保 `log.info()` 被正确调用，检查你的日志配置文件（如 `application.yml` 或 `logback-spring.xml`）是否正确配置了日志级别和输出。
  >    - **建议**：确认 `top.hazenix.controller.user` 包的日志级别设置为 `INFO` 或更低。
  >
  > 3. **请求未到达控制器**
  >    - 如果请求路径冲突或参数类型不匹配，请求可能没有到达 `getArticleDetail` 方法，因此不会打印日志。
  >    - **建议**：使用调试工具在 `log.info()` 处设置断点，确认方法是否被调用。
  >
  > 4. **knife4j 配置问题**
  >    - 检查 knife4j 的接口文档是否正确生成了请求路径和参数。
  >    - **建议**：尝试直接在浏览器或 Postman 中手动发送请求，验证是否是 knife4j 的问题。
  >
  > 5. **服务端异常**
  >    - 如果 `articleService.getArticleDetail(id)` 抛出异常且未被捕获，可能导致请求失败。
  >    - **建议**：在方法中添加 `try-catch` 块，打印异常信息，或者查看服务端的完整日志。
  >
  > 通过以上步骤排查，应该能够找到问题所在并解决。

#### cannot serialise exception



* 出错原因

  保存到redis的对象没有implements serializable，导致进行默认的序列化时报错



#### **序列化/反序列化不一致**

* 解决办法

  

* 出错原因

  > 你遇到的问题是：
  >
  > > **第一次请求正常（走数据库），缓存写入 Redis；第二次请求从 Redis 读取，但返回的是 `null` 或数据异常。**
  >
  > 这是 Spring Cache + Redis 集成中非常典型的 **序列化/反序列化不一致** 或 **缓存值结构问题**。下面从几个关键角度帮你排查和解决。
  >
  > ------
  >
  > ### 🔍 一、最可能的原因：**Redis 序列化配置问题**
  >
  > #### ✅ 现象匹配：
  >
  > - 第一次：调用方法 → 返回 `Result<List<ArticleDetailVO>>` → 成功存入 Redis。
  > - 第二次：Spring Cache 尝试从 Redis 读取 → 反序列化失败 → 返回 `null`（或抛异常被静默处理）。
  >
  > #### 🧪 检查点：
  >
  > 1. **你是否自定义了 `RedisTemplate`？**
  >
  >    - 如果没有，Spring Boot 默认使用 `JdkSerializationRedisSerializer`。
  >    - 而你的返回值是 `Result` 对象（包含泛型 `List<ArticleDetailVO>`），<u>**泛型在 JDK 序列化中会丢失类型信息**，反序列化时可能变成 `LinkedHashMap` 或直接失败。</u>
  >
  > 2. **查看 Redis 中实际存储的内容：**
  >
  >    ```bash
  >    redis-cli
  >    KEYS *articlesCache*
  >    GET "articlesCache::your_key_here"
  >    ```
  >
  >    - 如果看到一堆乱码（如 `\xac\xed\x00\x05...`）→ 是 JDK 序列化。
  >    - 如果是 JSON 字符串 → 是 JSON 序列化。
  >
  > 3. **是否所有类都实现了 `Serializable`？**
  >
  >    - `Result`
  >    - `ArticleDetailVO`
  >    - 以及它们内部的所有字段类（如日期、嵌套对象等）
  >
  >    ❌ 如果有一个没实现，JDK 序列化会失败，缓存可能存成 `null` 或读取出错。
  >
  > ------
  >
  > ### 🔧 二、推荐解决方案：**统一使用 JSON 序列化**
  >
  > 这是最干净、可读、跨语言的方式，也**避免泛型丢失问题。**
  >
  > #### 步骤 1：配置 RedisTemplate 使用 JSON
  >
  > ```java
  > @Configuration
  > public class RedisConfig {
  > 
  >     @Bean
  >     public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
  >         RedisTemplate<String, Object> template = new RedisTemplate<>();
  >         template.setConnectionFactory(connectionFactory);
  > 
  >         // Key 使用 String 序列化
  >         template.setKeySerializer(new StringRedisSerializer());
  >         template.setHashKeySerializer(new StringRedisSerializer());
  > 
  >         // Value 使用 JSON 序列化
  >         Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
  >         ObjectMapper objectMapper = new ObjectMapper();
  >         objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
  >         objectMapper.activateDefaultTyping(
  >             LaissezFaireSubTypeValidator.instance,
  >             ObjectMapper.DefaultTyping.NON_FINAL,
  >             JsonTypeInfo.As.PROPERTY
  >         );
  >         jsonSerializer.setObjectMapper(objectMapper);
  > 
  >         template.setValueSerializer(jsonSerializer);
  >         template.setHashValueSerializer(jsonSerializer);
  >         template.afterPropertiesSet();
  > 
  >         return template;
  >     }
  > 
  >     // 同时配置 CacheManager 使用相同的序列化方式
  >     @Bean
  >     public CacheManager cacheManager(RedisConnectionFactory factory) {
  >         RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
  >                 .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
  >                 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
  >                 .entryTtl(Duration.ofHours(1)); // 设置默认过期时间
  > 
  >         return RedisCacheManager.builder(factory).cacheDefaults(config).build();
  >     }
  > }
  > ```
  >
  > > ⚠️ 注意：`GenericJackson2JsonRedisSerializer` 会保留类型信息（通过 `@class` 字段），能正确反序列化泛型。
  >
  > #### 步骤 2：确保你的类是标准 Java Bean
  >
  > - 有无参构造函数
  > - 有 getter/setter
  > - 不需要 `implements Serializable`
  >
  > 例如：
  >
  > ```java
  > public class Result<T> {
  >     private int code;
  >     private String msg;
  >     private T data;
  > 
  >     // 必须有无参构造
  >     public Result() {}
  > 
  >     // getter/setter...
  > }
  > 
  > public class ArticleDetailVO {
  >     private Long id;
  >     private String title;
  >     // ...
  >     public ArticleDetailVO() {}
  >     // getter/setter...
  > }
  > ```
  >
  > ------
  >
  > ### 🔍 三、其他可能原因排查
  >
  > #### 1. **缓存 key 生成问题**
  >
  > 你的 key 表达式：
  >
  > ```java
  > key = "#root.args[0].title+ '_' + #root.args[0].categoryId+ ..."
  > ```
  >
  > - 如果某个字段为 `null`，SpEL 会转成字符串 `"null"`，导致 key 不一致。
  > - 但这种情况通常表现为 **缓存未命中（重新查库）**，而不是读出 `null`。
  >
  > ✅ 建议：加日志打印 key：
  >
  > ```java
  > String cacheKey = articleListQuery.getTitle() + "_" + ...;
  > log.info("Cache key: {}", cacheKey);
  > ```
  >
  > #### 2. **方法内修改了参数（影响一致性）**
  >
  > ```java
  > articleListQuery.setStatus(0); // ⚠️ 修改了传入参数！
  > ```
  >
  > - 第一次调用时，你强制把 `status` 设为 `0`。
  > - 但缓存 key 是基于原始参数（可能 `status=null` 或其他值）生成的。
  > - 下次相同原始参数进来，key 相同，但你又设 `status=0`，逻辑混乱。
  >
  > ✅ **修复建议**：不要修改入参！创建副本或在 Service 层处理。
  >
  > ```java
  > @GetMapping
  > @Cacheable(...)
  > public Result getArticleList(ArticleListQuery query) {
  >     // 创建副本，避免污染原始参数
  >     ArticleListQuery safeQuery = new ArticleListQuery();
  >     BeanUtils.copyProperties(query, safeQuery);
  >     safeQuery.setStatus(0); // 用户端只看已发布文章
  >     List<ArticleDetailVO> list = articleService.getArticleList(safeQuery);
  >     return Result.success(list);
  > }
  > ```
  >
  > > 否则：缓存 key 和实际查询条件不一致，可能导致错误复用缓存！
  >
  > #### 3. **Result 类被 Spring AOP 代理问题（较少见）**
  >
  > - 如果 `Result` 是内部类或动态代理类，可能无法正确序列化。
  > - 解决方案：使用标准 POJO。
  >
  > ------
  >
  > ### ✅ 最终检查清单
  >
  > | 检查项                                                       | 是否完成 |
  > | ------------------------------------------------------------ | -------- |
  > | ✅ 自定义了 `RedisTemplate` 和 `CacheManager` 使用 JSON 序列化 | ☐        |
  > | ✅ `Result` 和 `ArticleDetailVO` 有无参构造 + getter/setter   | ☐        |
  > | ✅ 不再依赖 `Serializable`                                    | ☐        |
  > | ✅ 不修改入参 `articleListQuery`，而是用副本                  | ☐        |
  > | ✅ Redis 中缓存值是可读 JSON，不是乱码                        | ☐        |
  >
  > ------
  >
  > ### 💡 补充：如何验证缓存是否生效？
  >
  > 1. 第一次访问：日志打印 “获取文章列表” → 说明走方法体。
  >
  > 2. 第二次访问：**没有日志** → 说明走缓存。
  >
  > 3. 查看 Redis 数据是否为合法 JSON：
  >
  >    ```json
  >    {
  >      "@class": "com.example.Result",
  >      "code": 200,
  >      "msg": "success",
  >      "data": [
  >        {
  >          "@class": "com.example.ArticleDetailVO",
  >          "id": 1,
  >          "title": "Hello"
  >        }
  >      ]
  >    }
  >    ```
  >
  > 如果看到 `@class` 字段，说明 `GenericJackson2JsonRedisSerializer` 生效。
  >
  > ------
  >
  > 如果你按照上述配置仍有问题，可以贴出：
  >
  > - `Result` 类定义
  > - `ArticleDetailVO` 类定义
  > - Redis 中实际缓存的值（截图或文本）
  >
  > 我可以进一步帮你分析。



#### mapper层报 no getter 

**nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'query' in 'class com.zeroone.star.project.query.j8.finance.tradeinvoice.TradeInvoiceQuery**

* 错误原因

  > #### 情况一：你直接传了 `TradeInvoiceQuery` 对象，但在 XML 里却用 `query.xxx`
  >
  > 比如你的 Mapper 方法是：
  >
  > ```
  > List<TradeInvoiceVO> selectInvoiceWithConditions(TradeInvoiceQuery query);
  > ```
  >
  > 然后在 XML 中写了：
  >
  > ```
  > <select id="selectInvoiceWithConditions" resultType="...">
  >     SELECT * FROM invoice
  >     WHERE supplier LIKE #{query.supplier}
  > </select>
  > ```
  >
  > ❌ **这是错的！**
  >
  > 因为此时 MyBatis 把整个 `TradeInvoiceQuery` 对象当作根参数，**它的属性就是 `supplier`、`customer` 等**，而不是嵌套在 `query` 里面的。
  >
  > 所以你应该写成：
  >
  > ```
  > #{supplier}
  > ```
  >
  > 而不是：
  >
  > ```
  > #{query.supplier}  <!-- ❌ 找不到 query 属性 -->
  > ```
  >
  > ------
  >
  > #### 情况二

* 解决直接引用里面的属性——#{id}
  而不是#{query.id}



#### 缓存与数据库不一致

* 问题：为什么新增评论之后文章id为43的文章的redis中的缓存没有被删除，导致查询到的评论还是之前的评论列表

  ```
  /**
       * 获取评论列表(用户端)
       */
      @GetMapping("/list")
      @Cacheable(cacheNames = "commentsCache", key = "#root.args[0].articleId+ '_' + #root.args[0].parentId+ '_' + #root.args[0].status+ '_' + #root.args[0].content+ '_' + #root.args[0].username + '_' + #root.args[0].replyId")
      public Result<List<CommentsVO>> getCommentsList(CommentsDTO commentsDTO){
          log.info("获取评论列表:{}",commentsDTO);
          List<CommentsVO> commentTree = commentsService.getCommentsList(commentsDTO);
          return Result.success(commentTree);
      }
  
      /**
       * 用户新增评论
       * @param commentsDTO
       * @return
       */
      @PostMapping
      @CacheEvict(cacheNames = "commentsCache", key = "#root.args[0].articleId+ '_' + #root.args[0].parentId+ '_' + #root.args[0].status+ '_' + #root.args[0].content+ '_' + #root.args[0].username + '_' + #root.args[0].replyId")
      public Result addComment(@RequestBody CommentsDTO commentsDTO){
          log.info("用户新增评论：{}",commentsDTO);
          commentsService.addComments(commentsDTO);
          return Result.success();
      }
  ```

* 此处出错原因：删除的key和查询的时候使用的key不一致，导致没有正确删除旧缓存

* 解决方案

  > 根据你提供的代码，我们来分析一下为什么新增评论后文章 ID 为 43 的 Redis 缓存没有被清除。
  >
  > ## 问题分析
  >
  > 1. **缓存键不一致**
  >    - 在 [getCommentsList](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-server\src\main\java\top\hazenix\service\CommentsService.java#L18-L18) 方法中使用的缓存键是：
  >      ```java
  >      #root.args[0].articleId+ '_' + #root.args[0].parentId+ '_' + #root.args[0].status+ '_' + #root.args[0].content+ '_' + #root.args[0].username + '_' + #root.args[0].replyId
  >      ```
  >
  >    - 而在 [addComment](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-server\src\main\java\top\hazenix\controller\user\CommentsController.java#L45-L51) 方法中清除缓存时也使用了相同的键表达式。
  >
  > 2. **可能的原因**
  >
  >    - 当调用 [addComment](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-server\src\main\java\top\hazenix\controller\user\CommentsController.java#L45-L51) 接口时，传入的 [CommentsDTO](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\dto\CommentsDTO.java#L7-L22) 对象中某些字段（如 [parentId](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\CommentsVO.java#L23-L23), [status](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\dto\LinkDTO.java#L16-L16), [content](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\CommentsVO.java#L31-L31), [username](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\UserVO.java#L18-L18), [replyId](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\CommentsVO.java#L38-L38)）与之前查询时传入的参数不同，导致计算出的缓存键不同。
  >    - 特别地，如果这些字段中有任何一个在添加评论时为空或者默认值，而在查询时有具体值，则会导致两个操作对应不同的缓存键。
  >
  > 3. **解决方案建议**
  >
  >    - **优化缓存键设计**：由于添加评论通常会影响整个文章的评论列表，可以考虑简化缓存键，仅基于 [articleId](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\CommentsVO.java#L33-L33) 来进行缓存管理。例如：
  >
  >      ```java
  >      @Cacheable(cacheNames = "commentsCache", key = "#commentsDTO.articleId")
  >      public Result<List<CommentsVO>> getCommentsList(CommentsDTO commentsDTO){ ... }
  >                                                   
  >      @CacheEvict(cacheNames = "commentsCache", key = "#commentsDTO.articleId")
  >      public Result addComment(@RequestBody CommentsDTO commentsDTO){ ... }
  >      ```
  >
  >
  >    - **统一缓存策略**：确保所有涉及同一资源的操作都使用一致的缓存键规则，避免因为参数差异造成缓存未命中或清理失败的问题。
  >
  > 综上所述，当前实现中复杂的缓存键组合可能是导致缓存未能正确清除的根本原因。建议调整为更简单的、以 [articleId](file://D:\ProjectOfBZH\HazenixBlogProject\blog-hazenix-top\blog-pojo\src\main\java\top\hazenix\vo\CommentsVO.java#L33-L33) 为核心的缓存策略。



#### java: 找不到符号  符号:   变量 log


[IDEA中 java: 找不到符号 符号: 变量 log背景 在运行某一个项目时候，命名已经采用@Slf4j注释，调用l - 掘金](https://juejin.cn/post/7522476555772919823)

* 本次出错上面文章的方法没用，直接使缓存失效、然后重新打开项目、加jdk 导入模块后就能启动了





#### Java版本和MP版本冲突

* 在微服务项目的**服务调用(OpenFeign)**中 出现该bug

* 报错信息

  > java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.lang.Class 

* 出错原因：高版本java不允许通过反射访问jdk内部的私有字段

  > 这个错误是 **Java 9+ 模块系统（JPMS）** 导致的典型问题。MyBatis-Plus 在解析 Lambda 表达式时，需要通过反射访问 JDK 内部类 `java.lang.invoke.SerializedLambda` 的私有字段，但 Java 9 及以上的模块系统默认禁止了这种访问。
  >
  > ##### 问题分析     
  >
  > 从堆栈信息可以看到：
  > - 错误发生在 `com.baomidou.mybatisplus.core.toolkit.ReflectionKit.setAccessible`
  > - 试图访问 `java.lang.invoke.SerializedLambda.capturingClass` 字段
  > - 模块 `java.base` 没有将 `java.lang.invoke` 包开放给未命名模块
  >
  > ##### 解决方案
  >
  > 你需要在 JVM 启动参数中添加 `--add-opens` 来开放这些模块。有以下几种方式：
  >
  > ##### 方式一：IDEA 运行配置（临时方案）
  >
  > 在 IntelliJ IDEA 中：
  > 1. 打开 **Run/Debug Configurations**
  > 2. 找到你的 Spring Boot 启动类配置
  > 3. 在 **VM options** 中添加：
  >    ```bash
  >    --add-opens java.base/java.lang.invoke=ALL-UNNAMED
  >    ```
  >
  > ##### 方式二：Maven 配置（推荐）
  >
  > 在 `pom.xml` 的 `spring-boot-maven-plugin` 插件中配置：
  >
  > ```xml
  > <build>
  >     <plugins>
  >         <plugin>
  >             <groupId>org.springframework.boot</groupId>
  >             <artifactId>spring-boot-maven-plugin</artifactId>
  >             <configuration>
  >                 <jvmArguments>
  >                     --add-opens java.base/java.lang.invoke=ALL-UNNAMED
  >                 </jvmArguments>
  >             </configuration>
  >         </plugin>
  >     </plugins>
  > </build>
  > ```
  >
  > ##### 方式三：Gradle 配置
  >
  > 在 `build.gradle` 中：
  >
  > ```gradle
  > bootRun {
  >     jvmArgs '--add-opens', 'java.base/java.lang.invoke=ALL-UNNAMED'
  > }
  > ```
  >
  > ##### 方式四：Docker 环境
  >
  > 如果在 Docker 中运行，在 `Dockerfile` 或启动脚本中：
  >
  > ```dockerfile
  > ENV JAVA_OPTS="--add-opens java.base/java.lang.invoke=ALL-UNNAMED"
  > ```
  >
  > ##### 方式五：直接命令行运行
  >
  > ```bash
  > java --add-opens java.base/java.lang.invoke=ALL-UNNAMED -jar your-app.jar
  > ```
  >
  > ##### 补充说明
  >
  > 如果你使用的是 **Java 17+**，可能还需要添加更多开放模块。建议一次性添加以下常用参数：
  >
  > ```bash
  > --add-opens java.base/java.lang.invoke=ALL-UNNAMED
  > --add-opens java.base/java.lang=ALL-UNNAMED
  > --add-opens java.base/java.util=ALL-UNNAMED
  > --add-opens java.base/java.time=ALL-UNNAMED
  > ```
  >
  > 添加后重新启动服务即可解决问题。

* 解决方式——启动的配置中加几个jvm参数

  ```
  --add-opens java.base/java.lang.invoke=ALL-UNNAMED
  --add-opens java.base/java.lang=ALL-UNNAMED
  --add-opens java.base/java.util=ALL-UNNAMED
  --add-opens java.base/java.time=ALL-UNNAMED
  ```



#### 依赖爆红

* 现象：一个模块已经引入了common，common里面有lombok依赖，@Data还爆红

* 可能问题：IDEA缓存问题；

  ![image-20251216120426257](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251216120426257.png)
  添加一下，然后去pom文件里面删掉，然后刷新pom文件



#### 注入对象爆红

* 本次问题：

  IDE 的 Maven 索引没有同步导致的，不是代码问题。

  试试这几步：

  IntelliJ IDEA 右侧 Maven 面板 → 点击刷新按钮（Reload All Maven Projects）
  或者菜单 File → **Invalidate Caches / Restart (通过这个解决)**









#### 配置问题



##### 读取不到application-dev.yml

可能是缩进搞错了，导致application-dev.yml没被激活

建议：直接在启动命令/IDE VM options 补上：--spring.profiles.active=dev 或环境变量 SPRING_PROFILES_ACTIVE=dev，確認啟動日誌出現 Active profiles: dev。









#### 启动报错-

##### ClassNotFoundException

* 出错原因：依赖引入不足

  > 错误的根本原因是：
  >
  > java.lang.ClassNotFoundException: org.springframework.dao.support.DaoSupport，即找不到 DaoSupport 类。这个类属于 Spring 的数据访问模块（spring-tx），但在你的 user-service 模块中缺少相关依赖。
  > 从你的 user-service/pom.xml 文件可以看出，虽然引入了 MyBatis Plus 相关依赖，但没有引入 Spring 的事务管理依赖（spring-tx）或者 JDBC 相关依赖，而这些是 MyBatis Plus 正常工作所必需的。





##### 无法注册bean

* ![image-20251207093029437](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251207093029437.png)

* 出错原因：
  这个错误信息表明你的 Spring Boot 应用程序在启动时遇到了 Bean 定义冲突的问题。具体来说，名为 item-service.FeignClientSpecification 的 bean 已经被定义了，而又试图重新注册同名的 bean，但 Spring 默认不允许覆盖已存在的 bean 定义。

  > 本次是因为我UserClient注解里面的name复制之后没改，还是item-service![image-20251207093220252](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251207093220252.png)





##### 缺少依赖

![image-20251207093836819](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251207093836819.png)

* 这种异常一般是缺依赖了



##### BeanCreationException











##### BeanDefinitionStoreException



* 本次出错原因：2.2.5的SpringBoot不支持jdk17

* > 2025-12-11 18:40:32.747  INFO 54084 --- [           main] com.louyy.zxxk.ZxxkApplication           : Starting ZxxkApplication on LAPTOP-AJ2CCTIC with PID 54084 (D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes started by 毕哲晖 in D:\ProjectOfBZH\zxg_zxxk\zxxk-api)
  > 2025-12-11 18:40:32.755  INFO 54084 --- [           main] com.louyy.zxxk.ZxxkApplication           : No active profile set, falling back to default profiles: default
  > 2025-12-11 18:40:33.121  WARN 54084 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanDefinitionStoreException: Failed to read candidate component class: file [D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes\com\louyy\zxxk\POJO\dto\CourseListView.class]; nested exception is org.springframework.core.NestedIOException: ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: file [D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes\com\louyy\zxxk\POJO\dto\CourseListView.class]; nested exception is java.lang.IllegalArgumentException: Unsupported class file major version 60
  > 2025-12-11 18:40:33.142 ERROR 54084 --- [           main] o.s.boot.SpringApplication               : Application run failed
  >
  > org.springframework.beans.factory.BeanDefinitionStoreException: Failed to read candidate component class: file [D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes\com\louyy\zxxk\POJO\dto\CourseListView.class]; nested exception is org.springframework.core.NestedIOException: ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: file [D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes\com\louyy\zxxk\POJO\dto\CourseListView.class]; nested exception is java.lang.IllegalArgumentException: Unsupported class file major version 60
  > 	at org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.scanCandidateComponents(ClassPathScanningCandidateComponentProvider.java:454) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.findCandidateComponents(ClassPathScanningCandidateComponentProvider.java:316) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ClassPathBeanDefinitionScanner.doScan(ClassPathBeanDefinitionScanner.java:276) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ComponentScanAnnotationParser.parse(ComponentScanAnnotationParser.java:132) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:295) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:249) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:206) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:174) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:319) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:236) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:275) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:95) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:706) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:532) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:141) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:747) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1226) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1215) ~[spring-boot-2.2.5.RELEASE.jar:2.2.5.RELEASE]
  > 	at com.louyy.zxxk.ZxxkApplication.main(ZxxkApplication.java:14) ~[classes/:na]
  > Caused by: org.springframework.core.NestedIOException: ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: file [D:\ProjectOfBZH\zxg_zxxk\zxxk-api\target\classes\com\louyy\zxxk\POJO\dto\CourseListView.class]; nested exception is java.lang.IllegalArgumentException: Unsupported class file major version 60
  > 	at org.springframework.core.type.classreading.SimpleMetadataReader.getClassReader(SimpleMetadataReader.java:61) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.core.type.classreading.SimpleMetadataReader.<init>(SimpleMetadataReader.java:50) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.core.type.classreading.SimpleMetadataReaderFactory.getMetadataReader(SimpleMetadataReaderFactory.java:103) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.core.type.classreading.CachingMetadataReaderFactory.getMetadataReader(CachingMetadataReaderFactory.java:123) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.scanCandidateComponents(ClassPathScanningCandidateComponentProvider.java:430) ~[spring-context-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	... 20 common frames omitted
  > Caused by: java.lang.IllegalArgumentException: Unsupported class file major version 60
  > 	at org.springframework.asm.ClassReader.<init>(ClassReader.java:196) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.asm.ClassReader.<init>(ClassReader.java:177) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.asm.ClassReader.<init>(ClassReader.java:163) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.asm.ClassReader.<init>(ClassReader.java:284) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	at org.springframework.core.type.classreading.SimpleMetadataReader.getClassReader(SimpleMetadataReader.java:58) ~[spring-core-5.2.4.RELEASE.jar:5.2.4.RELEASE]
  > 	... 24 common frames omitted

  > 是的！**你遇到的问题确实是 JDK 版本不兼容导致的**，具体来说：
  >
  > > ❌ **你用高版本 JDK（如 JDK 16）编译了代码，但 Spring Boot 2.2.5（基于 Spring 5.2.4）不支持该版本的 class 文件。**
  >
  > ------
  >
  > #### 🔍 错误关键信息解析
  >
  > ```
  > Caused by: java.lang.IllegalArgumentException: Unsupported class file major version 60
  > ```
  >
  > - `major version 60` 对应的是 **Java 16**
  > - Spring Framework 5.2.x（你用的 `spring-context-5.2.4.RELEASE`）**最高只支持到 Java 13（major version 57）**
  > - Spring Boot 2.2.x 官方支持的 JDK 范围是 **Java 8 ~ Java 13**
  >
  > | Java 版本   | Class 文件 Major Version |
  > | ----------- | ------------------------ |
  > | Java 8      | 52                       |
  > | Java 11     | 55                       |
  > | Java 13     | 57                       |
  > | **Java 16** | **60** ✅（你的问题来源） |
  > | Java 17     | 61                       |
  >
  > > 📌 **Spring 5.2 不认识 Java 16 编译出的 `.class` 文件，所以启动失败。**
  >
  > ------
  >
  > #### ✅ 解决方案（任选其一）
  >
  > ------
  >
  > ##### ✅ 方案 1：**降级 JDK 版本（推荐）**
  >
  > 将项目使用的 JDK 改为 **JDK 8、11 或 13**（Spring Boot 2.2.5 官方支持）。
  >
  > ##### 操作步骤：
  >
  > 1. 安装 JDK 11
  >
  >    （推荐 LTS 版本）
  >
  >    - 下载地址：[Oracle JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 或 [OpenJDK 11](https://adoptium.net/)
  >
  > 2. 在 IDE 中设置 Project SDK 为 JDK 11
  >
  >    - IntelliJ IDEA：`File > Project Structure > Project Settings > Project > SDK`
  >    - Eclipse：`Project > Properties > Java Build Path > Libraries`
  >
  > 3. 清理并重新编译项目
  >
  >    ```bash
  >    mvn clean compile
  >    # 或
  >    ./mvnw clean compile
  >    ```
  >
  > 4. 重新运行 `ZxxkApplication`
  >
  > ------
  >
  > ✅ 方案 2：**升级 Spring Boot 版本（长期推荐）**
  >
  > 如果你必须使用 **JDK 16+**，那就需要升级 Spring Boot 到支持高版本 JDK 的版本：
  >
  > | JDK 版本 | 最低 Spring Boot 版本                   |
  > | -------- | --------------------------------------- |
  > | JDK 16   | Spring Boot **2.4.0+**                  |
  > | JDK 17   | Spring Boot **2.5.0+**（官方 LTS 支持） |
  >
  > ##### 升级步骤（以升级到 Spring Boot 2.7.x 为例）：
  >
  > 1. 修改 
  >
  >    ```
  >    pom.xml
  >    ```
  >
  >    ：
  >
  >    ```xml
  >    <parent>
  >        <groupId>org.springframework.boot</groupId>
  >        <artifactId>spring-boot-starter-parent</artifactId>
  >        <version>2.7.18</version> <!-- 支持 JDK 17 -->
  >        <relativePath/>
  >    </parent>
  >    ```
  >
  > 2. 确保 JDK ≥ 17（如果用 2.7.x）
  >
  > 3. 清理并重编译：
  >
  >    ```bash
  >    mvn clean install
  >    ```
  >
  > > ⚠️ 注意：升级 Spring Boot 可能带来 API 变更，需测试兼容性。
  >
  > ------
  >
  > ✅ 方案 3：**强制指定编译版本（临时 workaround）**
  >
  > 即使你本地装的是 JDK 16，也可以让 Maven **用兼容的字节码版本编译**。
  >
  > 在 `pom.xml` 中添加：
  >
  > ```xml
  > <properties>
  >     <java.version>11</java.version>
  >     <maven.compiler.source>11</maven.compiler.source>
  >     <maven.compiler.target>11</maven.compiler.target>
  > </properties>
  > ```
  >
  > 然后执行：
  >
  > ```bash
  > mvn clean compile
  > ```
  >
  > > ✅ 这样生成的 `.class` 文件就是 **major version 55（Java 11）**，Spring 5.2 就能识别了。
  >
  > ------
  >
  > #### 🔧 验证当前 JDK 和编译版本
  >
  > 1. 查看当前 JDK：
  >
  >    ```bash
  >    java -version
  >    javac -version
  >    ```
  >
  > 2. 查看某个 `.class` 文件的版本（如 `CourseListView.class`）：
  >
  >    ```bash
  >    javap -v CourseListView.class | findstr "major"  # Windows
  >    javap -v CourseListView.class | grep "major"     # Linux/macOS
  >    ```
  >
  > ------
  >
  > #### ✅ 总结
  >
  > | 问题原因 | JDK 16 编译 → class version 60 → Spring 5.2 不支持  |
  > | -------- | --------------------------------------------------- |
  > | 推荐解决 | **使用 JDK 11 + Spring Boot 2.2.5**（匹配官方支持） |
  > | 长期建议 | **升级到 Spring Boot 2.7+ + JDK 17**（现代标准）    |
  >
  > > 💡 **最佳实践**：  
  > >
  > > - 老项目（Spring Boot 2.2）→ 用 **JDK 8 或 11**  
  > > - 新项目 → 用 **Spring Boot 3.x + JDK 17+**
  >
  > 你现在只需要 **切换 JDK 到 11 并重新编译**，问题就能立即解决！













#### jdk相关-无效的目标发行版: 11



* 出错原因

  > ### 根本问题：JDK 版本不匹配
  >
  > "无效的目标发行版: 11" 表示 Maven 想要用 Java 11 编译，但你的**实际 JDK 版本低于 11**（很可能是 JDK 8）。
  >
  > #### **1. 检查当前 JDK 版本**
  >
  > 打开终端，运行：
  >
  > ```bash
  > java -version
  > ```
  >
  > ```bash
  > mvn -v  # 查看 Maven 使用的 JDK
  > ```
  >
  > **如果输出是 1.8.x**，那就是 JDK 8，**必须升级到 JDK 11+**。
  >
  > ------
  >
  > #### **2. 检查项目配置（pom.xml）**
  >
  > 查看 `pom.xml` 中是否有以下配置：
  >
  > ```xml
  > <properties>
  >     <maven.compiler.source>11</maven.compiler.source>
  >     <maven.compiler.target>11</maven.compiler.target>
  > </properties>
  > ```
  >
  > 或
  >
  > ```xml
  > <build>
  >     <plugins>
  >         <plugin>
  >             <groupId>org.apache.maven.plugins</groupId>
  >             <artifactId>maven-compiler-plugin</artifactId>
  >             <configuration>
  >                 <source>11</source>
  >                 <target>11</target>
  >             </configuration>
  >         </plugin>
  >     </plugins>
  > </build>
  > ```
  >
  > **如果配置了 Java 11，但你本地是 JDK 8，就会报错！**
  >
  > ------
  >
  > ### 解决方案（按优先级）
  >
  > #### **方案一：升级本地 JDK 到 11+（推荐）**
  >
  > 这是最根本的解决方法，因为项目要求 Java 11。
  >
  > 1. **下载 JDK 11**：
  >
  >    - [OpenJDK 11](https://adoptium.net/temurin/releases/?version=11)
  >    - 或 [Oracle JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
  >
  > 2. **安装后配置环境变量**：
  >
  >    ```bash
  >    # Windows
  >    set JAVA_HOME=C:\Program Files\Java\jdk-11.0.x
  >    set PATH=%JAVA_HOME%\bin;%PATH%
  >                      
  >    # Mac/Linux
  >    export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.x.jdk/Contents/Home
  >    export PATH=$JAVA_HOME/bin:$PATH
  >    ```
  >
  >    
  >
  > 3. **验证**：
  >
  >    ```bash
  >    java -version  # 应该显示 11.0.x
  >    mvn -v         # Maven 应该识别到新 JDK
  >    ```
  >
  >    
  >
  > ------
  >
  > #### **方案二：临时降级项目配置（不推荐）**
  >
  > 如果你暂时无法升级 JDK，可以**临时**修改 `pom.xml` 使用 Java 8：
  >
  > xml
  >
  > 复制
  >
  > ```xml
  > <properties>
  >     <maven.compiler.source>8</maven.compiler.source>
  >     <maven.compiler.target>8</maven.compiler.target>
  > </properties>
  > ```
  >
  > **注意**：这可能导致 MyBatis-Plus 等新特性不兼容，仅作临时测试用。



* 修改全局jdk版本

  > 备注：除了修改项目jdk版本，还需要修改sdk版本
  >
  > ![image-20251207184601961](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251207184601961.png)

* 修改maven运行用的jdk版本
  ![image-20251207184742632](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251207184742632.png)

  * 如果修改之后仍然不行：重启IDEA

    (查看maven实际使用的jdk版本：`mvn -v`)












# Mybatis系列



#### 未自定义map类型属性的映射

> 重点看`cause by`
>
> 
>
> ```bash
> 2026-01-23T20:21:51.969+08:00 ERROR 89948 --- [haze-ai-hub] [ient-2-Worker-0] t.h.h.s.i.BailianThinkingServiceProImpl  : 保存会话历史失败: null
> 
> org.mybatis.spring.MyBatisSystemException: null
> 	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:97) ~[mybatis-spring-3.0.3.jar:3.0.3]
> 	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:439) ~[mybatis-spring-3.0.3.jar:3.0.3]
> 	at jdk.proxy2/jdk.proxy2.$Proxy86.insert(Unknown Source) ~[na:na]
> 	at org.mybatis.spring.SqlSessionTemplate.insert(SqlSessionTemplate.java:272) ~[mybatis-spring-3.0.3.jar:3.0.3]
> 	at com.baomidou.mybatisplus.core.override.MybatisMapperMethod.execute(MybatisMapperMethod.java:59) ~[mybatis-plus-core-3.5.7.jar:3.5.7]
> 	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy$PlainMethodInvoker.invoke(MybatisMapperProxy.java:152) ~[mybatis-plus-core-3.5.7.jar:3.5.7]
> 	at com.baomidou.mybatisplus.core.override.MybatisMapperProxy.invoke(MybatisMapperProxy.java:89) ~[mybatis-plus-core-3.5.7.jar:3.5.7]
> 	at jdk.proxy2/jdk.proxy2.$Proxy88.insert(Unknown Source) ~[na:na]
> 	at top.hazenix.hazeaihub.service.impl.BailianThinkingServiceProImpl.saveMessagesToDatabase(BailianThinkingServiceProImpl.java:284) ~[classes/:na]
> 	at top.hazenix.hazeaihub.service.impl.BailianThinkingServiceProImpl.lambda$chatWithThinking$6(BailianThinkingServiceProImpl.java:206) ~[classes/:na]
> 	at reactor.core.publisher.FluxPeekFuseable$PeekFuseableConditionalSubscriber.onComplete(FluxPeekFuseable.java:587) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxPeekFuseable$PeekFuseableConditionalSubscriber.onComplete(FluxPeekFuseable.java:595) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxHandleFuseable$HandleFuseableConditionalSubscriber.onComplete(FluxHandleFuseable.java:605) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onComplete(FluxFilterFuseable.java:391) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onComplete(FluxMapFuseable.java:350) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFilterFuseable$FilterFuseableConditionalSubscriber.onComplete(FluxFilterFuseable.java:391) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlatMap$FlatMapMain.checkTerminated(FluxFlatMap.java:850) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlatMap$FlatMapMain.drainLoop(FluxFlatMap.java:612) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlatMap$FlatMapMain.drain(FluxFlatMap.java:592) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlatMap$FlatMapMain.onComplete(FluxFlatMap.java:469) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.MonoFlatMapMany$FlatMapManyInner.onComplete(MonoFlatMapMany.java:261) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.Operators$MultiSubscriptionSubscriber.onComplete(Operators.java:2231) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxPublish$PublishSubscriber.checkTerminated(FluxPublish.java:634) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxPublish$PublishSubscriber.drain(FluxPublish.java:494) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxPublish$PublishSubscriber.onComplete(FluxPublish.java:355) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onComplete(FluxContextWrite.java:126) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxMapFuseable$MapFuseableConditionalSubscriber.onComplete(FluxMapFuseable.java:350) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxOnAssembly$OnAssemblySubscriber.onComplete(FluxOnAssembly.java:549) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlattenIterable$FlattenIterableSubscriber.drainAsync(FluxFlattenIterable.java:371) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlattenIterable$FlattenIterableSubscriber.drain(FluxFlattenIterable.java:724) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.core.publisher.FluxFlattenIterable$FlattenIterableSubscriber.onComplete(FluxFlattenIterable.java:273) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at reactor.adapter.JdkFlowAdapter$SubscriberToRS.onComplete(JdkFlowAdapter.java:160) ~[reactor-core-3.6.13.jar:3.6.13]
> 	at java.net.http/jdk.internal.net.http.ResponseSubscribers$PublishingBodySubscriber.complete(ResponseSubscribers.java:930) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.ResponseSubscribers$PublishingBodySubscriber.lambda$new$1(ResponseSubscribers.java:861) ~[java.net.http:na]
> 	at java.base/java.util.concurrent.CompletableFuture.uniAcceptNow(CompletableFuture.java:757) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture.uniAcceptStage(CompletableFuture.java:735) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture.thenAccept(CompletableFuture.java:2182) ~[na:na]
> 	at java.net.http/jdk.internal.net.http.ResponseSubscribers$PublishingBodySubscriber.lambda$new$2(ResponseSubscribers.java:861) ~[java.net.http:na]
> 	at java.base/java.util.concurrent.CompletableFuture.uniWhenComplete(CompletableFuture.java:863) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture$UniWhenComplete.tryFire(CompletableFuture.java:841) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:510) ~[na:na]
> 	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2147) ~[na:na]
> 	at java.net.http/jdk.internal.net.http.ResponseSubscribers$PublishingBodySubscriber.signalComplete(ResponseSubscribers.java:947) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.ResponseSubscribers$PublishingBodySubscriber.onComplete(ResponseSubscribers.java:1033) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Stream.schedule(Stream.java:201) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$LockingRestartableTask.run(SequentialScheduler.java:205) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$CompleteRestartableTask.run(SequentialScheduler.java:149) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$SchedulableTask.run(SequentialScheduler.java:230) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:303) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:256) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Stream.receiveDataFrame(Stream.java:337) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Stream.incoming(Stream.java:443) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection.processFrame(Http2Connection.java:812) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.frame.FramesDecoder.decode(FramesDecoder.java:155) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection$FramesController.processReceivedData(Http2Connection.java:232) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection.asyncReceive(Http2Connection.java:674) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection$Http2TubeSubscriber.processQueue(Http2Connection.java:1310) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$LockingRestartableTask.run(SequentialScheduler.java:205) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$CompleteRestartableTask.run(SequentialScheduler.java:149) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$SchedulableTask.run(SequentialScheduler.java:230) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:303) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:256) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection$Http2TubeSubscriber.runOrSchedule(Http2Connection.java:1328) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection$Http2TubeSubscriber.onNext(Http2Connection.java:1354) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.Http2Connection$Http2TubeSubscriber.onNext(Http2Connection.java:1288) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SSLTube$DelegateWrapper.onNext(SSLTube.java:210) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SSLTube$SSLSubscriberWrapper.onNext(SSLTube.java:492) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SSLTube$SSLSubscriberWrapper.onNext(SSLTube.java:295) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SubscriberWrapper$DownstreamPusher.run1(SubscriberWrapper.java:316) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SubscriberWrapper$DownstreamPusher.run(SubscriberWrapper.java:259) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$LockingRestartableTask.run(SequentialScheduler.java:205) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$CompleteRestartableTask.run(SequentialScheduler.java:149) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$SchedulableTask.run(SequentialScheduler.java:230) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:303) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler.runOrSchedule(SequentialScheduler.java:256) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SubscriberWrapper.outgoing(SubscriberWrapper.java:232) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SubscriberWrapper.outgoing(SubscriberWrapper.java:198) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SSLFlowDelegate$Reader.processData(SSLFlowDelegate.java:444) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SSLFlowDelegate$Reader$ReaderDownstreamPusher.run(SSLFlowDelegate.java:268) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$LockingRestartableTask.run(SequentialScheduler.java:205) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$CompleteRestartableTask.run(SequentialScheduler.java:149) ~[java.net.http:na]
> 	at java.net.http/jdk.internal.net.http.common.SequentialScheduler$SchedulableTask.run(SequentialScheduler.java:230) ~[java.net.http:na]
> 	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136) ~[na:na]
> 	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635) ~[na:na]
> 	at java.base/java.lang.Thread.run(Thread.java:842) ~[na:na]
> Caused by: org.apache.ibatis.exceptions.PersistenceException: 
> ### Error updating database.  Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'metadataJson'. It was either not specified and/or could not be found for the javaType (java.util.Map) : jdbcType (null) combination.
> ### The error may exist in top/hazenix/hazeaihub/mapper/ChatMessageMapper.java (best guess)
> ### The error may involve top.hazenix.hazeaihub.mapper.ChatMessageMapper.insert
> ### The error occurred while executing an update
> ### Cause: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'metadataJson'. It was either not specified and/or could not be found for the javaType (java.util.Map) : jdbcType (null) combination.
> 	at org.apache.ibatis.exceptions.ExceptionFactory.wrapException(ExceptionFactory.java:30) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:199) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.session.defaults.DefaultSqlSession.insert(DefaultSqlSession.java:184) ~[mybatis-3.5.16.jar:3.5.16]
> 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
> 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
> 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
> 	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
> 	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:425) ~[mybatis-spring-3.0.3.jar:3.0.3]
> 	... 97 common frames omitted
> Caused by: java.lang.IllegalStateException: Type handler was null on parameter mapping for property 'metadataJson'. It was either not specified and/or could not be found for the javaType (java.util.Map) : jdbcType (null) combination.
> 	at org.apache.ibatis.mapping.ParameterMapping$Builder.validate(ParameterMapping.java:117) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.mapping.ParameterMapping$Builder.build(ParameterMapping.java:104) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.builder.SqlSourceBuilder$ParameterMappingTokenHandler.buildParameterMapping(SqlSourceBuilder.java:146) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.builder.SqlSourceBuilder$ParameterMappingTokenHandler.handleToken(SqlSourceBuilder.java:89) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.parsing.GenericTokenParser.parse(GenericTokenParser.java:76) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.builder.SqlSourceBuilder.parse(SqlSourceBuilder.java:51) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.scripting.xmltags.DynamicSqlSource.getBoundSql(DynamicSqlSource.java:42) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.mapping.MappedStatement.getBoundSql(MappedStatement.java:320) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.executor.statement.BaseStatementHandler.<init>(BaseStatementHandler.java:65) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.executor.statement.PreparedStatementHandler.<init>(PreparedStatementHandler.java:42) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.executor.statement.RoutingStatementHandler.<init>(RoutingStatementHandler.java:47) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.session.Configuration.newStatementHandler(Configuration.java:719) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.executor.SimpleExecutor.doUpdate(SimpleExecutor.java:48) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.executor.BaseExecutor.update(BaseExecutor.java:117) ~[mybatis-3.5.16.jar:3.5.16]
> 	at org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:197) ~[mybatis-3.5.16.jar:3.5.16]
> 	... 103 common frames omitted
> 
> ```
>
> 

* 问题原因： MyBatis 不知道如何将 Java 的 Map<String, Object> 类型映射到数据库的 JSON/TEXT 字段。

* 解决方案：

  创建了 JsonTypeHandler 来处理 Map 和 JSON 字符串之间的转换
  在 ChatMessage.metadataJson 字段上添加了 @TableField(typeHandler = JsonTypeHandler.class) 注解
  这样 MyBatis 在插入和查询时就知道如何处理这个字段了。

  ```java
  package top.hazenix.hazeaihub.handler;
  
  import com.fasterxml.jackson.core.JsonProcessingException;
  import com.fasterxml.jackson.databind.ObjectMapper;
  import org.apache.ibatis.type.BaseTypeHandler;
  import org.apache.ibatis.type.JdbcType;
  import org.apache.ibatis.type.MappedTypes;
  
  import java.sql.CallableStatement;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.Map;
  
  @MappedTypes({Map.class})
  public class JsonTypeHandler extends BaseTypeHandler<Map<String, Object>> {
  
      private static final ObjectMapper objectMapper = new ObjectMapper();
  
      @Override
      public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
          try {
              ps.setString(i, objectMapper.writeValueAsString(parameter));
          } catch (JsonProcessingException e) {
              throw new SQLException("Error converting Map to JSON string", e);
          }
      }
  
      @Override
      public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
          String json = rs.getString(columnName);
          return parseJson(json);
      }
  
      @Override
      public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
          String json = rs.getString(columnIndex);
          return parseJson(json);
      }
  
      @Override
      public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
          String json = cs.getString(columnIndex);
          return parseJson(json);
      }
  
      private Map<String, Object> parseJson(String json) throws SQLException {
          if (json == null || json.isEmpty()) {
              return null;
          }
          try {
              return objectMapper.readValue(json, Map.class);
          } catch (JsonProcessingException e) {
              throw new SQLException("Error parsing JSON string to Map", e);
          }
      }
  }
  
  ```

  ```java
  @Schema(description = "扩展信息[模型名、token统计、thinking片段汇总、解析到的原谅值等]")
      @TableField(typeHandler = top.hazenix.hazeaihub.handler.JsonTypeHandler.class)
      private Map<String, Object> metadataJson;
  ```

  

  





# 数据库系列





* id要设置自增，避免不必要的bug

* 数据库数据不要乱造

  如果employee表中的department_id出现了department表的id之外的数值，后端使用关联查询的时候就会报错
  ![image-20250907081845463](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250907081845463.png)![image-20250907081902870](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250907081902870.png)



* 问题：数据库更新未同步到maven
  ![image-20250912163215196](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912163215196.png)

  在maven中找到数据库并刷新

  


---

---



* ![image-20250923075609914](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250923075609914.png)

  discard long time none received connection. , jdbcUrl : jdbc:mysql://localhost:3306/db_SkyTakeOut?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true, jdbcUrl : jdbc:mysql://localhost:3306/db_SkyTakeOut?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true, lastPacketReceivedIdleMillis : 199054

  * 错误原因：

    > 根据您提供的信息，这是一个**数据库连接超时错误**。以下是详细解释：
    >
    > ## 错误分析
    >
    > - **错误类型**: 数据库连接长时间无活动被丢弃
    > - **根本原因**: 连接在空闲状态下超过一定时间未收到数据包
    >
    > ## 关键信息解读
    >
    > 1. **`lastPacketReceivedIdleMillis : 199054`**
    >    - 表示连接已经空闲了约200秒(3.3分钟)没有收到任何数据包
    >    - 超过了MySQL服务器或连接池设置的超时时间
    >
    > 2. **JDBC连接参数**:
    >    - 数据库: MySQL
    >    - 地址: `localhost:3306`
    >    - 数据库名: `db_SkyTakeOut`
    >
    > ## 常见解决方案
    >
    > ### 1. 配置连接池心跳检测
    > ```java
    > // 以HikariCP为例
    > spring.datasource.hikari.connection-test-query=SELECT 1
    > spring.datasource.hikari.idle-timeout=600000
    > spring.datasource.hikari.max-lifetime=1800000
    > ```
    >
    >
    > ### 2. 添加连接保活参数到JDBC URL
    > ```properties
    > jdbc:mysql://localhost:3306/db_SkyTakeOut?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=10
    > ```
    >
    >
    > ### 3. 调整MySQL服务器超时设置
    > ```sql
    > -- 查看当前超时设置
    > SHOW VARIABLES LIKE '%timeout%';
    > 
    > -- 调整超时时间(在my.cnf中配置)
    > wait_timeout = 28800
    > interactive_timeout = 28800
    > ```
    >
    > 这个错误通常发生在应用程序长时间未使用数据库连接，连接被服务器主动关闭的场景中。
    >
    > #### 在JDBC URL中添加自动重连参数（推荐）
    >
    > ```
    > url: jdbc:mysql://${sky.datasource.host}:${sky.datasource.port}/${sky.datasource.database}?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true
    > ```
    >
    > ![image-20250923080620751](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250923080620751.png)

    

  

  

  



# 部署



##### 接口请求不到

换了个jar包之后docker compose up -d --build，发现接口全都请求不到了，怎么回事

* application.yml里面dev没有换成prod，打出来的jar包是错的























# 搜索





1. 直接搜索报错信息

   比如
   ![img](https://www.kimi.com/apiv2-files/sign-obj/kimi-fs%2Ffiles%2Fblob%2F5e51b57142a50a7106414b531c130421ae0622eb8e4b5ad84f8449a3c854991e?filename=image.png&sig=DKefRVqJcz5yTOs16ZLeHbqkOqCmY7zdQNsXRDu5lL4=&t=t)

   > 这里我直接复制并搜索“java：警告：源发行版 20 需要目标发行版 20”









# maven



* 如果在执行maven生命周期的时候报错，找到第一条error级别的日志
  这条日志基本上就是产生问题的原因

  











# 测试接口注意





* 测试update接口时，不仅要填写要进行修改的属性值，也要填写id，否则执行sql语句会报错

* ```
  {
    "id":23,
    "name": "四川菜",
    "sort": 3,
    "type": 1
  }
  ```

  json格式写正确了，之前23后面没加逗号，害得controll层一直识别不了，害我调试半天



#### 如何暂时关闭jwt校验



* application配置方式

![image-20251015144539235](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251015144539235.png)

![image-20251015144501359](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251015144501359.png)











### 异常中不认识的单词

invocable：可调用的

invoke：调用

synchronization：同步

transaction：事务

syntax：语法

manual：手册
