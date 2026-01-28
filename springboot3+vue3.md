**大量思考需求的程序实现，实在想不到就参考别人的设计**

**拆解需求的能力**

**博客网站：增删改查发布的文章；设计登录注册、评论、浏览记录**

**当你设计达到一定量的功能的时候，你会发现公司的需求也不过就是之前需求的变种**



（横向拓展技术栈，深入了解各种技术实现原理，并且选择自己感兴趣的领域）



javase - 数据库MySQL-javaweb（网络编程，多线程编程；Tomcat ; Servlet;Linux）

> (如果时间充裕，可以手动书写工具或框架)，比如Tomcat

SSM框架——spring + spring MVC + Mybatis + Mybatis-plus

学习真实场景前后端开发模式和流程，学习真实企业中团队协作流程及技术——前端框架、springboot + SVN/git + Jenkins

写贴合实际的项目

分布式 微服务开发——springcloud

![image-20250326103859094](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326103859094.png)



















目标：

<u>**掌握基于Springboot实现基础SSM框架整合**</u>

<u>**掌握第三方技术与Springboot整合思想**</u>







> spring-03-核心概念，解耦
>
> 、
>
> 

















# 1.Spring

### 一. 初识

* 为什么学——必备；简化开发；

* **学什么**

  * 框架整合
    * mybatis
    * mybatis-plus
    * struts

  
  
* **怎么学**

  * 学习整体设计思想
  * 学习基础操作，思考操作与思想间的联系
  * 熟练应用操作，体会思想



* Spring家族

  * web开发、微服务、云服务开发

  * Springboot家族技术 

    ![image-20250326105126435](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326105126435.png)

  * ![image-20250326105310379](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326105310379.png)
    从中选择需要的技术使用









### 二.SpringFramework

* 思想演化

  * ![image-20250326105510386](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326105510386.png)

    

* Spring是用来管对象的技术

  ![image-20250326110523877](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326110523877.png)

  AOP：面向切面编程（一种编程思想 ，是一种设计概念）能在不惊动原始程序的基础上增强功能
  Aspects：AOP思想的实现

  Core Container：核心容器（装对象）


  Data Access：数据访问（与数据层相关的技术）
  Data Integration：数据继承


  web：web开发


  Test：Spring在<u>单元测试</u>和集成设计方面也提供了帮助



1. 核心容器

* IOC(控制反转)的概念

  * ![image-20250326114230766](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326114230766.png)

  * 
    ![image-20250326114348327](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326114348327.png)

    交给IOC容器创建管理对象

    绑定Service和Dao的依赖关系，
    绑关系的过程叫做依赖注入

  * ![image-20250326120107988](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326120107988.png)
    ![image-20250326120355302](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326120355302.png)









* IOC入门案例思路分析

​		![image-20250326120653095](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250326120653095.png)

1. 引入spring相关依赖（pom.xml）
   ![image-20250328105642830](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328105642830.png)
2. 创建applicationContext.xml文件，配置spring坐标，标注bean（id表示对象名字，class表示对象类型）
   ![image-20250328105655623](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328105655623.png)
3. ![image-20250328105723299](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328105723299.png)
4. zaimain函数中实现一个能获取IOC容器的对象，获取容器（使用.getBean()获取）
   ![image-20250328105421149](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328105421149.png)

​	![	](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328105737447.png)







* DI入门案例

  1. ![image-20250328110641473](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328110641473.png)

  2. ![image-20250328110719525](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328110719525.png)
     set方法由容器调用

  3. ![image-20250328110742430](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328110742430.png)

     ```
     name = "bookDao"和 ref = "bookDao"    中这两个Dao不一样
     ```

     ![image-20250328110937039](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328110937039.png)





* ### bean配置

  1. 基本配置

     ![image-20250328111059791](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328111059791.png)

     

     
     

  2. 别名配置

     ![image-20250328111416271](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328111416271.png)

     > 还是建议使用id，

  3. spring默认给我们创建的bean是一个单例（因为不是单例的话，会出现用一次造一个的情况，耗费空间）
     创建非单例——使用配置

     ![image-20250328111711762](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328111711762.png)

  4. ![image-20250328111940433](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328111940433.png)





> ### domain，entity，pojo统称为实体类，只不过叫法不一样





* ### bean的实例化



 1. ![image-20250328112739695](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328112739695.png)

    

    
    

 2. ![image-20250328113205844](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328113205844.png)

    配置工厂还不够，还需要标注工厂里面造对象的方法
    ![image-20250328113345611](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328113345611.png)

    

    3. 先把实例工厂对象找出来， 

       ![image-20250328113817909](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328113817909.png)

       

       

       

* bean生命周期

  1. 概念
     ![image-20250328115010858](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250328115010858.png)

  2. 怎么控制  

     正常程序执行完毕会直接退出虚拟机，只会执行init()不会执行destroy();

     所以需要：
     注册关闭钩子【容器启动以后，在启动虚拟机之前先关闭容器】

     

     































> ### spring报错信息从末尾开始看













2. 整合



 





3. AOP





4. 事务

   



### 三.SpringMVC







### 四.SpringBoot



### 4.Springcloud







# 2.Springboot







































