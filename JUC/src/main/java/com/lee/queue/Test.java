package com.lee.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
//        test1();
//        test2();
//        test3();
        test4();
    }
    /**
     * 会抛异常的
     */
    //如果add操作超出了初始化的容量，会抛java.lang.IllegalStateException
    //而如果队列没有数据了的话，依然执行remove操作的话，会抛出java.util.NoSuchElementException
    public static void test1(){
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.add("1"));
        System.out.println(blockingQueue.add("2"));
        System.out.println(blockingQueue.add("3"));
//        System.out.println(blockingQueue.add("3"));
        System.out.println("===================");
        //这里我们看到遵循了先进先出（FIFO）的原则

        System.out.println(blockingQueue.element());//查看队列首部元素是谁  1
        System.out.println("*******************");
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());

//        System.out.println(blockingQueue.remove());
    }

    /**
     * 有返回值，不抛出异常
     */
    public static void test2(){
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println("====================");
        //如果offer添加操作超过了队列的初始化容量，会返回false，不会抛出异常
//        System.out.println(blockingQueue.offer("d"));
        System.out.println(blockingQueue.peek());  //查看队列首部元素  a
        System.out.println("********************");
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //如果队列中没有数据，进行poll移除操作时，会返回null
//        System.out.println(blockingQueue.poll());
    }

    /**
     * 等待，阻塞（一直阻塞）
     */
    public static void test3() throws InterruptedException {
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.put("i");
        blockingQueue.put("j");
        blockingQueue.put("k");
        //队列初始化容量满了再进行put，会一直阻塞
//        blockingQueue.put("n");
        System.out.println("==================");
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        //队列没有元素的情况下，再进行take（取）操作，会一直阻塞等待
        System.out.println(blockingQueue.take());
    }

    /**
     * 等待，阻塞（超时等待）
     */
    public static void test4() throws InterruptedException {
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("11"));
        System.out.println(blockingQueue.offer("22"));
        System.out.println(blockingQueue.offer("33"));
        System.out.println("====================");
        //如果offer添加操作超过了队列的初始化容量，在等待1s未果后，会终止添加
        System.out.println(blockingQueue.offer("d",1, TimeUnit.SECONDS));
        System.out.println("********************");
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //如果队列中没有数据时，进行poll移除操作，在等待1s未果后，会自动终止取数据
        System.out.println(blockingQueue.poll(1,TimeUnit.SECONDS));
    }
}


