> 这是本人听了狂神和黑马满老师的课程(强推[满老师的课程](https://www.bilibili.com/video/BV16J411h7Rd)) 之后重构出的笔记 ，其中面试高频部分已高亮

> 推荐使用typora阅读，ctrl + shift + L 可以打开目录



**面试：单例模式、排序算法、生产者和消费者、死锁问题**





[JUC并发编程笔记（狂神说） - 你我不在年少 - 博客园](https://www.cnblogs.com/th11/p/15330675.html)

# 1. 什么是JUC

JUC就是java.util.concurrent下面的类包，专门用于多线程的开发。

**源码 + 官方文档 面试高频问 **![在这里插入图片描述](https://img-blog.csdnimg.cn/20210129235524770.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

java.util 工具包

- 业务：无法通过普通的线程代码 Thread实现。
- Runnable 没有返回值、效率相比于Callable相对较低 
- **企业开发中Callable 使用较多**
  Callable ：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210129235638790.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

锁：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210129235646413.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

# 2、线程和进程

> 进程是操作系统中的应用程序、是资源分配的基本单位，线程是用来执行具体的任务和功能，是CPU调度和分派的最小单位
>
> 一个进程往往可以包含多个线程，至少包含一个



## 相关概念

### 进程

**一个程序(QQ.EXE Music.EXE)；程序的集合**
一个进程可以包含多个线程，至少包含一个线程 
Java默认有几个线程：2个线程   main线程、GC线程

### 线程

**开了一个进程Typora，写字，等待几分钟会进行自动保存(线程负责的)**
对于Java而言：Thread、Runable、Callable进行开启线程的。

**提问？JAVA真的可以开启线程吗？ 开不了的 **

Java是没有权限去开启线程、操作硬件的，这是一个native的一个本地方法，它调用的底层的C++代码。

```java
    public synchronized void start() {
        /**
         * This method is not invoked for the main method thread or "system"
         * group threads created/set up by the VM. Any new functionality added
         * to this method in the future may have to also be added to the VM.
         *
         * A zero status value corresponds to state "NEW".
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        /* Notify the group that this thread is about to be started
         * so that it can be added to the group's list of threads
         * and the group's unstarted count can be decremented. */
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                /* do nothing. If start0 threw a Throwable then
                  it will be passed up the call stack */
            }
        }
    }
	//这是一个本地方法，Java是没有权限操作底层硬件的
    private native void start0();
```



==**两者的区别**==

* 一个进程可以有多个线程，进程是资源分配的基本单位，线程是CPU调度的基本单位
* 进程之间互相隔离，线程之间共享进程的资源
* 进程间通信(IPC)复杂
  线程间通信简单，因为共享进程内的内存
* 进程进程创建和销毁开销大、上下文切换成本高
  线程轻量，创建和销毁开销小、上下文切换成本低
* 



### 并发

> concurrent: 同一时间**应对**多件事情的能力

多线程操作同一个资源。

- CPU 只有一核，通过时间片轮转，来模拟多线程。
- 并发编程的本质：**充分利用CPU的资源 **



### 并行

> parallel: 同一时间动手**做**多件事情的能力

**并行：** 多个线程一起工作(多个人一起行走)

- CPU多核，多个线程可以同时执行。 我们可以使用线程池

**获取cpu的核数**

```java
public class Test1 {
    public static void main(String[] args) {
        //获取cpu的核数
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
```



> 多线程程序处理同一系列任务，是否比单线程效率更高？
>
> * 单核CPU：效率更低，因为有上下文切换开销(但是多线程能避免饥饿现象)
> * 多核CPU：效率更高，因为可以把多个线程映射到不同内核上同时执行

> 补充知识：
>
> IO操作不占用CPU，但是阻塞IO会阻塞线程；  优化：非阻塞IO/异步IO





## ==线程的状态==

> Java API层面

```java
public enum State {	
	//新生
    NEW,

	//运行
    RUNNABLE,

	//阻塞
    BLOCKED,

	//等待
    WAITING,

	//超时等待
    TIMED_WAITING,

	//终止
    TERMINATED;
}
```

> **Java 的线程状态是对操作系统线程状态的一种抽象和简化**
>
> 其中:
>
> RUNNABLE对应就绪态 运行态 阻塞态(由BIO导致的线程阻塞)
>
> WAITING与TIMED_WAITING也对应阻塞态（是 Java API 层面对阻塞态的细分）
>
> | Java 状态     | 对应的 OS 状态                    | 说明                                                         |
> | ------------- | --------------------------------- | ------------------------------------------------------------ |
> | NEW           | 创建态                            | 线程对象已创建，但未调用 `start()`，OS 尚未为其分配资源。    |
> | RUNNABLE      | 就绪态 (Ready) + 运行态 (Running) | 这是一个“组合态”。只要线程在等待 CPU 调度（就绪）或正在 CPU 上执行（运行），Java 都将其视为 `RUNNABLE`。 |
> | BLOCKED       | 阻塞态 (Blocked)                  | 仅指等待获取 `synchronized` 监视器锁的线程。此时线程不占用 CPU，等待特定资源（锁）可用。 |
> | WAITING       | 阻塞态 (Blocked)                  | 线程无限期等待另一个线程执行特定操作（如 `notify` 或 `join` 结束）。 |
> | TIMED_WAITING | 阻塞态 (Blocked)                  | 带有时间限制的等待（如 `sleep`, `wait(timeout)`），超时后自动唤醒。 |
> | TERMINATED    | 终止态                            | 线程执行完毕，生命周期结束。                                 |
>
> #### **BLOCKED vs WAITING：同样是“等”，等的内容不同**
>
> 这是最容易混淆的地方，也是 Java 状态机比 OS 状态更细腻的地方：
>
> - BLOCKED (Java) = 等“钥匙”
>   - **场景：** 你想进 `synchronized` 房间，但门被锁了（锁被别人持有）。
>   - **特点：** 你（线程）在门口排队，只有等里面的人把钥匙（锁）扔出来，你才能进去。你不知道里面发生了什么，只知道门打不开。
>   - **OS 层面：** 这也是一种阻塞（等待资源）。
> - WAITING / TIMED_WAITING (Java) = 等“通知”
>   - **场景：** 你在房间里，调用了 `wait()` 或 `join()` 或 `LockSupport.park()`。
>   - **特点：** 你主动放弃了钥匙（释放了锁），并且进入了“休息室”（等待队列）。你不再参与锁的竞争，直到有人喊你名字（`notify`）或者时间到了，你才会出来重新抢钥匙。
>   - **OS 层面：** 这也是一种阻塞（等待事件/条件）。



### 线程状态转换

![image-20260131153549640](assets/image-20260131153549640.png)

假设有线程 `Thread t` 

#### 情况 1 `NEW --> RUNNABLE`

* 当调用 `t.start()` 方法时，由 `NEW --> RUNNABLE` 



#### 情况 2 `RUNNABLE <--> WAITING` 

t 线程用 `synchronized(obj)` 获取了对象锁后 

* 调用 `obj.wait()` 方法时，t 线程从 `RUNNABLE --> WAITING` 
* 调用 `obj.notify()` ， `obj.notifyAll()` ， `t.interrupt()` 时 
  * 竞争锁成功，t 线程从 `WAITING --> RUNNABLE`
  * 竞争锁失败，t 线程从 `WAITING --> BLOCKED`

```java
public class TestWaitNotify {
    final static Object obj = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码...."); // 断点
            }
        },"t1").start();
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行....");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其它代码...."); // 断点
            }
        },"t2").start();

        sleep(0.5);
        log.debug("唤醒 obj 上其它线程");
        synchronized (obj) {
            obj.notifyAll(); // 唤醒obj上所有等待线程 断点
        }
    }
}
```



#### 情况 3 `RUNNABLE <--> WAITING` 

* 当前线程调用 `t.join()` 方法时，当前线程从 `RUNNABLE --> WAITING` 
  * 注意是当前线程在t 线程对象的监视器上等待 
* t 线程运行结束，或调用了当前线程的 `interrupt()` 时，当前线程从 `WAITING --> RUNNABLE`



#### 情况 4 `RUNNABLE <--> WAITING` 

* 当前线程调用 `LockSupport.park()` 方法会让当前线程从 `RUNNABLE --> WAITING` 
* 调用 `LockSupport.unpark(目标线程)` 或调用了线程 的 `interrupt()` ，会让目标线程从 `WAITING --> RUNNABLE`



#### 情况 5 `RUNNABLE <--> TIMED_WAITING`

t 线程用 `synchronized(obj)` 获取了对象锁后 

* 调用 `obj.wait(long n)` 方法时，t 线程从 `RUNNABLE --> TIMED_WAITING` 
* t 线程等待时间超过了 n 毫秒，或调用 `obj.notify()` ， `obj.notifyAll()` ， `t.interrupt()` 时 
  * 竞争锁成功，t 线程从 `TIMED_WAITING --> RUNNABLE` 
  * 竞争锁失败，t 线程从 `TIMED_WAITING --> BLOCKED`



#### 情况 6 `RUNNABLE <--> TIMED_WAITING`

* **当前线程调用 `t.join(long n)` 方法**时，当前线程从 `RUNNABLE --> TIMED_WAITING` 
  * 注意是当前线程在t 线程对象的监视器上等待 
* 当前线程等待时间超过了 n 毫秒，或t 线程运行结束，或调用了当前线程的 `interrupt()` 时，当前线程从 `TIMED_WAITING --> RUNNABLE`



#### 情况 7 `RUNNABLE <--> TIMED_WAITING`

* **当前线程调用 `Thread.sleep(long n)`** ，当前线程从 `RUNNABLE --> TIMED_WAITING` 
* 当前线程等待时间超过了 n 毫秒，当前线程从 `TIMED_WAITING --> RUNNABLE`



#### 情况 8 `RUNNABLE <--> TIMED_WAITING`

* **当前线程调用 `LockSupport.parkNanos(long nanos)` 或 `LockSupport.parkUntil(long millis)`** 时，当前线 程从 `RUNNABLE --> TIMED_WAITING`
* 调用 `LockSupport.unpark(目标线程)` 或调用了线程 的 `interrupt()` ，或是等待超时，会让目标线程从 `TIMED_WAITING--> RUNNABLE`





#### 情况 9 `RUNNABLE <--> BLOCKED`

* t 线程用 `synchronized(obj)` **获取对象锁**时如果**竞争失败**，从 `RUNNABLE --> BLOCKED` 
* 持 obj 锁线程的同步代码块执行完毕，会唤醒该对象上所有 BLOCKED 的线程重新竞争，如果其中 t 线程竞争 成功，从 `BLOCKED --> RUNNABLE` ，其它失败的线程仍然 `BLOCKED`



#### 情况 10 `RUNNABLE <--> TERMINATED`

当前线程所有代码运行完毕，进入 `TERMINATED`







## ==创建线程的方法==



1**.使用Thread的start方法**

> 任务和线程合并(耦合)

```java
new Thread(){() -> {
    log.debug("hello,I'm running");
}}.start();
```

> ```java
> Thread t = new Thread(){
>     @Override
>     public void run(){
>         log.debug("hello,I'm running");
>     }
> };
> t.start();
> ```

重写了父类的run方法





**2.使用Runnable配合Thread**

> 把任务和线程分离

```java
new Thread(() -> {
        log.debug("I'm running");
}}, "线程一").start();
```

> ```java
> // Runnable runnable = () -> log.debug("running");
> Runnable runnable = new Runnable() {
>     public void run(){
>         log.debug("I'm running");
>     }
> };
> Thread t = new Thread(runnable, "线程一");
> t.start();
> ```
>
> 实际执行的时候，调用的是Runnable实现的run方法



**3.使用FutureTask配合Thread**

> 可以获取任务执行结果
>
> 间接实现了Runnable接口，也可以传入Thread：
>
> ![image-20260128120638151](assets/image-20260128120638151.png)
>
> ![image-20260128120647816](assets/image-20260128120647816.png)
>
> callable的唯一方法提供了返回值
>
> 
>
> 

```java
FutureTask<Integer> task = new FureTask<>(new Callable<Integer>(){
    @Override
    public Integer call() throws Exception {
        log.debug("I'm running");
        Thread.sleep(1000);
        return 100;
    }
});
new Thread(task, "线程一").start();

Integer res = task.get();// 获取返回结果(会阻塞，直到结果返回)
```



4.



#### 【查看进程】



**windows**

* 任务管理器

* `tasklist` 查看进程

  ```
  tasklist
  tasklist | findstr java
  ```

* `taskkill` 杀死进程

  ```
  taskkill /F /PID 12345
  ```

  



**linux**

* `ps -ef` 查看所有进程

  ```
  ps -ef | grep java
  ```

  > 会显示出相关进程+grep进程
  >
  > 查看java相关进程也可以用：`jps`

* `ps -fT -p <PID>`：查看某个进程的所有线程

* `kill <PID>`杀死进程

* `top -H -p <PID>` 动态查看<u>某个进程中的所有线程的</u>信息

  > 内存占用、CPU占用百分比
  >
  > 查看java相关线程也可以用：`jstack <PID>` 
  > (把**某一瞬间的**<u>进程中的所有线程的</u>信息抓取出来)

  > 按大写H可以切换是否显示线程



**Java**

* jconsole ：用图形化界面查看java进程中线程的运行情况



## 线程运行的原理

**栈与栈帧**

JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程

* 每个线程启动后，虚拟机就会为其分配一块栈内存 (按照线程分配) 

* 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
* 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法 



> 图解
>
> ![image-20260128132349169](assets/image-20260128132349169.png)
>
> ![image-20260128132419637](assets/image-20260128132419637.png)
>
> 





**线程上下文切换**

因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码 

* 线程的 cpu 时间片用完
* 垃圾回收
* 有更高优先级的线程需要运行
* <u>线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法</u>

当上下文切换发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念 就是程序计数器（Program Counter Register），它的作用是记住下一条 jvm 指令的执行地址，是线程私有的

* 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等

* 线程数不是越多越好，上下文切换频繁会影响性能

  > <u>如何选择合适的线程数</u>



> 
>
> ![image-20260128133552865](assets/image-20260128133552865.png)
>
> 



## 主线程和守护线程

默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其它非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束

* 垃圾回收器线程就是一种守护线程
* Tomcat 中的 Acceptor 和 Poller 线程都是守护线程，所以 Tomcat 接收到 shutdown 命令后，不会等 待它们处理完当前请求

> ```java
> log.debug("开始运行...");
> Thread t1 = new Thread(() -> {
>     log.debug("开始运行...");
>     TimeUnit.SECONDS.sleep(2);
>     log.debug("运行结束...");
> }, "daemon");
> // 设置该线程为守护线程
> t1.setDaemon(true);
> t1.start();
> 
> TimeUnit.SECONDS.sleep(1);
> log.debug("运行结束...");
> ```
>
> 







## Thread类常见方法



| 方法名                 | static | 功能说明                                                     | 注意                                                         |
| ---------------------- | ------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| start()                |        | 启动一个新线程，在新的线程运行run方法中的代码                | start 方法只是让线程进入就绪态(RUNNABLE)，不一定立刻进入运行(CPU时间片可能还没分给它)，每个线程对象的 start 方法只能调用一次，如果多次调用会出现IllegalThreadStateException |
| run()                  |        | 新线程启动后会调用的方法                                     | 如果在构造Thread对象的时候传递了 Runnable 参数，则线程启动后会调用 Runnable 中的 run 方法，否则默认不执行任何操作。但可以创建 Thread 的子类对象，来覆盖默认行为 |
| join()                 |        | 等待线程运行结束                                             |                                                              |
| join(long n)           |        | 等待线程运行结束，最多等待n毫秒                              |                                                              |
| getId()                |        | 获取线程长整型的id                                           | id 唯一                                                      |
| getName()              |        | 获取线程名                                                   |                                                              |
| setName(String)        |        | 修改线程名                                                   |                                                              |
| getPriority()          |        | 获取线程优先级                                               |                                                              |
| setPriority(int)       |        | 修改线程优先级                                               | java中规定线程优先级是 1~10 的整数，较大的优先级能提高该线程被CPU调度的机率 |
| getState()             |        | 获取线程状态                                                 | Java中线程状态是用 6个enum 表示，分别为：NEW，RUNNABLE，BLOCKED，WAITING，TIMED_WAITING，TERMINATED |
| <u>isInterrupted()</u> |        | 判断是否被打断                                               | <u>不会被清除`打断标记`</u>                                  |
| isAlive()              |        | 线程是否存活(还没运行完毕)                                   |                                                              |
| interrupt()            |        | 打断线程                                                     | 如果被打断的线程正在sleep / wait / join，会导致打断的线程抛出 InterruptedException，并清除`打断标记`，如果打断的是正在运行的线程，则会设置`打断标记`，park 的线程被打断，也会设置`打断标记` |
| <u>interrupted()</u>   | static | 判断当前线程是否被打断                                       | <u>会清除`打断标记`</u>                                      |
| currentThread()        | static | 获取当前正在执行的线程                                       |                                                              |
| sleep(long n)          | static | 让当前执行的线程休眠n毫秒(阻塞态 TIMED_WAITING)，休眠时让出cpu的时间片给其他线程 |                                                              |
| yield()                | static | 提示线程调度器让出当前线程对CPU的使用                        | 主要用于测试和调试                                           |





#### sleep 与 yield

**sleep**

> **"躺下"**

1. 调用sleep会让当前线程从**RUNNABLE(具体来说是RUNNING)进入TIMED_WAITING状态**

2. 其它线程可以使用 interrupt 方法打断正在睡眠的线程，这是sleep方法会抛出InterruptedException

3. 睡眠结束后的线程未必会立刻得到执行(进入就绪态 就绪队列)

4. 建议用TimeUnit 的sleep 代替 Thread 的 sleep 来获得更好的可读性

   ```java
   Thread.sleep(2000);
   TimeUnit.SECONDS.sleep(2); // 睡眠2s
   ```

   

**yield**

> “**谦让**”

1. 调用yield 之后， JVM 层面的状态**仍然是 `RUNNABLE`**，向线程调度器发出信号 在操作系统层面由运行态转成就绪态、让出CPU时间片，然后调度器选择其他同优先级的线程运行，如果不存在同优先级的线程(，则当前线程可能被立即重新调度)，那么不能保证让当前线程暂停的效果
2. 具体的实现依赖于操作系统的任务调度器





#### join

* 等待某个线程运行结束

  > 在哪个线程中调用就是哪个线程开始等待

  > 底层基于wait()

```java
log.debug("开始");
Thread t1 = new Thread(() -> {
    log.debug("开始");
    sleep(1);
	log.debug("结束");
    r = 10;
});
t1.start();
t1.join();
log.debug("结果为:{}", r);
log.debug("结束");
```

可以设置等待时间限制

```java
log.debug("开始");
Thread t1 = new Thread(() -> {
    log.debug("开始");
    sleep(1);
	log.debug("结束");
    r = 10;
});
t1.start();
t1.join(2000); // 最多等待两千毫秒
log.debug("结果为:{}", r);
log.debug("结束");
```



##### join原理

> 底层基于[同步模式之保护性暂停](##同步模式之保护性暂停)



> 源码：
>
> 
>
> ![image-20260131102641993](assets/image-20260131102641993.png)
>
> ![image-20260131102629620](assets/image-20260131103110983.png)
>
> 





#### interrupt

* 可以打断正在运行态或阻塞态的线程

  > 阻塞态(**sleep wait join** / WAITING TIMED_WAITING BLOCK)

```java
Thread t1 = new Thread(() -> {
	log.debug("sleep...");
	try {
		Thread.sleep(5000);
	} catch(InterruptedException e) {
		e.printStackTrace();
	}
}, "线程1");
t1.start();

TimeUnit.SECONDS.sleep(1);// 主线程睡一会，让线程1先进入睡眠
log.debug("interrupt");
t1.interrupt();
log.debug(": {}", t1.isInterrupted()); // 打印false
```



**打断标记**：可以用于判断线程被打断之后继续运行还是终止

* 在阻塞态被打断的线程，打断标记会置为false

* 在运行态被打断的线程，打断标记会置为true

  



> ```java
> Thread t1 = new Thread(() -> {
> 	while(true){
> 		boolean interrupted = Thread.currentThread().isInterrupted();
> 		if(interrupted) {
> 			log.debug("被打断了，退出循环");
> 			break;
> 		}
> 	}
> }, "线程1");
> t1.start();
> 
> TimeUnit.SECONDS.sleep(1);
> log.debug("打断线程1");
> t1.interrupt();
> 
> ```
>
> 



##### 使用 interrupt() 打断 park 线程

可以打断处于 park 状态的线程，让它继续往下执行

```java
private static void test() throws InterruptedException {
    Thread t1 = new Thread(() -> {
        log.debug("park...");
        LockSuport.park();
        // 如果不打断，会保持 park，下面的代码不再执行
        log.debug("unpark...");
        log.debug("打断状态: {}", Thread.currentThread().isInterrupted());
        
        LockSuport.park();
        log.debug("unpark...");// 二次park不会生效，会继续打印这行代码
    }, "线程1");
    t1.start();
    
    sleep(1);
    t1.interrupt();
}
```

打断标记为真的时候，park 方法不会生效



```java
private static void test() throws InterruptedException {
    Thread t1 = new Thread(() -> {
        log.debug("park...");
        LockSuport.park();
        // 如果不打断，会保持 park，下面的代码不再执行
        log.debug("unpark...");
        log.debug("打断状态: {}", Thread.currentThread().interrupted());
        // interrupted()在返回打断标记的同时把打断标记置为
        
        LockSuport.park();// 此时二次 park 会生效
        log.debug("unpark...");
    }, "线程1");
    t1.start();
    
    sleep(1);
    t1.interrupt();
}
```









## Object类



#### ==wait / notify==



> #### 小故事-为什么需要 wait
>
> ![image-20260130203823871](assets/image-20260130203823871.png)



##### **原理**

![image-20260130210132051](assets/image-20260130210132051.png)

* Owner 线程发现条件不满足 (需等待)，调用 wait 方法  即可进入 WaitSet 变为 WAITING 状态 
* BLOCKED 和 WAITING 的线程都处于阻塞状态，不占用 CPU 时间片 
* BLOCKED 线程会在 Owner 线程释放锁时唤醒 
* WAITING 线程会在 Owner 线程调用 notify 或 notifyAll 时唤醒，但唤醒后并不意味者立刻获得锁，仍需进入 EntryList 重新竞争



##### **API**

* `obj.wait()` 让进入 object 监视器的线程到 waitSet 等待 
* `obj.notify()` 在 object 上正在 waitSet 等待的线程中挑一个唤醒 
* `obj.notifyAll()` 让 object 上正在 waitSet 等待的线程全部唤醒

它们都是线程之间进行协作的手段，都属于 Object 对象的方法。**必须获得此对象的锁(Monitor)**，才能调用这三个方法

```java
final static Object obj = new Object();
public static void main(String[] args) {
    new Thread(() -> {
        synchronized (obj) {
            log.debug("执行....");
            try {
                obj.wait(); // 此时持有了Monitor，可以调用wait
                // 让线程在obj上一直等待下去
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("其它代码....");
        }
    }).start();
    new Thread(() -> {
        synchronized (obj) {
            log.debug("执行....");
            try {
                obj.wait(); // 让线程在obj上一直等待下去
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("其它代码....");
        }
    }).start();
    
    // 主线程两秒后执行
    sleep(2);
    log.debug("唤醒 obj 上其它线程");
    synchronized (obj) {
        obj.notify(); // 唤醒Monitor的WaitSet上的一个线程
        // obj.notifyAll(); // 唤醒obj上所有等待线程
    }
}
```

notify 的一种结果

```
20:00:53.096 [Thread-0] c.TestWaitNotify - 执行....
20:00:53.099 [Thread-1] c.TestWaitNotify - 执行....
20:00:55.096 [main] c.TestWaitNotify - 唤醒 obj 上其它线程
20:00:55.096 [Thread-0] c.TestWaitNotify - 其它代码.... 

```

notifyAll 的结果

```
19:58:15.457 [Thread-0] c.TestWaitNotify - 执行....
19:58:15.460 [Thread-1] c.TestWaitNotify - 执行....
19:58:17.456 [main] c.TestWaitNotify - 唤醒 obj 上其它线程
19:58:17.456 [Thread-1] c.TestWaitNotify - 其它代码....
19:58:17.456 [Thread-0] c.TestWaitNotify - 其它代码.... 

```

`wait()` 方法会释放对象的锁，进入 WaitSet 等待区，从而让其他线程就机会获取对象的锁。无限制等待，直到 notify 为止

`wait(long n)` 有时限的等待, 到 n 毫秒后结束等待，或是被 notify

> **源码**：
>
> ![image-20260130230802522](assets/image-20260130230802522.png)
>
> ![image-20260130230847121](assets/image-20260130230847121.png)
>
> ![image-20260130231308704](assets/image-20260130231308704.png)
>
> 在java中，Thread类线程执行完run()方法后，一定会自动执行notifyAll()方法。因为线程在die的时候会释放持用的资源和锁，自动调用自身的notifyAll方法。



##### ==区别 sleep & wait==

> ##### sleep(long n) 和 wait(long n) 的区别

1) [API角度]sleep 是 Thread 的(静态)方法，而 wait 是 Object 的方法 
2) [使用条件不同]sleep 不需要强制和 synchronized 配合使用，但 wait 需要 和 synchronized 一起用 
3) [是否释放锁]sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁 

> 共同点：调用之后，线程状态都是 TIMED_WAITING









##### 使用



**step 1**

```java
static final Object room = new Object(); // 共享变量(房间)
// 推荐锁对象加上final保证其引用地址不可变，锁住的都是同一个对象
static boolean hasCigarette = false;
static boolean hasTakeout = false;
```

思考下面的解决方案好不好，为什么？

```java
new Thread(() -> {
    synchronized (room) {
        log.debug("有烟没？[{}]", hasCigarette);
        if (!hasCigarette) {
            log.debug("没烟，先歇会！");
            sleep(2);
        }
        log.debug("有烟没？[{}]", hasCigarette);
        if (hasCigarette) {
            log.debug("可以开始干活了");
        }
    }
}, "小南").start();
for (int i = 0; i < 5; i++) {
    new Thread(() -> {
        synchronized (room) {
            log.debug("可以开始干活了");
        }
    }, "其它人").start();   
}
sleep(1);
new Thread(() -> {
    // 这里能不能加 synchronized (room)？
    hasCigarette = true;
    log.debug("烟到了噢！");
}, "送烟的").start();
```

> 不能加。加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加 synchronized 就好像 main 线程是翻窗户进来的 

输出

```java
20:49:49.883 [小南] c.TestCorrectPosture - 有烟没？[false]
20:49:49.887 [小南] c.TestCorrectPosture - 没烟，先歇会！
20:49:50.882 [送烟的] c.TestCorrectPosture - 烟到了噢！
20:49:51.887 [小南] c.TestCorrectPosture - 有烟没？[true]
20:49:51.887 [小南] c.TestCorrectPosture - 可以开始干活了
20:49:51.887 [其它人] c.TestCorrectPosture - 可以开始干活了
20:49:51.887 [其它人] c.TestCorrectPosture - 可以开始干活了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以开始干活了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以开始干活了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以开始干活了
```

* 其它干活的线程，都要一直阻塞，效率太低 
* 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来 
* 改进方法，使用 wait - notify 机制



**step 2**

思考下面的实现行吗，为什么？思考下面的实现行吗，为什么？

```java
new Thread(() -> {
    synchronized (room) {
        log.debug("有烟没？[{}]", hasCigarette);
        if (!hasCigarette) {
            log.debug("没烟，先歇会！");
            try {
                room.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("有烟没？[{}]", hasCigarette);
        if (hasCigarette) {
            log.debug("可以开始干活了");
        }
    }
}, "小南").start();

for (int i = 0; i < 5; i++) {
    new Thread(() -> {
        synchronized (room) {
            log.debug("可以开始干活了");
        }
 }, "其它人").start();
}
sleep(1);
new Thread(() -> {
    synchronized (room) {
        hasCigarette = true;
        log.debug("烟到了噢！");
        room.notify();
    }
}, "送烟的").start();

```

输出

```
20:51:42.489 [小南] c.TestCorrectPosture - 有烟没？[false]
20:51:42.493 [小南] c.TestCorrectPosture - 没烟，先歇会！
20:51:42.493 [其它人] c.TestCorrectPosture - 可以开始干活了
20:51:42.493 [其它人] c.TestCorrectPosture - 可以开始干活了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以开始干活了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以开始干活了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以开始干活了
20:51:43.490 [送烟的] c.TestCorrectPosture - 烟到了噢！
20:51:43.490 [小南] c.TestCorrectPosture - 有烟没？[true]
20:51:43.490 [小南] c.TestCorrectPosture - 可以开始干活了

```

* 解决了其它干活的线程阻塞的问题 
* 问题：
  * 如果有其它线程也在等待条件呢？
    如果还有其他线程也在等待，不能保证唤醒的是“小南”的线程



**step3**

```java
new Thread(() -> {
    synchronized (room) {
        log.debug("有烟没？[{}]", hasCigarette);
        if (!hasCigarette) {
            log.debug("没烟，先歇会！");
            try {
                room.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("有烟没？[{}]", hasCigarette);
        if (hasCigarette) {
            log.debug("可以开始干活了");
        } else {
            log.debug("没干成活...");
        }
    }
}, "小南").start();

new Thread(() -> {
    synchronized (room) {
        Thread thread = Thread.currentThread();
        log.debug("外卖送到没？[{}]", hasTakeout);
        if (!hasTakeout) {
            log.debug("没外卖，先歇会！");
            try {
                room.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("外卖送到没？[{}]", hasTakeout);
        if (hasTakeout) {
            log.debug("可以开始干活了");
        } else {
            log.debug("没干成活...");
        }
    }
}, "小女").start();
sleep(1);
new Thread(() -> {
    synchronized (room) {
        hasTakeout = true;
        log.debug("外卖到了噢！");
        room.notify();
    }
}, "送外卖的").start();
```

输出

```
20:53:12.173 [小南] c.TestCorrectPosture - 有烟没？[false]
20:53:12.176 [小南] c.TestCorrectPosture - 没烟，先歇会！
20:53:12.176 [小女] c.TestCorrectPosture - 外卖送到没？[false]
20:53:12.176 [小女] c.TestCorrectPosture - 没外卖，先歇会！
20:53:13.174 [送外卖的] c.TestCorrectPosture - 外卖到了噢！
20:53:13.174 [小南] c.TestCorrectPosture - 有烟没？[false]
20:53:13.174 [小南] c.TestCorrectPosture - 没干成活... 
```

* notify 只能随机唤醒一个 WaitSet 中的线程，这时如果有其它线程也在等待，那么就可能唤醒不了正确的线程，称之为【虚假唤醒】 
* 解决方法，改为 notifyAll



**step4**

```java
new Thread(() -> {
    synchronized (room) {
        hasTakeout = true;
        log.debug("外卖到了噢！");
        room.notifyAll();
    }
}, "送外卖的").start();

```

输出

```
20:55:23.978 [小南] c.TestCorrectPosture - 有烟没？[false]
20:55:23.982 [小南] c.TestCorrectPosture - 没烟，先歇会！
20:55:23.982 [小女] c.TestCorrectPosture - 外卖送到没？[false]
20:55:23.982 [小女] c.TestCorrectPosture - 没外卖，先歇会！
20:55:24.979 [送外卖的] c.TestCorrectPosture - 外卖到了噢！
20:55:24.979 [小女] c.TestCorrectPosture - 外卖送到没？[true]
20:55:24.980 [小女] c.TestCorrectPosture - 可以开始干活了
20:55:24.980 [小南] c.TestCorrectPosture - 有烟没？[false]
20:55:24.980 [小南] c.TestCorrectPosture - 没干成活... 

```

* 用 notifyAll 仅解决某个线程的唤醒问题，但使用 if + wait 判断仅有一次机会，一旦条件不成立，就没有重新判断的机会了 
* 解决方法，用 ==while + wait==（解决虚假唤醒问题，唤醒的时候惊喜那个条件判断，如果是假唤醒就继续等待），当条件不成立，再次 wait







**step5**

将 if 改为 while

```java
while (!hasCigarette) { // 类似自旋
    log.debug("没烟，先歇会！");
    try {
        room.wait();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

```

输出

```
20:58:34.322 [小南] c.TestCorrectPosture - 有烟没？[false]
20:58:34.326 [小南] c.TestCorrectPosture - 没烟，先歇会！
20:58:34.326 [小女] c.TestCorrectPosture - 外卖送到没？[false]
20:58:34.326 [小女] c.TestCorrectPosture - 没外卖，先歇会！
20:58:35.323 [送外卖的] c.TestCorrectPosture - 外卖到了噢！
20:58:35.324 [小女] c.TestCorrectPosture - 外卖送到没？[true]
20:58:35.324 [小女] c.TestCorrectPosture - 可以开始干活了
20:58:35.324 [小南] c.TestCorrectPosture - 没烟，先歇会！
```



**使用套路**

```java
synchronized(lock) {
    while(条件不成立) {
        lock.wait();
    }
    // 干活
}
//另一个线程
synchronized(lock) {
    lock.notifyAll();
}

```







#### 不推荐使用的方法

| 方法名    | 功能说明           | 替代   |
| --------- | ------------------ | ------ |
| stop()    | 停止线程运行       |        |
| suspend() | 挂起(暂停)线程运行 | wait   |
| resume()  | 恢复线程运行       | notify |

已过时，容易破坏同步代码块，造成死锁





## LockSupport类



### Park & Unpark



**基本使用**

它们是 LockSupport 类中的方法

```java
// 暂停当前线程
LockSupport.park();

// 恢复某个线程的运行
LockSupport.unpark(暂停线程对象)
```

**先 park 再 unpark**

```java
Thread t1 = new Thread(() -> {
    log.debug("start...");
    sleep(1);
    log.debug("park...");
    LockSupport.park();
    log.debug("resume...");
},"t1");
t1.start();

sleep(2);
log.debug("unpark...");
LockSupport.unpark(t1); // 恢复t1线程运行
```

输出

```
18:42:52.585 c.TestParkUnpark [t1] - start...
18:42:53.589 c.TestParkUnpark [t1] - park...
18:42:54.583 c.TestParkUnpark [main] - unpark...
18:42:54.583 c.TestParkUnpark [t1] - resume... 

```

**先 unpark 再 park**

```java
Thread t1 = new Thread(() -> {
    log.debug("start...");
    sleep(2);
    log.debug("park...");
    LockSupport.park();
    log.debug("resume...");
}, "t1");
t1.start();

sleep(1);
log.debug("unpark...");
LockSupport.unpark(t1);
```

输出

```
18:43:50.765 c.TestParkUnpark [t1] - start...
18:43:51.764 c.TestParkUnpark [main] - unpark...
18:43:52.769 c.TestParkUnpark [t1] - park...
18:43:52.769 c.TestParkUnpark [t1] - resume... 

```

> 原因参考“原理”部分



**与 Object 的 wait & notify 相比** 

* wait，notify 和 notifyAll 必须配合 Object Monitor 一起使用，而 park，unpark 不必
* park & unpark 是以线程为单位来【阻塞】和【唤醒】线程，而 notify 只能随机唤醒一个等待线程，notifyAll 是唤醒所有等待线程，就不那么【精确】 
* park & unpark 可以先 unpark，而 wait & notify 不能先 notify



#### 原理



**每个线程都有自己的一个 Parker 对象**，由三部分组成 _counter ， _cond 和 _mutex ，打个比喻：_

* **线程就像一个旅人，Parker 就像他随身携带的背包**，条件变量就好比背包中的帐篷。_counter 就好比背包中 的备用干粮（0 为耗尽，1 为充足） 
* 调用 park 就是要看需不需要停下来歇息 
  * 如果备用干粮耗尽(_counter为0)，那么钻进帐篷歇息 
  * 如果备用干粮充足(_counter为1)，那么不需停留，继续前进 
* 调用 unpark，就好比令干粮充足 (令_counter为1)
  * 如果这时线程还在帐篷，就唤醒让他继续前进 
  * 如果这时线程还在运行，那么下次他调用 park 时，仅是消耗掉备用干粮，不需停留继续前进 
    * 因为背包空间有限，多次调用 unpark 仅会补充一份备用干粮

![image-20260131152737834](assets/image-20260131152737834.png)

1. 当前线程调用 Unsafe.park() 方法 
2. 检查 _counter ，本情况为 0，这时，获得 _mutex 互斥锁 
3. 线程进入 _cond 条件变量阻塞 
4. 设置 _counter = 0

![image-20260131153304426](assets/image-20260131153304426.png)

1. 调用 Unsafe.unpark(Thread_0) 方法，设置 _counter 为 1 
2. 唤醒 _cond 条件变量中的 Thread_0 
3. Thread_0 恢复运行 
4. 设置 _counter 为 0











## 线程优先级

`package java.lang;`

![image-20260128194023767](assets/image-20260128194023767.png)

最小优先级1

默认优先级5



* 线程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它

* 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用



# 模式

## 终止模式之两阶段终止模式

> 模式方面

**错误的终止思路**：

* 使用线程对象的`stop()`方法停止线程

  * stop方法会真正杀死线程，如果这时线程锁住了共享资源，那么它被杀死后就再也没有机会释放锁，其他线程将永远无法获取锁

* 使用`System.exit(int)`方法停止线程

  此方法会让整个程序都停止，而不是仅停止一个线程



**两阶段终止模式**：



* 优点：给被终止线程处理后续工作的机会

* 场景：监控线程，如果需要停止

  > 监控线程一般会有个while(true)循环
  >
  > ![image-20260128212608909](assets/image-20260128212608909.png)
  >



**使用打断标记实现**

```java
public class Test {
    public static void main(String[] args) throws InterruptedException();
    tpt.start();
    
    Thread.sleep(3500);
    tpt.stop(); 
}
class TwoPhaseTermination {
    private Thread monitor;
    
    // 启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while(true){
                Thread current = Thread.currentThread();
                if(current.isInterrupted()){
                    log.debug("处理终止工作(料理后事)");
                    break;
                }
                
                try {
                    TimeUnit.SECONDS.sleep(1); // 1.睡眠中可能被打断
                    log.debug("执行监控记录"); // 2.运行中可能被打断 
                } catch (InterruptedException e) {
                    // 睡眠中被打断会以异常的形式处理
                    e.printStackTrace();
                    // 设置打断标记为真，确保睡眠中被打断也能正常终止程序
                    current.interrupt();// 容易忘记将这个interrupt重新设置为true
                }
            }
        });
        
        monitor.start();
    }
    
    // 停止监控线程
    public void stop() {
        monitor.interrupt();
    }
    
}
```



**使用volatile优化**

```java
public class Test {
    public static void main(String[] args) throws InterruptedException();
    tpt.start();
    
    Thread.sleep(3500);
    tpt.stop(); 
}
class TwoPhaseTermination {
    private Thread monitor;
    private volatile boolean stop; // 标记是否被打断了
    
    // 启动监控线程
    public void start() {
        monitor = new Thread(() -> {
            while(true){
                
                if(stop){
                    log.debug("处理终止工作(料理后事)");
                    break;
                }
                
                try {
                    TimeUnit.SECONDS.sleep(1); // 1.睡眠中可能被打断
                    log.debug("执行监控记录"); // 2.运行中可能被打断 
                } catch (InterruptedException e) {
                    // 睡眠中被打断会以异常的形式处理
                    e.printStackTrace();
                }
            }
        });
        
        monitor.start();
    }
    
    // 停止监控线程
    public void stop() { 
        stop = true;
        monitor.interrupt();
    }
    
}
```













## 同步模式之保护性暂停



##### **1.定义**

即 Guarded Suspension，用在一个线程等待另一个线程的执行结果 

要点 

* 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject 

* 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者） 

* JDK 中，**join 的实现、Future 的实现，采用的就是此模式** 

  > [join原理](####join原理)

* 因为要等待另一方的结果，因此归类到同步模式



![image-20260131094504397](assets/image-20260131094504397.png)



##### **2.实现**



```java
class GuardedObject {
    private Object response;
    private final Object lock = new Object();
    // 获取结果
    public Object get() {
        synchronized (lock) {
            // 条件不满足则等待
            while (response == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }
    // 产生结果
    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等待线程
            this.response = response;
            lock.notifyAll();
        }
    }
}

```



##### **应用**

一个线程等待另一个线程的执行结果

```java
public static void main(String[] args) {
    GuardedObject guardedObject = new GuardedObject();
    new Thread(() -> {
        try {
            // 子线程执行下载
            List<String> list = Downloader.download();
            log.debug("download complete...");
            guardedObject.complete(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }).start();
    log.debug("waiting...");
    // 主线程阻塞等待
    Object response = guardedObject.get();
    log.debug("get response: [{}] lines", ((List<String>) response).size());
}

```

执行结果

```
08:42:18.568 [main] c.TestGuardedObject - waiting...
08:42:23.312 [Thread-0] c.TestGuardedObject - download complete...
08:42:23.312 [main] c.TestGuardedObject - get response: [3] lines
```



##### **3.带超时版 GuardedObject**

控制超时时间的版本

```java
class GuardedObjectV2 {
    private Object response;
    private final Object lock = new Object();
    public Object get(long millis) {
        synchronized (lock) {
            // 1) 记录最初时间
            long begin = System.currentTimeMillis();
            // 2) 已经经历的时间
            long passedTime = 0;
            while (response == null) {
                // 4) 假设 millis 是 1000，结果在 400 时唤醒了，那么还有 600 要等
                log.debug("还需等待时间: {}", millis - passedTime);
                if (waitTime <= 0) {
                    log.debug("break...");
                    break;
                }
                try {
                    lock.wait(millis - passedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 3) 如果提前被唤醒，这时已经经历的时间假设为 400
                passedTime = System.currentTimeMillis() - begin;
                log.debug("passedTime: {}, object is null {}", passedTime, response == null);
            }
            return response;
        }
    }
    public void complete(Object response) {
        synchronized (lock) {
            // 条件满足，通知等待线程
            this.response = response;
            log.debug("notify...");
            lock.notifyAll();
        }
    }
}

```

测试，没有超时

```java
public static void main(String[] args) {
    GuardedObjectV2 v2 = new GuardedObjectV2();
    new Thread(() -> {
        sleep(1);
        v2.complete(null);
        sleep(1);
        v2.complete(Arrays.asList("a", "b", "c"));
    }).start();
    Object response = v2.get(2500);
    if (response != null) {
        log.debug("get response: [{}] lines", ((List<String>) response).size());
    } else {
        log.debug("can't get response");
    }
}

```





### 改进

> #### 多任务版 GuardedObject
>
> (消息队列)

![image-20260131103502510](assets/image-20260131103502510.png)

图中 Futures 就好比居民楼一层的信箱（每个信箱有房间编号），左侧的 t0，t2，t4 就好比等待邮件的居民，右 侧的 t1，t3，t5 就好比邮递员 

如果需要在多个类之间使用 GuardedObject 对象，作为参数传递不是很方便，因此设计一个用来解耦的中间类， 这样不仅能够解耦【结果等待者】和【结果生产者】，还能够同时支持多个任务的管理



```java
class GuardedObject {
    // 标识 Guarded Object
    private int id;
    public GuardedObject(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    // 结果
   private Object response;
    // 获取结果
    // timeout 表示要等待多久 2000
    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间 15:00:00
           long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                // 这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                // 经历的时间超过了最大等待时间时，退出循环
                if (timeout - passedTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime); // 虚假唤醒 15:00:01
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求得经历时间
                passedTime = System.currentTimeMillis() - begin; // 15:00:02 1s
            }
            return response;
        }
    }
    // 产生结果
    public void complete(Object response) {
        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
```

新增 id 用来标识 Guarded Object

**中间解耦类**：

```java
class Mailboxes {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;
    // 产生唯一 id (在中间类中产生，避免外部自定义的id冲突)
    private static synchronized int generateId() {
        return id++;
    }
    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id); // 删除，避免内存溢出
    }
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

```



**业务相关类**:

```java
class People extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000); // 收信，最多等5s
        log.debug("收到信 id:{}, 内容:{}", guardedObject.getId(), mail);
    }
}

```

```java
class Postman extends Thread {
    private int id;
    private String mail;
    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }
    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail); // （送信）
    }
}
```



测试

```java
public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 3; i++) {
        new People().start();
    }
    Sleeper.sleep(1);
    for (Integer id : Mailboxes.getIds()) {
        new Postman(id, "内容" + id).start();
    }
}

```



## 同步模式之顺序控制



#### **固定运行顺序**



案例：两个打印线程，要求必须先打印 2   后打印 1

方法一：使用`wait()`+`notify()`方法

```java
public class Main{
    static final Object lock = new Object();
    private int count = 0;
    static boolean t2Runned = false;// 【表示t2是否运行过】
    new Thread(() -> {
        synchronized (lock) {
            while(!t2Runned){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("1");
        }
    },"t1").start();
    new Thread(() -> {
        synchronized (lock) {
            log.debug("2");
            t2Runned = true;
            lock.notify();
        }
    },"t2").start();
}
```

方法二：使用`await()`+`signal()`方法

方法三：使用`park()` + `unpark()`方法

```java
public class Main{
    static final Object lock = new Object();
    private int count = 0;
    Thread t1 = new Thread(() -> {
        LockSupport.park();
        log.debug("1");
    },"t1");
    Thread t2 = new Thread(() -> {
        log.debug("2");
        LockSupport.park(t1);
    }, "t2");
    
    t1.start();
    t2.start();
}


```



#### 交替输出

线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现

方法一：使用`wait()`+`notify()`方法

```java
class SyncWaitNotify {
    private int flag;
    private int loopNumber;
    public SyncWaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
    public void print(int waitFlag, int nextFlag, String str) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {// 如果不是当前线程期望打印的数字就继续等待
                while (this.flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str); 
                flag = nextFlag;// flag标记为下一个应该打印的数字
                this.notifyAll();
            }
        }
    }
}

public static void main(String[] args){
    SyncWaitNotify syncWaitNotify = new SyncWaitNotify(1, 5);
    new Thread(() -> {
     syncWaitNotify.print(1, 2, "a");// 每个线程自己运行完了就把标记改成下一个线程的标记
    }).start();
    new Thread(() -> {
     syncWaitNotify.print(2, 3, "b");
    }).start();
    new Thread(() -> {
     syncWaitNotify.print(3, 1, "c");
    }).start();
}
```

方法二：使用`await()`+`signal()`方法

```java
class AwaitSignal extends ReentrantLock {
    public void start(Condition first) {
        this.lock();
        try {
            log.debug("start");
            first.signal();
        } finally {
            this.unlock();
        }
    }
    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                current.await();
                log.debug(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }
    // 循环次数
    private int loopNumber;
    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}

public static void main(String[] args){
    AwaitSignal as = new AwaitSignal(5);
    Condition aWaitSet = as.newCondition();
    Condition bWaitSet = as.newCondition();
    Condition cWaitSet = as.newCondition();
    new Thread(() -> {
        as.print("a", aWaitSet, bWaitSet);
    }).start();
    
    new Thread(() -> {
        as.print("b", bWaitSet, cWaitSet);
    }).start();
    new Thread(() -> {
        as.print("c", cWaitSet, aWaitSet);
    }).start();
    Thread.sleep(1000);
    try {
        System.out.println("开始...");
        aWaitSet.signal();
    } finally {
        awaitSignal.unlock();
    }
    
}
```





方法三：使用`park()` + `unpark()`方法

```java
class SyncPark {
    private int loopNumber;
    private Thread[] threads;
    public SyncPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }
    public void setThreads(Thread... threads) {
        this.threads = threads;
    }
    public void print(String str) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(nextThread());// 打印完唤醒下一个线程
        }
    }
    private Thread nextThread() {
        Thread current = Thread.currentThread();
        int index = 0;
        for (int i = 0; i < threads.length; i++) {
            if(threads[i] == current) {
                index = i;
                break;
            }
        }
        if(index < threads.length - 1) {
            return threads[index+1];
        } else {
            return threads[0];
        }
    }
    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
        LockSupport.unpark(threads[0]);
    }
}

public static void main(String[] args){
    SyncPark syncPark = new SyncPark(5);
    Thread t1 = new Thread(() -> {
        syncPark.print("a");
    });
    Thread t2 = new Thread(() -> {
        syncPark.print("b");
    });
    Thread t3 = new Thread(() -> {
        syncPark.print("c\n");
    });
    syncPark.setThreads(t1, t2, t3);
    syncPark.start();

}
```









**大厂面试原题 两个线程交替打印奇偶数**

方法一：使用`synchronized` + `wait/notify`（推荐）

```java
public class Main{
    public static void main(String[] args){
        static final Object lock = new Object();
        private int count = 1;
        private static final int MAX = 100;
        static boolean t2Runned = false;// 【表示t2是否运行过】
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (count <= MAX) {
                    if (count % 2 == 1) { // 奇数
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                        lock.notify(); // 唤醒t2
                    } else {
                        try {
                            lock.wait(); // 等待t2打印偶数
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        },"t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (count <= MAX) {
                    if (count % 2 == 0) { // 偶数
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                        lock.notify(); // 唤醒t1
                    } else {
                        try {
                            lock.wait(); // 等待t1打印奇数
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        },"t2");
        
        t1.start();
        t2.start();
        
    }    
}

```











> 
>
> 相关题：力扣1114题



## 同步模式之 Balking

> 和 “终止模式” 类似，都与监控场景相关
>
> ```java
> public class Test {
>     public static void main(String[] args) throws InterruptedException();
>     tpt.start();
>     
>     Thread.sleep(3500);
>     tpt.stop(); 
> }
> class TwoPhaseTermination {
>     private Thread monitor;
>     private volatile boolean stop; // 标记是否被打断了
>     
>     // 启动监控线程
>     public void start() {
>         monitor = new Thread(() -> {
>             while(true){
>                 
>                 if(stop){
>                     log.debug("处理终止工作(料理后事)");
>                     break;
>                 }
>                 
>                 try {
>                     TimeUnit.SECONDS.sleep(1); // 1.睡眠中可能被打断
>                     log.debug("执行监控记录"); // 2.运行中可能被打断 
>                 } catch (InterruptedException e) {
>                     // 睡眠中被打断会以异常的形式处理
>                     e.printStackTrace();
>                 }
>             }
>         });
>         monitor.start();
>     }
>     
>     // 停止监控线程
>     public void stop() { 
>         stop = true;
>         monitor.interrupt();
>     }
>     
> }
> ```
>
> 存在问题：start 方法可以反复调用，每次掉员工都会创建一个监控线程



**1.定义**

Balking （犹豫）模式用在一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做 了，直接结束返回

**2.实现**

例如：

```java
public class MonitorService {
    // 增加一个标记 用来表示是否已经有线程已经在执行启动了
    private volatile boolean starting;
    
    public void start() {
        log.info("尝试启动监控线程...");
        synchronized (this) { // 需要加锁，否则多线程进入这段代码，都判断为false，导致仍然有多个线程被创建 (此处volatile不足以应对多线程，需要锁住临界区)
            log.info("该监控线程已启动?({})", starting);
            if (starting) {
                return;
            }
            starting = true;
        }
        // 真正启动监控线程
        monitor = new Thread(() -> {
            while(true){       
                if(stop){
                    log.debug("处理终止工作(料理后事)");
                    starting = false; // 用volatile保证可见性
                    log.info("监控线程已经停止");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1); // 1.睡眠中可能被打断
                    log.debug("执行监控记录"); // 2.运行中可能被打断 
                } catch (InterruptedException e) {
                    // 睡眠中被打断会以异常的形式处理
                    e.printStackTrace();
                }
            }
        });
        monitor.start();
    }
}
```

当前端页面多次点击按钮调用 start 时 

输出

```
[http-nio-8080-exec-1] cn.itcast.monitor.service.MonitorService - 该监控线程已启动?(false)
[http-nio-8080-exec-1] cn.itcast.monitor.service.MonitorService - 监控线程已启动...
[http-nio-8080-exec-2] cn.itcast.monitor.service.MonitorService - 该监控线程已启动?(true)
[http-nio-8080-exec-3] cn.itcast.monitor.service.MonitorService - 该监控线程已启动?(true)
[http-nio-8080-exec-4] cn.itcast.monitor.service.MonitorService - 该监控线程已启动?(true)
```

它还经常用来实现线程安全的单例

```java
public final class Singleton {
    private Singleton() {
        
    }
    private static Singleton INSTANCE = null;
    public static synchronized Singleton getInstance() {
        if (INSTANCE != null) { // 也是Balking 模式的体现
            return INSTANCE;
        }
        INSTANCE = new Singleton();
        return INSTANCE;
    }
}
```

对比一下保护性暂停模式：保护性暂停模式用在一个线程等待另一个线程的执行结果，当条件不满足时线程等待



















## 异步模式之==生产者/消费者==

> #### 生产者和消费者问题



要点

* 与前面的保护性暂停中的 GuardObject 不同，不需要产生结果和消费结果的线程一一对应 
* 消费队列可以用来平衡生产和消费的线程资源 
* 生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据 
* 消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据 
* JDK 中各种阻塞队列，采用的就是这种模式

![image-20260131142201641](assets/image-20260131142201641.png)

### 代码实现



#### 满哥讲解

```java
class Message {
    private int id;
    private Object message;
    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }
    public int getId() {
        return id;
    }
    public Object getMessage() {
        return message;
    }
}
class MessageQueue {
    private LinkedList<Message> queue;
    private int capacity;
    public MessageQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }
    
    // 获取消息
    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                log.debug("没货了, wait");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.removeFirst();
            queue.notifyAll();
            return message;
        }
        
        // 存入消息
        public void put(Message message) {
            synchronized (queue) {
                while (queue.size() == capacity) {
                    log.debug("库存已达上限, wait");
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(message);
                queue.notifyAll();
            }
        }
}
```



```java
MessageQueue messageQueue = new MessageQueue(2);
// 4 个生产者线程, 下载任务
for (int i = 0; i < 4; i++) {
    int id = i;
    new Thread(() -> {
        try {
            log.debug("download...");
            List<String> response = Downloader.download();
            log.debug("try put message({})", id);
            // 存入下载结果
            messageQueue.put(new Message(id, response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }, "生产者" + i).start();
}
// 1 个消费者线程, 处理结果
new Thread(() -> {
    while (true) {
        Message message = messageQueue.take();// 获取消息
        List<String> response = (List<String>) message.getMessage();
        log.debug("take message({}): [{}] lines", message.getId(), response.size());
    }
}, "消费者").start();

```

某次运行结果

```
10:48:38.070 [生产者3] c.TestProducerConsumer - download...
10:48:38.070 [生产者0] c.TestProducerConsumer - download...
10:48:38.070 [消费者] c.MessageQueue - 没货了, wait
10:48:38.070 [生产者1] c.TestProducerConsumer - download...
10:48:38.070 [生产者2] c.TestProducerConsumer - download...
10:48:41.236 [生产者1] c.TestProducerConsumer - try put message(1)
10:48:41.237 [生产者2] c.TestProducerConsumer - try put message(2)
10:48:41.236 [生产者0] c.TestProducerConsumer - try put message(0)
10:48:41.237 [生产者3] c.TestProducerConsumer - try put message(3)
10:48:41.239 [生产者2] c.MessageQueue - 库存已达上限, wait
10:48:41.240 [生产者1] c.MessageQueue - 库存已达上限, wait
10:48:41.240 [消费者] c.TestProducerConsumer - take message(0): [3] lines
10:48:41.240 [生产者2] c.MessageQueue - 库存已达上限, wait
10:48:41.240 [消费者] c.TestProducerConsumer - take message(3): [3] lines
10:48:41.240 [消费者] c.TestProducerConsumer - take message(1): [3] lines
10:48:41.240 [消费者] c.TestProducerConsumer - take message(2): [3] lines
10:48:41.240 [消费者] c.MessageQueue - 没货了, wait

```



#### Synchronized 版

```
/**
 * 线程之间的通信问题：生产者和消费者问题 
 * 解释：两个线程交替执行 A B 操作同一个变量 num = 0
 * A 执行 num+1
 * B 执行 num-1
 * 使用“等待唤醒+通知唤醒”的方式让两个轮流执行
 */
```

生产者和消费者问题-演示代码

```JAVA
package com.zzy.pc;
//顺序：判断等待->业务->通知
public class A {
    public static void main(String[] args) {
        Data data = new Data();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        },"B").start();
    }
}

class Data {
    private int number = 0;

    //+1
    public synchronized void increment() throws InterruptedException {
        //一、等待
        if (number != 0) {
            this.wait();
        }
        
        // 二、业务
        number++;
        System.out.println(Thread.currentThread().getName() + "=>" + number);
        // 三、通知其他线程，我+1完毕了
        this.notifyAll();
    }

    //-1
    public synchronized void decrement() throws InterruptedException {
        //一、等待
        if (number == 0) {
            this.wait();
        }
        
        // 二、业务
        number--;
        System.out.println(Thread.currentThread().getName() + "=>" + number);
        // 三、通知其他线程，我-1完毕了
        this.notifyAll();
    }
}
```

##### 存在的虚假唤醒问题

**问题，如果有四个线程**，会出现虚假唤醒

![image-20200810224629273](https://img-service.csdnimg.cn/img_convert/1af314564aae84ed73a7ee90525f9d91.png)



**虚假唤醒问题**：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130202723375.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

解决方式 ，**if 改为while即可，防止虚假唤醒**

> 结论：**就是用if判断的话，唤醒后线程会从wait之后的代码开始运行，但是不会重新判断if条件，直接继续运行if代码块之后的代码，而如果使用while的话，也会从wait之后的代码运行，但是唤醒后会重新判断循环条件，如果不成立再执行while代码块之后的代码块，成立的话继续wait。**
> 这也就是为什么用while而不用if的原因了，因为线程被唤醒后，执行开始的地方是wait之后
>
> 
>
> (本来只需要一个消费者，但是notifyAll唤醒了一群消费者，一群只有一个可以消费，那么其他消费者就是虚假唤醒了，所以才需要使用while来多次判断条件)

```java
package com.marchsoft.juctest;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/10 22:33
 **/

public class ConsumeAndProduct {
    public static void main(String[] args) {
        Data data = new Data();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}

class Data {
    private int num = 0;

    // +1
    public synchronized void increment() throws InterruptedException {
        // 判断等待
        while (num != 0) {
            this.wait();
        }
        num++;
        System.out.println(Thread.currentThread().getName() + "=>" + num);
        // 通知其他线程 +1 执行完毕
        this.notifyAll();
    }

    // -1
    public synchronized void decrement() throws InterruptedException {
        // 判断等待
        while (num == 0) {
            this.wait();
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "=>" + num);
        // 通知其他线程 -1 执行完毕
        this.notifyAll();
    }
}
```

#### Lock版

![image-20200811094721678](https://img-service.csdnimg.cn/img_convert/dad7044c4b8b46648084823841cb6781.png)

> Lock场景下，Condition对象的await方法起到wait的作用，signal方法起到notify的作用



生产者和消费者问题-演示代码：

```java
package com.marchsoft.juctest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/11 9:48
 **/

public class LockCAP {
    public static void main(String[] args) {
        Data2 data = new Data2();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {

                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}

class Data2 {
    private int num = 0;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    // +1
    public  void increment() throws InterruptedException {
        lock.lock();
        try {
            // 一、判断等待
            while (num != 0) {
                condition.await();
            }
            // 二、业务
            num++;
            System.out.println(Thread.currentThread().getName() + "=>" + num);
            // 三、通知其他线程 +1 执行完毕
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }

    // -1
    public  void decrement() throws InterruptedException {
        lock.lock();
        try {
            // 判断等待
            while (num == 0) {
                condition.await();
            }
            num--;
            System.out.println(Thread.currentThread().getName() + "=>" + num);
            // 通知其他线程 +1 执行完毕
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }
}
```



#### Condition的优势

精准的通知和唤醒的线程 

**如果我们要指定通知的下一个进行顺序怎么办呢？ 我们<u>可以使用Condition来指定通知进程执行顺序</u>~**

示例代码

```java
package com.marchsoft.juctest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description：实现
 * A 执行完 调用B
 * B 执行完 调用C
 * C 执行完 调用A
 *
 * @author jiaoqianjin
 * Date: 2020/8/11 9:58
 **/

public class ConditionDemo {
    public static void main(String[] args) {
        Data3 data3 = new Data3();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                data3.printA();
            }
        },"A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                data3.printB();
            }
        },"B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                data3.printC();
            }
        },"C").start();
    }

}

//num为1的时候，让A执行；num为2的时候，让B执行；num为3的时候，让C执行；

class Data3 {
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();
    private int num = 1; // 1A 2B 3C

    public void printA() {
        lock.lock();
        try {
            // 业务代码 判断 -> 执行 -> 通知
            while (num != 1) {
                condition1.await();
            }
            System.out.println(Thread.currentThread().getName() + "==> AAAA" );
            num = 2;
            condition2.signal();//唤醒condition2
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printB() {
        lock.lock();
        try {
            // 业务代码 判断 -> 执行 -> 通知
            while (num != 2) {
                condition2.await();
            }
            System.out.println(Thread.currentThread().getName() + "==> BBBB" );
            num = 3;
            condition3.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void printC() {
        lock.lock();
        try {
            // 业务代码 判断 -> 执行 -> 通知
            while (num != 3) {
                condition3.await();
            }
            System.out.println(Thread.currentThread().getName() + "==> CCCC" );
            num = 1;
            condition1.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
/*
A==> AAAA
B==> BBBB
C==> CCCC
A==> AAAA
B==> BBBB
C==> CCCC
...
*/
```





















# ==共享模型-管程==

> 解决并发问题的思路-1



## 管程-悲观锁(阻塞)



### 共享资源的线程安全问题

 ![image-20260129094057576](assets/image-20260129094057576.png)

**问题分析** 

> 以上的结果可能是正数、负数、零。为什么呢？因为 Java 中对静态变量的自增，自减并不是原子操作，要彻底理 解，必须从字节码来进行分析
>
> 例如对于 i++ 而言（i 为静态变量），实际会产生如下的 JVM 字节码指令：
>
> ```java
> getstatic     i // 获取静态变量i的值
> iconst_1      i // 准备常量1
> iadd          i // 自增
> putstatic     i // 将修改后的值存入静态变量i
> ```
>
> 而对应 `i--` 也是类似：
>
> ```java
> getstatic     i // 获取静态变量i的值
> iconst_1      i // 准备常量1
> isub          i // 自减
> putstatic     i // 将修改后的值存入静态变量i
> ```
>
> 而 Java 的内存模型如下，完成静态变量的自增，自减需要在主存和工作内存中进行数据交换：
>
> ![image-20260129094456568](assets/image-20260129094456568.png)
>
> 如果是单线程以上 8 行代码是顺序执行（不会交错）没有问题：
>
> ![image-20260129094550253](assets/image-20260129094550253.png)
>
> 但多线程下这 8 行代码可能交错运行，(出现负数的情况)：
>
> ![image-20260129094653808](assets/image-20260129094653808.png)
>
> (出现正数的情况)：
>
> ![image-20260129094737654](assets/image-20260129094737654.png)
>
> 

——上下文切换导致的指令交错



**临界区**

* 一个程序运行多个线程本身是没有问题的
* 问题出在多个线程访问共享资源
  * 多个线程读共享资源其实也没有问题
  * 在多个线程对共享资源读写操作时发生指令交错，就会出现问题
* 一段代码块内如果存在对共享资源的多线程读写操作，称这段代码块为临界区

例如，下面代码中的临界区

 ![image-20260129101717264](assets/image-20260129101717264.png)



**竞争条件**

多个线程在临界区内执行，由于代码的**执行序列不同**而导致结果无法预测，称之为发生了**竞态条件**



#### 变量的线程安全分析

**成员变量和静态变量是否线程安全？**

* 如果它们没有共享，则线程安全
* 如果它们被共享了，根据它们的状态是否能够改变，又分两种情况
  * 如果只有读操作，则线程安全 
  * 如果有读写操作，则这段代码是临界区，需要考虑线程安全

> **成员变量一定要注意是否有线程安全问题**



**局部变量是否线程安全？** 

* 局部变量是线程安全的

  ```java
  public static void test1() {
      int i = 10;
      i++;
  }
  ```

  > 每个线程调用 test1() 方法时局部变量 i，会在**每个线程的栈帧内存中**被创建，因此不存在共享
  >
  > 

* 但局部变量引用的对象则未必

  * 如果该对象没有逃离方法的作用访问，它是线程安全的
  * 如果该对象逃离方法的作用范围，需要考虑线程安全



> 
>
> 方法访问修饰符带来的思考，如果把 method2 和 method3 的方法修改为 public 会不会代理线程安全问题？
>
> * 情况1：有其它线程调用 method2 和 method3 ——不会有线程安全问题
> * 情况2：在 情况1 的基础上，为 ThreadSafe 类添加子类，子类覆盖 method2 或 method3 方法——有线程安全问题：
>
> ```java
> class ThreadSafe {
>     public final void method1(int loopNumber) {
>         ArrayList<String> list = new ArrayList<>();
>         for (int i = 0; i < loopNumber; i++) {
>             method2(list);
>             method3(list);
>         }
>     }
>     public void method2(ArrayList<String> list) {
>         list.add("1");
>     }
>     public void method3(ArrayList<String> list) {
>         list.remove(0);
>     }
> }
> 
> class ThreadSafeSubClass extends ThreadSafe{
>     @Override
>     public void method3(ArrayList<String> list) {
>         new Thread(() -> {
>             list.remove(0);
>         }).start();
>     }
> }
> 
> ```
>
> 就是方法3里开了个新线程，新线程可以访问到list，此时就有两个线程共享list这个资源了 就会出现线程安全问题(可能执行删除的时候，还没执行添加操作)
>
> 所以访问修饰符 private  / final 是有意义的，能避免子类重写









### ==解决方案synchronized==

为了避免临界区的竞态条件发生，有多种手段可以达到目的。 

* 阻塞式的解决方案：synchronized，Lock
* 非阻塞式的解决方案：原子变量



synchronized，即俗称的【对象锁】，它采用互斥的方式让同一时刻至多只有一个线程能持有【对象锁】，其它线程再想获取这个【对象锁】时就会阻塞住。这样就能保证拥有锁 的线程可以安全的执行临界区内的代码，不用担心线程上下文切换

> 互斥：
>
> 保证临界区的代码片段同一时间内只有一个线程能够执行，不会因为线程的上下文切换导致指令的交错

> 如果共享变量能够**完全被**`synchronized`代码块保护，就不会有可见性、有序性、原子性的相关问题
>
> > 虽然也有指令重排，但是由于被完全保护，单线程环境下的指令重排不会出现问题

```java
synchronized(对象)
{
    临界区
}
```





```java
static int counter = 0;
static final Object room = new Object();

public static void main(String[] args) throws InterruptedException {
	Thread t1 = new Thread(() -> {
	for (int i = 0; i < 5000; i++) {
        synchronized (room) {
            counter++;
        }
    }
}, "t1");
    
Thread t2 = new Thread(() -> {
    for (int i = 0; i < 5000; i++) {
        synchronized (room) {
            counter--;
        }
    }
}, "t2");

t1.start();
t2.start();
t1.join();
t2.join();
log.debug("{}",counter);
}
```



类比： 

* `synchronized(对象)` 中的对象，可以想象为一个房间（room），有唯一入口（门）房间只能一次进入一人 进行计算，线程 t1，t2 想象成两个人
* 当线程 t1 执行到 synchronized(room) 时就好比 t1 进入了这个房间，并锁住了门拿走了钥匙，在门内执行 `count++`代码
* 这时候如果 t2 也运行到了 `synchronized(room)` 时，它发现门被锁住了，只能在门外等待，发生了上下文切换，阻塞住了
* 这**中间即使 t1 的 cpu 时间片不幸用完，被踢出了门外（不要错误理解为锁住了对象就能一直执行下去哦）， 这时门还是锁住的，t1 仍拿着钥匙，t2 线程还在阻塞状态进不来，只有下次轮到 t1 自己再次获得时间片时才 能开门进入**
* 当 t1 执行完 synchronized{} 块内的代码，这时候才会从 obj 房间出来并解开门上的锁，唤醒 t2 线程把钥匙给他。t2 线程这时才可以进入 obj 房间，锁住了门拿上钥匙，执行它的 count-- 代码

![image-20260129102507711](assets/image-20260129102507711.png)

synchronized 实际是用**对象锁**保证了**临界区内代码的原子性**，临界区内的代码对外是不可分割的，不会被线程切 换所打断。

为了加深理解，请思考下面的问题

* 如果把 synchronized(obj) 放在 for 循环的外面，如何理解？-- 原子性
  把整个循环里面的所有指令作为整体，在这些全部指令执行的过程中，不会被其他线程干扰

* 如果 t1 synchronized(obj1) 而 t2 synchronized(obj2) 会怎样运作？-- 锁对象

* 如果 t1 synchronized(obj) 而 t2 没有加会怎么样？如何理解？-- 锁对象

  和没加锁一样，锁只有线程一持有 线程二不用尝试获取锁，线程二不会因为锁而被阻塞



#### 八锁问题

如何判断锁的是谁 锁到底锁的是谁？

**锁会锁住：对象、Class**



**问题1**

两个同步方法，先执行发短信还是打电话

```java
public class dome01 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        new Thread(() -> { phone.sendMs(); }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() {
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    
    // 为什么加在方法上锁的是对象？ 
    // 实际上等价于这种写法:
    public void sendMs() {
        synchronized(this) {
            System.out.println("发短信");
        }
    }
    public void call() {
        synchronized (this) {
            System.out.println("打电话");
        }
    }
}
```

输出结果为

发短信

打电话

**为什么？ 如果你认为是顺序在前？ 这个答案是错误的 **

**问题2：**

**我们再来看：我们让发短信 延迟4s**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
}
```

TimeUnit

现在结果是什么呢？

结果：**==还是先发短信，然后再打电话 ==**

> 原因：sleep不会释放锁
>
> 并不是顺序执行，而是synchronized <u>锁住的对象</u>**是方法的调用者（也就是说，锁住的是对象）** 对于两个方法用的是同一个锁，谁先拿到谁先执行，另外一个等待
>
> ==对象锁==





**问题三**

加一个普通方法

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.hello(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果为

hello

发短信

> 原因：hello是一个**普通方法，不受synchronized锁的影响**，==**不用等待锁的释放**==
>
> 



**问题四**

**如果我们使用的是两个对象，一个调用发短信，一个调用打电话，那么整个顺序是怎么样的呢？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone2.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：<u>两个对象两把锁，不会出现等待的情况</u>，发短信睡了4s,所以先执行打电话



**问题五、六**

**如果我们把两个synchronized的方法加上static变成静态方法 那么顺序又是怎么样的呢？**

（1）我们先来使用一个对象调用两个方法 

答案是：**先发短信,后打电话**

（2）如果我们使用两个对象调用两个方法 

答案是：**还是先发短信，后打电话**

原因是什么呢？ 为什么加了static就始终前面一个对象先执行呢 为什么后面会等待呢？
原因是：**对于static静态方法来说，对于整个类Class来说只有一份，对于不同的对象使用的是同一份方法，相当于==这个方法是属于这个类的==，如果静态static方法使用synchronized锁定，那么这个synchronized锁会锁住整个类 不管多少个对象，各个静态方法使用的是同一把锁，谁先拿到这个锁就先执行，其他的进程都需要等待 **







**问题七**

**如果我们使用一个静态同步方法、一个同步方法、一个对象调用顺序是什么？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.call(); }).start();
    }
}

class Phone {
    public static synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：因为<u>一个锁的是Class类的模板，一个锁的是对象的调用者</u>。所以**不存在等待，直接运行**。



**问题八**

**如果我们使用一个静态同步方法、一个同步方法、两个对象调用顺序是什么？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone2.call(); }).start();
    }
}

class Phone {
    public static synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：两把锁锁的不是同一个东西



**小结**

- 锁对象：new this 具体的一个手机
- 锁class：static Class 唯一的一个模板



#### 锁应用案例

测试下面代码是否存在线程安全问题，并尝试改正

```java
public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账2000次后的总金额
        log.debug("total:{}",(a.getMoney() + b.getMoney()));
    }
    // Random 为线程安全
    static Random random = new Random();
    // 随机 1~100
    public static int randomAmount() {
        return random.nextInt(100) +1;
    }
}
class Account {
    private int money;
    public Account(int money) {
        this.money = money;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void transfer(Account target, int amount) {
        if (this.money > amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
    }
}

```

> 错误修正
>
> ```java
> public synchronized void transfer(Account target, int amount) {
>     if (this.money > amount) {
>         this.setMoney(this.getMoney() - amount);
>         target.setMoney(target.getMoney() + amount);
>     }
> }
> ```
>
> 只能锁住this(调用这个方法的对象)，而传入的target对象不会被锁住

正确方式：

```java
public void transfer(Account target, int amount) {
    synchronized (Account.class) {// 把调用这个方法的对象this和传入的对象都锁住
        if (this.money > amount) {
            this.setMoney(this.getMoney() - amount);
            target.setMoney(target.getMoney() + amount);
        }
    }
    
}
```

> 会锁住所有账户，效率不行，还可以改进







### Synchronized原理-Monitor



![image-20260129173719926](assets/image-20260129173719926.png)

> 普通对象对象头用八个字节存储( 4 个字节的 Mark Word，4 个自己的 Klass Word)
>
> Klass Word 是个指针，指向这个对象的类（用于判断类是什么类型）
>
> 

![image-20260129173853712](assets/image-20260129173853712.png)

> Normal：哈希码；分代年龄；是否是偏向锁；加锁状态
>
> * 哈希码用的时候才产生，默认是0，只有第一次调用对象的hashcode，对象的哈希码才会产生，才在对象头的markword里面填充哈希码
>
> ![image-20260130152905465](assets/image-20260130152905465.png)
>
> 



#### Monitor

Monitor是synchronized的底层原理，称之为monitor。

Monitor 通常被翻译为**监视器**或**管程** 

每个 Java 对象都可以关联一个 Monitor 对象，如果使用 synchronized 给对象上锁（重量级）之后，该对象头的 Mark Word 中就被设置指向 Monitor 对象的指针 

![s](assets/image-20260129175210665.png)



* 刚开始 Monitor 中 Owner 为 null

* 当Thread-2 执行 synchronized(obj)里面的代码时 就会将 **obj 对象和**操作系统层面提供的 **Monitor相关联(在 obj 里面记录 monitor 对象的指针地址)**，将 Monitor 的Owner置为 Thread-2

* 在 Thread-2 上锁的过程中，如果 Thread-3、 Thread-4、 Thread-5 也希望执行 synchronized(obj) 里面的代码，就会先判断 obj 是否关联和Monitor；

  * 如果关联了 monitor 锁：判断monitor 锁是否有owner 

    * 有owner了：Thread-3、 Thread-4、 Thread-5就会进入Monitor 的 EntryList（等待队列/阻塞队列）

      > ![image-20260130152159137](assets/image-20260130152159137.png)

* 执行完代码之后，就会**根据一定规则**唤醒 EntryList 里面的某个线程
  (先来先服务，优先级，最短时间优先......)

* WaitSet中的线程是之前获得过锁、但条件不满足进入 WAITING 状态的线程

  > ![image-20260130152527816](assets/image-20260130152527816.png)

**注意**：

* synchronized 必须是进入同一个对象的 monitor 才有上述效果
  另一个对象 关联 另一个 Monitor 锁
* 不加 synchronized 的对象不会做monitor的检查 ，不会看对象头上的 Mark Word 、不会执行monitor相关的逻辑



#### 原理讲解-字节码层面

```java
static final Object lock = new Object();
static int counter = 0;
public static void main(String[] args) {
    synchronized (lock) {
        counter++;
    }
}
```

对应的字节码为：

```java
public static void main(java.lang.String[]);
	descriptor: ([Ljava/lang/String;)V
	flags: ACC_PUBLIC, ACC_STATIC
	Code:
 		stack=2, locals=3, args_size=1
            0: getstatic #2 	// <- lock引用 （synchronized开始）
            3: dup
            4: astore_1 		// lock引用 -> slot 1
            5: monitorenter 	// 将 lock对象 MarkWord 置为 Monitor 指针
            6: getstatic #3 	// <- i
            9: iconst_1 		// 准备常数 1
            10: iadd 			// +1
            11: putstatic #3 	// -> i
            14: aload_1 		// <- lock引用
            15: monitorexit 	// 将 lock对象 MarkWord 重置, 唤醒 EntryList 里面的进程 竞争锁
            16: goto 24
            19: astore_2 		// e -> slot 2
            20: aload_1 		// <- lock引用
            21: monitorexit 	// 将 lock对象 MarkWord 重置, 唤醒 EntryList
            22: aload_2 		// <- slot 2 (e)
            23: athrow			// throw e
            24: return
            // 19~23 如果代码块执行过程中出现了异常，就会执行这部分，正常释放锁
        Exception table: // 监听异常的范围
            from to target type
            6    16   19   any
            19   22   19   any
		LineNumberTable:
            line 8: 0
            line 9: 6
            line 10: 14
            line 11: 24
        LocalVariableTable:
            Start Length Slot Name Signature
            0     25     0    args [Ljava/lang/String;
		StackMapTable: number_of_entries = 2
			frame_type = 255 /* full_frame */
				offset_delta = 19
				locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
				stack = [ class java/lang/Throwable ]
			frame_type = 250 /* chop */
				offset_delta = 4

```

> 注意
>
> 方法级别的 synchronized 不会在字节码指令中有所体现









> ##### 小故事 
>
> 故事角色 
>
> * 老王 - JVM
> * 小南 - 线程 
> * 小女 - 线程 
> * 房间 - 对象 
> * 房间门上 - 防盗锁 - Monitor 
> * 房间门上 - 小南书包 - 轻量级锁 
> * 房间门上 - 刻上小南大名 - 偏向锁 
> * 批量重刻名 - 一个类的偏向锁撤销到达 20 阈值 
> * 不能刻名字 - 批量撤销该类对象的偏向锁，设置该类不可偏向 
>
> 小南要使用房间保证计算不被其它人干扰（原子性），最初，他用的是防盗锁，当上下文切换时，锁住门。这样， 即使他离开了，别人也进不了门，他的工作就是安全的。
>
>  但是，很多情况下没人跟他来竞争房间的使用权。小女是要用房间，但使用的时间上是错开的，小南白天用，小女 晚上用。每次上锁太麻烦了，有没有更简单的办法呢？ 
>
> 小南和小女商量了一下，约定不锁门了，而是谁用房间，谁把自己的书包挂在门口，但他们的书包样式都一样，因 此每次进门前得翻翻书包，看课本是谁的，如果是自己的，那么就可以进门，这样省的上锁解锁了。万一书包不是 自己的，那么就在门外等，**并通知对方下次用锁门的方式**。(<u>发现有竞争之后，就不再使用轻量级锁，升级为重量级锁</u>)
>
> 后来，小女回老家了，很长一段时间都不会用这个房间。小南每次还是挂书包，翻书包，虽然比锁门省事了，但仍 然觉得麻烦。 于是，小南干脆在门上刻上了自己的名字：【小南专属房间，其它人勿用】，下次来用房间时，只要名字还在，那 么说明没人打扰，还是可以安全地使用房间。如果这期间有其它人要用这个房间，那么由使用者将小南刻的名字擦 掉，升级为挂书包的方式。 
>
> 同学们都放假回老家了，小南就膨胀了，在 20 个房间刻上了自己的名字，想进哪个进哪个。后来他自己放假回老 家了，这时小女回来了（她也要用这些房间），结果就是得一个个地擦掉小南刻的名字，升级为挂书包的方式。老 王觉得这成本有点高，提出了一种批量重刻名的方法，他让小女不用挂书包了，可以直接在门上刻上自己的名字 
>
> 后来，刻名的现象越来越频繁，老王受不了了：算了，这些房间都不能刻名了，只能挂书包



### Synchronized原理进阶



#### 1.轻量级锁

轻量级锁的使用场景：如果一个对象虽然有多线程要加锁，但加锁的时间是错开的（也就是没有竞争），那么可以 使用轻量级锁来优化。 

轻量级锁对使用者是透明的，即语法仍然是 synchronized 

假设有两个方法同步块，利用同一个对象加锁

```java
static final Object obj = new Object();
public static void method1() {
    synchronized( obj ) {
        // 同步块 A
        method2();
    }
}
public static void method2() {
    synchronized( obj ) {
        // 同步块 B
    }
}
```



* 创建锁记录（Lock Record）对象，每个线程都的栈帧都会包含一个锁记录的结构，内部可以存储锁定对象的 Mark Word

  ![image-20260130162128719](assets/image-20260130162956933.png)

* 让锁记录中 Object reference 指向锁对象，并尝试用 cas 替换 Object 的 Mark Word，将 Mark Word 的值存 入锁记录

  ![image-20260130162158061](assets/image-20260130163330103.png)

* 如果 cas 替换成功(数据交换成功)，对象头中存储了 锁记录地址和状态 00 ，表示由该线程给对象加锁，这时图示如下

  ![image-20260130162229309](assets/image-20260130163705658.png)

* 如果 cas 失败，有两种情况 

  * 如果是其它线程已经持有了该 Object 的轻量级锁，这时表明有竞争，进入锁膨胀过程 
  * 如果是自己执行了 synchronized **锁重入**，那么再添加一条 Lock Record 作为重入的计数(数据交换会失败，因为交换过了，但是这种情况的失败无影响，因为可以看到 Mark Word 里面的锁记录地址 是当前线程中的另一个锁记录地址)

![image-20260130162318854](assets/image-20260130162318854.png)

* 当退出 synchronized 代码块（解锁时）如果是取值为 null 的锁记录，表示有重入，这时重置锁记录，表示重入计数减一

![image-20260130162357531](assets/image-20260130162357531.png)

* 当退出 synchronized 代码块（解锁时）锁记录的值不为 null (最外层的一个锁)，这时使用 cas 将 Mark Word 的值恢复给对象 头 
  * 成功，则解锁成功 
  * 失败，<u>说明轻量级锁进行了锁膨胀或已经升级为重量级锁，进入重量级锁解锁流程</u>



#### 2.锁膨胀

如果在尝试加轻量级锁的过程中，CAS 操作无法成功，这时**一种情况就是有其它线程为此对象加上了轻量级锁（有竞争）**，这时需要进行锁膨胀，将轻量级锁变为重量级锁

```java
static Object obj = new Object();
public static void method1() {
    synchronized( obj ) {
        // 同步块
    }
}
```

* 当 Thread-1 进行轻量级加锁时，Thread-0 已经对该对象加了轻量级锁

![image-20260130164252690](assets/image-20260130164252690.png)

* 这时 Thread-1 **加轻量级锁失败，进入锁膨胀流程**——升级到重量级锁

  > 轻量级锁没有阻塞的说法，此时升级到重量级锁

  * 即为 Object 对象**申请 Monitor 锁**，让 **Object 的 Mark Word 指向 Monitor 重量级锁)**地址
  * 然后自己**进入 Monitor 的 EntryList** 

![image-20260130164322205](assets/image-20260130164322205.png)

* 当 Thread-0 退出同步块解锁时，使用 cas 将 Mark Word 的值恢复给对象头，失败。这时会**进入重量级解锁 流程**:  即按照 Monitor 地址找到 Monitor 对象，设置 Owner 为 null，唤醒 EntryList 中 BLOCKED 线程





#### 3.自旋优化

**重量级锁竞争的时候，还可以使用自旋来进行优化**，如果当前线程自旋成功（即这时候持锁线程已经退出了同步 块，释放了锁），这时当前线程就可以避免阻塞。 

> 自旋：让线程暂时不进入阻塞，而是循环几次
>
> 阻塞会有上下文切换，更消耗性能

自旋重试成功的情况

![image-20260130172107018](assets/image-20260130172107018.png)

自旋重试失败的情况

![image-20260130172126623](assets/image-20260130172126623.png)

* 自旋会占用 CPU 时间，单核 CPU 自旋就是无意义的(线程1占用了CPU，线程2没有CPU可用)，多核 CPU 自旋才能发挥优势，自旋优化适合多核CPU
* 在 Java 6 之后自旋锁是**自适应的**，比如对象刚刚的一次自旋操作成功过，那么认为这次自旋成功的可能性会 高，就多自旋几次；反之，就少自旋甚至不自旋，总之，比较智能。 
* **Java 7 之后不能控制是否开启自旋功能**









#### 4.偏向锁

轻量级锁在没有竞争时（就自己这个线程），每次锁 重入仍然需要执行 CAS 操作。 

Java 6 中引入了偏向锁来做进一步优化：<u>只有第一次使用 CAS 将**线程 ID** 设置到**对象的 Mark Word** 头(Mark Word 原本存的是**锁记录的地址**)，之后发现 这个线程 ID 是自己的就表示没有竞争，不用重新 CAS</u>。以后只要不发生竞争，这个对象就归该线程所有 

例如：

```java
static final Object obj = new Object();
public static void m1() {
    synchronized( obj ) {
        // 同步块 A
        m2();
    }
}
public static void m2() {
    synchronized( obj ) {
        // 同步块 B
        m3();
    }
}
public static void m3() {
    synchronized( obj ) {
        // 同步块 C
    }
}

```

![image-20260130172253150](assets/image-20260130172253150.png)

![image-20260130172302453](assets/image-20260130172302453.png)



**偏向状态**

![image-20260130190558361](assets/image-20260130193639699.png)

一个对象创建时： 

* 如果开启了偏向锁（默认开启），那么对象创建后，markword 值为 0x05 即最后 3 位为 101，这时它的 thread、epoch、age 都为 0 

* 偏向锁是默认是延迟的，不会在程序启动时立即生效，如果想避免延迟，可以加 VM 参数 - XX:BiasedLockingStartupDelay=0 来禁用延迟 

* 如果调用了hashcode()生成哈希码，就会撤销偏向状态(偏向状态和哈希码互斥)；因为不撤销，哈希码在 Mark Word 中没地方存

  > 轻量级锁、重量级锁调用 hashCode() 不会有问题，因为<u>轻量级锁哈希码存在线程栈帧的锁记录里面，重量级锁哈希码存在Monitor对象中</u>

* 如果没有开启偏向锁，那么对象创建后，markword 值为 0x01 即最后 3 位为 001



> Java15废弃了偏向锁



**撤销现象**

* 多线程访问同一个对象，会导致偏向锁被撤销
* 调用`wait() / notify()`会导致偏向锁被撤销 (等待、通知的机制只有重量级锁有)



**批量重偏向**

如果对象虽然被多个线程访问，但没有竞争，这时偏向了线程 T1 的对象仍有机会重新偏向 T2，重偏向会重置对象 的 Thread ID 

当撤销偏向锁阈值超过 20 次后，jvm 会这样觉得，我是不是偏向错了呢，于是会在给这些对象加锁时重新偏向至 加锁线程

> 注意：这个阈值针对的是<u>同一个类的不同实例对象</u>加锁的情况。

```java
rivate static void test3() throws InterruptedException {
    Vector<Dog> list = new Vector<>();
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 30; i++) {
            Dog d = new Dog();
            list.add(d);
            synchronized (d) { // 没有竞争，都是偏向锁；30个对象都是偏向线程 t1
                log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            }
        }
        synchronized (list) {
            list.notify();
        }
    }, "t1");
    t1.start();
    
    Thread t2 = new Thread(() -> {
        synchronized (list) {
            try {
                list.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug("===============> ");
        for (int i = 0; i < 30; i++) {
            Dog d = list.get(i);
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            synchronized (d) { // 对象被 t2 线程使用，撤销偏向状态（101），对象头末尾变成00 升级成轻量级锁
                log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            }
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            // 解锁之后，会发现对象的状态会变成不可偏向状态（末尾001）
        }
        // 而在循环到第20个对象之后，由于批量重偏向机制，会把剩下的对象的偏向状态都改成偏向t2
        
    }, "t2");
    t2.start();
}

```



**批量撤销**

当撤销偏向锁阈值达到 40 次后，jvm 会这样觉得，自己确实偏向错了，根本就不该偏向。于是 <u>整个类的所有对象</u> 都会变为不可偏向的(从第20次开始)，新建的对象也是不可偏向的

```java
static Thread t1,t2,t3;
private static void test4() throws InterruptedException {
    Vector<Dog> list = new Vector<>();
    int loopNumber = 39;
    t1 = new Thread(() -> {
        for (int i = 0; i < loopNumber; i++) {
            Dog d = new Dog();
            list.add(d);
            synchronized (d) {
                log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            }
        }
        LockSupport.unpark(t2);
    }, "t1");
    t1.start();
    t2 = new Thread(() -> {
        LockSupport.park();
        log.debug("===============> ");
        for (int i = 0; i < loopNumber; i++) { // 对象被 t2 线程使用，撤销偏向状态（101），对象头末尾变成00 升级成轻量级锁
            Dog d = list.get(i);
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            synchronized (d) {
                log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            }
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
        }
        LockSupport.unpark(t3);
    }, "t2");
    t2.start();
    t3 = new Thread(() -> {
        LockSupport.park();
        log.debug("===============> ");
        for (int i = 0; i < loopNumber; i++) {
            Dog d = list.get(i);
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            synchronized (d) {
                log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
            }
            log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
        }
    }, "t3");
    t3.start();
    t3.join();
    log.debug(ClassLayout.parseInstance(new Dog()).toPrintableSimple(true));
}

```



#### 5.锁消除



```java
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations=3)
@Measurement(iterations=5)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MyBenchmark {
    static int x = 0;
    @Benchmark
    public void a() throws Exception {
        x++;
    }
    @Benchmark
    public void b() throws Exception {
        Object o = new Object();
        synchronized (o) {
            x++;
        }
    }
}
```

![image-20260130203127415](assets/image-20260130203127415.png)

虽然b方法加锁了，但是两个方法的性能还是差不多

这是因为 JIT 即时编译器会对 Java 字节码进一步优化，对反复调用的方法进行进一步的优化

> JIT 发现局部变量o不可能被共享，加锁没有意义 于是会进行锁消除，把synchronized优化掉，真正执行的时候是没加锁的；所以性能特别相近
>
> 锁消除的开关是默认打开的，也可以通过JVM参数关闭





#### 6.锁粗化

对相同对象多次加锁，导致线程发生多次重入，可以使用锁粗化方式来优化，这不同于之前讲的细分锁的粒度

> 锁粗化就是，当多个方法重复调用锁synchronized ，比如在for 循环中，就可以相当于在synchronized中进行for循环，进行粗化



## 多把锁



#### 多把不相干的锁



> 一间大屋子有两个功能：睡觉、学习，互不相干。 
>
> 现在小南要学习，小女要睡觉，但如果只用一间屋子（一个对象锁）的话，那么并发度很低 
>
> 解决方法是准备多个房间（多个对象锁）

例如

```java
class BigRoom {
    public void sleep() {
        synchronized (this) {
            log.debug("sleeping 2 小时");
            Sleeper.sleep(2);
        }
    }
    public void study() {
        synchronized (this) {
            log.debug("study 1 小时");
            Sleeper.sleep(1);
        }
    }
}

```

执行

```java
BigRoom bigRoom = new BigRoom();
new Thread(() -> {
    bigRoom.study();
},"小南").start();
new Thread(() -> {
    bigRoom.sleep();
},"小女").start();

```

某次结果

```
12:13:54.471 [小南] c.BigRoom - study 1 小时
12:13:55.476 [小女] c.BigRoom - sleeping 2 小时

```

改进

```java
class BigRoom {
    private final Object studyRoom = new Object();
    private final Object bedRoom = new Object();
    public void sleep() {
        synchronized (bedRoom) {
            log.debug("sleeping 2 小时");
            Sleeper.sleep(2);
        }
    }
    public void study() {
        synchronized (studyRoom) {
            log.debug("study 1 小时");
            Sleeper.sleep(1);
        }
    }
}
   
```

某次执行结果

```
12:15:35.069 [小南] c.BigRoom - study 1 小时
12:15:35.069 [小女] c.BigRoom - sleeping 2 小时
```



**将锁的粒度细分** 

* 好处，是可以**增强并发度** 
* 坏处，如果一个线程需要同时获得多把锁，就容易发生死锁



## 活跃性



### 死锁

如果一个线程需要同时获取多把锁，这时就容易发生死锁 

> 例：
> `t1线程` 获得 `A对象` 锁，接下来想获取 `B对象` 的锁 `t2线程` 获得 `B对象` 锁，接下来想获取 `A对象` 的锁
>
> ```java
> Object A = new Object();
> Object B = new Object();
> Thread t1 = new Thread(() -> {
>     synchronized (A) {
>         log.debug("lock A");
>         sleep(1);
>         synchronized (B) {
>             log.debug("lock B");
>             log.debug("操作...");
>         }
>     }
> }, "t1");
> 
> Thread t2 = new Thread(() -> {
>     synchronized (B) {
>         log.debug("lock B");
>         sleep(0.5);
>         synchronized (A) {
>             log.debug("lock A");           
>             log.debug("操作...");
>         }
>     }
> }, "t2");
> t1.start();
> t2.start();
> 
> ```
>
> 结果
>
> ```
> 12:22:06.962 [t2] c.TestDeadLock - lock B
> 12:22:06.962 [t1] c.TestDeadLock - lock A 
> ```
>
> 

#### 定位死锁

* 检测死锁
  * 可以使用 jconsole工具，或者
  * 使用 jps 定位进程 id，再用 jstack 定位死锁：

```cmd
cmd > jps // 查看当前运行的所有进程
Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF-8
12320 Jps
22816 KotlinCompileDaemon
33200 TestDeadLock 			// JVM 进程
11508 Main
28468 Launcher
```



```cmd
cmd > jstack 33200 // 查看该进程中的线程信息
Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF-8
2018-12-29 05:51:40
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.91-b14 mixed mode):

"DestroyJavaVM" #13 prio=5 os_prio=0 tid=0x0000000003525000 nid=0x2f60 waiting on condition
[0x0000000000000000]
 java.lang.Thread.State: RUNNABLE

"Thread-1" #12 prio=5 os_prio=0 tid=0x000000001eb69000 nid=0xd40 waiting for monitor entry
[0x000000001f54f000]
   java.lang.Thread.State: BLOCKED (on object monitor) // 线程1阻塞
		at thread.TestDeadLock.lambda$main$1(TestDeadLock.java:28)
		- waiting to lock <0x000000076b5bf1c0> (a java.lang.Object) // 等待0x000000076b5bf1c0释放锁
		- locked <0x000000076b5bf1d0> (a java.lang.Object)
		at thread.TestDeadLock$$Lambda$2/883049899.run(Unknown Source)
		at java.lang.Thread.run(Thread.java:745)

"Thread-0" #11 prio=5 os_prio=0 tid=0x000000001eb68800 nid=0x1b28 waiting for monitor entry
[0x000000001f44f000]
   java.lang.Thread.State: BLOCKED (on object monitor) // 线程0阻塞
		at thread.TestDeadLock.lambda$main$0(TestDeadLock.java:15)
		
		- waiting to lock <0x000000076b5bf1d0> (a java.lang.Object) // 等待0x000000076b5bf1d0释放锁
		- locked <0x000000076b5bf1c0> (a java.lang.Object)
		at thread.TestDeadLock$$Lambda$1/495053715.run(Unknown Source)
		at java.lang.Thread.run(Thread.java:745)

// 略去部分输出

Found one Java-level deadlock: // 展示找到的死锁信息
=============================
"Thread-1":
  waiting to lock monitor 0x000000000361d378 (object 0x000000076b5bf1c0, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x000000000361e768 (object 0x000000076b5bf1d0, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above: // 指出出现循环等待的代码位置
===================================================
"Thread-1":
		at thread.TestDeadLock.lambda$main$1(TestDeadLock.java:28)
		- waiting to lock <0x000000076b5bf1c0> (a java.lang.Object)
		- locked <0x000000076b5bf1d0> (a java.lang.Object)
		at thread.TestDeadLock$$Lambda$2/883049899.run(Unknown Source)
		at java.lang.Thread.run(Thread.java:745)
"Thread-0":
		at thread.TestDeadLock.lambda$main$0(TestDeadLock.java:15)
		- waiting to lock <0x000000076b5bf1d0> (a java.lang.Object)
		- locked <0x000000076b5bf1c0> (a java.lang.Object)
		at thread.TestDeadLock$$Lambda$1/495053715.run(Unknown Source)
		at java.lang.Thread.run(Thread.java:745)

Found 1 deadlock.

```

* 避免死锁要注意加锁顺序 
* 另外如果由于某个线程进入了死循环，导致其它线程一直等待，对于这种情况 linux 下可以通过 top 先定位到 CPU 占用高的 Java 进程，再利用 top -Hp 进程id 来定位是哪个线程，最后再用 jstack 排查



#### 哲学家就餐问题

> 一个容易导致死锁的问题

![image-20260131165910184](assets/image-20260131165910184.png)

有五位哲学家，围坐在圆桌旁。 

* 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
* 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。 
* 如果筷子被身边的人拿着，自己就得等待 



筷子类：

```java
class Chopstick {
    String name;
    public Chopstick(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
```

哲学家类：

```java
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;
    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }
    private void eat() {
        log.debug("eating...");
        Sleeper.sleep(1);
    }

    @Override
    public void run() {
        while (true) {
            // 获得左手筷子
            synchronized (left) {
                // 获得右手筷子
                synchronized (right) {
                    // 吃饭
                    eat();
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }
}

```

就餐：

```java
Chopstick c1 = new Chopstick("1");
Chopstick c2 = new Chopstick("2");
Chopstick c3 = new Chopstick("3");
Chopstick c4 = new Chopstick("4");
Chopstick c5 = new Chopstick("5");

new Philosopher("苏格拉底", c1, c2).start();
new Philosopher("柏拉图", c2, c3).start();
new Philosopher("亚里士多德", c3, c4).start();
new Philosopher("赫拉克利特", c4, c5).start();
new Philosopher("阿基米德", c5, c1).start();
```

执行不多会，就执行不下去了

```
12:33:15.575 [苏格拉底] c.Philosopher - eating...
12:33:15.575 [亚里士多德] c.Philosopher - eating...
12:33:16.580 [阿基米德] c.Philosopher - eating...
12:33:17.580 [阿基米德] c.Philosopher - eating...
// 卡在这里, 不向下运行
```

使用 jconsole 检测死锁，发现

```java
-------------------------------------------------------------------------
名称: 阿基米德
状态: cn.itcast.Chopstick@1540e19d (筷子1) 上的BLOCKED, 拥有者: 苏格拉底
总阻止数: 2, 总等待数: 1
堆栈跟踪:
cn.itcast.Philosopher.run(TestDinner.java:48)
 - 已锁定 cn.itcast.Chopstick@6d6f6e28 (筷子5)
-------------------------------------------------------------------------
名称: 苏格拉底
状态: cn.itcast.Chopstick@677327b6 (筷子2) 上的BLOCKED, 拥有者: 柏拉图
总阻止数: 2, 总等待数: 1
堆栈跟踪:
cn.itcast.Philosopher.run(TestDinner.java:48)
 - 已锁定 cn.itcast.Chopstick@1540e19d (筷子1)
-------------------------------------------------------------------------
名称: 柏拉图
状态: cn.itcast.Chopstick@14ae5a5 (筷子3) 上的BLOCKED, 拥有者: 亚里士多德
总阻止数: 2, 总等待数: 0
堆栈跟踪:
cn.itcast.Philosopher.run(TestDinner.java:48)
 - 已锁定 cn.itcast.Chopstick@677327b6 (筷子2)
-------------------------------------------------------------------------
名称: 亚里士多德
状态: cn.itcast.Chopstick@7f31245a (筷子4) 上的BLOCKED, 拥有者: 赫拉克利特
总阻止数: 1, 总等待数: 1
堆栈跟踪:
cn.itcast.Philosopher.run(TestDinner.java:48)
 - 已锁定 cn.itcast.Chopstick@14ae5a5 (筷子3)
-------------------------------------------------------------------------
名称: 赫拉克利特
状态: cn.itcast.Chopstick@6d6f6e28 (筷子5) 上的BLOCKED, 拥有者: 阿基米德
总阻止数: 2, 总等待数: 0
堆栈跟踪:
cn.itcast.Philosopher.run(TestDinner.java:48)
 - 已锁定 cn.itcast.Chopstick@7f31245a (筷子4)
```

这种线程没有按预期结束，执行不下去的情况，归类为【活跃性】问题，除了死锁以外，还有活锁和饥饿者两种情 况



#### 活锁

活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束，例如

```java
public class TestLiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();
    public static void main(String[] args) {
        new Thread(() -> { 
            // 期望减到 0 退出循环
            while (count > 0) {
                sleep(0.2);
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                sleep(0.2);
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}

```

> **也可以使用随机睡眠时间的方法来避免活锁**







#### 饥饿者

很多教程中把饥饿定义为，一个线程由于优先级太低，始终得不到 CPU 调度执行，也不能够结束，饥饿的情况不易演示，讲读写锁时会涉及饥饿问题 

下面我讲一下我遇到的一个线程饥饿的例子，先来看看**使用顺序加锁的方式解决之前的死锁问题**

![image-20260131171303124](assets/image-20260131171303124.png)

![image-20260131171315848](assets/image-20260131171315848.png)

但是顺序加锁容易产生饥饿问题









## ReentrantLock

可重入锁

> 死锁、饥饿都可以用ReentrantLock解决
>
> 公平锁，可以避免饥饿现象



相对于 synchronized 它具备如下特点 

* 可中断 

  > 这里的中断是指别的线程可以破坏你的blocking状态，而不是指自己中断阻塞状态

* 可以设置超时时间 

* 可以设置为公平锁 

* 支持多个条件变量 

  > 支持多个WaitSet，Synchronized支持一个WaitSet

**与 synchronized 一样，都支持可重入**



基本语法

```java
ReentrantLock lock = new ReentrantLock();
// 获取锁
lock.lock();
try {
    // 临界区代码
} finally {
    // 释放锁
    reentrantLock.unlock(); // 保证是否有异常都会正常释放锁
}

```

> synchronized是关键字级别，ReentranLock是对象级别保护临界区  需要创建对象

> 在try块外面创建 （阿里规范）









#### 可重入

可重入是指同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁 

如果是不可重入锁，那么第二次获得锁时，自己也会被锁挡住

```java
static ReentrantLock lock = new ReentrantLock();
public static void main(String[] args) {
    method1();
}
public static void method1() {
    lock.lock();
    try {
        log.debug("execute method1");
        method2();
    } finally {
        lock.unlock();
    }
}
public static void method2() {
    lock.lock();
    try {
        log.debug("execute method2");
        method3();
    } finally {
        lock.unlock();
    }
}
public static void method3() {
    lock.lock();
    try {
        log.debug("execute method3");
    } finally {
        lock.unlock();
    }
}
```

输出

```
17:59:11.862 [main] c.TestReentrant - execute method1
17:59:11.865 [main] c.TestReentrant - execute method2
17:59:11.865 [main] c.TestReentrant - execute method3 
```



#### 可打断

示例

```java
ReentrantLock lock = new ReentrantLock();
Thread t1 = new Thread(() -> {
    debug("启动...");
    try {
        lock.lockInterruptibly(); // lockInterruptibly()方法是可打断的，lock()方法是不可打断的
        // 【如果没有竞争，那么此方法就会获取lock对象锁】
        // 【如果其他线程持有了锁，那么就会进入阻塞队列，可以被其他线程用interrupt打断阻塞状态】
    } catch (InterruptedException e) {
        e.printStackTrace();
        log.debug("等锁的过程中被打断");
        return;
    }
    try {
        log.debug("获得了锁");
    } finally {
        lock.unlock();
    }
}, "t1");

lock.lock();
log.debug("获得了锁");
t1.start();
try {
    sleep(1);
    t1.interrupt(); // 打断t1
    log.debug("执行打断");
} finally {
    lock.unlock();
}
```

输出

```
18:02:40.520 [main] c.TestInterrupt - 获得了锁
18:02:40.524 [t1] c.TestInterrupt - 启动...
18:02:41.530 [main] c.TestInterrupt - 执行打断
java.lang.InterruptedException
 at
java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireInterruptibly(AbstractQueuedSynchr
onizer.java:898)
 at
java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireInterruptibly(AbstractQueuedSynchron
izer.java:1222)
 at java.util.concurrent.locks.ReentrantLock.lockInterruptibly(ReentrantLock.java:335)
 at cn.itcast.n4.reentrant.TestInterrupt.lambda$main$0(TestInterrupt.java:17)
 at java.lang.Thread.run(Thread.java:748)
18:02:41.532 [t1] c.TestInterrupt - 等锁的过程中被打断
```

* 作用：防止无限制地在阻塞队列等待下去 (可用于打断死锁)
* 被打断之后会抛出 `InterruptedException` 异常

> synchorized的被打断：设置打断标识，根据打断标记是否为true来手动break或return
>
> 





注意如果是不可中断模式，那么即使使用了 interrupt 也不会让等待中断

```java
ReentrantLock lock = new ReentrantLock();
Thread t1 = new Thread(() -> {
    log.debug("启动...");
    lock.lock();
    try {
        log.debug("获得了锁");
    } finally {
        lock.unlock();
    }
}, "t1");

lock.lock();
log.debug("获得了锁");
t1.start();

try {
    sleep(1);
    t1.interrupt();
    log.debug("执行打断");
    sleep(1);
} finally {
    log.debug("释放了锁");
    lock.unlock();
}

```

输出

```
18:06:56.261 [main] c.TestInterrupt - 获得了锁
18:06:56.265 [t1] c.TestInterrupt - 启动...
18:06:57.266 [main] c.TestInterrupt - 执行打断 // 这时 t1 并没有被真正打断, 而是仍继续等待锁
18:06:58.267 [main] c.TestInterrupt - 释放了锁
18:06:58.267 [t1] c.TestInterrupt - 获得了锁
```



#### 锁超时

如果持有锁的的线程超过一定时间，就不继续等待 放弃等待，此次获取锁失败

* `tryLock()` 

  * 返回布尔值，false表示获取失败

  > 源码：
  >
  > ![image-20260131202558856](assets/image-20260131202558856.png)

* `lock.tryLock(1, TimeUnit.SECONDS)`——可设置等待时间

  如果等待时间内能获取到锁，就会停止等待  方法返回`true`

  > 需要用`try catch`捕获`InterruptedException`异常

  > `tryLock`方法也是可打断的

  > 源码：
  >
  > ![image-20260131201834053](assets/image-20260131201834053.png)
  >
  > 1. **参数转换**：首先将时间参数转换为纳秒单位，unit.toNanos(time) 将传入的时间转换成纳秒。
  > 2. **调用 AQS 的 tryAcquireNanos 方法**：这是 AbstractQueuedSynchronizer(AQS) 类中的一个核心方法，实现了带有超时功能的尝试获取同步状态。
  > 3. **AQS 的 tryAcquireNanos 实现逻辑**：
  >    * 首先检查当前线程是否被中断，如果是则抛出 [InterruptedException](file:///C:/Program Files/Java/jdk-17.0.5/lib/src/java.base/java/lang/InterruptedException.java#L54-L86)
  >    * 调用 tryAcquire 方法尝试立即获取锁，如果成功直接返回 true
  >    * 如果获取失败，计算超时截止时间
  >    * 将当前线程封装成节点加入到同步队列中
  >    * **在循环中持续尝试获取锁，直到成功或超时时间到达**
  >    * 如果超时仍未获取到锁，返回 false
  > 4. **超时机制**：该方法会在指定的时间内不断尝试获取锁，如果在超时时间内成功获取锁则返回 true，否则返回 false。

* 







**1.立刻失败**

```java
ReentrantLock lock = new ReentrantLock();
Thread t1 = new Thread(() -> {
    log.debug("t1线程启动...");
    if (!lock.tryLock()) {
        log.debug("获取立刻失败，返回");
        return;
    }
    try {
        log.debug("获得了锁");
    } finally {
        lock.unlock();
    }
}, "t1");
lock.lock();
log.debug("主线程获得了锁");

t1.start();
try {
    sleep(2);
} finally {
    lock.unlock();
}

```

输出

```
18:15:02.918 [main] c.TestTimeout - 主线程获得了锁
18:15:02.921 [t1] c.TestTimeout - t1线程启动...
18:15:02.921 [t1] c.TestTimeout - 获取立刻失败，返回
```



**2.超时失败**

```java
ReentrantLock lock = new ReentrantLock();
Thread t1 = new Thread(() -> {
    log.debug("启动...");
    try {
        if (!lock.tryLock(1, TimeUnit.SECONDS)) {
            log.debug("获取等待 1s 后失败，返回");
            return;
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
        log.debug("获取不到锁");
        return ;
    }
    try {
        log.debug("获得了锁");
    } finally {
        lock.unlock();
    }
}, "t1");

lock.lock();
log.debug("获得了锁");
t1.start();
try {
    sleep(2);
} finally {
    lock.unlock();
}





```

输出

```
18:19:40.537 [main] c.TestTimeout - 获得了锁
18:19:40.544 [t1] c.TestTimeout - 启动...
18:19:41.547 [t1] c.TestTimeout - 获取等待 1s 后失败，返回
```



**使用 tryLock 解决哲学家就餐问题**

* 继承 `ReentrantLock`
* 使用 `tryLock()`或`tryLock(n,TimeUnit.SECONDS)`
* 如果不能同时获取到两个锁，就会释放锁，不会导致死锁问题

```java
class Chopstick extends ReentrantLock {
    String name;
    public Chopstick(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}

class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;
    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }
    @Override
    public void run() {
        while (true) {
            // 尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    // 尝试获得右手筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        } finally {
                            // 释放锁
                            right.unlock();
                        }
                    }
                } finally {
                    // 释放锁
                    left.unlock();
                }
            }
        }
    }
    private void eat() {
        log.debug("eating...");
        Sleeper.sleep(1);
    }
}

```



#### 公平锁

ReentrantLock 默认是不公平的

```java
ReentrantLock lock = new ReentrantLock(false);
lock.lock();

for (int i = 0; i < 500; i++) {
    new Thread(() -> {
        lock.lock();
        try {
          System.out.println(Thread.currentThread().getName() + " running...");
        } finally {
            lock.unlock();
        }
    }, "t" + i).start();
}

// 1s 之后去争抢锁
Thread.sleep(1000);
new Thread(() -> {
    System.out.println(Thread.currentThread().getName() + " start...");
    lock.lock();
    try {
        System.out.println(Thread.currentThread().getName() + " running...");
    } finally {
        lock.unlock();
    }
}, "强行插入").start();
lock.unlock();
```

强行插入，有机会在中间输出

> 注意：该实验不一定总能复现

```
t39 running...
t40 running...
t41 running...
t42 running...
t43 running...
强行插入 start...
强行插入 running...
t44 running...
t45 running...
t46 running...
t47 running...
t49 running... 
```

改为公平锁后 （`ReentrantLock lock = new ReentrantLock(true);`）

强行插入，总是在最后输出

```
t465 running...
t464 running...
t477 running...
t442 running...
t468 running...
t493 running...
t482 running...
t485 running...
t481 running...
强行插入 running... 
```

公平锁意图是解决饥饿问题；

但是公平锁一般没有必要，会降低并发度，后面分析原理时会讲解

> 可以使用tryLock，立刻失败/超时失败，让其他线程先处理





#### 条件变量

synchronized 中也有条件变量，就是我们讲原理时那个 waitSet 休息室，当条件不满足时进入 waitSet 等待

ReentrantLock 的条件变量比 synchronized 强大之处在于，它是支持多个条件变量的，这就好比 

* synchronized 是那些不满足条件的线程都在一间休息室等消息 
* 而 ReentrantLock 支持多间休息室，有专门等烟的休息室、专门等早餐的休息室、唤醒时也是按休息室来唤醒 

使用要点： 

* await 前需要获得锁 
* await 执行后，会释放锁，进入 conditionObject 等待 
* await 的线程被唤醒（或打断、或超时），会重新竞争 lock 锁 
* 竞争 lock 锁成功后，从 await 后继续执行



例子

```java
// static final Object room = new Object();
static ReentrantLock lock = new ReentrantLock();
// 创建条件队列
static Condition waitCigaretteQueue = lock.newCondition();
static Condition waitbreakfastQueue = lock.newCondition();

static volatile boolean hasCigrette = false;
static volatile boolean hasBreakfast = false;
public static void main(String[] args) {
    new Thread(() -> {
        try {
            lock.lock();
            while (!hasCigrette) {
                try {
                    waitCigaretteQueue.await(); // 到指定的条件队列等待
                    // room.wait()
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("等到了它的烟，可以开始干活了");
        } finally {
            lock.unlock();
        }
    }).start();
    new Thread(() -> {
        try {
            lock.lock();
            while (!hasBreakfast) {
                try {
                    waitbreakfastQueue.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("等到了它的早餐，可以开始干活了");
        } finally {
            lock.unlock();
        }
    }).start();
    sleep(1);
    sendBreakfast(); // 送早餐
    sleep(1);
    sendCigarette(); // 送烟
}
private static void sendCigarette() {
    lock.lock();
    try {
        log.debug("送烟来了");
        hasCigrette = true;
        // 唤醒指定“休息室”里面的线程
        waitCigaretteQueue.signal();
    } finally {
        lock.unlock();
    }
}
private static void sendBreakfast() {
    lock.lock();
    try {
        log.debug("送早餐来了");
        hasBreakfast = true;
        waitbreakfastQueue.signal();
    } finally {
        lock.unlock();
    }
}

```

> **源码**：
>
> Condition接口类：
>
> ![image-20260131210014077](assets/image-20260131210014077.png)
>
> AbstractQueuedSynchronizer实现类
>
> ![image-20260131210116343](assets/image-20260131210116343.png)
>
> 底层基于队列
>
> ![image-20260131210149977](assets/image-20260131210149977.png)
> 



### ReentrantLock原理

> 在Java层面实现Monitor





# ==共享模型-内存==











## JMM



#### 什么是JMM

**JMM：JAVA内存模型**——Java Memory Model，它定义了主存、工作内存抽象概念，<u>底层对应着</u> CPU 寄存器、缓存、硬件内存、 CPU 指令优化等



JMM 体现在以下几个方面 

* 原子性 - 保证指令不会受到线程上下文切换的影响 
* 可见性 - 保证指令不会受 cpu 缓存的影响 
* 有序性 - 保证指令不会受 cpu 指令并行优化的影响





**关于JMM的一些同步的约定：**

1、线程加锁前，必须**读取主存**中的最新值到工作内存中；

2、线程解锁前，必须把共享变量**立刻**刷回主存；

3、加锁和解锁是同一把锁；

线程中分为 **工作内存、主内存**

**8种操作:**

读->加载->使用->赋值（assign）->写->存储

加锁、解锁

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130205242466.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130205246301.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

内存交互操作有8种，虚拟机实现必须保证每一个操作都是原子的，不可在分的（对于double和long类型的变量来说，load、store、read和write操作在某些平台上允许例外）

- **Read（读取）**：作用于主内存变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用；
- **load（载入）**：作用于工作内存的变量，它把read操作从主存中变量放入工作内存中；
- **Use（使用）**：作用于工作内存中的变量，它把工作内存中的变量传输给执行引擎，每当虚拟机遇到一个需要使用到变量的值，就会使用到这个指令；
- **assign（赋值）**：作用于工作内存中的变量，它把一个从执行引擎中接受到的值放入工作内存的变量副本中；
- **store（存储）**：作用于主内存中的变量，它把一个从工作内存中一个变量的值传送到主内存中，以便后续的write使用；
- **write（写入）**：作用于主内存中的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中；
- **lock（锁定）**：作用于主内存的变量，把一个变量标识为线程独占状态；
- **unlock（解锁）**：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定；



**JMM对这八种指令的使用，制定了如下规则：**

- 不允许read和load、store和write操作之一单独出现，必须成对使用。即使用了read必须load，使用了store必须write
- 不允许线程丢弃他最近的assign操作，即工作变量的数据改变了之后，必须告知主存
- 不允许一个线程将没有assign的数据从工作内存同步回主内存
  一个新的变量必须在主内存中诞生，不允许工作内存直接使用一个未被初始化的变量。就是怼变量实施use、store操作之前，必须经过assign和load操作
- 一个变量同一时间只有一个线程能对其进行lock。多次lock后，必须执行相同次数的unlock才能解锁
- 如果对一个变量进行lock操作，会清空所有工作内存中此变量的值，在执行引擎使用这个变量前，必须重新load或assign操作初始化变量的值
- 如果一个变量没有被lock，就不能对其进行unlock操作。也不能unlock一个被其他线程锁住的变量对一个变量进行unlock操作之前，必须把此变量同步回主内存
  问题： 程序不知道主内存的值已经被修改过了
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/2021013020545399.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)



### 可见性



> #### 退不出的循环 
>
> 先来看一个现象，main 线程对 run 变量的修改对于 t 线程不可见，导致了 t 线程无法停止：
>
> ```java
> static boolean run = true;
> public static void main(String[] args) throws InterruptedException {
>     Thread t = new Thread(()->{
>         while(run){
>             // ....
>         }
>     });
>     t.start();
>     sleep(1);
>     run = false; // 线程t不会如预想的停下来
> }
> 
> ```
>
> > 停下来的同学可能是在while里面加了println吧，因为println是一个线程安全的方法 ，底层有synchronized，而synchronized保证了可见性，不会一直循环

为什么呢？分析一下： 

1. 初始状态， t 线程刚开始从主内存读取了 run 的值到工作内存。

   ![image-20260201100729892](assets/image-20260201100729892.png)

2. 因为 t 线程要频繁从主内存中读取 run 的值，JIT 编译器会将 run 的值缓存至自己工作内存中的高速缓存中， 减少对主存中 run 的访问，提高效率

   ![image-20260201100747600](assets/image-20260201100747600.png)

3. 1 秒之后，main 线程修改了 run 的值，并**同步至主存**，而 t 是从**自己工作内存中的高速缓存中**读取这个变量 的值，结果永远是旧值

   ![image-20260201100823527](assets/image-20260201100823527.png)



**解决方法**

**1.volatile（易变关键字）** 

> 能保证可见性，不能保证原子性，适合一写多读的场景

```java
volatile static boolean run = true;
public static void main(String[] args) throws InterruptedException {
    Thread t = new Thread(()->{
        while(run){
            // ....
        }
    });
    t.start();
    sleep(1);
    run = false; // 线程t会如预想的停下来
}
```



- volatile 变量在修改后，会插入**写屏障（StoreBarrier）**，强制将该变量的最新值从 CPU 缓存写回主内存；
- 同时，通过**缓存一致性协议（如 Intel 的 MESI 协议）**，其他 CPU 核心会感知到该变量的缓存行已失效（Invalid），在下一次读取时必须从主内存重新加载最新值；
- 读取 volatile 变量前会插入**读屏障（LoadBarrier）**，确保不会使用过期的缓存值，而是从主内存或最新缓存中获取。

> 因此，volatile 的可见性并非“跳过缓存直接操作主存”，而是通过**控制缓存同步时机**和**禁止指令重排序**，保证多线程环境下变量的修改对所有线程立即可见。
>
> 

**2.synchronized**

```java
static boolean run = true;
final Object lock = new Object();
public static void main(String[] args) throws InterruptedException {
    Thread t = new Thread(()->{
        while(true){
            // ....
            synchronized(lock) {
                if(!run){
                    break;
                }
            }
        }
    });
    t.start();
    sleep(1);
    synchronized(lock) {
        run = false; // 线程t会如预想的停下来
    }
}
```

> 两者都能保证可见性，但是synchronized更重、需要关联Monitor，volatile更轻量
>
> 



**可见性 vs 原子性**

前面例子体现的实际就是可见性，它保证的是在多个线程之间，一个线程对 volatile 变量的修改对另一个线程可 见， 不能保证原子性，仅用在一个写线程，多个读线程的情况： 上例从字节码理解是这样的：

```
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
getstatic run // 线程 t 获取 run true
putstatic run // 线程 main 修改 run 为 false， 仅此一次
getstatic run // 线程 t 获取 run false 
```

思考一下之前我们将线程安全时举的例子：两个线程一个 i++ 一个 i-- ，是否能用volatile解决？

> 不能 volatile 只能保证看到最新值，不能解决指令交错

```cmd
/ 假设i的初始值为0
getstatic 	i // 线程2-获取静态变量i的值 线程内i=0

getstatic 	i // 线程1-获取静态变量i的值 线程内i=0
iconst_1 	  // 线程1-准备常量1
iadd 		  // 线程1-自增 线程内i=1
putstatic   i // 线程1-将修改后的值存入静态变量i 静态变量i=1

iconst_1 	  // 线程2-准备常量1
isub 		  // 线程2-自减 线程内i=-1
putstatic   i // 线程2-将修改后的值存入静态变量i 静态变量i=-1 

```

> **注意** synchronized 语句块既可以保证代码块的原子性，也同时保证代码块内变量的可见性。但缺点是 synchronized 是属于重量级操作，性能相对更低 
>
> 如果在前面示例的死循环中加入 System.out.println() 会发现即使不加 volatile 修饰符，线程 t 也能正确看到 对 run 变量的修改了，想一想为什么？
>
> ![image-20260201110954164](assets/image-20260201110954164.png)













#### volatile 原理

volatile 的底层实现原理是内存屏障，Memory Barrier（Memory Fence） 

* 对 volatile 变量的写指令后会加入写屏障 
* 对 volatile 变量的读指令前会加入读屏障



##### 1.如何保证可见性

* 写屏障（sfence）保证在该屏障之前的，对共享变量的改动，都同步到主存当中

```java
public void actor2(I_Result r) {
    num = 2;
    ready = true; // (ready被volatile修饰)
    // 写屏障加在赋值操作之后
}
```

* 而读屏障（lfence）保证在该屏障之后，对共享变量的读取，加载的是主存中最新数据(从主存加载最新数据到高速缓存中)

```java
public void actor1(I_Result r) {
    // 读屏障加在读取操作之前
    if(ready) {
        r.r1 = num + num;
    } else {
        r.r1 = 1;
    }
}
```

![image-20260201151101139](assets/image-20260201151101139.png)



##### 2.如何保证有序性

* 写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后

```java
public void actor2(I_Result r) {
    num = 2;
    ready = true; // (ready被volatile修饰)
    // 写屏障
}

```

* 读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前

```java
public void actor1(I_Result r) {
    // 读屏障
    if(ready) {
        r.r1 = num + num;
    } else {
        r.r1 = 1;
    }
}
```

![image-20260201151236530](assets/image-20260201151236530.png)

还是那句话，不能解决指令交错(保证了有序性和可见性，不能保证原子性)： 

* 写屏障仅仅是保证之后的读能够读到最新的结果，但不能保证读跑到它前面去 
* 而有序性的保证也只是保证了本线程内相关代码不被重排序



![image-20260201151315033](assets/image-20260201151315033.png)

##### 3.double-checked locking 问题

以著名的 double-checked locking 单例模式为例`

```java
public final class Singleton {
    private Singleton() { }
    private static Singleton INSTANCE = null;
    public static Singleton getInstance() {
        if(INSTANCE == null) { // 因为只有第一次之后的使用不需要线程安全保护；两次检查，只让第一次访问的时候加锁
            // t2
            // 首次访问会同步，而之后的使用不需要线程安全保护
            synchronized(Singleton.class) {
                if (INSTANCE == null) { // t1
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}

```

以上的实现特点是：

* 懒惰实例化 
* 首次使用 getInstance() 才使用 synchronized 加锁，后续使用时无需加锁 
* 有隐含的，但很关键的一点：第一个 if 使用了 INSTANCE 变量，是在同步块之外

但在多线程环境下，上面的代码是有问题的，getInstance 方法对应的字节码为：

> 由于synchronized代码块之外的INSTANCE变量不受保护，由于会有指令重排，可能导致问题

```
0: getstatic 		#2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
3: ifnonnull 		37
6: ldc 				#3 // class cn/itcast/n5/Singleton
8: dup
9: astore_0
10: monitorenter
11: getstatic 		#2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 		27

17: new 			#3 // class cn/itcast/n5/Singleton
20: dup
21: invokespecial 	#4 // Method "<init>":()V
24: putstatic 		#2 // Field INSTANCE:Lcn/itcast/n5/Singleton;

27: aload_0
28: monitorexit
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic 		#2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn
```

其中 

* 17 表示创建对象，将对象引用入栈 // new Singleton 
* 20 表示复制一份对象引用 // 引用地址 
* 21 表示利用一个对象引用，调用构造方法 
* 24 表示利用一个对象引用，赋值给 static INSTANCE 

也许 jvm 会优化为：先执行 24，再执行 21。如果两个线程 t1，t2 按如下时间序列执行：

![image-20260201151645710](assets/image-20260201155851561.png)关键在于 0: getstatic 这行代码在 monitor 控制之外，它就像之前举例中不守规则的人，可以越过 monitor 读取 INSTANCE 变量的值 

**这时 t1 还未完全将构造方法执行完毕，如果在构造方法中要执行很多初始化操作，那么 t2 拿到的是将是一个未初 始化完毕的单例** 

([05.016-volatile-原理-dcl-问题分析_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV16J411h7Rd?spm_id_from=333.788.player.switch&vd_source=67ef3bb4c8d68a96408acdaa865b1313&p=149))

对 INSTANCE 使用 volatile 修饰即可，可以禁用指令重排，但要注意在 JDK 5 以上的版本的 volatile 才会真正有效



![image-20260201155812432](assets/image-20260201155812432.png)







##### 4.double-checked locking 解决

加入`volatile`关键字

```java
public final class Singleton {
    private Singleton() { }
    private static volatile Singleton INSTANCE = null;
    public static Singleton getInstance() {
        // 实例没创建，才会进入内部的 synchronized代码块
        if (INSTANCE == null) {
            synchronized (Singleton.class) { 
                // t2
                // 也许有其它线程已经创建实例，所以再判断一次
                if (INSTANCE == null) { // t1
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}

```



```
// -------------------------------------> 加入对 INSTANCE 变量的读屏障
0: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;

3: ifnonnull 37
6: ldc #3 // class cn/itcast/n5/Singleton
8: dup
9: astore_0
10: monitorenter -----------------------> 保证原子性、可见性
11: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 27
17: new #3 // class cn/itcast/n5/Singleton
20: dup
21: invokespecial #4 // Method "<init>":()V
24: putstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
// -------------------------------------> 加入对 INSTANCE 变量的写屏障
27: aload_0
28: monitorexit ------------------------> 保证原子性、可见性
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn
```

如上面的注释内容所示，读写 volatile 变量时会加入内存屏障（Memory Barrier（Memory Fence）），保证下面 两点： 

* 可见性 
  * 写屏障（sfence）保证在该屏障之前的 t1 对共享变量的改动，都同步到主存当中 
  * 而读屏障（lfence）保证在该屏障之后 t2 对共享变量的读取，加载的是主存中最新数据 
* 有序性 
  * 写屏障会确保指令重排序时，不会将写屏障之前的代码排在写屏障之后 
  * 读屏障会确保指令重排序时，不会将读屏障之后的代码排在读屏障之前 
* 更底层是读写变量时使用 lock 指令来多核 CPU 之间的可见性与有序性

![image-20260201151908548](assets/image-20260201151908548.png)

保证了getStatic 获取引用的时候获取到的一定是构造完成的对象













#### 对Volatile 的理解

**Volatile** 是 Java 虚拟机提供 **轻量级的同步机制**

**1、保证可见性 2、不保证原子性 3、禁止指令重排**





**如何实现可见性**

volatile变量修饰的共享变量在进行写操作的时候回多出一行汇编：

0x01a3de1d:movb $0×0，0×1104800（%esi）;0x01a3de24**:lock** addl $0×0,(%esp);

Lock前缀的指令在多核处理器下会引发两件事情。

 1）将当前处理器缓存行的数据写回到系统内存。

 2）这个写回内存的操作会使其他cpu里缓存了该内存地址的数据无效。

**多处理器总线嗅探：**

为了提高处理速度，处理器不直接和内存进行通信，而是先将系统内存的数据读到内部缓存后再进行操作，但操作不知道何时会写到内存。如果对声明了**volatile**的变量进行写操作，JVM就会向处理器发送一条lock前缀的指令，将这个变量所在缓存行的数据写回到系统内存。但是在**多处理器下**，为了保证各个处理器的缓存是一致的，就会实现缓存缓存一致性协议，**每个处理器通过嗅探在总线上传播的数据来检查自己的缓存值是不是过期了，如果处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置无效状态**，当处理器对这个数据进行修改操作的时候，会重新从系统内存中把数据库读到处理器缓存中。



### 有序性

JVM 会在不影响正确性的前提下，可以调整语句的执行顺序(指令重排)，思考下面一段代码

```java
static int i;
static int j;
// 在某个线程内执行如下赋值操作
i = ...;
j = ...; 

```

可以看到，至于是先执行 i 还是 先执行 j ，对最终的结果不会产生影响。所以，上面代码真正执行时，既可以是

```java
i = ...;
j = ...;
```

也可以是

```java
j = ...;
i = ...; 
```

这种特性称之为『指令重排』



**多线程下『指令重排』会影响正确性：**

```java
int num = 0;
boolean ready = false;
// 线程1 执行此方法
public void actor1(I_Result r) {
    if(ready) {
        r.r1 = num + num;
    } else {
        r.r1 = 1;
    }
}

// 线程2 执行此方法
public void actor2(I_Result r) {
    num = 2;
    ready = true;
}
```

I_Result 是一个对象，有一个属性 r1 用来保存结果，问，可能的结果有几种？ 

有同学这么分析 

情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1 

情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1 

情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了） 

但我告诉你，结果还有可能是 0 😁😁😁，信不信吧！ 

这种情况下是：线程2 执行 ready = true，切换到线程1，进入 if 分支，相加为 0，再切回线程2 执行 num = 2 

相信很多人已经晕了 😵😵😵 

这种现象叫做指令重排，是 JIT 编译器在运行时的一些优化，这个现象需要通过大量测试才能复现： 

> 借助 java 并发压测工具 jcstress https://wiki.openjdk.java.net/display/CodeTools/jcstress
>
> ```
> mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jcstress -
> DarchetypeArtifactId=jcstress-java-test-archetype -DarchetypeVersion=0.5 -DgroupId=cn.itcast -
> DartifactId=ordering -Dversion=1.0 
> ```
>
> 创建 maven 项目，提供如下测试类
>
> ```java
> @JCStressTest
> @Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "ok")
> @Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
> @State
> public class ConcurrencyTest {
>     int num = 0;
>     boolean ready = false;
>     @Actor
>     public void actor1(I_Result r) {
>         if(ready) {
>             r.r1 = num + num;
>         } else {
>             r.r1 = 1;
>         }
>     }
>     @Actor
>     public void actor2(I_Result r) {
>         num = 2;
>         ready = true;
>     }
> }
> 
> ```
>
> 执行
>
> ```
> mvn clean install
> java -jar target/jcstress.jar 
> ```
>
> 会输出我们感兴趣的结果，摘录其中一次结果：
>
> ```
> *** INTERESTING tests
>  Some interesting behaviors observed. This is for the plain curiosity.
>  2 matching test results.
>  [OK] test.ConcurrencyTest
>  (JVM args: [-XX:-TieredCompilation])
>  Observed state Occurrences Expectation Interpretation
>  0 1,729 ACCEPTABLE_INTERESTING !!!!
>  1 42,617,915 ACCEPTABLE ok
>  4 5,146,627 ACCEPTABLE ok
>  [OK] test.ConcurrencyTest
>  (JVM args: [])
>  Observed state Occurrences Expectation Interpretation
>  0 1,652 ACCEPTABLE_INTERESTING !!!!
>  1 46,460,657 ACCEPTABLE ok
>  4 4,571,072 ACCEPTABLE ok 
> ```
>
> 可以看到，出现结果为 0 的情况有 638 次，虽然次数相对很少，但毕竟是出现了

**解决方法**

volatile 修饰的变量，可以禁用指令重排

```java
@JCStressTest
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "ok")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
@State
public class ConcurrencyTest {
    int num = 0;
    volatile boolean ready = false;// 通过写屏障，防止之前的指令重排序
    @Actor
    public void actor1(I_Result r) {
        if(ready) {
            r.r1 = num + num;
        } else {
            r.r1 = 1;
        }
    }
    @Actor
    public void actor2(I_Result r) {
        num = 2;
        ready = true;
    }
}
```

结果为：

```
*** INTERESTING tests
 Some interesting behaviors observed. This is for the plain curiosity.
 0 matching test results.
```



















#### CPU指令级并行原理

> 为什么要有重排指令这项优化呢？从 CPU 执行指令的原理来理解一下

**1.概念**

**Clock Cycle Time** 

主频的概念大家接触的比较多，而 CPU 的 Clock Cycle Time（时钟周期时间），等于主频的倒数，意思是 CPU 能 够识别的最小时间单位，比如说 4G 主频的 CPU 的 Clock Cycle Time 就是 0.25 ns，作为对比，我们墙上挂钟的 Cycle Time 是 1s 

例如，运行一条加法指令一般需要一个时钟周期时间

**CPI**

有的指令需要更多的时钟周期时间，所以引出了 CPI （Cycles Per Instruction）指令平均时钟周期数

**IPC**

IPC（Instruction Per Clock Cycle） 即 CPI 的倒数，表示每个时钟周期能够运行的指令数

**CPU 执行时间**

程序的 CPU 执行时间，即我们前面提到的 user + system 时间，可以用下面的公式来表示

```
程序 CPU 执行时间 = 指令数 * CPI * Clock Cycle Time 
```



> #### 鱼罐头的故事
>
> 加工一条鱼需要 50 分钟，只能一条鱼、一条鱼顺序加工...
>
> ![image-20260201140945194](assets/image-20260201140945194.png)
>
> 可以将每个鱼罐头的加工流程细分为 5 个步骤： 
>
> * 去鳞清洗 10分钟 
> * 蒸煮沥水 10分钟 
> * 加注汤料 10分钟 
> * 杀菌出锅 10分钟 
> * 真空封罐 10分钟![image-20260201141036033](assets/image-20260201141036033.png)即使只有一个工人，最理想的情况是：他能够在 10 分钟内同时做好这 5 件事，因为对第一条鱼的真空装罐，不会 影响对第二条鱼的杀菌出锅...



**2.利用指令重排序优化**

事实上，现代处理器会设计为一个时钟周期完成一条执行时间最长的 CPU 指令。为什么这么做呢？可以想到指令 还可以再划分成一个个更小的阶段，例如，每条指令都可以分为： `取指令 - 指令译码 - 执行指令 - 内存访问 - 数据` 写回 这 5 个阶段

![image-20260201141743322](assets/image-20260201141743322.png)



> 术语参考： 
>
> * instruction fetch (IF) 
> * instruction decode (ID) 
> * execute (EX) 
> * memory access (MEM) 
> * register write back (WB)

在不改变程序结果的前提下，这些指令的<u>各个阶段可以通过**重排序**和**组合**</u>来实现指令级并行，这一技术在 80's 中 叶到 90's 中叶占据了计算架构的重要地位

> **提示：**
>
> 分阶段，分工是提升效率的关键！



指令重排的前提是，重排指令不能影响结果，例如

```java
// 可以重排的例子
int a = 10; // 指令1
int b = 20; // 指令2
System.out.println( a + b );

// 不能重排的例子
int a = 10; // 指令1
int b = a - 5; // 指令2
```

> **参考：** Scoreboarding and the Tomasulo algorithm (which is similar to scoreboarding but makes use of register renaming) are two of the most common techniques for implementing out-of-order execution and instruction-level parallelism.



**3.支持流水线的处理器**

现代 CPU 支持**多级指令流水线**，例如支持同时执行 `取指令 - 指令译码 - 执行指令 - 内存访问 - 数据写回` 的处理 器，就可以称之为**五级指令流水线**。这时 CPU 可以在一个时钟周期内，**同时运行五条指令的不同阶段**（相当于一条执行时间最长的复杂指令），IPC = 1，本质上，流水线技术并不能缩短单条指令的执行时间，但它变相地提高了 指令地吞吐率

> 提示：
>
>  奔腾四（Pentium 4）支持高达 35 级流水线，但由于功耗太高被废弃

![image-20260201142133162](assets/image-20260201142133162.png)







**4.SuperScalar 处理器**

大多数处理器包含多个执行单元，并不是所有计算功能都集中在一起，可以再细分为整数运算单元、浮点数运算单 元等，这样可以把多条指令也可以做到并行获取、译码等，CPU 可以在一个时钟周期内，执行多于一条指令，IPC > 1

![image-20260201142200335](assets/image-20260201142200335.png)

![image-20260201142219851](assets/image-20260201142219851.png)



### happens-before

happens-before **规定了对共享变量的写操作对其它线程的读操作可见**，它**是可见性与有序性的一套规则总结**，抛开以下 happens-before 规则，JMM 并不能保证一个线程对共享变量的写，对于其它线程对该共享变量的读可见 

1. 线程解锁 m 之前对变量的写，对于接下来对 m 加锁的其它线程对该变量的读可见

```java
static int x;
static Object m = new Object();
new Thread(()->{
    synchronized(m) {
        x = 10;
    }
},"t1").start();

new Thread(()->{
    synchronized(m) {
        System.out.println(x);
    }
},"t2").start();

```



2. 线程对 volatile 变量的写，对接下来其它线程对该变量的读可见

```java
volatile static int x;
new Thread(()->{
    x = 10;
},"t1").start();
new Thread(()->{
    System.out.println(x);
},"t2").start();
```



3. 线程 start 前对变量的写，对该线程开始后对该变量的读可见

```java
static int x;
x = 10;
new Thread(()->{
    System.out.println(x);
},"t2").start();

```

4. 线程结束前对变量的写，对其它线程得知它结束后的读可见（比如其它线程调用 t1.isAlive() 或 t1.join()等待 它结束）

```java
static int x;
Thread t1 = new Thread(()->{
    x = 10;
},"t1");
t1.start();
t1.join();
System.out.println(x);
```

5. 线程 t1 打断 t2（interrupt）前对变量的写，对于其他线程得知 t2 被打断后对变量的读可见（通过 t2.interrupted 或 t2.isInterrupted）

```java
static int x;
public static void main(String[] args) {
    Thread t2 = new Thread(()->{
        while(true) {
            if(Thread.currentThread().isInterrupted()) {
                System.out.println(x);
                break;
            }
        }
    },"t2");
    t2.start();
    new Thread(()->{
        sleep(1);    
        x = 10;
        t2.interrupt();
    },"t1").start();
    while(!t2.isInterrupted()) {
        Thread.yield();
    }
    System.out.println(x);
}
```



6. 对变量默认值（0，false，null）的写，对其它线程对该变量的读可见 

7.  具有传递性，如果 x hb-> y 并且 y hb-> z 那么有 x hb-> z ，配合 volatile 的防指令重排，有下面的例子

```java
volatile static int x;
static int y;
new Thread(()->{
    y = 10;
    x = 20;
},"t1").start();
new Thread(()->{
    // x=20 对 t2 可见, 同时 y=10 也对 t2 可见
    System.out.println(x);
},"t2").start();
```

> 变量都是指成员变量或静态成员变量 
>
> 参考： 第17页







# 习题





#### 线程安全单例习题







# ==共享模型-无锁==

> 乐观锁

jdk提供的无锁并发的原子实现

* CAS 与 volatile
* 原子整数
* 原子引用
* 原子累加器
* Unsafe



































# ==并发-非共享模型==











# 3.Lock

### 1）传统的 synchronized

给方法加上synchronized属性

```java
package com.marchsoft.juctest;

import lombok.Synchronized;

/**
 * Description：synchronized
 *
 * @author jiaoqianjin
 * Date: 2020/8/10 21:36
 **/

public class Demo01 {
    public static void main(String[] args) {
        final Ticket ticket = new Ticket();
        //lambda表达式  (参数)->{代码}
        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"C").start();
    }
}
// 资源类 OOP 属性、方法
class Ticket {
    private int number = 30;

    //卖票的方式
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票剩余" + number + "张票");
        }
    }
}
```

### 2）Lock

java.util.concurrent.locks

![image-20200810221525974](https://img-service.csdnimg.cn/img_convert/77ca36442c3ced0659a5af7f06909bd2.png)

![image-20200810221731649](https://img-service.csdnimg.cn/img_convert/d0070945de646cdc612f11c99dd5fb7d.png)

> 传入一个true就能设置成公平锁

**公平锁：** 十分公平，必须先来后到~；

**非公平锁：** 十分不公平，可以插队；**(默认为非公平锁)**



```java
package com.marchsoft.juctest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description：
 *
 * @author 
 * Date: 2020/8/10 22:05
 **/

public class LockDemo {
    public static void main(String[] args) {
        final Ticket2 ticket = new Ticket2();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "C").start();
    }
}
//lock三部曲
//1、    Lock lock=new ReentrantLock();
//2、    lock.lock() 加锁
//3、    finally=> 解锁：lock.unlock();
class Ticket2 {
    private int number = 30;
	
    // 【创建锁】
    Lock lock = new ReentrantLock();
    //卖票的方式
    public synchronized void sale() {//
        lock.lock(); // 开启锁
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票剩余" + number + "张票");
            }
        }finally {
            lock.unlock(); // 关闭锁
        }

    }
}
```

### 3）==Synchronized 与Lock 的区别==

- Synchronized **内置的Java关键字**，Lock是一个**Java类**
- Synchronized 无法判断获取**锁的状态**，Lock可以判断是否获取到了锁
- Synchronized 会**自动释放锁**，lock必须要手动加锁和手动释放锁 可能会遇到死锁
- Synchronized 线程1(获得锁->阻塞)、线程2(等待)；lock就不一定会一直等待下去，l<u>ock会有一个**trylock**去尝试获取锁</u>，不会造成长久的等待。
- Synchronized **是可重入锁**，不可以中断的，**非公平的**；Lock，可重入的，可以判断锁，可以自己设置公平锁和非公平锁；
- Synchronized 适合锁少量的代码同步问题，Lock适合锁大量的同步代码；











# 6. 集合的线程不安全问题

### 1）List 不安全

代码演示：

```java
//java.util.ConcurrentModificationException 并发修改异常 
public class ListTest {
    public static void main(String[] args) {

        List<Object> arrayList = new ArrayList<>();

        for(int i=1;i<=10;i++){
            new Thread(()->{
                arrayList.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(arrayList);
            },String.valueOf(i)).start();
        }

    }
}
```

会导致java.util.ConcurrentModificationException 并发修改异常

**ArrayList 在并发情况下是不安全的**

解决方案：

```java
public class ListTest {
    public static void main(String[] args) {
        /**
         * 解决方案
         * 1. List<String> list = new Vector<>(); //使用Vector
         * 2. List<String> list = Collections.synchronizedList(new ArrayList<>()); //工具类 转换成线程安全的集合
         * 3. List<String> list = new CopyOnWriteArrayList<>(); //使用juc包下的CopyOnWriteArrayList类
         */
        List<String> list = new CopyOnWriteArrayList<>();
        

        for (int i = 1; i <=10; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```

==CopyOnWrite——写入时复制==

**COW 计算机程序设计领域的一种优化策略**

核心思想是，如果有多个调用者（Callers）同时要求相同的资源（如内存或者是磁盘上的数据存储），他们会共同获取相同的指针指向相同的资源，直到某个调用者视图修改资源内容时，系统才会真正复制一份专用副本（private copy）给该调用者，而其他调用者所见到的最初的资源仍然保持不变。这过程对其他的调用者都是透明的（transparently）。此做法主要的优点是如果调用者没有修改资源，就不会有副本（private copy）被创建，因此多个调用者只是读取操作时可以共享同一份资源。

读的时候不需要加锁，如果读的时候有多个线程正在向CopyOnWriteArrayList添加数据，读还是会读到旧的数据，因为写的时候不会锁住旧的CopyOnWriteArrayList。



**多个线程调用，读取的时候读取固定的数据，写入时复制一份数据（存在覆盖操作）；**

> 作用：能在写入的时候避免多个线程操作的结果相互覆盖；



**CopyOnWriteArrayList**比**Vector**厉害在哪里？

**Vector**底层是使用**synchronized**关键字来实现的：效率特别低下。

![image-20200811144549151](https://img-service.csdnimg.cn/img_convert/ec0193ec03b4e67874350644b0f7b6ec.png)

**CopyOnWriteArrayList**使用的是ReentrantLock锁，效率会更加高效 

![image-20200811144447781](https://img-service.csdnimg.cn/img_convert/65d81d2d9f14913e79f91dcf23f8eea7.png)





### 2）set 不安全

**Set和List同理可得:** 多线程情况下，普通的Set集合是线程不安全的；

解决方案还是两种：

- 使用Collections工具类的**synchronized**包装的Set类
- 使用CopyOnWriteArraySet 写入复制的**JUC**解决方案

```java
public class SetTest {
    public static void main(String[] args) {
        /**
         * 1. Set<String> set = Collections.synchronizedSet(new HashSet<>());
         * 2. Set<String> set = new CopyOnWriteArraySet<>();
         */
//        Set<String> set = new HashSet<>();
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }
}
```



> **HashSet底层是什么？**
>
> hashSet底层就是一个**HashMap**；
>
> ```java
> 源码
> 
> // Dummy value to associate with an Object in the backing Map
> private static final Object PRESENT = new Object();
> 
> public HashSet() {
>     map = new HashMap<>();
> }
> 
> public boolean add(E e) {
>     return map.put(e, PRESENT)==null;// 这里的PRESENT就是一个常量
> }
> 
> 
> ```
>
> 



### 3）Map不安全

```java、

Map<String, String> map = new HashMap<>();
//加载因子、初始化容量
```

1. map 是这样用的吗？  不是，工作中不使用这个（太绝对）
2. 默认等价什么？ new HashMap<>(16,0.75);

默认**加载因子是0.75**,默认的**初始容量是16**

![image-20200811150700927](https://img-service.csdnimg.cn/img_convert/a35fac9739ec6cb78381f0a82c1a8927.png)

```
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```







同样的HashMap基础类也存在**并发修改异常**

```java
public class MapTest {
    public static void main(String[] args) {
        
        /**
         * 解决方案
         * 1. Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
         *  Map<String, String> map = new ConcurrentHashMap<>();
         */
        Map<String, String> map = new ConcurrentHashMap<>();
        //加载因子、初始化容量
        for (int i = 1; i < 100; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
```

**TODO:研究ConcurrentHashMap底层原理：**







7. Callable创建线程

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210131173127786.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

**1、可以有返回值；
2、可以抛出异常；
3、方法不同，run()/call()**



![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130203506125.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130203549739.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

FutureTask是Runnable的实现类，其构造参数中能传入collable





![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130203553754.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

```java
package com.zzy.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 1、探究原理
 * 2、觉自己会用
 */
public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //new Thread(new Runnable()).start();
        //new Thread(new FutureTask<V>()).start();
        //new Thread(new FutureTask<V>( Callable )).start();

        //new Thread().start();// 怎么启动Callable

        MyThread thread = new MyThread();
        FutureTask futureTask = new FutureTask(thread);//适配类

        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start();// 结果会被缓存，效率高，结果只打印一次

        //获取Callable的返回结果
        String o = (String) futureTask.get();//这个get 方法可能会产生阻塞 把他放到最后 或者使用异步通信来处理 

        System.out.println(o);
    }
}
class MyThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("jjjj");
        // 耗时的操作
        return "hello";
    }
}
```

细节：
1、有缓存
2、结果可能需要等待，会阻塞 



### 常见的线程安全类

* String 
* Integer 
* StringBuffer 
* Random 
* Vector 
* Hashtable 
* java.util.concurrent 包下的类 

这里说它们是线程安全的是指，多个线程调用它们同一个实例的某个方法时，是线程安全的



```java
Hashtable table = new Hashtable();
new Thread(()->{
 table.put("key", "value1"); // 内部有synchronized关键字
}).start();
new Thread(()->{
 table.put("key", "value2");
}).start();

// 结果是value1
```

* 它们的每个方法是原子的

* 但注意它们多个方法的组合不是原子的：

  ```java
  private Hashtable table;
  // 线程1，线程2先后调用这个方法 
  void method(table){
      if( table.get("key") == null) {
          table.put("key", value);
      }
  }
  // 这个方法虽然是两个线程安全的方法的组合，但是也不能保证操作原子性 保证线程安全
  // 如果要保证这个方法的原子性，需要在外层(方法上)加 synchronized 关键字
  ```

  ![image-20260129150142520](assets/image-20260129150142520.png)

  线程1先进入，但是线程2先执行了put，出现线程安全问题





#### 不可变类的线程安全性



String、Integer 等都是不可变类，因为其内部的状态(属性)不可以改变，因此它们的方法都是线程安全的 

有同学或许有疑问，String 有 replace，substring 等方法【可以】改变值啊，那么这些方法又是如何保证线程安全的呢——会重新创建新的字符串对象(而不是改变原有的值)

 













# 8、常用的辅助类(高并发必会)

加法计数器，减法计数器和信号量

### 1）CountDownLatch

减法计数器

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130203635895.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

**主要方法：**

- countDown 减一操作；
- await 等待计数器归零

作用：等待计数器归零，再唤醒、继续向下运行

> ==和join的作用类似，能实现线程执行顺序的定义==

```java
package com.zzy.add;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Zhao
 * @DATE 2021/1/31 - 17:54
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) throws InterruptedException{
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for(int i = 1;i<=6;i++){
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "Go out");
                countDownLatch.countDown();//数量减一
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//等待计数器归零，然后再向下执行
        
        System.out.println("Close Door");

    }
}
```

如果没有`countDownLatch.await();`这行代码，会输出

```
Close Door
1 Go out
2 Go out
3 Go out
4 Go out
5 Go out
6 Go out
```





### 2）CyclickBarrier

加法计数器

> ![image-20200811202603352](https://img-service.csdnimg.cn/img_convert/dee6ef3d75096d41547b6729fcce3037.png)



加法计数器

```java
package com.zzy.add;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Zhao
 * @DATE 2021/1/31 - 17:54
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        /**
         * 集齐7颗龙珠召唤神龙
         */
        // 召唤龙珠的线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙。。。");
        });

        for (int i = 1; i <= 7; i++) {
            // lambda不能操作到 i
            int temp = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"收集第"+temp+"颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}
```

CyclicBarrier 与 CountDownLatch 区别

- CountDownLatch 是一次性的，CyclicBarrier 是可循环利用的
- CountDownLatch 参与的线程的职责是不一样的，有的在倒计时，有的在等待倒计时结束。CyclicBarrier 参与的线程职责是一样的。

### 3）Semaphore（**信号量**）

> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130203800381.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

抢车位问题：

6车—3个停车位置

```java
package com.zzy.add;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhao
 * @DATE 2021/1/31 - 18:05
 */
public class SemaphoreDemo {
    public static void main(String[] args) {

        // 线程数量：停车位! 用来限流 
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                //acquire() 得到
                //release() 释放
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到车位 ");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName()+"离开车位 ");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }

            }, String.valueOf(i)).start();

        }
    }
}
```





**原理：**

- `semaphore.acquire();` **获得，假设如果已经满了，等待资源盈余，等到有资源被释放为止**
- `semaphore.release();` **释放，会将当前的信号量释放 + 1，然后唤醒等待的线程 **

作用：

- **并发限流，控制最大的线程数 **



> CycliBarrier：指定个数的线程执行完毕再执行操作
>
> Semaphore：同一时间只能有指定数量的线程得到资源 允许执行（弹幕：底层用的AQS）









# 9. 读写锁



作用：允许多个线程读(提高效率)；只让一个线程写

```java
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        int num = 6;
        for (int i = 1; i <= num; i++) {
            int finalI = i;
            new Thread(() -> {

                myCache.write(String.valueOf(finalI), String.valueOf(finalI));

            },String.valueOf(i)).start();
        }

        for (int i = 1; i <= num; i++) {
            int finalI = i;
            new Thread(() -> {

                myCache.read(String.valueOf(finalI));

            },String.valueOf(i)).start();
        }
    }
}

/**
 *  自定义缓存；方法未加锁，导致写的时候被插队
 */
class MyCache {
    private volatile Map<String, String> map = new HashMap<>();

    public void write(String key, String value) {
        System.out.println(Thread.currentThread().getName() + "线程开始写入");
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "线程写入ok");
    }

    public void read(String key) {
        System.out.println(Thread.currentThread().getName() + "线程开始读取");
        map.get(key);
        System.out.println(Thread.currentThread().getName() + "线程写读取ok");
    }
}
2线程开始写入
2线程写入ok
3线程开始写入
3线程写入ok
1线程开始写入    # 没加锁的情况：1线程写入的时候插入了其他线程，导致数据不一致
4线程开始写入
4线程写入ok
1线程写入ok
6线程开始写入
6线程写入ok
5线程开始写入
5线程写入ok
1线程开始读取
1线程写读取ok
2线程开始读取
2线程写读取ok
3线程开始读取
3线程写读取ok
4线程开始读取
4线程写读取ok
5线程开始读取
6线程开始读取
6线程写读取ok
5线程写读取ok

Process finished with exit code 0
```

不加锁的情况，多线程的读写会造成数据不可靠的问题。



我可以采用**synchronized**这种重量锁和轻量锁 **lock**去保证数据的可靠。

也可以采用更细粒度的锁：**ReadWriteLock** 读写锁来保证（颗粒度更细，效率更高）

* 写锁(独占锁)
* 读锁(共享锁)



> ![image-20200811213503631](https://img-service.csdnimg.cn/img_convert/8ee70d5ab31bef2c8afd95cdc32a381e.png)

```java
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache2 myCache = new MyCache2();
        int num = 6;
        for (int i = 1; i <= num; i++) {
            int finalI = i;
            new Thread(() -> {

                myCache.write(String.valueOf(finalI), String.valueOf(finalI));

            },String.valueOf(i)).start();
        }

        for (int i = 1; i <= num; i++) {
            int finalI = i;
            new Thread(() -> {

                myCache.read(String.valueOf(finalI));

            },String.valueOf(i)).start();
        }
    }

}
class MyCache2 {
    private volatile Map<String, String> map = new HashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void write(String key, String value) {
        lock.writeLock().lock(); // 写锁
        try {
            System.out.println(Thread.currentThread().getName() + "线程开始写入");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "线程写入ok");

        }finally {
            lock.writeLock().unlock(); // 释放写锁
        }
    }

    public void read(String key) {
        lock.readLock().lock(); // 读锁
        try {
            System.out.println(Thread.currentThread().getName() + "线程开始读取");
            map.get(key);
            System.out.println(Thread.currentThread().getName() + "线程写读取ok");
        }finally {
            lock.readLock().unlock(); // 释放读锁
        }
    }
}
1线程开始写入
1线程写入ok
6线程开始写入
6线程写入ok
3线程开始写入
3线程写入ok
2线程开始写入
2线程写入ok
5线程开始写入
5线程写入ok
4线程开始写入
4线程写入ok
    
1线程开始读取
5线程开始读取
2线程开始读取
1线程写读取ok
3线程开始读取
2线程写读取ok
6线程开始读取
6线程写读取ok
5线程写读取ok
4线程开始读取
4线程写读取ok
3线程写读取ok

Process finished with exit code 0
```

# 10. 阻塞队列

![image-20200812092316296](https://img-service.csdnimg.cn/img_convert/3b6b0b33e6e9b0f2261a89b6e42e78ea.png)

发生阻塞的情况：

1. 如果队列满了，写入操作就会被阻塞
2. 如果队列是空的，读取操作会被阻塞

> 
>
> ![image-20200812093115670](https://img-service.csdnimg.cn/img_convert/d651ccc40069352ee6c8b86ae2cee8eb.png)



使用场景

* 多线程
* 线程池



### 1）BlockQueue

是Collection的一个子类

什么情况下我们会使用阻塞队列

> 多线程并发处理、线程池

![image-20200812093254651](https://img-service.csdnimg.cn/img_convert/cae49c50458adc0997d57b2044666ccb.png)

BlockingQueue 有四组api

| 操作逻辑        | 抛出异常 | 不会抛出异常，有返回值 | 阻塞，等待 | 超时等待                |
| --------------- | -------- | ---------------------- | ---------- | ----------------------- |
| 添加 - 相关方法 | add      | offer                  | put        | offer(timenum.timeUnit) |
| 移出 - 相关方法 | remove   | poll                   | take       | poll(timenum,timeUnit)  |
| 判断队首元素    | element  | peek                   | -          | -                       |

演示代码





- **抛出异常**

```JAVA
    public static void test1() {
        // 队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
		System.out.println(blockingQueue.element());//查看队首元素
        System.out.println("---------------------");

        //IllegalStateException: Queue full
        System.out.println(blockingQueue.add("d"));//继续添加，超出容量；就会报异常

        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

        //java.util.NoSuchElementException 抛出异常 
        System.out.println(blockingQueue.remove());//继续移除，就会报异常
     }
```

- **有返回值，不抛出异常**

```java
/**
* 有返回值，没有异常
*/
    public static void test2() {
        // 队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));

        System.out.println("---------------------");
        System.out.println(blockingQueue.peek());//查看队首元素

		System.out.println(blockingQueue.offer("d"));// 返回false 不抛出异常 

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());

        System.out.println(blockingQueue.poll()); // null 不抛出异常 
    }
```

- **等待，阻塞（一直阻塞）（用的较少）**

```java
	/**
     * 等待，阻塞（一直阻塞）
     */
    public static void test3() throws InterruptedException {
        // 队列的大小
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        // 一直阻塞
        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");

        // blockingQueue.put("d"); // 队列没有位置了，会被阻塞，【一直等待】
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take()); // 没有这个元素，一直阻塞
    }
```

- **等待，阻塞（等待超时）（用的较多）**

```java
/**
     * 等待 超时阻塞
     *  这种情况也会等待队列有位置 或者有产品 但是会超时结束
     */
    public static void test4() throws InterruptedException {
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.offer("a");
        blockingQueue.offer("b");
        blockingQueue.offer("c");
        System.out.println("开始等待");
        blockingQueue.offer("d",2, TimeUnit.SECONDS);  // 超时时间设置为2s 等待如果超过2s就结束等待
        System.out.println("结束等待");
        System.out.println("===========取值==================");
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println("开始等待");
        blockingQueue.poll(2,TimeUnit.SECONDS); //超过两秒 就不再等待
        System.out.println("结束等待");
    }
```



### 2）同步队列

- <u>同步队列 没有容量，也可以视为**容量为1的队列**；</u>
- <u>进去一个元素，必须等待取出来之后，才能再往里面放入一个元素；</u>
- **put**方法 和 **take**方法；
- Synchronized 和 其他的BlockingQueue 不一样 它不存储元素；
- put了一个元素，就必须从里面先take出来，否则不能再put进去值 
- 并且SynchronousQueue 的take是使用了lock锁保证线程安全的

```java
package com.marchsoft.queue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/12 10:02
 **/

public class SynchronousQueue {
    public static void main(String[] args) {
        BlockingQueue<String> synchronousQueue = new java.util.concurrent.SynchronousQueue<>();// SynchronousQueue和BlockingQueue是父子关系
        // 网queue中添加元素
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "put 01");
                synchronousQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "put 02");
                synchronousQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "put 03");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 取出元素
        new Thread(()-> {
            try {
                System.out.println(Thread.currentThread().getName() + "take" + synchronousQueue.take());
                System.out.println(Thread.currentThread().getName() + "take" + synchronousQueue.take());
                System.out.println(Thread.currentThread().getName() + "take" + synchronousQueue.take());
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
Thread-0put 01
Thread-1take1
Thread-0put 02
Thread-1take2
Thread-0put 03
Thread-1take3

Process finished with exit code 0
```

 







# 11、线程池(重点)

**必会： 线程池：三大方法、7大参数、4种拒绝策略**

线程池：三大方式、七大参数、四种拒绝策略

> 池化技术：事先准备好资源
>
> 线程池、JDBC的连接池、内存池、对象池 等等。



**池化技术**：事先准备好一些资源，如果有人要用，就来我这里拿，用完之后还给我，以此来提高效率。

### 1）线程池的好处：

1、降低资源的消耗；

> 资源的创建、销毁十分消耗资源
>
> 

2、提高响应的速度；

3、方便管理；





**线程复用、可以控制最大并发数、管理线程；**

### 2）线程池：三大方法

- ExecutorService threadPool = Executors.newSingleThreadExecutor();//单个线程

- ExecutorService threadPool2 = Executors.newFixedThreadPool(5); //创建一个固定的线程池的大小

- ExecutorService threadPool3 = Executors.newCachedThreadPool(); //可伸缩的

  > 



**线程池使用**

```java
package com.zzy.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Executors 工具类， 3大方法
public class Demo01 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();//单个线程
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);//创建一个固定大小的线程池
//        ExecutorService threadPool = Executors.newCachedThreadPool();//创建一个可伸缩的线程池【不推荐使用这种方式创建，最大线程数量过大 可能导致OOM】

        try {
            for (int i = 0; i < 10; i++) {
                // 旧方式：new Thread().start();

                // 使用线程池来创建线程
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "  OK");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }
    }
}
```

> #### 运行结果：
>
> **newSingleThreadExecutor（）**
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/2021013120510032.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)
> **newFixedThreadPool(5)**
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210131205022621.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)
>
> **newCachedThreadPool()**
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210131205042255.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)
>





三者本质上都是调用`ThreadPoolExecutor`

> 源码分析
>
> ![image-20251230111800499](assets/image-20251230111902291.png)
>
> ![image-20251230112119042](assets/image-20251230112119042.png)
>
> ![image-20251230112206974](assets/image-20251230112206974.png)
>
> ![image-20251230112607808](assets/image-20251230112607808.png)

```java
public static ExecutorService newSingleThreadExecutor() {
 return new FinalizableDelegatedExecutorService
     (new ThreadPoolExecutor(1, 1,
                             0L, TimeUnit.MILLISECONDS,
                             new LinkedBlockingQueue<Runnable>()));
}

public static ExecutorService newFixedThreadPool(int nThreads) {
 return new ThreadPoolExecutor(nThreads, nThreads,
                               0L, TimeUnit.MILLISECONDS,
                               new LinkedBlockingQueue<Runnable>());
}

public static ExecutorService newCachedThreadPool() {
 return new ThreadPoolExecutor(0, Integer.MAX_VALUE,// 21亿
                               60L, TimeUnit.SECONDS,
                               new SynchronousQueue<Runnable>());
}
```



> <u>阿里巴巴开发规范</u>：
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204310456.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)
>
> 







### 3）七大参数

> ```java
> 
>    
>    // 本质: 都是调用ThreadPoolExecutor（）
>    
>    //【七大参数】
> public ThreadPoolExecutor(int corePoolSize, // 核心线程池大小
>                        int maximumPoolSize,  // 最大核心线程池大小
>                        long keepAliveTime, // 存活时间（如果存活时间之内没有调用就会被释放）
>                           TimeUnit unit, // 超时单位
>                           BlockingQueue<Runnable> workQueue,// 阻塞队列
>                           ThreadFactory threadFactory,// 线程工厂：创建线程的，一般不用动
>                        RejectedExecutionHandler handler// 拒绝策略
>                        ) {
>  if (corePoolSize < 0 ||
>         maximumPoolSize <= 0 ||
>         maximumPoolSize < corePoolSize ||
>         keepAliveTime < 0)
>      throw new IllegalArgumentException();
>  if (workQueue == null || threadFactory == null || handler == null)
>      throw new NullPointerException();
>  this.acc = System.getSecurityManager() == null ?
>          null :
>          AccessController.getContext();
>     this.corePoolSize = corePoolSize;
>     this.maximumPoolSize = maximumPoolSize;
>     this.workQueue = workQueue;
>     this.keepAliveTime = unit.toNanos(keepAliveTime);
>     this.threadFactory = threadFactory;
>     this.handler = handler;
>    }
>    ```
>    
>    
>    





阻塞队列满了的时候，才会触发最大连接数
![image-20251230192551944](assets/image-20251230192551944.png)





##### 线程池的正确使用方式

```java
package com.zzy.pool;

import java.util.concurrent.*;

//Executors 工具类， 3大方法
// Executors 工具类、3大方法

/**
 * new ThreadPoolExecutor.AbortPolicy() // 银行满了，还有人进来，不处理这个人的，抛出异常
 * new ThreadPoolExecutor.CallerRunsPolicy() // 哪来的去哪里 
 * new ThreadPoolExecutor.DiscardPolicy() //队列满了，丢掉任务，不会抛出异常 
 * new ThreadPoolExecutor.DiscardOldestPolicy() //队列满了，尝试去和最早的竞争，也不会抛出异常 
 */
public class Demo01 {
    public static void main(String[] args) {
        // 自定义线程池 工作 ThreadPoolExecutor
        ExecutorService threadPool = new ThreadPoolExecutor(
            	2,// 核心线程数
                5,// 最大核心线程数
                3, // 存活时间
            	TimeUnit.SECONDS,// 存活时间单位
                new LinkedBlockingDeque<>(3),// 
                Executors.defaultThreadFactory(),//线程工厂（使用默认线程工厂）
                new ThreadPoolExecutor.AbortPolicy()//拒绝策略
            银行人满了，还有人进来，不处理这个人，抛出异常
        );

        try {
            // 最大承载：Deque + max
            // 超过 抛出异常： RejectedExecutionException
            for (int i = 1; i <= 8; i++) {
                // 使用了线程池之后，使用线程池来创建线程
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "  OK");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }
    }
}
```

### 4）拒绝策略

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204506207.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)











1. **AbortPolicy（中止策略）——`new ThreadPoolExecutor.AbortPolicy() `**
   - 默认策略。
   - 直接抛出 `RejectedExecutionException` 异常，通知调用者任务被拒绝。
   - 适用于<u>对任务丢失敏感、希望及时感知异常</u>的场景。
2. **CallerRunsPolicy（调用者运行策略）——`new ThreadPoolExecutor.CallerRunsPolicy() `**
   - 由提交任务的线程（即调用 `execute()` 的线程）直接执行该任务。
   - 不会丢弃任务，也不会抛异常，但可能降低新任务提交的速度（起到“反馈抑制”作用）。
   - 适用于希望任务最终都能被执行，且能容忍主线程阻塞的场景。
3. **DiscardPolicy（丢弃策略）——`new ThreadPoolExecutor.DiscardPolicy() `**
   - <u>静默丢弃</u>被拒绝的任务，<u>不抛异常、不执行</u>。
   - 适用于<u>允许任务丢失、且不希望影响系统稳定性</u>的场景。
4. **DiscardOldestPolicy（丢弃最旧策略）——`new ThreadPoolExecutor.DiscardOldestPolicy() `**
   - <u>丢弃任务队列中最老（即将被执行）的任务</u>，然后尝试重新提交当前任务。
   - 可能导致某些任务永远无法执行（如持续高负载时），但保留了最新任务。
   - 适用于<u>更关注最新任务价值的场景</u>。

> 注意：这些策略只在使用有界队列（如 `ArrayBlockingQueue`）且线程数达到上限时才会触发。若使用无界队列（如 `LinkedBlockingQueue`），一般不会触发拒绝策略（除非内存耗尽）





### 5）如何设置最大线程池

> 设置最大线程数

**1、CPU密集型：电脑的核数是几核就选择几，可以保持CPU效率最高；**
**选择maximunPoolSize的大小**

```java

int maxNum = Runtime.getRuntime().availableProcessors();// 获取cpu 的核数（软编码）
ExecutorService service =new ThreadPoolExecutor(
        2,
        maxNum,
        3,
        TimeUnit.SECONDS,
        new LinkedBlockingDeque<>(3),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy()
);
```

**2、I/O密集型：**

> 比如一个程序中有15个大型任务，其中io十分占用资源；I/O密集型就是判断我们程序中十分耗I/O的线程数量，大约是最大I/O数的一倍到两倍之间。
>

判断你的程序中 耗IO 的线程数num ，设成 num~2*num之间









# 12. 四大函数式接口

传统技术必会：泛型、枚举、反射

新时代的程序员：**lambda表达式、链式编程、函数式接口、Stream流式计算**

**函数式接口(FunctionalInterface)：只有一个方法的接口**

> 学习必要性：函数式接口简化了编程模型，在新版本的框架底层大量应用 

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}

```



java.util.function

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204603793.png)

### 1）Function 函数型接口

> 源码
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204621549.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

* 特性：有一个输入参数，有一个输出

* 只要是函数型接口，就可以用lambda表达式简化（适当简化，否则影响可读性）

  > 不需要定义一个类，重写方法，然后再创建对象

```java
package com.zzy.function;

import java.util.function.Function;

/**
 * Function 函数型接口, 
 * 只要是函数型接口，就可以用 lambda表达式简化
 */
public class Demo01 {
    public static void main(String[] args) {
        //工具类：输出输入的值
        Function function = new Function<String, String>() {// 不需要定义一个类，重写方法，然后再创建对象
            @Override
            public String apply(String str) {
                return str;
            }
        };

        //只要是函数型接口，就可以用 lambda表达式简化
        Function function1 = (str) -> {
            return str;
        };

        System.out.println(function1.apply("123"));
    }
}
```





### 2）Predicate 断定型接口

* 特性： 有一个输入参数，返回值只能是 布尔值



![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204643644.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

```java
package com.zzy.function;

import java.util.function.Predicate;

/**
 * 断定型接口：有一个输入参数，返回值只能是 布尔值 
 */
public class Demo02 {

    public static void main(String[] args) {
        //判断字符串是否为空
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return str.isEmpty();
            }
        };
        //只要是函数型接口，就可以用 lambda表达式简化
        Predicate<String> predicate2 = (str) -> {
            return str.isEmpty();
        };

        System.out.println(predicate.test("asddd"));
    }
}
```





### 3）Suppier 供给型接口

* 特性：没有参数，只有返回值



![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204745367.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

```java
package com.zzy.function;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Supplier 供给型接口 没有参数，只有返回值
 */
public class Demo04 {
    public static void main(String[] args) {
        Supplier supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 1024;
            }
        };

        //只要是函数型接口，就可以用 lambda表达式简化
        Supplier supplier2 = () -> {
            return 1024;
        };
        
        System.out.println(supplier2.get());
    }
}
```

### 4）Consummer 消费型接口

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204721140.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

```java
package com.zzy.function;

import com.zzy.pc.C;

import java.util.function.Consumer;

/**
 * Consumer 消费型接口: 只有输入，没有返回值
 */
public class Demo03 {
    public static void main(String[] args) {
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String str) {
                System.out.println(str);
            }
        };


        //只要是函数型接口，就可以用 lambda表达式简化
        Consumer<String> consumer1 = (str)->{
            System.out.println(str);
        };
        consumer.accept("qwre");
    }
}
```

简化编程

# 13. Stream流式计算

> 什么是Stream流式计算

> 大数据：存储 + 计算
>
> 集合、MySQL 本质就是**存储**东西的；
>
> 计算都应该交给**流**来操作





见“stream流.md”











# 14. ForkJoin

ForkJoin 在JDK1.7，并行执行任务 提高效率~。在大数据量速率会更快 

大数据中：**Map Reduce 核心思想->把大任务拆分为小任务 **

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204911181.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)





### 1）ForkJoin 特点： 工作窃取 

实现原理是：**双端队列** 从上面和下面都可以去拿到任务进行执行 

![image-20200812163701588](https://img-service.csdnimg.cn/img_convert/7ccffb99e41ec5a89ef2118cf0c4f0f2.png)







### 2）如何使用ForkJoin?

使用场景：大数据量场景

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204941807.png)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130204946159.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

- 通过**ForkJoinPool**来执行

- 调用 **`execute(ForkJoinTask<?> task)`**执行计算任务

  ```
  forkJoinPool.execute(task)
  ```

- 计算类要去继承`RecursiveTask`；

  > 底层集成了`ForkJoinTask`



```java
ForkJoinPool forkJoinPool = new ForkJoinPool();
ForkJoinTask<Long> task = new ForkJoinDemo(0L, SUM);
ForkJoinTask<Long> submit = forkJoinPool.submit(task);// 提交任务，获取结果
Long along = submit.get();
```



使用案例

> 这里只是举个使用案例，当然等差数列求和肯定用公式计算的

```java
package com.marchsoft.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/13 8:33
 **/

public class ForkJoinDemo extends RecursiveTask<Long> {
    private long star;
    private long end;
    /** 临界值 */
    private long temp = 1000000L;

    public ForkJoinDemo(long star, long end) {
        this.star = star;
        this.end = end;
    }

    /**
     * 重写计算方法
     * @return
     */
    @Override
    protected Long compute() {
        if ((end - star) < temp) {//小于临界值普通计算
            Long sum = 0L;
            for (Long i = star; i < end; i++) {
                sum += i;
            }
            return sum;
        }else {
            // 大于等于临界值，使用ForkJoin 分而治之 计算
            //1 . 计算平均值
            long middle = (star + end) / 2;
            ForkJoinDemo forkJoinDemo1 = new ForkJoinDemo(star, middle);
            // 拆分任务，把线程压入线程队列
            forkJoinDemo1.fork();
            ForkJoinDemo forkJoinDemo2 = new ForkJoinDemo(middle, end);
            forkJoinDemo2.fork();

            long taskSum = forkJoinDemo1.join() + forkJoinDemo2.join();
            return taskSum;
        }
    }
}
```

**测试类**

```java
package com.marchsoft.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/13 8:43
 **/

public class ForkJoinTest {
    private static final long SUM = 20_0000_0000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test1();
        test2();
        test3();
    }

    /**
     * 使用普通方法
     */
    public static void test1() {
        long star = System.currentTimeMillis();
        long sum = 0L;
        for (long i = 1; i < SUM ; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        System.out.println(sum);
        System.out.println("时间：" + (end - star));
        System.out.println("----------------------");
    }
    /**
     * 使用ForkJoin 方法
     */
    public static void test2() throws ExecutionException, InterruptedException {
        long star = System.currentTimeMillis();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinDemo(0L, SUM);
        ForkJoinTask<Long> submit = forkJoinPool.submit(task);
        Long along = submit.get();

        System.out.println(along);
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - star));
        System.out.println("-----------");
    }
    /**
     * 使用 Stream 流计算
     */
    public static void test3() {
        long star = System.currentTimeMillis();

        long sum = LongStream.range(0L, 20_0000_0000L).parallel().reduce(0, Long::sum);
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - star));
        System.out.println("-----------");
    }
}
```

![image-20200813090527527](https://img-service.csdnimg.cn/img_convert/12579f3c5a94870c6a9f546390896e72.png)

**.parallel().reduce(0, Long::sum)使用一个并行流去计算整个计算，提高效率。**

![image-20200812164023833](https://img-service.csdnimg.cn/img_convert/44c24ea717d569f1966ec8f14f6ce39f.png)







# 15、异步回调

讲得不清楚，跳过·

> Future 设计的初衷： 对将来的某个事件的结果进行建模



**CompletableFuture：Future的一个实现类，可以实现异步回调**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130205040360.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)



#### （1）没有返回值的runAsync异步回调

```java
package com.zzy.future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用： CompletableFuture
 * // 异步执行
 * // 成功回调
 * // 失败回调
 */
public class Demo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 没有返回值的 runAsync 异步回调
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "runAsync=>void");
        });

        System.out.println("11111111");
        completableFuture.get();//获取执行结果
    }

}
```

#### （2）有返回值的异步回调supplyAsync

```java
package com.zzy.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用： CompletableFuture
 * // 异步执行
 * // 成功回调
 * // 失败回调
 */
public class Demo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // completableFuture.get(); // 获取阻塞执行结果
        // 有返回值的 supplyAsync 异步回调
        // ajax，成功和失败的回调
        // 失败返回的是错误信息；
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "supplyAsync=>Integer");
//            int i = 10 / 0;
            return 1024;
        });

        System.out.println(completableFuture.whenComplete((t, u) -> {
            System.out.println("t=>" + t);// 正常的返回结果
            System.out.println("u=>" + u);// 错误信息：

        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 233;// 可以获取到错误的返回结果
        }).get());
    }
    /**
     * 工作中：
     * succee Code 200
     * error Code 404 500
     */
}
```

**whenComplete**: 有两个参数，一个是t 一个是u

T：是代表的 **正常返回的结果**；

U：是代表的 **抛出异常的错误信息**；

如果发生了异常，get可以获取到**exceptionally**返回的值；











# 16. JMM



# 17. volatile

### 1）保证可见性

如果此处不加volatile，程序就会死循环，因为线程一感知不到number的变化

```java
public class JMMDemo01 {

