package com.lee.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 1.标准情况下，两个线程先打印发短信还是打电话？   //发短信
 * 2.sendMsg()延迟4s后，两个线程是先打印发短息还是打电话？  //发短信
 * 3.增加一个普通方法后，先执行发短信还是hello？//这个时候就不存在抢锁的问题了，看谁的延时少就去执行谁
 * 4.两个对象分别去调用两个同步方法，是先发短信还是先打电话?(sendMsg延迟4s，call延迟1s)  //打电话啊
 *
 */
public class Test1 {
    public static void main(String[] args) {

        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
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

class Phone{
    /**
     * synchronized锁的对象是方法的调用者
     * 两个同步方法用的是同一把锁，谁先拿到就谁先执行！
     */
    public synchronized void sendMsg(){
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
    public void hello(){
        System.out.println("Hello");
    }
}
