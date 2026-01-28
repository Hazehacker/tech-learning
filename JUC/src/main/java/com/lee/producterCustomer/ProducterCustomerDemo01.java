package com.lee.producterCustomer;

/**
 * 线程之间的通信问题，生产者和消费者问题
 * 线程A B操作同一个变量 num=0
 * A:num+1;
 * B:num-1;
 */
public class ProducterCustomerDemo01 {
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

class Data{
    private int number = 0;

    public synchronized void increment() throws InterruptedException {
        while (number!=0){
            this.wait();
        }
        number++;
        //通知其他线程，我+1完毕
        System.out.println(Thread.currentThread().getName()+"=>"+number);
        this.notifyAll();
    }
    public synchronized void decrement() throws InterruptedException {
        while (number==0){
            this.wait();
        }
        number--;
        //通知其他线程，我+1完毕
        System.out.println(Thread.currentThread().getName()+"=>"+number);
        this.notifyAll();
    }
}