    // 如果不加volatile 程序会死循环
    // 加了volatile是可以保证可见性的
    private volatile static Integer number = 0;

    public static void main(String[] args) {
        //main线程
        //子线程1
        new Thread(()->{
            while (number==0){
                
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //子线程2
        new Thread(()->{
            while (number==0){
            }

        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        number=1;
        System.out.println(number);
    }
}
```

### 2）不保证原子性

> 原子性：不可分割，
>
> 线程A在执行任务的时候，不能被打扰的，也不能被分割的，要么同时成功，要么同时失败。

下面这个程序运行的结果<=2w，表明 不保证原子性

```java
/*
 * 不保证原子性
 * number <=2w
 * 
 */
public class VDemo02 {

    private static volatile int number = 0;

    public static void add(){
        number++; 
        //++ 不是一个原子性操作，是两个~3个操作
        //
    }

    public static void main(String[] args) {
        //理论上number  === 20000

        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000 ; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2){
            //main  gc
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+",num="+number);
    }
}
```

****

> “++”这个操作分为三步
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130205614525.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)



==如果不加lock和synchronized ，怎么样保证原子性？==

**使用原子类，解决原子性问题**，比锁高效很多倍

【底层基于CAS】

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130205626683.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

```java
public class VDemo02 {

    private static volatile AtomicInteger number = new AtomicInteger();

    public static void add(){
//        number++;
        number.incrementAndGet();  //底层是CAS保证的原子性
    }

    public static void main(String[] args) {
        //理论上number  === 20000

        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000 ; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2){
            //main  gc
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+",num="+number);
    }
}
```

这Unsafe类底层都直接和操作系统挂钩，是在内存中修改值。



> 原子类为什么这么高级？
>
> 



### 3）禁止指令重排

**什么是指令重排？**

计算机并不是完全按照我们自己写的程序顺序去执行的

源代码–>编译器优化重排–>指令并行也可能会重排–>内存系统也会重排–>执行

> **当然，处理器在进行指令重排的时候，会考虑数据之间的依赖性**
>
> ```java
> int x=1; //1
> int y=2; //2
> x=x+5;   //3
> y=x*x;   //4
> 
> //我们期望的执行顺序是 1_2_3_4  可能执行的顺序会变成2134 1324
> //但不可能是4123
> ```
>
> 
>
> 

可能造成的影响结果：

a b x y这四个值 默认都是0

| 线程A   | 线程B   |
| ------- | ------- |
| 执行x=a | 执行y=b |
| 执行b=1 | 执行a=2 |

正常的结果： x = 0; y =0;

> 但可能在线程A中会出现，先执行b=1,然后再执行x=a、在B线程中可能会出现，先执行a=2，然后执行y=b；
>
> | 线程A   | 线程B   |
> | ------- | ------- |
> | 执行b=1 | 执行a=2 |
> | 执行x=a | 执行y=b |
>
> 那么就有可能结果如下：x=2; y=1.
>

---

**volatile可以避免指令重排：**

**volatile中会加一道内存的屏障，这个内存屏障可以保证在这个屏障中的指令顺序。**



内存屏障：CPU指令。作用：

1、保证特定的操作的执行顺序；

2、可以保证某些变量的内存可见性（利用这些特性，就可以保证volatile实现的可见性）

volatile在上下加上两层内存屏障，防止指令重排。

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021013021045412.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

### 4）总结

- **volatile可以保证可见性；**
- **不能保证原子性**
- **由于内存屏障，可以避免指令重排的现象产生**

面试官：那么你知道在哪里用这个内存屏障用得最多呢？**单例模式**

# 18. 玩转单例模式

为什么枚举可以避免单例模式被破坏？

饿汉式 DCL懒汉式，深究 

### 1）饿汉式

```java
package com.zzy.single;
//饿汉式单例
public class Hungry {
    //一上来就实例化，可能会浪费空间
    private byte[] data1 =new byte[1024*1024];
    private byte[] data2 =new byte[1024*1024];
    private byte[] data3 =new byte[1024*1024];
    private byte[] data4 =new byte[1024*1024];

    //私有化构造器
    private Hungry() {

    }
    private final static Hungry HUNGRY = new Hungry();

    public Hungry getInstance() {
        return HUNGRY;
    }
}
```

### 2）DCL懒汉式

```java
package com.zzy.single;

// 懒汉式单例
// 道高一尺，魔高一丈 
public class LazyMan {
    //私有化构造器
    private LazyMan() {
        System.out.println(Thread.currentThread().getName()+"OK");
    }

    private static LazyMan lazyMan;

    public static LazyMan getInstance() {
        if (lazyMan == null) {
            lazyMan = new LazyMan();
        }
        return lazyMan;
    }


    //多线程并，会有隐患 
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                lazyMan.getInstance();
            }).start();
        }

    }
}
```

> 双重检测锁模式的懒汉式单例（DCL懒汉式）

```java
package com.zzy.single;

