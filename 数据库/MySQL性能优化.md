


# ==【MySQL性能优化】==

1. 定位慢查询

   * 聚合查询

   * 多表查询

   * 表数据量过大查询

   * 深度分页查询

     > 接口测试响应时间过长(大于1s)

2. 提高sql查询速度(解决慢查询)

   1. 分析SQL执行计划

3. 增加索引，提升性能

4. [优化SQL语句](#SQL优化)





## 定位慢查询

常见的慢查询：

* 聚合查询

* 多表查询

* 表数据量过大查询——添加索引

* 深度分页查询

  > 接口测试响应时间过长(大于1s)

### 如何定位

#### 1.开源工具

**调试工具：Arthas（阿尔萨斯）**

可以使用命令的方式监控已经上线的项目，可以跟踪执行比较慢的方法，查看方法的执行时间，最终确定哪里出了问题

**运维工具：Prometheus、Skywalking**



监控中会有各种指标，可以实时查看接口的响应情况，

> ![image-20251213144433160](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251213144433160.png)
>
> ![image-20251213144556741](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251213144556741.png)
>
> 



#### **2.查看MySQL自带的慢查询日志**



查看慢查询日志是否开启了

```
show variables like 'slow_query_log'
```

> **慢查询日志需要提前开启**
>
> （生产环境不建议开启，影响性能；开发环境开启）



**开启慢查询日志**：

> ![image-20251201171319906](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201171319906.png)
>
> 重启mysql
>
> ```
> //可通过重启容器 方式
> restart 
> ```
>
> 





查看最新追加的日志

```
tail -f localhost-slow.log
```

![image-20251213145424801](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251213145424801.png)

> * Query_time：这条语句从开始进入解析器到执行完毕的总耗时（默认记录的是 **从接收客户端请求到返回结果的总时间**，包括多个阶段，而在服务器本地执行时只测“纯 SQL 执行”阶段。）
>
>   > ##### ✅ 详细对比：两个场景的时间组成
>   >
>   > | 阶段                           | 在应用中执行（慢日志记录）   | 在 DB 服务器本地执行（`mysql` 命令行） |
>   > | ------------------------------ | ---------------------------- | -------------------------------------- |
>   > | 1. 网络传输（应用 → DB）       | ✅ 包含（可能几十~几百 ms）   | ❌ 无（本地 socket，几乎 0）            |
>   > | 2. SQL 解析 & 优化             | ✅ 包含                       | ✅ 包含                                 |
>   > | 3. **SQL 执行（真正查表）**    | ✅ 包含                       | ✅ 包含（你测的就是这个）               |
>   > | 4. **结果集返回（DB → 应用）** | ✅ 包含（大数据量时极慢！）   | ✅ 包含，但输出到终端很快               |
>   > | 5. 应用处理结果（如 ORM 映射） | ❌ 不包含（慢日志只到 DB 层） | ❌ 不包含                               |
>   >
>   > > ⚠️ **最关键的区别：结果集大小 + 网络带宽**
>
> * Lock_time：在真正开始执行之前，花在「等待锁」上的时间，单位秒
>
> * Rows_sent：最终返回给客户端的行数
>
> * Rows_examined：引擎为了返回这 Rows_sent 行，一共扫描/检查了 Rows_examined 行







#### 3.**查看profile详情**



![image-20251201183934183](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201183934183.png)

> 查看系统的having_profiling参数，判断是否支持profile操作
>
> ```
> SELECT @@have_profiling 
> ```
>
> 查看是否开启了profile操作
>
> ```
> select @@profiling;
> ```
>
> 开启开关
>
> ```
> setprofiling = 1;
> ```
>
> 查看会话中所有的sql语句 的执行耗时情况
>
> ```
> show profiles;
> ```
>
> 查看指定(query_id)的SQL语句<u>**各个阶段的耗时情况**</u>
>
> ```
> show profile for query query_id;
> //例 ↓
> show profile for query 16;
> ```
>
> 查看指定(query_id)的SQL语句的<u>**CPU使用情况**</u>
>
> ```
> show profile cpu for query query_id;
> //例 ↓
> show profile cpu for query 16;
> ```
>
> 







分析方式

1. **分析 SQL执行频率**
2. **分析(查看)慢查询日志**
3. 查看profile详情（查看各阶段耗时）
4. **<u>查看explain执行计划</u>**
5. 





#### (**分析 SQL执行频率**)

![image-20251201170512495](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201170512495.png)

> 通过查看执行频率，判断当前数据库以哪种操作方式为主
>
> 这里Com后面跟七个下划线 

![image-20251201170758480](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201170758480.png)



## 解决慢查询



### **分析sql的执行计划(explain)**

> **找到这条sql执行慢的原因**

![image-20251201184221159](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201184221159.png)



![image-20251201185342084](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201185342084.png)

> id表示查询的序列号，id相同，执行顺序从上到下；id不同，值大的先执行
>
> 如果访问系统表，会出现system；根据主键/唯一索引访问，一般会出现const
> 使用非唯一性索引(比如`where name  = '张三'`)，会出现ref；index：扫描索引，变遍历整个索引树；出现all，代表全表扫描
> 尽量往前优化





![image-20251201185257485](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201185257485.png)



**重点关注：type(看出来这条sql语句的性能大概怎么样)、possible_keys、key、key_len、Extra；**



> type
>
> (从上至下，效率降低)
>
> | type   | 含义                                                     | 说明                                     | 性能等级      | 备注     |
> | ------ | -------------------------------------------------------- | ---------------------------------------- | ------------- | -------- |
> | NULL   | 这条sql执行的时候没使用到表（少见）                      |                                          |               |          |
> | system | 这条sql查询的表是MySQL内置的表（少见）                   |                                          |               |          |
> | const  | 常量查询[只返回一条数据]（多见）                         | 主键或唯一索引等值查询，数据唯一且确定   | ⭐⭐⭐⭐⭐         |          |
> | eq_ref | 非主键的唯一索引查询，或两个表的等值连接[只返回一条数据] | 非主键的唯一索引查询，或两个表的等值连接 | ⭐⭐⭐⭐☆         |          |
> | ref    | 使用普通索引进行等值查询                                 | 普通索引匹配，可能返回多条记录           | ⭐⭐⭐☆☆         |          |
> | range  | 范围查询，走索引但范围扫描                               | 如 `BETWEEN`, `IN`, `>`, `<` 等操作      | ⭐⭐☆☆☆         | 最低要求 |
> | index  | 全索引扫描，遍历了整个索引树                             | 不走主键，而是扫描完整个索引             | ⭐☆☆☆☆         | 需要优化 |
> | all    | 全盘扫描                                                 |                                          | ☆☆☆☆☆（最差） | 需要优化 |
>
> Extra
>
> | Extra                        | 含义                                                         |
> | ---------------------------- | ------------------------------------------------------------ |
> | Using where;Using index      | 查找使用了索引，需要的数据都能在索引列中找到，不需要回表查询数据 |
> | Using index condition        | 查找使用了索引，但是需要回表查询数据——有优化空间             |
> | Using index(order by语句)    | 通过有序索引顺序扫描直接返回有序数据，不需要额外排序，操作效率高(order by语句中的解释) |
> | Using filesort(order by语句) | 通过表的索引或全表扫描，读取满足条件的数据行，然后在排序缓冲区sort buffer中完成排序操作，所有不是通过索引直接返回排序结果的排序都叫FileSort排序(order by语句中出现) |
>
> 
>
> 



#### [sql执行耗时测试]

可以直接在数据库看到

![image-20251213202345452](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251213202345452.png)







### 1.优化sql语句



#### limit优化

> PageHelper插件不会底层实现并不是覆盖索引+子查询，要自己手写
>
> ##### (PageHelper实现机制)
>
> PageHelper 是 MyBatis 生态中最流行的分页插件之一，它的**核心原理是通过 MyBatis 的插件（Interceptor）机制，在 SQL 执行前动态重写 SQL，加上 `LIMIT`（MySQL）、`ROWNUM`（Oracle）等分页语法**。
>
> ------
>
> ##### 📌 二、工作流程（以 MySQL 为例）
>
> 1. 调用 
>
>    ```
>    PageHelper.startPage(1, 10);
>    ```
>
>    - 将分页参数（pageNum=1, pageSize=10）存入 **ThreadLocal**
>
> 2. 执行 Mapper 查询方法
>
>    - 触发 MyBatis 的 `Executor.query()`
>
> 3. PageHelper 拦截该调用
>
>    - 从 ThreadLocal 取出分页参数
>    - 解析原始 SQL（如 `SELECT * FROM user WHERE name = ?`）
>    - 重写为：`SELECT * FROM user WHERE name = ? LIMIT 0, 10`
>
> 4. 同时发起一个 `COUNT(*)` 查询获取总数
>
> 5. 将结果封装为 `Page<User>` 对象返回
>
> ------
>
> ##### 🧩 三、拼接出来的 SQL 长什么样？
>
> ##### 场景 1：简单查询
>
> ```java
> PageHelper.startPage(2, 10);
> userMapper.selectAll(); // 原始 SQL: SELECT * FROM user
> ```
>
> ✅ **实际执行的 SQL**：
>
> ```sql
> -- 数据查询
> SELECT * FROM user LIMIT 10, 10;
> 
> -- 总数查询（自动触发）
> SELECT count(0) FROM user;
> ```
>
> ------
>
> ##### 场景 2：带条件的查询
>
> ```java
> PageHelper.startPage(1, 5);
> userMapper.selectByName("alice"); // 原始 SQL: SELECT * FROM user WHERE name = #{name}
> ```
>
> ##### ✅ **实际执行的 SQL**：
>
> ```sql
> -- 数据查询
> SELECT * FROM user WHERE name = 'alice' LIMIT 0, 5;
> 
> -- 总数查询
> SELECT count(0) FROM user WHERE name = 'alice';
> ```
>
> > ⚠️ 注意：PageHelper **不会解析 SQL 语义**，它只是在末尾加 `LIMIT`。如果原始 SQL 已有 `ORDER BY`、`GROUP BY`，它会保留。
>
> 
>
> 
>
> ##### ⚠️ PageHelper 的局限性（重点！）
>
> | 问题                     | 说明                                                         |
> | ------------------------ | ------------------------------------------------------------ |
> | **深分页性能差**         | `LIMIT 1000000, 10` 仍会扫描前 100 万行                      |
> | **不支持游标分页**       | 无法基于 `id > lastId` 优化                                  |
> | **COUNT 查询可能不准**   | 如果原始 SQL 有 `DISTINCT`、`GROUP BY`，需手动写 `countQuery` |
> | **SQL 注入风险（极低）** | 因为是参数化查询，一般安全                                   |
>
> ## 
>
> | 项目             | 说明                                        |
> | ---------------- | ------------------------------------------- |
> | **核心机制**     | MyBatis Interceptor + ThreadLocal           |
> | **SQL 改写**     | 在原始 SQL 末尾追加 `LIMIT offset, size`    |
> | **自动 COUNT**   | 会额外执行一次 `SELECT count(0) FROM (...)` |
> | **是否智能优化** | ❌ 不会自动用子查询/覆盖索引，需手动干预     |
> | **适用场景**     | 浅分页（前几十页），不适合深度分页          |
>
> ------
>
> ##### 💡 建议
>
> - **前 100 页**：放心用 PageHelper
> - **超过 100 页**：改用 **游标分页** 或 **子查询优化**
> - **高并发列表**：考虑前端禁止跳页，只允许“下一页”
>
> 



> **对于大页(>1000页)，我们最好自己手写覆盖索引+子查询的方式实现分页查询**



大数据量下，越往后，分页效率越低(limit 20,10    ->        limit 200000,10)

![image-20251203140537152](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203140537152.png)

优化方式：覆盖索引+子查询

```
例如：
select s.* from tb_sku s order by id limit 2000000,10;

select s.* from tb_sku s, (select id from tb_sku order by id limit 2000000,10) a where s.id = a.id;


```

> 子查询覆盖索引，主查询通过聚集索引
>
> 避免了原sql语句的低性能全表扫描



> 原sql语句
>
> 1. 数据库需要 **按 `id` 排序**（虽然主键本身有序，但不影响下面的问题）。
> 2. 为了跳过前 2,000,000 行，MySQL 必须 **逐行扫描并计数** 到第 2,000,001 行。
> 3. 在这个过程中，**每一条记录都要从磁盘或缓冲池中读取完整的行数据（即 `s.*`）**，即使前 200 万条最终被丢弃。（查询的是*，没有覆盖索引）
> 4. I/O 和内存开销巨大，尤其当表很大、行很宽（字段多/有 text/blob）时，性能急剧下降。
>
> > 💥 **问题核心**：**在跳过阶段也读取了整行数据**，做了大量无用功。
>
> 
>
> 改良语句
>
> ##### 第一步：执行子查询
>
> ```
> SELECT id FROM tb_sku ORDER BY id LIMIT 2000000, 10
> ```
>
> - 这里只查 `id` 字段。
> - 如果 `id` 是 **主键**（InnoDB 聚簇索引），或者有 **二级索引**，那么这个查询可以 **完全在索引上完成**，**无需回表**。
> - 这就是 **覆盖索引（Covering Index）**：**查询所需的所有字段都包含在索引中**，直接从索引 B+ 树中获取数据，速度极快。
> - 即使要跳过 200 万行，也只是在**紧凑的索引结构**中遍历，每个索引项很小（比如 8 字节 bigint），I/O 少、缓存效率高。
>
> ##### 第二步：用子查询结果关联主表
>
> ```
> SELECT s.* FROM tb_sku s INNER JOIN (...) a ON s.id = a.id
> ```
>
> - 子查询返回最多 10 个 `id` 值。
> - 然后通过这 10 个 `id` 去主表 `tb_sku` 中 **精确查找完整行**。
> - 因为 `id` 是主键（或有唯一索引），所以是 **10 次主键等值查询（回表）**，效率非常高。
>
> 



#### 范围查询优化

在业务允许的情况下，尽量使用>= / <=这样的范围查询（**范围查询( > / < )右边的列索引将会失效**）



#### 多条件查询优化

如果此多条件查询 想让它用到联合索引，注意**遵循最左前缀法则**(最左前缀法则指的是从索引的最左列开始，并且不跳过索引中的)



#### 联表查询优化

能用inner join ，就不用 left join/right join，如必须使用，一定要以小表为驱动

> 内连接会对两个表进行优化，优先把小表放到外边，把大表放到里边。left join / right join不会重新调整顺序

> 假设
>
> - 表 A：100 万行（大表）
> - 表 B：1 万行（小表）
>
> | 写法                                | 实际执行顺序              | 扫描行数估算         | 性能 |
> | :---------------------------------- | :------------------------ | :------------------- | :--- |
> | `select … from B inner join A on …` | 优化器自动把 B 放前       | 1 w × 索引 ≈ 1 w     | 快   |
> | `select … from A inner join B on …` | 同上，优化器仍会把 B 放前 | 1 w × 索引 ≈ 1 w     | 快   |
> | `select … from A left join B on …`  | **必须按你写的顺序** A→B  | 100 w × 索引 ≈ 100 w | 慢   |
> | `select … from B left join A on …`  | **必须按你写的顺序** B→A  | 1 w × 索引 ≈ 1 w     | 快   |





> 据说：数据量和业务量过大的情况下，不使用join，单独查询两个表的数据，在后端处理成需要的数据集合



.

#### order by语句优化

<u>优化order by 语句的时候，尽量优化为Using index;</u>

![image-20251203125651677](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203125651677.png)



**order by要走联合索引 就不能违背最左前缀法则（这里是要按顺序的）**

```
比如联合索引创建语句是：
CREATE INDEX idx_user_age_phone on tb_user(age, phone);

explain select id, age, phone from tb_user order by age, phone;//符合最左前缀法则
explain select id, age, phone from tb_user order by phone, age;//违背了最左前缀法则
explain select id, age, phone from tb_user order by age asc, phone desc;//这种情况，age会走Using index ；phone会走Using filesort（有额外的排序）
```

> ```
> CREATE INDEX idx_user_age_phone_ad on tb_user(age asc, phone desc);
> ```
>
> 如果这样创建索引，第三条sql就不会再排一次
>
>
> ![image-20251203131135823](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203131135823.png)



总

![image-20251203131334908](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203131334908.png)



#### group by 优化

测试

```
# 查看执行计划——根据profession分组
explain select profession, count(*) from tb_user group by profession;
# 创建索引
CREATE INDEX idx_user_pro_age_sta on tb_user(profession, age, status);

# 查看执行计划——根据profession字段分组
explain select profession, count(*) from tb_user group by profession;
//Using index;

# 查看执行计划——根据profession字段分组
explain select age, count(*) from tb_user group by age;
//Using index;Using temporary

# 查看执行计划——根据profession，age字段分组
explain select profession, count(*) from tb_user group by profession,age;


```

，





#### 避免索引失效

检查是否有 会让索引失效的 操作

> 研究where 之后的部分



1. 在**索引列上进行运算操作**，索引将失效
2. **字符串类型的字段**，**没加单引号**，索引将失效
3. 模糊查询：如果仅仅是尾部模糊匹配，索引不会失效，如果是**头部模糊匹配**，索引失效
   (%string会失效，string%不会失效)
4. **or连接的条件**
   用or分割开的条件，如果or前的条件中的列有索引，而后面的列中没有索引，那么涉及的索引都不会被用到
5. 有时mysql会根据字段的数据分布情况判断是否要走索引，如果mysql评估  走全表扫描比走索引更快，此时 索引也会失效
   (如果判断全表扫描效率更高，就全表扫描)
6. **避免直接使用`select *`**



#### insert语句优化



**一条语句批量插入  而不是  多条语句插入**

如果要批量插入，一次性插入的数据也不建议超过1000条（500-1000）

更大的数据量、把它分成多条语句插入



**改成手动提交事务**

MySQL的事务默认自动提交，这意味着你执行完一个insert语句之后，事务就会自动提交；这就会涉及到频繁的事务开启与提交，所以建议手动提交事务

```
start transaction
insert into tb_test value(1, 'Tom'),(2, 'Cat'),(3,'jerry');
insert into tb_test value(4, 'Tom'),(5, 'Cat'),(6,'jerry');
insert into tb_test value(7, 'Tom'),(8, 'Cat'),(9,'jerry');
commit
```



**主键顺序插入**

采用主键顺序插入，顺序插入的性能高于乱序插入

![image-20251202113054842](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202113054842.png)



**(load指令大批量插入)**

如果一次性需要插入大批量数据，使用insert语句插入性能较低，此时可以使用MySQL数据库提供的load指令进行插入，操作如下：

![image-20251203105142831](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203105142831.png)

```
# 客户端连接服务端时，加上参数 --local-infile
mysql --local-infile -u root -p
# 设置全局参数local_infile为1，开启从本地加载文件导入数据库的开关
set global local_infile = 1;
# 执行load指令将准备好的数据，加载到表结构中
load data local infile '/root/sql1.log' into table 'tb_user' fields terminated by ';' lines terminated by '\n'; 
```

> load插入的时候，使用主键顺序插入，顺序插入的性能高于乱序插入







#### update语句优化



![image-20251203180109211](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203180109211.png)

```
update course set name = 'JavaEE' where id = 1;//会给id为1的这行加上行锁 
update course set name = 'Kafka1' where id = 4;//不影响这条语句的更新

update course set name = 'SpringBoot' where name = 'PHP';//由于name这个字段没有索引，会加个表锁
update course set name = 'Kafka2' where id = 4;//此时就会影响这条语句就的 正常执行
```

> InnoDB的行锁是针对索引加的锁，不是针对记录加的锁，并且该索引不能失效，否则会从行锁升级为表锁



**总结：更新某个字段是一定要走索引，否则走全表扫描会变成表级锁**

**——执行update如果条件字段没有索引 会进行全表扫描，就会上表锁，所以<u>在update的sql的条件尽量要使用有索引的字段</u>**

> 表锁的并发性能低





### 2.添加索引



==**索引创建原则**==

1. 针对**数据量较大**，且**查询比较频繁的表**建立索引

   > 一百多万的数据量算大

2. 针对常作为**查询条件(where)、排序(order by)、分组(group by)操作的字段**建立索引

3. 尽量选择**区分度高的列**作为索引，尽量建立唯一索引，**区分度越高，使用索引的效率越高**

   > 常见区分度高的字段：用户名、手机号、身份证号
   >
   > 区分度不高的字段：关于状态的字段、关于逻辑删除的字段

4. 如果是字符串类型的字段，字段的长度较长，可以针对于字段的特点，建立前缀索引

5. **尽量使用联合索引**，减少单列索引，查询时，联合索引很多时候可以覆盖索引，节省存储空间，避免回表，提高查询效率

6. 创建**联合索引**的时候还需要**考虑字段顺序**

7. **尽量使用覆盖索引**（查询使用了索引，并且需要返回的列，在该索引中已经全部能找到）

8. 要**控制索引的数量**，索引并不是多多益善，索引越多，维护索引结构的代价也就越大，会影响增删改的效率

   > 只创建必要的索引，没必要的索引不创建  会影响增删改的效率

9. 如果索引列不能存储NULL值，请在创建表的时候使用**NOT NULL**约束它，当优化器知道每列是否包含NULL值时，它可以更好地确定哪个索引最有效地用于查询

**使用案例**

![image-20251201192228797](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201192228797.png)

> 由于sn这个字段没有索引，所以执行效率较低（21s）
>
> 给sn这个字段加了索引之后(`create index idx_sku_sn on tb_sku(sn)`)，时间优化到0.01s
>
> 



## 主从复制、读写分离







## 分库分表







## SQL优化



![image-20251203181837911](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203181837911.png)


![[技术学习/数据库/MySQL#SQL优化|MySQL]]




