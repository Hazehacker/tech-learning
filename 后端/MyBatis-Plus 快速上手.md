#article/myblog 

> 年初搓项目的时候用到了 MyBatis-Plus，整理一篇即看即用的快速上手教程
>
> 全文共计xx字，预计阅读时间

# 快速入门

1. **引入 MyBatis-Plus 依赖**

   ```xml
   <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-boot-starter</artifactId>
       <version>3.5.3.1</version>
   </dependency>
   ```

2. 自定义Mapper**继承MybatisPlus提供的BaseMapper类**：

   ```java
   public interface UserMapper extends BaseMapper<User>{
   	
   }
   ```

   > 继承的时候要指定泛型（**泛型为想要操作的实体类**）
   >
   > * mp通过扫描 通过泛型 指定的实体类，并基于反射获取实体类信息作为数据库表信息
   >
   >   * **类名驼峰转下划线作为表名**
   >
   >   * **名为id的字段作为主键**
   >
   >   * **变量名驼峰转下划线作为表的字段名**
   >
   > 

3. **注入xxMapper对象**

   然后直接使用xxMapper对象调用它继承的方法

   ![image-20251229084933286](assets/image-20251229084933286.png)

4. [配置 配置文件](##配置)

   > 点击跳转

5. [调用api完成查询语句](##查询语句编写)
   [调用api完成更新语句](##更新语句编写)
   [调用api完成插入语句](##插入语句编写)
   [调用api完成删除语句](##删除语句编写)

   





## 配置

常见配置

* mybatisplus的配置项继承了MyBatis原生配置，并且有自己特有的配置

  ```yaml
  mybatis-plus:
    #mapper配置文件
    mapper-locations: classpath*:mapper/**/*.xml
    type-aliases-package: top.blog.entity # 别名扫描包(项目中实体类放置的包路径)
    configuration:
      #开启驼峰和下划线的映射
      map-underscore-to-camel-case: true
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
      cache-enabled: false # 是否开启二级缓存
    global-config:
      db-config:
        id-type: assign_id # 设置全局的id生成策略为雪花算法【mybatis plus 会自动主键回填】
        update-strategy: not_null # 设置更新策略：只更新非空字段
      
  ```

  

* 常用配置讲解

  ```yaml
  mybatis-plus:
    # ===== 核心路径配置 =====
    mapper-locations: classpath*:mapper/**/*.xml          # XML 映射文件路径
    type-aliases-package: top.blog.entity                 # 实体类别名扫描包
    type-enums-package: top.blog.enums                    # 枚举类扫描包（启用枚举自动映射）
    type-handlers-package: top.blog.handler               # 自定义类型处理器包
    
    # ===== MyBatis 原生增强配置 =====
    configuration:
      map-underscore-to-camel-case: true                  # 驼峰与下划线自动映射
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # SQL 日志输出（开发环境）
      cache-enabled: false                                # 禁用二级缓存（避免缓存一致性问题）
      call-setters-on-nulls: true                         # 查询字段为 null 时也调用 setter（避免基本类型默认值干扰）
      jdbc-type-for-null: 'null'                          # 插入 null 值时指定 JDBC 类型（解决 Oracle 等数据库问题）
    
    # ===== MyBatis-Plus 全局策略配置 =====
    global-config:
      banner: false                                       # 启动时关闭 MP Logo 打印（生产环境推荐）
      db-config:
        id-type: assign_id                                # 全局主键策略：雪花算法（自动回填）
        table-prefix: t_                                  # 表名前缀（实体类无需加 t_）
        logic-delete-field: deleted                       # 逻辑删除字段名（全局指定，实体类可省略注解）
        logic-delete-value: 1                             # 逻辑已删除值
        logic-not-delete-value: 0                         # 逻辑未删除值
        insert-strategy: not_null                         # 插入策略：仅插入非空字段
        update-strategy: not_null                         # 更新策略：仅更新非空字段
        select-strategy: not_null                         # 查询策略：查询结果非空字段才映射（减少内存）
        column-underline: true                            # 字段是否使用下划线命名（默认 true，与驼峰映射配合）
  ```

  

  | 配置项                  | 作用                   | 使用场景建议                                                 |
  | ----------------------- | ---------------------- | ------------------------------------------------------------ |
  | `type-enums-package`    | 自动注册枚举处理器     | 实体含状态枚举（如 OrderStatus）时必配                       |
  | `call-setters-on-nulls` | null 值触发 setter     | 强烈建议开启：避免 int/boolean 等基本类型被赋默认值（0/false）导致逻辑错误 |
  | `logic-delete-*` 系列   | 全局逻辑删除策略       | 统一管理软删除，实体类可省略 `@TableLogic` 注解              |
  | `table-prefix`          | 表名前缀自动剥离       | 数据库表带 `t_` 前缀时，实体类名无需包含前缀                 |
  | `banner: false`         | 关闭启动 Logo          | 生产环境日志整洁，避免干扰                                   |
  | `select-strategy`       | 查询字段过滤           | 大字段（如 text）表查询时提升性能                            |
  | `jdbc-type-for-null`    | 指定 null 的 JDBC 类型 | Oracle 等严格数据库插入 null 时必备                          |

  > 备注：大部分不用手动配置，使用默认即可，如果有需要更改配置就到 yml 文件中自己修改

  > [官方文档](https://baomidou.com/reference/)中写明了mp支持的全部配置，需要了解的可以跳转



## 条件构造器



* 作用：
  应对真实业务中复杂的条件，使用条件构造器构造 (简单查询用wrapper，复杂查询自己写 sql)

![image-20251025170923571](assets/image-20251229091106572.png)![image-20251025171047452](assets/image-20251229091145794.png)

QueryWrapper拓展了查询相关的功能（允许指定需要查询的字段）

![image-20251229091302941](assets/image-20251229091302941.png)

updataWrapper扩展了更新功能（set部分，）

![image-20251229091316999](assets/image-20251229091316999.png)



**几种条件构造器的用法**

* QueryWrapper和LambdaQueryWrapper通常用来构建select、delete、update的where条件部分
* UpdateWrapper和LambdaUpdateWrapper通常只有在set语句比较特殊才使用
* 尽量使用LambdaQueryWrapper和LambdaUpdateWrapper，避免硬编码







## 查询语句编写

> #### 基于LambdaQueryWrapper的查询



**步骤**：

1. 构建查询条件【lambdaQueryWrapper】
   泛型填写<u>数据表所映射的实体类</u>

2. 调用 select * 方法查询

   ![image-20251231201728759](assets/image-20251231201728759.png)



**条件查询**

```java
//1. 构建查询条件
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
    .select(User::getId,User::getUsername,User::getInfo,User::getBalance)// （如果需要 指定查询出来的字段）
    // 构建查询条件
    .like(User::getUsername,"o")
    .and(wrapper -> wrapper.eq(User::getAge, 18).or().eq(User::getAge, 20))// （多条件）
    .ge(user::getBalance,1000);

//2. 查询
List<User> users = userMapper.selectList(wrapper);
```

**分页查询**

> 核心方法：`IPage<T> selectPage(P page, Wrapper<T> queryWrapper)`
>
> 1. 创建分页对象 `Page`
> 2. 构建查询条件
> 3. 调用`selectPage`方法
> 4. 获取分页结果
>    (返回值包括**当前页数据列表、总记录数、总页数**、当前页、每页大小)

```java
Page<User> page = new Page<>(currentPage, pageSize)// 创建分页对象（传入要查询的页数 和 页的大小）
    
// 构建查询条件(可选，如果需要分页查询+条件查询的话)
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getName, "周杰伦");
wrapper.gt(User::getAge, 18); // 大于18岁

// 调用selectPage方法
IPage<User> result = trackMapper.selectPage(page, wrapper);// 返回值包括当前页数据列表、总记录数、总页数、当前页、每页大小
List<User> records = result.getRecords();        // 当前页的数据列表
long total = result.getTotal();                   // 总记录数
int pages = result.getPages();                    // 总页数
int current = result.getCurrent();                // 当前页
int size = result.getSize();                      // 每页大小
```

> ##### 补充：为什么推荐使用Lambdaxxx构建查询条件
>
> 在 MyBatis-Plus 中，**`LambdaQueryWrapper<T>` 和 `QueryWrapper<T>` 都能构建查询条件**，但 **推荐优先使用 `LambdaQueryWrapper<T>`**(若不涉及复杂sql函数、动态列时)，原因如下：
>
> 区别：字段引用方式不同
>
> | 特性     | `QueryWrapper<User>`                   | `LambdaQueryWrapper<User>`                     |
> | -------- | -------------------------------------- | ---------------------------------------------- |
> | 字段引用 | 字符串硬编码： `.eq("name", "周杰伦")` | Lambda 表达式： `.eq(User::getName, "周杰伦")` |
> | 类型安全 | ❌ 编译期无法检查字段是否存在           | ✅ 编译时报错（若字段不存在或拼错）             |
> | IDE 支持 | ❌ 无自动补全、跳转                     | ✅ 自动补全、Ctrl+点击跳转到 getter 方法        |
> | 重构友好 | ❌ 改实体类字段名后需手动改字符串       | ✅ IDE 自动重命名所有引用                       |
>
> 备注：
>
> - **MyBatis-Plus 官方文档**明确推荐使用 `LambdaQueryWrapper` 提升代码健壮性。
> - **《阿里巴巴 Java 开发手册》** ：避免魔法值（magic string），字段名应通过代码引用。
>
> ------
>
> 
>
> ##### 什么时候用 `QueryWrapper`？
>
> 虽然 `LambdaQueryWrapper` 是首选，但以下场景**必须用 `QueryWrapper`**：
>
> | 场景                           | 示例                                                         |
> | ------------------------------ | ------------------------------------------------------------ |
> | **动态字段名**                 | `String field = isVip ? "vip_level" : "level"; wrapper.eq(field, 5);` |
> | **数据库函数/原生 SQL**        | `wrapper.apply("DATE(create_time) = {0}", "2025-12-31");`    |
> | **复杂子查询或 EXISTS**        | `wrapper.exists("SELECT 1 FROM album WHERE album.id = track.album_id AND status = 1");` |
> | **多表关联字段（非当前实体）** | 查询中涉及 `user.name` 但当前是 `Order` 实体                 |
>
> > 💡 小技巧：可以在 `LambdaQueryWrapper` 中嵌套 `QueryWrapper` 来处理特殊片段：
> >
> > ```java
> > lqw.and(w -> w.apply("MOD(id, 2) = 0"));
> > ```
>
> 





## 更新语句编写



**1.使用`updateById`方法**

```java
User user = new User();
user.setId(1L);
user.setAge(18);
user.setBalance(1000);

// 将 id=1 的用户 balance 改为 1000, 年龄改为18
userMapper.update(user);
```

> 注意：
> updateById 方法不会自动只更新不为 Null 的属性，它会将实体对象中的所有字段值更新到数据库中，包括显式设置为 null 的字段



**2.使用`update(entity, wrapper)`方法**

> 传入的entity用于 指定需要重新set的字段（自动只set非空的字段）
>
> 传入的wrapper指定更新条件——对什么数据进行更新

```java
User user = new User();
user.setAge(18);
user.setBalance(1000);

// 将 id=1 的用户 balance 改为 1000, 年龄改为18
userMapper.update(
    user, // SET 部分
    new LambdaUpdateWrapper<User>().eq(User::getId, 1L)
);
```



**3.使用`update(wrapper)`方法【只用wrapper】**

```java
LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getId, id)
                .set(User::getName, userDTO.getName())
                .set(User::getAge, userDTO.getAge());
        
userMapper.update(updateWrapper);
```

> update user set balance = balance - 200 where id in (1,2,4)

> ge 大于等于（greater and equal）
>
> gt 大于（greater than）



**4.链式更新**

```java
// MyBatis-Plus 3.4+ 支持
userMapper.update()
    .set(User::getBalance, new BigDecimal("5000"))
    .eq(User::getId, 1L)
    .update();
```









## 插入语句编写

> 不需要 wrapper，直接转实体



```java
User user = new User();
user.setUsername("alice");
user.setAge(22);
user.setBalance(new BigDecimal("2000"));

userMapper.insert(user); // 自动填充 id（如果配置了）
```

> ✅ 如果有自动填充字段（如 `createTime`），可在实体类上加 `@TableField(fill = FieldFill.INSERT)`。





## 删除语句编写



**1.构造wrapper**

```java
// 删除 age < 18 的用户
userMapper.delete(
    new LambdaQueryWrapper<User>()
        .lt(User::getAge, 18)
);
```





**2.链式删除**

```java
userMapper.delete()
    .lt(User::getAge, 18)
    .remove();
```



> ##### 封装的其他删除方法
>
> ```java
> userMapper.deleteById(1L);                 // 单个
> userMapper.deleteBatchIds(List.of(1L,2L)); // 批量
> ```
>
> 







# 常见注解

> 通过注解，mp可以知道要访问那张表、表中有哪些信息等.MyBatis-Plus 遵循约定大于配置，符合约定就不用配置；但是如果实体类不符合约定，就需要手动配置（自己定义表名、注解名、字段）

[注解配置 | MyBatis-Plus官方文档](https://baomidou.com/reference/annotation/#tablename)



* **@TableName**—**—用于指定表名**

  ```java
  @TableName("tb_user")
  public class User{
  
  }
  ```

  

* **@TableId——用来指定表中的主键字段信息**

  ```java
  @TableId("id")
  private Long xx;
  ```

  支持设定id的增长策略

  ```java
  @TableId(value="id",type=IdType.AUTO)
  private Long xx;
  ```

  * AUTO：数据库自增长

  * INPUT：通过set方法执行输入

  * ASSIGN_ID：分配ID
    使用IdentifierGenerator接口的nextId方法生成id
    （即默认实现类DefaultIdentifierGenerator的雪花算法 来生成id）

    > 注意：
    >
    > 1. 如果没有指定type=IdType.xx，会默认使用ASSIGN_ID（即雪花算法）
    > 2. **必须有一个主键**，MyBatis-Plus 才能基于主键进行增删改查操作

    

* **@TableField**——**用来指定表中的普通字段信息**

  **场景1：成员变量名和字段名不一致**；
  成员变量名和字段名不一致时，通过`@TableField()`指定

  ```java
  @TableField("username")
  private String name;
  ```

  **场景2：is 开头的布尔字段**
  由于**is开头的mp底层会把is去掉、然后作为变量名**，**所以is开头的成员变量也要手动指定对应的字段**

  ```java
  @TableField("is_married")
  private Boolean isMarried;
  ```

  **场景3：字段名是数据库关键字**

  如果有成员变量名和数据库关键字一致，也要通过`@TableField()`指定

  ```java
  @TableField("`order`") // 用反引号包裹关键字
  private Integer order;
  ```


  **场景4：非数据库字段**

  如果有一个字段不是数据库字段，使用 `@TableField("exist=false")`

  ```java
  @TableField("exist=false")
  private String address; // 该字段不会参与数据库操作
  ```

* **@TableLogic** —— **逻辑删除注解**
  标记该字段为逻辑删除字段，删除时更新状态而非物理删除

  ```java
  @TableLogic
  @TableField("is_deleted")
  private Integer deleted; // 0-未删除 1-已删除
  ```

  > 配合全局配置后，查询时自动过滤已删除数据，`deleteById()` 会转为 update 操作

  

* **@Version** —— **乐观锁注解**
  标记版本号字段，防止并发更新冲突

  ```java
  @Version
  @TableField("version")
  private Integer version; // 每次更新自动+1
  ```

  > ⚠️ 使用前需注册 `MybatisPlusInterceptor` 并添加 `OptimisticLockerInnerInterceptor`

  

* **@EnumValue** —— **枚举映射注解**
  指定枚举类中哪个属性对应数据库存储值

  ```java
  public enum Gender {
      MALE(1, "男"),
      FEMALE(2, "女");
      
      @EnumValue // 标记存储到数据库的值
      private final int code;
      private final String desc;
      
      // 构造方法和 getter
  }
  
  // 实体类中使用
  private Gender gender; // 数据库存储 1 或 2
  ```

  > 需配合 `MybatisPlusAutoConfiguration` 自动注册枚举处理器







#### 如何选择id的增长策略

| 策略               | 适用场景                   | 注意事项                         |
| ------------------ | -------------------------- | -------------------------------- |
| IdType.AUTO        | MySQL 等支持自增的数据库   | 需设置数据库字段为自增           |
| IdType.ASSIGN_ID   | 分布式系统（默认雪花算法） | 主键类型支持 Long/Integer/String |
| IdType.INPUT       | 需要手动指定 ID 的场景     | 需自行保证 ID 唯一性             |
| IdType.ASSIGN_UUID | String 类型主键            | 生成 32 位 UUID（不含 `-`）      |

- 单体应用 + MySQL：优先用 `IdType.AUTO`
- 分布式系统：用 `IdType.ASSIGN_ID`（默认雪花算法）
- 避免使用已废弃的 `ID_WORKER`/`UUID` 策略，改用新策略 `ASSIGN_ID`/`ASSIGN_UUID`