// 懒汉式单例
// 道高一尺，魔高一丈 
public class LazyMan {
    //私有化构造器
    private LazyMan() {
        System.out.println(Thread.currentThread().getName() + "OK");
    }

    private static LazyMan lazyMan;

    // 双重检测锁模式的 懒汉式单例 DCL懒汉式
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();
                }
            }
        }
        return lazyMan;
    }

    //多线程并发
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lazyMan.getInstance();
            }).start();
        }

    }
}
```

> 加volatile，防止指令重排

```java
package com.zzy.single;

// 懒汉式单例
// 道高一尺，魔高一丈 
public class LazyMan {
    //私有化构造器
    private LazyMan() {
        System.out.println(Thread.currentThread().getName() + "OK");
    }

    private volatile static LazyMan lazyMan;

    // 双重检测锁模式的 懒汉式单例 DCL懒汉式
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();// 不是一个原子性操作
                    /**
                     * 1. 分配内存空间
                     * 2、执行构造方法，初始化对象
                     * 3、把这个对象指向这个空间
                     * 执行顺序123,132都有可能
                     * A：123
                     * B：132
                     * B把这个对象指向这个空间，发现不为空执行return
                     * // 但是此时在线程A中，lazyMan还没有完成构造，lazyMan要加volatile，防止指令重排
                     */
                }
            }
        }
        return lazyMan;
    }


    //多线程并发
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lazyMan.getInstance();
            }).start();
        }
    }
}
```

> 静态内部类

```java
package com.zzy.single;

