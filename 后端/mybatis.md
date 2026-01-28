## java程序操作数据库

![image-20250817090538280](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817090538280.png)

## JDBC



### 是什么

> java  database connectivity

![image-20250817090726148](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817090726148.png)

![image-20250817090859629](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817090859629.png)



# mybatis

![image-20250817154000024](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817154000024.png)

![image-20250817153908261](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817153908261.png)

### 准备工作

![image-20250817154340995](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817154340995.png)

![image-20250817154401645](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817154401645.png)

> 用mapper注解标明当前是mybatis的一个持久层接口



![image-20250817195608922](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250817195608922.png)

> 如图：应用程序在运行时，会自动为该接口创建一个实现类对象(代理对象)，并且会自动将该实现类对象存入IOC容器 - bean

![image-20250818092911913](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250818092911913.png)



#### 数据库连接池



![image-20250818093837938](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250818093837938.png)

> 连接复用
>
> 减少资源浪费

![image-20250818094107199](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250818094107199.png)



如果要从默认的hikari改成druid

![image-20250818094147521](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250818094147521.png)









![image-20250820074701971](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250820074701971.png)

![image-20250820074753305](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250820074753305.png)









![image-20250820075030785](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250820075030785.png)



```java
/**
 *测试新增
 */
@Test
public void testInsert(){
    userMapper.insert(new User(null,"gaoyuan","1234","高原",18));
}


```

d









![image-20250820080054467](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250820080054467.png)



![image-20250820080906098](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250820080906098.png)







![image-20250821104848157](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821104848157.png)





### 基于xml映射配置文件定义sql语句



![image-20250821105249469](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821105249469.png)

![image-20250821110529460](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821110529460.png)

![image-20250821110828034](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821110828034.png)

















![image-20250821110300867](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250821110300867.png)

> 如果UserMapper和UserMapper.xml不同路径，就需要添加这项配置
>
> 



### 数据封装



![image-20250824180627265](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250824180627265.png)

* ​	

  > 这里的“封装”指的是 MyBatis 将数据库查询结果自动映射并赋值到实体类对象的属性中。当实体类属性名和数据库字段名一致时，MyBatis 可以自动完成这一过程；若不一致，则需要手动配置映射规则来实现属性赋值。

**<u>解决办法</u>**

1. 手动指定字段和属性
2. 起别名
3. **在配置中开启驼峰命名【推荐】**

![image-20250824181335522](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250824181335522.png)

```
mybatis:
 configuration:
  mapp-underscore-to-camel
```









