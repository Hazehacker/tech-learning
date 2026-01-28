

#### 创建数据库





> ![image-20250814105950907](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814105950907.png)
>
> 

### 数据库相关

![image-20250814110851375](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814110851375.png)

> ![image-20250814110949990](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814110949990.png)

表名设为tableX



### DDL-表结构-创建表

![image-20250814112641354](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814112641354.png)

```
create table tablename(
	字段1 字段类型 [约束] [comment 字段1注释]
	字段1 字段类型 [约束] [comment 字段1注释]
	......
)[comment 表注释]
```

![image-20250814113947229](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814113947229.png)

![image-20250814131023569](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814131023569.png)

<u>**数值类型**</u>

![image-20250814131133481](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814131133481.png) 

![image-20250814132010260](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814132010260.png)

> <u>**年龄——tinyint unsigned**</u>
>
> <u>**ID——int unsigned**</u>



![image-20250814132549132](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814132549132.png)

> ![image-20250814132857277](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814132857277.png)
> username——varchar(50)
>
> idcard——char(18)
>
> phone——char(11)

![image-20250814132809860](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814132809860.png)

> <u>**birthday————date**</u>
>
> <u>**operateTime——datetime**</u>

### DLL-表结构-查询、修改、删除

```
show tables;											-- 查询当前数据库所有表
desc 表名;											   -- 查询表结构
show create table 表名;										 -- 查询建表语句

alter table 表名 add 字段名 类型(长度) [comment 注释] [约束]; -- 添加字段
alter table 表名 modify 字段名 新数据类型(长度);			  -- 修改字段类型
alter table 表名 change [comment 注释][约束];				  -- 修改字段名与字段类型
alter table 表名 drop column 字段名;						   -- 删除字段
alter table 表名 rename to 新表名;						   -- 修改表名

drop table [if exists] 表名;								 -- 删除表
```









> data query language

### DQL-数据-查询

![image-20250814190100925](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814190100925.png)



#### 基本查询-查询某个表所有数据



```
select count(*) from tableX;//计数
```

```
select sum(age) from tableX;//计算总年龄
```

```
select avg(salary) from emp;//查询平均薪资
```



```
-- 查询多个字段
select 字段1,字段2,字段3 from 表名;

-- 查询所有字段
select * from 表名;

-- 为查询字段设置别名，as关键字可以省略
select 字段1 [as 别名1],字段2 [as 别名2] from ;
select name as '姓名',entry_date as '入职日期' from emp;

-- 去除重复记录
select distinct 字段列表 from 表名;
```

> ![image-20250815090549979](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250815090549979.png)
>
> 推荐使用第一种：性能更高
>
> 先写select from 表名
>
> 再依据提示一键搞定所有字段名









#### 条件查询

```
select * from tableX where id = 1;
select * from tableX where name = '青戈' and password = '1234';
```

![image-20250815091331163](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250815091331163.png)



> 查询没有分配职位的员工信息
> select * from emp where job is null;
>
> 
>
> 查询有职位的员工信息
> select * from emp where job is not null;
>
> ![image-20250815092000690](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250815092000690.png)
>
> 
>
> 查询职位是  2 （讲师），3 （学工主管），4 （教研主管）的员工信息
> select * from emp where job=2 or job=3 or job=4;
>
> select * from emp where job in (2,3,4);
>
> 
>
> 







##### 模糊查询

_：单个字符；%：任意个字符

```
select * from tableX where name like '%哥哥%';//全部模糊
select * from tableX where name like '%哥哥';//前缀模糊
select * from tableX where name like '哥哥%';//后缀模糊
```

> 查询姓名为两个字符的员工信息
> select * from emp where name like '__';
>
> 
>
> 







新增查询

```
select * from tableX order by id desc;//按照id倒序排序（id越大、排越前面）
```

> desc--倒序排序
> adc---正序排序



#### 分组查询【一般用于统计个数】

![image-20250816095628260](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250816095628260.png)

```
SELECT * FROM employee GROUP BY sex HAVING sex = '男';
SELECT count(*) FROM employee GROUP BY sex HAVING sex = '男';
//统计数量

-- 统计企业员工数量
	-- 1. count(字段)——统计某个字段的条目数量(如果是null，就不会被统计进去)
	select count(id) from emp;
	-- 2. count(*)	——统计这张表的总条目
	select count(*) from emp;
	-- 3. count(常量)
	select count(1) from emp;

-- 统计员工平均薪资
select avg(salary) from emp;

-- 统计该企业员工的最低薪资
select min(salary) from emp;

-- 统计该企业员工的最高薪资
select max(salary) from emp

-- 每月给员工发放的薪资总额
select sum(salary) from emp;
```

> 统计数量优先考虑使用count*



![ ](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250816104039465.png)

![image-20250816104144391](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250816104144391.png)

```
-- 统计男性和女性员工数量
select gender from emp group by gender;

-- 先查询入职时间在'2015-01-01'（包含）以前的员工，并对结果根据职位分组，获取员工数量大于2的职位
select job,count(*) from emp where entry_date <= '2015-01-01' group by job having count(*)>2;


```

![image-20250816103847533](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250816103847533.png)



> HAVING后面跟条件
> 分组之后，select后的字段不能随意熟悉鹅，能写的一般是 分组字段+聚合函数









#### 排序查询

![image-20250816104341280](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250816104341280.png)

> 排序默认升序排序

```
-- 根据入职时间，对员工进行升序排序- asc
select * from emp order by entry_date asc;
select * from emp order by entry_date;


-- 根据入职时间，对员工进行降序排序- desc
select * from emp order by entry_date desc;

-- 根据入职时间，对员工进行升序排序，入职时间相同、再按照更新时间进行降序排序
select * from emp order by entry_date,update_time desc;

```





#### 分页查询

```
select * from tableX limit 0,3;//（第一行开始，显示三行）
```

> 第一个参数表示起始索引，第二个是显示多少个数据条目

> 如果起始索引是0，可以省略
>
> 

```
-- 从其实索引0开始查询员工数据，每页展示5条记录
select *from emp limit 0,5;
select *from emp limit 5;

-- 查询第1页员工数据，每页展示5条记录
select *from emp limit 0,5;

-- 查询第2页员工数据，每页展示5条记录
select *from emp limit 5,5;

-- 查询第3页员工数据，每页展示5条记录
select *from emp limit 10,5;




```

> 起始索引 = （查询页码-1）* 每页显示记录数









#### ==关联查询(多表查询)==

> 需要复习多表关系，可以看"数据库设计.md"



![image-20250912113540609](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912113540609.png)

![image-20250912113552400](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912113552400.png)

**内连接**

![image-20250912115755473](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912115755473.png)

> 内连接查询两个表达的交集部分

