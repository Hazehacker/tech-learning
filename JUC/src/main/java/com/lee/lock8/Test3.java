package com.lee.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 7.一个静态同步方法，一个普通同步方法，通过一个对象去分别在不同的线程里面调用，谁先谁后? //打电话，因为
 * 静态同步方法（类锁）和普通同步方法（对象的锁）用的不是一把锁，所以自然就是先打电话
 * 8.两个对象分别去调用一个静态同步方法和普通同步方法，先打印发短信还是打电话？//打电话  因为两个对象不是用的一把锁，
 * 自然就是谁的延迟小，先打印谁咯
 *
 */
public class Test3 {
    public static void main(String[] args) {

        //这两个对象的类加载器只有一个，static方法锁的是class
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();
        //显然这里一定是发短信方法先拿到对象的锁，所以不管怎么样，都会先执行
        new Thread(()->{
            phone1.sendMsg();
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(()->{
            phone2.call();
        }).start();

    }
}

//只有唯一的一个class对象
class Phone3{
    /**
     * synchronized锁的对象是方法的调用者
     * static 是静态方法，在类进行加载的时候就已经初始化了
     * 所以这里synchronized是在给class进行加锁，所以会按照调用的先后顺序来进行输出
     */
    public static synchronized void sendMsg(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("发短信");
    }
    public synchronized void call(){
        System.out.println("打电话");
    }
}
