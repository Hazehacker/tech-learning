# 问题



#### mysql空间不足

> 根据您提供的日志信息，MySQL容器（mysql8）出现了严重的磁盘空间不足问题（错误码28："No space left on device"），导致无法写入数据并最终停止服务。以下是详细的解决方案：
>
> ## 问题分析
>
> 从日志可以看到：
> ```
> 2025-05-20T01:13:48.400197Z [ERROR] InnoDB: Operating system error number 28
> 2025-05-20T01:13:48.400018Z [ERROR] InnoDB: Error number 28 means 'No space left on device'
> ```
>
> 随后MySQL服务器因binlog写入失败而停止服务。
>
> ## 立即解决方案
>
> ### 1. **检查磁盘空间**
> ```bash
> # 检查宿主机磁盘空间
> df -h
> 
> # 检查Docker使用的磁盘空间
> docker system df
> 
> # 检查MySQL数据卷具体占用
> docker exec mysql8 du -sh /var/lib/mysql
> 
> # 查看具体目录大小
> docker exec mysql8 du -sh /var/lib/mysql/*
> ```
>
> ### 2. **清理MySQL日志和临时文件**
> ```bash
> # 进入MySQL容器
> docker exec -it mysql8 bash
> 
> # 登录MySQL（使用root密码）
> mysql -uroot -p
> 
> # 在MySQL命令行中执行以下清理操作：
> 
> # 清理binlog（如果不需要用于主从复制）
> PURGE BINARY LOGS BEFORE NOW() - INTERVAL 3 DAY;
> 
> # 检查并清理临时表
> SELECT * FROM information_schema.TABLES WHERE TABLE_SCHEMA='his_system_dev' AND ENGINE='MEMORY';
> 
> # 优化表以释放空间（可选）
> OPTIMIZE TABLE his_charge.st_charge_detail;
> 
> # 退出MySQL
> exit
> 
> # 清理操作系统缓存（在容器内）
> sync && echo 3 > /proc/sys/vm/drop_caches
> ```
>
> ### 3. **清理Docker相关文件**
> ```bash
> # 清理停止的容器
> docker container prune
> 
> # 清理无用的镜像
> docker image prune -a
> 
> # 清理构建缓存
> docker builder prune
> 
> # 清理Docker系统（包括停止的容器、无标签镜像、build缓存）
> docker system prune -a --volumes
> 
> # 查看Docker磁盘使用情况
> docker system df
> ```
>
> ### 4. **扩展Docker存储空间**
>
> 如果数据盘已满，需要扩展存储：
>
> ```bash
> # 方案A：扩展卷组（如果使用LVM）
> lvextend -L +10G /dev/mapper/centos-root
> xfs_growfs /
> 
> # 方案B：迁移Docker数据目录
> # 停止Docker服务
> systemctl stop docker
> 
> # 迁移数据到新位置（假设新挂载点为 /data/docker）
> rsync -avz /var/lib/docker/ /data/docker/
> 
> # 修改Docker配置
> cat > /etc/docker/daemon.json <<EOF
> {
>   "data-root": "/data/docker"
> }
> EOF
> 
> # 启动Docker
> systemctl start docker
> 
> # 删除旧数据（确认迁移成功后）
> rm -rf /var/lib/docker/*
> ```
>
> ### 5. **调整MySQL配置以预防**
>
> 编辑MySQL配置文件（在宿主机上挂载的配置文件，或通过docker-compose）：
>
> ```ini
> [mysqld]
> # 限制binlog保留时间（3天）
> expire_logs_days = 3
> binlog_expire_logs_seconds = 259200
> 
> # 限制临时表大小
> tmp_table_size = 64M
> max_heap_table_size = 64M
> 
> # 启用innodb文件压缩
> innodb_file_per_table = 1
> innodb_compression_level = 6
> 
> # 限制最大连接数（避免过多连接消耗内存）
> max_connections = 200
> ```
>
> 应用配置：
> ```bash
> docker restart mysql8
> ```
>
> ### 6. **创建清理脚本**
>
> 创建定期清理的脚本 `/opt/mysql_cleanup.sh`：
>
> ```bash
> #!/bin/bash
> # MySQL自动清理脚本
> 
> # 清理3天前的binlog
> docker exec mysql8 mysql -uroot -pYOUR_PASSWORD -e "PURGE BINARY LOGS BEFORE NOW() - INTERVAL 3 DAY;"
> 
> # 清理docker日志（限制日志大小）
> docker system prune -f --volumes
> 
> # 记录日志
> echo "[$(date)] MySQL cleanup completed" >> /var/log/mysql_cleanup.log
> ```
>
> 添加到crontab每天执行：
> ```bash
> 0 2 * * * /opt/mysql_cleanup.sh
> ```
>
> ### 7. **监控磁盘空间**
>
> 设置告警监控：
> ```bash
> # 安装监控工具
> yum install -y nagios-plugins
> 
> # 创建监控脚本
> cat > /opt/disk_monitor.sh <<'EOF'
> #!/bin/bash
> USED=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//')
> if [ $USED -gt 80 ]; then
>   echo "警告：磁盘使用率已达${USED}%" | mail -s "磁盘空间告警" your-email@example.com
> fi
> EOF
> chmod +x /opt/disk_monitor.sh
> 
> # 每30分钟检查一次
> */30 * * * * /opt/disk_monitor.sh
> ```
>
> ## 快速恢复服务
>
> 如果MySQL容器已停止，按以下步骤恢复：
>
> ```bash
> # 清理空间后重启容器
> docker start mysql8
> 
> # 检查容器状态
> docker ps | grep mysql8
> 
> # 查看日志确认恢复
> docker logs -f --tail 100 mysql8
> 
> # 如果无法启动，可能需要修复表
> docker exec -it mysql8 bash
> mysqlcheck -uroot -p --all-databases --auto-repair
> ```
>
> ## 长期优化建议
>
> 1. **数据归档**：定期将历史数据归档到对象存储（如MinIO）
> 2. **分库分表**：his_charge库表过大，考虑按时间或业务分表
> 3. **慢查询优化**：检查并优化高频写入的SQL
> 4. **使用云存储**：考虑将数据目录放在扩容更方便的云盘上
> 5. **定期备份**：设置每日备份并清理旧备份
>
> 通过以上步骤，您可以立即解决磁盘空间不足的问题，并通过配置优化和监控预防未来再次发生。









* 
* 作用
  * 忽略了安装的复杂过程
  * 忽略了系统间的差异
  * 忽略了系统和应用之间的干扰
  * 大幅提高部署效率








### docker安装案例

> ![image-20250918200820802](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918200820802.png)
>
> 
>
> 



### 





#### Docker安装MySQL-docker原理

![image-20250918202129100](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918202129100.png)

```
docker run -d \
  --name mysql \
  -p 3307:3306 \
  -e TZ=Asia/Shanghai \
  -e MYSQL_ROOT_PASSWORD=1234 \
  mysql:8




```

> 使用Docker安装之后不用单独写指令开放端口
>
> 
>
> 





![image-20250918203855698](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918203855698.png)

> 这个镜像不依赖于操作系统，这个镜像不仅可以在linux上用，还可以在其他系统上用
>
> 【镜像把它所依赖的系统环境及其函数库也一起打包了，所以镜像中的应用可以在各种操作系统直接运行】

> 各个应用所需的环境、函数库往往都不一样，通过容器这种隔离环境的机制，让各个应用各自运行、相互不干扰
>
> 

> 镜像只用下载一次，一个镜像可以多开（可以开启多次）、不需要重复下载





