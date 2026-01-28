package com.lee.rewriteLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试读写锁
 *
 * 独占锁(写锁) 一次只能被一个线程占有
 * 共享锁(读锁) 可以同时被多个线程占有
 */
public class ReadWriteLockDemo {
    public static void main(String[] args) {
//        MyCache myCache = new MyCache();
        MyCacheLock myCacheLock = new MyCacheLock();
        for (int i = 1; i <= 10; i++) {
            final int temp = i;
            new Thread(()->{
                myCacheLock.put(temp+"",temp+"");
            },String.valueOf(i)).start();
        }
        for (int i = 1; i <= 10; i++) {
            final int temp = i;
            new Thread(()->{
                myCacheLock.get(temp+"");
            },String.valueOf(i)).start();
        }
    }
}
//不加读写锁之前
class MyCache{
    private volatile Map<String,Object> map = new HashMap<>();
    //存
    public void put(String key,Object value){
        System.out.println(Thread.currentThread().getName()+"写入"+key);
        map.put(key,value);
        System.out.println(Thread.currentThread().getName()+"写入ok");
    }
    //取
    public void get(String key){
        System.out.println(Thread.currentThread().getName()+"读取"+key);
        Object value = map.get(key);
        System.out.println(Thread.currentThread().getName()+"读入ok");
    }
}
//加了读写锁之后
class MyCacheLock{
    private volatile Map<String,Object> map = new HashMap<>();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //存
    public void put(String key,Object value){
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"写入"+key);
            map.put(key,value);
            System.out.println(Thread.currentThread().getName()+"写入ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    //取
    public void get(String key){
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+"读取"+key);
            Object value = map.get(key);
            System.out.println(Thread.currentThread().getName()+"读入ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}