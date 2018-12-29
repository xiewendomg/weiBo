package com.spider;

public class A {
    public int constInt =5;
    {
        System.out.println("初始化代码块");
    }
   static {
            System.out.println("haha");

        }


        public static void main(String[] args) {
            String s="bb";
            Integer a=8;
        new A();
        change(a,s);
            System.out.println("s="+s);
            System.out.println(a);
        }

        public static void  change(Integer a,String b){
        }

}
