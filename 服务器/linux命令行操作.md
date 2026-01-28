### linux简介

![image-20250824085200042](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250824085200042.png)

![image-20250918123048570](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918123048570.png)

![image-20250918123138483](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918123138483.png)

![image-20250918123150872](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918123150872.png)











> ## linux文件目录管理
>
> ![image-20250824085237528](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250824085237528.png)
>











![image-20250824092504599](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250824092504599.png)



### 虚拟机运行linux

* 下载虚拟机
* 

> ![image-20250918123707386](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918123707386.png)
>
> ![image-20250918123906746](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918123906746.png)
>
> 
> 

* 虚拟机网络配置
  ![image-20250918124335036](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918124335036.png)

* 将准备好的CentOS7系统挂载到VMware当中

  ![image-20250918125639132](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918125639132.png)

  > 双击运行这个文件后，软件中“我的计算机”下面就会多一个虚拟机

  ![image-20250918125957523](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918125957523.png)

* ip a 或 ip addr
  查看ip地址

  ![image-20250918130603655](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918130603655.png)

  > 如果有网卡，但是没有ip地址；可能是因为启动过程中这块网卡没有正常激活到
  > 可以手动激活下
  >
  > ![image-20250918130645450](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918130645450.png)
  >
  > 运行这个脚本来手动激活

* 选择挂起虚拟机；下次就可以快速启动，并且不会丢失配置

  ![image-20250918130839568](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918130839568.png)

> ![image-20250918131118404](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918131118404.png)



> ####  真实企业开发使用真实的服务器安装linux系统，不是用虚拟机







### 使用远程连接工具操作服务器

见 运维 文档

























# linux

## 目录结构

![image-20250828171016627](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828171016627.png)

![image-20250828171041869](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828171041869.png)

![image-20250918134655010](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918134655010.png)

![image-20250828171404731](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828171404731.png)







## 常用命令

```java
cd /  		回到根目录
ll    		展示当前文件夹下的目录结构
ll -a 
pwd   		当前路径

cat 文件路径  查看文件



mkdir newDir 新建文件夹


//搜索某一关键词
/关键词 + 回车

```





![image-20250918135154845](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918135154845.png)











### 快捷键

ctrl+l  ——  清屏

Tab键自动补全

联系两次Tab键，给出操作提示

使用上下箭头快速调出使用过的命令

使用“history”可以查看使用过的指令

## 目录相关命令

![image-20250828171529505](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828171529505.png)







#### 定位与查看目录

![image-20250828171707899](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828171707899.png)

* 以列表形式展示根目录：ls -l，即 ll

* 需要查看文件大小时：ls -h

* **总结**

  * ll

  * ll -h

  * ll - a

  * ll /bin

    > ll +路径  ——  展示指定文件夹下的目录

![image-20250918140022086](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918140022086.png)



> 用 . 开头的一般是隐藏文件

#### 切换目录与展示目录结构

![image-20250828172439963](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828172439963.png)

* 命令“cd”会直接回到用户的家目录(~，即/root或/home)

* "cd ../"——表示回到上一级

* “cd -”   ——切换到上一次所在目录

* 进入usr目录

  * cd /usr/local
    或 cd /usr/local/（末尾是否有"/"不影响进入目标目录）

  * cd usr/local （当处于/目录时可用）
    或 usr/local/

    > **<u>推荐末尾带个“/”</u>**

> 开头带/的都是绝对路径
>
> 开头没带/的是相对路径
>
> ![image-20250918140346000](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918140346000.png)







* 利用tree查看某个目录的内部结构
  * tree /usr/local/

#### 创建和删除目录

![image-20250828174140618](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828174140618.png)

> 一次创建多级目录需要加入 -p



* 总
  * mkdir  aaa
  * mkdir -p aaa/bbb
  * rmdir bbb（当前目录包含bbb目录时）(只能用于删除空目录)



## 文件相关命令

### 创建、复制、移动、重命名、删除文件

![image-20250828191634219](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828191634219.png)



* 创建文件

  *  <u>touch a.txt</u>
  *  touch -t 202508281921 b.txt

* 复制文件或文件夹

  * cp a.txt b.txt new/
    ![image-20250828192923442](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828192923442.png)

    

    可以复制的同时进行改名操作
    cp a.txt ~/aaa.txt

    > cp: copy