> 隐式：两个表之间用逗号分隔，后面跟连接条件（where关键字），其他查询条件用and连接后跟在后面
>
> ```
> select emp.id , emp.name, dept.name from emp, dept where emp.dept_id = dept.id and emp.gender = 1;
> ```
>
> 显式：多张表直接显式的使用关键字，后面更联查条件(on关键字)
>
> ```
> select emp.id ,emp.name ,dept.name from emp inner join dept on emp.dept_id = dept.id;
> select emp.id ,emp.name ,dept.name from emp join dept on emp.dept_id = dept.id;//(inner关键字可省略)
> 
> ```
>
> 

> 如果只有其中一个表有这个字段，可以不用加所属表；但是还是<u>**建议：如果涉及到多表查询，字段前面都加上所属的表名**</u>

> 给表起别名（了解）
>
> ![image-20250912115808229](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912115808229.png)
>
> ```
> select emp.id ,emp.name ,dept.name from emp as e inner join dept as d on e.dept_id = d.id;
> ```
>
> 

**外连接**

![image-20250912125539189](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912125539189.png)

> 左外连接完全包含左表的字段

> 查询员工表所有员工的姓名，和对应的部门名称
> select emp.name,dept.name from emp left join dept on emp.dept_id = dept.id

**子查询**

![image-20250912131201813](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912131201813.png)

 

> [套娃查询]
>
> ```
> select * from emp where entry_date = (select min(entry_date) from emp);
> ```
>
> ```
> select * from emp where entry_date > (select entry_date from emp where name = '阮小五')
> ```
>
> ![image-20250912135419554](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912135419554.png)
>
> ![image-20250912135714281](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912135714281.png)

> select * from emp where dept_id = (select id from dept where name = '教研部') and entry_time > '2011_05_01'
>
> ![image-20250912141308470](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912141308470.png)
>
> ![image-20250912142022878](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912142022878.png)
>
> ![image-20250912142619833](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250912142619833.png)
>
> 

select emp.*,dept.name from emp left join dept where emp.dept_id  = dept.id





```
SELECT employee.*,department.name as departmentName left join department on employee.department_id = department.id
//逻辑外键
```

![image-20250713210205847](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250713210205847.png)

> yid是tableX表里面的一个属性（关联两表），tableY.id是tableY表里面的id属性

> ### 大厂不让使用join查询（部分场景）

【【b站搜索关键词：关联 大厂】】



> ![image-20250913151551115](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913151551115.png)
>
> 如果两张表中存在两个字段名字一样，可以像这样给字段取个别名
>
> 







路径传参：/{id}    @PathVariable











> data manipulation language
>
> ![image-20250814183607944](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814183607944.png)

### DML-insert-插入



```
-- 为指定字段添加数据
insert into 表名 (字段1,字段2,字段3...) values (数据1,数据2,数据3...);
-- 为全部字段添加数据
insert into 表名 values (数据1,数据2,数据3...);
-- 批量添加数据（指定字段）
insert into 表名 (数据1,数据2,数据3) values (数据1,数据2,数据3),(数据a,数据b,数据c);
-- 批量添加数据（全部字段）
insert into 表名 values (数据1,数据2,数据3...),(数据1,数据2,数据3...);


```

insert into emp(username,password,name,gender,phone) values ('宋江')

> 如果要 插入当前时间，不要写死，使用mysql里面的now()函数

![image-20250814184957715](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814184957715.png)

> 双引号也可以，推荐用单引号



### DML-update-修改

```
update 表名 set 字段1 = 数据1,字段2 = 数据2... where id = x;
```

![image-20250814185212027](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814185212027.png)

#### 优化-动态sql

> 只更新有改动的字段

![image-20250913171333275](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913171333275.png)

```
update emp set 
	username=#{username},
	password=#{password},
	name=#{name},
	gender=#{gender}，
	phone=#{phone},
	{job}=#{job},
	salary=#{salary},
	image=#{image},
	entry_date=#{entryDate},
	dept_id=#{deptId},
	update_time=#{updateTime}
where id=#{id}
```

**动态sql版本**

> 注意：如果字段为String类型，不仅要判断
>
> ```
> !=null
> ```
>
> 、还要判断
>
> ```
> !=‘’
> ```
>
> 

```
update emp set 
	<set>
        <if test="username != null and username != ''">username = #{username},</if>
        <if test="password != null and password != ''">password = #{password},</if>
        <if test="name != null and name != ''">name = #{name},</if>
        <if test="gender != null">gender = #{gender},</if>
        <if test="phone != null and phone != ''">phone = #{phone},</if>
        <if test="job != null">job = #{job},</if>
        <if test="salary != null">salary = #{salary},</if>
        <if test="image != null and image != ''">image = #{image},</if>
        <if test="entryDate != null">entry_date = #{entryDate},</if>
        <if test="deptId != null">dept_id = #{deptId},</if>
        <if test="updateTime != null">update_time = #{updateTime}</if>
    </set>
where id=#{id}
```



> #### set标最后
>
> 1. 自动生成set关键字
>
> 2. 自动删除掉更新字段后多余的逗号
>
>    ![image-20250913173342465](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250913173342465.png)
>
>    比如如果最后一个字段没有被录入，整个sql语句就会多一个逗号，此时set标签会自动删除这个逗号
>
> 











### DML-delete-删除

```
delete from 表名 where id = x;
```

![image-20250814185619788](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250814185619788.png)



> ## 经验
>
> 一般不存储年龄，因为年龄是变化的，一般存储生日
>
> dept是department的简写

#### 设置某一字段为唯一索引

![image-20250713211613912](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20250713211613912.png)

* 即不能出现相同的值





## 函数



#### 字符串函数

![image-20251123202156357](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123202156357.png)

```
select concat('%','张三','%')

select lower('Hello')
	   upper('Hello')
	   lpad('01',5,'-') //用 - 填充字符串，填充到5位
	   rpad('01',5,'-') //
	   trim(' Hello MySQL ')
	   substring('Hello MySQL',1,5)  //从索引1位置开始截，截取5位  //注意这个函数的索引从1开始，不从0开始（特例）
	   
```



![image-20251123203100066](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123203100066.png)

```
update emp set workno = lpad(workno,5,'0')
```



#### 数值函数

![image-20251123203359209](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123203359209.png)



#### 日期函数



![image-20251123204132705](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123204132705.png)





![image-20251123204920585](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123204920585.png)

```
select name, DATEDIFF(CURDATE(), entrydate) AS 'entrydays' from emp where ORDER BY entrydays
```

> <u>取个别名，方便后面复用</u>













#### ==流程函数==

![image-20251123205025340](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123205025340.png)

```
IF(条件表达式, 'OK', 'Error') //此处：如果条件表达式 为true，返回OK，否则返回Error

IFNULL('OK', 'ERROR')
IFNULL(null, 'ERROR')

//语法；CASE WHEN THEN ELSE END
```



