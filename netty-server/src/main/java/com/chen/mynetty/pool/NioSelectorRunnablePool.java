package com.chen.mynetty.pool;

import com.chen.mynetty.NioServerBoss;
import com.chen.mynetty.NioServerWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Selector线程管理者
 * @Author liu
 * @Date 2019-11-10 11:18
 */
public class NioSelectorRunnablePool {

    /**
     * boss线程数组
     */
    private final AtomicInteger bossIndex = new AtomicInteger();
    private Boss[] bosses;
    /**
     * worker线程数组
     */
    private final AtomicInteger workIndex = new AtomicInteger();
    private Worker[] workers;

    /**
     * Executor {void execute(Runnable command)}
     * @param boss
     * @param worker
     */
    public NioSelectorRunnablePool(Executor boss,Executor worker) {
        //初始化线程池boss,默认是1
        initBoss(boss,1);
        //初始化worker，数量默认是系统的核心数量*2
        initWorker(worker,Runtime.getRuntime().availableProcessors() * 2);
    }

    /**
     * 初始化boss线程池
     * @param boss
     * @param count
     */
    private void initBoss(Executor boss,int count) {
        this.bosses = new NioServerBoss[count];
        for (int i = 0; i < bosses.length; i++) {
            bosses[i] = new NioServerBoss(boss,"boss thread" + (i + 1),this);
        }
    }

    /**
     * 初始化worker线程
     * @param worker
     * @param count
     */
    private void initWorker(Executor worker,int count) {
        this.workers = new NioServerWorker[count];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new NioServerWorker(worker,"worker thread " + (i+1),this);
        }
    }

    /**
     * 获取下一个boss线程
     * @return
     */
    public Boss nextBoss() {
        return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
    }
    /**
     * 获取一个workerxianc
     * @return
     */
    public Worker nextWorker() {
        return workers[Math.abs(workIndex.getAndIncrement() % workers.length)];
    }




}
