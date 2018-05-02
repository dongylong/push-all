package com.noriental.push;

/**
 * @author dongyl
 * @date 15:41 1/26/18
 * @project push-all
 */
public class TestThread {
    public static void main(String[] args) {
        Thread thread = new Thread(new Worker());
        thread.start();
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                System.out.println("Thread Name:" + Thread.currentThread().getName());
            }
        }
    }
}
