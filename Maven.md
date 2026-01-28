### 介绍

![image-20250722095349991](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722095349991.png)

![image-20250722095455420](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722095455420.png)

![image-20250722100130897](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722100130897.png)





![image-20250722100303585](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722100303585.png)

> 解决同一个项目因为项目结构不同，而不能在不同工具中直接运行的弊端

![image-20250722100354868](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722100354868.png)







![image-20250722100936139](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722100936139.png)

maven市场占有份额最高，其次gradle

![image-20250722101955638](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722101955638.png)

![image-20250722102130328](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722102130328.png)

![image-20250722102412083](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722102412083.png)

> 描述项目信息
> 1.组织名
> 2.模块名
> 3.项目版本
>
> 
>
> 1.编译使用的jdk
> 2.运行使用的jdk
> 3.字符集编码
>
> 



![image-20250722102651818](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722102651818.png)

配置依赖

从本地仓库、中央仓库获取jar包（先查本地，后查中央仓库）

> 私服（远程仓库）（一般给公司内部成员用）
>
> 如果有私服(先查本地，后查私服，最后查中央仓库并下载到私服)
> 

![image-20250722102825232](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722102825232.png)



### 安装

![image-20250722102841481](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250722102841481.png)

> bin目录存放可执行文件
> conf存放配置文件
> lib存放依赖的jar包



### maven坐标

![image-20250723085237168](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723085237168.png)

> 不带SNAPSHOT也是release版本
> 版本号：规范，但是不是规定



### 导入Maven项目

![image-20250723085357323](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723085357323.png)





![image-20250723085550341](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723085550341.png)



### 依赖管理

![image-20250723090004242](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723090004242.png)

> 依赖传递
>
> a依赖了b、b依赖了c；添加依赖a，bc也会传递进来
> 

 ![image-20250723090957724](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723090957724.png)

* **引入依赖之后记得刷新**



![image-20250723091203699](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723091203699.png)



### **生命周期**

![image-20250723091322558](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723091322558.png)

![image-20250723091528916](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723091528916.png)

![image-20250723091624268](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723091624268.png)

![image-20250723091833988](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723091833988.png)


package：打包成jar包在target目录下

> jar包是一个可执行文件，你可以通过传递jar包，来让别人直接运行你的程序，而不是还要跑源代码



### 单元测试

![image-20250723092704229](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723092704229.png)

![image-20250723092802607](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723092802607.png)

![image-20250723092828262](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250723092828262.png)

* 单元测试主要测试代码逻辑是否正常运行
* 集成测试总体和代码都关注
* 系统测试关注功能是否能正常运行

![image-20250728124955254](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728124955254.png)

![image-20250728125132755](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728125132755.png)



![image-20250728125508028](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728125508028.png)

![image-20250728125426354](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728125426354.png)

![image-20250728130029790](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728130029790.png)

> 规定必须要遵守
>

#### 断言和常见注释

![image-20250728130557496](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728130557496.png)

![image-20250728130845434](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250728130845434.png)

> 后面的提示信息(msg)可以不传递，也可以传递
>
> 

```java


@Test
public void testGenderWithAssert(){
	UserService userService = new UserService();
    String gender = userService.getGender('1000000200010011011');
    Assertions.assertEquals("男",gender,"性别获取逻辑有问题");
}


//出现异常时的断言
@Test
public void testGenderWithAssert2(){
	UserService userService = new UserService();
    Assertions.assertThrows(IllegalArgumentException.class.() -> {
		userService.getGender(null);
    });
}





```

写好测试之后，只要运行完看绿色还是红色、就能判断测试是否通过



#### 常见注解

![image-20250804112204958](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804112204958.png)

before主要做资源准备工作

after做资源释放、环境清理的工作

![image-20250804112610887](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804112610887.png)



* 参数化测试
* ![image-20250804112932969](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804112932969.png)



* 名字注解

* ![image-20250804113116079](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804113116079.png)

  ![image-20250804113133622](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804113133622.png)



### 单元测试-企业开发规范

![image-20250804113618114](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250804113618114.png)



 ![image-20250807083120667](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807083120667.png)

* 不同公司对核心代码和非核心代码覆盖率有不同的规定



* 可以直接使用ai快捷键“生成单元测试”



### maven依赖范围 

![image-20250807084019304](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807084019304.png)

> 绝大部分都是compile默认



### 常见问题

![image-20250807084558150](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807084558150.png) 

![image-20250807084716369](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807084716369.png)

* 或者直接双击提前封装好的del.bat文件执行删除操作

![image-20250807084829870](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250807084829870.png)





