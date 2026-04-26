

https://lcn41634gsrj.feishu.cn/wiki/WZgpw7R99iXobPkZ19oc8KQZnnh?from=from_copylink



# 1、初始JVM

## 1.1 什么是JVM

JVM 全称是 Java Virtual Machine，中文译名 Java虚拟机。JVM 本质上是一个运行在计算机上的程序，<u>他的职责是运行Java字节码文件</u>。

Java源代码执行流程如下：

![img](1777082344341-1.png)

分为三个步骤：

1、编写Java源代码文件。

2、使用Java编译器（javac命令）将源代码编译成Java字节码文件。

3、使用Java虚拟机加载并运行Java字节码文件，此时会启动一个新的进程。

## 1.2 ==JVM的功能==

- 1 - 解释和运行
- 2 - 内存管理
- 3 - 即时编译

### 1.2.1 解释和运行

**对字节码文件中的指令**，实时的解释成机器码，让计算机执行。

字节码文件中包含了字节码指令，计算器无法直接执行，Java虚拟机会将字节码文件中的字节码指令实时地解释成机器码，机器码是计算机可以运行的指令。

![img](assets/1777081809045-1.png)

### 1.2.2 内存管理

- 自动为对象、方法等分配内存
- 自动的垃圾回收机制，回收不再使用的对象

Java虚拟机会帮助程序员为对象分配内存，同时将不用的对象使用垃圾回收器回收掉，这是对比C和C++这些语言的一个优势。在C/C++语言中，对象的回收需要程序员手动去编写代码完成，如果遗漏了这段删除对象的代码，这个对象就会永远占用内存空间，不会再回收。所以JVM的这个功能降低了程序员编写代码的难度。

### 1.2.3 ==即时编译==

对热点代码进行优化，提升执行效率。即时编译可以说是提升Java程序性能最核心的手段。

#### Java性能低的主要原因和跨平台特性

Java语言如果不做任何的优化，性能其实是不如C和C++语言的。主要原因是：

在程序运行过程中，**Java虚拟机需要将字节码指令实时地解释成计算机能识别的机器码，这个过程在运行时可能会反复地执行，所以效率较低**。

![img](assets/1777081809045-2.png)

C和C++语言在执行过程中，只需要将源代码编译成可执行文件，就包含了计算机能识别的机器码，无需在运行过程中再实时地解释，所以性能较高。

![img](1777083478027-4.png)

Java为什么要选择一条执行效率比较低的方式呢？主要是为了实现跨平台的特性。Java的字节码指令，如果希望在不同平台（操作系统+硬件架构），比如在windows或者linux上运行。可以使用同一份字节码指令，交给windows和linux上的Java虚拟机进行解释，这样就可以获得不同平台上的机器码了。这样就实现了Write Once，Run Anywhere 编写一次，到处运行 的目标。

![img](assets/1777081809045-4.png)

但是C/C++语言，如果要让程序在不同平台上运行，就需要将一份源代码在不同平台上分别进行编译，相对来说比较麻烦。

再回到即时编译，在JDK1.1的版本中就推出了即时编译去优化对应的性能。

![img](assets/1777081809045-5.png)

**虚拟机在运行过程中如果发现某一个方法甚至是循环是热点代码（被非常高频调用），即时编译器会优化这段代码并将优化后的机器码保存在内存中，如果第二次再去执行这段代码。Java虚拟机会将机器码从内存中取出来直接进行调用**。这样节省了一次解释的步骤，同时执行的是优化后的代码，效率较高。

Java通过即时编译器获得了接近C/C++语言的性能，在某些特定的场景下甚至可以实现超越。

## 1.3 常见的JVM

### 1.3.1 Java虚拟机规范

- 《Java虚拟机规范》由Oracle制定，内容主要包含了Java虚拟机在设计和实现时需要遵守的规范，主要包含class字节码文件的定义、类和接口的加载和初始化、指令集等内容。
- 《Java虚拟机规范》是对虚拟机设计的要求，而不是对Java设计的要求，也就是说虚拟机可以运行在其他的语言比如Groovy、Scala生成的class字节码文件之上。
- 官网地址：https://docs.oracle.com/javase/specs/index.html

### 1.3.2 Java虚拟机规范

平时我们最常用的，就是Hotspot虚拟机。

| 名称                       | 作者    | 支持版本                  | 社区活跃度（github star） | 特性                                                         | 适用场景                             |
| -------------------------- | ------- | ------------------------- | ------------------------- | ------------------------------------------------------------ | ------------------------------------ |
| HotSpot (Oracle JDK版)     | Oracle  | 所有版本                  | 高(闭源)                  | 使用最广泛，稳定可靠，社区活跃JIT支持Oracle JDK默认虚拟机    | 默认                                 |
| HotSpot (Open JDK版)       | Oracle  | 所有版本                  | 中(16.1k)                 | 同上开源，Open JDK默认虚拟机                                 | 默认对JDK有二次开发需求              |
| GraalVM                    | Oracle  | 11, 17,19企业版支持8      | 高（18.7k）               | 多语言支持高性能、JIT、AOT支持                               | 微服务、云原生架构需要多语言混合编程 |
| Dragonwell JDK龙井         | Alibaba | 标准版 8,11,17扩展版11,17 | 低(3.9k)                  | 基于OpenJDK的增强高性能、bug修复、安全性提升JWarmup、ElasticHeap、Wisp特性支持 | 电商、物流、金融领域对性能要求比较高 |
| Eclipse OpenJ9 (原 IBM J9) | IBM     | 8,11,17,19,20             | 低(3.1k)                  | 高性能、可扩展JIT、AOT特性支持                               | 微服务、云原生架构                   |

### 1.3.3 HotSpot的发展历程

##### 初出茅庐 - 1999年4月  

源自1997年收购的SmallTalk语言的虚拟机，HotSpot虚拟机初次在JDK中使用。在JDK1.2中作为附加功能存在，

JDK1.3之后作为默认的虚拟机。

##### 野蛮生长 - 2006年12月

JDK 6发布，并在虚拟机层面做了大量的优化，这些优化对后续虚拟机的发展产生了深远的影响。

##### 稳步前进 - 2009-2013

JDK7中首次推出了G1垃圾收集器。收购了Sun公司之后，吸纳了JRockIt虚拟机的一些设计思想，JDK8中引入了JMC等工具，去除了永久代。

##### 百家争鸣 - 2018-2019

JDK11优化了G1垃圾收集器的性能,同时推出了ZGC新一代的垃圾回收器，JDK12推出Shenan-doah垃圾回收器。

##### 拥抱云原生 - 2019-至今

以Hotspot为基础的GraalVM虚拟机诞生，不仅让解决了单体应用中多语言整合的难题，同时也提升了这些语言运行时的效率。极高的性能、极快的启动速度也更适用于当下的云原生架构。

# 2、字节码文件详解

## 2.1 ==Java虚拟机的组成==

Java虚拟机主要分为以下几个组成部分：

![img](assets/1777081809045-6.png)

- 类加载子系统：核心组件类加载器，负责将字节码文件中的内容加载到内存中。
- 运行时数据区：<u>JVM管理的内存</u>，**创建出来的对象、类的信息等等内容都会放在这块区域中**。
- 执行引擎：包含了<u>解释器、即时编译器、垃圾回收器</u>，执行引擎使用解释器将字节码指令解释成机器码，使用即时编译器优化性能，使用垃圾回收器回收不再使用的对象。
- 本地接口：调用<u>本地</u>使用<u>C/C++编译好的方法</u>，本地方法在Java中声明时，都会带上native关键字，如下图所示。

![img](assets/1777081809045-7.png)

## 2.2 字节码文件的组成

### 2.2.1 以正确的姿势打开文件

字节码文件中保存了源代码编译之后的内容，以二进制的方式存储，无法直接用记事本打开阅读。

通过NotePad++使用十六进制插件查看class文件：

![img](assets/1777081809045-8.png)

无法解读出文件里包含的内容，推荐使用 jclasslib工具查看字节码文件。 Github地址： https://github.com/ingokegel/jclasslib

安装方式：找到 资料\工具\jclasslib_win64_6_0_4.exe 安装即可

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTlhZGFmM2FjYjlhZmY4MzZiMDZjMTRiMmEyZDI2ZDlfSFh3T01FWHZQN3d5RHMzOTQxRDlkYUt2VjB0TVdaTnJfVG9rZW46QU5hZWJodDZKb3N6ZTh4RHllZWMzS2JCbktmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)



### 2.2.2 字节码文件的组成

字节码文件总共可以分为以下几个部分：

- **基础信息**：魔数、字节码文件对应的Java版本号、访问标识(public final等等)、父类和接口信息
- **常量池**： 保存了字符串常量、类或接口名、字段名，主要在字节码指令中使用
- **字段：** 当前类或接口声明的字段信息
- **方法：** 当前类或接口声明的方法信息，核心内容为方法的字节码指令
- **属性：** 类的属性，比如源码的文件名、内部类的列表等



#### 2.2.2.1 基本信息

基本信息包含了jclasslib中能看到的两块内容：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTkyMjZmNGY0MjhmYjk0ZjA4MzFiYTFhYjRjMGY0MTdfaVdYcnhGZ05RUGQ0VHN2RDMwQWlOUEFSWm1qNGRJTlNfVG9rZW46U2hHZ2JUS1gzb3VYSTh4Q2lYT2NqS00xblhmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### Magic魔数

**每个Java字节码文件的前四个字节是固定的**，用16进制表示就是0xcafebabe。

> **文件是无法通过文件扩展名来确定文件类型的，文件扩展名可以随意修改不影响文件的内容**。<u>软件会使用文件的头几个字节（文件头）去校验文件的类型</u>，如果软件不支持该种类型就会出错。

比如常见的文件格式校验方式如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODc5NzY2Mzk3NTU4OTc5YTYxODlkMjY4N2Y4NjY1MTZfSHloYlowMzZ6bFhUWFlmczNZTUpKeE9yNTZMTjJ5S21fVG9rZW46TDAyQmJGQXRQbzJ1aEN4YlN2OWNKbW1IbjZkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

Java字节码文件中，将文件头称为magic魔数。Java虚拟机会校验字节码文件的前四个字节是不是0xcafebabe，如果不是，该字节码文件就无法正常使用，Java虚拟机会抛出对应的错误



##### 主副版本号

主副版本号指的是编译字节码文件时使用的JDK版本号，<u>主版本号用来标识大版本号</u>，JDK1.0-1.1使用了45.0-45.3，JDK1.2是46之后每升级一个大版本就加1；<u>副版本号是当主版本号相同时作为区分不同版本的标识，一般只需要关心主版本号</u>。

> 1.2之后大版本号计算方法就是 : 主版本号 – 44，比如主版本号52就是JDK8。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MmE5YzFmN2ZmNzAxMjY4NGM5NzM2M2RmNDcyMjM1YTlfRHdnNEl1RzhGdlpiWk9KNjY2cHp5Y1FmZ3MyY0ljUGNfVG9rZW46VFhVNGJ4b3N2bzN6aG94Zm9mY2NYMURkbkZlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

> **版本号的作用主要是判断当前字节码的版本和运行时的JDK是否兼容**。如果使用较低版本的JDK去运行较高版本JDK的字节码文件，无法使用会显示如下错误：
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MjYwZmRkZWNlMjRlN2MxNTNkNDJlZTI1MGEyYzkwY2JfUzFVYWtGOExUNTNpbEIzUlEybEdrSTFZZzRmWTh2OWJfVG9rZW46QWNIN2JxUlR1b3hYSDJ4cUpzSGNrbW5ObjFlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> (使用JDK6去运行了JDK8的字节码文件)



有两种方案：

1.升级JDK版本，将图中使用的JDK6升级至JDK8即可正常运行，**容易引发其他的兼容性问题，并且需要大量的测试**。

2.<u>将第三方依赖的版本号降低或者更换依赖，以满足JDK版本的要求</u>。建议使用这种方案



##### 其他基础信息

其他基础信息包括访问标识、类和接口索引，如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWY2NWUwNjJmNWU5ODNkZThlMzYyZDNiNzFhNDNkNWNfcXhOWkVPMFhodzh1SlpIZHhwd1hDdWlRMmE3b1cyZFVfVG9rZW46R3BkQ2JFOGhWb3R5Vkx4amNBTWNpSDFBbnNmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)



#### 2.2.2.2 常量池

字节码文件中常量池的作用：**避免相同的内容重复定义，节省空间**。如下图，常量池中定义了一个字符串，字符串的字面量值为123。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWQ2ODg3NmNkNDcxNTEzOTlhNGUwMjY2MmIyYzRiOGJfRlBxTWNzUFhVQlRlZk90RDZaT2F4ZHdHUThYS0g1dGlfVG9rZW46Q3FWemJCM3p1b2tqbHJ4RUNaZ2N4NTNRbmRkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

比如在代码中，编写了两个相同的字符串“我爱北京天安门”，字节码文件甚至将来在内存中使用时其实只需要保存一份，此时就可以将这个字符串以及字符串里边包含的字面量，放入常量池中以达到节省空间的作用。

```Java
String str1 = "我爱北京天安门";
String str2 = "我爱北京天安门";
```

常量池中的数据都有一个编号，编号从1开始。比如“我爱北京天安门”这个字符串，在常量池中的编号就是7。在字段或者字节码指令中通过编号7可以快速的找到这个字符串。

字节码指令中通过编号引用到常量池的过程称之为符号引用。

![image-20260425114130884](image-20260425114130884.png)





#### 2.2.2.3 字段

字段中存放的是当前类或接口声明的字段信息。

如下图中，定义了两个字段a1和a2，这两个字段就会出现在字段这部分内容中。同时还包含**字段的名字、描述符（字段的类型）、访问标识**（public/private static final等）。

![img](image-20260425113952162.png)



#### 2.2.2.4 方法

字节码中的方法区域是存放**字节码指令**的核心位置，字节码指令的内容存放在方法的Code属性中。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTlhZTY5MjM5NzFiZjYwMDZmYmY2ODhlZTIxNmUwYTFfU01kc2k5Rmx3RVlkUThJSmh0QkczbnhMSERQUTBFWGFfVG9rZW46WHRIMWI2MTVobzZ6UVJ4bjRid2NkcmNZbnFlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

通过分析方法的字节码指令，可以清楚地了解一个方法到底是如何执行的。先来看如下案例：

```Java
int i = 0;
int j = i + 1;
```

> 这段代码编译成字节码指令之后是如下内容：
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDcxNmNkMWQ1ZjRkOTAwM2MzOGMzMWYwNjc1MjBjNjlfMGxocHM1TEJvU3cxSm1qVjB3N2R2aFY4SDZiVm10S0tfVG9rZW46Q2ltcmJBbVR1b0ZEMDJ4TFlLWWNSR3Q4blJlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 要理解这段字节码指令是如何执行的，我们需要先理解两块内存区域：操作数栈和局部变量表。
>
> **操作数栈**是用来存放临时数据的内容，是一个栈式的结构，先进后出。
>
> **局部变量表**是存放方法中的局部变量，包含方法的参数、方法中定义的局部变量，在编译期就已经可以确定方法有多少个局部变量。
>
> 1、iconst_0，将常量0放入操作数栈。此时栈上只有0。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGFlMTg3N2NmNjQ4NTBhZWZiMDllYmRlN2I0MjY3N2RfWEs0V0lFcXRIaEdHb2tmaDFTYkZSbW94UVdkUVE2eEFfVG9rZW46QXdhS2J2ZDJkb0ZTTUl4WEk2dWM4c0xObmg3XzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 2、istore_1会从操作数栈中，将栈顶的元素弹出来，此时0会被弹出，放入局部变量表的1号位置。局部变量表中的1号位置，在编译时就已经确定是局部变量i使用的位置。完成了对局部变量i的赋值操作。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTdhMWIwNDYwY2ZiOTRmMGEzNmYyOGUzMjFhYTYwNGZfb0xPS29MUzl4QkRia3A2VDJNZklhM3l0M28xdDdGNVNfVG9rZW46SngxQWI5QVBib1R0VFN4VHZ5OWNIaWQxbjhCXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 3、iload_1将局部变量表1号位置的数据放入操作数栈中，此时栈中会放入0。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWU2MjU5MTE2NjAyY2NhMzM3YjMxMDMxMTNkNDcxYzNfZnZMajVEZ0NYb2Fic3p5MkhEdHM0NHNPU2FGVkZxbnFfVG9rZW46UTlST2JTdDBwb1lBQTR4VWdoQWNkaXVibndkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 4、iconst_1会将常量1放入操作数栈中。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWM2OTFlNzc2OGY0M2VjZTRmOGQwMTZjZjk3ZjY1ODVfUlJoTmVEWWpWMW1mTm9NdjhMc29TYVR0ZU5zQXZXSk9fVG9rZW46V1ljcmJXYjV2b0swYlB4dkFoaGNwUTUwbmhiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 5、iadd会将操作数栈顶部的两个数据相加，现在操作数栈上有两个数0和1，相加之后结果为1放入操作数栈中，此时栈上只有一个数也就是相加的结果1。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzRlZGU5YzMxOTYxZDI1ZWZiOWQxMjI0ZWFlZmFiOGZfQVNITnRxM0Q3NVZZaXhzbUxGMXVVUFZhM0s0R1BVWmdfVG9rZW46Vm1RZmJ3RkdXb0N4enJ4S0ZTRGNMNnBtbmJmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 6、istore_2从操作数栈中将1弹出，并放入局部变量表的2号位置，2号位置是j在使用。完成了对局部变量j的赋值操作。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGViY2ZiYjMxYmUzMzU0NTFiMjdmYTlhYjJkMDQzNTVfeXd4a09UcGtldzVMTVpWUUJQbm5BbVd6NEFsMlZYMHVfVG9rZW46T0VkUGJaQXZUb2NxdjB4dEtaZ2M2ejFqblZlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 7、return语句执行，方法结束并返回。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzNlMGRiYTVlYmRlOTk4ZTgyMzA2YjA3MjIzMzQ1NWNfeXFNZDd1QWJFV09wSXBpUzg2RDBNTXMwYTZtVElXT1BfVG9rZW46VFJIWmJQZ1Ftb2ZaZUx4UXI3TGM4Y2FtbjZkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 同理，同学们可以自行分析下i++和++i的字节码指令执行的步骤。
>
> i++的字节码指令如下，其中iinc 1 by 1指令指的是将局部变量表1号位置增加1，其实就实现了i++的操作。
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzFlMzg0M2M5YmQyOTI3OGQ1ZWJhMDk1MDgyNzgwMDBfSE1ST3V2TXh5NFZlTWFmNkRSYUtUY2hQSktGVU5zWEJfVG9rZW46UE9rbWJ3QWhmb1JnWFR4WXJ0Z2MzOXZjblZoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> 而++i只是对两个字节码指令的顺序进行了更改：
>
> ![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmJmYmI4M2JkMWJmZmVmMTU2MGIyMWJiOTAxOGRlOWZfUEswSUlBcjdUU1dFZ0dIQ0Z4MkhOd0lkeFQ2VEljdTlfVG9rZW46RmFaMGJ6dXFHb3pyNnF4MHBmZmNjNVJubkJoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)
>
> ##### 面试题：
>
> 问：int i = 0; i = i++; 最终i的值是多少？
>
> 答：答案是0，我通过分析字节码指令发现，i++先把0取出来放入临时的操作数栈中，
>
> 接下来对i进行加1，i变成了1，最后再将之前保存的临时值0放入i，最后i就变成了0。
>



#### 2.2.2.5 属性

属性主要指的是类的属性，比如源码的文件名、内部类的列表等。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTBlNzg1M2MzYWMwNTg2ODhkZGMwNzgwODhiYTY5NjNfbFNuSmtyVGJWbE85SEg4aFF2ZWJiSDJlbTVvMjZHQzJfVG9rZW46TlZuNWJLTjRZb0xOdXR4RUFFN2NHTUV0bk5oXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)



### 2.2.3 ==玩转字节码常用工具==

#### 2.2.3.1 javap

javap是JDK自带的反编译工具，可以通过控制台查看字节码文件的内容。适合在服务器上查看字节码文件内容。

直接输入javap查看所有参数。输入`javap -v` 字节码文件名称 查看具体的字节码信息。如果jar包需要先使用 `jar –xvf` 命令解压。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZThlYmRmYTZjZjBmM2UwNTQ4NDBmYzU1ZTg1Yjc2YzRfOGlDaXVzbDRwb3FVUVAwck02SDZnQzZBRTBIS0wycm5fVG9rZW46WUJES2JtUFRCb1J3cTV4WGVFRWN4RlNNbkxiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 2.2.3.2 jclasslib插件

jclasslib也有Idea插件版本，建议开发时使用Idea插件版本，可以在代码编译之后实时看到字节码文件内容。

安装方式：

1、打开idea的插件页面，搜索jclasslib

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmRkODAxOGM4MzQyNWE0YjZiNjg2ODY0ZjYxY2JmZmRfd21uRVVPQmRzTTFFYjQ4YUFZb0lGRzJXRGJPaFdWWnZfVG9rZW46WE5mZ2J2d3B5b1JoSWh4dTJESmN4VXpYbnlkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、选中要查看的源代码文件，选择 视图(View) - Show Bytecode With Jclasslib

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2MzMTE3YWI5NWQxZGNjZjIyNDE1MWZhYzdhMDBjNWFfeDZFanV3V25UNzh0VE5STlNvVUJLYVRETmpuTndKT3RfVG9rZW46RDYxdmJCb2dmb0VhT1h4RTBiTmNTeERnbk9jXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

右侧会展示对应源代码编译后的字节码文件内容：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzAxZDBjNjQyZDFkM2I1MmVmMzE2NWFlYzY2NGNmNzBfTEZjM1pRam5SY25PeHFwQ1hidmhwNExPaTFyYWVaNlNfVG9rZW46QmswWmJJcXBLbzNtQlR4eHhUVmNOSnNCblNjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

> tips:
>
> 1、一定要选择文件再点击视图(view)菜单，否则菜单项不会出现。
>
> 2、文件修改后一定要重新编译之后，再点击刷新按钮。

#### 2.2.3.3 Arthas

Arthas 是一款线上监控诊断产品，通过全局视角实时查看应用 load、内存、gc、线程的状态信息，并能在不修改应用代码的情况下，对业务问题进行诊断，大大提升线上问题排查效率。 官网：https://arthas.aliyun.com/doc/ Arthas的功能列表如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NmE4NTczYmYyMzgxZDhkMmE1MTAxNTFlZWZjOGJhM2VfTm5WcEd1VTM5ZzMwYVZxQUx6OVBTeXNhVDF0RVY1Z01fVG9rZW46SGM0ZGJsMExJb3l2NVp4M3I4UGNPM3FXbjBkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

