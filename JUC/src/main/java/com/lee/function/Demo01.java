package com.lee.function;

import java.util.function.Function;

/**
 * 测试函数型接口
 */
public class Demo01 {
    public static void main(String[] args) {
        //输出输入的值
//        Function<String, String> function = new Function<String, String>() {
//            @Override
//            public String apply(String s) {
//                return s;
//            }
//        };
        Function<String,String> function = (str)->{
            return str;
        };
        System.out.println(function.apply("abc"));
    }
}