![image-20251123214019928](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123214019928.png)

```
select name, workaddress from emp
```

```
select 
	name, 
	(Case workaddress
		WHEN '北京' or '上海' THEN '一线城市'
		ELSE '二线城市'
	END) AS '工作地址'
	from emp 
```



```
select 
	id,
	name,
	(CASE 
		WHEN math >= 85 THEN '优秀'
		WHEN math >= 60 THEN '及格'
		ELSE '不及格'
	END) AS '数学成绩',
	(CASE 
		WHEN math >= 85 THEN '优秀'
		WHEN math >= 60 THEN '及格'
		ELSE '不及格'
	END) AS '语文成绩',
	(CASE 
		WHEN math >= 85 THEN '优秀'
		WHEN math >= 60 THEN '及格'
		ELSE '不及格'
	END) AS '英语成绩',
	FROM score;
```





















## 图形化界面

* column——字段





##### 查看不到近两年数据

可能是navicat的bug，将表示时间的字段按照降序排序就能看到



## 可视化查看数据库



（**SSH 隧道 + 本地可视化工具**）的意思是：**不直接让数据库暴露在公网上，而是通过你已经建立的 SSH 连接，把数据库端口“安全地转发”到你自己的电脑上**，然后用本地的图形化工具连接这个“本地转发端口”。

> ------
>
> ### 举个通俗的例子：
>
> - 你的云服务器 IP 是 `123.123.123.123`，上面运行着 MySQL（端口 3306），但 **只允许本机访问**（即 `localhost`）。
> - 你不想修改数据库配置或开放 3306 端口给公网（出于安全考虑）。
> - 于是你利用 SSH 的“隧道”功能，在你自己的电脑上开一个“假的”端口（比如 3307），所有发往这个端口的数据，都会通过 SSH 加密传送到云服务器，并自动转给服务器上的 `localhost:3306`。
>
> ------
>
> ### 具体操作步骤：
>
> **在这之前要先在阿里云安全组开放数据库的端口**
>
> #### 1. 在**本地电脑**打开终端（Windows 可用 PowerShell ）：
>
> ```bash
> ssh -L 3307:localhost:3306 your_username@123.123.123.123
> ```
>
> - ```
>   -L 3307:localhost:3306
>   ```
>
>    表示：
>
>   - 把本地的 `3307` 端口 → 转发到远程服务器的 `localhost:3306`
>
> - 输入密码后，保持这个终端窗口开着（SSH 隧道就建立了）
>
> #### 2. 打开你本地的数据库可视化工具（如 DBeaver、MySQL Workbench）：
>
> - 主机（Host）：`127.0.0.1`（或 `localhost`）
> - 端口（Port）：`3307`
> - 用户名/密码：填数据库的实际账号密码（如 root / your_password）
>
> ✅ 这样就能像操作本地数据库一样，**安全地可视化查看远程服务器上的数据库**，而且数据库本身不需要监听公网，更安全！
>
> ------
>
> ### 优点：
>
> - 不用改数据库配置（如 `bind-address`）
> - 不用在云平台开放数据库端口（如 3306）
> - 所有数据传输都经过 SSH 加密
>
> > 💡 提示：如果你用的是 Windows 且没有命令行经验，也可以用 **PuTTY** 或 **FinalShell** 等图形化 SSH 工具设置端口转发。



方案一：本地 Navicat → SSH 隧道 → 云服务器本机 MySQL
（无需开 3306，无需改安全组，只要你能 SSH 登录服务器）

1. 打开 Navicat → 新建连接 → 选「MySQL」。

2. 先填「常规」页

   - 主机：localhost（必须填 localhost，因为隧道打通后就是本地端口）
   - 端口：3306（或你自定义的本地端口，如 3307）
   - 用户名/密码：云服务器里 MySQL 的账号密码。

3. 切到「SSH」页 → 勾选「使用 SSH 隧道」

   - 主机：云服务器的公网 IP
   - 端口：22
   - 用户名/密码（或私钥）：你平时 SSH 登录用的账号。

4. 点「测试连接」→ 成功后保存即可。

5. 双击连接就能可视化查看、编辑库表数据，全程 3306 端口未对外开放，安全组无需任何改动 

   。





## 大.sql文件导入



本地数据库：

在本地打开cmd

如果.sql文件 里面没有创建数据库的语句，自己要先创建数据库

```
mysql -u 用户名 -p 数据库名 < your_file.sql
```

```
mysql -u 用户名 -p 数据库名 < D:\Desk\klassbox3.sql
```



如果.sql文件 里面有创建数据库的语句

```
mysql -u 用户名 -p < your_file.sql
```



云服务器环境：用“重定向进容器”最稳妥

```bash
# 把文件内容通过标准输入灌进去，同时指定数据库
docker exec -i mysql mysql -uroot -pHazenixbzh66MySQLbulai seata < seata-tc.sql
```



### .sql文件导出



```
mysqldump -u username -p database_name > backup.sql
```



```
docker cp mysql-container:/database_name.sql /root/database_name.sql
```



## 细节

> mapper里面的注释要这样写
>
> ```
> <!-- buy 类型处理 -->
> ```
>
> 这种运行时会报错：`-- 注释`





# 约束

![image-20251123215143153](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123215143153.png)

 **约束作用于表中字段， 可以再创建表/修改表的时候添加约束**

一个字段可以有多个约束



#### 约束演示

![image-20251123215806833](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123215806833.png)

```
create table user {
	id int primary key auto_increment comment '主键',
	name varchar(10) not null unique comment '姓名',
	age int check( age > 0 && age<=120) comment '年龄',
	status char(1) default '1' comment '状态',
	gender tinyint comment '性别'
} comment '用户表';
```





#### (外键约束)











# 事务



## 事务简介



![image-20251123221754781](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123221754781.png)





## 事务四大特性

![image-20251123222935905](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251123222935905.png)

















## 并发事务问题

![image-20251124080443346](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251124080443346.png)

![image-20251124080622471](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251124080622471.png)

![image-20251124080815422](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251124080815422.png)

> 这条数据在读取的时间间隔中被另一个事务更新了



![image-20251124080955816](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251124080955816.png)

> 这个场景的前提是已经解决了不可重复读，即读取时不会读取其他事物改变后的结果
>
> 现象：数据插入的时候无法插入(提示已经存在)，但读取又无法读取的情况









## 事务隔离级别





![image-20251124081404292](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251124081404292.png)

* 读未提交：三种并发事务问题都可能出现
* **读已提交**：解决了脏读问题
* 可重复读：解决了脏读和不可重复读问题（默认）
* 串行化：解决了三种并发问题
  使用锁机制，性能差，不能随便用



事务隔离级别越高，数据越安全，性能越低 











