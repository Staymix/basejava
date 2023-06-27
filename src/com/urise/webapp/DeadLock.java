package com.urise.webapp;

public class DeadLock {
    public static final Object LOCK_1 = new Object();
    public static final Object LOCK_2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        deadLock(LOCK_1, LOCK_2).start();
        deadLock(LOCK_2, LOCK_1).start();
    }

    static Thread deadLock(Object lock1, Object lock2) {
        return new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + ": lock1" + " захвачен\n" +
                        Thread.currentThread().getName() + ": попытка захватить lock2");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + ": lock1 и lock2 захвачен");
                }
            }
        });
    }
}