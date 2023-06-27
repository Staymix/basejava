package com.urise.webapp;

public class MainConcurrency {
    private static volatile int counter;
    private static final Object LOCK = new Object();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main " + Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread0 " + getName() + ", " + getState());
            }
        };
        thread0.start();
        new Thread(() -> {
            System.out.println("thread1 "+Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
        }).start();

        System.out.println("state" + thread0.getState());

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    inc();
                }
            }).start();
        }
        Thread.sleep(100);
        System.out.println(counter);
    }

    private static void inc() {
        double a = Math.sin(13);
        synchronized (LOCK) {
            counter++;
        }
    }
}