```bash
D:\code\jdk\jdk17\bin\java.exe -Dmaven.multiModuleProjectDirectory=D:\ProjectOfBZH...
```

**这里能看到maven是==在用什么版本的jdk编译==，能看看是否符合自己预期**



# Maven高级



## 分模块设计

![image-20250917123934063](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917123934063.png)







![image-20250917124210781](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917124210781.png)

* 拆分为子模块，工具类和通用组件也单独取到一个子模块中

* 分模块设计之后，如果某一个功能模块需要用到另一个模块，引入对应模块的依赖即可
* 分模块开发，便于项目的管理和维护、提高了模块的复用性

 ![image-20250917124318698](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917124318698.png)

> 具体使用哪种策略，按照项目组的设计规范





![image-20250917125011598](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917125011598.png)

> 如果要使用pojo或utils里面的类，就引入依赖



**<u>pojo模块</u>**

![image-20250917130958400](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917130958400.png)

>   创建一个同名的包存放代码 

* 项目中使用的同一种依赖保持版本一致 
* 其他中模块引入pojo依赖：

![image-20250917131256279](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917131256279.png)



**utils包**



一样的操作，同名的包存放代码，缺什么依赖引什么依赖

![image-20250917133421945](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917133421945.png)



## 继承与聚合





#### 继承

针对所有这些模块的共同依赖，

> 如果不使用继承，这几十个模块的同一个依赖如果要改版本号就要一个一个的修改，非常繁琐

* 继承关系

  ![image-20250917174814391](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917174814391.png)

  >
  > 在pom文件中用<parent> </parent>描述父工程

  * 创建父工程
    （删除src文件夹[不需要]）

    * 指定打包方式

      ```
      <packaging>pom</packaging>
      ```

      ![image-20250917175318004](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917175318004.png)

    * 继承springboot父工程的坐标

      ![image-20250917175508400](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917175508400.png)

      * 配置其他模块共有的依赖  于 父工程中
        

  * 让子工程继承父工程
    <parent>标签包裹父工程的坐标 

    ![image-20250917175746522](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917175746522.png)

    并指定父工程pom文件的相对位置（如果不指定，默认从本地仓库和远程仓库中来寻找父工程）

    <relativePath></relativePath>

    ![image-20250917180616396](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917180616396.png)

    > 设置了继承关系之后，groupId也会从父工程继承下来，所以子工程的groupId可以省略；
    >
    > ![image-20250917181918473](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917181918473.png)

    > ![image-20250917182003653](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917182003653.png)
    >
    > 两种子工程和父工程的结构









* 版本锁定（继承之后）

  ![image-20250917194352910](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917194352910.png)

  统一管理所有依赖的版本，子工程之后需要用到其中的依赖时仍然需要引入依赖、但是不用标注版本号(父工程统一管理)

    

  ```java
  <dependencyManagement>
      <dependencies>
      	<!--JWT令牌-->
      	<dependency>
      		<groupId>io.jsonwebtoken</groupId>
      		<artifactId>jjwt</artifactId>
      		<version>0.9.1</version>
      	</dependency>
      </dependencies>
  </dependencyManagement> 
  
  
  ```

  * 借助maven中的自定义属性

    > 如果项目依赖非常多，使用上面的方法找依赖可能比较麻烦，此时可以借助自定义属性

    ![image-20250917195306578](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250917195306578.png)

    ```
    <properties>
    	<lombok.version>1.18.30</lombok.version>
    	<jjwt.version>0.9.1</jjwt.version>
    </properties>
    
    ```

    

  



#### 聚合

> 将拆分出来的模块组织成一个整体，同时进行项目的构建

![image-20250918080626663](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918080626663.png)



此处父工程既是父工程、也是聚合工程

```java
<modules>
	<module></module>
</modules>
```

在聚合工程中执行clean/package等都有在所有聚合的工程中执行

![image-20250918081152019](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918081152019.png)

















## 私服





![image-20250918081917730](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918081917730.png)



![image-20250918082246410](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918082246410.png)

> 生命周期中的deploy阶段会将模块传到私服当中
> 本地要配置私服的url地址、私服的用户名/密码

![image-20250918082315525](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918082315525.png)

![image-20250918082437434](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918082437434.png)



![image-20250918082709367](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918082709367.png)

> 下载资源时所需要的配置 ↑
>
> ```
> <mirror>
> 
> </mirror>
> ```
>
> ```
> <profile>
> 
> </profile>
> ```
>
> 

![image-20250918083439505](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918083439505.png)

![image-20250918083703343](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250918083703343.png)























