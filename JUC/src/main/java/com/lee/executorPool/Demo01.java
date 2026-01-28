package com.lee.executorPool;

import java.util.concurrent.*;

public class Demo01 {
    public static void main(String[] args) {

//        ExecutorService executor = Executors.newSingleThreadExecutor();//单个线程
//        ExecutorService executor = Executors.newFixedThreadPool(5);//创建一个固定的线程池大小
/**
         * 创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，
         * 那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，
         * 此线程池又可以智能的添加新线程来处理任务。此线程池不会对线程池大小做限制，
         * 线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。
         */
//        ExecutorService executor = Executors.newCachedThreadPool();

        //获取CPU的核数
        System.out.println(Runtime.getRuntime().availableProcessors());

        //自定义线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,
                5,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        try {
            //最大承载：阻塞队列值+最大线程数(这里是5+3=8)
            for (int i = 0; i < 9; i++) {
                poolExecutor.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"ok");
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            poolExecutor.shutdown();
        }
    }
}
