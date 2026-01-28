package com.lee.unsafeCollection;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
          Map<String, String> map = new ConcurrentHashMap<>();
//        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());

        for (int i = 0; i <=30 ; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
