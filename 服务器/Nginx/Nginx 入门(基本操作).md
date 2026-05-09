> 在公司部署又涉及到了 nginx，之前只学到了会用的程度，借此机会深入学习一下，顺便整理一些博客出来，感兴趣的朋友可以订阅这个系列👍(附个链接)






## 是什么 

Nginx 是一款轻量级的 Web 服务器、反向代理服务器，由于它的内存占用少，启动极快，高并发能力强，在互联网项目中广泛应用。

* 官方文档：

  * https://nginx.org/en/linux_packages.html

  * https://nginx.org/en/docs/beginners_guide.html#control

  * https://nginx.org/en/docs/http/ngx_http_core_module.html#listen

  * https://nginx.org/en/docs/http/server_names.html

  * https://docs.nginx.com/nginx/admin-guide/web-server/reverse-proxy/

    http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_set_header

  * https://nginx.org/en/docs/http/ngx_http_core_module.html#location
    https://docs.nginx.com/nginx/admin-guide/web-server/serving-static-content/

  * https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_buffer_size

    https://docs.nginx.com/nginx/admin-guide/content-cache/content-caching/

    http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache_path



##  Nginx 安装





## 基本信息

* 日志默认在/var/log/nginx 目录

#### 配置文件





* 配置文件默认在/etc/nginx 目录下
	* 默认配置文件是 nginx.conf
	* 默认包含 conf.d 目录下的所有配置文件（`*.conf`）
	* 配置文件位于 /etc/nginx/nginx.conf , 下列命令会引用/etc/nginx/conf.d目录下所有的.conf文件，这样可以保持主配置文件的简洁，同时配多个.conf文件方便区分，增加可读性
	  
	  ```nginx
	  include /etc/nginx/conf.d/*.conf
	  ```
	  
	   （详情参考[模块化设计]）
	  
	  * 默认配置/etc/nginx/conf.d/default.conf
	  
	    ```nginx
	    server {
	        listen       80; #监听端口
	        server_name  localhost;
	    
	        location / {
	            root   /usr/share/nginx/html; #根目录
	            index  index.html index.htm; #首页
	        }
	    
	        error_page   500 502 503 504  /50x.html;
	        location = /50x.html {
	            root   /usr/share/nginx/html;
	        }
	    }
	    ```
	  
	    
	* 默认监听80端口；访问80端口会指向这个目录下的 index.html (“welcome to nginx”页面)
	
	  ```nginx
	  listen		80;
	  ......
	  location / {
	      root /usr/share/nginx/html;
	      index index.html index.htm;
	  }
	  ```

#### 相关指令

* 启动 nginx ：`systemctl start nginx`
* 查看 nginx 状态：`systemctl status nginx`
* 重新加载 nginx 的配置文件：`nginx -s reload`

> #### 可能的问题
>
> 在 centos 7 中，用 systemctl 启动 nginx 可能出现如下错误：
>
> `nginx:[emerg]bind()to0.0.0.0:8000 failed (13:Permission denied)`
>
> 这是 selinux 的安全策略引起的。解决方法如下：
>
> - setenforce 0 （临时）
> - 修改/etc/selinux/config，设置SELINUX=disabled （永久有效，需重启）
>
> 





## 配置文件编写

### 文件结构

```nginx
http {

  server{#虚拟主机
     
    location {
      listen 80；
      server_name localhost;
    }
    location {
       
    }
      
  }

  server{
  
  }

}
```



### 配置静态页面

#### listen

监听可以配置成`IP`或`端口`或`IP+端口` 

```
listen 127.0.0.1:8000; 
listen 127.0.0.1;（ 端口不写,默认80 ） 
listen 8000; 
listen *:8000; 
listen localhost:8000;
```



#### server_name

server_name：`server_name` 的作用就是**告诉 Nginx，当收到一个网络请求时，应该由哪一个“虚拟服务器”来处理**

* 可以使用变量 ` $hostname ` 配置成主机名

* 使用域名：` example.org ` ` www.example.org ` ` *.example.org `

**如果多个 server 的端口重复，那么匹配 server_name 来确定怎么转发**

> 如果有多个 server，server之间的 “监听端口+server_name” 组合不能重复



下面的例子中：

```nginx
curl http://localhost:80`会访问`/usr/share/nginx/html
curl http://nginx-dev:80`会访问`/home/AdminLTE-3.2.0
# curl http://localhost:80 会访问这个
server {
    listen       80;
    server_name  localhost;

    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }

 # curl http://nginx-dev:80 会访问这个
server{
    listen 80;
    server_name nginx-dev;#主机名
    
    location / {
        root /home/AdminLTE-3.2.0;
        index index.html index2.html index3.html;
    }
  
}
```