/**
 * @author Zhao
 * @DATE 2021/2/1 - 23:08
 */
public class Holder {
    private Holder() {
    }

    private static Holder getInstance() {
        return InnerClass.HOLDER;
    }

    public static class InnerClass {
        private static final Holder HOLDER = new Holder();
    }
}
```

> 以上都不安全，可以通过反射破坏 

```java
package com.zzy.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// 懒汉式单例
// 道高一尺，魔高一丈 
public class LazyMan {
    //私有化构造器
    private LazyMan() {
        System.out.println(Thread.currentThread().getName() + "OK");
    }

    private volatile static LazyMan lazyMan;

    // 双重检测锁模式的 懒汉式单例 DCL懒汉式
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();// 不是一个原子性操作
                }
            }
        }
        return lazyMan;
    }

    //多线程并发
    public static void main(String[] args) throws Exception {
        LazyMan instance = LazyMan.getInstance();
        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);//无视私有
        LazyMan instance2 = declaredConstructor.newInstance();

        System.out.println(instance);
        System.out.println(instance2);
       
    }
}
```

> 防止反射破坏异常

```java
package com.zzy.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// 懒汉式单例
// 道高一尺，魔高一丈 
public class LazyMan {
    //私有化构造器
    private LazyMan() {
        synchronized (LazyMan.class) {
            if (lazyMan != null) {
                throw new RuntimeException("不要试图使用反射破坏异常");
            }
        }
        System.out.println(Thread.currentThread().getName() + "OK");
    }

