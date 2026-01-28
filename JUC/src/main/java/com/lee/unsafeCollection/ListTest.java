package com.lee.unsafeCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//java.util.ConcurrentModificationException 并发修改异常
public class ListTest {
    public static void main(String[] args) {
        //并发操作下list是不安全的
        List<String> list = new ArrayList<>();

        for (int i = 0; i <=10; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