* 如何获取的镜像？

  ![image-20250918211648603](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918211648603.png)

  > 从docker/国内公司 维护的镜像仓库，就能获取到想要的镜像（或者公司也可以搭建自己的镜像仓库的私服(类似maven私服)）
  >
  > 【[DockerHub](https://hub.docker.com)——该网站作用：方便我们查阅里面已经保存的镜像和】
  
  * docker安装好之后，系统就会启动一个守护进程（用来处理客户端发过来的一些请求[比如docker run ]），监听到请求之后就会从镜像仓库找到这个镜像
  * 然后将这个镜像(image)下载到本地
  * docker可以基于一个镜像构建出多个容器（多个容器之间相互隔离互不影响），各个容器之中可以独立的运行不同的软件、相互独立互不影响（每个容器有独立的网络环境、ip地址等）

> 执行指令的时候，docker先会从本地找镜像、本地没有才从镜像仓库下载
>
> 

```
# 使用docker ps指令就能验证镜像是否启动了
# 查看和docker相关的进程
docker ps

```

![image-20250918212657555](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918212657555.png)

> 容器id：容器的唯一标识
>



#### Docker命令解读

* ```
  docker run -d \
    --name mysql \
    -p 3307:3306 \
    -e TZ=Asia/Shanghai \
    -e MYSQL_ROOT_PASSWORD=123 \
    mysql:8
  ```

  ```
  docker run -d --name mysql -p 3307:3306 
  ```

  

![image-20250918213652774](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918213652774.png)

> 端口映射
>
> 宿主机的端口:容器的端口
>
> 当外部访问宿主机的3307端口的时候，dokcer就会把这个请求转到容器内的3306端口
>
> 如果有两台mysql容器，就要映射到宿主机的不同端口（宿主机只有一个3306端口）

> 不同镜像，需要配置的环境变量不一样、是由官方决定的（需要去[官网](https://hub.docker.com)查询需要/必配置的  环境变量）
>
> 在hub.docker.com找到对应的镜像，然后找到他需要配置的环境变量

> 最后的mysql是镜像的名字，搜索部署和下载都是靠这个名字来指定的
>
> 

> 
>
> ```
> # 查看指定容器的信息
> docker inspect containerName
> ```

![image-20250918213716931](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918213716931.png)

> 完整版命名↑  

![image-20250918213821085](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918213821085.png)



# Docker基础





## Docker安装

**不同系统的安装流程<u>推荐</u>查看[官方文档](https://docs.docker.com/engine/install/ubuntu)**



（centos版本流程如下）



> 1. 移除系统中的旧版本Docker
>
>    ```
>    yum remove docker \
>    	docker-client \
>    	docker-client-latest \
>    	docker-common \
>    	docker-latest \
>    	docker-latest-logrotate \
>    	docker-engine \
>    	docker-selinux
>    ```
>
>    
>
> 2. **配置Docker的yum库**
>
>    首先安装一个yum工具
>
>    ```
>    sudo yum install -y yum-utils device-mapper-persistent-data lvm2
>    ```
>
>    安装成功后，执行命令，配置Docker的yum源（已更新为阿里云源）
>
>    ```
>    sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
>    
>    sudo sed -i 's+download.docker.com+mirrors.aliyun.com/docker-ce+' /etc/yum.repos.d/docker-ce.repo
>    ```
>
>    更新yum，建立缓存
>
>    ```Bash
>    sudo yum makecache fast
>    ```
>
> 3. **安装Docker**
>
>    执行命令、安装Docker
>
>    ```Bash
>    yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
>    ```
>
> 4. **启动和校验**
>
>    ```
>    # 启动Docker
>    systemctl start docker
>    
>    # 停止Docker
>    systemctl stop docker
>    
>    # 重启
>    systemctl restart docker
>    
>    # 设置开机自启动
>    systemctl enable docker
>    
>    # 执行docker ps命令，如果不报错，说明安装启动成功
>    docker ps
>    ```
>
> 5. **配置镜像加速**
>
>    镜像地址可能会变更，如果失效可以百度找最新的docker镜像。
>
>    配置镜像步骤如下：
>
>    ```
>    # 创建目录
>    rm -f /etc/docker/daemon.json
>                                                    
>    # 复制内容
>    tee /etc/docker/daemon.json <<-'EOF'
>    {
>        "registry-mirrors": [
>            "http://hub-mirror.c.163.com",
>            "https://mirrors.tuna.tsinghua.edu.cn",
>            "http://mirrors.sohu.com",
>            "https://ustc-edu-cn.mirror.aliyuncs.com",
>            "https://ccr.ccs.tencentyun.com",
>            "https://docker.m.daocloud.io",
>            "https://docker.awsl9527.cn"
>        ]
>    }
>    EOF
>                                                    
>    # 重新加载配置
>    systemctl daemon-reload
>                                                    
>    # 重启Docker
>    systemctl restart docker
>    ```
>
>    



## (解决云服务器网络问题)

> ```
> Unable to find image 'hello-world:latest' locally
> docker: Error response from daemon: Get "https://registry-1.docker.io/v2/": context deadline exceeded
> Run 'docker run --help' for more information
> ```
>
> 

> 
>
> ### ❌ 错误含义：
>
> > **Docker 无法连接到 Docker Hub（官方镜像仓库）**，超时了。
>
> 这说明：
> 👉 你的服务器或网络环境 **无法访问 `registry-1.docker.io`**（即 Docker 官方镜像仓库），导致拉取镜像失败。
>
> ------
>
> ## ✅ 根本原因分析
>
> | 原因                    | 说明                                                 |
> | ----------------------- | ---------------------------------------------------- |
> | <u>🔴 **网络问题**</u>   | 服务器在大陆地区，直接访问 Docker Hub 被墙或延迟极高 |
> | 🔴 **DNS 解析失败**      | 域名 `registry-1.docker.io` 无法解析                 |
> | 🔴 **防火墙 / 代理拦截** | 某些云服务商（如阿里云、腾讯云）默认限制出站流量     |
> | 🔴 **系统时间不正确**    | TLS 证书验证失败，导致连接被拒绝                     |
>
> 
>
> ### ✅ 方案一：使用 **国内镜像加速器**（最推荐！）
>
> #### 🛠️ 修改 Docker 镜像源为国内加速器
>
> 编辑 Docker 的配置文件：
>
> ```bash
> sudo nano /etc/docker/daemon.json
> ```
>
> 添加以下内容（选择一个可用的镜像源）：
>
> ```json
> {
>   "registry-mirrors": [
>     "https://registry.docker-cn.com",
>     "https://docker.mirrors.ustc.edu.cn",
>     "https://hub-mirror.c.163.com",
>     "https://mirror.baidubce.com"
>   ]
> }
> ```
>
> > 🔁 推荐组合：
> >
> > - 阿里云：`https://<your-id>.mirror.aliyuncs.com`（需注册）
> > - 清华大学：`https://docker.mirrors.tuna.tsinghua.edu.cn`
> > - 网易：`https://hub-mirror.c.163.com`
>
> 📌 如果你不知道自己的 ID，可以先用清华大学的：
>
> ```json
> {
>   "registry-mirrors": ["https://docker.mirrors.tuna.tsinghua.edu.cn"]
> }
> ```
>
> ------
>
> ### ✅ 方案二：重启 Docker 服务
>
> 修改完配置后，重启 Docker：
>
> ```bash
> sudo systemctl restart docker
> ```
>
> 再测试：
>
> ```bash
> sudo docker run hello-world
> ```
>
> ✅ 应该就能成功了！
>
> ------
>
> #### ✅ 方案三：检查系统时间是否正确
>
> 如果时间不对，TLS 证书会验证失败，导致连接中断。
>
> ```bash
> timedatectl status
> ```
>
> 如果显示 `NTP enabled: no` 或时间明显错误，同步时间：
>
> ```bash
> sudo timedatectl set-ntp true
> ```
>
> 或者手动设置：
>
> ```bash
> sudo date -s "2024-04-05 10:00:00"
> ```
>
> ------
>
> #### ✅ 方案四：更换 DNS（解决域名解析问题）
>
> 有些服务器 DNS 不通，可以临时换为公共 DNS：
>
> ```bash
> sudo nano /etc/resolv.conf
> ```
>
> 添加：
>
> ```conf
> nameserver 8.8.8.8
> nameserver 8.8.4.4
> ```
>
> > ⚠️ 注意：如果你是云服务器（如阿里云），可能需要通过控制台修改 DNS，否则会被重置。
>
> ------
>
> #### ✅ 方案五：使用代理（适用于公司内网）
>
> 如果你在公司或学校，可能需要走代理：
>
> ```bash
> export http_proxy=http://your-proxy.com:8080
> export https_proxy=http://your-proxy.com:8080
> ```
>
> 然后运行：
>
> ```bash
> sudo docker run hello-world
> ```
>
> ------
>
> #### ✅ 方案六：测试网络连通性
>
> 确认能否访问 Docker Hub：
>
> ```bash
> curl -v https://registry-1.docker.io
> ```
>
> 如果返回 `Connection refused` 或超时，说明网络不通。
>
> 也可以 ping 测试：
>
> ```bash
> ping registry-1.docker.io
> ```
>
> > ⚠️ 但注意：Docker Hub 使用 HTTPS，不能 ping 成功不代表不能访问。
>
> ------
>
> ## ✅ 最终建议流程
>
> 1. **先加国内镜像源**（最有效）
> 2. **重启 Docker**
> 3. **测试 `docker run hello-world`**
> 4. 如果还不行，检查 **系统时间 + DNS + 网络策略**
>
> ------
>
> ## ✅ 示例：完整操作步骤
>
> ```bash
> # 1. 创建或编辑 daemon.json
> sudo nano /etc/docker/daemon.json
> 
> # 2. 写入清华镜像源
> {
>   "registry-mirrors": ["https://docker.mirrors.tuna.tsinghua.edu.cn"]
> }
> 
> # 3. 重启 Docker
> sudo systemctl restart docker
> 
> # 4. 测试
> sudo docker run hello-world
> ```
>
> ------
>
> ## ✅ 补充：常见镜像加速器地址
>
> | 名称     | 地址                                          |
> | -------- | --------------------------------------------- |
> | 清华大学 | `https://docker.mirrors.tuna.tsinghua.edu.cn` |
> | 阿里云   | `https://<your-id>.mirror.aliyuncs.com`       |
> | 网易     | `https://hub-mirror.c.163.com`                |
> | 华为云   | `https://mirrors.huaweicloud.com`             |
> | 百度云   | `https://mirror.baidubce.com`                 |
>
> > 📌 如何获取阿里云镜像地址？登录 [https://cr.console.aliyun.com](https://cr.console.aliyun.com/) → 镜像加速器
>
> ------
>
> 如果你告诉我：
>
> - 你在哪家云服务商？（阿里云 / 腾讯云 / AWS / 自建服务器？）
>
> - 是不是在公司/学校网络？
>
>   

**配置阿里镜像加速**



https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors

![image-20251107201515803](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251107201515803.png)

* 具体流程参照官网的操作文档即可

  完成流程之后可以用docker run hello-world测试，此时下载镜像就很快了

```
docker info //可以查看当前镜像源 

//在/etc/docker/目录下更改镜像源
```

{
  "registry-mirrors": ["https://svkirxldgijghl.xuanyuan.run/"]
}

```
systemctl daemon-reload//更换完镜像源，需要重启
systemctl restart docker
```







## 常见命令

![image-20250918214515290](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918214515290.png)

![image-20250918214526382](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918214526382.png)

1. 略

2. 拉取Nginx镜像

   ```
   docker pull 镜像名:版本号
   docker pull nginx:1.20.2
   ```

3. **查看本地镜像**列表

   ```
   docker images
   ```

   ![image-20250919075126750](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919075126750.png)

   > #### 删除镜像
   >
   > ```
   > docker rmi nginx:1.20.2
   > ```
   >
   > 跟名称/id

4. **创建并运行**容器

   ```
   docker run -d --name theName -p 80:80 nginx:1.20.2
   ```

   > nginx不用环境变量，所以没加-e参数

   > 注意：docker run是创建并运行，**每次执行都会创建一个新的容器**（注意判断是否有必要使用，可能容器只是stop了，没必要再创建一个）

5. 查看容器（查看和容器相关的进程）

   ```
   docker ps
   ```

   > 默认只查看运行中的容器 推荐加上 -a

   **查看所有容器**

   ```
   docker ps -a
   ```

   

6. 停止容器

   ```
   docker stop nginx
   ```

7. 再次启动容器

   ```
   docker start nginx
   ```

8. 进入容器

   ```
   docker exec -it nginx bash
   ```

   补充：进入mysql命令行`docker exec -it mysql mysql -uroot -p`

   > bash：指通过命令行和容器交互

   > bash代表进入nginx容器之后，要打开一个bash shell命令行进行交互

   ![image-20250919081014585](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919081014585.png)

   退出容器

   ```
   exit
   ```

9. 删除容器

   ```
   docker stop nginx
   docker rm nginx
   ```

   > 注意和docker rm 区分34

> 如何将镜像打包并发给别人：
>
> ```
> docker save -o nginx-1.20.2.tar nginx:1.20.2
> ```
>
> 打包之后下载到本地就可以发给别人
>
>
> ```
> docker load -i nginx-1.20.2.tar
> ```
>
> 
>
> 其他方式
>
> 把(可能是自己构建的)镜像
> 使用docker push推到镜像仓库（公用/公司的私服（））
>
> 

* 其他

  * 保存镜像

    ```
    # docker save -o 保存到的文件名 镜像名:版本
    docker save -o nginx.tar nginx:latest
    ```

  * 读取本地的镜像

    ```
    # docker load -i 要读取的文件名
    docker load -i nginx.tar
    ```

  * 查看容器日志

    ```
    # docker logs 镜像名
    docker logs nginx
    ```

  * 进入mysql容器里面的mysql

    ```
    docker exec -it mysql mysql -uroot -p
    
    ```

    

> 不清楚某个指令可以加上 “--help”查看帮助文档
>
> ```
> docker save --help
> ```
>



#### 其他

* **自定义镜像**

  

## 运行容器一条龙

* 在DockerHub中搜索xx镜像，查看镜像名称

* 拉取xx镜像

  ```
  git pull 镜像名:版本号
  或
  git pull 镜像名
  ```

  > #### 怎么查镜像
  >
  > 你提了一个非常关键的问题！我们来一步步解答：
  >
  > ------
  >
  > ## ✅ 问题1：你的项目是 **JDK 1.8**，但之前我推荐了 `openjdk:17-jre-slim`，这是错误的！
  >
  > > ❌ **不能混用！**
  > > 如果你的 Spring Boot 项目是用 **JDK 1.8** 编译的（即 `sourceCompatibility=1.8`），那么运行时也必须使用 **Java 8 或更高版本**的 JVM。
  >
  > 但更准确地说：✅ **你应该使用 `openjdk:8-jre-slim` 作为基础镜像**。
  >
  > ------
  >
  > ### ✅ 正确的基础镜像选择（针对 JDK 1.8）
  >
  > ```dockerfile
  > FROM openjdk:8-jre-slim
  > ```
  >
  > 这才是匹配你项目的正确基础镜像。
  >
  > ------
  >
  > ## 📚 问题2：怎么知道该写什么基础镜像？官网哪里找？
  >
  > ### 🔍 官方镜像在 Docker Hub 上
  >
  > > 网站地址：👉 https://hub.docker.com/
  >
  > 搜索关键词：`openjdk`
  >
  > 👉 进入页面：https://hub.docker.com/_/openjdk
  >
  > ------
  >
  > ### 🧭 在 Docker Hub 上如何查找合适的镜像？
  >
  > #### 步骤 1：打开 OpenJDK 镜像页面
  >
  > - 地址：https://hub.docker.com/_/openjdk
  > - 这是官方维护的 OpenJDK 镜像（由 Docker 官方认证 ✅）
  >
  > #### 步骤 2：查看支持的标签（Tags）
  >
  > 点击顶部的 **"Tags"** 标签页： 👉 https://hub.docker.com/_/openjdk/tags
  >
  > 你会看到很多镜像标签，<u>比如</u>：
  >
  > | 标签          | 含义                                                   |
  > | ------------- | ------------------------------------------------------ |
  > | `8-jre-slim`  | Java 8 的 JRE，轻量版（推荐）                          |
  > | `8-jre`       | Java 8 的 JRE，体积稍大                                |
  > | `8-jdk`       | Java 8 的完整开发工具包（含 javac 等），不推荐用于运行 |
  > | `17-jre-slim` | Java 17 的 JRE，轻量版                                 |
  > | `latest`      | 最新版本（不建议用于生产）                             |
  >
  > 

* 查看本地镜像列表(是否拉取成功)

  ```
  docker images
  ```

* 查看镜像需要的环境变量

* 创建并运行x容器(注意设置容器名、端口映射、配置环境变量)

  ```
  docker -run -d --name 镜像名 -p 宿主机端口:容器端口 -e 环境变量1=xx 容器名
  ```

* 查看容器(容器是否创建成功)

  ```
  docker ps -a
  ```

  

## 技巧

#### 设置 指令别名

* 案例

  设置<u>简化输出进程信息</u>的指令

* 进入 ~/.bashrc

  ```
  vi ~/.bashrc
  ```

  

* 设置别名

  ```
  alias dps='docker ps --format "table {{.ID}}\t{{.Ports}}\t{{.Status}}\t{{.Names}}" -a'
  ```

* 让别名生效

  ```
  source ~/.bashrc
  ```

  

## 数据卷



>![image-20250919082111594](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919082111594.png)
>
>![image-20250919082051344](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919082051344.png)
>
>* 所有镜像都是最小化的系统环境，它只具备运行所必须的环境
>  容器内修改文件、拷贝文件很困难
>
>所以需要借助数据卷来操作



![image-20250919082825246](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919082825246.png)

* 数据卷是虚拟的，但是宿主机会自动在指定目录下创建对应的和数据卷绑定的目录，数据卷又和容器有关联；从而让容器和宿主机映射

  > 双向绑定，一边变 另一边也变

* 指令一览

  * docker volume create 创建数据卷
  * docker volume ls 查看所有数据卷
  * docker volume rm 数据卷名  ——删除指定数据卷
  * docker volume inspect 数据卷名  ——查看某个数据卷的详情
  * docker volume prune ——清楚所有未使用的数据卷

  ```
  docker volume --help
  ```

  


![image-20250919083053718](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919083053718.png)

> **创建容器的同时**才能挂载数据卷
>
> 如果容器已经创建了 ，就没办法挂载数据卷了

> **容器内目录去官网看**

* 案例

  ```
  docker run -d --name nginx -p 80:80 -v html:/usr/share/nginx/html nginx
  ```

  > 这里的html是数据卷名字，冒号后阿民事容器内目录

  * 通过docker volume ls可以查看已经挂载的数据卷



```
# 展示数据卷的详细信息
docker volume inspect 数据卷名字
```

> 包括映射到的文件位置

![image-20250919083611045](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919083611045.png)

![image-20250919083851703](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919083851703.png)

![image-20250919084243722](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919084243722.png)

### 本地目录挂载(自定义挂载位置)



> 匿名数据卷：
>
> 起到数据备份的作用
> 容器自带；
> 缺点：名字长；挂载位置深；不方便迁移
>
> 建议挂载到固定的、自己设置的目录

![image-20250919090915062](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919090915062.png)



案例

> ![image-20250919091842250](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250919091842250.png)
>
> ```
> docker run -d \
>   --name mysql \
>   -p 3306:3306 \
>   -e TZ-Asia/shanghai \
>   -e MYSQL_ROOT_PASSWORD-123 \
>   -v /root/mysql/data:/var/lib/mysql \
>   -v /root/mysql/init:docker-entrypoint-initdb.d \
>   -v /root/mysql/conf:/etc/mysql/conf.d \
>   mysql
> ```
>
> 





















```
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8" />
  <title>动态表格 CRUD</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f4f4f4;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }
    th, td {
      padding: 10px;
      text-align: left;
      border-bottom: 1px solid #ddd;
    }
    th {
      background-color: #4CAF50;
      color: white;
    }
    tr:hover {
      background-color: #f5f5f5;
    }
    .btn {
      padding: 6px 12px;
      margin: 5px;
      cursor: pointer;
      border: none;
      border-radius: 4px;
    }
    .add-btn { background-color: #4CAF50; color: white; }
    .edit-btn { background-color: #2196F3; color: white; }
    .delete-btn { background-color: #f44336; color: white; }
    .form-group {
      margin: 10px 0;
    }
    label {
      display: inline-block;
      width: 100px;
    }
    input[type="text"], input[type="number"] {
      padding: 8px;
      width: 200px;
    }
    .hidden { display: none; }
  </style>
</head>
<body>

<h2>用户管理表格</h2>

<!-- 表格 -->
<table id="userTable">
  <thead>
    <tr>
      <th>ID</th>
      <th>姓名</th>
      <th>年龄</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody id="tableBody"></tbody>
</table>

<!-- 操作按钮 -->
<button class="btn add-btn" onclick="showForm()">新增用户</button>

<!-- 表单 -->
<div id="formContainer" class="hidden">
  <form id="userDataForm">
    <div class="form-group">
      <label>姓名：</label>
      <input type="text" id="name" required />
    </div>
    <div class="form-group">
      <label>年龄：</label>
      <input type="number" id="age" required />
    </div>
    <div class="form-group">
      <button type="submit" class="btn add-btn">保存</button>
      <button type="button" class="btn" onclick="hideForm()">取消</button>
    </div>
  </form>
</div>

<script>
  // 数据存储（模拟数据库）
  let users = JSON.parse(localStorage.getItem('users')) || [];

  // 渲染表格
  function renderTable() {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';

    users.forEach((user, index) => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.age}</td>
        <td>
          <button class="btn edit-btn" onclick="editUser(${index})">编辑</button>
          <button class="btn delete-btn" onclick="deleteUser(${index})">删除</button>
        </td>
      `;
      tbody.appendChild(row);
    });
  }

  // 显示表单
  function showForm() {
    document.getElementById('formContainer').classList.remove('hidden');
    document.getElementById('name').value = '';
    document.getElementById('age').value = '';
    document.getElementById('userDataForm').onsubmit = function(e) {
      e.preventDefault();
      addUser();
    };
  }

  // 隐藏表单
  function hideForm() {
    document.getElementById('formContainer').classList.add('hidden');
    document.getElementById('userDataForm').onsubmit = null;
  }

  // 添加用户
  function addUser() {
    const name = document.getElementById('name').value;
    const age = parseInt(document.getElementById('age').value);

    if (!name || !age) {
      alert('请输入完整信息！');
      return;
    }

    const newUser = {
      id: Date.now(), // 用时间戳作为 ID
      name,
      age
    };

    users.push(newUser);
    saveData();
    renderTable();
    hideForm();
  }

  // 编辑用户
  function editUser(index) {
    const user = users[index];
    document.getElementById('name').value = user.name;
    document.getElementById('age').value = user.age;
    document.getElementById('formContainer').classList.remove('hidden');
    document.getElementById('userDataForm').onsubmit = function(e) {
      e.preventDefault();
      const newName = document.getElementById('name').value;
      const newAge = parseInt(document.getElementById('age').value);

      if (!newName || !newAge) {
        alert('请输入完整信息！');
        return;
      }

      users[index] = { ...user, name: newName, age: newAge };
      saveData();
      renderTable();
      hideForm();
    };
  }

  function deleteUser(index) {
    if (confirm('确定要删除这个用户吗？')) {
      users.splice(index, 1);
      saveData();
      renderTable();
    }
  }


  function saveData() {
    localStorage.setItem('users', JSON.stringify(users));
  }

  window.onload = function() {
    renderTable();
  };
</script>

</body>
</html>

```



## 自定义镜像



> 自己自定义自己java程序的镜像

* 镜像包含了应用程序、程序运行的系统函数库、运行配置等文件的文件包。构建镜像的过程其实就是把上述文件打包的过程



![image-20251102211917127](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251102211917127.png)

![image-20251102212255331](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251102212255331.png)

> Layer结构能够解耦合，提升效率

**使用dockerFile**

![image-20251102212351656](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251102212351656.png)

> 相当于封装了我们人工部署JAR包的过程
> 利用固定的指令来描述镜像的结构和构建过程，让docker依次构建镜像

* 我们只需要描述好镜像的结构，基础是xx，中间层有哪些层，docker就会自动帮我们完成整个镜像的构建了

*  案例

  > ![image-20251102212942479](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251102212942479.png)
  >
  > ![image-20251102230123809](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251102230123809.png)
  >
  > > Dockerfile放置的目录有讲究，Dockerfile目录所在位置是和文件有关联的，因为里面有个拷贝操作，所以要和被拷贝的那个jar包放一起

  * 准备Dockerfile和jar包

  * 下载所需的jdk基础镜像

  * 进入到Dockerfile所在目录

  * 执行指令，部署容器

    ```
    # docker build -t 构建出的镜像的名字 .
    docker build -t docker-demo .
    ```

    > 指令没有指定的话，Dockerfile文件名就要叫Dockerfile

    > 这里的“.”指定了Dockerfile的路径

    检查是否添加上了
    
    ```
    docker images
    ```

    创建并运行容器
    
    ```
    docker run -d --name dockerName -p 8080:8080 docker-demo
    ```
    
    

## 容器网络互连



![image-20251103110535072](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103110535072.png)

> 这种通过ip相互连接的方式不太好
>
> ip会变，如果另外一个容器先启动、占掉了mysql的之前的ip地址，mysql容器再启动之后ip地址就变化了

优化：自定义网络

![image-20251103110941288](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103110941288.png)

> 自定义网络之中的容器 可以通过容器名互相访问，不需要知道对方的ip地址

![image-20251103143619735](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103143619735.png)

![image-20251103143645710](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103143645710.png)





* 如果有同一个网关，就处于同一个网段

> 同一个网段中的容器是可以互相访问的
>
> 



* 指令使用

  创建自定义网段

  ```
  docker network create heima
  ```

  查看当前的网段 

  ```
  docker network ls
  ```

  加入容器

  ```
  # docker network connect 网络名 容器名
  docker network connect heima mysql
  ```

  或者在创建容器的同时加入网段

  ```
  docker run -d --name dd -p 8080:8080 -network heima docker-demo
  ```

  





## DockerCompose部署项目



![image-20251103185802420](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103185802420.png)

> 描述了项目内多个容器及其参数信息
> 

 

* 运行的时候读取这个文件
  按照这个文件的描述自动创建和运行所需要的容器
  
  

**语法**



![image-20251103191101801](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103191101801.png)

> java应用不在需要我们 手动通过docker build构建成镜像，再创建运行容器
>
> 而这里配置了“build: ”  “路径”和“Docker“，它就会按照这个路径读取Dockerfile，
> 按照其中描述构建镜像、并创建运行容器

> 网络也不需要自己创建网络，直接在里面描述



* 按照这个路径
  docker-compose.yml要和Dockerfile、mysql、nginx、jar包放在一起
  
  ![image-20251103200032863](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103200032863.png)



* **使用docker compose命令构建**

  ![image-20251103191745727](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103191745727.png)

  























## Docker部署nacos













## Docker部署mysql













#### 创建和本地虚拟机mysql之间的连接之前，我想用本地控制台测试一下，应该使用什么指令

> 你现在的目标是：**在本地 Windows 控制台中测试是否能连接到虚拟机（或 Docker 容器）中的 MySQL 服务**，但你发现 `ping` 命令无法用于端口检测。
>
> ---
>
> ## ❌ 为什么 `ping` 不行？
>
> `ping` 是 **ICMP 协议**，只能测试网络连通性（主机是否在线），**不能测试特定端口（如 3306、3307）是否开放**。
>
> 比如：
>
> ```cmd
> ping 192.168.100.128:3306
> ```
>
> 这是无效的！`ping` 不支持带端口号的格式。
>
> > ✅ 正确做法：用 **端口探测工具** 来测试指定端口是否可达。
>
> ---
>
> ## ✅ 推荐方法：使用 `telnet` 测试端口连通性
>
> ### 🔧 方法一：使用 `telnet`（推荐）
>
> #### 1. 确保 `telnet` 客户端已启用（Windows 默认关闭）
>
> - 打开“控制面板” → “程序” → “启用或关闭 Windows 功能”
> - 勾选 **“Telnet 客户端”**
> - 点击确定并重启
>
> #### 2. 使用 `telnet` 测试端口
>
> ```cmd
> telnet 192.168.100.128 3307
> ```
>
> > - `192.168.100.128`：你的虚拟机 IP 地址
> > - `3307`：你映射的端口（Docker 中 `-p 3307:3306`）
>
> #### ✅ 成功标志：
>
> - 屏幕变黑，无提示 → 表示连接成功！
> - 可以按 `Ctrl+C` 退出
>
> #### ❌ 失败标志：
>
> - 出现错误：“无法打开到主机的连接” 或 “连接被拒绝”
>   → 说明端口未开放或服务未启动
>
> ---
>
> ### 🔧 方法二：使用 PowerShell 测试端口（替代方案）
>
> 如果 `telnet` 不可用，可以用 PowerShell 写个简单脚本：
>
> ```powershell
> Test-NetConnection -ComputerName 192.168.100.128 -Port 3307
> ```
>
> 输出示例：
>
> ```text
> TcpTestSucceeded : True
> ```
>
> ✅ 如果是 `True`，说明端口畅通！
>
> ---
>
> ### 🔧 方法三：使用第三方工具（推荐新手）
>
> 下载以下任意一个工具：
>
> | 工具                                                         | 说明             |
> | ------------------------------------------------------------ | ---------------- |
> | [Advanced Port Scanner](https://www.advanced-port-scanner.com/) | 图形化扫描端口   |
> | [Curl](https://curl.se/)                                     | 能测试 TCP 连接  |
> | [nmap](https://nmap.org/)                                    | 强大网络扫描工具 |
>
> 例如用 `nmap` 扫描：
>
> ```bash
> nmap -p 3307 192.168.100.128
> ```
>
> ---
>
> ## ✅ 验证步骤总结
>
> | 步骤 | 操作                                                         | 目标                    |
> | ---- | ------------------------------------------------------------ | ----------------------- |
> | 1️⃣    | `ping 192.168.100.128`                                       | 确认虚拟机是否在线      |
> | 2️⃣    | `telnet 192.168.100.128 3307`                                | 测试 MySQL 是否监听端口 |
> | 3️⃣    | `Test-NetConnection -ComputerName 192.168.100.128 -Port 3307` | PowerShell 替代方案     |
>
> ---
>
> ## 🛠️ 附加建议
>
> 1. **确认 Docker 映射端口正确**
>
>    ```bash
>    docker port mysql8
>    ```
>
>    查看实际映射情况。
>
> 2. **确认虚拟机防火墙允许该端口**
>
>    - 在 Linux 虚拟机中运行：
>
>      ```bash
>      sudo ufw allow 3307
>      ```
>
>      或者关闭防火墙测试：
>
>      ```bash
>      sudo systemctl stop firewalld
>      ```
>
> 3. **确保 MySQL 允许远程连接**
>
>    - 登录 MySQL 后执行：
>
>      ```sql
>      GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456';
>      FLUSH PRIVILEGES;
>      ```
>
>    - 修改配置文件 `/etc/mysql/my.cnf`：
>
>      ```ini
>      bind-address = 0.0.0.0
>      ```
>
> ---
>
> ## ✅ 最终测试流程
>
> ```cmd
> # 1. 测试主机是否可达
> ping 192.168.100.128
> 
> # 2. 测试端口是否开放
> telnet 192.168.100.128 3307
> ```
>
> 如果这两个都成功，就可以用 MySQL 客户端连接了！
>
> ---
>
> ## 💡 小贴士
>
> - `ping` 只能测主机，不能测端口。
> - `telnet` 是最简单的端口测试工具。
> - 本地连接 MySQL 时，地址写 `192.168.100.128`，端口写 `3307`。
> - 如果失败，请检查：
>   - Docker 是否运行？
>   - 防火墙是否放行？
>   - MySQL 是否绑定 `0.0.0.0`？
>
> ---
>
> 如有问题，欢迎继续提问 😊





























## Docker部署redis















# Docker完成项目部署-实战







**[首先安装docker](#Docker安装)**





#### 部署后端

**准备好application-prod.yml文件**

* 调整主机(云服务器的ip地址)
* 调整云服务器mysql密码
  调整云服务器redis密码
* 修改google登录的redirect-url

> 真实项目部署中，数据库不会设置端口映射，都是通过容器名访问
> 所以后端的jdbc连接要按容器名配置



编写Dockerfile文件

![image-20251103145226478](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103145226478.png)

> ![image-20251103145242763](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103145242763.png)
>
> 
>
> 





使用maven的package打包项目成jar包

* 找到父工程
* Lifecysle-package
* jar包在Service模块中的target目录下：blog-service.jar



把jar包和Dockerfile扔到root目录

执行指令

```
docker build -t 构建出的镜像名:1.0 .
```

查看是否构建成功

```
docker images
```

创建自定义网络

```
docker network create 网络名
docker network ls
```

构建后台容器

```
docker run -d --name 构建出的容器名 -p 8080:8080 --network 网络名 镜像名:1.0
```

> 这里创建容器的同时加入了网络

查看容器是否正常启动了

```
docker ps -a
```

> 测试：访问接口:比如http:ip/user/articles/1
> 看看是否能正常访问上





#### 部署前端

 

**打包项目**

* 我们开发用的脚手架其实就是一个微型服务器，用于：支撑开发环境，运行代理服务器等

* 打包完的文件中不存在：.vue、.jsx、.less等文件，而是：html、css、js等浏览器能识别的文件

* 打包后的文件不借助脚手架运行，而是需要部署到服务器上运行

* **打包前，先梳理好前端项目的ajax封装（请求前缀、代理规则等）**;

  开发里面的所有请求都有一个"/dev"前缀（要dev是防止静态资源public中有与接口重名的路径的文件名，所以加个公共前缀）

  > 命名成其他的也可以



```
npm run build
```

打包后会生成dist/build文件夹







##### 云服务器部署方式



**下载nginx镜像**

```

```

创建nginx容器

```
docker run -d
```

将准备好的静态资源、nginx.conf与容器挂载









## 使用Docker compose部署



#### **编写docker-compose.yml文件**

```
version: "3.8"

services:
  mysql:
    image: mysql
    container_name: mysql
    ports:
      - "3306:3306"
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: Hazenixbzh66MySQLbulai
    volumes:
      - "./mysql/conf:/etc/mysql/conf.d"
      - "./mysql/data:/var/lib/mysql"
      - "./mysql/init:/docker-entrypoint-initdb.d"
    networks:
      - blog-net
  redis:
    image: redis:7-alpine  # 推荐使用轻量 alpine 版本
    container_name: redis
    ports:
      - "6379:6379"        # 如果只在 Docker 内部访问，可注释掉此行以提高安全性
    volumes:
      - "./redis/data:/data"  # 持久化数据（可选）
    networks:
      - blog-net
    # 可选：设置密码（生产环境建议开启）
    # command: ["redis-server", "--requirepass", "your_strong_password"]
  blog:
    build:
      context: .
      dockerfile: ./Dockerfile
    container_name: blog
    ports:
      - "8080:8080"
    networks:
      - blog-net
    depends_on:
      - mysql
  nginx:
    image: nginx
    container_name: nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - "./nginx/nginx.conf:/etc/nginx/nginx.conf"
      - "./nginx/html:/usr/share/nginx/html"
      - "./nginx/ssl:/etc/nginx/ssl/:ro"
    depends_on:
      - blog
    networks:
      - blog-net
networks:
  blog-net:
    name: blog
```

##### [上面有敏感信息，写博客注意]

> `version` 字段是必需的，而且必须是有效的 Compose 文件版本号（如 `"3.8"`、`"3"` 等）。
>
> > ✅ 建议使用较新的版本，比如 `"3.8"` 或 `"3.7"`。

> blog容器的内部端口映射应该和你的jar包设置的端口一致（比如我java应用用的8080端口，这里内部端口就要设置成8080）

```
  redis:
    image: redis:alpine
    container_name: redis
    ports:
      - "6379:6379"  # 可选：仅调试时暴露；生产可不暴露
    command: redis-server --requirepass your-redis-password-here --appendonly yes
    environment:
      TZ: Asia/Shanghai
    volumes:
      - "./redis/data:/data"           # 持久化 RDB/AOF 数据
      - "./redis/redis.conf:/usr/local/etc/redis/redis.conf"  # 可选配置文件
    networks:
      - blog-net
    restart: unless-stopped
```

> docker-compose.yml的数据库密码要和配置文件一样，不然连接不上
>
> 

**准备好application-prod.yml文件**

- 调整host，改为**mysql的容器名**
- 调整云服务器mysql密码 调整云服务器redis密码
- 修改google登录的redirect-url

> 真实项目部署中，数据库不会设置端口映射（避免mysql暴露到公网），都是通过容器名访问 所以后端的jdbc连接要按容器名配置

* ![image-20251103210538233](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103210538233.png)


  由激活application-dev.yml改成激活application-prod.yml



#### 后端

**编写Dockerfile文件**

![image-20251103145226478](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251103145226478.png)

```
# 基础镜像
FROM openjdk:8-jre-slim
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 拷贝jar包
# COPY blog-server-1.0-SNAPSHOT.jar /app.jar
COPY blog-server-*.jar /app.jar

# 入口
ENTRYPOINT ["java","-jar","/app.jar"]
```





**使用maven的package打包项目成jar包**




* 找到父工程
* Lifecysle-package
* jar包在Service模块中的target目录下：blog-service.jar



**把jar包和Dockerfile扔到root目录**





#### **准备mysql相关文件**

进入root目录，然后创建目录

```
cd /root/
mkdir -p mysql/conf mysql/data mysql/init
```

/conf存放mysql的配置文件

> 放置 `.cnf` 文件来覆盖或添加 MySQL 的配置项；可能包括字符集设置、缓存大小调整、日志设置等

```
[client]
default_character_set=utf8mb4
[mysql]
default_character_set=utf8mb4
[mysqld]
character_set_server=utf8mb4
collation_server=utf8mb4_0900_ai_ci
init_connect='SET NAMES utf8mb4'
```



**/data**

> MySQL 数据库的主要数据存储位置。
>
> 
>
> - 此目录存放所有 MySQL 数据库的数据文件。当你的数据库中创建表、插入数据等操作时，这些更改都会被保存到此目录下的文件中。
> - 通过将这个目录映射到宿主机，即使容器被删除或重新创建，你的数据仍然安全地保留在宿主机上。





**/init**

> 用于初始化数据库。
>
> - 在这个目录下，你可以放置一些 SQL 脚本或者 shell 脚本，这些脚本会在 MySQL 容器启动并且没有检测到现有的数据库时自动执行。
> - 常见的做法是放置 `.sql` 文件来创建数据库、表或插入初始数据；也可以放置 `.sh` 脚本来进行更复杂的初始化操作。

```

```

> 如何从已有的数据库获取建表sql和插入数据的sql（我用的是Navicat）：
>
> **导出整个数据库**：右键点击**数据库名**，同样选择“转储 SQL 文件” -> “仅数据”、“仅结构” 或 “结构和数据”







#### **准备nginx相关文件**

**创建目录**

```
mkdir -p nginx/html
```



**编写.env.production文件**

> 原来的.env.devlopment文件不用删除
>
> Vite 在 **不同命令下会自动加载不同的 `.env` 文件**：
>
> | 命令                    | 加载的文件                  |
> | ----------------------- | --------------------------- |
> | `npm run dev`（开发）   | `.env` + `.env.development` |
> | `npm run build`（构建） | `.env` + `.env.production`  |
>
> > 🔗 官方文档：https://vitejs.dev/guide/env-and-mode.html#env-files
>
> 

**打包项目**

- 我们开发用的脚手架其实就是一个微型服务器，用于：支撑开发环境，运行代理服务器等

- 打包完的文件中不存在：.vue、.jsx、.less等文件，而是：html、css、js等浏览器能识别的文件

- 打包后的文件不借助脚手架运行，而是需要部署到服务器上运行

- **打包前，先梳理好前端项目的ajax封装（请求前缀、代理规则等）**;

  开发里面的所有请求都有一个"/dev"前缀（要dev是防止静态资源public中有与接口重名的路径的文件名，所以加个公共前缀）

  > 命名成其他的也可以



```
 npm run build
```

打包后会生成dist/build文件夹

**/html**

>  目录用于存放 **前端静态资源文件**，也就是用户通过浏览器访问你的博客时，Nginx 直接返回的 HTML、CSS、JavaScript、图片等内容。
>
> 



```
npm run build
```

然后把 `dist/` 里的所有文件复制到部署目录的 `nginx/html`

最终结构类似

```
nginx/html/
├── index.html
├── assets/
│   ├── index-abc123.js
│   ├── index-def456.css
│   └── logo.woff2
├── favicon.ico
└── robots.txt
```







**编写nginx.conf**

```

worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/json;

    sendfile        on;
    
    keepalive_timeout  65;

        # ========== HTTP 重定向到 HTTPS ==========
    server {
        listen 80;
        server_name hazenix.top www.hazenix.top blog.hazenix.top;

        # 所有 HTTP 请求 301 永久重定向到 HTTPS
        return 301 https://$host$request_uri;
    }

    # ========== HTTPS 服务 ==========
    server {
        listen 443 ssl;
        server_name hazenix.top www.hazenix.top blog.hazenix.top;

        # SSL 证书和私钥路径（根据你的实际路径修改）
        ssl_certificate      /etc/nginx/ssl/hazenix.top.pem;     # 证书链（含中间证书）
        ssl_certificate_key  /etc/nginx/ssl/hazenix.top.key;       # 私钥文件


        # 前端静态资源
        location / {
            root /usr/share/nginx/html;
            try_files $uri $uri/ /index.html;
        }

        # 错误页面
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root html;
        }

        # API 代理
        location /api/ {
            proxy_pass http://blog:8080/;  # 注意结尾的 /
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    
}
```

> ![image-20251104134552877](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104134552877.png)
> 表示如果请求的是云服务器的18080端口，就会进入nginx里面创建的这个server
>
> (这里listen的端口和docker-compose.yml文件中配置的端口保持一致[指冒号右边的端口号  即内部 Nginx 监听的端口]）





#### 运行docker-compose.yml

进入文件所在目录

```
docker compose up -d
```

查看项目下的所有进程

```
docker compose ps
```





> 如果有失误，
>
> **比如mysql初始化失误**
>
> ```
> # 1. 停止并删除容器
> docker compose down
> 
> # 2. 删除旧的 MySQL 数据（⚠️ 会丢失所有数据！）
> sudo rm -rf ./mysql/data/*
> 
> # 3. （可选）确认 init 目录下是你新改好的 SQL 文件
> ls ./mysql/init/
> 
> # 4. 重新启动
> docker compose up -d
> ```
>
> **如果jar包有问题**
>
> ```
> # 换jar包
> 
> docker compose down
> docker compose up -d --build # --build 会强制重新构建镜像，确保用的是新 JAR。
> 
> ```
>
> **如果需要插入数据**
>
> 进入mysql容器
>
> ```
> docker exec -it <mysql容器名> mysql -uroot -p
> ```
>
> 执行
>
> ```
> mysql> USE your_prod_db;
> mysql> SOURCE /path/to/your_data.sql;
> //上面这个路径是容器内的路径（通过数据卷把.sql文件映射进去）
> ```
>
> **如果需要修改mysql密码**
>
> 
>
> 
>
> 
>
> > **MySQL 的数据是持久化存储的，即使你重建了容器，只要数据卷（volume）或绑定挂载（bind mount）没有被清除，MySQL 就会沿用之前初始化时设置的密码和数据**。
> >
> > ------
> >
> > #### 为什么 `docker compose down && docker compose up -d --build` 没用？
> >
> > - `docker compose down` 默认 **不会删除命名卷（named volumes）**，只会停止并删除容器、网络等。
> > - 如果你的 `docker-compose.yml` 中为 MySQL 定义了 `volumes`（例如 `mysql_data:/var/lib/mysql`），那么数据库文件（包括用户、密码、表结构等）都保存在这个卷里。
> > - 重新 `up` 时，MySQL 容器启动后会检测到 `/var/lib/mysql` 已存在数据，于是 **跳过初始化脚本（如 `MYSQL_ROOT_PASSWORD` 等环境变量只在首次初始化时生效）**，直接使用已有数据。
> >
> > ------
> >
> > #### 如何真正重置 MySQL 密码（或重新初始化）？
> >
> > #### ✅ 方法一：删除相关数据卷（推荐用于开发环境）
> >
> > ```bash
> > # 1. 停止并删除容器
> > docker compose down
> > 
> > # 2. 删除关联的卷（注意：这会永久删除所有数据库数据！）
> > docker volume ls                # 查看卷名，通常是 <项目名>_mysql_data
> > docker volume rm <卷名>
> > 
> > # 或者更暴力一点（删除所有未被使用的卷）：
> > docker volume prune
> > ```
> >
> > > 💡 项目名默认是当前目录名，你也可以在 `docker-compose.yml` 中显式指定卷名，比如：
> > >
> > > ```yaml
> > > docker exec -it <mysql容器名> mysql -uroot -p
> > > ```
> >
> > #### ✅ 方法二：临时修改配置，强制重置密码（保留数据但改密码）
> >
> > 如果你**不想丢数据**，但想改密码，可以：
> >
> > 1. 进入正在运行的 MySQL 容器：
> >
> >    ```bash
> >    docker exec -it <mysql容器名> mysql -uroot -p
> >    ```
> >
> > 2. 执行 SQL 修改密码（MySQL 5.7+）：
> >
> >    ```sql
> >    ALTER USER 'root'@'%' IDENTIFIED BY '新密码';
> >    FLUSH PRIVILEGES;
> >    ```
> >
> > 或者通过初始化脚本（需配合 `volumes` 挂载 SQL 文件到 `/docker-entrypoint-initdb.d/`，但**仅在首次初始化时运行**）。
> >
> > #### ✅ 方法三：使用绑定挂载 + 手动清理（适合调试）
> >
> > 在 `docker-compose.yml` 中使用主机目录挂载：
> >
> > ```yaml
> > volumes:
> >   - ./mysql_data:/var/lib/mysql
> > ```
> >
> > 然后你可以直接删除本地的 `./mysql_data` 文件夹来重置：
> >
> > ```bash
> > rm -rf ./mysql_data
> > docker compose up -d --build
> > ```
> >
> > ------
> >
> > ### 补充建议
> >
> > - 如果你只是想测试新配置，建议在开发阶段**明确控制数据持久化行为**。
> > - 生产环境切勿随意删除卷！应通过 SQL 或管理工具修改密码。
> > - 可以在 `docker-compose.yml` 中加注释说明卷的作用，避免误操作。
> >
> > ------
> >
> > ### 总结
> >
> > > 🔁 `docker compose down/up --build` **不会重置数据库密码**，因为数据卷还在。
> > > 🧹 要真正重置，必须 **删除 MySQL 的数据卷** 或 **手动修改密码**。
> >
> > 需要我帮你检查你的 `docker-compose.yml` 配置吗？可以贴出来看看。



开放nginx80端口，让公网能访问上服务器

**给云服务器添加<u>入方向</u>规则**

![image-20251104151936176](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251104151936176.png)

#### 为什么配置了 Docker Registry Mirrors 仍然走官方源？

![image-20251111203943727](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251111203943727.png)





# 其他



#### 抓取线程

```
sudo netstat -tulnp | grep ':8082'
```

- `-t`: TCP连接
- `-u`: UDP连接
- `-l`: 仅显示监听端口
- `-n`: 以数字形式显示地址和端口
- `-p`: 显示进程ID和进程名称（需要sudo权限）

> ##### windows系统
>
> ```
> netstat -ano | findstr ":8082"
> ```
>
> 



#### 杀死进程

```
kill -9 [PID]
```



> ##### windows
>
> ```
> taskkill /PID [PID] /F
> ```
>
> 







#### 更换微服务jar包



```
mv 原文件 新文件名
# 上传压缩包
./service.sh restart
```



#### 查看实时最新日志

```
tail -f console.log
```

查看新追加进入的行







#### ==服务器日志查看==

> 启动jar包并且不开后台运行就能 看到后端控制台的运行日志
>
> 
>
> 





`service.sh`脚本

```
#!/bin/bash

#设置jar文件名
APP_NAME=clinic-boot.jar

#使用说明，用来提示输入参数
usage() {
echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
exit 1
}

#检查程序是否在运行
is_exist(){
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
#如果不存在返回1，存在返回0
if [ -z "${pid}" ]; then
return 1
else
return 0
fi
}

#启动方法
start(){
is_exist
if [ $? -eq "0" ]; then
echo "${APP_NAME} is already running. pid=${pid} ."
else
nohup java -Xms512m -Xmx512m -jar -Dspring.profiles.active=dev  $APP_NAME > console.log &
echo "Service is up!!"
tail -50f console.log
fi
}

#停止方法
stop(){
is_exist
if [ $? -eq "0" ]; then
kill -9 $pid
else
echo "${APP_NAME} is not running"
fi
}

#输出运行状态
status(){
is_exist
if [ $? -eq "0" ]; then
echo "${APP_NAME} is running. Pid is ${pid}"
else
echo "${APP_NAME} is NOT running."
fi
}

#重启
restart(){
stop
start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"start")
start
;;
"stop")
stop
;;
"status")
status
;;
"restart")
restart
;;
*)
usage
;;
esac
```

> ```
> [root@iZwz9bcfcqrtcsom557w5aZ clinic]# ./service.sh
> Usage: sh 执行脚本.sh [start|stop|restart|status]
> ```
>
> ```
> -Xms512m -Xmx512m
> ```
>
> 启动参数
>
> 





使用指南

```
./service.sh start          # 启动
./service.sh stop           # 停止
./service.sh restart        # 重启
./service.sh status         # 查看状态
```



































































































































> # 生產環境部署檢查清單
>
> ## 部署前準備
>
> ### 1. 環境變量配置
>
> - [ ] 創建 `.env.production` 文件（參考 `.env.production.example`）
> - [ ] 配置 `VITE_API_BASE_URL`：
>   - 如果 API 與前端同域：設置為空字符串 `VITE_API_BASE_URL=`
>   - 如果 API 在不同域名：設置完整地址 `VITE_API_BASE_URL=https://api.yourdomain.com`
>
> ### 2. 構建配置檢查
>
> - [ ] 檢查 `vite.config.js` 中的 `base` 配置
>   - 如果部署在根路徑：`base: '/'`
>   - 如果部署在子路徑：`base: '/子路徑/'`
> - [ ] 檢查構建輸出目錄：默認為 `dist`
>
> ### 3. 代碼檢查
>
> - [ ] 確認所有 API 請求使用統一的配置工具（`@/utils/apiConfig`）
> - [ ] 確認沒有硬編碼的 localhost 或絕對 URL（除了外部資源）
> - [ ] 確認路由配置使用 `createWebHistory(import.meta.env.BASE_URL)`
>
> ## 構建步驟
>
> ### 1. 安裝依賴
>
> ```bash
> npm install
> ```
>
> ### 2. 構建生產版本
>
> ```bash
> npm run build
> ```
>
> ### 3. 檢查構建結果
>
> - [ ] 確認 `dist` 目錄已生成
> - [ ] 檢查 `dist/index.html` 中的資源路徑是否正確
> - [ ] 檢查 `dist/assets` 目錄中的靜態資源
>
> ## 服務器配置
>
> ### 1. Nginx 配置（推薦）
>
> #### 前端靜態文件服務
>
> ```nginx
> server {
>     listen 80;
>     server_name yourdomain.com;
>     
>     # 前端靜態文件目錄
>     root /path/to/vue-blog/dist;
>     index index.html;
>     
>     # SPA 路由支持 - 關鍵配置
>     location / {
>         try_files $uri $uri/ /index.html;
>     }
>     
>     # API 代理（如果 API 與前端同域）
>     location /user/ {
>         proxy_pass http://localhost:9090;
>         proxy_set_header Host $host;
>         proxy_set_header X-Real-IP $remote_addr;
>         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
>         proxy_set_header X-Forwarded-Proto $scheme;
>     }
>     
>     location /api/ {
>         proxy_pass http://localhost:9090;
>         proxy_set_header Host $host;
>         proxy_set_header X-Real-IP $remote_addr;
>         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
>         proxy_set_header X-Forwarded-Proto $scheme;
>     }
>     
>     location /common/ {
>         proxy_pass http://localhost:9090;
>         proxy_set_header Host $host;
>         proxy_set_header X-Real-IP $remote_addr;
>         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
>         proxy_set_header X-Forwarded-Proto $scheme;
>     }
>     
>     # 靜態資源緩存
>     location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
>         expires 1y;
>         add_header Cache-Control "public, immutable";
>     }
> }
> ```
>
> ### 2. 其他服務器配置
>
> #### Apache 配置
>
> ```apache
> <VirtualHost *:80>
>     ServerName yourdomain.com
>     DocumentRoot /path/to/vue-blog/dist
>     
>     <Directory /path/to/vue-blog/dist>
>         Options -Indexes +FollowSymLinks
>         AllowOverride All
>         Require all granted
>         
>         # SPA 路由支持
>         RewriteEngine On
>         RewriteBase /
>         RewriteRule ^index\.html$ - [L]
>         RewriteCond %{REQUEST_FILENAME} !-f
>         RewriteCond %{REQUEST_FILENAME} !-d
>         RewriteRule . /index.html [L]
>     </Directory>
>     
>     # API 代理
>     ProxyPass /user/ http://localhost:9090/user/
>     ProxyPassReverse /user/ http://localhost:9090/user/
>     
>     ProxyPass /api/ http://localhost:9090/api/
>     ProxyPassReverse /api/ http://localhost:9090/api/
> </VirtualHost>
> ```
>
> ## 部署後測試
>
> ### 1. 基本功能測試
>
> - [ ] 訪問首頁：`https://yourdomain.com/`
> - [ ] 訪問文章列表：`https://yourdomain.com/articles`
> - [ ] 訪問文章詳情（使用 ID）：`https://yourdomain.com/article/1`
> - [ ] 訪問文章詳情（使用 slug）：`https://yourdomain.com/article/deploy-personal-website-guide`
> - [ ] 測試路由跳轉是否正常
> - [ ] 測試瀏覽器刷新是否正常
>
> ### 2. API 請求測試
>
> - [ ] 打開瀏覽器開發者工具（F12）
> - [ ] 檢查網絡請求是否成功
> - [ ] 確認 API 請求的 URL 是否正確
> - [ ] 檢查是否有 CORS 錯誤
> - [ ] 檢查是否有 404 錯誤
>
> ### 3. 錯誤處理測試
>
> - [ ] 測試訪問不存在的文章：應該顯示 404 頁面
> - [ ] 測試網絡錯誤：應該顯示適當的錯誤提示
> - [ ] 測試 API 服務器不可用：應該顯示連接錯誤
>
> ### 4. 性能測試
>
> - [ ] 檢查頁面加載速度
> - [ ] 檢查靜態資源是否正確緩存
> - [ ] 檢查資源大小是否合理
>
> ## 常見問題排查
>
> ### 問題 1：直接訪問路由返回 404
>
> **原因**：服務器沒有正確配置 SPA 路由支持
>
> **解決方案**：
> 1. 檢查 Nginx 配置中是否有 `try_files $uri $uri/ /index.html;`
> 2. 重啟 Nginx：`sudo nginx -s reload`
>
> ### 問題 2：API 請求失敗
>
> **原因**：API 基礎 URL 配置錯誤或 CORS 問題
>
> **解決方案**：
> 1. 檢查 `.env.production` 文件中的 `VITE_API_BASE_URL` 配置
> 2. 檢查後端服務器的 CORS 配置
> 3. 檢查 Nginx 代理配置是否正確
>
> ### 問題 3：靜態資源加載失敗
>
> **原因**：資源路徑配置錯誤
>
> **解決方案**：
> 1. 檢查 `vite.config.js` 中的 `base` 配置
> 2. 檢查構建後的 `dist/index.html` 中的資源路徑
> 3. 確認服務器配置正確處理靜態資源請求
>
> ## 部署後維護
>
> ### 1. 更新部署
>
> 1. 修改代碼後，重新構建：`npm run build`
> 2. 將新的 `dist` 目錄內容上傳到服務器
> 3. 重啟服務器或重新加載配置
>
> ### 2. 監控和日誌
>
> - [ ] 設置錯誤監控（如 Sentry）
> - [ ] 設置性能監控
> - [ ] 定期檢查服務器日誌
> - [ ] 定期檢查 API 響應時間
>
> ### 3. 備份
>
> - [ ] 定期備份構建產物
> - [ ] 定期備份環境變量配置
> - [ ] 定期備份服務器配置
>
> ## 總結
>
> 完成以上檢查清單後，您的應用應該能夠在生產環境中正常運行。如果遇到問題，請參考 `SLUG_ROUTING_TROUBLESHOOTING.md` 進行故障排除。
