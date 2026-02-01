

> 共同目标：维持用户的登录状态





# Cookie



* 作用

  * 在HTTP请求中 携带数据给服务器

    > 位于 请求头
    >
    > ![image-20251114134847096](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114134847096.png)
    >
    >
    > ![image-20251114191458171](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114191458171.png)

    ![image-20251114133512634](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114133512634.png)
    

* 缺点

  * 把信息(比如用户名和密码)存在浏览器的cookie里面是很不安全的，如果你的密码被黑，你的信息就会泄露
  * 用户可以篡改cookie的信息(比如userId)，让服务器以为是另一个用户的操作











# Session

* 会话

  > 不同的网站对每个用户的的会话都设置了时间和唯一的ID(也就是session Id)

* 由服务器自己定义，一般保持在数据库里面



流程

* 浏览器携带信息发起登录请求

* 服务器生成session ID和过期时间
  然后设置一个cookie，将session ID设置到cookie里面、把过期时间对应设置为这个cookie的有效期

* 浏览器保存cookie

  > 浏览器发现响应头里面有set-cookie属性，就会自动存session ID到cookie里面，前端不需要调用SetCookie方法
  >
  > 
  >
  > ![image-20251114192057250](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114192057250.png)
  > 

* 浏览器的下次访问会自动携带cookie给服务器、服务器可以获取到里面的session ID，直到过期

  > ![image-20251114191607880](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114191607880.png)
  >
  > 



> 浏览器存储临时key，服务器根据key本地调取用户信息



* 相比于cookie方案的优点
  * 保存的信息容量大(cookie只用存session ID)，用户信息直接从数据库取，可以取一整个对象
  * 此时浏览器保存的cookie没有重要信息，里面只有session ID和过期时间



* 缺点
  * 占用服务器资源
  * 扩展性差，不适配于  分布式集群
  * 依然需要依赖cookie
    有跨域限制













# Redis+Session方案

* 解决的问题

  * 分布式集群中，有多台服务器
    某些服务器会没有会话机制，导致鉴权失败

    > 由于负载均衡机制，每一次请求可能会打到不同的服务器

    而复制session到其他服务器，......

  * 所以在服务端架设一个中心化的存储服务(比如redis)来专门存储会话数据

![image-20251114190705406](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114190705406.png)



* 弊端
  * 占用服务器资源，容易给分布式系统造成瓶颈，中心化服务器如果故障就会造成所有服务器故障
  * 一些边缘计算的项目中，没办法使用中心化服务
  * 依然需要依赖cookie
    有跨域限制（H5、安卓端、苹果端、小程序有自己的端口和域名），这时候前端再请求后端就会有跨域问题(cookie在跨域情况下无法传递)，需要在后端设置允许跨域、前端设置允许跨域的cookie传递

![image-20251114192840623](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114192840623.png)







* 还是希望会话数据尽量由客户端保管，接口尽可能无状态
  于是有了JWT(token的一种标准)









# JSON WEB Token



* 随着互联网的发展，如果依然使用基于Cookie的Session，服务器就需要存储大量的Session ID在服务器里
* 而且如果有多台服务器(分布式系统)，一台服务器存储了Session ID、就需要分享Session ID给其他服务器
* 且  客户端(app、小程序 )的网络请求是没有cookie机制的，所以需要token



* 解决方案

  1. redis实现共享session机制（即数据库存储session id）

     > 但是有内存溢出的风险(redis内存有限)，不过可以使用redis集群

  2. 使用token校验



* 优点
  * 服务器不用保存



**流程**

* 服务器生成JWT令牌，发送给浏览器

  > ![image-20251114192942772](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114192942772.png)
  >
  > ![image-20251114192958945](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251114192958945.png)
  > 

* 浏览器以cookie 或 storage的方式存储

* 在后面的请求中携带

  > 这里请求不是使用cookie字段，而是使用Authorization字段





> 如果只是token的话，服务器仍然需要存储数据，如果是jwt，直接采取解析就行
>
> token存在服务器redis上作为key，value是用户信息
>
> 分发token给浏览器，浏览器每次请求携带token 
>
> 











