## 作用

* 





## **逻辑链**

* 图解 

  ![image-20251104134446391](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104134446391.png)


  ![image-20251104134410222](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104134410222.png)

* 







#### 为什么我看很多前端项目发起的请求都用/api/开头

> 这是一个非常好的问题！前端项目中很多请求使用 `/api/` 开头，主要是出于 **开发便利性、前后端分离架构** 和 **代理配置** 的考虑。下面详细解释原因：
>
> ------
>
> ### 1. **统一 API 路径前缀，便于管理**
>
> - 使用 `/api/xxx` 作为所有后端接口的统一前缀，可以让前端代码更清晰、可维护。
>
> - 比如：
>
>   ```js
>   fetch('/api/users')
>   fetch('/api/orders')
>   ```
>
>   一看就知道这些是调用后端 API，而不是静态资源或其他路径。
>
> ------
>
> ### 2. **配合开发服务器（如 Vite、Webpack Dev Server）的代理功能**
>
> 在本地开发时，前端通常运行在 `http://localhost:3000`，而后端 API 可能在 `http://localhost:8080`。直接请求会跨域。
>
> 为了解决这个问题，前端开发服务器支持 **代理（proxy）** 功能。例如在 `vite.config.js` 或 `webpack.config.js` 中配置：
>
> ```js
> // vite.config.js 示例
> export default {
>   server: {
>     proxy: {
>       '/api': {
>         target: 'http://localhost:8080', // 后端地址
>         changeOrigin: true,
>         rewrite: (path) => path.replace(/^\/api/, '') // 去掉 /api 前缀再转发
>       }
>     }
>   }
> }
> ```
>
> 这样，前端发 `/api/users` 请求时，开发服务器会自动转发到 `http://localhost:8080/users`，**避免了跨域问题**，同时代码里不需要写完整 URL。
>
> > ✅ 生产环境中，这个代理通常由 Nginx 或网关来处理，把 `/api/xxx` 的请求转发给后端服务。
>
> ------
>
> ### 3. **与静态资源路径隔离**
>
> 前端项目通常有 `/static`、`/assets`、`/images` 等静态资源路径。使用 `/api/` 作为 API 前缀可以**避免路径冲突**，也方便服务器或 CDN 配置路由规则。
>
> ------
>
> ### 4. **符合 RESTful 或微服务架构惯例**
>
> 很多后端框架（如 Spring Boot、Express、Django）也会约定 API 路径以 `/api/v1/...` 开头，表示这是对外暴露的接口，并可能包含版本控制（v1、v2）。前端沿用这一约定，保持一致性。
>
> ------
>
> ### 总结
>
> | 原因     | 说明                        |
> | -------- | --------------------------- |
> | 统一管理 | 所有 API 请求有统一入口标识 |
> | 开发代理 | 配合 dev server 解决跨域    |
> | 路径隔离 | 区分 API 与静态资源         |
> | 架构规范 | 符合后端 API 设计惯例       |
>
> 所以，`/api/` 并不是强制标准，而是一种**广泛采用的最佳实践**。
>
> 如果你在自己的项目中也这样做，会让协作和部署更加顺畅 😊



#### 生产环境中，不以/api/开头的请求nginx不能转发吗