解决了表















# 复杂sql案例





#### 单据



涉及表

* record

  > 

* sell

* sell_bill

* sre

* sre_bill

* buy

* buy_bill

* bre

* bre_bill

* swap

* entry

* extry



#### 购销费用查询

* 前端结构

  ![image-20251109085034354](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251109085034354.png)

* 返回的DTO

  ![image-20251109084925120](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251109084925120.png)



sql编写（返回List<TradeExpenseResponseDTO>）

> 





**sql讲解**

> 这段 SQL 是一个 **购销费用报表查询语句**，目的是从多个业务表中提取与“支出/核销”相关的记录，并统一格式化为 `TradeExpenseResponseDTO` 所需的字段结构。
>
> 下面我将逐部分详细解释它的逻辑和设计思路：
>
> ------
>
> ## 🔍 一、目标
>
> 生成一份包含以下信息的报表：
>
> - 单据类型（采购、销售、退货等）
> - 往来单位（客户或供应商）
> - 支出类别（运输费、包装费等）
> - 金额
> - 结算状态
> - 单据编号、时间、组织等元数据
>
> 这些数据分散在多个表中，需要通过 `record` 表作为“事件中心”进行关联。
>
> ------
>
> ## 🧱 二、核心表说明
>
> | 表名        | 作用                                                         |
> | ----------- | ------------------------------------------------------------ |
> | `record`    | 记录系统中所有业务事件（如创建采购单、核销费用等），包含 `type`（事件类型）和 `source`（关联主键） |
> | `buy`       | 采购订单                                                     |
> | `sell`      | 销售订单                                                     |
> | `bre`       | 采购退货单（buy Return）                                     |
> | `sre`       | 销售退货单（Sell Return）                                    |
> | `buy_bill`  | 采购核销明细（费用项）                                       |
> | `sell_bill` | 销售核销明细                                                 |
> | `sre_bill`  | 销售退货核销明细                                             |
>
> > 💡 注意：`*_bill` 表中的 `pid` 字段指向对应主单（如 `buy.id`），而 `record.source` 也指向这个主单 ID。
>
> ------
>
> ## 📌 三、SELECT 字段解析
>
> ### 1. 基础字段
>
> ```sql
> r.id AS id,                -- record 主键，作为 DTO 的 id
> r.source AS clazz,         -- 关联的单据主键（如 buy.id）
> r.type AS documentType     -- 事件类型，如 'buy_bill'
> ```
>
> ### 2. 动态映射字段（使用 `CASE WHEN`）
>
> #### 所属组织 (`organization`)
>
> ```sql
> CASE 
>     WHEN r.type IN ('buy_bill', 'buy') THEN b.frame
>     WHEN r.type IN ('sell_bill', 'sell') THEN s.frame
>     WHEN r.type IN ('sre_bill', 'sre') THEN sr.frame
>     ELSE br.frame
> END AS organization
> ```
>
> - 根据 `record.type` 判断是哪种单据，然后从对应的表取 `frame`（所属组织）。
>
> #### 往来单位 (`partnerUnit`)
>
> ```sql
> CASE 
>     WHEN r.type IN ('buy_bill', 'buy') THEN b.supplier   -- 采购 → 供应商
>     WHEN r.type IN ('sell_bill', 'sell') THEN s.customer -- 销售 → 客户
>     WHEN r.type IN ('sre_bill', 'sre') THEN sr.customer  -- 销售退货 → 客户
>     ELSE br.supplier
> END AS partnerUnit
> ```
>
> #### 单据时间 (`documentDate`)、编号 (`documentNumber`)
>
> 类似逻辑，根据类型从不同表取 `time` 和 `number`。
>
> #### 支出类别 (`expenseType`)
>
> ```sql
> COALESCE(bb.type, sb.type, srb.type) AS expenseType
> ```
>
> - `COALESCE` 返回第一个非空值。
> - 因为每条记录只属于一种 bill 类型，所以只会有一个非 NULL。
>
> #### 金额 (`amount`)
>
> ```sql
> CASE 
>     WHEN r.type = 'buy_bill' THEN bb.money
>     WHEN r.type = 'sell_bill' THEN sb.money
>     WHEN r.type = 'sre_bill' THEN srb.money
>     ELSE NULL
> END AS amount
> ```
>
> - 精确匹配 bill 类型，取出对应金额。
>
> #### 结算状态 (`settlementStatus`)
>
> ```sql
> CASE 
>     WHEN r.type IN ('buy_bill', 'buy') THEN b.cse
>     ...
> END AS settlementStatus
> ```
>
> - `cse` 字段在主单表中（buy/sell 等），表示结算状态（0~3）。
>
> ------
>
> ## 🔗 四、JOIN 逻辑
>
> ```sql
> FROM record r
> LEFT JOIN buy b ON r.source = b.id AND r.type IN ('buy', 'buy_bill')
> LEFT JOIN sell s ON r.source = s.id AND r.type IN ('sell', 'sell_bill')
> ...
> LEFT JOIN buy_bill bb ON bb.pid = r.source AND r.type = 'buy_bill'
> ...
> ```
>
> - **为什么用 LEFT JOIN？**
>   因为 `record` 是驱动表，我们只关心特定类型的事件，其他表可能不匹配，但不能丢弃主记录。
> - **ON 条件中加了 `r.type` 限制**
>   避免错误关联。例如：只有当 `r.type = 'buy_bill'` 时，才尝试关联 `buy` 和 `buy_bill`。
>
> ------
>
> ## 🚦 五、WHERE 条件
>
> ```sql
> WHERE r.type IN ('buy_bill', 'sell_bill', 'sre_bill')
>   AND r.source IS NOT NULL
>   AND (bb.money > 0 OR sb.money > 0 OR srb.money > 0)
> ```
>
> - 只查 **核销事件**（`*_bill` 类型）
> - 排除无效记录（`source` 为空）
> - **只保留金额 > 0 的费用项**（避免导出零值或负值干扰报表）
>
> > ✅ 这个条件确保结果都是有效的“支出”记录。
>
> ------
>
> ## 📈 六、排序
>
> ```sql
> ORDER BY r.time DESC
> ```
>
> - 按 `record` 表的时间倒序排列（最新事件在前）
>
> > ⚠️ 注意：`record.time` 是否存在？你前面没提。如果 `record` 表没有 `time` 字段，这里会报错！
> > 实际上，单据时间应来自 `buy.time` 等，建议改为：
> >
> > ```sql
> > ORDER BY documentDate DESC
> > ```
> >
> > 或者在 SELECT 中加一个 `real_time` 字段用于排序。
>
> ------
>
> ## ⚠️ 七、潜在问题 & 优化建议
>
> ### 1. **`sre` 的往来单位可能是 `customer` 而非 `supplier`**
>
> ```sql
> -- 当前：
> WHEN r.type IN ('sre_bill', 'sre') THEN sr.supplier
> 
> -- 建议改为：
> WHEN r.type IN ('sre_bill', 'sre') THEN sr.customer
> ```
>
> ### 2. **`ORDER BY r.time` 可能无效**
>
> 确认 `record` 表是否有 `time` 字段。如果没有，应改用计算出的 `documentDate` 排序。
>
> ### 3. **性能问题**
>
> - 如果数据量大，建议在 `record(type, source)` 上建索引。
> - `OR` 条件可能影响性能，可考虑 UNION ALL 拆分查询。
>
> ### 4. **`bre_bill` 缺失？**
>
> 你提到了采购退货（`bre`），但没有 `bre_bill` 表。如果存在，也需要加入。
>
> ------
>
> ## ✅ 总结
>
> 这段 SQL 的设计思路是：
>
> > **以 `record` 表为中心，根据事件类型动态关联不同业务单据和核销明细，最终统一输出标准化的费用报表数据。**
>
> 它巧妙地使用了：
>
> - `CASE WHEN` 实现多表字段映射
> - `LEFT JOIN + ON 条件过滤` 避免错误关联
> - `COALESCE` 简化多来源字段取值
>
> 只要确认字段名和业务逻辑无误（特别是 `sre` 的往来单位和排序字段），这段 SQL 就能正确工作。
>
> 需要我帮你改成 **UNION ALL 版本**（更清晰、可能更快）吗？