#### location

> 如果说 `server_name` 是决定请求进入哪栋“大楼”，那么 **`location` 就是这栋大楼里的“前台”或“导航员**
>
> 它的核心作用是：**在确定了服务器（Server）之后，根据你访问的具体路径（URI），决定由谁来处理这个请求 / 把文件存放在哪里。**
>
> 当 Nginx 确定了由哪个 `server` 块来处理请求后，它会拿着 URL 中的路径部分（比如 `/images/logo.png` 中的 `/images/`），去和配置文件里的一个个 `location` 规则进行比对。
>
> - **静态资源服务**：告诉 Nginx，“如果用户要图片，就去服务器的硬盘里找文件夹”。
> - **反向代理**：告诉 Nginx，“如果用户要查数据（API），就把请求转发给后端的 Java/Python 程序”。
> - **权限控制**：告诉 Nginx，“如果是管理员页面，需要密码才能进”。

##### location 修饰符

location 中可以使用修饰符或正则表达式

1. `=`：等于，**精确匹配** ，匹配优先级最高。
2. `^~`  ：表示普通字符匹配，**前缀匹配**。如果匹配成功，则不再匹配其它 location。优先级第二高。
3. `~` ：区分大小写
4. `~*`：不区分大小写，**正则匹配**

**示例**：

```nginx
location ^~ /images/ {
    proxy_pass http://localhost:8080;
}

location ~ \.jpg {
    proxy_pass http://localhost:8080;
}
```

> `/images/1.jpg` 会被代理到 `http://localhost:8080/images/1.jpg`
>
> `/some/path/1.jpg` 会被代理到 `http://localhost:8080/some/path/1.jpg`



**匹配规则的优先级**

Nginx 匹配 `location` 是有严格等级制度的，就像公司审批流程一样：

| 优先级          | 符号/类型  | 说明                                                         | 场景举例                              |
| :-------------- | :--------- | :----------------------------------------------------------- | :------------------------------------ |
| **最高 (秒杀)** | `=`        | **精确匹配**。必须一模一样，差一个字符都不行。               | `location = /login` (只匹配登录页)    |
| **高 (特权)**   | `^~`       | **前缀匹配**。只要以这个开头，就立刻通过，**不再检查后面的正则规则**。 | `location ^~ /static/` (所有静态资源) |
| **中 (正则)**   | `~` / `~*` | **正则匹配**。按书写顺序一个个试，匹配上就停。`~`区分大小写，`~*`不区分。 | `location ~ \.php$` (所有PHP文件)     |
| **低 (普通)**   | (无符号)   | **普通前缀匹配**。匹配最长的路径。                           | `location /images/`                   |
| **最低 (兜底)** | `/`        | **通用匹配**。通常写在最后，接住所有漏网之鱼。               | `location /`                          |

**示例**

假设你的配置如下：

```nginx
server {
    listen 80;
    server_name example.com;

    # 1. 精确匹配：只有访问 / 时才生效，常用于首页优化
    location = / {
        return 200 "这是首页！";
    }

    # 2. 静态资源：匹配 /images/ 开头的路径
    location /images/ {
        root /var/www; 
        # 结果：去 /var/www/images/ 目录下找文件
    }
    
    location ^~ /static/ {
        root /var/www; 
        # 如果以/static开头，就去 /var/www/static/ 目录下寻找
    }

    # 3. 动态接口：匹配 .php 结尾的文件
    location ~ \.php$ {
        proxy_pass http://127.0.0.1:9000;
        # 结果：转发给后端处理
    }

    # 4. 兜底规则：上面都没匹配到，就走这里
    location / {
        return 404 "找不到页面";
    }
}
```

**场景：**

- 访问 `example.com/` → 命中 **1** (`=`)，返回“这是首页！”
- 访问 `example.com/images/a.jpg` → 命中 **2** (普通前缀)，去读文件。
- 访问 `example.com/index.php` → 命中 **3** (`~` 正则)，转发给后端。
- 访问 `example.com/abc` → 命中 **4** (`/`)，返回 404。



> - **`server_name`** 负责把流量引到正确的**网站配置块**。
> - **`location`** 负责在这个配置块内部，把具体的**URL 路径**分发到正确的处理逻辑（读文件、转发、重写等）。



> 

















### 配置反向代理

* 正向代理：在**客户端**代理转发请求称为**正向代理**（例如VPN）
  ![forward-proxy-flow.svg](assets/forward-proxy-flow.svg)

* 反向代理：在**服务器端**代理转发请求称为**反向代理**（例如nginx）

  ![reverse-proxy-flow.svg](assets/reverse-proxy-flow.svg)



假设现在有一个后端服务，端口为8080；

