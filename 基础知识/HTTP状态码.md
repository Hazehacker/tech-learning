![http-status-code.png](assets/http-status-code.png)

#### 参考
https://javaguide.cn/cs-basics/network/http-status-codes.html

> > HTTP 协议规定，`2xx` 代表成功，`4xx` 代表客户端错，`5xx` 代表服务端错。
> 
> - 如果你的用户登录密码错了，这确实是客户端传参错了，应该返回 `400`。
>     
> - 但是，从程序角度看，服务器成功接收并处理了这个请求（返回了友好的提示），这又像是 `200`
>     

**最佳实践方案：** 通常我们会让 **HTTP 状态码保持为 `200`**（表示 API 调用成功，服务器正常工作），然后在 Body 里用业务错误码来表示“业务失败”。这样既符合协议，又方便前端统一处理。