**安装方法：**

1、将 资料/工具/arthas-boot.jar 文件复制到任意工作目录。

2、使用`java -jar arthas-boot.jar ` 启动程序。

3、输入需要Arthas监控的进程id。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODgxMTY5OWRkNDI0MWI1YTlkMjlhMDFiZmJlYmQ0ZmZfdmxXUkllbzdWRWpJc2lFSGFzbFJmaVFhOW1DaGZmeTlfVG9rZW46QVlVZmJOd0VDb3lZdmV4TWRoeGNCOTVGbkhnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、输入命令即可使用。

**dump**

命令详解：https://arthas.aliyun.com/doc/dump.html

dump命令可以将字节码文件保存到本地，如下将`java.lang.String` 的字节码文件保存到了/tmp/output目录下：

```Bash
$ dump -d /tmp/output java.lang.String

 HASHCODE  CLASSLOADER  LOCATION
 null                   /tmp/output/java/lang/String.class
Affect(row-cnt:1) cost in 138 ms.
```

**jad**

命令详解：https://arthas.aliyun.com/doc/jad.html

jad命令可以将类的字节码文件进行反编译成源代码，用于确认服务器上的字节码文件是否是最新的，如下将demo.MathGame的源代码进行了显示。

```Java
$ jad --source-only demo.MathGame
/*
 * Decompiled with CFR 0_132.
 */
package demo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MathGame {
    private static Random random = new Random();
    public int illegalArgumentCount = 0;
...
```

## 2.3、类的生命周期

类的生命周期描述了一个类加载、使用、卸载的整个过程。整体可以分为：

- 加载
- 连接，其中又分为验证、准备、解析三个子阶段
- 初始化
- 使用
- 卸载

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGZmYTZiNGI1YTk0ZDgxYzg4YjEzOGQ5ZTcxNDg3NWZfTmtINURTU3ppdWhzS1lNSTRaMlk4bHVudjUya1lzckxfVG9rZW46VXg4T2I3UW9Ob2pJc0F4a2hXUmNuR2xwbmFnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 2.3.1 加载阶段

1、加载(Loading)阶段第一步是类加载器根据类的全限定名通过不同的渠道以二进制流的方式获取字节码信息，程序员可以使用Java代码拓展的不同的渠道。

- 从本地磁盘上获取文件
- 运行时通过动态代理生成，比如Spring框架
- Applet技术通过网络获取字节码文件

2、类加载器在加载完类之后，Java虚拟机会将字节码中的信息保存到方法区中，方法区中生成一个InstanceKlass对象，保存类的所有信息，里边还包含实现特定功能比如多态的信息。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGU3NDhmZjk2MDJkMmU0MzYyYjI4ZWIwY2I4YmJkNTNfRGxKaHlrb01MTnhzSWdLUVFsRjBiTmxTd05VYURHZnNfVG9rZW46Qm00RWJlSTR1b3d1N014R1hiUWNtTWhObkt5XzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、Java虚拟机同时会在堆上生成与方法区中数据类似的java.lang.Class对象，作用是在Java代码中去获取类的信息以及存储静态字段的数据（JDK8及之后）。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODlhMTUyMDNhNjgzZjlkMWI5M2M4NGM3MWM3MTI3ZWFfSmpmelREdDV3eTByRHhmMml4ZFZhSGVWT1hiWDB2cEJfVG9rZW46RzRWcWJZVXdQb3M3Q2h4dlhLaGM4OGFnbkRmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 2.3.2 连接阶段

连接阶段分为三个子阶段:

- 验证，验证内容是否满足《Java虚拟机规范》。
- 准备，给静态变量赋初值。
- 解析，将常量池中的符号引用替换成指向内存的直接引用。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTA3M2E2NWJhYzE1MDA4NTM3YWNlMjliNDZhNjc0NmZfQzZ1WmpsbXFQQVVaQWVOaHBlRkZrZ2Jac0RVVnRlNUlfVG9rZW46SXpYWGJIV290b1g3Vmh4UTNnaGMzN2NFbkJmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 验证

验证的主要目的是检测Java字节码文件是否遵守了《Java虚拟机规范》中的约束。这个阶段一般不需要程序员参与。主要包含如下四部分，具体详见《Java虚拟机规范》：

1、文件格式验证，比如文件是否以0xCAFEBABE开头，主次版本号是否满足当前Java虚拟机版本要求。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjkxN2VlMmQxY2ZiMzczNGMyOGEyMjQwYjk5ODExYTNfWTNTNEFJalFRbDNXNTRMeVpBV0pYUUhzTHB3SVUzMU5fVG9rZW46WDlZZGJlSGt5b0VDNVB4TmVrV2NiNEpVblFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、元信息验证，例如类必须有父类（super不能为空）。

3、验证程序执行指令的语义，比如方法内的指令执行中跳转到不正确的位置。

4、符号引用验证，例如是否访问了其他类中private的方法等。

对版本号的验证，在JDK8的源码中如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDA2MGNjODg2MzQ0ZGEwYmI5ODcwYWJiM2I3ZTM5MGJfUG5sNjZQN1pDSk92bmxaMEdOUXVyRHZpeUx5TjhZenVfVG9rZW46VDdTOWJSUVhhb1Nhdk94YlhqWmNHU1BnbnNjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

编译文件的主版本号不能高于运行环境主版本号，如果主版本号相等，副版本号也不能超过。

#### 准备

准备阶段为静态变量（static）分配内存并设置初值，每一种基本数据类型和引用数据类型都有其初值。

| **数据类型**         | **初始值**   |
| -------------------- | ------------ |
| **int**              | **0**        |
| **long**             | **0L**       |
| **short**            | **0**        |
| **char**             | **‘\u0000’** |
| **byte**             | **0**        |
| **boolean**          | **false**    |
| **double**           | **0.0**      |
| **引用****数据类型** | **null**     |

如下代码：

```Java
public class Student{

public static int value = 1;

}
```

在准备阶段会为value分配内存并赋初值为0，在初始化阶段才会将值修改为1。

> final修饰的基本数据类型的静态变量，准备阶段直接会将代码中的值进行赋值。
>
> 如下例子中，变量加上final进行修饰，在准备阶段value值就直接变成1了，因为final修饰的变量后续不会发生值的变更。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzJjMTE3ZTM1YjQwMjU5YTU3OWE3YThmY2EzMDZiZmJfOEY0bVdWTkJQWHJObm1XMkRLbmVqVnR1aFdtYUN3cE9fVG9rZW46Wnpzc2JUWnZtb1hoNWp4WmlpVGMyZlUybm9nXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

来看这个案例：

```Java
public class HsdbDemo {
    public static final int i = 2;
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        HsdbDemo hsdbDemo = new HsdbDemo();
        System.out.println(i);
        System.in.read();
    }
}
```

从字节码文件也可以看到，编译器已经确定了该字段指向了常量池中的常量2：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODFiZjZkNDVhNDM3NmJmMjYxODkxMmU4NzllNjJmZjZfa0dUSGNPS3NOWmpHRTBVWHFuRFVxT2gwekJyaDNyRUdfVG9rZW46U2lKbWI1R0lrb3dYU2p4enVGbWNaZEFqbjdkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 解析

解析阶段主要是将常量池中的符号引用替换为直接引用，符号引用就是在字节码文件中使用编号来访问常量池中的内容。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWIyMGQ3YWI2NmNiOTE0NWQyOWRjY2QxNTI2MWY4MDdfYTd2QklKdzZITXNQWUZCcGM0QXdmbExtUkp4ODdCc1NfVG9rZW46TjZrSGJ2ZnpCb0ZHWU94d3k1cWNRaDdNbnJoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

直接引用不在使用编号，而是使用内存中地址进行访问具体的数据。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWQ5ZjhlYTkyYzA0ZmQ1N2Q0NTNkNzY3ZWYyYjIxNzhfSjMxYWJENjRBNmhzNlRWbmFnbm9BaVlCZWRwUWZpYXBfVG9rZW46UjA4b2JCU0JtbzN2YnN4b3piVmNZTDhpbjNmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 2.3.3 初始化阶段

初始化阶段会执行字节码文件中clinit（class init 类的初始化）方法的字节码指令，包含了静态代码块中的代码，并为静态变量赋值。

如下代码编译成字节码文件之后，会生成三个方法：

```Java
public class Demo1 {

    public static int value = 1;
    static {
        value = 2;
    }
   
    public static void main(String[] args) {

    }
}
```

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MjZjMDBlODI0NWViOWVjMTJlN2ZmZDU3YTI3NmQ5N2ZfSUFpeTh2UjM3Sm5tajdEVEZ5bFhyY3pXMGpHdThweWxfVG9rZW46VTR5UGIxaE5lb0o2aVp4bmx5OWNpQ0tsbmVmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- init方法，会在对象初始化时执行
- main方法，主方法
- clinit方法，类的初始化阶段执行

继续来看clinit方法中的字节码指令：

1、iconst_1，将常量1放入操作数栈。此时栈中只有1这个数。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2I5MzVmYzE1NDRiYTEwMWYzODkwNDU2ZGViNmE0MWVfajBFOGZDVmZVZjlKTVZhaUFqTEdPM0oxOVdVdkFUbWpfVG9rZW46UWE3RmJqUXNxb0J0VnJ4dHoxN2NqekdmbkZiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、putstatic指令会将操作数栈上的数弹出来，并放入堆中静态变量的位置，字节码指令中#2指向了常量池中的静态变量value，在解析阶段会被替换成变量的地址。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MjRjMjkyMWYzNzdlZjk5NDhiYTVkMjlmMzg0NjUxYmFfcDg3V2hEbGNnWVZUWmdjdDg5eWd1ajVLRFZWbG5RMmVfVG9rZW46UjdxdWJIRWpUb3N1U2p4a1pzdmNaS2s3bkhnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、后两步操作类似，执行value=2，将堆上的value赋值为2。

如果将代码的位置互换：

```Java
public class Demo1 {
    static {
        value = 2;
    }
   
    public static int value = 1;
   
    public static void main(String[] args) {

    }
}
```

字节码指令的位置也会发生变化：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjZhYmQwYmFmNGJhZTI4OWQwNWUzMDA3ZmRkN2Y4MDhfQnNCUUZLb2NqUlFEeEtUamxLVVFENzg4WWNpa3JFbWlfVG9rZW46VHVUcGJaUzREb09yZzZ4M1VDVGMwV2hjbmtnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这样初始化结束之后，最终value的值就变成了1而不是2。

以下几种方式会导致类的初始化：

1.访问一个类的静态变量或者静态方法，注意变量是final修饰的并且等号右边是常量不会触发初始化。

2.调用Class.forName(String className)。

3.new一个该类的对象时。

4.执行Main方法的当前类。

添加-XX:+TraceClassLoading 参数可以打印出加载并初始化的类

#### 面试题1：

如下代码的输出结果是什么？

```Java
public class Test1 {
    public static void main(String[] args) {
        System.out.println("A");
        new Test1();
        new Test1();
    }

    public Test1(){
        System.out.println("B");
    }

    {
        System.out.println("C");
    }

    static {
        System.out.println("D");
    }
}
```

分析步骤：

1、执行main方法之前，先执行clinit指令。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjVkMTc4MWRkOTVjYTRlYWQ5OWQ4NjVmY2U4YzZmMzRfMlh0RGRyelFPRkZQb2VyVUlRYlY0bDBIc1RHWEZzSXVfVG9rZW46WWhCRGJXZzBPb0o5UFB4V1h0bWM3eXNQblNkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

指令会输出D

2、执行main方法的字节码指令。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Nzc3YTEzMWZmY2E2MzQxN2E5ZTkxOWUyM2EyOGRlYWRfRktCRURlOERHUXB4Tks1QjNKTTByOG9saUFqRE5Uc0FfVG9rZW46VGVkbWIyWlN1b09mQ1J4cUhxNmNDQlIwblpiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

指令会输出A

3、创建两个对象，会执行两次对象初始化的指令。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDUyMjlhNTc5ODU0NzM0MjllNjEyZDA5OWVlNjMxYzNfbFhNdmswUkpsTFN0R0VJOUN4dUZIUVlJeGRET0Q3RGRfVG9rZW46R2VrS2JaNGZHb1pSV0Z4MVp0WmN5ZkFVbkVmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这里会输出CB，源代码中输出C这行，被放到了对象初始化的一开始来执行。

所以最后的结果应该是DACBCB

#### clinit不会执行的几种情况

如下几种情况是不会进行初始化指令执行的：

1.无静态代码块且无静态变量赋值语句。

2.有静态变量的声明，但是没有赋值语句。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTQ0NjI1YzdhYjJlMDI2Yzg5ZjVhZTU2MDRiYWU3NWRfUFVDRWJKSFF1bWc2bVFjeFBScENjVjJKTHcxSVBYejBfVG9rZW46UXNjNWJSeWVtb2tOUUN4MFN2UmN4RFh6bmNjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3.静态变量的定义使用final关键字，这类变量会在准备阶段直接进行初始化。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2ZlNjc3ZDFkYTM3OGQ4OTIyODI5YmFlMTk3NGM5MTNfYWZpWDg5OXl3WlhUSXg2UUE1U1lBNXFIdExMU2pVMGtfVG9rZW46R2FkS2JPUXRVb2RHQmh4aTA0VGNQeWNlblNlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 面试题2：

如下代码的输出结果是什么？

```Java
public class Demo01 {
    public static void main(String[] args) {
        new B02();
        System.out.println(B02.a);
    }
}

class A02{
    static int a = 0;
    static {
        a = 1;
    }
}

class B02 extends A02{
    static {
        a = 2;
    }
}
```

分析步骤：

1、调用new创建对象，需要初始化B02，优先初始化父类。

2、执行A02的初始化代码，将a赋值为1。

3、B02初始化，将a赋值为2。

##### 变化

将`new B02();`注释掉会怎么样？

分析步骤：

1、访问父类的静态变量，只初始化父类。

2、执行A02的初始化代码，将a赋值为1。

#### 补充练习题

分析如下代码执行结果:

```Java
public class Test2 {
    public static void main(String[] args) {
        Test2_A[] arr = new Test2_A[10];

    }
}

class Test2_A {
    static {
        System.out.println("Test2 A的静态代码块运行");
    }
}
```

数组的创建不会导致数组中元素的类进行初始化。

```Java
public class Test4 {
    public static void main(String[] args) {
        System.out.println(Test4_A.a);
    }
}

class Test4_A {
    public static final int a = Integer.valueOf(1);

    static {
        System.out.println("Test3 A的静态代码块运行");
    }
}
```

final修饰的变量如果赋值的内容需要执行指令才能得出结果，会执行clinit方法进行初始化。

## 2.4、类加载器

### 2.4.1 什么是类加载器

类加载器（ClassLoader）是Java虚拟机提供给应用程序去实现获取类和接口字节码数据的技术，类加载器只参与加载过程中的字节码获取并加载到内存这一部分。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWZiMDk3N2VlZGExNjkzZWMyNDkwZDM2Y2Y5YjU4Y2RfYnNDNjA1VTdZUnZrNXBnd2dvaDJqSTRxTm5SV0JJS2xfVG9rZW46SGFjbWJrV21lb1planF4NkM2dWNGYVhobkJjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

类加载器会通过二进制流的方式获取到字节码文件的内容，接下来将获取到的数据交给Java虚拟机，虚拟机会在方法区和堆上生成对应的对象保存字节码信息。

### 2.4.2 类加载器的分类

类加载器分为两类，一类是Java代码中实现的，一类是Java虚拟机底层源码实现的。

- 虚拟机底层实现：源代码位于Java虚拟机的源码中，实现语言与虚拟机底层语言一致，比如Hotspot使用C++。主要目的是保证Java程序运行中基础类被正确地加载，比如java.lang.String，Java虚拟机需要确保其可靠性。
- JDK中默认提供或者自定义：JDK中默认提供了多种处理不同渠道的类加载器，程序员也可以自己根据需求定制，使用Java语言。所有Java中实现的类加载器都需要继承ClassLoader这个抽象类。

类加载器的设计JDK8和8之后的版本差别较大，首先来看JDK8及之前的版本，这些版本中默认的类加载器有如下几种：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MWRiZTJjZWY1MmJlMTQxM2UzZjhlMDU4YTk3M2FjNTVfOW9sVGs4S2dYejRkZndUUDN5MGFzOGJNYk1FZ3dyQXlfVG9rZW46SWt5V2JEZmpXb3lkMGZ4TmR2amN5NmRpbkNiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

类加载器的详细信息可以通过Arthas的classloader命令查看：

> `classloader` - 查看 classloader 的继承树，urls，类加载信息，使用 classloader 去 getResource

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGU1MDBkYzlmOTUyODNmM2E5NTM1NGU2ODRjOTI3YjVfYTVpS0lnTmVyNmtUYmw3dGJLemNnOHU2MXZtVnRQckNfVG9rZW46WDNmVGJVMGJsb3JOQkd4YjJXMWNIbkM3blpqXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- BootstrapClassLoader是启动类加载器，numberOfInstances是类加载器的数量只有1个，loadedCountTotal是加载类的数量1861个。
- ExtClassLoader是扩展类加载器
- AppClassLoader是应用程序类加载器

### 2.4.3 启动类加载器

- 启动类加载器（Bootstrap ClassLoader）是由Hotspot虚拟机提供的、使用C++编写的类加载器。
- 默认加载Java安装目录/jre/lib下的类文件，比如rt.jar，tools.jar，resources.jar等。

运行如下代码：

```Java
/**
 * 启动程序类加载器案例
 */
public class BootstrapClassLoaderDemo {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = String.class.getClassLoader();
        System.out.println(classLoader);

        System.in.read();
    }
}
```

这段代码通过String类获取到它的类加载器并且打印，结果是`null`。这是因为启动类加载器在JDK8中是由C++语言来编写的，在Java代码中去获取既不适合也不安全，所以才返回`null`

在Arthas中可以通过`sc -d 类名`的方式查看加载这个类的类加载器详细的信息，比如：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzVjOGRiZWNmNTE5MDdlNDlkNTc2MzNkNDdkMTI0Y2VfU1NuaUNESlVZWkZ4ekdOYzRsNEk0b3hBNXhCcmhxb1ZfVG9rZW46RnQ4SWJUVXE2b0VMQ2d4c1hFZmM4V3NtbjBmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

通过上图可以看到，java.lang.String类的类加载器是空的，Hash值也是null。

#### 用户扩展基础jar包

如果用户想扩展一些比较基础的jar包，让启动类加载器加载，有两种途径：

- **放入jre/lib下进行扩展**。不推荐，尽可能不要去更改JDK安装目录中的内容，会出现即时放进去由于文件名不匹配的问题也不会正常地被加载。
- **使用参数进行扩展。**推荐，使用-Xbootclasspath/a:jar包目录/jar包名 进行扩展，参数中的/a代表新增。

如下图，在IDEA配置中添加虚拟机参数，就可以加载`D:/jvm/jar/classloader-test.jar`这个jar包了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGYzMTZmZDBhZjcwNmZjOGE2MTM1NGRlZjg5ZWJhYTBfekdraFROOEJyVmpod1R0Umh1WjQ4SWhNYVNCNnpIdFFfVG9rZW46TkFQVmJ5bU1Ub1NCSXN4V0dRRmNMQlBwbnRjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 2.4.4 扩展类加载器和应用程序类加载器

- 扩展类加载器和应用程序类加载器都是JDK中提供的、使用Java编写的类加载器。
- 它们的源码都位于sun.misc.Launcher中，是一个静态内部类。继承自URLClassLoader。具备通过目录或者指定jar包将字节码文件加载到内存中。

继承关系图如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MWNlYjFlNmVjNzI3Y2VlMTNiNTkxODM1ZWFkMDNmOTZfc1l6dFc5aGRabkh3S2JzRG1GOFZneHhidkJCMVVoa2pfVG9rZW46Q3BKVmJOVTdXb295QUF4ZmJoaGM0ZlRIblRlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- ClassLoader类定义了具体的行为模式，简单来说就是先从本地或者网络获得字节码信息，然后调用虚拟机底层的方法创建方法区和堆上的对象。这样的好处就是让子类只需要去实现如何获取字节码信息这部分代码。
- SecureClassLoader提供了证书机制，提升了安全性。
- URLClassLoader提供了根据URL获取目录下或者指定jar包进行加载，获取字节码的数据。
- 扩展类加载器和应用程序类加载器继承自URLClassLoader，获得了上述的三种能力。

#### 扩展类加载器

扩展类加载器（Extension Class Loader）是JDK中提供的、使用Java编写的类加载器。默认加载Java安装目录/jre/lib/ext下的类文件。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTE4ZDRhYmE2YzZjZDVkZWUyNDNiNTU1Mjk5OTgwM2ZfZjJHN1pwbUFmQk5oWEl6NFNrdnNtNmg3UnFzdGU2RG5fVG9rZW46VkxaUmJROXVib0NrM2h4OVdZdGNWbWZJbnJoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如下代码会打印ScriptEnvironment类的类加载器。ScriptEnvironment是nashorn框架中用来运行javascript语言代码的环境类，他位于nashorn.jar包中被扩展类加载器加载

```Java
/**
 * 扩展类加载器
 */
public class ExtClassLoaderDemo {
    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = ScriptEnvironment.class.getClassLoader();
        System.out.println(classLoader);
    }
}
```

打印结果如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NmZhZTg5OTRiNDAzNjQzZTk5ZjNhMjdjMzE0MmIxMDJfZ0tIU3hkYTFoaEVLY0hUSEk5b2FVNnZzZ09TUklhTG9fVG9rZW46Qm5aeGJDU2Jqb3hlZkV4bzBhaWNtN0dPblBGXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

通过扩展类加载器去加载用户jar包：

- **放入/jre/lib/ext下进行扩展**。不推荐，尽可能不要去更改JDK安装目录中的内容。
- **使用参数进行扩展使用参数进行扩展**。推荐，使用-Djava.ext.dirs=jar包目录 进行扩展,这种方式会覆盖掉原始目录，可以用;(windows):(macos/linux)追加上原始目录