# 



# MySQL体系结构





![image-20251126164359785](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251126164359785.png)

存储引擎控制MySQL中数据存储和提取的方式，服务器会通过API和存储引擎来进行通讯



> **索引是在存储引擎层实现的，这意味着不同的存储引擎的索引结构不一样**

![image-20251126164626126](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251126164626126.png)







# 存储引擎



## 存储引擎简介

存储引擎就是存储数据、简历索引、更新/查询数据等技术的实现方式

，而不是基于库的，所以存储引擎也可能被称为表类型  s

> 决定了数据库存储、获取、更新、查询的方式

```
// 创建表user，并指定MyISAM存储引擎
create table user {
	id int,
	name varchar(10)
} engine = MyISAM ;

//查看当前数据库支持的存储引擎
SHOW ENGINES;

```







## 存储引擎特点

**InnoDB**

* **InnoDB**

  * 介绍
    InnoDB是一种兼顾高可靠性和高性能的通用存储引擎，在MySQL5.5之后，InnoDB是默认的MySQL存储引擎

  * 特点
    DML操作遵循ACID模型，支持事务；
    行级锁，提高并发访问性能

    支持外键约束

* 涉及到的磁盘文件

  xxx.ibd: xxx代表的是表名，innoDB引擎的每张表都会对应这样一个的表空间文件，存储该表的表结构（frm、sdi）、数据和引擎

  参数：innodb_file_per_table（这个参数决定：每张表对应一个表空间文件/所有表对应一个表空间文件）[默认每一张表对应一个表空间文件]

  > user 表对应 user.ibd文件

* 逻辑存储结构
  TableSpace：表空间
  Segment：段
  Extent：区
  Page：页
  Row：行

  ![image-20251126183248745](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251126183248745.png)

* 





**MyISAM**



* 介绍
  MySQL早期的默认存储引擎
* 缺点
  不支持事务，不支持外键
  支持表锁，不支持行锁
  访问速度快
* 涉及到的磁盘文件
  xxx.sdi：存储表结构信息
  xxx.MYD：存储数据
  xxx.MYI：存储索引



**Memory**

* 介绍
  Memory引擎的表数据存储在内存中，由于受到硬件问题、或断点问题的影响，只能将这些表作为临时表或缓存使用

* 特点
  内存存放
  支持Hash索引

* 涉及到的磁盘文件

  xxx.sdi：存储表结构信息



![image-20251126191926652](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251126191926652.png)





## 存储引擎选择



![image-20251126192112693](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251126192112693.png)

InnoDB：存储业务系统对于事务、数据完整性要求较高的核心数据

MyISAM：存储业务系统的非核心事务

> 后面两个用得少，因为
>
> 在MyISAM的使用场景中，大多使用MongoDB
>
> 在MEMORY的使用场景中，大多使用Redis5











# 索引





## 概述

![image-20251201080038944](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201080038944.png)

![image-20251201080316094](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201080316094.png)

无索引：全全表扫描

使用索引：就是使用数据结构





* 优点

  * 提高数据的检索速度，降低了数据库的IO成本
  * 通过索引列队数据进行排序，降低数据排序的成本，降低CPU的消耗

* 缺点

  * 占用磁盘空间

    > 但是磁盘很便宜

  * 会降低更新表达的速度（INSERT/UPDATE/DELETE）

    > 对于一个正常的业务系统，增删改的比例很小，主要是查询



索引 为什么叫索引？

> 这是一个非常好的问题！“索引”这个词听起来有点抽象，但它其实来源于我们日常生活中非常熟悉的概念——**书籍的索引（index）**。数据库中的“索引”正是借用了这个比喻。
>
> ------
>
> ##### 📚 一、现实中的“索引”：书本的索引页
>
> 想象你有一本厚厚的《操作系统原理》教材，你想找“虚拟内存”相关内容：
>
> - **没有索引**：你得从第1页翻到最后一章，逐页查找 → **效率低（全表扫描）**
>
> - 有索引
>
>   ：书末有个“索引”章节，写着：
>
>   ```
>   虚拟内存 ………… 见 第142, 156, 178页
>   ```
>
>   你直接翻到142页 → 
>
>   效率高
>
> 这里的“索引”就是一个**按关键词排序的目录**，它不包含全文，但告诉你**去哪里找内容**。
>
> ------
>
> ##### 💾 二、数据库中的“索引”：同样的逻辑
>
> 在数据库中：
>
> - **表的数据行** = 书的正文内容（存储在磁盘上，可能很大）
> - **索引** = 书末的索引页（一个额外的、更小的、有序的数据结构）
>
> 例如，你对 `users` 表的 `email` 字段建索引：
>
> ```sql
> CREATE INDEX idx_email ON users(email);
> ```
>
> 数据库会额外维护一个**按 email 排序的结构**（通常是 B+ 树），每个条目包含：
>
> - `email` 的值（如 `alice@example.com`）
> - 指向实际数据行的“指针”（比如主键 ID 或物理地址）
>
> 当你查询：
>
> ```sql
> SELECT * FROM users WHERE email = 'alice@example.com';
> ```
>
> 数据库不用扫描整张表，而是：
>
> 1. 在索引中**快速查找** `alice@example.com`（利用 B+ 树的 O(log n) 查找）
> 2. 通过索引中的指针**直接定位到数据行**
> 3. 返回结果
>
> 这就像查书的索引一样：**先查索引，再跳转到正文**。
>
> ------
>
> ##### 🌟 三、为什么叫“索引”？核心原因
>
> | 特性             | 书本索引          | 数据库索引                   |
> | ---------------- | ----------------- | ---------------------------- |
> | **目的**         | 快速定位内容位置  | 快速定位数据行位置           |
> | **结构**         | 按关键词排序      | 按字段值排序（B+树、哈希等） |
> | **本身不含全文** | 只有关键词 + 页码 | 只有字段值 + 指针            |
> | **额外开销**     | 占几页纸          | 占额外存储空间               |
> | **加速查找**     | 避免全文翻阅      | 避免全表扫描                 |
>
> 所以，“索引”这个名字非常贴切：**它是一个指向真实数据的“指引目录”**。
>
> ------
>
> ##### 🔍 补充：技术术语的来源
>
> - 英文 “**index**” 本意就是“指示器”“目录”“指标”
> - 在计算机科学早期（1950–60年代），数据库先驱们直接借用了图书馆/出版领域的术语
> - 中文翻译为“索引”，也沿用了图书检索的含义（“索”有“搜寻、探求”之意）
>
> > 类似例子：  
> >
> > - “**缓存（cache）**” 原意是“藏匿处”  
> > - “**垃圾回收（garbage collection）**” 像收垃圾一样清理内存
>
> ------
>
> ##### ✅ 总结
>
> > **数据库索引之所以叫“索引”，是因为它和书本末尾的索引功能完全一致：
> > 不存储全部内容，但提供一种快速找到内容位置的方法。**
>
> 它不是靠“魔法”，而是靠**精心设计的数据结构（如 B+ 树）+ 额外存储空间**，换取查询速度的大幅提升——这正是“空间换时间”的经典体现。
>
> 