    private volatile static LazyMan lazyMan;

    // 双重检测锁模式的 懒汉式单例 DCL懒汉式
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();// 不是一个原子性操作
                }
            }
        }
        return lazyMan;
    }

    //多线程并发
    public static void main(String[] args) throws Exception {
        LazyMan instance = LazyMan.getInstance();
        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);//无视私有
        LazyMan instance2 = declaredConstructor.newInstance();
        System.out.println(instance);
        System.out.println(instance2);
    }
}
```

> 但是仍然可以通过如下方式破坏：

```java
 //多线程并发
    public static void main(String[] args) throws Exception {
//        LazyMan instance = LazyMan.getInstance();
        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);//无视私有
        LazyMan instance1 = declaredConstructor.newInstance();
        LazyMan instance2 = declaredConstructor.newInstance();

        System.out.println(instance1);
        System.out.println(instance2);
    }
```

> 设置一个别人不知道的变量

```java
package com.zzy.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// 懒汉式单例
public class LazyMan {

    //定义一个别人不知道的变量
    private static boolean hello = false;

    //私有化构造器
    private LazyMan() {
        synchronized (LazyMan.class) {
            if (hello == false) {
                hello = true;
            } else {
                throw new RuntimeException("不要试图使用反射破坏异常");
            }
        }
        System.out.println(Thread.currentThread().getName() + "OK");
    }