如下图中：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTY4M2FmYjQ3NmNjNTA5NTMyZjBlZjEzMGEzMDQxNGFfM1luOGx1andPYXVhdTd6UWpyQTZNMHZFOGpwU1NsbXZfVG9rZW46RkNqT2JTOHZqb0hwd1d4TGk0bGNJd2ZWbmRjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

使用`引号`将整个地址包裹起来，这样路径中即便是有空格也不需要额外处理。路径中要包含原来ext文件夹，同时在最后加上扩展的路径。

####  应用程序加载器

应用程序类加载器会加载classpath下的类文件，默认加载的是项目中的类以及通过maven引入的第三方jar包中的类。

如下案例中，打印出`Student`和`FileUtils`的类加载器：

```Java
/**
 * 应用程序类加载器案例
 */
public class AppClassLoaderDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        //当前项目中创建的Student类
        Student student = new Student();
        ClassLoader classLoader = Student.class.getClassLoader();
        System.out.println(classLoader);

        //maven依赖中包含的类
        ClassLoader classLoader1 = FileUtils.class.getClassLoader();
        System.out.println(classLoader1);

        Thread.sleep(1000);
        System.in.read();

    }
}
```

输出结果如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWY4ZDgyMTY5MzllYzQ1NDRmM2UyMDViMjIxMGYyZmFfckFjUWk2WTNWWmp4R3JtU3FoaXltTk1OY0lVOURZZW5fVG9rZW46TjZGVmI5S1Bsb0V0WGl4aXZ3UGNrOGNCbklmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这两个类均由应用程序类加载器加载。

类加载器的加载路径可以通过classloader –c hash值 查看：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGMyOTEzNjEwMDQyOTE2MGUzOGI3OTM2OGUyOTU3ODlfSUdPbVFuVTJibDN4ODJoQUhCYnFaYkhHNjZTcVREWklfVG9rZW46RDc1dGI5SUtrb0ViTUd4UTlwQ2NEbVU4blpnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

## 2.5、双亲委派机制

双亲委派机制指的是：当一个类加载器接收到加载类的任务时，会自底向上查找是否加载过，

再由顶向下进行加载。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGMwYWI2ZmViNTJmNDM5OGYwYzRmN2ViMzQzNjVhODJfSlRtaVR6emMwOEhNcUcyUVJBeXl2bW5za3BmYVVpSVNfVG9rZW46WHBxZWJBT1lHb3FWemp4TkkwamNOTHh6bmFiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

详细流程：

每个类加载器都有一个父类加载器。父类加载器的关系如下，启动类加载器没有父类加载器：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTliNzEyMGQ5YWYxMmExNmYwZjc1ZDVkZmEwM2U4M2NfUXU0U1R2RnhXYjA0U1l5bnU1Vlc5SWlWcUluRTEzZ0dfVG9rZW46RTloVmJBN3Rpb1hkSTJ4bGs3eGNWSG9rbm1oXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

在类加载的过程中，每个类加载器都会先检查是否已经加载了该类，如果已经加载则直接返回，否则会将加载请求委派给父类加载器。

#### 案例1：

比如com.itheima.my.A假设在启动类加载器的加载目录中，而应用程序类加载器接到了加载类的任务。

1、应用程序类加载器首先判断自己加载过没有，没有加载过就交给父类加载器 - 扩展类加载器。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWZiYWQzMWJhZWFlYWJjYWM0ZTRhNzM0YjkzYzI4ZDBfNm9YRXFoaVd3NXh1a09tak5zNzl1SkdhVzcydnFaUkxfVG9rZW46WHNJWGJlNWx5b3ZvM2R4RnFSdGNqQXVEbkdsXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、扩展类加载器也没加载过，交给他的父类加载器 - 启动类加载器。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWJlNTUyY2M2ZjUwNWNlNGU1YWQyYWEzOTVmNzgzNWRfcWk0bnM4ME9OMGlvVXBvNkRkV1k4WXFIS1FzRndZY1FfVG9rZW46RWJjR2JySHpub050UEp4WXdNTWN0a0x6bldkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、启动类加载器发现已经加载过，直接返回。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjA1ZWNjMWZjNTFkOTQxOGJmYmQ3NjM3OGQ5ODZlMmNfYVVsOEk0S1VRUmZXSkY1cTdBWk9uNnNxSGdUWWp1TmpfVG9rZW46UllhTGJsUnFCb3EyZzR4M0RrM2NiQVlwbkFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 案例2：

B类在扩展类加载器加载路径中，同样应用程序类加载器接到了加载任务，按照案例1中的方式一层一层向上查找，发现都没有加载过。那么启动类加载器会首先尝试加载。它发现这类不在它的加载目录中，向下传递给扩展类加载器。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzM2MmU4ZGEyNWYwNjkzYjVhZGU5NzY1ZmZlZWI3N2Ffa0d4WG1sNjM1UDdRMjRXOTF1dkRWNUdLWmRMaGF4MmFfVG9rZW46SWE5WmIwYXVqb3dXZ294TjV0cmNMeGpmbk1kXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

扩展类加载器发现这个类在它加载路径中，加载成功并返回。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWY0NDRmYjUyNDM2ODBkNWY0ZTU2MWVmMTE5YzQzYTFfb3pHN3R5ZGdxWGR4eUpoRGZ5UHBseVJ0V2dxdFF6ZEtfVG9rZW46V2Y1aGJLNllXb3dZdkJ4dlNabWNHd0ZqbnJmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果第二次再接收到加载任务，同样地向上查找。扩展类加载器发现已经加载过，就可以返回了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTY5ZmI5NTc2MTliZjNjODE0ZjdmZTU2MjM5Nzc2MTJfNkVlWnlrM1d5WVF2cjVVaWFueDNjbjlxQlNwcW5UZ0xfVG9rZW46SmFLV2JDMXFsb0M5Mll4WEVRZWN3NFEwbkRjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 双亲委派机制的作用

1.保证类加载的安全性。通过双亲委派机制避免恶意代码替换JDK中的核心类库，比如java.lang.String，确保核心类库的完整性和安全性。

2.避免重复加载。双亲委派机制可以避免同一个类被多次加载。

#### 如何指定加载类的类加载器？

在Java中如何使用代码的方式去主动加载一个类呢？

方式1：使用Class.forName方法，使用当前类的类加载器去加载指定的类。

方式2：获取到类加载器，通过类加载器的loadClass方法指定某个类加载器加载。

例如：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWZlZDgzOWQ5MzM0NWRlODg2NmEzNzM5YzM3M2M0NTJfeTlFQnhkeXl5VU1zVnRqUDBuRDlYV0pkRXdzZTNvc0NfVG9rZW46SFQ2ZWJVempLbzBMTzN4aVlhZGM0UnVkbm5mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 三个面试题

1、如果一个类重复出现在三个类加载器的加载位置，应该由谁来加载？

启动类加载器加载，根据双亲委派机制，它的优先级是最高的

2、String类能覆盖吗，在自己的项目中去创建一个java.lang.String类，会被加载吗？

不能，会返回启动类加载器加载在rt.jar包中的String类。

3、**类的双亲委派机制是什么？**

- 当一个类加载器去加载某个类的时候，会自底向上查找是否加载过，如果加载过就直接返回，如果一直到最顶层的类加载器都没有加载，再由顶向下进行加载。
- 应用程序类加载器的父类加载器是扩展类加载器，扩展类加载器的父类加载器是启动类加载器。
- 双亲委派机制的好处有两点：第一是避免恶意代码替换JDK中的核心类库，比如java.lang.String，确保核心类库的完整性和安全性。第二是避免一个类重复地被加载。

## 2.6、打破双亲委派机制

打破双亲委派机制历史上有三种方式，但本质上只有第一种算是真正的打破了双亲委派机制：

- 自定义类加载器并且重写loadClass方法。Tomcat通过这种方式实现应用之间类隔离，《面试篇》中分享它的做法。
- 线程上下文类加载器。利用上下文类加载器加载类，比如JDBC和JNDI等。
- Osgi框架的类加载器。历史上Osgi框架实现了一套新的类加载器机制，允许同级之间委托进行类的加载，目前很少使用。

#### 自定义类加载器

一个Tomcat程序中是可以运行多个Web应用的，如果这两个应用中出现了相同限定名的类，比如Servlet类，Tomcat要保证这两个类都能加载并且它们应该是不同的类。如果不打破双亲委派机制，当应用类加载器加载Web应用1中的MyServlet之后，Web应用2中相同限定名的MyServlet类就无法被加载了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmNiODJiMzVjM2QxZTVlZTMyNGU3NDlhMWZiNjk4MjZfRGQxVmlMT3h5QzR2bnJ4dzlsaVlrMUd3U2hLSjhBTjJfVG9rZW46SzlFamJNQzd4b1d6bEx4bEhtSGNCYWdrbldlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

Tomcat使用了自定义类加载器来实现应用之间类的隔离。 每一个应用会有一个独立的类加载器加载对应的类。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MjNjODFlNWUzY2IwMGU5YmMxZTBiNTdlOTAyOTJlZWFfUXNhZkpMY01aQ1Z6Wkk5b0FDYXkyMWNDR1JwMnRIcXlfVG9rZW46RzdUamJiYVV0b2diR0V4dlZ0bGNMR3c4bkRWXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

那么自定义加载器是如何能做到的呢？首先我们需要先了解，双亲委派机制的代码到底在哪里，接下来只需要把这段代码消除即可。

ClassLoader中包含了4个核心方法，双亲委派机制的核心代码就位于loadClass方法中。

```Java
public Class<?> loadClass(String name)
类加载的入口，提供了双亲委派机制。内部会调用findClass   重要

protected Class<?> findClass(String name)
由类加载器子类实现,获取二进制数据调用defineClass ，比如URLClassLoader会根据文件路径去获取类文件中的二进制数据。重要

protected final Class<?> defineClass(String name, byte[] b, int off, int len)
做一些类名的校验，然后调用虚拟机底层的方法将字节码信息加载到虚拟机内存中

protected final void resolveClass(Class<?> c)
执行类生命周期中的连接阶段
```

1、入口方法：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGNkMWM5MTM3YzEzMDRjNGJjNTE4NmRkY2ZmY2U4OTRfV2dsRDNjNlJxVEZDTUtQWmRYYmZobEpYZ0xDREZSUEdfVG9rZW46THRNVmIzMEJzbzdOZDZ4NHF4UWNtd3ZLbkxQXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、再进入看下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NmNmOTllYTA5ZDNlMDE0NTNmNjAwY2NjYTg4NWRhNDVfZlk0clFDVnE0bVRkTEZ4Zm9BU0taSGhJWmRPQkJYeU9fVG9rZW46RWpiN2JySmx3b0VzU2Z4V2NYS2N4S0RNbk1lXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果查找都失败，进入加载阶段，首先会由启动类加载器加载，这段代码在`findBootstrapClassOrNull`中。如果失败会抛出异常，接下来执行下面这段代码：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWQ0ZDM5Y2U0MTAwZTNjN2RiY2IzMDU5MjY0YmIxNTFfeDNQcVBPSFIxN3dob0tOYkFHcjhWWTZNcXZabzc5d2lfVG9rZW46R0dZU2JDSVBKb3NTV3R4a3pDd2N6TjNGbnJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

父类加载器加载失败就会抛出异常，回到子类加载器的这段代码，这样就实现了加载并向下传递。

3、最后根据传入的参数判断是否进入连接阶段：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGM4MzJiNzk1MzJjM2VkYWQ0MjU1NDU1ZTBiYThmZDBfVzd1bEYwVDZiYUtPdnpTVWFiQ1U4UncxMlJyekJubkZfVG9rZW46SnA3VWJMU1dhb3I2MEx4ZDd1UGNtZkdPbll0XzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

接下来实现打破双亲委派机制：

```Java
package classloader.broken;//package com.itheima.jvm.chapter02.classloader.broken;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.ProtectionDomain;
import java.util.regex.Matcher;

/**
 * 打破双亲委派机制 - 自定义类加载器
 */

public class BreakClassLoader1 extends ClassLoader {

    private String basePath;
    private final static String FILE_EXT = ".class";

    //设置加载目录
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    //使用commons io 从指定目录下加载文件
    private byte[] loadClassData(String name)  {
        try {
            String tempName = name.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            FileInputStream fis = new FileInputStream(basePath + tempName + FILE_EXT);
            try {
                return IOUtils.toByteArray(fis);
            } finally {
                IOUtils.closeQuietly(fis);
            }

        } catch (Exception e) {
            System.out.println("自定义类加载器加载失败，错误原因：" + e.getMessage());
            return null;
        }
    }

    //重写loadClass方法
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //如果是java包下，还是走双亲委派机制
        if(name.startsWith("java.")){
            return super.loadClass(name);
        }
        //从磁盘中指定目录下加载
        byte[] data = loadClassData(name);
        //调用虚拟机底层方法，方法区和堆区创建对象
        return defineClass(name, data, 0, data.length);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //第一个自定义类加载器对象
        BreakClassLoader1 classLoader1 = new BreakClassLoader1();
        classLoader1.setBasePath("D:\\lib\\");

        Class<?> clazz1 = classLoader1.loadClass("com.itheima.my.A");
         //第二个自定义类加载器对象
        BreakClassLoader1 classLoader2 = new BreakClassLoader1();
        classLoader2.setBasePath("D:\\lib\\");

        Class<?> clazz2 = classLoader2.loadClass("com.itheima.my.A");

        System.out.println(clazz1 == clazz2);

        Thread.currentThread().setContextClassLoader(classLoader1);

        System.out.println(Thread.currentThread().getContextClassLoader());

        System.in.read();
     }
}
```

##### 自定义类加载器父类怎么是AppClassLoader呢？

默认情况下自定义类加载器的父类加载器是应用程序类加载器：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmFiODA3ODljZGJhMDM5ODAzNWMzM2M0OGFkNDMwN2VfT0RYMVFuejhiUUJESm1vaE1Za3lhU2RvTWdPa3pRZjlfVG9rZW46SGNDRWJ6bnBCb1VKNDV4QmVFY2NyRUQzbm1mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

以Jdk8为例，ClassLoader类中提供了构造方法设置parent的内容：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjZkMWRhMWM1NThmYTA2YWI1MWFlMmJhOTA1Nzk1NDlfdUJ6MVV2a05zcGpyZ21ONDV3OURaZU5acmloZ1BVOTdfVG9rZW46UnloRGIwZ3Vxb3RXM0F4ejVLa2NmZGlsbjVjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这个构造方法由另外一个构造方法调用，其中父类加载器由getSystemClassLoader方法设置，该方法返回的是AppClassLoader。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDYxZTE1MThjYjVmNzJmZGVmNTY0MWVkMWNhN2RkOTVfNUxvUEk1Z21XZTdxdUdEcGo2Qks1TEprNldIZTNzN3ZfVG9rZW46SU9ZYmJldTdtb0NGV2V4b2ZZb2NCZUkybnljXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### 两个自定义类加载器加载相同限定名的类，不会冲突吗？

不会冲突，在同一个Java虚拟机中，只有相同类加载器+相同的类限定名才会被认为是同一个类。

在Arthas中使用sc –d 类名的方式查看具体的情况。

如下代码：

```Java
 public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //第一个自定义类加载器对象
        BreakClassLoader1 classLoader1 = new BreakClassLoader1();
        classLoader1.setBasePath("D:\\lib\\");

        Class<?> clazz1 = classLoader1.loadClass("com.itheima.my.A");
         //第二个自定义类加载器对象
        BreakClassLoader1 classLoader2 = new BreakClassLoader1();
        classLoader2.setBasePath("D:\\lib\\");

        Class<?> clazz2 = classLoader2.loadClass("com.itheima.my.A");

        System.out.println(clazz1 == clazz2);
     }
```

打印的应该是false，因为两个类加载器不同，尽管加载的是同一个类名，最终Class对象也不是相同的。

通过Arthas看：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjY3ZGU0MjA5MWVlOGQ4MTU0MjJhY2RiZWM2NGVjOGJfUERYaGN5S01WMGUyS1FvbTdXWnB1R2puOGhReGs0aUFfVG9rZW46RkZJYmJvdmJibzg4Zzl4WXM4ZmN2YnlEblJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

也会出现两个不同的A类。

#### 线程上下文类加载器

利用上下文类加载器加载类，比如JDBC和JNDI等。

我们来看下JDBC的案例：

1、JDBC中使用了DriverManager来管理项目中引入的不同数据库的驱动，比如mysql驱动、oracle驱动。

```Java
package classloader.broken;//package com.itheima.jvm.chapter02.classloader.broken;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * 打破双亲委派机制 - JDBC案例
 */

public class JDBCExample {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql:///bank1";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, account_name FROM account_info";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 4: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                String name = rs.getString("account_name");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + name + "\n");
            }
            //STEP 5: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main
}//end FirstExample
```

2、DriverManager类位于rt.jar包中，由启动类加载器加载。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTlmZDJlYzY3ZjE1MjM2ZDI5ZmExODdiODZmYWJjZmNfb0lqTjBzQUxNdFpBWE4yelJLT0FLNE9PYmx1bEpRa2NfVG9rZW46Q1JSUGI1VW1Fb2ZneGx4bWo0TWNzQ3JzbmtnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、依赖中的mysql驱动对应的类，由应用程序类加载器来加载。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MWM0N2JmYzlmM2IzYzM3NDVhYTRhNWI5YWIzNTIyYTBfVTN1WTl2SHJYOXE1c0xFYXBSYnpZRkc4bkxiaTlYa1pfVG9rZW46UWpMY2J1RVQyb2Z0ajB4M0E2cGMySVhtbmJlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

在类中有初始化代码：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGUyMTEyOTg5MjViMjI1NWVhMTRmYjkzM2RhYWM5NWJfWTFxSEREZzNzMDBNYlk3dVNSU2pvb2RTR2lvVmhHNzdfVG9rZW46V2VYb2I2TGFkb09ycE54WVptU2N4eDVubk5oXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

DriverManager属于rt.jar是启动类加载器加载的。而用户jar包中的驱动需要由应用类加载器加载，*这就违反了双亲委派机制*。（这点存疑，一会儿再讨论）

那么问题来了，DriverManager怎么知道jar包中要加载的驱动在哪儿？

1、在类的初始化代码中有这么一个方法`LoadInitialDrivers`：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTg5NzhiNWJiMjg1YTQ4YjI5NDdjOTAxZjg5N2M0ZmJfS00xa3c4Y25VaGhiRmIxclhXbzZQcmFObmNYWHI0VWxfVG9rZW46Q0I4ZmIxUzBrb3A5aEl4VjVXOWNuTFRxbmpjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、这里使用了SPI机制，去加载所有jar包中实现了Driver接口的实现类。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzkxYjU4YzJiYzdlMmEzNzg2ODQzZWRlOWE2NDdiN2NfZmJxcEQyMGYyNHNTWW5MV3ZVallVTXBiUzhrd1J1YmhfVG9rZW46R2hxc2IzQ3h3b2NjMlF4V3owZWNNQW9xblRkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、SPI机制就是在这个位置下存放了一个文件，文件名是接口名，文件里包含了实现类的类名。这样SPI机制就可以找到实现类了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWEwZDhmMTE1MjIwMDliYTNiMWZlM2JhMGY5NjRkYzZfMFNwY0plSzRHR3ZyM05IQVdLN0RJM1ZkOE05b1VNbW5fVG9rZW46S0c1UWJNaTh3b1pVM3N4NEZyY2N2dW1kblFjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjJiODVlMDZkNjc1NDRkZDQwY2NmYzU2MjhkNjk3OTdfb2tEQTlITGdaVzdGcEplRVZncmlwWFNaSVJDejEwMlRfVG9rZW46Tkx3MGJxZFdIb0RIdkx4NHdUTWNjejg4bk1mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、SPI中利用了线程上下文类加载器（应用程序类加载器）去加载类并创建对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWVkZjViNTFiMWEwZWVkZjc2NzkzMGM3MjgyOGM2YTVfd0NrYW16d0NpeDRSaXFOZzVnanBNbDBFdnltTk80VmRfVG9rZW46WUU5d2JwYVY5b0phRVl4U3BSSGNDSldqbmFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

总结：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzUxMThkNWZhMTNkZDI2ZjM5NTBmMWY5NmY0ZDU0ZWJfVXhuazhVRERGa3BSdzZyczhhdHl3UEJTYTAyMVE3S0hfVG9rZW46STNGZmJLQUhOb0VTZ2F4Y0YzeWNMaG5ublNnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### JDBC案例中真的打破了双亲委派机制吗？

最早这个论点提出是在周志明《深入理解Java虚拟机》中，他认为打破了双亲委派机制，这种由启动类加载器加载的类，委派应用程序类加载器去加载类的方式，所以打破了双亲委派机制。

但是如果我们分别从DriverManager以及驱动类的加载流程上分析，JDBC只是在DriverManager加载完之后，通过初始化阶段触发了驱动类的加载，类的加载依然遵循双亲委派机制。

所以我认为这里没有打破双亲委派机制，只是用一种巧妙的方法让启动类加载器加载的类，去引发的其他类的加载。

#### Osgi框架的类加载器

历史上，OSGi模块化框架。它存在同级之间的类加载器的委托加载。OSGi还使用类加载器实现了热部署的功能。热部署指的是在服务不停止的情况下，动态地更新字节码文件到内存中。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzYzN2FiYWMxYTg2YTAwNjQyNDZkZTE2MjI5MTJmYmZfR3RCYzJURnQ3T2l2c1Rra3VxWG9pbVFPNXBFWThwcjFfVG9rZW46SUkxZWJUbmhIb2g5VWh4Zk81aGNBNjk4bkhlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

由于这种机制使用已经不多，所以不再过多讨论OSGi，着重来看下热部署在实际项目中的应用。

#### 案例：使用阿里arthas不停机解决线上问题

**背景：**

小李的团队将代码上线之后，发现存在一个小bug，但是用户急着使用，如果重新打包再发布需要一个多小时的时间，所以希望能使用arthas尽快的将这个问题修复。

**思路：**

1. 在出问题的服务器上部署一个 arthas，并启动。
2. jad --source-only 类全限定名 > 目录/文件名.java      jad 命令反编译，然后可以用其它编译器，比如 vim 来修改源码
3. mc –c 类加载器的hashcode 目录/文件名.java -d 输出目录

​      mc 命令用来编译修改过的代码

1.  retransform class文件所在目录/xxx.class

​      用 retransform 命令加载新的字节码

**详细流程：**