* 删除文件

  * rm a.txt（当前目录下有a.txt这个文件时）
  * rm -f a.txt （-f，无需确认、直接删除）
  * rm -r a.txt b.txt aaa （-r，将目录及  其中文件或目录  逐一删除  (递归删除)）
  * rm -rf a.txt b.txt aaa （添加-rf，能够同时删除文件和文件夹）【**这是一个危险的命令，误操作的后果严重**】

> y表示yes；n表示no

> ### 多使用tab键提示，这样既节省时间，又能尽量减少语法错误。

* 移动文件和文件夹

  ```
  mv source dest
  ```

  ![image-20250918153253210](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918153253210.png)

  * mv a.txt b.txt ../saves/
    ![image-20250828194027837](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828194027837.png)
    ![image-20250828194049018](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828194049018.png)

    > 如果是已存在的目录，就执行移动，否则就是从命名

  * 重命名

    * ·mv a.txt aaa.txt（当前文件夹里面有a.txt时）

      > 改名为aaa.txt

      可以移动的同时进行改名操作
      mv a.txt ~/aaa.txt

### 查找文件

> 使用通配符*可模糊查找

![image-20250828200411853](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828200411853.png)



* 根据名字搜索
  * 指定路径范围，使用-name配置文件名称
    ![image-20250828200834880](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828200834880.png)
  * (可使用-type配置 f或d—指定只查找文件夹或文件)





* 查找大于100M的文件

  * ![image-20250828201557453](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828201557453.png)

    ![image-20250828201535583](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828201535583.png)

* 将/usr/中大于100M的文件拷贝到一个目录下
  ![image-20250828202527713](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828202527713.png)

  > 使用 {} 表示查询到的内容

  此时这个命令要求以 /; 结尾

* 根目录中搜索三天之前的文件
  find / -mtime +3

* 搜索三天内的文件
  find / -mtime -3

* 搜索**正好**三天之前的文件
  find / -mtime 3 





![image-20250828202800155](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250828202800155.png)

![image-20250918155506301](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918155506301.png)

```
grep -i exception tlias.log
```





### 编辑文件

![image-20250829105234994](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829105234994.png)

> 或者用“yum install vim”安装

![image-20250829105559345](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829105559345.png)

![image-20250829105651326](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829105651326.png)

* 删除(其实是剪切)
  * dd——删除光标所在行
  * ndd
* 黏贴（从光标的下一行开始黏贴）
  * p
* 到文件末尾
  * G
* 撤销与恢复
  * **u与ctrl+R** 





![image-20250829105718195](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829105718195.png)

* **<u>主要使用i进入编辑模式（insert）</u>**

![image-20250829105906677](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829105906677.png)

> 小写的x

> set nu--set number
> w--  write
> wq--write and quit 

* 保存
  * :w
* 保存并退出
  * :x
* 给文件设置密码
  * :X
* 搜索某一关键词
  * /关键词
* 不保存直接退出
  * :q!

* 回车
  * 执行命令
  * 退出底行模式
* ![image-20250829133735673](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829133735673.png)





* 追加内容
  * echo:追加的内容  >> file                    将内容追加到文件尾部





### 查看文件

![](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829134408244.png) 

![image-20250829134654161](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829134654161.png)

* 小文件
  * cat 文件路径
  * cat -n 文件路径（-n，显示行号）
* 查看某个文件的最新(后)50行
  * tail -50 a.txt
* 实时监听文件末尾内容
  * tail - f a.txt
    ![image-20250918142247178](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918142247178.png)
* less查看文件
  * 方向键上下滚  动
  * “/关键词”进行搜索
  * q退出查看
* 抓出文件中包含“ssh”的行
  * grep ssh /var/log/messages
* 

### 文件解压缩





 ![image-20250829142541980](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829142541980.png)

> tar：对文件进行打包、解包、压缩、解压
>
> ![image-20250918150113854](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918150113854.png)
>
> > c：打包
> > x：解包
> >
> > c和x二选一、不同时使用
> >
> > ![image-20250918150645639](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918150645639.png)

* **<u>打包操作</u>**

  * ```
    -cvf
    ```

* **<u>解包操作</u>**

  * ```
    -xvf
    ```

* **<u>打包并压缩</u>**

  * ```
    -zcvf
    ```

* **<u>解压缩</u>**

  * ```
    -zxvf
    ```

    










* 解压操作用x d

![image-20250829142836622](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829142836622.png)



* 打包：将aaa目录、a.txt、b.txt打包成root.tar.gz

  * tar -czvf root.tar.gz aaa/ a.txt b.txt