    private volatile static LazyMan lazyMan;

    // 双重检测锁模式的 懒汉式单例 DCL懒汉式
    public static LazyMan getInstance() {
        if (lazyMan == null) {
            synchronized (LazyMan.class) {
                if (lazyMan == null) {
                    lazyMan = new LazyMan();// 不是一个原子性操作
                }
            }
        }
        return lazyMan;
    }

    //多线程并发
    public static void main(String[] args) throws Exception {
//        LazyMan instance = LazyMan.getInstance();
        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);//无视私有
        LazyMan instance1 = declaredConstructor.newInstance();
        LazyMan instance2 = declaredConstructor.newInstance();

        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

继续破坏，道高一尺，魔高一丈 

```java
 //多线程并发
    public static void main(String[] args) throws Exception {
//        LazyMan instance = LazyMan.getInstance();
        Field hello = LazyMan.class.getDeclaredField("hello");
        hello.setAccessible(true);

        Constructor<LazyMan> declaredConstructor = LazyMan.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);//无视私有
        LazyMan instance1 = declaredConstructor.newInstance();
        hello.set(instance1,false);
        LazyMan instance2 = declaredConstructor.newInstance();

        System.out.println(instance1);
        System.out.println(instance2);
    }
```

### 3）静态内部类

```java
//静态内部类
public class Holder {
    private Holder(){

    }
    public static Holder getInstance(){
        return InnerClass.holder;
    }
    public static class InnerClass{
        private static final Holder holder = new Holder();
    }
}
```

> 单例不安全, 因为反射

### 4）枚举

使用枚举，我们就可以防止反射破坏了。

```java
package com.zzy.single;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// enum 是一个什么？ 本身也是一个Class类
public enum EnumSingle {
    INSTANCE;

    public  EnumSingle getInstance(){
        return INSTANCE;
    }
}


class Test{
    public static void main(String[] args) throws Exception {
        EnumSingle instance1 = EnumSingle.INSTANCE;
        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);

        //java.lang.NoSuchMethodException: com.zzy.single.EnumSingle.<init>() 没有空参构造方法
        EnumSingle instance2 = declaredConstructor.newInstance();
        
        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

反编译

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210201233130398.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130210713789.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

**使用jad工具反编译为java**

> 枚举类型的最终反编译源码：

```java
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: EnumSingle.java
public final class EnumSingle extends Enum
{
    public static EnumSingle[] values()
    {
        return (EnumSingle[])$VALUES.clone();
    }
    public static EnumSingle valueOf(String name)
    {
        return (EnumSingle)Enum.valueOf(com/kuang/single/EnumSingle, name);
    }
    private EnumSingle(String s, int i)
    {
        super(s, i);
    }
    public EnumSingle getInstance()
    {
        return INSTANCE;
    }
    public static final EnumSingle INSTANCE;
    private static final EnumSingle $VALUES[];
    static
    {
        INSTANCE = new EnumSingle("INSTANCE", 0);
        $VALUES = (new EnumSingle[] {
                INSTANCE
        });
    }
}
```

> 源码骗了我们，用了一个有参构造器

```java
class Test{
    public static void main(String[] args) throws Exception {
        EnumSingle instance1 = EnumSingle.INSTANCE;
        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(String.class,int.class);
        declaredConstructor.setAccessible(true);

        //java.lang.IllegalArgumentException: Cannot reflectively create enum objects
        EnumSingle instance2 = declaredConstructor.newInstance();
        
        System.out.println(instance1);
        System.out.println(instance2);
    }
}
```

抛出异常： `java.lang.IllegalArgumentException: Cannot reflectively create enum objects`。









# 19、深入理解CAS

### 1）什么是CAS？

CAS : CompareAndSet 比较并交换

> 进大厂必须要深入研究底层 否则进不去 
> 有所突破  修内功，**操作系统，计算机网络原理**。





```java
public class casDemo {
    //
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2020);

