Directory

- [Spring Security 框架说明](#Spring%20Security%20%E6%A1%86%E6%9E%B6%E8%AF%B4%E6%98%8E)
	- [一、Spring Security 的作用](#%E4%B8%80%E3%80%81Spring%20Security%20%E7%9A%84%E4%BD%9C%E7%94%A8)
	- [二、引入与实战](#%E4%BA%8C%E3%80%81%E5%BC%95%E5%85%A5%E4%B8%8E%E5%AE%9E%E6%88%98)
		- [1. 引入依赖](#1.%20%E5%BC%95%E5%85%A5%E4%BE%9D%E8%B5%96)
			- [启动后的默认行为](#%E5%90%AF%E5%8A%A8%E5%90%8E%E7%9A%84%E9%BB%98%E8%AE%A4%E8%A1%8C%E4%B8%BA)
		- [2. 编写基础安全配置类](#2.%20%E7%BC%96%E5%86%99%E5%9F%BA%E7%A1%80%E5%AE%89%E5%85%A8%E9%85%8D%E7%BD%AE%E7%B1%BB)
		- [3. 使用 BCrypt 加密/校验密码](#3.%20%E4%BD%BF%E7%94%A8%20BCrypt%20%E5%8A%A0%E5%AF%86/%E6%A0%A1%E9%AA%8C%E5%AF%86%E7%A0%81)
		- [4. 使用注解进行方法级权限控制(可选)](#4.%20%E4%BD%BF%E7%94%A8%E6%B3%A8%E8%A7%A3%E8%BF%9B%E8%A1%8C%E6%96%B9%E6%B3%95%E7%BA%A7%E6%9D%83%E9%99%90%E6%8E%A7%E5%88%B6(%E5%8F%AF%E9%80%89))
	- [三、实践的注意事项](#%E4%B8%89%E3%80%81%E5%AE%9E%E8%B7%B5%E7%9A%84%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
	- [四、小结](#%E5%9B%9B%E3%80%81%E5%B0%8F%E7%BB%93)


## Spring Security 框架说明

### 一、Spring Security 的作用

- **1. 认证（Authentication）**
  - 用来识别“你是谁”，例如用户名/密码登录、短信验证码登录、OAuth2 登录等。
  - **提供完整的认证流程**：登录请求 → 用户信息查询 → 凭证校验 → 生成认证对象并保存到安全上下文。

- **2. 授权（Authorization）/ 权限控制**
  - 在“知道你是谁”之后，判断“你能干什么”。
  - **支持基于 URL、方法、注解**（如 `@PreAuthorize`、`@Secured`）等多种方式**的权限控制**。
  - 可以基于角色（Role）、权限（Authority）、组织结构等多维度进行访问控制。

- **3. 防护常见安全攻击**
  - **CSRF 防护**：防止跨站请求伪造，默认对状态变更请求（POST/PUT/DELETE 等）开启 CSRF 保护。
  - **Session 固定攻击防护**：登录后重新生成 Session ID，降低会话劫持风险。
  - **点击劫持防护**：通过 `X-Frame-Options`、`Content-Security-Policy` 等响应头增强安全。
  - **<u>密码加密存储</u>**：提供 `PasswordEncoder`（如 BCrypt）保证密码非明文存储。

- **4. 与 Spring 生态深度集成**
  - 与 Spring MVC、Spring Data、Spring Boot 等无缝整合。
  - **通过<u>自动配置和注解方式</u>，大部分安全逻辑可配置而非硬编码**。

- **5. 高度可扩展**
  - 通过过滤器链、认证管理器、用户详情服务等扩展点可以定制：
    - 自定义登录逻辑（短信、二维码、单点登录等）
    - 自定义权限模型、数据权限
    - 与第三方认证系统对接（OAuth2 / OpenID Connect / SSO 等）

---

### 二、引入与实战

> 当前项目使用 **Spring Boot 2.7.3**，对应的 Spring Security 版本为 **5.7.x** 左右，下面结合实际代码说明接入过程。



#### 1. 引入依赖

- **Maven 示例**

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```



##### 启动后的默认行为

- 所有 HTTP 接口默认需要认证访问。
- 会自动生成一个默认用户：
  - 默认用户名：`user`
  - 默认密码：在应用启动日志中输出（`Using generated security password`）。
- 未登录访问受保护资源时，会自动跳转到框架提供的默认登录页。

在实际项目中，一般会**通过配置类来自定义登录逻辑和放行规则**。





#### 2. 编写基础安全配置类

**定义过滤链与基本访问规则**

```java
@Configuration
@EnableMethodSecurity // 如果需要使用 @PreAuthorize 等方法级权限控制
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 关闭 csrf（仅示例，实际生产环境应根据前后端架构正确配置）
            .csrf(csrf -> csrf.disable())

            // 2. 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 放行静态资源、登录接口等
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                // 其他请求都需要认证
                .anyRequest().authenticated()
            )

            // 3. 配置表单登录
            .formLogin(form -> form
                .loginPage("/login")           // 自定义登录页地址
                .loginProcessingUrl("/doLogin")// 登录表单提交地址
                .defaultSuccessUrl("/")        // 登录成功后的默认跳转地址
                .permitAll()
            )

            // 4. 配置登出
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }
}
```





```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 基础的 Spring Security 配置
 *
 * 说明：
 * 1. 当前项目用户认证仍然采用自定义的 JWT 方案
 * 2. 为了不影响现有接口行为，这里暂时对所有请求放行，仅启用密码加密能力（BCrypt）
 * 3. 后续如果接入基于 Spring Security 的认证/鉴权，只需要在此类中补充规则即可
 */
@Configuration
public class SecurityConfig {

    /**
     * 对所有请求暂时放行，保留现有 JWT 拦截器逻辑
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();

        return http.build();
    }

    /**
     * 提供全局 PasswordEncoder Bean，使用 BCrypt 实现密码加密与校验
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



```





- **1）关闭默认表单登录，放行所有请求，避免影响现有接口行为**

```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();

        return http.build();
    }
}
```

- **2）提供全局的 `PasswordEncoder` Bean，统一使用 BCrypt 做加密**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

> 后续如果要开启更细粒度的权限控制（例如某些接口必须登录、基于角色限制等），只需要在这个配置类里调整 `authorizeRequests()` 规则即可。





#### 3. 使用 BCrypt 加密/校验密码

> #### 登录 / 注册 / 修改密码



1. 注入在配置类配置过的`BCryptPasswordEncoder`对象

   ```java
   private final PasswordEncoder passwordEncoder;// 这是多态的写法
   ```

2. ==加密密码==

   使用`String encode(CharSequence rawPassword);`方法在注册时 

   > `String` 继承了 `CharSequence`，第一个参数传入 String 即可

   ```java
   String encodedPassword = passwordEncoder.encode(userLoginDTO.getPassword())
   ```

3. ==校验是否相等==

   使用 `boolean matches(CharSequence rawPassword, String encodedPassword);` 方法 

   ```java
   if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
       throw new BussinessException(ErrorCode.A01002, MessageConstant.EMAIL_OR_PASSWORD_ERROR);
   }
   ```

   > ![image-20260212223339235](assets/image-20260212223339235.png)
   >
   > ![image-20260212223510489](assets/image-20260212223510489.png)
   >
   > ![image-20260212224253866](assets/image-20260212224253866.png)
   >
   > 源码底层会将明文加密之后再与数据库中的加密字符串





> **辅助：提供一个简单的 BCrypt 生成工具（测试类）**
>
> ```java
> PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
> String bcryptPassword = passwordEncoder.encode("123456");
> System.out.println(bcryptPassword);
> ```
>
> 用途：本地需要手动插入用户时，可以通过它生成 BCrypt 加密后的密码。
>











#### 4. 使用注解进行方法级权限控制(可选)

在类上开启 `@EnableMethodSecurity` 后，可以在业务层/控制层使用注解：

- **示例：基于角色和权限的控制**

```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyOperation() {
    // 只有 ADMIN 角色用户才能访问
}

@PreAuthorize("hasAuthority('article:write')")
public void writeArticle() {
    // 只有拥有 article:write 权限的用户才能访问
}
```





### 三、实践的注意事项

- **1. 密码一定要加密存储**
  - 强制使用 `BCryptPasswordEncoder` 等安全算法，而不是明文或可逆加密。

- **2. 明确区分“认证失败”和“权限不足”**
  - 认证失败：用户未登录或凭证错误，一般返回 401 或跳转登录页。
  - 权限不足：已经登录，但无访问该资源的权限，一般返回 403。

- **3. 结合前后端分离架构设计**
  - 若为前后端分离，可使用：
    - Session + Cookie
    - JWT + `OncePerRequestFilter`
    - OAuth2 / OpenID Connect 等方案。
  - 需要根据架构设计合理开启/关闭 CSRF、配置跨域（CORS）等。

- **4. 审计与日志**
  - 建议记录登录成功/失败日志、关键接口访问日志，便于安全审计与问题追踪。

- **5. 与现有权限模型对齐**
  - 在引入 Spring Security 时，需要结合项目现有的“用户-角色-权限-菜单”等模型进行适配。
  - 可以将数据库中的角色/权限映射为 `GrantedAuthority`，统一走框架的鉴权逻辑。

---

### 四、小结

- **Spring Security 的核心价值**：提供标准化、可扩展的认证与授权能力，并内建大量安全防护机制，避免重复造轮子和安全细节遗漏。
- **引入步骤概览**：
  1. 在构建工具中添加 `spring-boot-starter-security` 依赖。
  2. 编写安全配置类（`SecurityFilterChain`），定义登录、登出和访问规则。
  3. 配置用户信息来源（内存/数据库）与密码加密方式。
  4. 根据业务需要使用注解等方式做细粒度权限控制。
  5. 结合项目架构完善 CSRF、防护策略、日志审计等安全细节。

