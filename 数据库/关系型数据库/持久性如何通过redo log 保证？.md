
## Redo log

Redo log（重做日志）是 InnoDB 存储引擎独有的，它让 MySQL 拥有了崩溃恢复能力。



![](https://oss.javaguide.cn/github/javaguide/02.png)
**组成：**

Redo log 由两部分组成：重做日志缓冲 (redo log buffer ) 以及重做日志文件 (redo log file )，前者在内存中，后者在磁盘中。当事务提交之后会把所有修改信息都存到该日志文件中，**用于在刷新脏页到磁盘发生错误时(实例挂了或宕机)，进行数据恢复使用**

**工作过程：**

MySQL 中数据是以页为单位，你查询一条记录，会从硬盘把一页的数据加载出来，加载出来的数据叫数据页，会放入到 `Buffer Pool` 中。

**后续的查询都是先从 `Buffer Pool` 中找，没有命中再去硬盘加载，减少硬盘 IO 开销，提升性能。**

**更新表数据的时候，也是如此，发现 `Buffer Pool` 里存在要更新的数据，就直接在 `Buffer Pool` 里更新。**

**然后会把“在某个数据页上做了什么修改”记录到重做日志缓存（`redo log buffer`）里，接着刷盘到 redo log 文件里。**

![](https://oss.javaguide.cn/github/javaguide/03.png)



理想情况，事务一提交就会进行刷盘操作，但实际上，刷盘的时机是根据策略来进行的。

> (每条 redo 记录由“表空间号+数据页号+偏移量+修改数据长度+具体修改的数据”组成)



#### ==如果没有 redolog，可能会存在什么问题的？==

在 InnoDB 引擎中的内存结构中，主要的内存区域就是缓冲池，在缓冲池中缓存了很多的数据页。当我们在一个事务中，执行多个增删改的操作时， InnoDB 引擎会先操作缓冲池中的数据，如果缓冲区没有对应的数据，会通过后台线程将磁盘中的数据加载出来，存放在缓冲区中，然后将缓冲池中的数据修改，修改后的数据页我们称为脏页。而脏页则会在一定的时机，通过后台线程刷新到磁盘中，从而保证缓冲区与磁盘的数据一致。 而缓冲区的脏页数据并不是实时刷新的，而是一段时间之后将缓冲区的数据刷新到磁盘中，假如刷新到磁盘的过程出错了，而提示给用户事务提交成功，而数据却没有持久化下来，这就出现问题了，没有保证事务的持久性


### 刷盘时机

在 InnoDB 存储引擎中，**redo log buffer**（重做日志缓冲区）是一块用于暂存 redo log 的内存区域。为了确保事务的持久性和数据的一致性，InnoDB 会在特定时机将这块缓冲区中的日志数据刷新到磁盘上的 redo log 文件中。这些时机可以归纳为以下六种：

1. **事务提交时（最核心）**：当事务提交时，redo log buffer 里的 redo log 会被刷新到磁盘(`redolog file`)（可以通过 ` innodb_flush_log_at_trx_commit ` 参数控制，后文会提到）。
2. **redo log buffer 空间不足时**：这是 InnoDB 的一种主动容量管理策略，旨在避免因缓冲区写满而导致用户线程阻塞。
   - *当 redo log buffer 的已用空间超过其总容量的**一半 (50%)** 时，后台线程会**主动**将这部分日志刷新到磁盘，为后续的日志写入腾出空间*，这是一种“未雨绸缪”的优化。
   - 如果因为大事务或 I/O 繁忙导致 buffer 被**完全写满**，那么所有试图写入新日志的用户线程都会被**阻塞**，并强制进行一次同步刷盘，直到有可用空间为止。这种情况会影响数据库性能，应尽量避免。
3. **触发检查点 (Checkpo1int) 时**：Checkpoint 是 InnoDB 为了缩短崩溃恢复时间而设计的核心机制。当 Checkpoint 被触发时，InnoDB 需要将在此检查点之前的所有脏页刷写到磁盘。根据 **Write-Ahead Logging (WAL)** 原则，数据页写入磁盘前，其对应的 redo log 必须先落盘。因此，执行 Checkpoint 操作必然会确保相关的 redo log 也已经被刷新到了磁盘。
4. **后台线程周期性刷新**：InnoDB 有一个后台的 master thread，它会大约每秒执行一次例行任务，其中就包括将 redo log buffer 中的日志刷新到磁盘。这个机制是 `innodb_flush_log_at_trx_commit` 设置为 0 或 2 时的主要持久化保障。
5. **正常关闭服务器**：在 MySQL 服务器正常关闭的过程中，为了确保所有已提交事务的数据都被完整保存，InnoDB 会执行一次最终的刷盘操作，将 redo log buffer 中剩余的全部日志都清空并写入磁盘文件。
6. **binlog 切换时**：当开启 binlog 后，在 MySQL 采用 `innodb_flush_log_at_trx_commit=1` 和 `sync_binlog=1` 的 双一配置下，为了保证 redo log 和 binlog 之间状态的一致性（用于崩溃恢复或主从复制），在 binlog 文件写满或者手动执行 flush logs 进行切换时，会触发 redo log 的刷盘动作。

总之，**InnoDB 在多种情况下会刷新重做日志，以保证数据的持久性和一致性**。

我们要注意设置正确的刷盘策略 `innodb_flush_log_at_trx_commit` 。根据 MySQL 配置的刷盘策略的不同，MySQL 宕机之后可能会存在轻微的数据丢失问题。

`innodb_flush_log_at_trx_commit` 的值有 3 种，也就是共有 3 种刷盘策略：

- **0**：设置为 0 的时候，表示<u>每次事务提交时不进行刷盘操作。这种方式性能最高，但是也最不安全</u>，因为如果 MySQL 挂了或宕机了，可能会丢失最近 1 秒内的事务。
- **1**：设置为 1 的时候，表示<u>每次事务提交时都将进行刷盘操作。这种方式性能最低，但是也最安全，因为只要事务提交成功，redo log 记录就一定在磁盘里</u>，不会有任何数据丢失。
- **2**：设置为 2 的时候，表示每次事务提交时都只把 log buffer 里的 redo log 内容写入 page cache（文件系统缓存）。Page cache 是专门用来缓存文件的，这里被缓存的文件就是 redo log 文件。这种方式的性能和安全性都介于前两者中间。

**刷盘策略 `innodb_flush_log_at_trx_commit` 的默认值为 1，设置为 1 的时候才不会丢失任何数据。为了保证事务的持久性，我们必须将其设置为 1**。

> 另外，InnoDB 存储引擎有一个后台线程，每隔 `1` 秒，就会把 `redo log buffer` 中的内容写到文件系统缓存（`page cache`），然后调用 `fsync` 刷盘。
>
> ![](https://oss.javaguide.cn/github/javaguide/04.png)
>
> **也就是说，一个没有提交事务的 redo log 记录，也可能会刷盘。**
>
> ![](https://oss.javaguide.cn/github/javaguide/05.png)
>
> 
>
> 
>
> **除了后台线程每秒 `1` 次的轮询操作，还有一种情况，当 `redo log buffer` 占用的空间即将达到 `innodb_log_buffer_size` 一半的时候，后台线程会主动刷盘**



下面是不同刷盘策略的流程图

> #### Innodb_flush_log_at_trx_commit=0
>
> ![](https://oss.javaguide.cn/github/javaguide/06.png)
>
> 为 `0` 时，如果 MySQL 挂了或宕机可能会有 `1` 秒数据的丢失。
>
> #### Innodb_flush_log_at_trx_commit=1
>
> ![](https://oss.javaguide.cn/github/javaguide/07.png)
>
> 为 `1` 时， 只要事务提交成功，redo log 记录就一定在硬盘里，不会有任何数据丢失。
>
> 如果事务执行期间 MySQL 挂了或宕机，这部分日志丢了，但是事务并没有提交，所以日志丢了也不会有损失。
>
> #### Innodb_flush_log_at_trx_commit=2
>
> ![](https://oss.javaguide.cn/github/javaguide/09.png)
>
> 为 `2` 时， 只要事务提交成功，`redo log buffer` 中的内容只写入文件系统缓存（`page cache`）。
>
> 如果仅仅只是 MySQL 挂了不会有任何数据丢失，但是宕机可能会有 `1` 秒数据的丢失。
>

### 日志文件组

硬盘上存储的 redo log 日志文件不只一个，而是以一个**日志文件组**的形式出现的，每个的 `redo` 日志文件大小都是一样的。

比如可以配置为一组 `4` 个文件，每个文件的大小是 `1GB`，整个 redo log 日志文件组可以记录 `4G` 的内容。

它采用的是环形数组形式，从头开始写，写到末尾又回到头循环写，如下图所示。

![](https://oss.javaguide.cn/github/javaguide/10.png)

在这个**日志文件组**中还有两个重要的属性，分别是 `write pos、checkpoint`

- **write pos** 是当前记录的位置，一边写一边后移
- **checkpoint** 是当前要擦除的位置，也是往后推移

每次刷盘 redo log 记录到**日志文件组**中，`write pos` 位置就会后移更新。

<u>*每次 MySQL 加载**日志文件组**恢复数据时，会清空加载过的 redo log 记录，并把 `checkpoint` 后移更新*</u>。

`write pos` 和 `checkpoint` 之间的还空着的部分可以用来写入新的 redo log 记录。

![](https://oss.javaguide.cn/github/javaguide/11.png)

如果 `write pos` 追上 `checkpoint` ，表示**日志文件组**满了，这时候不能再写入新的 redo log 记录，MySQL 得停下来，清空一些记录，把 `checkpoint` 推进一下。

![](https://oss.javaguide.cn/github/javaguide/12.png)

注意从 MySQL 8.0.30 开始，日志文件组有了些许变化：

> The innodb_redo_log_capacity variable supersedes the innodb_log_files_in_group and innodb_log_file_size variables, which are deprecated. When the innodb_redo_log_capacity setting is defined, the innodb_log_files_in_group and innodb_log_file_size settings are ignored; otherwise, these settings are used to compute the innodb_redo_log_capacity setting (innodb_log_files_in_group \* innodb_log_file_size = innodb_redo_log_capacity). If none of those variables are set, redo log capacity is set to the innodb_redo_log_capacity default value, which is 104857600 bytes (100MB). The maximum redo log capacity is 128GB.

> Redo log files reside in the #innodb_redo directory in the data directory unless a different directory was specified by the innodb_log_group_home_dir variable. If innodb_log_group_home_dir was defined, the redo log files reside in the #innodb_redo directory in that directory. There are two types of redo log files, ordinary and spare. Ordinary redo log files are those being used. Spare redo log files are those waiting to be used. InnoDB tries to maintain 32 redo log files in total, with each file equal in size to 1/32 \* innodb_redo_log_capacity; however, file sizes may differ for a time after modifying the innodb_redo_log_capacity setting.

意思是在 MySQL 8.0.30 之前可以通过 `innodb_log_files_in_group` 和 `innodb_log_file_size` 配置日志文件组的文件数和文件大小，但在 MySQL 8.0.30 及之后的版本中，这两个变量已被废弃，即使被指定也是用来计算 `innodb_redo_log_capacity` 的值。而日志文件组的文件数则固定为 32，文件大小则为 `innodb_redo_log_capacity / 32` 。

> 关于这一点变化，我们可以验证一下。
>
> 首先创建一个配置文件，里面配置一下 `innodb_log_files_in_group` 和 `innodb_log_file_size` 的值：
>
> ```properties
> [mysqld]
> Innodb_log_file_size = 10485760
> Innodb_log_files_in_group = 64
> ```
>
> Docker 启动一个 MySQL 8.0.32 的容器：
>
> ```bash
> docker run -d -p 3312:3309 -e MYSQL_ROOT_PASSWORD=your-password -v /path/to/your/conf:/etc/mysql/conf. D --name
> MySQL830 mysql: 8.0.32
> ```
>
> 现在我们来看一下启动日志：
>
> ```plain
> 2023-08-03T02:05:11.720357Z 0 [Warning] [MY-013907] [InnoDB] Deprecated configuration parameters innodb_log_file_size and/or innodb_log_files_in_group have been used to compute innodb_redo_log_capacity=671088640. Please use innodb_redo_log_capacity instead.
> ```
>
> 这里也表明了 `innodb_log_files_in_group` 和 `innodb_log_file_size` 这两个变量是用来计算 `innodb_redo_log_capacity` ，且已经被废弃。
>
> 我们再看下日志文件组的文件数是多少：
>
> ![img](assets/redo-log-B8wAvbOi.png)
>
> 可以看到刚好是 32 个，并且每个日志文件的大小是 `671088640 / 32 = 20971520`
>
> <u>所以在使用 MySQL 8.0.30 及之后的版本时，推荐使用 `innodb_redo_log_capacity` 变量配置日志文件组</u>

### Redo log 小结

相信大家都知道 redo log 的作用和它的刷盘时机、存储形式。

现在我们来思考一个问题：**只要每次把修改后的数据页直接刷盘不就好了，还有 redo log 什么事？**

它们不都是刷盘么？差别在哪里？

```java
1 Byte = 8bit
1 KB = 1024 Byte
1 MB = 1024 KB
1 GB = 1024 MB
1 TB = 1024 GB
```

实际上，数据页大小是`16KB`，刷盘比较耗时，可能就修改了数据页里的几 `Byte` 数据，有必要把完整的数据页刷盘吗？

而且**数据页刷盘是随机写**，因为一个数据页对应的位置可能在硬盘文件的随机位置，所以**性能很差**。

如果是写 redo log，一行记录可能就占几十 `Byte`，只包含表空间号、数据页号、磁盘文件偏移量、更新值，再加上是**顺序写**，所以**刷盘速度很快**。

所以**用 redo log 形式记录修改内容**，性能会远远超过刷数据页的方式，这也让数据库的并发能力更强。

> 其实内存的数据页在一定时机也会刷盘，我们把这称为页合并，讲 `Buffer Pool`的时候会对这块细说





> #### ==为什么每一次提交事务，要刷新 redo log buffer 到磁盘中呢，而不是直接将buffer pool中的脏页刷新到磁盘?==
>
> 直接**刷脏页（Buffer Pool 中的数据页）**到磁盘存在两个巨大的性能瓶颈，而**刷 Redo Log** 则巧妙地规避了这些问题
>
> **A. 随机 I/O vs 顺序 I/O**
>
> - **直接刷脏页（随机 I/O）**：一个事务可能修改了表中分散在不同位置的多个数据页。如果要直接刷盘，就需要将这些分散在磁盘各处的数据页一个个写入，这属于**随机 I/O**，速度非常慢。
> - **刷 Redo Log（顺序 I/O）**：Redo Log 文件是追加写入的（Append-Only），新日志总是写在旧日志的后面。这种**顺序 I/O** 的写入速度远快于随机 I/O，能极大减少磁盘寻址时间。
>
> **B. 写入粒度大小**
>
> - **直接刷脏页（大粒度）**：InnoDB 的数据页默认大小是 16KB。即使你只修改了一个字节，为了保证事务持久性，你也需要把整个 16KB 的数据页刷到磁盘，非常浪费带宽。
> - **刷 Redo Log（小粒度）**：Redo Log 只记录“修改了什么”，日志体积极小。比如“将第100号页的第500字节从A改为B”，这条指令远小于 16KB。
>
> 口语版：因为在实际操作中，我们操作数据一般都是随机读写磁盘的，而不是顺序读写磁盘。 **而写入redo log是顺序IO**。顺序写的效率，要远大于随机写，mysql通过高效的顺序写入日志 加上 后台异步刷盘，大幅提高了效率
>
>  这种先写日志的方式，称之为 WAL（Write-Ahead Logging  ==预写日志机制==）
>
> > #### ==关键==
> >
> > **兑现操作记录（刷脏页）”和“写日志记录（刷Redo Log）”并不是在同一个时间点发生的。**
> >
> > 正是因为这两个动作**在时间上解耦了**，数据库才获得了巨大的性能提升。
> >
> > 简单来说：**事务提交时，数据库只做，把“慢的那部分”（根据redolog的记录随机写数据）扔给后台慢慢做**
> >
> > 事务提交时，数据库只做“将 Redo Log 刷盘”这一个同步动作**[顺序写日志，”快“]**。而脏页的落地，是由<u>后台线程（如 `Page Cleaner Threads`）</u>根据*系统负载、脏页比例、Redo Log 的水位线*等指标**异步**推进的。这种解耦是数据库高并发吞吐的基石

> #### 解析顺序IO与随机IO
>
> **“追加写入”意味着磁头（或写指针）永远只在文件末尾进行操作，不需要来回移动去寻找不同的存储位置。** 正是这种“位置的单一性和连续性”定义了顺序 I/O。
>
> ##### 1. 物理位置的差异：日志文件 vs 数据文件
>
> 要理解为什么是顺序 I/O，必须把“日志文件”和“数据文件”分开来看：
>
> - **数据文件的写入（随机 I/O 的根源）：**
>   假设你修改了用户 ID 为 100 的记录，它可能在磁盘数据文件的第 100 个数据块；接着你修改了用户 ID 为 5000 的记录，它可能在第 500 个数据块。如果直接写数据文件，磁头必须在第 100 块和第 500 块之间来回移动，这就是**随机 I/O**。
>
> - 日志文件的写入（顺序 I/O 的本质）：
>
>   Redo Log 是一个独立的、固定大小的物理文件（比如`ib_logfile0`）。不管你修改的是数据文件的哪个位置，你产生的日志统统追加在这个日志文件的末尾。
>
>   - 第一条日志写在文件开头。
>   - 第二条日志紧接在第一条后面。
>   - 第三条紧接在第二条后面。
>   - ...
>     **磁头不需要跳来跳去，只需要一直向前写，或者绕到开头继续写（循环写）。**
>
> ##### 2. “记录内容”不等于“写入位置”
>
> 你提到的“随机同步修改”，是指日志**记录的内容**描述了随机的修改动作，但这不影响日志**本身存储的位置**。
>
> - **日志内容（逻辑）：** “修改第100号页” -> “修改第500号页”。（看起来是跳跃的、随机的）。
> - **日志存储（物理）：** [物理地址1000] 存第一条 -> [物理地址1001] 存第二条。（在磁盘上是紧密相连的）。
>
> **打个比方：**
> 你在写日记（日志文件）。
>
> - **内容：** “早上去了公司A，下午去了医院B，晚上去了餐厅C。”（地点是随机的）。
> - **写法：** 你是在日记本上一行接一行地写下去的。（动作是顺序的）。
>
> 数据库写 Redo Log 就像写日记，不管业务逻辑跳到哪里，**落笔（写磁盘）永远是在最后一页的下一行**。
>
> ##### 3. 底层实现：环形缓冲区与检查点
>
> InnoDB 的 Redo Log 在磁盘上逻辑上是一个**环形结构**。
>
> - **指针移动：** InnoDB 维护了一个写入位置的指针。每次写日志，指针就向后移动相应的字节数。
> - **循环覆盖：** 当指针走到文件末尾时，它会绕回文件开头继续写（前提是这部分旧日志已经没用了，即已经通过检查点机制确认对应的脏页已刷入数据文件）。
>
> 在这个过程中，**磁头要么向前移动一点点（写入新内容），要么跳回开头（循环）**。相比于在数据文件中为了维护 B+ 树索引而频繁地在不同扇区跳转，这种写入模式的寻道时间极短，因此被归类为顺序 I/O。







## Binlog

Redo log 它是物理日志，记录内容是“在某个数据页上做了什么修改”，属于 InnoDB 存储引擎。

而 binlog 是逻辑日志，记录内容是语句的原始逻辑，类似于“给 ID=2 这一行的 c 字段加 1”，属于`MySQL Server` 层。

不管用什么存储引擎，只要发生了表数据更新，都会产生 binlog 日志。

那 binlog 到底是用来干嘛的？

可以说 MySQL 数据库的**数据备份、主备、主主、主从**都离不开 binlog，需要依靠 binlog 来同步数据，保证数据一致性。

![](https://oss.javaguide.cn/github/javaguide/01-20220305234724956.png)

Binlog 会记录所有涉及更新数据的逻辑操作，并且是顺序写。

### 记录格式

Binlog 日志有三种格式，可以通过`binlog_format`参数指定。

- **statement**
- **row**
- **mixed**

指定`statement`，记录的内容是`SQL`语句原文，比如执行一条`update T set update_time=now () where id=1`，记录的内容如下。

![](https://oss.javaguide.cn/github/javaguide/02-20220305234738688.png)

同步数据时，会执行记录的`SQL`语句，但是有个问题，`update_time=now ()`这里会获取当前系统时间，直接执行会导致与原库的数据不一致。

为了解决这种问题，我们需要指定为`row`，记录的内容不再是简单的`SQL`语句了，还包含操作的具体数据，记录内容如下。

![](https://oss.javaguide.cn/github/javaguide/03-20220305234742460.png)

`row`格式记录的内容看不到详细信息，要通过`mysqlbinlog`工具解析出来。

`update_time=now ()`变成了具体的时间`update_time=1627112756247`，条件后面的@1、@2、@3 都是该行数据第 1 个~3 个字段的原始值（**假设这张表只有 3 个字段**）。

这样就能保证同步数据的一致性，通常情况下都是指定为`row`，这样可以为数据库的恢复与同步带来更好的可靠性。

*但是这种格式，需要更大的容量来记录，比较占用空间，恢复与同步时会更消耗 IO 资源，影响执行速度*。

所以就有了一种折中的方案，指定为`mixed`，记录的内容是前两者的混合。

*MySQL 会判断这条`SQL`语句是否可能引起数据不一致，如果是，就用`row`格式，否则就用`statement`格式。*

### 写入机制

Binlog 的写入时机也非常简单，<u>事务执行过程中，先把日志写到`binlog cache`，事务提交的时候，再把`binlog cache`写到 binlog 文件中</u>。

因为一个事务的 binlog 不能被拆开，无论这个事务多大，也要确保一次性写入，所以系统会给每个线程分配一个块内存作为`binlog cache`

我们可以通过`binlog_cache_size`参数控制单个线程 binlog cache 大小，如果存储内容超过了这个参数，就要暂存到磁盘（`Swap`）。

Binlog 日志刷盘流程如下

![](https://oss.javaguide.cn/github/javaguide/04-20220305234747840.png)

- **上图的 write，是指把日志写入到文件系统的 page cache，并没有把数据持久化到磁盘，所以速度比较快**
- **上图的 fsync，才是将数据持久化到磁盘的操作**

`write`和`fsync`的时机，可以由参数`sync_binlog`控制，默认是`1`。

**为`0`的时候**，表示<u>每次提交事务都只`write`，由系统自行判断什么时候执行`fsync`</u>。

![](https://oss.javaguide.cn/github/javaguide/05-20220305234754405.png)

虽然性能得到提升，但是机器宕机，`page cache`里面的 binlog 会丢失。

为了安全起见，可以**设置为`1`**，表示每<u>次提交事务都会执行`fsync`</u>，就如同 **redo log 日志刷盘流程** 一样。

最后还有一种折中方式，可以设置为`N (N>1)`，表示<u>每次提交事务都`write`，但累积`N`个事务后才`fsync`</u>

![](https://oss.javaguide.cn/github/javaguide/06-20220305234801592.png)

> **在出现 IO 瓶颈的场景里，将`sync_binlog`设置成一个比较大的值，可以提升性能。**
>
> 但同样的，<u>如果机器宕机</u>，会丢失最近`N`个事务的 binlog 日志



## 两阶段提交

Redo log（重做日志）让 InnoDB 存储引擎拥有了崩溃恢复能力。

Binlog（归档日志）保证了 MySQL 集群架构的数据一致性。

虽然它们都属于持久化的保证，但是侧重点不同。

在执行更新语句过程，会记录 redo log 与 binlog 两块日志，以基本的事务为单位，**redo log  在事务执行过程中可以不断写入**，而 **binlog 只有在提交事务时才写入**，所以 redo log 与 binlog 的写入时机不一样。

> InnoDB 会在特定时机将 redo log buffer 中的日志数据刷新到磁盘上的 redo log 文件中。这些时机可以归纳为以下六种：
>
> 1. **事务提交时（最核心）**：当事务提交时，redo log buffer 里的 redo log 会被刷新到磁盘(`redolog file`)（` innodb_flush_log_at_trx_commit ` 参数为1时）。
> 2. **redo log buffer 空间不足时**：这是 InnoDB 的一种主动容量管理策略，旨在避免因缓冲区写满而导致用户线程阻塞。
>    - *当 redo log buffer 的已用空间超过其总容量的**一半 (50%)** 时，后台线程会**主动**将这部分日志刷新到磁盘，为后续的日志写入腾出空间*，这是一种“未雨绸缪”的优化。
>    - 如果因为大事务或 I/O 繁忙导致 buffer 被**完全写满**，那么所有试图写入新日志的用户线程都会被**阻塞**，并强制进行一次同步刷盘，直到有可用空间为止。这种情况会影响数据库性能，应尽量避免。
> 3. **触发检查点 (Checkpo1int) 时**：Checkpoint 是 InnoDB 为了缩短崩溃恢复时间而设计的核心机制。当 Checkpoint 被触发时，InnoDB 需要将在此检查点之前的所有脏页刷写到磁盘。根据 **Write-Ahead Logging (WAL)** 原则，数据页写入磁盘前，其对应的 redo log 必须先落盘。因此，执行 Checkpoint 操作必然会确保相关的 redo log 也已经被刷新到了磁盘。
> 4. **后台线程周期性刷新**：InnoDB 有一个后台的 master thread，它会大约每秒执行一次例行任务，其中就包括将 redo log buffer 中的日志刷新到磁盘。这个机制是 `innodb_flush_log_at_trx_commit` 设置为 0 或 2 时的主要持久化保障。
> 5. **正常关闭服务器**：在 MySQL 服务器正常关闭的过程中，为了确保所有已提交事务的数据都被完整保存，InnoDB 会执行一次最终的刷盘操作，将 redo log buffer 中剩余的全部日志都清空并写入磁盘文件。
> 6. **binlog 切换时**：当开启 binlog 后，在 MySQL 采用 `innodb_flush_log_at_trx_commit=1` 和 `sync_binlog=1` 的 双一配置下，为了保证 redo log 和 binlog 之间状态的一致性（用于崩溃恢复或主从复制），在 binlog 文件写满或者手动执行 flush logs 进行切换时，会触发 redo log 的刷盘动作。

![](https://oss.javaguide.cn/github/javaguide/01-20220305234816065.png)

回到正题，redo log 与 binlog 两份日志之间的逻辑不一致，会出现什么问题？

我们以`update`语句为例，假设`id=2`的记录，字段`c`值是`0`，把字段`c`值更新成`1`，`SQL`语句为`update T set c=1 where id=2`。

假设执行过程中写完 redo log 日志后，binlog 日志写期间发生了异常，会出现什么情况呢？

![](https://oss.javaguide.cn/github/javaguide/02-20220305234828662.png)

<u>由于 binlog 没写完就异常，这时候 binlog 里面没有对应的修改记录。因此，之后用 binlog 日志恢复数据时，就会少这一次更新，恢复出来的这一行`c`值是`0`，而原库因为 redo log 日志恢复，这一行`c`值是`1`，最终数据不一致</u>。

![](https://oss.javaguide.cn/github/javaguide/03-20220305235104445.png)

**为了解决两份日志之间的一致性问题**，InnoDB 存储引擎使用**两阶段提交**方案。

原理很简单，将 redo log 的写入拆成了两个步骤`prepare`和`commit`，这就是**两阶段提交**。

![](https://oss.javaguide.cn/github/javaguide/04-20220305234956774.png)



使用**两阶段提交**后，写入 binlog 时发生异常也不会有影响，因为 MySQL 根据 redo log 日志恢复数据时，**发现 redo log 还处于`prepare`阶段，并且没有对应 binlog 日志，就会回滚该事务**。

![](https://oss.javaguide.cn/github/javaguide/05-20220305234937243.png)

再看一个场景，redo log 设置`commit`阶段发生异常，那会不会回滚事务呢？

![](https://oss.javaguide.cn/github/javaguide/06-20220305234907651.png)

并不会回滚事务，它会执行上图框住的逻辑，虽然 **redo log 是处于`prepare`阶段，但是能通过事务`id`找到对应的 binlog 日志，所以 MySQL 认为是完整的，就会提交事务恢复数据**。

## Undo log

> 这部分内容为 JavaGuide 的补充：

每一个事务对数据的修改都会被记录到 undo log ，当执行事务过程中出现错误或者需要执行回滚操作的话，MySQL 可以利用 undo log 将数据恢复到事务开始之前的状态。

Undo log 属于逻辑日志，记录的是 SQL 语句，比如说事务执行一条 DELETE 语句，那 undo log 就会记录一条相对应的 INSERT 语句。同时，undo log 的信息也会被记录到 redo log 中，因为 undo log 也要实现持久性保护。并且，undo-log 本身是会被删除清理的，例如 INSERT 操作，在事务提交之后就可以清除掉了；UPDATE/DELETE 操作在事务提交不会立即删除，会加入 history list，由后台线程 purge 进行清理。

Undo log 是采用 segment（段）的方式来记录的，每个 undo 操作在记录的时候占用一个 **undo log segment**（undo 日志段），undo log segment 包含在 **rollback segment**（回滚段）中。事务开始时，需要为其分配一个 rollback segment。每个 rollback segment 有 1024 个 undo log segment，这有助于管理多个并发事务的回滚需求。

通常情况下， **rollback segment header**（通常在回滚段的第一个页）负责管理 rollback segment。Rollback segment header 是 rollback segment 的一部分，通常在回滚段的第一个页。**history list** 是 rollback segment header 的一部分，它的主要作用是记录所有已经提交但还没有被清理（purge）的事务的 undo log。这个列表使得 purge 线程能够找到并清理那些不再需要的 undo log 记录。

另外，`MVCC` 的实现依赖于：**隐藏字段、Read View、undo log**。在内部实现中，InnoDB 通过数据行的 `DB_TRX_ID` 和 `Read View` 来判断数据的可见性，如不可见，则通过数据行的 `DB_ROLL_PTR` 找到 undo log 中的历史版本。每个事务读到的数据版本可能是不一样的，在同一个事务中，用户只能看到该事务创建 `Read View` 之前已经提交的修改和该事务本身做的修改

## 总结

> 这部分内容为 JavaGuide 的补充：

MySQL InnoDB 引擎使用 **redo log (重做日志)** 保证事务的**持久性**，使用 **undo log (回滚日志)** 来保证事务的**原子性**。

MySQL 数据库的**数据备份、主备、主主、主从**都离不开 binlog，需要依靠 binlog 来同步数据，保证数据一致性。

## 参考

- 《MySQL 实战 45 讲》
- 《从零开始带你成为 MySQL 实战优化高手》
- 《MySQL 是怎样运行的：从根儿上理解 MySQL》
- 《MySQL 技术 Innodb 存储引擎》

----



> 事务进行过程中会不断写入 redo log buffer，redo log buffer 应该是在prepare阶段刷盘到redo log file的吧，binlog也是在事务进行过程中不断写入binlogcache，然后事务提交(还是prepare？)的时候write到 page cache ，然后根据sync策略在一定时机同步到binglog file

> 你的理解已经非常深入，触及了 MySQL 最核心的两阶段提交（2PC, Two-Phase Commit）机制。
>
> 针对你的问题，结论先行：
>
> 1. **Redo Log 的刷盘**：确实是在 **Prepare 阶段**完成主要的刷盘（`fsync`），但在 Commit 阶段还会写入一个额外的 Commit 标记。
> 2. **Binlog 的 Write 和 Sync**：完全是在 **Commit 阶段**进行的，与 Prepare 阶段无关。
>
> 为了让你完全理清这个过程，我们把事务从“进行中”到“最终提交”的每一个动作，按时间和空间维度进行拆解。
>
> ------
>
> #### 一、 事务进行中（未提交）
>
> 正如你所说，此时数据还在不断被修改。
>
> - **Redo Log**：不断写入 `redo log buffer`。
> - **Binlog**：每个线程有自己独立的 `binlog cache`（因为 binlog 必须保证事务的原子性，一个事务的 binlog 内容在磁盘上必须是连续的，不能和其他事务错综交织，所以先存在线程私有的 cache 中）。
>
> **注意一个细节：** 即使事务还没提交，`redo log buffer` 也有可能提前刷盘到 `redo log file` 中。
>
> - **原因**：InnoDB 有后台线程每秒刷新、或者当 `redo log buffer` 占用超过一半时，就会触发异步写盘。但此时磁盘里的 Redo Log 状态是 **未提交（Active）** 的。
>
> ------
>
> #### 二、 事务提交开始：两阶段提交（2PC）
>
> 当客户端执行 `COMMIT` 命令时，MySQL 开启两阶段提交，流程如下：
>
> #### 1. Prepare 阶段（InnoDB 层）
>
> 1. InnoDB 将该事务的底层操作记录状态设置为 **PREPARE**。
> 2. 将 `redo log buffer` 中的内容（包含 PREPARE 标记）写入（Write）**到操作系统的 Page Cache，并**同步（Fsync）刷新到磁盘的 `redo log file` 中。
>
> - **你的理解完全正确：** 确保 PREPARE 状态的 Redo Log 安全落盘，是这个阶段的核心任务。
>
> #### 2. Commit 阶段（Server 层 + InnoDB 层）
>
> 此时进入第二阶段，分为两个大步：
>
> - **第一步：写 Binlog（Server 层）**
>   1. 将该线程 `binlog cache` 中的所有内容，**Write** 到操作系统的 Page Cache（即 binlog file 的 OS 缓存）。
>   2. 根据参数 `sync_binlog` 的策略，调用 **Fsync** 真正同步到磁盘。
>      - 如果 `sync_binlog = 1`（最安全），此时立刻强制刷盘。
>      - **解答你的疑问**：Binlog 绝对是在 **Commit 阶段**进入 Page Cache 并依策略刷盘的，而不是 Prepare 阶段。一旦 Binlog 成功落盘，这个事务在逻辑上就被认为“不可逆转”了。
> - **第二步：完成事务 Commit（InnoDB 层）**
>   1. InnoDB 收到 Server 层 Binlog 写入成功的信号。
>   2. 在 Redo Log 中写入一个 **COMMIT** 标记（表示两阶段提交圆满结束）。
>   3. 释放锁，释放 `binlog cache`，事务彻底完成。
>      - *补充细节*：这个最后的 COMMIT 标记也会写入 `redo log buffer` 并刷盘，但它的刷盘策略受到 `innodb_flush_log_at_trx_commit` 的控制（如果为 1，则立刻写盘）。
>
> ------
>
> #### 三、 为什么要设计得这么复杂？（崩溃恢复的逻辑闭环）
>
> 为什么 Redo Log 要先在 Prepare 阶段刷盘，而 Binlog 要在 Commit 阶段刷盘？
>
> 我们可以假设在不同时机**发生宕机（Crash）**，看 MySQL 如何利用这两个日志实现一致性：
>
> - **场景 A：Redo Log 刚刷完 Prepare，Binlog 还没写，此时断电。**
>   - **恢复时发现**：Redo Log 里有 Prepare 记录，但拿着事务 ID 去 Binlog 里找，发现 **没有** 对应的记录。
>   - **处理方案**：说明 Binlog 没写成功，备库（Slave）肯定没有这条数据。为了保证主备一致，主库选择 **回滚（Rollback）** 该事务。
> - **场景 B：Redo Log 刷完 Prepare，Binlog 也写完并刷盘了，但在 Redo Log 写 COMMIT 标记前断电。**
>   - **恢复时发现**：Redo Log 里有 Prepare 记录，去 Binlog 里找，发现 **有** 对应的记录。
>   - **处理方案**：既然 Binlog 已经写盘，说明该事务可能已经发送给备库了。为了保证主备一致，主库在重启后选择 **提交（Commit）** 该事务（即使 Redo Log 缺失最后的 COMMIT 标记，也会帮它补上）。
>
> #### 总结验证
>
> 你的直觉和基础认知是完全准确的：
>
> - `redo log buffer` $\rightarrow$ `redo log file` 的硬刷盘（保证持久化准备）发生在 **Prepare 阶段**。
> - `binlog cache` $\rightarrow$ Page Cache $\rightarrow$ `binlog file` 发生在 **Commit 阶段**。