        //boolean compareAndSet(int expect, int update)
        //期望值、更新值
        //如果实际值 和 我的期望值相同，那么就更新
        //如果实际值 和 我的期望值不同，那么就不更新
        System.out.println(atomicInteger.compareAndSet(2020, 2021));//修改成功
        System.out.println(atomicInteger.get());

        //因为期望值是2020  实际值却变成了2021  所以会修改失败
        //CAS 是CPU的并发原语
        atomicInteger.getAndIncrement(); //++操作
        System.out.println(atomicInteger.compareAndSet(2020, 2025));//修改失败
        System.out.println(atomicInteger.get());
    }
}
```

> **Unsafe 类**
>
> ![在这里插入图片描述](assets/image-20251230205742621.png)
>

---



![在这里插入图片描述](assets/image-20251231102428715.png)

---

自旋锁：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130210820672.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)







### 2）总结

**CAS**：比较当前工作内存中的值 和 主内存中的值，如果这个值是期望的，那么则执行操作 如果不是就一直循环(使用自旋锁)。

**缺点：**

- 循环会耗时；
- 一次性只能保证一个共享变量的原子性；
- 它会存在ABA问题



### CAS的ABA问题

> #### (狸猫换太子)



![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130210837700.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

线程1：期望值是1，要改成2；

线程2：两个操作：

- 1、期望值是1，要改成3
- 2、期望是3，要1改成

> 对于线程1来说，A的值还是1；但是这个值已经被线程2改动过了，只不过最后改回来了

==ABA问题是否有影响？==

**看业务场景，如果不影响业务就不用管，如果有影响就引入版本号**



问题：

1、循环会耗时

2、一次性只能保证一个共享变量

（基本变量在栈里面都是单线程，跟多线程没关系，只有堆里面才会出现多线程问题）







```java
package com.zzy.cas;

