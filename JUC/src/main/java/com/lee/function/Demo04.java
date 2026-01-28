package com.lee.function;

import java.util.function.Supplier;

/**
 * 供给型接口，没有参数，只有返回值
 */
public class Demo04 {
    public static void main(String[] args) {
//        Supplier<Integer> supplier = new Supplier<Integer>() {
//            @Override
//            public Integer get() {
//                return 1024;
//            }
//        };
        Supplier<Integer> supplier1 = ()->{
            return 1024;
        };
        System.out.println(supplier1.get());
    }
}