> 非常好的问题！我们来澄清一个关键点：
>
> > **Nginx 能否转发请求，不取决于前端请求是否以 `/api/` 开头，而是取决于你在 Nginx 配置中怎么写规则。**
>
> ------
>
> ### ✅ 简单回答：
>
> **不是“不以 `/api/` 开头就不能转发”，而是“你配置了只转发 `/api/` 开头的请求”。**
>
> 如果你愿意，完全可以配置 Nginx 转发任意路径的请求，比如 `/user`、`/data`，甚至所有请求都转发给后端。
>
> ------
>
> ### 🔧 举个例子
>
> #### 场景 1：只转发 `/api/` 开头的请求（常见做法）
>
> ```nginx
> location /api/ {
>     proxy_pass http://backend_server;
> }
> ```
>
> - 前端请求 `/api/users` → 转发给后端
> - 前端请求 `/static/logo.png` → 由 Nginx 直接返回静态文件（不转发）
>
> ✅ 优点：清晰分离静态资源和 API，安全、高效。
>
> ------
>
> #### 场景 2：转发所有请求给后端（不推荐，但可行）
>
> ```nginx
> location / {
>     proxy_pass http://backend_server;
> }
> ```
>
> - 所有请求（包括 `/user`、`/login`、`/`）都发给后端
> - 后端需要同时处理 API 和页面渲染（比如 SSR）
>
> ⚠️ 缺点：静态资源也走后端，性能差；前后端耦合。
>
> ------
>
> #### 场景 3：转发多个自定义前缀
>
> ```nginx
> location /api/ {
>     proxy_pass http://backend_api;
> }
> 
> location /auth/ {
>     proxy_pass http://auth_service;
> }
> 
> location /graphql {
>     proxy_pass http://graphql_service;
> }
> ```
>
> → 可以灵活支持不同路径转发到不同服务。
>
> ------
>
> ### 🌐 生产环境典型架构
>
> 在前后端分离项目中，生产部署通常是这样的：
>
> ```
> 用户浏览器
>    │
>    ▼
>  Nginx（入口）
>    ├── /api/xxx  → 转发给后端 API 服务（如 http://127.0.0.1:8080）
>    └── 其他路径（/、/static/、/assets/） → 返回前端打包的静态文件（index.html, js, css...）
> ```
>
> 所以，**用 `/api/` 是为了方便 Nginx 写转发规则，而不是技术限制**。
>
> ------
>
> ### 💡 如果你不想用 `/api/` 怎么办？
>
> 完全可以！比如你希望所有 API 路径是 `/v1/users`，那就在 Nginx 中这样写：
>
> ```nginx
> location /v1/ {
>     proxy_pass http://backend;
> }
> ```
>
> 或者用正则匹配特定路径：
>
> ```nginx
> location ~ ^/(users|orders|products) {
>     proxy_pass http://backend;
> }
> ```
>
> 但注意：**路径越模糊，越容易误转发或冲突**。<u>所以 `/api/` 成为一种约定俗成的“安全区”。</u>
>
> ------
>
> ### ✅ 总结
>
> - **Nginx 能转发任何路径**，只要配置了对应的 `location`。
>
> - 使用 
>
>   ```
>   /api/
>   ```
>
>    是
>
>   工程实践上的约定
>
>   ，便于：
>
>   - 区分 API 与静态资源
>   - 简化代理配置
>   - 避免路径冲突
>
> - 如果你有特殊需求，完全可以不用 `/api/`，只需调整 Nginx 配置即可。
>
> 希望这能帮你彻底理解背后的原理！如有具体配置需求，也可以贴出来帮你分析 😊







## 反向代理的配置

* 作用

  ![image-20251104104549133](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104104549133.png)

  ![image-20251104105037531](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104105037531.png)
  前端请求路径全部发向nginx，然后由nginx做转发

  ![image-20251104105203589](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104105203589.png)











![image-20250919201236897](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919201236897.png)

> proxy_pass转发请求，将所有前缀是/api/的换成
>
> 
> 





## 为不同域名代理



```



```





















## 负载均衡的配置



![image-20250919201456788](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919201456788.png)

> 还是基于反向代理，只不过分到多台服务器上



![image-20250919201701207](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919201701207.png)







#### nginx.conf

* 作用

  * 配置代理

    ![image-20251103162359409](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103162359409.png)

    > 为管理端和用户端分别设置代理，监听的(云服务器)端口分别是18080和18081（因为这个项目的管理端应用端口是18080、用户端应用端口是18081(不是5173)）

    > /usr/share/nginx/html/hmall-portal表示容器内的目录路径
    > 也就是说项目要设置的映射也是这个目录

    > proxy_pass http://hmall:8080
    >
    > 配置实际代理到的服务器地址（由于容器直接通信使用容器名，这里hmall表示容器名）















