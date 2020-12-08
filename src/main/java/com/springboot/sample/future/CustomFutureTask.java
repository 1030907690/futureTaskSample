package com.springboot.sample.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class CustomFutureTask<V> implements Runnable {

    private V result;
    private CustomCallable<V> customCallable;

    private volatile String state = "NEW";

    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();


    public CustomFutureTask(CustomCallable<V> customCallable) {
        this.customCallable = customCallable;
    }


    @Override
    public void run() {
        try {
            result = customCallable.call();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            state = "DONE";
            Thread waiter = waiters.poll();
            while (waiter != null) {
                LockSupport.unpark(waiter);// 唤醒
                waiter = waiters.poll();
            }
        }
    }

    public V get() throws InterruptedException, ExecutionException {
        if ("DONE".equals(state)) {
            return result;
        }
        while (!"DONE".equals(state)) {
            waiters.add(Thread.currentThread());
            LockSupport.park(); // 挂起线程
        }
        return result;
    }
}