nginx配置文件：

```nginx
server {
  
  listen 80;
  
  server_name ruoyi.localhost;
  
  location / {
    proxy_pass http://localhost:80;80
  }

}
```



**proxy_pass配置说明：**

如果`proxy-pass`的地址只配置到端口，不包含`/`或其他路径，那么location将被追加到转发地址中

```nginx
location /some/path/ {
    proxy_pass http://localhost:8080;
}
```

> 访问 `http://localhost/some/path/page.html` 将被代理到 `http://localhost:8080/some/path/page.html` 



如果 `proxy-pass` 的地址包括 `/` 或其他路径，那么 `/some/path` 将会被替换

```nginx
location /some/path/ {
    proxy_pass http://localhost:8080/zh-cn/;
}
```

> 访问 `http://localhost/some/path/page.html` 将被代理到 `http://localhost:8080/zh-cn/page.html`（/some/path 不会被追加到请求中）







#### 设置代理请求headers

‎用户可以 **重新定义或追加 header 信息** 传递给后端[‎](http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_pass_request_headers)‎服务器。可以包含文本、变量及其组合。默认情况下，仅重定义两个字段：‎

```nginx
proxy_set_header Host       $proxy_host;
proxy_set_header Connection close;
```



<u>由于使用反向代理之后，后端服务无法获取用户的真实IP，所以，一般反向代理都会设置以下header信息</u>

```nginx
location /some/path/ {
    #nginx的主机地址
    proxy_set_header Host $http_host;
    #用户端真实的IP，即客户端IP
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

    proxy_pass http://localhost:8088;
}
```

常用变量的值：

* `$host`：nginx主机IP，例如192.168.56.105
* `$http_host`：nginx主机IP和端口，192.168.56.105:8001
* `$proxy_host`：localhost:8088，proxy_pass里配置的主机名和端口
* `$remote_addr`:用户的真实IP，即客户端IP。



#### 非HTTP代理

如果要将请求传递到非 HTTP 代理服务器，可以使用下列指令：