1、这段代码编写有误，在枚举中的类型判断上使用了`==` 而不是`equals`。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGJjNzIzYjk5ZTMxODhhYjczMTRmZGM3NTlhM2ViMmFfN2JYeW0yQjZLSHJ4WDVIa3lYZlN6SXlJMFdyMndwSTJfVG9rZW46UlliRmJWWm5ib2YxdUh4azJtRGNQdUsyblljXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、枚举中是这样定义的，1001是普通用户，1002是VIP用户：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2M0YjE5Y2FhMDk3YWNkNWUwN2NjNDM3OGZhMWIwZmZfcGZDc1JMT0owUlJaY0pRdHZMRUxuSW1NVzBsTzllMzRfVG9rZW46SkFZNGIwZWNpb09TbE54THVwamNBbHZobkpmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、由于代码有误，导致传递1001参数时，返回的是收费用户的内容。

![img](assets/1777081809048-97.png)

4、`jad --source-only 类全限定名 > 目录/文件名.java` 使用 jad 命令反编译，然后可以用其它编译器，比如 vim 来修改源码

![img](assets/1777081809048-98.png)

这里直接双击文件使用finalShell编辑：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDhmNDczYWExYWVjMTFlODU5Njk3ZDEwNzg3Y2FiMThfaEdNcGJWaEV2NXlKbWFqNE1OWXNhSzdmalpCZmd3YldfVG9rZW46SEdJVmJSWHVKb252VUJ4MzZLc2NGMXlYbjJiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

5、`mc –c 类加载器的hashcode 目录/文件名.java -d 输出目录` 使用mc 命令用来编译修改过的代码

![img](assets/1777081809048-100.png)

6、`retransform class文件所在目录/xxx.class` 用 retransform 命令加载新的字节码

![img](assets/1777081809049-101.png)

7、测试：

![img](assets/1777081809049-102.png)

**注意事项：**

1、程序重启之后，字节码文件会恢复，除非将class文件放入jar包中进行更新。

2、使用retransform不能添加方法或者字段，也不能更新正在执行中的方法。

## 2.7、JDK9之后的类加载器

JDK8及之前的版本中，扩展类加载器和应用程序类加载器的源码位于rt.jar包中的sun.misc.Launcher.java。

![img](assets/1777081809049-103.png)

由于JDK9引入了module的概念，类加载器在设计上发生了很多变化。

1.启动类加载器使用Java编写，位于jdk.internal.loader.ClassLoaders类中。

   Java中的BootClassLoader继承自BuiltinClassLoader实现从模块中找到要加载的字节码资源文件。

   启动类加载器依然无法通过java代码获取到，返回的仍然是null，保持了统一。

2、扩展类加载器被替换成了平台类加载器（Platform Class Loader）。

​     平台类加载器遵循模块化方式加载字节码文件，所以继承关系从URLClassLoader变成了BuiltinClassLoader，BuiltinClassLoader实现了从模块中加载字节码文件。平台类加载器的存在更多的是为了与老版本的设计方案兼容，自身没有特殊的逻辑。

# 3、运行时数据区

Java虚拟机在运行Java程序过程中管理的内存区域，称之为运行时数据区。《Java虚拟机规范》中规定了每一部分的作用。

![img](assets/1777081809049-104.png)

### 3.1 程序计数器

程序计数器（Program Counter Register）也叫PC寄存器，每个线程会通过程序计数器记录当前要执行的的字节码指令的地址。

![img](assets/1777081809049-105.png)

一个程序计数器的具体案例：

在加载阶段，虚拟机将字节码文件中的指令读取到内存之后，会将原文件中的偏移量转换成内存地址。每一条字节码指令都会拥有一个内存地址。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZGE0MmIxOWYyNDQ3ZGVhNmEzMTUxNmIyNDcyYTQxZGJfaUlWbUNscVhaYTZGM05nVWYzMFpDSEpmWFM2Qk9YQ0VfVG9rZW46VEJOOGIxSnRMbzVpWGN4OTcxTmNrTGF3blliXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

在代码执行过程中，程序计数器会记录下一行字节码指令的地址。执行完当前指令之后，虚拟机的执行引擎根据程序计数器执行下一行指令。这里为了简单起见，使用偏移量代替，真实内存中执行时保存的应该是地址。

比如当前执行的是偏移量为0的指令，那么程序计数器中保存的就是下一条的地址（偏移量1）。

![img](assets/1777081809049-107.png)

一路向下执行

![img](assets/1777081809049-108.png)

一直执行到方法的最后一行指令，此时方法执行return语句，当前方法执行结束，程序计数器中会放入方法出口的地址（栈中讲解，简单来说就是这个B方法结束了，A调用了B，那么要回到A方法）

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTBjZTAwOTNmZTAxZDQ4OTIxYThhZjQzM2M5YmZkOTNfajFJQXhPZEJtQzI1WXpySHp3RjNNQjE0TzlzZDBLSjlfVG9rZW46SWZuUmJSUmpmb2hKcll4dUJqTWMxR3d0bnBkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以，程序计数器可以控制程序指令的进行，实现分支、跳转、异常等逻辑。不管是分支、跳转、异常，只需要在程序计数器中放入下一行要执行的指令地址即可。

在多线程执行情况下，Java虚拟机需要通过程序计数器记录CPU切换前解释执行到那一句指令并继续解释运行。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NjJlNjkzYzgwZDc2OThhZjc3NDUwYmUyMzNjYzQ3MWZfaThpU21nV3dGZHAwNHlJRGJyQ0hIVkI5VHh5a1ZtQ2pfVG9rZW46V2tDNmJCMm1PbzVWVWl4bDFJMWN3aWlXbnJiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 程序计数器会出现内存溢出吗？

内存溢出指的是程序在使用某一块内存区域时，存放的数据需要占用的内存大小超过了虚拟机能提供的内存上限。由于每个线程只存储一个固定长度的内存地址，程序计数器是不会发生内存溢出的。程序员无需对程序计数器做任何处理。

### 3.2 Java虚拟机栈

Java虚拟机栈（Java Virtual Machine Stack）采用栈的数据结构来管理方法调用中的基本数据，先进后出（First In Last Out）,每一个方法的调用使用一个栈帧（Stack Frame）来保存。

```Java
public class MethodDemo {   
    public static void main(String[] args) {        
         study();    
     }

    public static void study(){
        eat();

        sleep();
    }   
    
    public static void eat(){       
         System.out.println("吃饭");   
    }    
    
    public static void sleep(){        
        System.out.println("睡觉");    
        }
  }
```

main方法执行时，会创建main方法的栈帧：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Yzc5YTAxMWNhMzgyMTg4M2NjZDhhYjkyNmEzMjg4NmRfUmtUVWZxU0xBZElEbEdtMW44S3dkTUpBYlFOcGZadkpfVG9rZW46SWVGVGI2QUEwbzREMFN4VUhuSGM4WUhNblFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

接下来执行study方法，会创建study方法的栈帧

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzI2Nzc3Mjk2NDhjMzYyZDNhMjhkNTFjZGIwY2NkMzZfdk9wNFR2NUNJT2Z1OVZwVlJaZnYxV2ZKZnNmMFh6TzRfVG9rZW46RUJSR2JoWkMxb3RNa0Z4ckx2WGNUQTRWbk9NXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

进入eat方法，创建eat方法的栈帧

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2M1NjNiZTFkODQ5MjBmMmU2YTRlNWNhOGVkNjRhMzhfN2I1eEhSVFJleW01RHJRUllLREFCR2ZjMDhsdlpzQjRfVG9rZW46QkFSNmJVbnphb2s3QlJ4SDlsZGNnNE5UbnljXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

eat方法执行完之后，会弹出它的栈帧：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTU4NGUzYWYwZmE4MTRiYzRmNTcwZGQwZDExZDczZjlfUXd5amcwWG9KSVhkVkRCWE5rcXVSZnVIV2JVWEg0QWRfVG9rZW46RzdZcWJyTHFEb0RLRXd4NHdIQ2NGNjZpbmtnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

然后调用sleep方法，创建sleep方法栈帧

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=N2E5ZDgzNjliMTRiMzNjNTkxYjRiNmU3OWIyYjMyZGRfY2EyaTczSkl6S0pYdWxDRFlpVnhsMHBIM1g4SUZqTWxfVG9rZW46TjFDcWI5TFZrb0N0YlR4MWgyemN3eTEybkVmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

最后study方法结束之后弹出栈帧，main方法结束之后弹出main的栈帧。

在IDEA中也可以看到对应的栈帧：

```Java
package chapter03.frame;

/**
 * 栈帧测试1
 */
public class FrameDemo {
    public static void main(String[] args) {
        A();
    }

    public static void A() {
        System.out.println("A执行了...");
        B();
    }

    public static void B() {
        System.out.println("B执行了...");
        C();
    }

    public static void C() {
        System.out.println("C执行了...");
        throw new RuntimeException("测试");
    }
}
```

打上断点debug之后会出现栈帧内容：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmE0MTA0OWE3NmJjYmFlYjQ0MGQwMmVmOTA0M2EyMzFfWWIwb1BUZzhOSE5Od1VaanprZVVIb3lNUjVkTHJhV3lfVG9rZW46R2pCSGJQM1p5b3REZzB4cG82RGMzd3NmbnJRXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

Java虚拟机栈随着线程的创建而创建，而回收则会在线程的销毁时进行。由于方法可能会在不同线程中执行，每个线程都会包含一个自己的虚拟机栈。如下就有两个线程的虚拟机栈，main线程和线程A。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGVkNWFlOWQ2MWViMWNiZTZlYmE4OTk2OWE3YjlmYTRfN21rdmlRUUo1QkVkbW0xR045WjFYVDQ4d3R0QTQwUDRfVG9rZW46SmRZMmJ6aUI5b1V1ZTR4WVpHbGNqWXNTbkFlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

Java虚拟机栈的栈帧中主要包含三方面的内容：

- 局部变量表，局部变量表的作用是在运行过程中存放所有的局部变量
- 操作数栈，操作数栈是栈帧中虚拟机在执行指令过程中用来存放临时数据的一块区域
- 帧数据，帧数据主要包含动态链接、方法出口、异常表的引用

#### 局部变量表

局部变量表的作用是在方法执行过程中存放所有的局部变量。局部变量表分为两种，一种是字节码文件中的，另外一种是栈帧中的也就是保存在内存中。栈帧中的局部变量表是根据字节码文件中的内容生成的。

我们先来看下字节码文件中的局部变量表：编译成字节码文件时就可以确定局部变量表的内容。

```Java
public static void test1(){
    int i = 0;
    long j = 1;
}
```

test1方法的局部变量表如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Nzk4MDU4NDNmNDk4YzYyOWJkMTM1OTc3YmQ5N2E4ZTdfRk02Y3c0TjJIMjBmQ0h0aUt6ZklKcDJKdTNtQ25RcDlfVG9rZW46SUJpcGI2NFFibzNpSXd4S04zN2NZTTBNblllXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

局部变量表中保存了字节码指令生效的偏移量：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MWUxNmY3NDUxM2I5ZDZiM2VjYzk1ZTI1MjViYThmZmRfT3dUVVJJZTd6OWZ1VklzWmhnNVNVczNQVlVnZHR0Y2NfVG9rZW46T1h4bGJoYXBob3JTQWl4Sk5qMGNxWHV6bkZOXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

比如`i`这个变量，它的起始PC是2，代表从`lconst_1`这句指令开始才能使用`i`，长度为3，也就是2-4这三句指令都可以使用`i`。为什么从2才能使用，因为0和1这两句字节码指令还在处理`int i = 0`这句赋值语句。`j`这个变量只有等3指令执行完之后也就是`long j = 1`代码执行完之后才能使用，所以起始PC为4，只能在4这行字节码指令中使用。

接下来看下栈帧中的局部变量表，栈帧中的局部变量表是一个数组，数组中每一个位置称之为槽(slot) ，long和double类型占用两个槽，其他类型占用一个槽。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTUxMjFhNDFlZGI3ZWRhMjVkNDhhNzVkMTU4MzE5NGVfWlA4TVU3V2hxYjM2NE5ycTRGZ3F4c2tNaWhLTmE5dlRfVG9rZW46R3lzbGJxNk1kb052WnN4YUlxcWNZMTRXbnNlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

`i`占用数组下标为0的位置，`j`占用数组下标1-2的位置。

刚才看到的是静态方法，实例方法中的序号为0的位置存放的是this，指的是当前调用方法的对象，运行时会在内存中存放实例对象的地址。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YThiOWRhZDAzZmM5NDZlOTJlMWI2NDczMGMxNGU2MThfUEo0M25Kb3JUektwTjNrOTdqY1F1cHhRWFpXamxoTUhfVG9rZW46WEpVamJQdTVKb203eGt4cE1Qc2N6Y29EbjNkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

方法参数也会保存在局部变量表中，其顺序与方法中参数定义的顺序一致。局部变量表保存的内容有：实例方法的this对象，方法的参数，方法体中声明的局部变量。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzM0ZjA0ZmJlZjhlYjQ0YzYyYmRhMmNjMmJhZDg1NTZfQ2tMNkswOUM0N3JlNHhHRzZtYlEyaEp5MWpSZXl6eTFfVG9rZW46U0E5a2JCSVZrbzM0Y2t4N3QxcWNpOE91bk9nXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

test3方法中包含两个参数`k`,`m`，这两个参数也会被加入到局部变量表中。

**以下代码的局部变量表中会占用几个槽？**

```Java
public void test4(int k,int m){
    {
        int a = 1;
        int b = 2;
    }
    {
        int c = 1;
    }
    int i = 0;
    long j = 1;
}
```

为了节省空间，局部变量表中的槽是可以复用的，一旦某个局部变量不再生效，当前槽就可以再次被使用。

1、方法执行时，实例对象`this`、`k`、`m` 会被放入局部变量表中，占用3个槽

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTBlM2E5OWY1NjA3MGRiYTYyOWIwMzBiN2JlYTc4MzJfYzJ6R1c5TGdQMVNwQjdGRFc1cDB3d3F0aXJ1aGIyNGZfVG9rZW46QmJzSmJsNkpPb1dnQ1J4bzBUZWNSeXBwbklkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、将1的值放入局部变量表下标为3的位置上，相当于给a进行赋值。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWVhOWU1MzE3ZmY0NmZhMGJjMDMyYTgzODY1ZjE4NzNfVGI2OFpiZFJoMHpGT0czYW00cUxhMDBidUpjT2NsSEZfVG9rZW46QU1lZGJ4NFExb1doS1l4R3l4TmN2eHgxbndlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、将2放入局部变量表下标为4的位置，给b赋值为2。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzAzMjc1NTk1NGY2MTFkNWNhYjY2MGEwZDA2OWRhZTZfQ0pXeFBabkRPRjY2RXFESVhzbUthZjYxeDlGVmY3UUhfVG9rZW46SzRVbGJCekJWb29NVUV4S2EzOGM5dzRpbmloXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、ab已经脱离了生效范围，所以下标为3和4的这两个位置可以复用。此时c的值1就可以放入下标为3的位置。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Yjg2ZTI4YjYyNWM5M2I4YWJjYTE2YjNlZGE4NDJmM2Rfbk9XQW5CM1BaWlVGTncyQ0daZE5IV2pMZ0hjWFJUVTlfVG9rZW46SDVqN2JKeG5Xb0NRd3d4YUYzZGNsN1VJbmZjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、脱离c的生效范围之后，给i赋值就可以复用c的位置。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjVlZDJhMDkyMzQ5MzAwMDUzYmI1OTBkMTUwYmZmNzVfU0pOUGlnbjVGc090elNTWGxoY1Npem96VnpxSEd2UUZfVG9rZW46VTlQcGJOOHM0bzRxTHR4cThFd2NIcEZabnhoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

5、最后放入j，j是一个long类型，占用两个槽。但是可以复用b所在的位置，所以占用4和5这两个位置

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTM5YThjZDhhMjI1ZDE4OWRhMDA3ZDc4MTQxZmRiOGJfN2lEakNjOThNc0tGd0huYXZ6S0hVR085MHNNMTVxTkRfVG9rZW46TWFYZGJJQkU3b1BsVXZ4cnhsZGM0VVZqbjllXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以，局部变量表数值的长度为6。这一点在编译期间就可以确定了，运行过程中只需要在栈帧中创建长度为6的数组即可。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGY4MmQ4YzUyNzZiMGMyNjFjZTRlYmI3MTdiMTgwZTBfVG9HUng1bXB6ZU10bTdCTzltRThsSlVvSUlkZWRoRkpfVG9rZW46UUNTQmJDU3RHb1liYnl4M2dGUmNnclpXblBlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 操作数栈

操作数栈是栈帧中虚拟机在执行指令过程中用来存放中间数据的一块区域。他是一种栈式的数据结构，如果一条指令将一个值压入操作数栈，则后面的指令可以弹出并使用该值。

在编译期就可以确定操作数栈的最大深度，从而在执行时正确的分配内存大小。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2Q4ZjMyYWQ4YmY1YjJmM2RmMTRlODYxOGUyOTdjMTFfTDhlMkwyN01zVnNiR0FxVU5nN25nczFIb0w0RksxSXJfVG9rZW46UWFBY2JZVHFZbzlZUHd4ZkNieGNXbmNRblBjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

比如之前的相加案例中，操作数栈最大的深入会出现在这个时刻：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGY4MTBlYTFiN2QwYjFkZjJlMmNmMzE1MzZlNDMyMzRfSDE3MnNIYXJPNG94Vll5ZndFT3E1V3UxU21lS3l1SGhfVG9rZW46TXpDN2JrQmN0b2tYRGJ4M1NBMWN4RENwbmZkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以操作数栈的深度会定义为2。

#### 帧数据

帧数据主要包含动态链接、方法出口、异常表的引用。

##### 动态链接

当前类的字节码指令引用了其他类的属性或者方法时，需要将符号引用（编号）转换成对应的运行时常量池中的内存地址。动态链接就保存了编号到运行时常量池的内存地址的映射关系。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjFkNzg3ZDgxMWIwNDBlOTAwZGExNWQwMTg5MDgzODJfQ0F1azlWUlZxeTh4cmsyQkZBUXFveHozR05zQ1phUFlfVG9rZW46QnhEeGJhNVVYb2hMbEh4RVZmTWNXcnkybk5mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### 方法出口

方法出口指的是方法在正确或者异常结束时，当前栈帧会被弹出，同时程序计数器应该指向上一个栈帧中的下一条指令的地址。所以在当前栈帧中，需要存储此方法出口的地址。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTY0OTM5NTJmNTgzMjg2MmExYjc5NzY1Y2E0NTJhYjdfQVRSR1dKZlRKdWlCUGRWRTJQRlBjWDl1QkJJczRUY3FfVG9rZW46THo2TWJLdlUwb2xRTXh4eUhaSWNHUXBGbnJmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### 异常表

异常表存放的是代码中异常的处理信息，包含了异常捕获的生效范围以及异常发生后跳转到的字节码指令位置。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTE0MGMyOThjNzk5NjU2MzRhMThhNGUyZjQ1NWYzYTNfSklQMW1GODVFQmhpY0dmM0VWUGs2cWFRMm1yeDZRZlRfVG9rZW46RktIYmJkNXJFb29iWFd4dWs3RGMwVUd5bnpiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

`如下案例：i=1`这行源代码编译成字节码指令之后，会包含偏移量2-4这三行指令。其中2-3是对i进行赋值1的操作，4的没有异常就跳转到10方法结束。如果出现异常的情况下，继续执行到7这行指令，7会将异常对象放入操作数栈中，这样在catch代码块中就可以使用异常对象了。接下来执行8-9，对i进行赋值为2的操作。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmQyNWJlMzQ1ZjA0NTcwMmM3MzZkNjEzODIzYzYyYjRfQmZKWG5kdzBhTWNkUmJ5bUxhN21jWFEwbkhxWm13MElfVG9rZW46SFNvMWJBVzd5b3haSk54UlVUWmNLeHY4bjhmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以异常表中，异常捕获的起始偏移量就是2，结束偏移量是4，在2-4执行过程中抛出了`java.lang.Exception`对象或者子类对象，就会将其捕获，然后跳转到偏移量为7的指令。

#### 栈内存溢出

Java虚拟机栈如果栈帧过多，占用内存超过栈内存可以分配的最大大小就会出现内存溢出。Java虚拟机栈内存溢出时会出现StackOverflowError的错误。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzJlZmM4NjdmMzU2NWJhMDY0ZDlhMWE1NWJjNDMzZTJfY3lOTjg3SWxGeXB0MVVRUUNEeDdZUzBNeXEwSTZtc2lfVG9rZW46UHBNNmJyUjNQb2VMejR4elJCN2N1elNXbmhmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果我们不指定栈的大小，JVM 将创建一个具有默认大小的栈。大小取决于操作系统和计算机的体系结构。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Nzg4ZWUxZTI1NDk1MGIzZTM2MzI0NDQzZDBkZTQ5ODRfNXFVZHdjQ00wc3J6U08xVXFSSXdGbVBnZGF2Ym1CaHhfVG9rZW46SGY0V2JyUldjb1Z4bkh4SkNUYWNKbG91bmpkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

我们来模拟下栈内存的溢出情况:

```Java
public static int count = 0;
     //递归方法调用自己
     public static void recursion(){
         System.out.println(++count);
         recursion();
     }
```

使用递归让方法调用自身，但是不设置退出条件。定义调用次数的变量，每一次调用让变量加1。查看错误发生时总调用的次数。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWJiYmM5MDQ1MTc2NjQ1OTYzY2JiMTQ3NmQ4YTdkYWNfMFFLR3VrR0V3NXBQcnE5SWpEaUFSWFhpNkVzd2tkUXVfVG9rZW46UGNST2J0Z1pab1dzV014cmpWRWNOeURibnpiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

执行之后可以打印出溢出时总栈帧的数量，并且发现虚拟机已经抛出了StackOverflow的错误。

要修改Java虚拟机栈的大小，可以使用虚拟机参数 -Xss 。

- 语法：-Xss栈大小
- 单位：字节（默认，必须是 1024 的倍数）、k或者K(KB)、m或者M(MB)、g或者G(GB)

例如：

```Java
-Xss1048576 
-Xss1024K      
-Xss1m
-Xss1g
```

操作步骤如下，不同IDEA版本的设置方式会略有不同：

1、点击修改配置Modify options

2、点击Add VM options

3、添加参数

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzE1ZDFkZmQ2YTg3MDRkNmMyMmExMDlmNWY4MTAwM2Jfd2FJMFl2ZXRpcDdiOHJ1bVNLMHo3ZEcyZkw5c2pNa2lfVG9rZW46Wk9qRWJEZWxNb0UwbUZ4ajB0MmNZWVh4blJlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