* 解压到当前目录

  * tar -xzvf root.tar.gz

* 解压到指定位置

  * tar -xzvf root.tar.gz -C bbb/

  * > 大写C

![image-20250829143854881](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829143854881.png)

* 打包：将aaa目录、a.txt、b.txt打包成root.zip
  * zip -r root.zip aaa/ a.txt b.txt
* 解压到当前目录
  * unzip root.zip
* 解压到指定位置
  * unzip root.zip -d bbb/





> ![image-20250829145803277](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250829145803277.png)
>
> 
>
> 以



## linux软件安装







![image-20250918160113696](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918160113696.png)

> rpm:redhat package manager 
> 第二种：按照rpm的规范打包，然后用如平面命令安装





#### 安装JDK

![image-20250918160508067](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918160508067.png)

1. 获取安装包

2. 解压安装包到/usr/local/

   ```
   tar -zxvf jdk-17.0.10_linux-x64_bin.tar.gz -C /usr/local/
   ```

3. 配置环境变量（仔细）

   ```
   vim /etc/profile
   ```

   profile后面没有"/"

   > ![image-20250918162404853](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918162404853.png)
   >
   > * 把当前的环境变量改成原来的环境变量加上java的环境变量

4. 重新加载profile文件

5. 检查安装是否成功

   ![image-20250918162606345](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918162606345.png)
   安装成功




























#### 安装MySQL





![image-20250918164747922](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918164747922.png)

1. 卸载就版本

   

   查找当前系统所有安装包并找出包含mariadb的文本

   ```
   rpm -qa | grep mariadb
   ```

   > qa：quiry all
   >
   > “|”管道符：在前一个指令执行完的结果的基础上执行操作并打印结果

   ![image-20250918165641106](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918165641106.png)

2. 上传安装包

3. 解压安装包并移动到到/usr/local目录，命名为mysql

4. 配置环境变量

5. 

   ```
   cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql
   chkconfig --add mysql
   ```

   





> linux系统自带的低版本的安装包删掉

![image-20250918165211142](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918165211142.png)





2. 

   创建mysql用户组，然后创建一个用户mysql

   ```
   useradd -r -g mysql -s /bin/false mysql
   ```

   指定当前mysql的安装目录，以及数据具体往哪存放

   ```
   mysqld --initialize --user=mysql --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data
   ```

   



* 查看mysql状态

  ```
  systemctl status mysql
  ```

* 连接mysql

  ```
  mysql -uroot -pxxxxx
  ```

* 1. 创建一个远程访问用户，用户名root，可以在任意主机上访问mysql
  2. 权限：可以访问所有数据库，所有表
  3. 刷新权限

  ```
  CREATE USER 'root'@'%' IDENTIFIED BY '1234';
  
  GRANT ALL PEIVILEGES ON *.* TO 'root'@'%';
  
  FLUSH PRIVILEGES;
  ```

  

##### 如何远程访问这个mysql数据库？

> 如何解决linux防火墙阻止访问的问题

![image-20250918172204242](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918172204242.png)



* 严禁关闭防火墙

* 开放需要用到的指定端口

  ```
  firewall-cmd --zone=public --add-port=3306/tcp --permanent
  ```

  



* 远程访问

  * 命令窗口访问

    ```
    mysql -hip地址 -P3306 -uroot -p1234
    ```

    > mysql -h192.168.100.128 -P3306 -uroot -p1234

  * 图形化界面访问

    ![image-20250918173029993](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918173029993.png)

    ![image-20250918173118347](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918173118347.png)
    







#### 安装Nginx

![image-20250918185723901](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918185723901.png)

* 安装nginx运行时需要的依赖

  ```
  yum install -y pcre pcre-devel zlib zlib-devel openssl openssl-devel
  ```

* 上传并解压源码包到当前目录

  ```
  tar -zxvf nginx-1.20.2.tar.gz
  ```

* 进入到解压目录（nginx-1.20.2）后，执行指令

  ```
  ./configure --prefix=/usr/local/nginx
  ```

* 执行编译nginx的指令：make

* 执行安装nginx的指令：make install

  > 自动安装到之前在preifx里面定义的位置 

* 进入到nginx安装目录（/usr/local/nginx），启动nginx服务：sbin/nginx

* 开放80端口，让浏览器能够访问网页

  ```
  firewall-cmd --zone=public --add-port=80/tcp --permanent
  ```

  















