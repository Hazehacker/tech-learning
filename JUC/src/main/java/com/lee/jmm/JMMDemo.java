package com.lee.jmm;

import java.util.concurrent.TimeUnit;

public class JMMDemo {

//    private static int num = 0;
    //加了volatile关键字后就不会出现死循环了（volatile的保证可见性）
    private volatile static int num = 0;
    public static void main(String[] args){
        new Thread(()->{
            while (num==0){

            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
            num=1;
            System.out.println(num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