调成512k之后，明显发现最大栈帧数量减少了：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjMyOGMwNzA1ZmJmNmUyZjI3NzdmZWIyMTg3NDJlZjhfRXVoeWJQN0ZQaU02aWNEV1E3OUNTR1o1c2lDSVBCMWpfVG9rZW46STZLaWI1ZW9sb0daR2V4amIwOGM3azRkblJiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

**注意事项：**

1、与-Xss类似，也可以使用 -XX:ThreadStackSize 调整标志来配置堆栈大小。

格式为： `-XX:ThreadStackSize=1024`

2、HotSpot JVM对栈大小的最大值和最小值有要求：

​      比如测试如下两个参数，会直接报错:

```
-Xss1k
-Xss1025m
```

Windows（64位）下的JDK8测试最小值为`180k`，最大值为`1024m`。

3、局部变量过多、操作数栈深度过大也会影响栈内存的大小。我们在这段代码中添加一些局部变量。

```Java
//递归方法调用自己
public static void recursion() {
    long a,b,c,d,f,g,h,i,j,k;
    System.out.println(++count);
    recursion();
}
```

使用默认大小来测试之后，发现栈帧数量从10000+减少了到8000+

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MDYzOGIwYzU3OTQzNzYyYTIwZDJlODM4ZmU2Njc2NGJfQXRsTXM0SFNBTlE5bWhOaWd3bFBCeTlaQkRGTmhOZVdfVG9rZW46VFJVM2JBVVo0bzhzQUR4S2Myb2NlSDNzbkJlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

一般情况下，工作中即便使用了递归进行操作，栈的深度最多也只能到几百,不会出现栈的溢出。所以此参数可以手动指定为-Xss256k节省内存。

### 3.3 本地方法栈

Java虚拟机栈存储了Java方法调用时的栈帧，而本地方法栈存储的是native本地方法的栈帧。

在Hotspot虚拟机中，Java虚拟机栈和本地方法栈实现上使用了同一个栈空间。本地方法栈会在栈内存上生成一个栈帧，临时保存方法的参数同时方便出现异常时也把本地方法的栈信息打印出来。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmQ5NGE5Mjg2Y2ZjMGQwNjBkODBlNmYwMTBiYmYzOWRfNmhQRGlmMkt0ak1xckJ1blVXOWh0SmJ4QmREMWVjcjVfVG9rZW46RmxJMWJZVEFkbzdBbUF4TFlVVWNsd2k5bkVoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

比如测试下这段代码：

```Java
/**
 * 本地方法栈
 */
public class NativeDemo1 {
    public static void main(String[] args) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("E:\\123.txt");
            fileOutputStream.write(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

执行之后发生异常，会打印出所有栈帧的名字：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzNhMTFkYThmMDkxOTI4NWJiYWJlNzUxMzY3MmFmYTlfYlBRZkl2cGZ2YnRjc1pOTUozQkVpcGlVTURTZjFkTlBfVG9rZW46SElTY2JHekJEb3VOdEN4Y0tYMWNDaHFVbnMwXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

`open0`是一个本地方法，所以创建了本地方法的栈帧。本地方法和Java虚拟机方法的栈帧在一个栈上。

### 3.4 堆内存

一般Java程序中堆内存是空间最大的一块内存区域。创建出来的对象都存在于堆上。栈上的局部变量表中，可以存放堆上对象的引用。静态变量也可以存放堆对象的引用，通过静态变量就可以实现对象在线程之间共享。

```Java
public class Test {    
    public static void main(String[] args) {        
        Student s1 = new Student();        
        s1.name = "张三";       
        s1.age = 18;       
        s1.id = 1;
        s1.printTotalScore();        
        s1.printAverageScore();        
        
        Student s2 = new Student();       
        s2.name = "李四";        
        s2.age = 19;        
        s2.id= 2;        
        s2.printTotalScore();        
        s2.printAverageScore();    
    }
}
```

这段代码中通过`new`关键字创建了两个`Student`类的对象，这两个对象会被存放在堆上。在栈上通过`s1`和`s2`两个局部变量保存堆上两个对象的地址，从而实现了引用关系的建立。

![img](assets/1777081809050-144.png)

#### 堆内存的溢出

通过new关键字不停创建对象，放入集合中，模拟堆内存的溢出，观察堆溢出之后的异常信息。

```Java
package chapter03.heap;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 堆内存的使用和回收
 */
public class Demo1 {
    public static void main(String[] args) throws InterruptedException, IOException {

        ArrayList<Object> objects = new ArrayList<Object>();
        System.in.read();
        while (true){
            objects.add(new byte[1024 * 1024 * 100]);
            Thread.sleep(1000);
        }


    }
}
```

堆内存大小是有上限的，当对象一直向堆中放入对象达到上限之后，就会抛出OutOfMemory错误。在这段代码中，不停创建100M大小的字节数组并放入ArrayList集合中，最终超过了堆内存的上限。抛出如下错误：

![img](assets/1777081809050-145.png)

#### 三个重要的值

堆空间有三个需要关注的值，used、total、max。used指的是当前已使用的堆内存，total是java虚拟机已经分配的可用堆内存，max是java虚拟机可以分配的最大堆内存。

![img](assets/1777081809050-146.png)

堆内存used total max三个值可以通过dashboard命令看到。

> 手动指定刷新频率（不指定默认5秒一次）：`dashboard –i  刷新频率(毫秒)`

![img](assets/1777081809050-147.png)

随着堆中的对象增多，当total可以使用的内存即将不足时，java虚拟机会继续分配内存给堆。

![img](assets/1777081809050-148.png)

此时used达到了total的大小，Java虚拟机会向操作系统申请更大的内存。

![img](assets/1777081809050-149.png)

但是这个申请过程不是无限的，total最多只能与max相等。

![img](assets/1777081809050-150.png)

那么是不是当used = max = total的时候，堆内存就溢出了呢？

不是，堆内存溢出的判断条件比较复杂，在下一章《垃圾回收器》中会详细介绍。

如果不设置任何的虚拟机参数，max默认是系统内存的1/4，total默认是系统内存的1/64。在实际应用中一般都需要设置total和max的值。 Oracle官方文档：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html

#### 设置堆的大小

要修改堆的大小，可以使用虚拟机参数 –Xmx（max最大值）和-Xms (初始的total)。

语法：`-Xmx值 -Xms值`

单位：字节（默认，必须是 1024 的倍数）、k或者K(KB)、m或者M(MB)、g或者G(GB)

限制：Xmx必须大于 2 MB，Xms必须大于1MB

```Java
-Xms6291456
-Xms6144k
-Xms6m
-Xmx83886080
-Xmx81920k
-Xmx80m
```

这样可以将max和初始的total都设置为4g，在启动后就已经获得了最大的堆内存大小。运行过程中不需要向操作系统申请。

![img](assets/1777081809050-151.png)

使用`arthas`的`memory`命令同样可以看到：

![img](assets/1777081809050-152.png)

为什么arthas中显示的heap堆大小与设置的值不一样呢？

arthas中的heap堆内存使用了JMX技术中内存获取方式，这种方式与垃圾回收器有关，计算的是可以分配对象的内存，而不是整个内存。

**建议：**

Java服务端程序开发时，建议将-Xmx和-Xms设置为相同的值，这样在程序启动之后可使用的总内存就是最大内存，而无需向java虚拟机再次申请，减少了申请并分配内存时间上的开销，同时也不会出现内存过剩之后堆收缩的情况。-Xmx具体设置的值与实际的应用程序运行环境有关，在《实战篇》中会给出设置方案。

### 3.5 方法区

方法区是存放基础信息的位置，线程共享，主要包含三部分内容：

- 类的元信息，保存了所有类的基本信息
- 运行时常量池，保存了字节码文件中的常量池内容
- 字符串常量池，保存了字符串常量

#### 类的元信息

方法区是用来存储每个类的基本信息（元信息），一般称之为InstanceKlass对象。在类的加载阶段完成。其中就包含了类的字段、方法等字节码文件中的内容，同时还保存了运行过程中需要使用的虚方法表（实现多态的基础）等信息。

![img](assets/1777081809050-153.png)

#### 运行时常量池

方法区除了存储类的元信息之外，还存放了运行时常量池。常量池中存放的是字节码中的常量池内容。

字节码文件中通过编号查表的方式找到常量，这种常量池称为静态常量池。当常量池加载到内存中之后，可以通过内存地址快速的定位到常量池中的内容，这种常量池称为运行时常量池。

![img](assets/1777081809050-154.png)

#### 方法区的实现

方法区是《Java虚拟机规范》中设计的虚拟概念，每款Java虚拟机在实现上都各不相同。Hotspot设计如下：

JDK7及之前的版本将方法区存放在堆区域中的永久代空间，堆的大小由虚拟机参数来控制。

JDK8及之后的版本将方法区存放在元空间中，元空间位于操作系统维护的直接内存中，默认情况下只要不超过操作系统承受的上限，可以一直分配。

![img](assets/1777081809050-155.png)

可以通过arthas的`memory`命令看到方法区的名称以及大小：

- JDK7及之前的版本查看ps_perm_gen属性。

![img](assets/1777081809050-156.png)

- JDK8及之后的版本查看metaspace属性。

![img](assets/1777081809050-157.png)

#### 方法区的溢出

通过ByteBuddy框架，动态创建类并将字节码数据加载到内存中。通过死循环不停地加载到方法区，观察方法区是否会出现内存溢出的情况。分别在JDK7和JDK8上运行上述代码。

ByteBuddy是一个基于Java的开源库，用于生成和操作Java字节码。

1.引入依赖

```XML
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy</artifactId>
    <version>1.12.23</version>
 </dependency>
```

2.创建ClassWriter对象

```Java
 ClassWriter classWriter = new ClassWriter(0);
```

3.调用visit方法，创建字节码数据。

```Java
classWriter.visit(Opcodes.V1_7,Opcodes.ACC_PUBLIC,name,null ,"java/lang/Object",null);
byte[] bytes = classWriter.toByteArray();
```

代码：

```Java
package chapter03.methodarea;

import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.Opcodes;

import java.io.IOException;

/**
 * 方法区的溢出测试
 */
public class Demo1 extends ClassLoader {
    public static void main(String[] args) throws IOException {
        System.in.read();
        Demo1 demo1 = new Demo1();
        int count = 0;
        while (true) {
            String name = "Class" + count;
            ClassWriter classWriter = new ClassWriter(0);
            classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, name, null
                    , "java/lang/Object", null);
            byte[] bytes = classWriter.toByteArray();
            demo1.defineClass(name, bytes, 0, bytes.length);
            System.out.println(++count);
        }
    }
}
```

实验发现，JDK7上运行大概十几万次，就出现了错误。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDBiNThmYjlmYWJjMzFjYWQ3YTVkZWU2ZWNmNTY1Y2ZfcnkyU0s1ZXd0UjQ2dW9GakdZYnhpd3VMZmFqdXhHa2JfVG9rZW46SDJ6bmJBS3hRb2t0M1V4MEd0Y2NZTlBXbjNmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

在JDK8上运行百万次，程序都没有出现任何错误，但是内存会直线升高。这说明JDK7和JDK8在方法区的存放上，采用了不同的设计。

- JDK7将方法区存放在堆区域中的永久代空间，堆的大小由虚拟机参数-XX:MaxPermSize=值来控制。
- JDK8将方法区存放在元空间中，元空间位于操作系统维护的直接内存中，默认情况下只要不超过操作系统承受的上限，可以一直分配。可以使用-XX:MaxMetaspaceSize=值将元空间最大大小进行限制。

在JDK8中将最大元空间内存设置为256m，再次测试

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjcyMWNhZGVhMmExNWI2ZWQxODI3NGMxMzAwZTY1MGZfS2JveWRLb0c2c2tzN3ZNV1JHeHlBcHNaS1lBQWlISk5fVG9rZW46TnFOTGJDMDVrbzJvdG54V0tpZ2Mwc252bmhnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这次就出现了MetaSpace溢出的错误：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmVhYjc1MDc5NDU0Y2U0NzBmMTFlNmQwZTg4MzdlZmJfMVRMUnVZM0dwZGJNdzA4SjNlYjJiTGh4V1ZVSWNWOGRfVG9rZW46SGJzVWJ4ZlNWb1hibTJ4cW5Ha2NLNE1GbmdlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 字符串常量池

方法区中除了类的元信息、运行时常量池之外，还有一块区域叫字符串常量池(StringTable)。

字符串常量池存储在代码中定义的常量字符串内容。比如“123” 这个123就会被放入字符串常量池。

如下代码执行时，代码中包含`abc`字符串，就会被直接放入字符串常量池。在堆上创建String对象，并通过局部变量s1引用堆上的对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjAwNzk5YTFkNmU5ZWIyNmUyYWRmMWZlZjUwYWJjNDdfT0lrRTVuZElBN1JaUld2alNLSnF1QklGRmhKc1V3R3VfVG9rZW46U0dhQ2JXVW8xb3BBSkV4bjhNWGNPNVRlbm5jXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

接下来通过s2局部变量引用字符串常量池的`abc`。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDRhMTRkMmJjN2ZkODVhYjVhOGM0MjhjN2VjNGFiNWVfSU9RQUhiSTZrTUxwbVNsMFZFSTJYQnpWOTN3R2Q0bmRfVG9rZW46VE5CTGIxWk8zb0MzNml4NnRiQ2NsRGpublZiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以s1和s2指向的不是同一个对象，打印出`false`。

##### 字符串常量池和运行时常量池有什么关系？

早期设计时，字符串常量池是属于运行时常量池的一部分，他们存储的位置也是一致的。后续做出了调整，将字符串常量池和运行时常量池做了拆分。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODIyNjE2ZjA4MjAyOGI1NDg1YTBmNzEzN2ZjMzc0ODlfSlpoZzJsRGptc0N6NVlhWnFZZDdERkh5Rk1jWnJRVUlfVG9rZW46QXJxWmJLZ3Bqb1dBYjF4NllkRmNXVExIbktnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### StringTable的练习题1：

```Java
/**
 * 字符串常量池案例
 */
public class Demo2 {
    public static void main(String[] args) {
        String a = "1";
        String b = "2";
        String c = "12";
        String d = a + b;
        System.out.println(c == d);
    }
}
```

1、首先将`1`放入字符串常量池，通过局部变量a引用字符串常量池中的`1`字符串。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDNmZTQ3NDI3ZTAzOTAyOGE0MzQwYjRlMmNkYzlmNzVfS2x6SXlJR2VJQUc1TzhscjVoQnhrWWl1OXlESHl4SHFfVG9rZW46RXRaRWJnY1p0b0w5VWV4WFJoZGNSWVBzbjRiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、同理处理b和c：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGNiNjM3YjViMjRiMzliN2RhYTQzYTU2OTc4Nzc0ZGJfMTR1N2JSc0dYTDhmZjZPR3FPRmxZY0M3dVd5SVRlZFhfVG9rZW46Ukk2cWJnR2hWbzlsOW94TDJ1WGNEbHBybjRjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、将a和b指向的字符串进行连接，本质上就是使用StringBuilder进行连接，最后创建了一个新的字符串放入堆中。然后将局部变量d指向堆上的对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGM4NTI4OTJjZWYxZmViMzYwMWExMTViMjIyZTVkNjhfcDBzbzVndW5WUDRzZjB3UkdKV2M3MURsRk9JN1RNaXhfVG9rZW46V3ZlTGJLa1k0b2JDRXF4M0t3cmNSRnQybkxiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、所以c和d指向的不是同一个对象，打印出的结果就是false。

##### StringTable的练习题2：

```Java
package chapter03.stringtable;

/**
 * 字符串常量池案例
 */
public class Demo3 {
    public static void main(String[] args) {
        String a = "1";
        String b = "2";
        String c = "12";
        String d = "1" + "2";
        System.out.println(c == d);
    }
}
```

编译之后的字节码指令如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWQ1YzhmNzdiMDVhYzY3M2FkYWE1ODdmZmIyNmI1YTBfV2pFMjdwU1lpUHB0Uks0eElHUlNMREladEt3aHJ5bWJfVG9rZW46SU4wVmJMcXlSb1NsNTh4SFJCUGNWTDZ6bnNlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

说明在编译阶段，已经将1和2进行连接，最终生成12的字符串常量池中的结果。所以返回结果就是true，c和d都指向字符串常量池中的对象。

总结一下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTFlYTUxZjQ0OGI0ZDM1YWRkMWU4MzRmZjgwZDMzNmZfaVpUOEVpdlBobHY4TFZHbE1lWmtDbEcxSmZ4SjJmSWNfVG9rZW46SW9CM2JTcEFmb2dVWVd4SE5maGNHSnhTbkJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##### 神奇的intern

String.intern()方法是可以手动将字符串放入字符串常量池中，分别在JDK6 JDK8下执行代码，JDK6 中结果是false false ，JDK8中是true false

```Java
package chapter03.stringtable;

/**
 * intern案例
 */
public class Demo4 {
    public static void main(String[] args) {
        String s1 = new StringBuilder().append("think").append("123").toString();

        System.out.println(s1.intern() == s1);
//        System.out.println(s1.intern() == s1.intern());

        String s2 = new StringBuilder().append("ja").append("va").toString();

        System.out.println(s2.intern() == s2);
    }
}
```

先来分析JDK6中，代码执行步骤如下：

1、使用StringBuilder的将`think`和`123`拼接成`think123`，转换成字符串，在堆上创建一个字符串对象。局部变量`s1`指向堆上的对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmM4MjJkMDFkOGM5ZTBkYjNiOTMzMTVlODQ3OGNmNmZfWUpWaGlGenJyTkVvQmppU001cW1EQkZvUXNLVm81SUVfVG9rZW46S1hiUmIwVEpHb0tnN3B4azJTMGNvNzBJblNkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、调用s1.intern方法，会在字符串常量池中创建think123的对象，最后将对象引用返回。所以s1.intern和s1指向的不是同一个对象。打印出false。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NjIxNDYwOTQzNmM2YjQ3OWRlYmRlN2NjMWQ0NDVkOGFfYjZCWTRJQVA1WTFzTjNjY21QYjFuTk44ZzhmQ3JvelRfVG9rZW46SmVQa2JRSXE5b2lxcjF4bUxadWNtRTZibm1lXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、同理，通过StringBuilder在堆上创建java字符串对象。这里注意字符串常量池中本来就有一个java字符串对象，这是java虚拟机自身使用的所以启动时就会创建出来。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NjhhMjM4MTc1OTdjOTE2YzAyNmJjMzBiNmRhY2NlNTZfTVd2ZDFUUGdBeFZHZUs0WGo1aE9iZnFNZ0NoS3VwR3BfVG9rZW46T09sR2JNOHIwb2ltZUV4aGwyUGNZWXRtbmNlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、调用s2.intern发现字符串常量池中已经有java字符串对象了，就将引用返回。所以s2.intern指向的是字符串常量池中的对象，而s2指向的是堆中的对象。打印结果为false。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YmRmN2NhYmI3ZTRiNmM5NDFhYzBlYmVmYTEyMWFiZDFfcEdOYlNnVTdQb21mNTZlQVlMNTRtcUZuOFFHM0F1aW9fVG9rZW46RDhlcWJaUEROb2xNbzd4T2g3SmNGUWt3bkpnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

接下来分析JDK7中，JDK7及之后版本中由于字符串常量池在堆上，所以intern () 方法会把第一次遇到的字符串的引用放入字符串常量池。

代码执行步骤如下：

1、执行第二句代码时，由于字符串常量池中没有think123的字符串，所以直接创建一个引用，指向堆中的think123对象。所以s1.intern和s1指向的都是堆上的对象，打印结果为true。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWI4ZWUxMDQyYzNjMzlhZDgyYzc5YmVlMTYxYTk5Y2VfTlV1RDExWkdPZUlGQ1dQRk9qd3ZXTGMyTXdkdHRjbFRfVG9rZW46TjJNZGJ5Tkxrbzd5Zkp4bDFIVGNaTHoybjdiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、s2.intern方法调用时，字符串常量池中已经有java字符串了，所以将引用返回。这样打印出来的结果就是false。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTVhODc0ODU2OTBkN2JmYmJhZDU1NTQzYzYzNDVkYTlfczIzZ1hTMGV0dFVsQ3RFaGRlMUc0QlM1NzkyUlJzSnFfVG9rZW46T3lSZWJ5Tk83b1Yya0Z4MDY2bmNPYmRzbm9nXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

> 后续JDK版本中，如果Java虚拟机不需要使用java字符串，那么字符串常量池中就不会存放`java`。打印结果有可能会出现两个true。

####  面试题：静态变量存储在哪里呢？

- JDK6及之前的版本中，静态变量是存放在方法区中的，也就是永久代。
- JDK7及之后的版本中，静态变量是存放在堆中的Class对象中，脱离了永久代。具体源码可参考虚拟机源码：BytecodeInterpreter针对putstatic指令的处理。

### 3.6 直接内存

直接内存（Direct Memory）并不在《Java虚拟机规范》中存在，所以并不属于Java运行时的内存区域。

在 JDK 1.4 中引入了 NIO 机制，使用了直接内存，主要为了解决以下两个问题:

1、Java堆中的对象如果不再使用要回收，回收时会影响对象的创建和使用。

2、IO操作比如读文件，需要先把文件读入直接内存（缓冲区）再把数据复制到Java堆中。

现在直接放入直接内存即可，同时Java堆上维护直接内存的引用，减少了数据复制的开销。写文件也是类似的思路。

使用堆创建对象的过程：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDFmZTY3MTU5ODJkZTkyNzljZThiZWYzMjUwNjNjNWFfSUZVY3VSdWJBcWNNdzlnN0xiSEllaGRLZlNXdFNYSVlfVG9rZW46UDB2aGJ6N0hUb1hncWN4VDZlRWNqSzEwbndiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

使用直接内存创建对象的过程，不需要进行复制对象，数据直接存放在直接内存中：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGRiM2RkODkwZmE5MTMwNzBkYmI0YzhlMzVhYWEzMmNfblRBQWI2d2JKR3JSbWlVaGc0NWlJR29pUTlFVkEybGVfVG9rZW46VGd5cWJZOThNb3hyMzR4YzJuTGNXaWpJbnJoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 使用方法：

要创建直接内存上的数据，可以使用`ByteBuffer`。

语法： `ByteBuffer directBuffer = ByteBuffer.allocateDirect(size);`

注意事项： arthas的memory命令可以查看直接内存大小，属性名direct。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTVkZmM3MTk0Y2ZmNWU0MDE0NGNhMzlmMzQ5MTRkM2FfRnZzcUllNDFPOWF3UXhwb0Q4d1ltblhJTEc1ZEhTb0lfVG9rZW46WERMN2J1c2lWb2NKRXN4bm16SmNXTEFYbldiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

代码：

```Java
package chapter03.direct;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 直接内存的使用和回收
 */
