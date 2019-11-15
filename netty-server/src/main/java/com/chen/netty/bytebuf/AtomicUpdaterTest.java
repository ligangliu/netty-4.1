package com.chen.netty.bytebuf;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Author liu
 * @Date 2019-03-15 9:50
 */
public class AtomicUpdaterTest {
    public static void main(String[] args) {
        Person person = new Person();
        /*for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                }catch (InterruptedException e){}
                System.out.println(person.age++);

            }).start();
        }*/

        AtomicIntegerFieldUpdater<Person> atomicIntegerFieldUpdater =
                AtomicIntegerFieldUpdater.newUpdater(Person.class,"age");
        for (int i = 0; i < 10; i++) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(20);
//            }catch (InterruptedException e){}
            new Thread(() -> {
                System.out.println(atomicIntegerFieldUpdater.getAndIncrement(person));
            }).start();
        }
    }
}

class Person {
    volatile int age = 1;
}
