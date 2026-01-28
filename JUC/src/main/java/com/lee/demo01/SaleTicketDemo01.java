package com.lee.demo01;

public class SaleTicketDemo01 {
    public static void main(String[] args) {
        //创建多个线程，调用资源类中的方法

        Ticket ticket = new Ticket();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"CC").start();
    }
}

class Ticket{

    //票数
    private int number = 30;

    //操作方法:卖票
    public synchronized void sale(){
        //判断是否有票
        if (number>0){
            number--;
            System.out.println(Thread.currentThread().getName()+"卖出1张票"+",还剩"+number+"张票;");
        }
    }
}