public class Demo1 {
    public static int size = 1024 * 1024 * 100; //100mb
    public static List<ByteBuffer> list = new ArrayList<ByteBuffer>();
    public static int count = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.in.read();
        while (true) {
            //1.创建DirectByteBuffer对象并返回
            //2.在DirectByteBuffer构造方法中，向操作系统申请直接内存空间
            ByteBuffer directBuffer = ByteBuffer.allocateDirect(size);
            //directBuffer = null;

            list.add(directBuffer);
            System.out.println(++count);
            Thread.sleep(5000);
        }

    }
}
```

运行之后，用arthas监控发现，每隔5秒直接内存会增长100M：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MTVmMjE1OTk1NzMzN2RkNzVjM2E5OGE5NDM3MmY4ODBfU25ucFVBQXdkSVV4c2tuYm5NMXhzZFh0RWdvbkg3eGVfVG9rZW46UXJNaGJ2b0hEb0Y3Tkl4MnFIcGM1WkI0blhmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果将`Thread.`*`sleep`*`(5000);`注释掉，让直接内存快速大量分配。操作系统内存不足时就会报错：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWViMTlhZDIyYjQ4MWQzMTRiMTQ1ODIwMzk4YmQxNTZfQ3VxdVNTdmFvOUFZWFpLa0pYQVBPYUY3Vjc3Q1VJeVZfVG9rZW46TVNqUGJYRW9ub0JSQ2R4Zk92RWNWWGQ3bnRTXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

但是工作中服务器上有可能部署了其他应用，为了避免将内存耗尽，需要设置直接内存的最大值。如果需要手动调整直接内存的大小，可以使用`XX:MaxDirectMemorySize=大小`

单位k或K表示千字节，m或M表示兆字节，g或G表示千兆字节。默认不设置该参数情况下，JVM 自动选择 最大分配的大小。

以下示例以不同的单位说明如何将 直接内存大小设置为 1024 KB：

```Java
-XX:MaxDirectMemorySize=1m
-XX:MaxDirectMemorySize=1024k
-XX:MaxDirectMemorySize=1048576
```

在Idea中设置直接内存最大值为1g：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTBkZjRiM2I1OWZlNGVkMDI1M2FhYmQ3OGU0MzBhNGNfc0lpSkxTVTFFcExSTllzYzF2WjZjc3BIaDhDZDdhd3ZfVG9rZW46S0VVa2JDcDUyb2NmSkZ4UG9jRGM2YzltbmdkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

直接循环11次之后，超过最大值就会报错：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTMyMGEzMjQwZDI1ZTBiNmU4OWM0YzZjZWY2NmVmNWNfR01meG9QTTNpbU1mUWRyNG9Pd0M5UGdKS2FpZjJmT1ZfVG9rZW46S05peWI0ZnBUb25zcUl4dTZaQmMybnJtbmVQXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

# 4、垃圾回收

在C/C++这类没有自动垃圾回收机制的语言中，一个对象如果不再使用，需要手动释放，否则就会出现内存泄漏。

内存泄漏指的是不再使用的对象在系统中未被回收，内存泄漏的积累可能会导致内存溢出。   

在这段代码中，通过死循环不停创建Test类的对象，每一轮循环结束之后，这次创建的对象就不再使用了。但是没有手动调用删除对象的方法，此时对象就会出现内存泄漏。     

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTg0YjYxMjE0YjQ4MTNmMjg5MWU5OTcxYzM1MDJhMzNfc2xJUUl4ejU0SDZKakZIdjg3U0RhWUZIR3dzODVyd2lfVG9rZW46WnpTQ2JzT1htb3hTTTJ4Njg5NGMybWdWblliXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这段代码中，手动调用`delete`删除对象，就不会出现内存泄漏。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzYwODkyNWZhNDBmOTY1NzZiZDJjZTcwOTNmMmE5YTZfVTAwWmd4Z1lnSXh5ZHdiT3RpeVZQWGxTOWI2MDJMTndfVG9rZW46VURCNWJ5OVlHb2lQSG54RlZPR2N1RjFMbnZiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

我们称这种释放对象的过程为垃圾回收，而需要程序员编写代码进行回收的方式为手动回收。手动回收的方式相对来说回收比较及时，删除代码执行之后对象就被回收了，可以快速释放内存。缺点是对程序员要求比较高，很容易出现创建完对象之后，程序员忘记释放对象。

Java中为了简化对象的释放，引入了自动的垃圾回收（Garbage Collection简称GC）机制。通过垃圾回收器来对不再使用的对象完成自动的回收，垃圾回收器主要负责对堆上的内存进行回收。其他很多现代语言比如C#、Python、Go都拥有自己的垃圾回收器。

垃圾回收器如果发现某个对象不再使用，就可以回收该对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGYzMTEzOTU4ODFlZjMwMTAwODgxMGZkODZmODU5ZDZfN290WnJtZG04aE1TTWhsQVJ3U3pjM0dmc0tEazRyS1ZfVG9rZW46TkprR2JtVkJTb3M2OTJ4VktubmNocWJ6bnFiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=N2U2NTU1MmVlYWMxZDk5N2QwNGY4YjI5NWRlZDVmNmRfTlloVlhPakVNNjlESDl4VnlpNXNyOUVRcFpxdGNHSjJfVG9rZW46RGJLRWJjRURIb3U2bFd4WW16U2NUdlVHbmRkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- 自动垃圾回收，自动根据对象是否使用由虚拟机来回收对象
  - 优点：降低程序员实现难度、降低对象回收bug的可能性
  - 缺点：程序员无法控制内存回收的及时性
- 手动垃圾回收，由程序员编程实现对象的删除
  - 优点：回收及时性高，由程序员把控回收的时机
  - 缺点：编写不当容易出现悬空指针、重复释放、内存泄漏等问题

那么垃圾回收器需要负责对哪些部分的内存进行回收呢？

首先是线程不共享的部分，都是伴随着线程的创建而创建，线程的销毁而销毁。而方法的栈帧在执行完方法之后就会自动弹出栈并释放掉对应的内存。所以这一部分不需要垃圾回收器负责回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YWM4Yzk0MjViOTcxYjgyY2E5M2RhOWNiMzk3ZTZjMDJfQTUxeFNrZUpwaFlSWWkxQW9JeGhsTEdXZGF2WU1idWRfVG9rZW46S0pDdmJtOWFpb1JJMVB4UVY1MGNFVDFwbjNkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

## 4.1 方法区的回收

方法区中能回收的内容主要就是不再使用的类。

判定一个类可以被卸载。需要同时满足下面三个条件：

1、此类所有实例对象都已经被回收，在堆中不存在任何该类的实例对象以及子类对象。

这段代码中就将局部变量对堆上实例对象的引用去除了，所以对象就可以被回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWQ5NWZlYzllNjk0ZGE4MGFjMjYxZmYxOGJiMWFmZTNfYVdHd05Da3YzbFhFaE1kQnBycWlPd0xGUWVCTExMaElfVG9rZW46SWw4OGJRczhDb3N3SnN4S0gycGNTWHlJblJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、加载该类的类加载器已经被回收。

这段代码让局部变量对类加载器的引用去除，类加载器就可以回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGNhYzNiZGNhNzE3ZGJjY2IxOTI4ODE1YTUxZWNmMThfajZCSWt2MXpxNFdHZGNOSTlCY1ZwVFNBaVV6RkRkenpfVG9rZW46UW9NN2JHeDRRb3Bud214QThJNWNIY2hPbnNjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、该类对应的 java.lang.Class 对象没有在任何地方被引用。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDI5ZTRkYzU5MGE3ZGFmOTUwMzcxNmNiM2JiZTZkZWZfa3YycWVIcmFvYUFKSHJSM0tpVnhFTUZMeWRHd0V4eXNfVG9rZW46UDJ3TGJvM2NZb3V0OXd4YWtJdWN3bWV1bkNkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

代码:

```Java
package chapter04.gc;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * 类的卸载
 */
