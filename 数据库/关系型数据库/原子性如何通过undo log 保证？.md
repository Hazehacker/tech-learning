
> 补充到“MVCC”那篇博客

事务需要保证**原子性**，也就是事务中的操作要么全部完成，要么什么也不做。但有时候事务执行到一半会出现一些情况，比如：

- 情况一：事务执行过程中可能遇到各种错误，比如[服务器](https://cloud.tencent.com/product/cvm?from_column=20065&from=20065)本身的错误，操作系统错误，甚至是突然断电导致的错误。
- 情况二：DBA 可以在事务执行过程中手动输入 ROLLBACK 语句结束当前事务的执行。以上情况出现，<u>我们需要把数据改回原先的样子，这个过程称之为回滚</u>。

<u>每当我们要对一条记录做改动时</u> (这里的改动可以指 `INSERT、DELETE、UPDATE`)，<u>都需要把回滚时所需的东西记下来</u>。比如:

- 你插入一条记录时，至少要<u>把这条记录的主键值记下来，之后回滚的时候只需要把这个主键值对应的记录删掉就好了</u>。(对于每个 `INSERT`, InnoDB 存储引擎会完成一个 `DELETE`)
- 你删除了一条记录，至少要<u>把这条记录中的内容都记下来，这样之后回滚时再把由这些内容组成的记录插入到表中就好了</u>。(对于每个 `DELETE`, InnoDB 存储引擎会执行一个 `INSERT`)
- 你修改了一条记录，<u>至少要把修改这条记录前的旧值都记录下来，这样之后回滚时再把这条记录更新为旧值就好了</u>。(对于每个 `UPDATE`，InnoDB 存储引擎会执行一个 `相反的UPDATE`，将修改前的行放回去)

[MySQL](https://cloud.tencent.com/product/cdb?from_column=20065&from=20065) 把**这些为了回滚而记录的这些内容称之为 `撤销日志` 或者 `回滚日志` (即 Undo Log)**。注意，由于查询操作 (SELECT）并不会修改任何用户记录，所以在杳询操作行时，并不需要记录相应的 Undo日志

此外，**Undo Log 会产生 `Redo Log`，也就是 Undo Log 的产生会伴随着 Redo Log 的产生，这是因为 Undo Log 也需要持久性的保护**






### **Undo Log 的存储结构**

- **回滚段与 Undo 页**

InnoDB 对 Undo Log 的管理采用段的方式，也就是 `回滚段（rollback segment）`。每个回滚段记录了 `1024 个Undo Log segment`，而在每个 Undo Log segment 段中进行 `Undo页` 的申请。

在 `InnoDB1.1版本` 之前（不包括1.1版本），只有一个 `rollback segment`，因此支持同时在线的事务限制为 **1024**。虽然对绝大多数的应用来说都已经够用。

从1.1版本开始 InnoDB 支持最大 `128个rollback segment`，故其支持同时在线的事务限制提高到了 `**128*1024**`。


```javascript
mysql> show variables like 'innodb_undo_logs';
+------------------+-------+
| Variable_name    | Value |
+------------------+-------+
| innodb_undo_logs | 128   |
+------------------+-------+
```

虽然 InnoDB1.1版本支持了128个 `rollback segment`，但是这些 `rollback segment` 都存储于共享表空间**ibdata**中。从 lnnoDB1.2版本开始，可通过参数对 `rollback segment` 做进一步的设置。这些参数包括:

`innodb_undo_directory:` 设置 rollback segment 文件所在的路径。这意味着 rollback segment 可以存放在共享表空间以外的位置，即可以设置为独立表空间。该参数的默认值为“./”，表示当前 InnoDB 存储引擎的目录。

`innodb_undo_logs:` 设置 rollback segment 的个数，默认值为128。在 InnoDB1.2版本中，该参数用来替换之前版本的参数 innodb_rollback_segments。

`innodb_undo_tablespaces:` 设置构成 rollback segment 文件的数量，这样 rollback segment 可以较为平均地分布在多个文件中。设置该参数后，会在路径 innodb_undo_directory 看到 undo 为前缀的文件，该文件就代表 rollback segment 文件。

- **回滚段与事务**

1. 每个事务只会使用一个回滚段（rollback segment），一个回滚段在同一时刻可能会服务于多个事务。

2. 当一个事务开始的时候，会制定一个回滚段，在事务进行的过程中，当数据被修改时，原始的数据会被复制到回滚段。

3. 在回滚段中，事务会不断填充盘区，直到事务结束或所有的空间被用完。如果当前的盘区不够用，事务会在段中请求扩展下一个盘区，如果所有已分配的盘区都被用完，事务会覆盖最初的盘区或者在回滚段允许的情况下扩展新的盘区来使用。

4. 回滚段存在于 Undo 表空间中，在数据库中可以存在多个 Undo 表空间，但同一时刻只能使用一个 Undo 表空间。

5. 当事务提交时，InnoDB 存储引擎会做以下两件事情： 1. 将 Undo Log 放入列表中，以供之后的 purge (清洗、清除) 操作 2. 判断 Undo Log 所在的页是否可以重用 (低于3/4可以重用)，若可以分配给下个事务使用

- **回滚段中的数据分类**

`未提交的回滚数据(uncommitted undo information)：` 该数据所关联的事务并未提交，用于实现读一致性，所以该数据不能被其他事务的数据覆盖。

`已经提交但未过期的回滚数据(committed undo information)：` 该数据关联的事务已经提交，但是仍受到 undo retention 参数的保持时间的影响。

`事务已经提交并过期的数据(expired undo information)：` 事务已经提交，而且数据保存时间已经超过 undo retention 参数指定的时间，属于已经过期的数据。当回滚段满了之后，会优先覆盖"事务已经提交并过期的数据"。

- **Undo 页的重用**

当我们开启一个事务需要写 Undo log 的时候，就得先去 `Undo Log segment` 中去找到一个空闲的位置，当有空位的时候，就去申请 Undo 页，在这个申请到的 Undo 页中进行 Undo Log 的写入。我们知道 MySQL 默认一页的大小是 `16k`。

为每一个事务分配一个页，是非常浪费的 (除非你的事务非常长)，假设你的应用的 TPS (每秒处理的事务数目) 为1000，那么1s 就需要1000个页，大概需要16M 的存储，1分钟大概需要1G 的存储。如果照这样下去除非 MySQL 清理的非常勤快，否则随着时间的推移，磁盘空间会增长的非常快，而且很多空间都是浪费的。

**于是 Undo 页就被设计的可以重用了**，当事务提交时，并**不会立刻删除**Undo 页。因为重用，所以这个 Undo 页可能混杂着其他事务的 Undo Log。Undo Log 在 commit 后，会被放到一个链表中，然后判断 Undo 页的使用空间是否小于3/4，如果小于3/4的话，则表示当前的 Undo 页可以被重用，那么它就不会被回收，其他事务的 Undo Log 可以记录在当前 Undo 页的后面。由于 Undo Log 是离散的，所以清理对应的磁盘空间时，效率不高。

- **Undo Log 日志的存储机制**

![](https://developer.qcloudimg.com/http-save/yehe-9439753/f71b36d3a5f0612350e386ed097a02fc.png)

如上图，可以看到，Undo Log 日志里面不仅存放着数据更新前的记录，还记录着 `RowID`、`事务ID`、`回滚指针`。其中事务 ID 每次递增，回滚指针第一次如果是 INSERT 语句的话，回滚指针为 NULL，第二次 UPDATE 之后的 Undo Log 的回滚指针就会指向刚刚那一条 Undo Log 日志，以此类推，就会形成一条 Undo Log 的回滚链，方便找到该条记录的历史版本。




### **Undo Log 的工作原理**

在更新数据之前，MySQL 会提前生成 Undo Log 日志，当事务提交的时候，并不会立即删除 Undo Log，因为后面可能需要进行回滚操作，要执行回滚（ROLLBACK）操作时，从缓存中读取数据。Undo Log 日志的删除是通过通过后台 purge 线程进行回收处理的。

![](https://developer.qcloudimg.com/http-save/yehe-9439753/e9e1c16a5124947b75fd71d815d2f43b.png)

- 1、事务 A 执行 UPDATE 操作，此时事务还没提交，会将数据进行备份到对应的 Undo Buffer，然后由 Undo Buffer 持久化到磁盘中的 Undo Log 文件中，此时 Undo Log 保存了未提交之前的操作日志，接着将操作的数据，也就是 test 表的数据持久保存到 InnoDB 的数据文件 IBD。
- 2、此时事务 B 进行查询操作，直接从 Undo Buffer 缓存中进行读取，这时事务 A 还没提交事务，如果要回滚（ROLLBACK）事务，是不读磁盘的，先直接从 Undo Buffer 缓存读取

### **Undo Log 的类型**

在 InnoDB 存储引擎中，Undo Log 分为：

- **insert Undo Log** insert Undo Log 是指在 insert 操作中产生的 Undo Log。因为 insert 操作的记录，只对事务本身可见，对其他事务不可见 (这是事务隔离性的要求)，故该 Undo Log 可以在事务提交后直接删除。不需要进行 purge 操作。
- **update Undo Log** update Undo Log 记录的是对 delete 和 update 操作产生的 Undo Log。该 Undo Log 可能需要提供 MVCC 机制，因此**不能在事务提交时就进行删除**。提交时放入 Undo Log 链表，等待 purge 线程进行最后的删除。

### **Undo Log 的生命周期**

**简要生成过程** 以下是 Undo+Redo 事务的简化过程: 假设有2个数值，分别为 A=1 和 B=2 ，然后将 A 修改为3，B 修改为4

代码语言：javascript

AI 代码解释

```javascript
1.start transaction;
2.记录A=1到Undo Log;
3.update A = 3;
4.记录A=3 到Redo Log;
5.记录B=2到Undo Log;
6.update B = 4;
7.记录B=4到Redo Log;
8.将Redo Log刷新到磁盘;
9.commit
```

- 在1-8步骤的任意一步系统宕机，事务未提交，该事务就不会对磁盘上的数据做任何影响。
- 如果在8-9之间宕机。
    - Redo Log 进行恢复
    - Undo Log 发现有事务没完成，进行回滚。
- 若在9之后系统宕机，内存映射中变更的数据还来不及刷回磁盘，那么系统恢复之后，可以根据 Redo Log 把数据刷回磁盘。

流程图：

![](https://developer.qcloudimg.com/http-save/yehe-9439753/bbbd436b11ce06c53abd7ac8b9979d14.png)


#### 解读



你可以把：

- **Undo Log** 理解成“后悔药 / 回滚录像”
- **Redo Log** 理解成“重做录像 / 恢复录像”

它们一个负责：

- **Undo Log：事务失败时撤销**
- **Redo Log：宕机后恢复已提交的数据**

你现在卡住的点，其实是：

> 为什么既需要 Undo Log，又需要 Redo Log？

我们用你这个例子完整走一遍。

##### 一、先理解数据库真正的问题

数据库修改数据时：

```text
update A = 3;
```

实际上：

- 先改的是 **内存(Buffer Pool)**
- 之后某个时间才刷盘

因为：

> 磁盘太慢，不能每次 update 都立刻写磁盘。

所以就会出现一种危险情况

——假设：

```text
A 原来 = 1
```

事务已经 commit 了。

但是：

- 内存改成了 3
- 磁盘还是 1
- 此时突然断电

那：

```text
提交的数据丢了
```

这是绝对不允许的。



##### 二、Redo Log：保证“提交的数据不丢”

所以 MySQL 的思路是：



##### 正式数据页可以慢慢刷盘

但：

> 我先把“修改操作”记到一个小日志里。

这个日志：

- 顺序写
- 很快
- 容易落盘

这就是：

##### Redo Log

比如：

```text
A 从 1 改成 3
```

Redo Log 记录：

```text
把 A 改成 3
```

然后：

```text
redo log 落盘成功
```

即使：

- 数据页还没刷盘
- 系统宕机

恢复时：

```text
重新执行 redo log
```

数据又变回：

```text
A = 3
```

这叫：

##### WAL（Write Ahead Logging）

即：

> 先写日志，再写数据页。



##### 三、Undo Log：保证“失败事务可以撤销”

现在另一个问题：

假设事务执行到一半：

```text
A = 1 → 3
B = 2 → 4
```

突然：

```text
事务失败
```

那数据库必须恢复成：

```text
A = 1
B = 2
```

所以修改前：

数据库先记录旧值：

```text
A 原来是 1
B 原来是 2
```

这就是：

##### Undo Log

它记录的是：

> “怎么撤销这次修改”



##### 四、你这个流程真正发生了什么

我们一步步翻译。



**原始数据**

```text
A = 1
B = 2
```

事务想改成：

```text
A = 3
B = 4
```



**Step 1**

```javascript
start transaction;
```

开启事务。



**Step 2**

```javascript
记录A=1到Undo Log;
```

先记：

```text
A 原来是 1
```

因为以后可能回滚。

Undo Log：

```text
A -> 1
```



**Step 3**

```javascript
update A = 3;
```

修改 Buffer Pool 中的数据：

```text
A = 3
```

注意：

> 这里只是内存改了。

磁盘可能还是：

```text
A = 1
```



**Step 4**

```javascript
记录A=3 到Redo Log;
```

Redo Log 记录：

```text
把 A 改成 3
```



**Step 5~7**

同理：



Undo Log：

```text
B 原来 = 2
```

Redo Log：

```text
B 改成 4
```



**Step 8**

```javascript
将Redo Log刷新到磁盘;
```

这是最关键一步。

此时：

```text
redo log 已经安全
```

意味着：

> 即使现在断电，
> 也能恢复事务结果。

注意：

此时数据页可能还没刷盘！

磁盘可能还是：

```text
A = 1
B = 2
```

但没关系。

因为 redo log 在。



**Step 9**

```javascript
commit
```

事务正式提交。



##### 五、为什么 8~9 宕机也没事？

你原文这里最难理解：



##### 情况：

Redo Log 已落盘

但：

```text
commit 还没来得及写
```

系统宕机。

------

恢复时：

数据库发现：

```text
这个事务 redo log 完整
但事务状态未完成
```

此时：



##### Undo Log 登场

因为事务没真正 commit：

所以：

```text
要回滚
```

Undo Log 会：

```text
A: 3 -> 1
B: 4 -> 2
```

恢复原状。

------

##### 六、commit 后宕机为什么还能恢复？

这是：



##### 情况：

事务已经 commit。

但：

数据页还没刷盘。

磁盘还是：

```text
A=1
B=2
```

系统突然断电。



恢复时：

数据库看到：

```text
redo log 里有：
A=3
B=4
```

于是：

重新执行 redo log：

```text
A=3
B=4
```

这叫：

##### crash recovery（崩溃恢复）



##### 一张图理解

```text
事务修改数据：

        修改前
           ↓
     写 Undo Log
           ↓
      修改内存页
           ↓
     写 Redo Log
           ↓
 redo log 刷盘成功
           ↓
        commit
           ↓
   后台慢慢刷数据页
```



##### 为什么 MySQL 不直接刷数据页？

因为：

```text
随机磁盘写 太慢
```

而 redo log：

```text
顺序追加写
```

速度非常快。

所以：

------

真正提交事务时：

```text
只需要 redo log 落盘
```

不用等待真正数据页刷盘。

这就是：

##### InnoDB 高性能事务的核心。



### **Undo Log 的配置参数**

`innodb_max_undo_log_size:` Undo 日志文件的最大值，默认1GB，初始化大小10M

`innodb_undo_log_truncate:` 标识是否开启自动收缩 Undo Log 表空间的操作

`innodb_undo_tablespaces:` 设置独立表空间的个数，默认为0，标识不开启独立表空间，Undo 日志保存在 ibdata1中

`innodb_undo_directory:` Undo 日志存储的目录位置 innodb_undo_logs: 回滚的个数 默认128

### **参考文章**

- 《MySQL 是怎样运行的--从根儿上理解 MySQL》—小孩子4919 (**https://juejin.cn/book/6844733769996304392**)