## 索引结构



![image-20251201080623275](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201080623275.png)

> 只有memory支持Hash索引

![image-20251201080848236](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201080848236.png)



![image-20251201081015075](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201081015075.png)



![image-20251201081323979](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201081323979.png)

![image-20251201081930799](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201081930799.png)



**B+树**

![image-20251201082335846](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201082335846.png)

![image-20251201082407388](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201082407388.png)



**Hash索引**



![image-20251201155243985](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201155243985.png)

![image-20251201155431590](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201155431590.png)

> 等值匹配：根据这个key计算hash值，然后根据hash值到链表中查找对应的元素
>
> 所以说查找用的是等值匹配，由于存储的时候不是顺序存储的、不支持范围查询





![image-20251201155831656](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201155831656.png)

## 索引分类

![image-20251201160302359](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201160302359.png)



![image-20251201160552859](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201160552859.png)

> 二级索引（也叫辅助索引/非聚集索引）



![image-20251201161531551](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201161531551.png)

> 字符串之间也能进行大小比较

查询案例（回表查询）

> ![image-20251201162037026](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201162037026.png)
>
> 


![image-20251201163058130](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201163058130.png)

> 即使存储两千多万的数据，也只有三层





## 索引语法



![image-20251201164823560](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201164823560.png)



案例

> ![image-20251201165052765](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201165052765.png)
>
> ```
> CREATE INDEX idx_username ON tb_user(name);
> CREATE INDEX idx_phone ON tb_user(phone);
> CREATE INDEX idx_pro_age_sta ON tb_user(profession,age,status);
> CREATE INDEX idx_email ON tb_user(email);''
> ```
>
> 

















## SQL性能优化

常见的慢查询：

* 聚合查询

* 多表查询

* 表数据量过大查询

* 深度分页查询

  > 接口测试响应时间过长(大于1s)



#### **查看MySQL自带的慢查询日志**



查看慢查询日志是否开启了

```
show variables like 'slow_query_log'
```

> **慢查询日志需要提前开启**
>
> （生产环境不建议开启，影响性能；开发环境开启）



**开启慢查询日志**：

![image-20251201171319906](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201171319906.png)

> 进入容器，修改变量
>
> ```
> -- 查看当前状态
> SHOW VARIABLES LIKE 'slow_query_log';
> SHOW VARIABLES LIKE 'long_query_time';
> 
> -- 开启慢查询日志（临时）
> SET GLOBAL slow_query_log = 'ON';
> SET GLOBAL long_query_time = 0.2;
> SET GLOBAL slow_query_log_file = '/var/log/mysql/slow.log';
> ```
>
> 

重启mysql

```
//通过重启容器 方式
restart 
```

查看最新追加的日志

```
tail -f localhost-slow.log
```















#### **查看profile详情**



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





#### **分析 SQL执行频率**

![image-20251201170512495](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201170512495.png)

> 通过查看执行频率，判断当前数据库以哪种操作方式为主
>
> 这里Com后面跟七个下划线 

![image-20251201170758480](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201170758480.png)





#### **查看explain执行计划**

![image-20251201184221159](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201184221159.png)



![image-20251201185342084](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201185342084.png)

> id表示查询的序列号，id相同，执行顺序从上到下；id不同，值大的先执行
>
> 如果访问系统表，会出现system；根据主键/唯一索引访问，一般会出现const
> 使用非唯一性索引(比如`where name  = '张三'`)，会出现ref；index：扫描索引，变遍历整个索引树；出现all，代表全表扫描
> 尽量往前优化





![image-20251201185257485](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201185257485.png)



重点关注：type(看出来这条sql语句的性能大概怎么样)、possible_keys、key、key_len；













## 索引使用原则





**使用案例**

![image-20251201192228797](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201192228797.png)

> 由于sn这个字段没有索引，所以执行效率较低（21s）
>
> 给sn这个字段加了索引之后(`create index idx_sku_sn on tb_sku(sn)`)，时间优化到0.01s
>
> 



#### 联合索引-**最左前缀法则**

> 研究where之后的部分

![image-20251201201032215](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251201201032215.png)

> 索引最左边的字段必须存在（但是不一定要放在where条件的最左边(可交换)）



**范围查询( > / < )右边的列索引将会失效**

> ```
> select * from tb_user where profession = '软件工程' and age > 30 and status = '0';
> ```
>
> #### 如何规避
>
> 在业务允许的情况下，尽量使用>= / <=这样的范围查询
>
> 

#### 索引失效的情况

> 研究where 之后的部分

1. 在索引列上进行运算操作，索引将失效
2. 字符串类型的字段，没加单引号，索引将失效
3. 模糊查询：如果仅仅是尾部模糊匹配，索引不会失效，如果是头部模糊匹配，索引失效
   (%string会失效，string%不会失效)
4. or连接的条件
   用or分割开的条件，如果or前的条件中的列有索引，而后面的列中没有索引，那么涉及的索引都不会被用到