public class ClassUnload {
    public static void main(String[] args) throws InterruptedException {

        try {
            ArrayList<Class<?>> classes = new ArrayList<>();
            ArrayList<URLClassLoader> loaders = new ArrayList<>();
            ArrayList<Object> objs = new ArrayList<>();
            while (true) {

                URLClassLoader loader = new URLClassLoader(
                        new URL[]{new URL("file:D:\\lib\\")});
                Class<?> clazz = loader.loadClass("com.itheima.my.A");
                Object o = clazz.newInstance();


//                objs.add(o);
//                classes.add(clazz);
//                 loaders.add(loader);

                 System.gc();

            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
```

添加这两个虚拟机参数进行测试：

```Java
-XX:+TraceClassLoading -XX:+TraceClassUnloading
```

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzMxNmQ0ZGM3ZjkxY2M0Mzg2YzkwYTFmY2QyY2JmMDBfckVSS3ZmMDlrTHE3amJtM1pzdFBnNmlxejhDc3pCb1VfVG9rZW46SktOT2I2VWpCb1J5bTd4eXFQTWNYdlJBbmFiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果注释掉代码中三句add调用，就可以同时满足3个条件。但是需要手动调用`System.gc()`方法，让垃圾回收器进行回收。

> 如果需要手动触发垃圾回收，可以调用System.gc()方法。
>
> 语法： `System.gc()`
>
> 注意事项：
>
>    调用System.gc()方法并不一定会立即回收垃圾，仅仅是向Java虚拟机发送一个垃圾回收的请求，具体是否需要执行垃圾回收Java虚拟机会自行判断。

执行之后，日志中就会打印出类卸载的内容：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzczMmM0ZDk3MmM2MTY5NGQ2YTRiMjhkNjI1MmU5YzRfVDJWOUpnNFBIUVhsZk5VTVZaRWl6SGRkcFdvU0ZybkJfVG9rZW46Sko0V2JrRW94b04zbnB4aWxqcmNSb2hLbmVkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

那么类卸载主要用在什么场景下呢？

开发中此类场景一般很少出现，主要在如 OSGi、JSP 的热部署等应用场景中。

每个jsp文件对应一个唯一的类加载器，当一个jsp文件修改了，就直接卸载这个jsp类加载器。重新创建类加载器，重新加载jsp文件。

## 4.2 如何判断对象可以回收

垃圾回收器要回收对象的第一步就是判断哪些对象可以回收。Java中的对象是否能被回收，是根据对象是否被引用来决定的。如果对象被引用了，说明该对象还在使用，不允许被回收。

比如下面代码的内存结构图：

第一行代码执行之后，堆上创建了Demo类的实例对象，同时栈上保存局部变量引用堆上的对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZmVkY2U3ZWI1Zjk0MWQxMWYyYmEyMzk2M2Q5MTY1NWZfUnNjbTNmNTN0YzcyeGVBVGtnSzJNa2hVNGwxNWFtaXhfVG9rZW46VldpamJ0VUhNbzJGQVB4TFdDdWNVeUtRblNYXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

第二行代码执行之后，局部变量对堆上的对象引用去掉，那么堆上的对象就可以被回收了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MmE4ODhkMzQyNDg4Mzc5ODkwMmE3Mzg1OWJmZWQ1NjdfdmtjV0RTdjNYQ09qR2JFSmU1bldRUHR1cWZlZG8xdktfVG9rZW46VDZmS2JIcHVBb3g0d0h4NHoyY2NhdmhhbmtlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

一个更复杂的案例：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzI4YzVlOTAwNTRiMGFhYTU4Y2I1MWQ1MGUxNWQ1NDhfUGFIMktyRDJGUDJFSXgwRTJPdzVjcVlmcDB2VGtleU1fVG9rZW46WkVLcGIwNHVQb2UzZWR4TVR2RGNURlJxbm5jXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这个案例中，如果要让对象a和b回收，必须将局部变量到堆上的引用去除。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2EwNGI0OGQ1Nzc5NTUzNWQ1ZGFmYjAyZTdjMzViYTdfVEp1SmQ1YWpNMWxsQUJHQzFzY3BQTE40bk11ODFVZlVfVG9rZW46VDlPOWJUUDV6bzFRWnB4ZVZJQ2NIaFZLbnlmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

那么问题来了，A和B互相之间的引用需要去除吗？答案是不需要，因为局部变量都没引用这两个对象了，在代码中已经无法访问这两个对象，即便他们之间互相有引用关系，也不影响对象的回收。

判断对象是否可以回收，主要有两种方式：引用计数法和可达性分析法。

### 引用计数法

引用计数法会为每个对象维护一个引用计数器，当对象被引用时加1，取消引用时减1。

比如下图中，对象A的计数器初始为0，局部变量a1对它引用之后，计数器加1就变成了1。同样A对B产生了引用，B的计数器也是1。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OThjYzhjNWE4ZmVkODlhYmFiNjkzMGYzY2NhOGNjMzVfU29TamZvTUVCMm10ZUFNQU9lNUJFN2txVTJMYmk3TlpfVG9rZW46VnNpTGJuQnVSbzJGMEp4VjR5eGNRQmpibkZoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

引用计数法的优点是实现简单，C++中的智能指针就采用了引用计数法，但是它也存在缺点，主要有两点：

1.每次引用和取消引用都需要维护计数器，对系统性能会有一定的影响

2.存在循环引用问题，所谓循环引用就是当A引用B，B同时引用A时会出现对象无法回收的问题。

这张图上，由于A和B之间存在互相引用，所以计数器都为1，两个对象都不能被回收。但是由于没有局部变量对这两个代码产生引用，代码中已经无法访问到这两个对象，理应可以被回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzI1ZTFhZGYwMWJlOTg0ZTNhYmMzNWM5OGI2NTkwMzJfRm4wVzd1NnFEUlB3aFM5QjYyaFl4NXFNNjg0YzVQbjJfVG9rZW46TWNlQWJuQXNYb1RsOW14RzhKeWNjQUVhbjhlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

我们来做一个实验，验证下Java中循环引用不会导致内存泄漏，因为Java虚拟机根本没有使用引用计数法。首先我们要学会去看一个对象有没有被回收，可以通过垃圾回收日志来查看。

如果想要查看垃圾回收的信息，可以使用`-verbose:gc`参数。

语法： `-verbose:gc`

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Yjc3NzRkYTdmYTExMWU0M2U0MDBjMTQxZGFmNDk1ODBfd3c2VTVGc2o0Q0NMMUlLNk1MbGcyZWhyOTIzVExLS2VfVG9rZW46R2pFU2JCVkU2b09qTG94SUtsVGNQd2V2bldoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

加上这个参数之后执行代码，发现对象确实被回收了：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjFjZGUxOWE0MGY2ZDg2NjBkNmVkNTJhYjQ3MzM3NzRfbzNJRnd0MGliSm9VWmdVbENHSzdOZURFWURCZ3pnMkVfVG9rZW46Vm0wVGJFaG51b0FROUJ4OEVCUWNXN3gxbmtjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

通过不同的死循环创建对象，内存并没有上升，一直维持在1000K,说明每轮循环创建的两个对象在垃圾回收之后都被回收了。

### 可达性分析法

Java使用的是可达性分析算法来判断对象是否可以被回收。可达性分析将对象分为两类：垃圾回收的根对象（GC Root）和普通对象，对象与对象之间存在引用关系。

下图中A到B再到C和D，形成了一个引用链，可达性分析算法指的是如果从某个到GC Root对象是可达的，对象就不可被回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGQzZjdlN2M0OWIxMGY1M2QyZDYxYzZkMmJhN2RlMmJfVVlIUlVjUEphenpiRDljbkcwb09aN1ZxMjdjWUFJOW5fVG9rZW46RHo0NmJzazlab002NnJ4R2dLRmNYRUVYbjhmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

哪些对象被称之为GC Root对象呢？

- 线程Thread对象，引用线程栈帧中的方法参数、局部变量等。
- 系统类加载器加载的java.lang.Class对象，引用类中的静态变量。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGUzOGJkN2YzNzg0MmEwMDFhNzg4YzhmZGRhNTVhZTdfelYwVU9scDhhZkY2azUwYVUwMVpiNUh6aVVqU3htOHBfVG9rZW46UTdUcGI4OGpTb0VEZXR4M1Z2dmNZbXZqbndoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- 监视器对象，用来保存同步锁synchronized关键字持有的对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzJiMDBlNDM3ZTQ0M2UwZGI0MTUzODE2ZjdhZjBhMjJfTmVCaGFPVFg0R2ZmVkJNYXVMbUpxRjA4R1hXU0VmcXBfVG9rZW46RmpnbWI4TllGb2FRVDB4ZThmVWNDZjFVbnFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

- 本地方法调用时使用的全局对象。

通过arthas和eclipse Memory Analyzer (MAT) 工具可以查看GC Root，MAT工具是eclipse推出的Java堆内存检测工具。具体操作步骤如下：

1、使用arthas的heapdump命令将堆内存快照保存到本地磁盘中。

2、使用MAT工具打开堆内存快照文件。

3、选择GC Roots功能查看所有的GC Root。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OGZjZDc3NmE4MmNjYTRhODI4ZGJjYmFjNWYyMjFhMDZfZXE4VzBvZ2lTVEZwMWRIVmozTEk5TlBEMmJTNUtQU2dfVG9rZW46VHZ2WWJOa1prb0pJczR4ZkdROWNzRjJZbnlkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

步骤详解：

1、代码如下：

```Java
package com.itheima.jvm.chapter04;

import java.io.IOException;

public class ReferenceCounting {
    public static A a2 = null;
    public static void main(String[] args) throws IOException {
//        while (true){
            A a1 = new A();
            B b1 = new B();
            a1.b = b1;
            b1.a = a1;
            a2 = a1;
            System.in.read();
//            a1 = null;
//            b1 = null;
//        }

    }
}

class A {
    B b;
//    byte[] t = new byte[1024 * 1024 * 10];
}

class B {
    A a;
//    byte[] t = new byte[1024 * 1024 * 10];
}
```

2、使用arthas连接到程序，输入如下命令:

```Java
heapdump 目录/test2.hprof
```

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2E2MzI0ODBmOGNmMWY4MDU3NTQ5MDQyMTNjZjMwNjZfbTNVZkI1Nmdzd2h2OGo1ZFBFUXYzT3dINzU5dmdCVlJfVG9rZW46Vm1UZmJmaGhub05XNUJ4cUM3c2NtT21YblFkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这样就生成了一个堆内存快照（后面介绍，简单来说就是包含了所有堆中的对象信息）。

3、打开资料中提供的MAT工具，如果出现如下错误，请将环境变量中的JDK版本升级到17以上。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjI0ZjI0NDFiZTBkNDE0YTZlZWI2MjA5YzY5OWIwOTRfbjE5VkdSS0Z4OUpTeVdMTFc0MkhzQ2RuaTlORFh3WUVfVG9rZW46QkVWY2JseUs4b2Vkbm14WjJnWGN2eEo1bmZjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、选择菜单中的打开堆内存快照功能，并选择刚才生成的文件。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWRhZjVjY2ZkNjA4NzA4MzkzNDdjNjU2ZWFmMWQyYjNfWVphWVZjbnBsV0JtMDU5R3hZTXJpa1B5OFJFNFBJOFRfVG9rZW46SWNydWJyZHhKb0xFRzZ4VFc1M2NOa0pXbkhjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

5、选择内存泄漏检测报告，并确定。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2EwZjNkYjUwMTdjNzIxNDkyZmU5OTIwMTBlZDNhYTJfUTRuaGVMS2N2M3RTeDZ4OHM1b3IzY25mbFp6UzA2ckVfVG9rZW46RDdjWmJPUGQzb09hMjZ4bUJiVmNIOTFrbnRnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

6、通过菜单找到GC Roots。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODZiMjUwZDc5ZGZkM2M2OWVlMjQ4ODJmMmI1ODdkNTlfeHNWUDVBdXd1ZGlVNUd1eHVaSDRJWnkyV21hZVdmNmRfVG9rZW46RUdpV2I1d1NCb1p0b3B4OEhzdGM2emVwbngxXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

7、MAT对4类GC Root对象做了分类。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTQwMzNmZTI1YzAyYzU2NGY2ODlkODU2MGUwNTQ4MWVfYXVzQ0NjbWtEazdQdWhhakNMQk85NG0zRDEzZm0yNWZfVG9rZW46STU5OWJrQVMzb29hdjN4ZkF1RmNaSURTbkhkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

8、找到静态变量。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MDFjMGMxMDMyODkwMDlkY2M5YjY5MTg0NGY0NjExZjZfYnFIQWl1Mnd5Ukp2OVB0V1MxdEUxc0dRODV4eHk2VTdfVG9rZW46VGg4QmJWNEdpb0ZRamN4d1NIT2NQdDc3bk5iXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

9、找到局部变量

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTI4NGFlMTE5ZDNjMGI0NGM0NTIwOTEwMWYxNzg5NjNfRUR0TDVXT0VabzVXaEtJUFRwOG05TjM1Nm9NOFVzZ1RfVG9rZW46TG9yNmJ1aFA5b1B6UGh4R1N1T2Nja2trbkllXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

##  4.3 常见的引用对象

可达性算法中描述的对象引用，一般指的是强引用，即是GCRoot对象对普通对象有引用关系，只要这层关系存在，普通对象就不会被回收。除了强引用之外，Java中还设计了几种其他引用方式：

- 软引用
- 弱引用
- 虚引用
- 终结器引用

### 软引用

软引用相对于强引用是一种比较弱的引用关系，如果一个对象只有软引用关联到它，当程序内存不足时，就会将软引用中的数据进行回收。在JDK 1.2版之后提供了SoftReference类来实现软引用，软引用常用于缓存中。

如下图中，对象A被GC Root对象强引用了，同时我们创建了一个软引用SoftReference对象（它本身也是一个对象），软引用对象中引用了对象A。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Zjc3YjdlOWY5OTkzMjMwNzQxM2Q2MmJjZDRiOGQ5NWJfdEZseDJaSzlMYkVFM2twRmllY015YVdJOGFaZ0JxRzdfVG9rZW46WUVQN2I2MzF5bzNXN1N4MTdIN2NHaDZJbmRoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

接下来强引用被去掉之后，对象A暂时还是处于不可回收状态，因为有软引用存在并且内存还够用。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWFjZmY3MzE0N2M5Y2VjZDAxMmFmODJlNWNmMmE5ODlfVUp6VU1xblIxWlpHbEhObUdSZXMxdENWNXJuR0E4OWNfVG9rZW46VTBRN2JJQkRIb2hrTHZ4TmROMGNhS2NRbmtLXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

如果内存出现不够用的情况，对象A就处于可回收状态，可以被垃圾回收器回收。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGVmM2IxMzgxMDI5ODA4M2NkZDA3ZjZjZjIyYzg5NWFfVnd6cmZZNnZlMWF6ZFRpc3hpeUQ1SDhPVnoweGZnQzZfVG9rZW46WHBHc2JXVnlCb0dIUTJ4ZXpoQmNabUxNbmpmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这样做有什么好处？如果对象A是一个缓存，平时会保存在内存中，如果想访问数据可以快速访问。但是如果内存不够用了，我们就可以将这部分缓存清理掉释放内存。即便缓存没了，也可以从数据库等地方获取数据，不会影响到业务正常运行，这样可以减少内存溢出产生的可能性。

**特别注意：**

软引用对象本身，也需要被强引用，否则软引用对象也会被回收掉。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Yzc2MGNlMDkzNjYwNmY4M2Q2Y2UyZDI4ZWEyYTRiNjhfS2Y5WG5pbHl4V3k1eVNPWE5qQkhiYjluUERGTFNHZW1fVG9rZW46R1hlbWI4bFZFb0hLZGV4ZVRBZmM0SEcxbjZnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 软引用的使用方法

软引用的执行过程如下：

1.将对象使用软引用包装起来，new SoftReference<对象类型>(对象)。

2.内存不足时，虚拟机尝试进行垃圾回收。

3.如果垃圾回收仍不能解决内存不足的问题，回收软引用中的对象。

4.如果依然内存不足，抛出OutOfMemory异常。

代码：

```Java
/**
 * 软引用案例2 - 基本使用
 */
public class SoftReferenceDemo2 {
    public static void main(String[] args) throws IOException {

        byte[] bytes = new byte[1024 * 1024 * 100];
        SoftReference<byte[]> softReference = new SoftReference<byte[]>(bytes);
        bytes = null;
        System.out.println(softReference.get());

        byte[] bytes2 = new byte[1024 * 1024 * 100];
        System.out.println(softReference.get());
//
//        byte[] bytes3 = new byte[1024 * 1024 * 100];
//        softReference = null;
//        System.gc();
//
//        System.in.read();
    }
}
```

添加虚拟机参数，限制最大堆内存大小为200m：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NTUzYjJiYTAzOTdlMmQ0OWNhMTRkZGUwYWQzMWUyZTZfTmhnTEtuYmJDVDBLc2VoaDczTVY0am9ZYzFZUlE5RUNfVG9rZW46SDB3bmJkVHRWb2FSYTV4a2ZmV2Nib3k1bmpmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

执行后发现，第二个100m对象创建之后需，软引用中包含的对象已经被回收了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OWZhZTRhMGMwMzJiM2IwMjA3OTdjNmU0NzA0NGNkNmRfZVBRTjB5Q1JPMEpnTnBoNXVTbG1xMWJNRDh1U1ZrOXVfVG9rZW46RVd2UWJjVlRObzJhUld4NENVWmNRUnNEblFoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 软引用对象本身怎么回收呢？

如果软引用对象里边包含的数据已经被回收了，那么软引用对象本身其实也可以被回收了。

SoftReference提供了一套队列机制：

1、软引用创建时，通过构造器传入引用队列

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NWU4NjNiZWJhOTlmNDY0MmYyMGEwNDMyNWU3NDJkMGJfdHB4TGdVQzhyWEhYaFZNb2pUeEEyY1JkMVY1QmFjMG1fVG9rZW46UlllTmJxc0dWb3dreFJ4aEljMGNmc1pmbk1mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2、在软引用中包含的对象被回收时，该软引用对象会被放入引用队列

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MDZhMDc0M2I2NTI5NTFhN2UwN2QxM2ZiYzkzODk1YzRfWE5mUm45REdsS3hkdGFsbmNzUlVBNncxa040a0N0TnBfVG9rZW46TEhtZ2J1dllabzFyUGd4Z3lVQmN0OVBPbkhiXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3、通过代码遍历引用队列，将SoftReference的强引用删除

代码

```Java
/**
 * 软引用案例3 - 引用队列使用
 */
public class SoftReferenceDemo3 {

    public static void main(String[] args) throws IOException {

        ArrayList<SoftReference> softReferences = new ArrayList<>();
        ReferenceQueue<byte[]> queues = new ReferenceQueue<byte[]>();
        for (int i = 0; i < 10; i++) {
            byte[] bytes = new byte[1024 * 1024 * 100];
            SoftReference studentRef = new SoftReference<byte[]>(bytes,queues);
            softReferences.add(studentRef);
        }

        SoftReference<byte[]> ref = null;
        int count = 0;
        while ((ref = (SoftReference<byte[]>) queues.poll()) != null) {
            count++;
        }
        System.out.println(count);

    }
}
```

最终展示的结果是：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTM2MThhNGM5ZmZlZGIwYjYyZWQwYWJhYjUwOGI0MjFfVlZXbGJuWEJhc0VDUkRNSjVWT1B6QzRrdW5VeFl5SVRfVG9rZW46RVlDMmJ1Vll0b0J3MWh4cHBCNWNJdFIzbnlkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

这9个软引用对象中包含的数据已经被回收掉，所以可以手动从ArrayList中去掉，这样就可以释放这9个对象。

#### 软引用的缓存案例

使用软引用实现学生信息的缓存，能支持内存不足时清理缓存。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzljN2YxZGI1Njc0NTE4ODJhZTkxMzliNDhhYjVjOTlfTU1obUNtdU1PS3UyUnJ4TTRwM1g3NWd3OWJnYTVFcWpfVG9rZW46RUJDYmJDUmZFb2VZemR4NHJ0VmNiSmdlbnFmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

代码：

```Java
package chapter04.soft;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
/**
 * 软引用案例4 - 学生信息的缓存
 */
public class StudentCache {

    private static StudentCache cache = new StudentCache();

    public static void main(String[] args) {
        for (int i = 0; ; i++) {
            StudentCache.getInstance().cacheStudent(new Student(i, String.valueOf(i)));
        }
    }

    private Map<Integer, StudentRef> StudentRefs;// 用于Cache内容的存储
    private ReferenceQueue<Student> q;// 垃圾Reference的队列

    // 继承SoftReference，使得每一个实例都具有可识别的标识。
    // 并且该标识与其在HashMap内的key相同。
    private class StudentRef extends SoftReference<Student> {
        private Integer _key = null;

        public StudentRef(Student em, ReferenceQueue<Student> q) {
            super(em, q);
            _key = em.getId();
        }
    }

    // 构建一个缓存器实例
    private StudentCache() {
        StudentRefs = new HashMap<Integer, StudentRef>();
        q = new ReferenceQueue<Student>();
    }

    // 取得缓存器实例
    public static StudentCache getInstance() {
        return cache;
    }

    // 以软引用的方式对一个Student对象的实例进行引用并保存该引用
    private void cacheStudent(Student em) {
        cleanCache();// 清除垃圾引用
        StudentRef ref = new StudentRef(em, q);
        StudentRefs.put(em.getId(), ref);
        System.out.println(StudentRefs.size());
    }

    // 依据所指定的ID号，重新获取相应Student对象的实例
    public Student getStudent(Integer id) {
        Student em = null;
// 缓存中是否有该Student实例的软引用，如果有，从软引用中取得。
        if (StudentRefs.containsKey(id)) {
            StudentRef ref = StudentRefs.get(id);
            em = ref.get();
        }
// 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
// 并保存对这个新建实例的软引用
        if (em == null) {
            em = new Student(id, String.valueOf(id));
            System.out.println("Retrieve From StudentInfoCenter. ID=" + id);
            this.cacheStudent(em);
        }
        return em;
    }

    // 清除那些所软引用的Student对象已经被回收的StudentRef对象
    private void cleanCache() {
        StudentRef ref = null;
        while ((ref = (StudentRef) q.poll()) != null) {
            StudentRefs.remove(ref._key);
        }
    }

//    // 清除Cache内的全部内容
//    public void clearCache() {
//        cleanCache();
//        StudentRefs.clear();
//        //System.gc();
//        //System.runFinalization();
//    }
}

class Student {
    int id;
    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 弱引用

弱引用的整体机制和软引用基本一致，区别在于弱引用包含的对象在垃圾回收时，不管内存够不够都会直接被回收。在JDK 1.2版之后提供了WeakReference类来实现弱引用，弱引用主要在ThreadLocal中使用。

弱引用对象本身也可以使用引用队列进行回收。

```Java
package chapter04.weak;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 弱引用案例 - 基本使用
 */
public class WeakReferenceDemo2 {
    public static void main(String[] args) throws IOException {

        byte[] bytes = new byte[1024 * 1024 * 100];
        WeakReference<byte[]> weakReference = new WeakReference<byte[]>(bytes);
        bytes = null;
        System.out.println(weakReference.get());

        System.gc();

        System.out.println(weakReference.get());
    }
}
```

执行之后发现gc执行之后，对象已经被回收了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTI0ODdiY2ViNWVhYTU5OTZmOWEzZDA3Y2EwZWRmYmZfeWZURlF6YkNlOTBJS3lOalZaazMzdHljY0FJOTNneFRfVG9rZW46TXhCUWJrR2tTb1duZFl4dGJDUmNNQW5vbjdkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 虚引用和终结器引用

这两种引用在常规开发中是不会使用的。

- 虚引用也叫幽灵引用/幻影引用，不能通过虚引用对象获取到包含的对象。虚引用唯一的用途是当对象被垃圾回收器回收时可以接收到对应的通知。Java中使用PhantomReference实现了虚引用，直接内存中为了及时知道直接内存对象不再使用，从而回收内存，使用了虚引用来实现。

- 终结器引用指的是在对象需要被回收时，终结器引用会关联对象并放置在Finalizer类中的引用队列中，在稍后由一条由FinalizerThread线程从队列中获取对象，然后执行对象的finalize方法，在对象第二次被回收时，该对象才真正的被回收。在这个过程中可以在finalize方法中再将自身对象使用强引用关联上，但是不建议这样做。

```Java
package chapter04.finalreference;

/**
 * 终结器引用案例
 */
public class FinalizeReferenceDemo {
    public static FinalizeReferenceDemo reference = null;

    public void alive() {
        System.out.println("当前对象还存活");
    }

    @Override
    protected void finalize() throws Throwable {
        try{
            System.out.println("finalize()执行了...");
            //设置强引用自救
            reference = this;
        }finally {
            super.finalize();
        }
    }

    public static void main(String[] args) throws Throwable {
        reference = new FinalizeReferenceDemo();
       test();
       test();
    }

    private static void test() throws InterruptedException {
        reference = null;
        //回收对象
        System.gc();
        //执行finalize方法的优先级比较低，休眠500ms等待一下
        Thread.sleep(500);
        if (reference != null) {
            reference.alive();
        } else {
            System.out.println("对象已被回收");
        }
    }
}
```

## 4.4 垃圾回收算法

Java是如何实现垃圾回收的呢？简单来说，垃圾回收要做的有两件事：

1、找到内存中存活的对象

2、释放不再存活对象的内存，使得程序能再次利用这部分空间 

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDdiNjg0M2YyN2Q2NzkwZTY1ZjM4YWVlNTExNTg4ZDJfalNxYk8xYWdGdUdzV1pVa2RtSTM5V29uRGlOdE5yYzNfVG9rZW46V09oQWJQUERpbzJpaE54azZXZWNLbHhPbjNmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 垃圾回收算法的历史和分类

1960年John McCarthy发布了第一个GC算法：标记-清除算法。

1963年Marvin L. Minsky 发布了复制算法。

本质上后续所有的垃圾回收算法，都是在上述两种算法的基础上优化而来。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZDdlZmZmYTU1NDgwMzkyMDJmYWNjYjcxNzdjNGRjZGVfZjdQU2RWcXhpWVBVOGJTcmtNNzNPNWxvaWxMcm1zZ2JfVG9rZW46WHBlQWJTdmhvb3VBZVJ4TDJYeGM0eFBEblljXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 垃圾回收算法的评价标准

Java垃圾回收过程会通过单独的GC线程来完成，但是不管使用哪一种GC算法，都会有部分阶段需要停止所有的用户线程。这个过程被称之为Stop The World简称STW，如果STW时间过长则会影响用户的使用。

如下图，用户代码执行和垃圾回收执行让用户线程停止执行（STW）是交替执行的。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzJmZjAxZDkzY2YzNDgxZDg4YzdkZjJjOWVkOWY0NDBfWnJrVENGRjhIY2hlbkhneGtEUE5lWXBwS0p4Nmt5VllfVG9rZW46S3hjemJ0Njh0b3FGUGN4TFZCUGM1QlZKbk1lXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

所以判断GC算法是否优秀，可以从三个方面来考虑：

1.吞吐量

吞吐量指的是 CPU 用于执行用户代码的时间与 CPU 总执行时间的比值，即吞吐量 = 执行用户代码时间 /（执行用户代码时间 + GC时间）。吞吐量数值越高，垃圾回收的效率就越高。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzNlYTdhNzBmNDA1ZDU3NjE3ZWU0OTc4YzllMjBkZTVfRlFXSlNTc2RaT3ZRNkpkY0FPSXNjQjNrSERpYjdWc29fVG9rZW46Qm8zOGJRQXh4b1VoeWd4VE44Z2Ntekwzbm1aXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2.最大暂停时间

最大暂停时间指的是所有在垃圾回收过程中的STW时间最大值。比如如下的图中，黄色部分的STW就是最大暂停时间，显而易见上面的图比下面的图拥有更少的最大暂停时间。最大暂停时间越短，用户使用系统时受到的影响就越短。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzgzNWYwMDlkZTUzOTI2MzgyNTJhODNlYWJmNTU3YmRfaThEaXBaQmtmVjlLbUZkd1BrTVYzQ3Z1WkRnQ3N0MnJfVG9rZW46TlJQM2I0ako3bzBYWWN4UEIxUGM2bjV1bndlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3.堆使用效率

不同垃圾回收算法，对堆内存的使用方式是不同的。比如标记清除算法，可以使用完整的堆内存。而复制算法会将堆内存一分为二，每次只能使用一半内存。从堆使用效率上来说，标记清除算法要优于复制算法。

上述三种评价标准：堆使用效率、吞吐量，以及最大暂停时间不可兼得。

一般来说，堆内存越大，最大暂停时间就越长。想要减少最大暂停时间，就会降低吞吐量。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGRlMTE5NzIyNzc2NjAzZmNmZWIyYWYyODU3Yjc0MDBfNGxld1I4M2NlbVhVM2dFSEYxZGJublB3ZVIzVkpyeThfVG9rZW46QnJIQWJXQjhFb2NHUmh4dFpvQWMzOERzbkVlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

没有一个垃圾回收算法能兼顾上述三点评价标准，所以不同的垃圾回收算法它的侧重点是不同的，适用于不同的应用场景。

### 标记清除算法

标记清除算法的核心思想分为两个阶段：

1.标记阶段，将所有存活的对象进行标记。Java中使用可达性分析算法，从GC Root开始通过引用链遍历出所有存活对象。

2.清除阶段，从内存中删除没有被标记也就是非存活对象。

第一个阶段，从GC Root对象开始扫描，将对象A、B、C在引用链上的对象标记出来：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YTFlNjJmODI5NjFlMTQ0ODBkOWMxNzBhNzFlYWJjY2NfTGprSVlHZEd2cm0zTGNuMDVxb3liS05Ma05Fa3FRUVdfVG9rZW46UGNacWJRTE9Fb3VHd2x4VHhlQWNYNldobmxmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

第二个阶段，将没有标记的对象清理掉，所以对象D就被清理掉了。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjEyZDc3YWVmMjA0MmFkOTMyNjNjZTMxZDI0ZDM3MzdfY3hqU1g3YWIzdmg1dUljYzNlblI1ZEJMQ08zNVZCUVdfVG9rZW46WHBUZmJWbDFab2lDdzZ4MEJOMmNBYzF1bnBnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

优点：实现简单，只需要在第一阶段给每个对象维护标志位，第二阶段删除对象即可。

缺点：1.碎片化问题

由于内存是连续的，所以在对象被删除之后，内存中会出现很多细小的可用内存单元。如果我们需要的是一个比较大的空间，很有可能这些内存单元的大小过小无法进行分配。

如下图，红色部分已经被清理掉了，总共回收了9个字节，但是每个都是一个小碎片，无法为5个字节的对象分配空间。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZjY4ZTRmNjQ3NjQ3MzVlMTFmYmFhODc3ZjdiODQwNzhfQjRMZ3RkNjZkN0lCWm1YVkhna2Rvc20yaVNjRFB2cjdfVG9rZW46TEdhZWJldlV4b3EzckV4cjRJMWMwczQ4blVrXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

标记清除算法的缺点：

2.分配速度慢。由于内存碎片的存在，需要维护一个空闲链表，极有可能发生每次需要遍历到链表的最后才能获得合适的内存空间。 我们需要用一个链表来维护，哪些空间可以分配对象，很有可能需要遍历这个链表到最后，才能发现这块空间足够我们去创建一个对象。如下图，遍历到最后才发现有足够的空间分配3个字节的对象了。如果链表很长，遍历也会花费较长的时间。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NzhlNjgxN2EzNDgzMjg4Y2JjOWUwNzdlZGQwNGViZmZfd2VNV3MyUUJBS2J2Qmg5ZFV1UTAySk5pamo3Y2N5QUxfVG9rZW46V3VSS2J5U2tLbzhxWDB4cTRrV2NrV2Z3bmplXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 复制算法

复制算法的核心思想是：

1.准备两块空间From空间和To空间，每次在对象分配阶段，只能使用其中一块空间（From空间）。

对象A首先分配在From空间：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzliZDlhNDI2MTY2MDA5ODdiMjA4ZWQyOGViNWI5NGJfdk00bXhoWjROSUVKWVRRZG92Yk1Ra1E2U3pDRjk2cE9fVG9rZW46WlZpdmJoWjNsb05mdDB4dEtpU2NsdnljbmVjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

2.在垃圾回收GC阶段，将From中存活对象复制到To空间。

在垃圾回收阶段，如果对象A存活，就将其复制到To空间。然后将From空间直接清空。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODIwNWZlYmRlOWE2NTMzMTY3N2FkZTNlZDQ3NGYxYzZfeW1OZnRnOG9uYkNiZlk3YVFnREk1UUJ0SjBXZzJpcGtfVG9rZW46VXM0aGJoOWlnb0d2RWR4WG5wUWM2VERYbnRnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

3.将两块空间的From和To名字互换。

接下来将两块空间的名称互换，下次依然在From空间上创建对象。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDVmNDViZWQxZmViNWU2M2RjNTgyZWFkYWYwNzdhYjNfMDQ1N2hrVVVMWkcybVltZWJkV09PZDVZUFZtdml1N3dfVG9rZW46VXZqUWJoVVJjb1hoRHh4UWFjOWNaQzdNbjlnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

完整的复制算法的例子：

1.将堆内存分割成两块From空间 To空间，对象分配阶段，创建对象。

![img](assets/1777081809053-236.png)

2.GC阶段开始，将GC Root搬运到To空间

![img](assets/1777081809053-237.png)

3.将GC Root关联的对象，搬运到To空间

![img](assets/1777081809053-238.png)

4.清理From空间，并把名称互换

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NDE4N2FjNmQxYjQ4OTU3ZjZmMjQ0MjU3ODNhZmZkYThfd0NPcFdmczgwWGJkSFhTUTViY2MwQmU3M3NlZW9leHZfVG9rZW46UkZucWI3NXVpb3k2dWt4YkU2bmN5RHhubnJjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

优点：

- 吞吐量高，复制算法只需要遍历一次存活对象复制到To空间即可，比标记-整理算法少了一次遍历的过程，因而性能较好，但是不如标记-清除算法，因为标记清除算法不需要进行对象的移动
- 不会发生碎片化，复制算法在复制之后就会将对象按顺序放入To空间中，所以对象以外的区域都是可用空间，不存在碎片化内存空间。

缺点：

内存使用效率低，每次只能让一半的内存空间来为创建对象使用。

### 标记整理算法

标记整理算法也叫标记压缩算法，是对标记清理算法中容易产生内存碎片问题的一种解决方案。

核心思想分为两个阶段：

1.标记阶段，将所有存活的对象进行标记。Java中使用可达性分析算法，从GC Root开始通过引用链遍历出所有存活对象。

2.整理阶段，将存活对象移动到堆的一端。清理掉存活对象的内存空间。

![img](assets/1777081809053-240.png)

优点：

- 内存使用效率高，整个堆内存都可以使用，不会像复制算法只能使用半个堆内存
- 不会发生碎片化，在整理阶段可以将对象往内存的一侧进行移动，剩下的空间都是可以分配对象的有效空间

缺点：

整理阶段的效率不高，整理算法有很多种，比如Lisp2整理算法需要对整个堆中的对象搜索3次，整体性能不佳。可以通过Two-Finger、表格算法、ImmixGC等高效的整理算法优化此阶段的性能。

### 分代垃圾回收算法

现代优秀的垃圾回收算法，会将上述描述的垃圾回收算法组合进行使用，其中应用最广的就是分代垃圾回收算法(Generational GC)。

分代垃圾回收将整个内存区域划分为年轻代和老年代：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YjcwOTdmZTc1NzFjNDBkNjQwZmU1NmI1NmVlYTAxNDJfeXhLV2lxYzExZk1VaHpuUkIwNzNCR1VCTEpoZnZXN2VfVG9rZW46SUQya2J0amJZb2RqUFV4UnhEdmNNVVI5bk1lXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

我们通过arthas来验证下内存划分的情况：

在JDK8中，添加-XX:+UseSerialGC参数使用分代回收的垃圾回收器，运行程序。

在arthas中使用memory命令查看内存，显示出三个区域的内存情况。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MDg3MGU5NDkxYjFmOWQ0Mzg0YzcwOWVmZjZlZGE2ZTdfVEdLbDBEZ1ZFSkpOeWZ4Q3p3T1pEVHpBWEpBbU1Qb2ZfVG9rZW46UEswcGJVUGJlbzB5dzR4RWpkUGNNeXpxbk5mXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

Eden + survivor 这两块区域组成了年轻代。

tenured_gen指的是晋升区域，其实就是老年代。

另外还可以选择的虚拟机参数如下

| 参数名                        | 参数含义                                                     | 示例                                                    |
| ----------------------------- | ------------------------------------------------------------ | ------------------------------------------------------- |
| -Xms                          | 设置堆的最小和初始大小，必须是1024倍数且大于1MB              | 比如初始大小6MB的写法： -Xms6291456 -Xms6144k -Xms6m    |
| -Xmx                          | 设置最大堆的大小，必须是1024倍数且大于2MB                    | 比如最大堆80 MB的写法： -Xmx83886080 -Xmx81920k -Xmx80m |
| -Xmn                          | 新生代的大小                                                 | 新生代256 MB的写法： -Xmn256m -Xmn262144k -Xmn268435456 |
| -XX:SurvivorRatio             | 伊甸园区和幸存区的比例，默认为8 新生代1g内存，伊甸园区800MB,S0和S1各100MB | 比例调整为4的写法：-XX:SurvivorRatio=4                  |
| -XX:+PrintGCDetailsverbose:gc | 打印GC日志                                                   | 无                                                      |

代码：

```Java
package chapter04.gc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 垃圾回收器案例1
 */
//-XX:+UseSerialGC  -Xms60m -Xmn20m -Xmx60m -XX:SurvivorRatio=3  -XX:+PrintGCDetails
public class GcDemo0 {

    public static void main(String[] args) throws IOException {
        List<Object> list = new ArrayList<>();
        int count = 0;
        while (true){
            System.in.read();
            System.out.println(++count);
            //每次添加1m的数据
            list.add(new byte[1024 * 1024 * 1]);
        }
    }
}
```

使用arthas的memory展示出来的效果：

![img](assets/1777081809053-243.png)

heap展示的是可用堆，survivor区每次只有一块能使用，所以60 - 4 = 56m。

1、分代回收时，创建出来的对象，首先会被放入Eden伊甸园区。

![img](assets/1777081809053-244.png)

2、随着对象在Eden区越来越多，如果Eden区满，新创建的对象已经无法放入，就会触发年轻代的GC，称为Minor GC或者Young GC。

Minor GC会把需要eden中和From需要回收的对象回收，把没有回收的对象放入To区。

![img](assets/1777081809053-245.png)

3、接下来，S0会变成To区，S1变成From区。当eden区满时再往里放入对象，依然会发生Minor GC。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MGM2MGI2NWQxNDBjZjI4ZmQ3YWQ4ZjBhNGFjYzViOGRfdEZJeTFJZ3A0V0pKdE96U1ZBeTNERnBmZmhmVmlsM1NfVG9rZW46UE5DdGJPUHk1b3RYbVF4YnhyQWNnVng3blhkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

此时会回收eden区和S1(from)中的对象，并把eden和from区中剩余的对象放入S0。

注意：每次Minor GC中都会为对象记录他的年龄，初始值为0，每次GC完加1。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=M2U0ZmMwNTMwNGNiMzE0MGM2YmJhMTIyMmU2Yzg5ZDdfa3pjT0E2dlZhQXFPS0RGY095dHpPcGJxc1lmd09JQ3hfVG9rZW46QkdxMmJzaWdBb0xQVWZ4Ylo3NGNnMkFmbnVmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

4、如果Minor GC后对象的年龄达到阈值（最大15，默认值和垃圾回收器有关），对象就会被晋升至老年代。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=OTY4MGIzNmVlM2E5M2FhNjdmNDhhYTdmMGU1ZjExZmVfdEg5UENDazVCTDdxUHNDZDBxN0NETjRCaUROODdXeXFfVG9rZW46UFVNcmJHandxb0UzRGF4a1RlWmN6aEdobnFlXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

![img](assets/1777081809053-249.png)

当老年代中空间不足，无法放入新的对象时，先尝试minor gc如果还是不足，就会触发Full GC，Full GC会对整个堆进行垃圾回收。

如果Full GC依然无法回收掉老年代的对象，那么当对象继续放入老年代时，就会抛出Out Of Memory异常。

![img](assets/1777081809053-250.png)

下图中的程序为什么会出现OutOfMemory？

![img](assets/1777081809053-251.png)

从上图可以看到，Full GC无法回收掉老年代的对象，那么当对象继续放入老年代时，就会抛出Out Of Memory异常。

继续使用这段代码测试：

```Java
//-XX:+UseSerialGC  -Xms60m -Xmn20m -Xmx60m -XX:SurvivorRatio=3  -XX:+PrintGCDetails
public class GcDemo0 {

    public static void main(String[] args) throws IOException {
        List<Object> list = new ArrayList<>();
        int count = 0;
        while (true){
            System.in.read();
            System.out.println(++count);
            //每次添加1m的数据
            list.add(new byte[1024 * 1024 * 1]);
        }
    }
}
```

结果如下：

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzM3ODg2OTZkNzRmY2VjOTdlMzY3YzQ0MDUwMjA1MGFfaVlkemRKb0lTN3RjM2RCRmtHa1NkRUJqS1hoOFYyaDFfVG9rZW46WTN3RWJiRFBtb2hjYUd4cmVmT2Nqc21Lbk4yXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

老年代已经满了，而且垃圾回收无法回收掉对象，如果还想往里面放就发生了`OutOfMemoryError`。

## 4.5 垃圾回收器

为什么分代GC算法要把堆分成年轻代和老年代？首先我们要知道堆内存中对象的特性：

- 系统中的大部分对象，都是创建出来之后很快就不再使用可以被回收，比如用户获取订单数据，订单数据返回给用户之后就可以释放了。
- 老年代中会存放长期存活的对象，比如Spring的大部分bean对象，在程序启动之后就不会被回收了。
- 在虚拟机的默认设置中，新生代大小要远小于老年代的大小。

分代GC算法将堆分成年轻代和老年代主要原因有：

1、可以通过调整年轻代和老年代的比例来适应不同类型的应用程序，提高内存的利用率和性能。

2、新生代和老年代使用不同的垃圾回收算法，新生代一般选择复制算法，老年代可以选择标记-清除和标记-整理算法，由程序员来选择灵活度较高。

3、分代的设计中允许只回收新生代（minor gc），如果能满足对象分配的要求就不需要对整个堆进行回收(full gc),STW时间就会减少。

垃圾回收器是垃圾回收算法的具体实现。

由于垃圾回收器分为年轻代和老年代，除了G1之外其他垃圾回收器必须成对组合进行使用。

具体的关系图如下：

![img](assets/1777081809053-253.png)

### 年轻代-Serial垃圾回收器

Serial是是一种单线程串行回收年轻代的垃圾回收器。

![img](assets/1777081809053-254.png)

**回收年代和算法：**

年轻代

复制算法

**优点**

单CPU处理器下吞吐量非常出色

**缺点**

多CPU下吞吐量不如其他垃圾回收器，堆如果偏大会让用户线程处于长时间的等待

**适用场景**

Java编写的客户端程序或者硬件配置有限的场景

### 老年代-SerialOld垃圾回收器

SerialOld是Serial垃圾回收器的老年代版本，采用单线程串行回收

-XX:+UseSerialGC 新生代、老年代都使用串行回收器。

![img](assets/1777081809053-255.png)

**回收年代和算法：**

老年代

标记-整理算法

**优点**

单CPU处理器下吞吐量非常出色

**缺点**

多CPU下吞吐量不如其他垃圾回收器，堆如果偏大会让用户线程处于长时间的等待

**适用场景**

与Serial垃圾回收器搭配使用，或者在CMS特殊情况下使用

### 年轻代-ParNew垃圾回收器

ParNew垃圾回收器本质上是对Serial在多CPU下的优化，使用多线程进行垃圾回收

-XX:+UseParNewGC 新生代使用ParNew回收器， 老年代使用串行回收器

![img](assets/1777081809053-256.png)

**回收年代和算法：**

年轻代

复制算法

**优点**

多CPU处理器下停顿时间较短

**缺点**

吞吐量和停顿时间不如G1，所以在JDK9之后不建议使用

**适用场景**

 JDK8及之前的版本中，与CMS老年代垃圾回收器搭配使用

### 老年代- CMS(Concurrent Mark Sweep)垃圾回收器

CMS垃圾回收器关注的是系统的暂停时间，允许用户线程和垃圾回收线程在某些步骤中同时执行，减少了用户线程的等待时间。

参数：XX:+UseConcMarkSweepGC

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODkwNmYzMjJmZjUzMDM4MDhiNzYyYjI2ZmE5MDZkNTVfVXNYbmIzQU5ha2NvbzNLeVNrREZjdnBKa09qV2hLYXFfVG9rZW46UUJSTmJRUTJab2tVWVp4dTdKZ2NHME14bkJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

**回收年代和算法：**

老年代

标记清除算法

**优点**

系统由于垃圾回收出现的停顿时间较短，用户体验好

**缺点**

1、内存碎片问题

2、退化问题

3、浮动垃圾问题

**适用场景**

 大型的互联网系统中用户请求数据量大、频率高的场景，比如订单接口、商品接口等

CMS执行步骤：

1.初始标记，用极短的时间标记出GC Roots能直接关联到的对象。

2.并发标记,   标记所有的对象，用户线程不需要暂停。

3.重新标记，由于并发标记阶段有些对象会发生了变化，存在错标、漏标等情况，需要重新标记。

4.并发清理，清理死亡的对象，用户线程不需要暂停。

缺点：

1、CMS使用了标记-清除算法，在垃圾收集结束之后会出现大量的内存碎片，CMS会在Full GC时进行碎片的整理。这样会导致用户线程暂停，可以使用-XX:CMSFullGCsBeforeCompaction=N 参数（默认0）调整N次Full GC之后再整理。

2.、无法处理在并发清理过程中产生的“浮动垃圾”，不能做到完全的垃圾回收。

3、如果老年代内存不足无法分配对象，CMS就会退化成Serial Old单线程回收老年代。

并发线程数：

在CMS中并发阶段运行时的线程数可以通过-XX:ConcGCThreads参数设置，默认值为0，由系统计算得出。

计算公式为(-XX:ParallelGCThreads定义的线程数 + 3) / 4， ParallelGCThreads是STW停顿之后的并行线程数

ParallelGCThreads是由处理器核数决定的：

  1、当cpu核数小于8时，ParallelGCThreads = CPU核数

  2、否则 ParallelGCThreads = 8 + (CPU核数 – 8 )*5/8 

我的电脑上逻辑处理器有12个，所以ParallelGCThreads  =  8 + （12 - 8）* 5/8 = 10，ConcGCThreads = (-XX:ParallelGCThreads定义的线程数 + 3) / 4 = （10 + 3） / 4 = 3

![img](assets/1777081809054-258.png)

最终可以得到这张图：

![img](assets/1777081809054-259.png)

并发标记和并发清理阶段，会使用3个线程并行处理。重新标记阶段会使用10个线程处理。

由于CPU的核心数有限，并发阶段会影响用户线程执行的性能。

![img](assets/1777081809054-260.png)

### 年轻代-Parallel Scavenge垃圾回收器

Parallel Scavenge是JDK8默认的年轻代垃圾回收器，多线程并行回收，关注的是系统的吞吐量。具备自动调整堆内存大小的特点。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NGZjYWZiMTNiNjkzM2RjNTU2OGFhZTVlNWYyNDI4YzNfWEZYWHZYQm5hcE4xcTlrOEhKZk1UR0xSWUx3MElZU3hfVG9rZW46UHRvM2I2N2wwb29nQTV4ZjhBWWM4UFdPbmxoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

**回收年代和算法：**

年轻代

复制算法

**优点**

吞吐量高，而且手动可控。为了提高吞吐量，虚拟机会动态调整堆的参数

**缺点**

不能保证单次的停顿时间

**适用场景**

后台任务，不需要与用户交互，并且容易产生大量的对象。比如：大数据的处理，大文件导出

**常用参数：**

Parallel Scavenge允许手动设置最大暂停时间和吞吐量。Oracle官方建议在使用这个组合时，不要设置堆内存的最大值，垃圾回收器会根据最大暂停时间和吞吐量自动调整内存大小。

- 最大暂停时间，`-XX:MaxGCPauseMillis=n` 设置每次垃圾回收时的最大停顿毫秒数
- 吞吐量，`-XX:GCTimeRatio=n` 设置吞吐量为n（用户线程执行时间 = n/n + 1）
- 自动调整内存大小, `-XX:+UseAdaptiveSizePolicy`设置可以让垃圾回收器根据吞吐量和最大停顿的毫秒数自动调整内存大小

### 老年代-Parallel Old垃圾回收器

Parallel Old是为Parallel Scavenge收集器设计的老年代版本，利用多线程并发收集。

参数： -XX:+UseParallelGC  或

​           -XX:+UseParallelOldGC可以使用Parallel Scavenge + Parallel Old这种组合。

![img](assets/1777081809054-262.png)

**回收年代和算法：**

老年代

标记-整理算法

**优点**

并发收集，在多核CPU下效率较高

**缺点**

暂停时间会比较长

**适用场景**

与Parallel Scavenge配套使用

### G1垃圾回收器

JDK9之后默认的垃圾回收器是G1（Garbage First）垃圾回收器。Parallel Scavenge关注吞吐量，允许用户设置最大暂停时间 ，但是会减少年轻代可用空间的大小。CMS关注暂停时间，但是吞吐量方面会下降。

而G1设计目标就是将上述两种垃圾回收器的优点融合：

1.支持巨大的堆空间回收，并有较高的吞吐量。

2.支持多CPU并行垃圾回收。

3.允许用户设置最大暂停时间。

JDK9之后强烈建议使用G1垃圾回收器。

G1出现之前的垃圾回收器，年轻代和老年代一般是连续的，如下图：

![img](assets/1777081809054-263.png)

G1的整个堆会被划分成多个大小相等的区域，称之为区Region，区域不要求是连续的。分为Eden、Survivor、Old区。Region的大小通过堆空间大小/2048计算得到，也可以通过参数-XX:G1HeapRegionSize=32m指定(其中32m指定region大小为32M)，Region size必须是2的指数幂，取值范围从1M到32M。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YzE5M2Q1NDUxYjNjNjAwYWU5Yzk4YTQ2Y2M4ZDE0OThfME1TVWVaVnVvZ1h0S1dxa0dCQlh0RDd2VVpoOENraURfVG9rZW46Tjlnd2Iyb2o1b05TcWZ4VmZRamNqT0wwbjNjXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

G1垃圾回收有两种方式：

1、年轻代回收（Young GC）

2、混合回收（Mixed GC）

#### 年轻代回收

年轻代回收（Young GC），回收Eden区和Survivor区中不用的对象。会导致STW，G1中可以通过参数

-XX:MaxGCPauseMillis=n（默认200）  设置每次垃圾回收时的最大暂停时间毫秒数，G1垃圾回收器会尽可能地保证暂停时间。

1、新创建的对象会存放在Eden区。当G1判断年轻代区不足（max默认60%），无法分配对象时需要回收时会执行Young GC。

![img](assets/1777081809054-265.png)

2、标记出Eden和Survivor区域中的存活对象，

3、根据配置的最大暂停时间选择某些区域将存活对象复制到一个新的Survivor区中（年龄+1），清空这些区域。

![img](assets/1777081809054-266.png)

![img](assets/1777081809054-267.png)

G1在进行Young GC的过程中会去记录每次垃圾回收时每个Eden区和Survivor区的平均耗时，以作为下次回收时的参考依据。这样就可以根据配置的最大暂停时间计算出本次回收时最多能回收多少个Region区域了。

比如 -XX:MaxGCPauseMillis=n（默认200），每个Region回收耗时40ms，那么这次回收最多只能回收4个Region。

4、后续Young GC时与之前相同，只不过Survivor区中存活对象会被搬运到另一个Survivor区。

![img](assets/1777081809054-268.png)

![img](assets/1777081809054-269.png)

5、当某个存活对象的年龄到达阈值（默认15），将被放入老年代。

![img](assets/1777081809054-270.png)

6、部分对象如果大小超过Region的一半，会直接放入老年代，这类老年代被称为Humongous区。比如堆内存是4G，每个Region是2M，只要一个大对象超过了1M就被放入Humongous区，如果对象过大会横跨多个Region。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTRjMjFmY2EwMzZkZmIxMGE0Y2Q4ZDZhZTA1OTM2OTBfSXMzY3NCOEV6OGJ0UjZiSHhzVENhWjlSUlAyd0s0T0RfVG9rZW46VDhrWmJGdzhjb0Z3aFd4WDZFUWNYaHVBbm9lXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

7、多次回收之后，会出现很多Old老年代区，此时总堆占有率达到阈值时

（-XX:InitiatingHeapOccupancyPercent默认45%）会触发混合回收MixedGC。回收所有年轻代和部分老年代的对象以及大对象区。采用复制算法来完成。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ZTU5ZTAyOTMzM2JlYTg1ZGNlYmE4OTQwNjc2MGIwZGZfMWdEYzY0bUdncVMyU1UzMHZpV2NYSHI5UDlDSWU0RTBfVG9rZW46RWN3M2JiWGhKb1FEazB4NUY0R2NNYnpubktnXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=NjJlZTQxODA4ZWFiMWRkNzU3NmY0ODU1ZGJhMDFhZWNfVkZXUE9OS0VqU1pJUGRMdEJ1QTZqc0JadnFIUGlyY2FfVG9rZW46RDAzNGJHTUVJb2JHTHd4MFdtQWNTUlJubnBmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

#### 混合回收

混合回收分为：初始标记（initial mark）、并发标记（concurrent mark）、最终标记（remark或者Finalize Marking）、并发清理（cleanup）

G1对老年代的清理会选择存活度最低的区域来进行回收，这样可以保证回收效率最高，这也是G1（Garbage first）名称的由来。

   

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=ODExMjc2N2UxZjE0ODU1NmU2ZmY5NDQ0Y2NhZjIyNTBfNEN3dGZzS25uZ3FLS1lwWVNIRW51QWNaQ2Y5M1EzYlNfVG9rZW46TEZMYWJoYjFZb1k5cDJ4NHl4NGNvdDlvbkRkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

G1对老年代的清理会选择存活度最低的区域来进行回收，这样可以保证回收效率最高，这也是G1（Garbage first）名称的由来。最后清理阶段使用复制算法，不会产生内存碎片。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzFjOTI4ZTA1MTMxMWY4YjI2YjUyNTBjZWEwNGUwOTdfQTloMDFoSGZQUHNqdUtpTVFreUZLYUxKOGJpQ0Q4TFJfVG9rZW46T1gya2JVdFA3b3lNV3F4YVhxQmNJZUwzbmpoXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

注意：如果清理过程中发现没有足够的空Region存放转移的对象，会出现Full GC。单线程执行标记-整理算法，此时会导致用户线程的暂停。所以尽量保证应该用的堆内存有一定多余的空间。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzJiNjZjMjJmNWJlYTRkNzE0YzEwMWMyN2QzNTczNDlfQmM2ZUY2TlFwS3A5M1lsNERLQ2RBY3hjdFBRcVJrSjlfVG9rZW46R2lFY2J1VE1Rb1owOWV4Q2x4d2NZVGlIblJkXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### G1 – Garbage First 垃圾回收器

参数1： `-XX:+UseG1GC`  打开G1的开关，JDK9之后默认不需要打开

参数2：`-XX:MaxGCPauseMillis=毫秒值` 最大暂停的时

**回收年代和算法：**

年轻代+老年代

复制算法

**优点**

对比较大的堆如超过6G的堆回收时，延迟可控

不会产生内存碎片

并发标记的SATB算法效率高

**缺点**

JDK8之前还不够成熟

**适用场景**

JDK8最新版本、JDK9之后建议默认使用

使用以下代码测试g1垃圾回收器，打印出每个阶段的时间:

```Java
package chapter04.gc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 垃圾回收器案例3
 */
//-XX:+UseG1GC   -Xmn8g -Xmx16g -XX:SurvivorRatio=8  -XX:+PrintGCDetails -verbose:gc 
public class GcDemo2 {

    public static void main(String[] args) throws IOException {
        int count = 0;
        List<Object> list = new ArrayList<>();
        while (true){
            //System.out.println(++count);
            if(count++ % 10240 == 0){
                list.clear();
            }
//            byte[] bytes = new byte[1024 * 1024 * 1];
            list.add(new byte[1024 * 1024 * 1 / 2]);
//            System.gc();
        }
    }
}
```

每个region大小为2m，一共有84个young区，26个幸存者区。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=Y2FlOGRhNDRlM2UyMzdiN2EwZGI1N2NmOWY4YjE2OGRfRkVheU5WZmN5MU5XeWc3SzR1anQ3YWdXRXNuZkRiaEVfVG9rZW46UERmM2J0OVo3b3lqQWt4Y3hQamM2QmtObk5nXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

初始标记花了0.0478秒。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=YmM4ZmYxZWI2M2YyZmVjNDQ5ZDFjMzMzMGFhNWU1ODdfUkM3SWJKSVprNkk3elJ2eVhST01ZZzJncmRicENYM1NfVG9rZW46Rk1vSGJieVdFb282d1d4YUpNQ2NCa2g1bmxmXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

并发标记总共耗时10ms，不会产生STW。

![img](https://lisxpq12rl7.feishu.cn/space/api/box/stream/download/asynccode/?code=MzY3ZTZlYWI4OWZiOWExODQ0YzVmNWE4MWU2MmZjNzdfcWV4V1RHTEs0VktRZm9yaVhNQ2JsNUdLR0hxRjhHSkVfVG9rZW46T1pSaWJ1MlNPb1FXRVJ4Q29zTWNwcmJZbm1jXzE3NzcwODE3Njk6MTc3NzA4NTM2OV9WNA)

### 总结

垃圾回收器的组合关系虽然很多，但是针对几个特定的版本，比较好的组合选择如下：

JDK8及之前：

ParNew + CMS（关注暂停时间）、Parallel Scavenge + Parallel Old (关注吞吐量)、 G1（JDK8之前不建议，较大堆并且关注暂停时间）

JDK9之后:

G1（默认）

从JDK9之后，由于G1日趋成熟，JDK默认的垃圾回收器已经修改为G1，所以强烈建议在生产环境上使用G1。

G1的实现原理将在《原理篇》中介绍，更多前沿技术ZGC、GraalVM将在《高级篇》中介绍。