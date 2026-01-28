package com.lee.commonAuxiliary;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        //这里的3是线程总数
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <=6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到了车位！");
                    TimeUnit.SECONDS.sleep(2); //等待2s
                    System.out.println(Thread.currentThread().getName()+"离开了车位！");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    //释放车位
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
