package com.lee.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 测试Callable
 */
public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建适配器
        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable());
        //因为FutureTask实现了RunnableFuture接口所以可以通过Thread启动线程
        new Thread(futureTask,"A").start();
        //获取线程返回值
        Integer i = futureTask.get();
        System.out.println(i);
    }
}
class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}