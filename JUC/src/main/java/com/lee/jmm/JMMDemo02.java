package com.lee.jmm;

import java.util.concurrent.atomic.AtomicInteger;

public class JMMDemo02 {

    //volatile不保证原子性
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add(){
        num.getAndIncrement();
    }

    public static void main(String[] args) {
        //理论上num结果应该为20000
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }
        while(Thread.activeCount()>2){
            //让出计算机资源并且重新竞争资源
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+""+num); //19469
    }
}
