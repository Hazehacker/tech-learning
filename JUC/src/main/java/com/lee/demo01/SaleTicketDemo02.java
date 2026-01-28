package com.lee.demo01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用可重入锁ReentrantLock
 */

public class SaleTicketDemo02 {
    public static void main(String[] args) {
        //创建多个线程，调用资源类中的方法

        Ticket02 ticket = new Ticket02();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"CC").start();
    }
}

class Ticket02{

    //票数
    private int number = 30;
    //1.new ReentrantLock对象
    Lock lock = new ReentrantLock();
    //操作方法:卖票
    public void sale(){
        lock.lock();//2.加锁
        //判断是否有票
        try {
            if (number>0){
                number--;
                System.out.println(Thread.currentThread().getName()+"卖出1张票"+",还剩"+number+"张票;");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //3.释放锁
            lock.unlock();
        }
    }
}