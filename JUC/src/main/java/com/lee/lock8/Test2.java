package com.lee.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 5.增加两个静态的同步方法，只有一个对象，先打印发短信还是打电话？//发短信  因为static 是静态方法，在类进行加载的时
 * 候就已经初始化了所以这里synchronized是在给class进行加锁，所以会按照调用的先后顺序来进行输出
 * 6.new两个对象，两个同步的静态方法，先打印发短信还是打电话？ //发短信
 */
public class Test2 {
    public static void main(String[] args) {

        //这两个对象的类加载器只有一个，static方法锁的是class
        Phone2 phone1 = new Phone2();
        Phone2 phone2 = new Phone2();

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
class Phone2{
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
    public static synchronized void call(){
        System.out.println("打电话");
    }
}
