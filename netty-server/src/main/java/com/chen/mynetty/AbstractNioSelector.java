package com.chen.mynetty;


import com.chen.mynetty.pool.NioSelectorRunnablePool;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象selector线程类
 * @Author liu
 * @Date 2019-11-10 11:18
 */
public abstract class AbstractNioSelector implements Runnable {

    //线程池
    private final Executor executor;
    //选择器
    protected Selector selector;
    //选择器wakeUp标记状态
    protected final AtomicBoolean wakeUp = new AtomicBoolean();
    //任务队列
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    //线程名词
    private String threadName;

    /**
     * 线程管理对象
     */
    protected NioSelectorRunnablePool selectorRunnablePool;

    public AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        this.executor = executor;
        this.threadName = threadName;
        this.selectorRunnablePool = selectorRunnablePool;
        //一个线程加一个应该绑定一个selector，类似于一个线程为一批客户端服务
        openSelector();
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a selector");
        }
        /**
         * 使用线程池执行当前对象，运行当前线程
         * 也就是执行下面的run方法
         * boss[i] = new NioServerBoss(boss,"boss thread " + (i+1), this)
         * boss是我们初始化传进来的线程池，
         */
        executor.execute(this);
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        //让该线程一直执行
        while (true) {
            try {
                wakeUp.set(false);
                //让子类，NioServerBoss和NioServerWorker去实现
                //因为有可能子类需要select.select(long)的情况
                select(selector);
                //执行队列中任务
                processTaskQueue();
                //让子类去实现具体的逻辑
                process(selector);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 执行队列中的任务
     */
    private void processTaskQueue() {
        for (;;) {
            final Runnable task = taskQueue.poll();
            if (task == null) {
                break;
            }
            task.run();
        }
    }

    /**
     * 注册一个任务，并且激活selector
     * @param task
     */
    protected final void registerTask(Runnable task) {
        taskQueue.add(task);
        Selector selector = this.selector;
        if (selector != null) {
            if (wakeUp.compareAndSet(false,true)) {
                /**
                 * 也就是说加入一个新的客户端时，应该立马激活selector状态，即
                 * 如果select或select(long)被阻塞，调用这个方法将其立即返回。
                 */
                selector.wakeup();
            }
        }else {
            taskQueue.remove(task);
        }
    }

    /**
     * 获取线程管理对象
     * @return
     */
    public NioSelectorRunnablePool getSelectorRunnablePool() {
        return selectorRunnablePool;
    }

    protected abstract int select(Selector selector) throws IOException;
    protected abstract void process(Selector selector) throws IOException;
}
