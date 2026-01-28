package com.lee.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 测试同步队列
 */
public class SynchronousQueueDemo {
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "put1");
            try {
                blockingQueue.put("1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "put2");
            try {
                blockingQueue.put("2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "put3");
            try {
                blockingQueue.put("3");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"T1").start();
        new Thread(()->{
            //******************************
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println(Thread.currentThread().getName()+"=>"+ blockingQueue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //******************************
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println(Thread.currentThread().getName()+"=>" + blockingQueue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //**********************************
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //**********************************
            try {
                System.out.println(Thread.currentThread().getName()+"=>" + blockingQueue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"T2").start();
    }
}