5. 有时mysql会根据字段的数据分布情况判断是否要走索引，如果mysql评估  走全表扫描比走索引更快，此时 索引也会失效
   (如果判断全表扫描效率更高，就全表扫描)











在索引列上进行运算操作，索引将失效



**字符串类型的字段，没加单引号，索引将失效**

> 不加单引号，会有隐式类型转换

```
select * from tb_user where phone = '13685067027';

select * from tb_user where phone = 13685067027;
explain select * from tb_user where phone = 13685067027;
```



**模糊查询：如果仅仅是尾部模糊匹配，索引不会失效，如果是头部模糊匹配，索引失效**

**<u>如果是大数据量的情况，一定要规避模糊查询前面加百分号的情况</u>**

```
explain select * from tb_user where profession like '软件%';
explain select * from tb_user where profession like '%工程、';
explain select * from tb_user where profession like '%工%';
```



**用or分割开的条件，如果or前的条件中的列有索引，而后面的列中没有索引，那么涉及的索引都不会被用到**

```
explain select * from tb_user where id = 10 or age = 23;
explain select * from tb_user where phone = '13685067027' or age = 23;
```



**is NULL 和 is not NULL 走不走索引不是固定的，取决于当前数据库表结构这个字段的数据分布情况**

```
explain select * from tb_user where profession is not null;
explain select * from tb_user where profession is null;
```







#### **覆盖索引**

> 研究select 之后的部分

尽量使用覆盖索引（查询使用了索引，并且需要返回的列，在该索引中已经全部能找到），减少select *



**案例**

> ```
> explain select * from tb_user where profession = '软件工程' and age = 31 and status = '0';
> ```
>
> 
>
> explain表中的extra的解释：
>
> using index condition：查找使用了索引，但是需要回表查询数据
> using where;using index：查找使用了索引，但是需要的数据都在该索引列中能找到，所以不需要回表查询数据



回表查询图解

> 第二条sql语句查询所返回的字段没有超出覆盖索引的范畴，不需要回表查询
> 第三条sql查询所返回的字段超出了覆盖索引的范畴，需要回表查询
>
> ![image-20251202100149739](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202100149739.png)





应用：对sql进行优化

> ![image-20251202100447478](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202100447478.png)
>
> 建立username、password联合索引



#### 前缀索引

降低索引占用空间，提高磁盘效率

![image-20251202100651995](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202100651995.png)

> 判断选择截取前几个
>
> ```
> select count(distinct substring(email,1,10))/count(*) from tb_user;
> select count(distinct substring(email,1,6))/count(*) from tb_user;
> select count(distinct substring(email,1,5))/count(*) from tb_user;
> 
> ```
>
> 
>
> ![image-20251202102859181](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202102859181.png)
>
> 创建email取前五个字符的 前缀索引(只针对前五个字符创建索引)
>
> ```
> CREATE INDEX idx_email_5 on tb_user(email(5));
> ```
>
> ![image-20251202103610532](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202103610532.png)
>
> 





#### 单列索引&联合索引的选择

![image-20251202103914309](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202103914309.png)

> 也就是说，上面这个sql语句中涉及到的两个索引 只被用到了一个

**多个查询条件，尽量使用联合索引：效率较高，而且使用得当(覆盖索引)就能避免回表查询**



![image-20251202104646418](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202104646418.png)

创建联合索引的时候还需要考虑字段顺序

> 比如上面这个sql中，phone在最左、则要求使用联合索引的时候必须包含phone这个字段











## 索引设计原则

![image-20251202104843551](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202104843551.png)



1. 针对数据量较大，且查询比较频繁的表建立索引

   > 一百多万的数据量算大

2. 针对常作为查询条件(where)、排序(order by)、分组(group by)操作的字段建立索引

3. 尽量选择区分度高的列作为索引，尽量建立唯一索引，区分度越高，使用索引的效率越高

   > 常见区分度高的字段：用户名、手机号、身份证号
   >
   > 区分度不高的字段：关于状态的字段、关于逻辑删除的字段

4. 如果是字符串类型的字段，字段的长度较长，可以针对于字段的特点，建立前缀索引

5. 尽量使用联合索引，减少单列索引，查询时，联合索引很多时候可以覆盖索引，节省存储空间，避免回表，提高查询效率

6. 要控制索引的数量，索引并不是多多益善，索引越多，维护索引结构的代价也就越大，会影响增删改的效率

   > 只创建必要的索引，没必要的索引不创建  会影响增删改的效率

7. 如果索引列不能存储NULL值，请在创建表的时候使用NOT NULL约束它，当优化器知道每列是否包含NULL值时，它可以更好地确定哪个索引最有效地用于查询











# SQL优化



![image-20251203181837911](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203181837911.png)

## insert语句优化



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





## 主键优化



![image-20251203110815668](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203110815668.png)

![image-20251203123338309](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203123338309.png)

**页分裂现象**

主键顺序插入：

![image-20251203123525092](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203123525092.png)



主键乱序插入：

可能发生页分裂现象，需要 移动数据，调整链表指针



![image-20251203123919467](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203123919467.png)

**页合并**

![image-20251203123945304](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203123945304.png)

![image-20251203124015446](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203124015446.png)



#### 主键设计原则

1. 满足业务需求的情况下，尽量降低主键的长度

   > 二级索引中挂的是主键的值，如果主键长度比较长、二级索引比较多，就会占用较大磁盘空间  在搜索的时候耗费大量的磁盘IO

2. 插入数据时，尽量选择顺序插入，选择使用AUTO_INCREMENT自增主键

   > 不会出现页分裂现象

3. 尽量不要使用UUID或其他自然主键(如身份证号) 作为主键

   > UUID无序，插入的时候就是乱序插入 存在页分裂现象
   >
   > 身份证号 长度较长，会耗费大量磁盘IO

4. 业务操作时，避免对主键的修改

   > 修改主键 还需要动对应的索引结构，代价很大





## order by优化

![image-20251203125159814](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203125159814.png)

优化order by 语句的时候，尽量优化为Using index;

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







## group by优化





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









## limit优化

> 面试会问



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



## count优化

![image-20251203142756241](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203142756241.png)

> “自己计数”指自己建一张表，专门计数、并添加维护的逻辑



**count的几种用法以及几种用法之间的性能差异**

![image-20251203174616514](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203174616514.png)

![image-20251203174631099](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203174631099.png)

所以**选择count(*)**





sql8对count进行优化会自动走索引





## update语句优化



![image-20251203180109211](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203180109211.png)

```
update course set name = 'JavaEE' where id = 1;//会给id为1的这行加上行锁 
update course set name = 'Kafka1' where id = 4;//不影响这条语句的更新

update course set name = 'SpringBoot' where name = 'PHP';//由于name这个字段没有索引，会加个表锁
update course set name = 'Kafka2' where id = 4;//此时就会影响这条语句就的 正常执行
```

