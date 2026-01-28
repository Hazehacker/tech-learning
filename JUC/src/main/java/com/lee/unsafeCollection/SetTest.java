package com.lee.unsafeCollection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *java.util.ConcurrentModificationException（同样也会出现并发修改异常）
 */
public class SetTest {
    public static void main(String[] args) {
//      Set<String> hashSet = new HashSet<>();
//      Set<String> hashSet = Collections.synchronizedSet(new HashSet<>());
//        Set<String> hashSet = new CopyOnWriteArraySet<>();
        Set<String> hashSet = new ConcurrentSkipListSet<>();
        for (int i = 0; i <= 1000; i++) {
            new Thread(()->{
                hashSet.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(hashSet);
            },String.valueOf(i)).start();
        }
    }
}
