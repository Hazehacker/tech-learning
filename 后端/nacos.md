![image-20251015193253313](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251015193253313.png)





* 





## 下载





## 启动

![image-20251020112245961](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251020112245961.png)



#### 大项目nacos配置

* 创建命名空间psi-dev

* 在psi-dev中创建各个配置【复制进去，然后去掉中文】

  system.yaml

  ```
  spring:
    cloud:
      inetutils:
        #优先网络IP选择
        preferred-networks: 
          - 192.168
          - 39.99
        #忽略一些虚拟网卡
        ignored-interfaces:
          - docker0
          - veth.*
          - VM.*
          - br-.*
  ```

  （固定配置）

  数据源配置(配置数据库) 
  data-source.yaml

  ```
  #References
  #https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter
  #https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE
  #https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
  spring:
    #配置MySQL数据库
    datasource:
      url: jdbc:mysql://192.168.220.128:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
      username: root
      password: 123456
      driver-class-name: com.mysql.cj.jdbc.Driver
      type: com.alibaba.druid.pool.DruidDataSource
      druid:
        name: DruidDataSource
        initial-size: 1
        min-idle: 1
        max-active: 20
        async-init: true
        max-wait: 6000
        time-between-eviction-runs-millis: 60000
        min-evictable-idle-time-millis: 300000
        max-evictable-idle-time-millis: 900000
        validation-query: SELECT 1
        test-while-idle: true
        test-on-borrow: false
        test-on-return: false
        pool-prepared-statements: true
        max-pool-prepared-statement-per-connection-size: 20
        connection-init-sqls: SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;
        filters: stat
    #配置Redis数据库
    redis:
      host: 192.168.220.128
      port: 6379
      password: 01star
    data:
      #mongodb配置
      mongodb:
        #格式: mongodb://账号:密码@主机地址:端口/数据库名称
        uri: mongodb://awei:123456@192.168.220.128:27017/firstDb
  ```

  > 改成自己数据库ip地址，端口，名称
  >
  > ```
  > #References
  > #https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter
  > #https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE
  > #https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
  > spring:
  >   datasource:
  >     url: jdbc:mysql://127.0.0.1:3306/zo_psi?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
  >     username: root
  >     password: 1234
  >     driver-class-name: com.mysql.cj.jdbc.Driver
  >     type: com.alibaba.druid.pool.DruidDataSource
  >     druid:
  >       name: DruidDataSource
  >       initial-size: 1
  >       min-idle: 1
  >       max-active: 20
  >       async-init: true
  >       max-wait: 6000
  >       time-between-eviction-runs-millis: 60000
  >       min-evictable-idle-time-millis: 300000
  >       max-evictable-idle-time-millis: 900000
  >       validation-query: SELECT 1
  >       test-while-idle: true
  >       test-on-borrow: false
  >       test-on-return: false
  >       pool-prepared-statements: true
  >       max-pool-prepared-statement-per-connection-size: 20
  >       connection-init-sqls: SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;
  >       filters: stat
  >   redis:
  >     host: 127.0.0.1
  >     port: 6379
  >     password: 123456
  >   data:
  >     mongodb:
  >       uri: mongodb://awei:123456@192.168.220.128:27017/firstDb
  > ```
  >
  > ![image-20251020113906279](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251020113906279.png)

  第三方服务配置

  third-services.yaml

  ```
  #C++提供服务器
  cpp:
    sample:
      url: http://localhost:8090
      name: feign-cpp-sample
  #sentinel提供服务
  sentinel:
      dashboard: 192.168.220.128:8718
  #rocketmq配置
  rocket-mq:
      name-server: 192.168.220.128:9876
  #seata配置
  seata:
    default: 192.168.220.128:8091
  #logstash配置
  logstash:
    host: 192.168.220.128
  #FASTDFS配置
  #References
  #https://i4t.com/4758.html
  #https://github.com/happyfish100/fastdfs/tree/master/docker/dockerfile_network/conf
  fastdfs:
    charset: UTF-8
    connect-timeout: 5
    network-timeout: 30
    http-secret-key: FastDFS1234567890
    http-anti-steal-token: true
    connection-pool-max-idle: 20
    connection-pool-max-total: 20
    connection-pool-min-idle: 2
    nginx-servers: 192.168.220.128:8888
    tracker-servers: 192.168.220.128:22122
  #Easy ES
  easy-es:
    address: 192.168.220.128:9200
    username: elastic #es用户名,若无则删去此行配置
    password: WG7WVmuNMtM4GwNYkyWH #es密码,若无则删去此行配置
  ```

  密钥配置文件

  key-config.yaml







```

```

```

```

```

```













