package com.lee.commonAuxiliary;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 测试线程辅助类CyclicBarrierDemo
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        //达到初始化参数的值，执行返回对应的结果
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("召唤神龙成功！");
        });

        for (int i = 1; i <= 7; i++) {
            //这里不加final也是可以的，jdk会自动帮我们加上
            int temp = i;
            new Thread(()->{
                //注意！lambda表达式里面不能直接操作循环变量；需要通过final 一个中间量来进行操作
                System.out.println(Thread.currentThread().getName()+"集齐"+temp+"颗龙珠;");
                try {
                    //等待线程执行完，触发相应的事件
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