> InnoDB的行锁是针对索引加的锁，不是针对记录加的锁，并且该索引不能失效，否则会从行锁升级为表锁



**总结：更新某个字段是一定要走索引，否则走全表扫描会变成表级锁**

**——执行update如果条件字段没有索引 会进行全表扫描，就会上表锁，所以在update的sql的条件尽量要使用有索引的字段**

> 表锁的并发性能低





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



























































# 视图/存储过程/存储函数/触发器





## 视图







## 存储过程



## 存储函数





## 触发器





# 锁



## 概述

![image-20251203182017805](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203182017805.png)

MySQL提供了各种锁来应对并发场景

按照锁的粒度分，有：

1. 全局锁：锁定数据库中的所有表
1. 表级锁：每次操作锁住整张表
1. 行级锁：每次操作锁住对应的行数据





## 全局锁



![image-20251203182903677](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203182903677.png)

> 场景：全库的逻辑备份

![image-20251203190033545](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203190033545.png)





![image-20251203183152421](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203183152421.png)

案例 指令操作

```
flush tables with read lock;
mysqldump -uroot -p1234 itcast > itcast.sql
unlock tables;
```

















## 表级锁

![image-20251203190335055](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203190335055.png)

#### 表锁



读锁：加上之后只能读 不能写，不会阻塞其他客户端的读

写锁：加上之后可以读 可以写，阻塞其他客户端的读和写

![image-20251203190747333](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203190747333.png)



#### **元数据锁**

维护表结构的

![image-20251203191927036](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203191927036.png)

对一张表进行增删改查的时候，会加MDL读锁(共享锁)【其中<u>增删改</u>加的是SHARED_WRITE锁，<u>查</u>加的是SHARED_READ锁】，当对表结构进行变更操作的时候，加MDL写锁(排他锁)【也就是EXCLUSIVE类型】

只要不是修改表结构，另外两种又分为查询和增删改，查询是shared_read，增删改锁类型是shared_write。修改表结构锁类型是exclusive

> **有两种类型，一种是表数据锁，一种是元数据锁。各自分别又有共享和排他锁，这里有4种锁**



#### 意向锁

![image-20251203202123906](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203202123906.png)

![image-20251203202331002](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203202331002.png)



> 弹幕：
>
> 工作的时候 其实一般很少用锁的  主要的原因因为很多时候 锁定表以后 其他的线程可能会处于阻塞状态 ，很影响效率
>
> 不是很少用锁，你执行的sql䣰，MySQL已经帮你实现了加锁，释放锁的过程
>
> 这么说吧 写锁读锁要注意前面的前缀，究竟是表锁中的还是元数据中的要分清楚 他们有点不一样



![image-20251203202538057](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203202538057.png)

过于难，暂时不搞，先前进

后面这集开始看[69. 进阶-锁-表级锁-意向锁_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Kr4y1i7ru?spm_id_from=333.788.player.switch&vd_source=67ef3bb4c8d68a96408acdaa865b1313&p=126)















## 行级锁































# InnoDB引擎



## 逻辑存储结构

![image-20251202113743308](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202113743308.png)





![image-20251202114415464](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202114415464.png)

> 每一个ibd文件，都是一个表空间文件



## 架构

![image-20251202114703551](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202114703551.png)



### 内存结构

**缓冲池：**

![image-20251202115225117](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202115225117.png)



**更改缓冲区：**

![image-20251202132546028](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202132546028.png)

> 唯一索引和一级索引不会操作“更改缓冲区”

把changeBuffer中的数据以一定频率刷新到BufferPool，然后再刷新到磁盘当中



**自适应哈希索引**

![image-20251202132652012](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202132652012.png)



**日志缓冲区**

![image-20251202132927335](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202132927335.png)

### 磁盘结构



**系统表空间**

**每张表的独立表空间**

![image-20251202210218596](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202210218596.png)



**通用表空间**

**撤销表空间**

**临时表空间**



![image-20251202210302657](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202210302657.png)



**双写缓冲区**

**重做日志**



![image-20251202210549948](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202210549948.png)



### 后台线程

将inno db存储引擎的缓冲池中的数据在合适的时机刷新到磁盘文件当中

![image-20251202210822335](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202210822335.png)







## 事务原理



![image-20251202211914051](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202211914051.png)



**原子性和持久性是由undo log 和 redo log来保证的**

> 原子性-undo log
> 持久性-redo log

**隔离性是由innoDB存储引擎底层的锁机制 和 MVCC (多版本并发控制)来保证的**

> ![image-20251202212208902](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202212208902.png)



### Redo log

刷新脏页数据到磁盘发生错误的时候 用于数据恢复，从而保证数据的持久性

![image-20251202213101724](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202213101724.png)

**WAL机制**：

<u>为什么每次提交的时候要把redo log 刷新到磁盘当中，而不是 不用redo log 、直接将Buffer Pool中变更的数据页自己刷新到磁盘当中</u>

因为一组操作、是随机 无规律地操作的数据页，直接刷新到磁盘当中就会涉及到大量的**随机磁盘IO**，性能较低；而redo log文件在提交时的异步刷新磁盘是顺序磁盘**IO**(因为它是日志文件，日志文件的记录是追加的)，**这就是WAL日志**



### Undo log



![image-20251202214154860](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202214154860.png)

> 物理日志：数据具体什么样
>
> 逻辑日志：每一步的操作是什么







## MVCC



#### **基本概念**





![image-20251202215711321](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202215711321.png)

四个事务隔离级别中，只有在序列化的情况下，快照读才变成当前读，其他三个隔离级别都是 快照读，只不过生成快照的时机不同

> 例如：可重复读的隔离级别下，第一次select的时候生成快照，后面每次读取都是读取这个快照



#### 实现原理



![image-20251202215948703](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251202215948703.png)

> TRX：指Transaction
>
> 



![image-20251203080249950](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203080249950.png)

![image-20251203080307935](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203080307935.png)

![image-20251203080758851](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203080758851.png)

![image-20251203081126804](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203081126804.png)

![image-20251203081201606](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203081201606.png)





**RC级别下的MVCC提取原理**

![image-20251203103642012](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203103642012.png)

[88. 进阶-InnoDB引擎-MVCC-原理分析(RC级别)_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1Kr4y1i7ru?spm_id_from=333.788.player.switch&vd_source=67ef3bb4c8d68a96408acdaa865b1313&p=145)



**RR级别下的MVCC提取原理**

![image-20251203103909742](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203103909742.png)

使用的readview一样、匹配规则一样，查找出来的数据也是一样的，保证了可重复读



![image-20251203104050547](C:\Users\毕哲晖\AppData\Roaming\Typora\typora-user-images\image-20251203104050547.png)







# MySQL管理





























