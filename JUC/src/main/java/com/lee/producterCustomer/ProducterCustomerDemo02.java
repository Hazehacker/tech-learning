package com.lee.producterCustomer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程之间的通信问题，生产者和消费者问题
 * 线程A B操作同一个变量 num=0
 * A:num+1;
 * B:num-1;
 */
public class ProducterCustomerDemo02 {
    public static void main(String[] args) {

        Data data = new Data();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"C").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"D").start();
    }
}

class Data02{
    private int number = 0;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
//    condition.await();//等待
//    condition.signalAll();//唤醒全部线程
    public void increment() throws InterruptedException {

        try {
            lock.lock();
            //业务代码
            while (number!=0){
                //等待
                condition.await();
            }
            number++;
            //通知其他线程，我+1完毕
            System.out.println(Thread.currentThread().getName()+"=>"+number);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void decrement() throws InterruptedException {
        try{
            lock.lock();
            while (number==0){
                condition.await();//等待
            }
            number--;
            //通知其他线程，我+1完毕
            System.out.println(Thread.currentThread().getName()+"=>"+number);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
