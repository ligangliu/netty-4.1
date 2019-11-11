package com.chen.mynetty;


import com.chen.mynetty.pool.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * https://www.bilibili.com/video/av44457831?p=7
 * https://blog.csdn.net/as403045314/article/details/101337368
 * https://www.jianshu.com/p/46861a05ce1e  分析源代码
 * @Author liu
 * @Date 2019-11-10 11:19
 */
public class Start {

    public static void start(int port) {
        NioSelectorRunnablePool nioSelectorRunnablePool =
                new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);
        bootstrap.bind(new InetSocketAddress(port));
        System.out.println("----------------server start------------------");
    }

    public static void main(String[] args) {
        start(9999);
    }
}
