package com.lee.commonAuxiliary;

import java.util.concurrent.CountDownLatch;

/**
 * 测试线程辅助类减法计数器
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //初始化，总数是6，在必须要执行完某个任务的时候，使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"Go out");
                countDownLatch.countDown(); //数量减一
            },String.valueOf(i)).start();
        }
        //等待计数器归0,然后再向下执行
        countDownLatch.await();
        System.out.println("close the door");
    }
}