import java.util.concurrent.atomic.AtomicInteger;


public class CASDemo {
    // CAS compareAndSet : 比较并交换 
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2020);


        //平时写的SQL：乐观锁
        
        //期望 更新
        //public final boolean compareAndSet(int expect, int update)
        // 如果我期望的值达到了，那么就更新，否则，就不更新, CAS 是CPU的并发原语 
        // ============== 捣乱的线程 ==================
        System.out.println(atomicInteger.compareAndSet(2020, 2021));
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(2021, 2020));
        System.out.println(atomicInteger.get());

        // ============== 期望的线程 ==================
        System.out.println(atomicInteger.compareAndSet(2020, 77777));
        System.out.println(atomicInteger.get());
    }
}
```

#### 原子引用解决ABA问题

> 原子引用的思想：乐观锁

**带版本号 的原子操作**

考虑重听

[35、原子引用解决ABA问题_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1B7411L7tE/?spm_id_from=333.1391.0.0&p=35&vd_source=67ef3bb4c8d68a96408acdaa865b1313)

```java
package com.marchsoft.lockdemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Description：
 *
 * @author jiaoqianjin
 * Date: 2020/8/12 22:07
 **/

public class CASDemo {
    /**AtomicStampedReference 注意，如果泛型是一个包装类，注意对象的引用问题
     * 正常在业务操作，这里面比较的都是一个个对象
     */
    static AtomicStampedReference<Integer> atomicStampedReference = new
            AtomicStampedReference<>(1, 1);// 版本号是1，只要被修改过，版本号就会变化


    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp(); // 获得版本号
            System.out.println("a1=>" + stamp);// 输出
            
            // 修改操作时，版本号更新 + 1
            atomicStampedReference.compareAndSet(1, 3,
                    atomicStampedReference.getStamp(),
                    atomicStampedReference.getStamp() + 1);
            
            System.out.println("a2=>" + atomicStampedReference.getStamp());
            // 重新把值改回去， 版本号更新 + 1
            System.out.println(atomicStampedReference.compareAndSet(2, 1,
                    atomicStampedReference.getStamp(),
                    atomicStampedReference.getStamp() + 1));
            System.out.println("a3=>" + atomicStampedReference.getStamp());
        }, "a").start();
        
        // 乐观锁的原理相同 
        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp(); // 获得版本号
            System.out.println("b1=>" + stamp);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicStampedReference.compareAndSet(1, ,
                    stamp, stamp + 1));
            System.out.println("b2=>" + atomicStampedReference.getStamp());
        }, "b").start();
    }
}
```

**注意：**

**Integer 使用了对象缓存机制，默认范围是 -128 ~ 127 ，推荐使用静态工厂方法 valueOf 获取对象实例，而不是 new，因为 valueOf 使用缓存，而 new 一定会创建新的对象分配新的内存空间；**

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021013021094151.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)





# 21、各种锁的理解

### 1）公平锁，非公平锁

1. 公平锁： 非常公平， 不能够插队，必须先来后到 
2. 非公平锁：非常不公平，可以插队 （默认都是非公平）

> ```java
> public ReentrantLock() {
> 	sync = new NonfairSync();
> }
> 
> public ReentrantLock(boolean fair) {
> 	sync = fair ? new FairSync() : new NonfairSync();
> }
> ```



### 2）可重入锁

可重入锁（递归锁）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130211017693.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)



Synchronized：可重入锁

Lock：不可重入锁



> **Synchronized**

```java
package com.zzy.lock;


//Synchronized
public class Demo01 {

    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(()->{
            phone.sms();
        },"A").start();

        new Thread(()->{
            phone.sms();
        },"B").start();
    }
}
class Phone{
    public synchronized void sms(){
        System.out.println(Thread.currentThread().getName()+"：sms");
        call();//这里也有锁
    }

    public synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"：call");
    }
}
```

> **Lock 版**

```java
package com.zzy.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Synchronized
public class Demo02 {

    public static void main(String[] args) {
        Phone2 phone = new Phone2();
        new Thread(() -> {
            phone.sms();
        }, "A").start();

        new Thread(() -> {
            phone.sms();
        }, "B").start();
    }
}

class Phone2 {
    Lock lock = new ReentrantLock();

    public synchronized void sms() {
        lock.lock();// 细节问题：lock.lock(); lock.unlock(); 
        // lock锁必须配对，lock与unlock成对出现，否则就会死在里面
        try {
            System.out.println(Thread.currentThread().getName() + "：sms");
            call();//这里也有锁
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void call() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "：call");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
```

- lock锁必须配对，lock和 unlock 必须数量相同；
- 在外面加的锁，也可以在里面解锁；在里面加的锁，在外面也可以解锁；





### 3）自旋锁

> spinlock

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130211117949.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)



案例：自己写一个自旋锁

```java
package com.zzy.lock;


import java.util.concurrent.atomic.AtomicReference;

/**
 * 自己写一个自旋锁
 */
public class SpinlockDemo {
    //初始： int -> 0; 引用类型 Thread -> null
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    // 加锁
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "==>myLock");
        //自旋锁
        while (!atomicReference.compareAndSet(null, thread)) {// 现成如果没抢到锁，就阻塞在循环（自旋）

        }
    }

    // 解锁
    public void myUnlock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "==>myUnlock");
        //自旋锁
        atomicReference.compareAndSet(thread, null);

    }
}
```

> 测试

```java
package com.zzy.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Zhao
 * @DATE 2021/2/3 - 23:29
 */
public class TestSpinLock {
    public static void main(String[] args) {
        //ReentrantLock
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.unlock();

        // 底层使用的自旋锁CAS
        SpinlockDemo lock = new SpinlockDemo();

        new Thread(()->{
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnlock();
            }

        },"T1").start();

        new Thread(()->{
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnlock();
            }

        },"T2").start();

    }
}
```

运行结果：

**t2进程必须等待t1进程Unlock后，才能Unlock，在这之前进行自旋等待。。。。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021020323352695.png)

### 4）==死锁==

> 死锁是什么

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210130211610635.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

**死锁四要素**：

1. 





**死锁测试，怎么排除死锁：**

```java
package com.zzy.lock;
import java.util.concurrent.TimeUnit;

public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new MyThread(lockA, lockB), "T1").start();

        new Thread(new MyThread(lockB, lockA), "T2").start();

    }
}

class MyThread implements Runnable {
    private String lockA;
    private String lockB;

    public MyThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + "lock:" + lockA + "=>get" + lockB);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "lock:" + lockB + "=>get" + lockA);
            }
        }
    }
}
```



如何排查死锁问题

**1、使用jps定位出现问题的进程号（jdk的bin目录下： 有一个jps**工具）

命令：`jps -l`

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210203234752484.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mzc1ODYzMw==,size_16,color_FFFFFF,t_70)

**2、使用`jstack 进程号`，找到死锁问题**

```
jstack 14380
```

![在这里插入图片描述](assets/image-20251231095052538.png)

面试7：工作中排查问题：

1. 查看日志
2. 堆栈信息





> ### 视频地址：https://space.bilibili.com/95256449
>
> 笔记参考：https://blog.csdn.net/weixin_44491927/article/details/108560692
>
> 笔记参考：https://blog.csdn.net/weixin_43758633/article/details/113408853

https://blog.csdn.net/weixin_44491927/article/details/108560692

https://blog.csdn.net/weixin_43758633/article/details/113408853





```

```