- [fastcgi_pass](https://nginx.org/en/docs/http/ngx_http_fastcgi_module.html#fastcgi_pass)：将请求转发到 FastCGI 服务器（多用于PHP）
- [scgi_pass](https://nginx.org/en/docs/http/ngx_http_scgi_module.html#scgi_pass)：将请求转发到 SCGI server 服务器（多用于PHP）
- [uwsgi_pass](https://nginx.org/en/docs/http/ngx_http_uwsgi_module.html#uwsgi_pass)：将请求转发到 uwsgi 服务器（多用于python）
- [memcached_pass](https://nginx.org/en/docs/http/ngx_http_memcached_module.html#memcached_pass)：将请求转发到 memcached 服务器



## 缓冲和缓存

### 缓冲（buffer）

缓冲一般放在内存中，如果不适合放入内存（比如超过了指定大小），则会将响应写入磁盘临时文件中。

**启用缓冲后，nginx先将后端的请求响应（response）放入缓冲区中，等到整个响应完成后，再发给客户端**。

![img](assets/1659585056819-df82befd-d847-4a0b-bffc-84cf626c2fed.png)

客户端往往是公共网络，情况复杂，可能出现网络不稳定，速度较慢的情况。

而nginx到后端server一般处于同一个机房或者区域，网速稳定且速度极快。

![img](assets/1659585159748-308fa557-418b-4bda-9f3f-a16efccaa673.png)

如果禁用了缓冲，则在客户端从代理服务器接收响应时，响应将同步发送到客户端。对于需要尽快开始接收响应的快速交互式客户端，此行为可能是可取的。

这就会带来一个问题：**因为客户端到nginx的网速过慢，导致nginx只能以一个较慢的速度将响应传给客户端；进而导致后端server也只能以同样较慢的速度传递响应给nginx**，造成一次请求连接耗时过长。

在高并发的情况下，后端server可能会出现<u>大量的连接积压，最终拖垮server端</u>

> **如果<u>不使用缓冲区</u>，过程是这样的：**
>
> 1. 后端服务器瞬间生成好了一份 10MB 的数据，想一口气扔给 Nginx。
>
> 2. Nginx 接到数据，转头想扔给客户端。
>
> 3. **但是！** 客户端接收能力很差，一秒钟只能接一点点数据。
>
> 4. 因为 Nginx 手里没有“仓库”（缓冲区），它不能先把数据存下来，它必须**一边从后端收，一边往客户端发**。
>
> 5. 由于客户端那边堵住了（接收太慢），Nginx 的发车口就堵住了，且 Nginx 此时没法把数据暂存，所以它**无法继续从后端读取新数据**（否则内存会爆）
>
>    **结果：** 后端服务器明明一秒钟能处理完的事，现在被迫陪着客户端慢慢“磨”。后端服务器的连接（Connection）一直被占用，直到这 10MB 数据全部慢吞吞地传完给客户端，这个连接才能释放。



![img](assets/1659585458893-c276995d-f299-444b-bd33-cee5933ab878.png)

开启代理缓冲后，**nginx可以用较快的速度尽可能将响应体读取并缓冲到本地内存或磁盘中，然后同时根据客户端的网络质量以合适的网速将响应传递给客户端**。

这样既解决了server端连接过多的问题，也保证了能持续稳定的像客户端传递响应。



可以使用 [proxy_buffering](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_buffering) 参数启用和禁用缓冲，nginx默认为 on 启用缓冲，若要关闭，设置为 off  。 

```nginx
proxy_buffering off;
```

[proxy_buffers](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_buffers) 指令设置每个连接读取响应的缓冲区的`大小`和`数量` 。默认情况下，缓冲区大小等于一个内存页，4K 或 8K，具体取决于操作系统。

来自后端服务器响应的第一部分存储在单独的缓冲区中，其大小通过 [proxy_buffer_size](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_buffer_size) 指令进行设置，此部分通常是相对较小的响应headers，通常将其设置成小于默认值。

```nginx
location / {
    proxy_buffers 16 4k;
    proxy_buffer_size 2k;
    proxy_pass http://localhost:8088;
}
```

如果整个响应不适合存到内存里，则将其中的一部分保存到磁盘上的‎‎临时文件中‎‎。

> 其他：
>
> ‎[‎proxy_max_temp_file_size‎](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_max_temp_file_size)‎：设置临时文件的最大值。
>
> ‎[‎proxy_temp_file_write_size‎](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_temp_file_write_size)‎：设置一次写入临时文件的大小。



### 缓存（cache）

启用缓存后，nginx 将响应保存在磁盘中，返回给客户端的数据首先从缓存中获取，这样子相同的请求不用每次都发送给后端服务器，减少到后端请求的数量。

![img](assets/1659599505974-889b4227-80eb-4deb-8bae-303828cc3bc4.png)

启用缓存，需要在http上下文中使用 [proxy_cache_path](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache_path) 指令，定义缓存的本地文件目录，名称和大小。

缓存区可以被多个server共享，使用 [proxy_cache](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache) 指定使用哪个缓存区。

```nginx
http {
    proxy_cache_path /data/nginx/cache keys_zone=mycache:10m;
    server {
        proxy_cache mycache;
        location / {
            proxy_pass http://localhost:8000;
        }
    }
}
```

缓存目录的文件名是 [proxy_cache_key](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache_key) 的MD5值。 

例如：`/data/nginx/cache/c/29/b7f54b2df7773722d382f4809d65029c`

[proxy_cache_key](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache_key) 默认设置如下：

```nginx
proxy_cache_key $scheme$proxy_host$uri$is_args$args;
```

也可以自定义缓存的键，例如

```nginx
proxy_cache_key "$host$request_uri$cookie_user";
```

缓存不应该设置的太敏感，可以使用[proxy_cache_min_uses](https://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_cache_min_uses)设置相同的key的请求，访问次数超过指定数量才会被缓存。

```nginx
proxy_cache_min_uses 5;
```

默认情况下，响应无限期地保留在缓存中。仅当缓存超过最大配置大小时，按照时间删除最旧的数据。

### 示例



```nginx
proxy_cache_path /var/cache/nginx/data keys_zone=mycache:10m;

server {

    listen 8001;
    server_name ruoyi.localhost;
    
    location / {
        #设置buffer
        proxy_buffers 16 4k;
        proxy_buffer_size 2k;
        proxy_pass http://localhost:8088;        

    }


    location ~ \.(js|css|png|jpg|gif|ico) {
        #设置cache
        proxy_cache mycache;
        proxy_cache_valid 200 302 10m;
        proxy_cache_valid 404      1m;
        proxy_cache_valid any 5m;

        proxy_pass http://localhost:8088;  
    }

    location = /html/ie.html {

        proxy_cache mycache;
        proxy_cache_valid 200 302 10m;
        proxy_cache_valid 404      1m;
        proxy_cache_valid any 5m;

        proxy_pass http://localhost:8088;  
    }

    location ^~ /fonts/ {

        proxy_cache mycache;
        proxy_cache_valid 200 302 10m;
        proxy_cache_valid 404      1m;
        proxy_cache_valid any 5m;

        proxy_pass http://localhost:8088;  
    }

}
```















## 负载均衡



## URL 重写



## TCP 反向代理






## HTTPS 配置

















