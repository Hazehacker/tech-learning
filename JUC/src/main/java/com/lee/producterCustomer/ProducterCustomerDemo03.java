package com.lee.producterCustomer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A执行完调用B,B执行完调用C
 */
public class ProducterCustomerDemo03 {
    public static void main(String[] args) {
        Data03 data = new Data03();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printA();
            }
        },"AA").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printB();
            }
        },"BB").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printC();
            }
        },"CC").start();
    }
}

class Data03{
    private Lock lock = new ReentrantLock();
    //new 3个监视器
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    private int number = 1; //1A  2B  3C
    public void printA(){
        lock.lock();
        try {
            //业务，判断，执行，通知
            while (number!=1){
                condition1.await();
            }
            System.out.println(Thread.currentThread().getName()+"=>"+"AAAAA");
            //唤醒指定的人B
            number = 2;
            condition2.signal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void printB(){
        lock.lock();
        try {
            //业务，判断，执行，通知
            while (number!=2){
                condition2.await();
            }
            System.out.println(Thread.currentThread().getName()+"=>"+"BBBBB");
            //唤醒指定的人B
            number = 3;
            condition3.signal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void printC(){
        lock.lock();
        try {
            //业务，判断，执行，通知
            while (number!=3){
                condition3.await();
            }
            System.out.println(Thread.currentThread().getName()+"=>"+"CCCCC");
            //唤醒指定的人B
            number = 1;
            condition1.signal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}