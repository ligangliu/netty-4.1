package com.chen.mynetty;

import com.chen.mynetty.pool.Boss;
import com.chen.mynetty.pool.NioSelectorRunnablePool;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 服务类
 * @Author liu
 * @Date 2019-11-10 11:19
 */
public class ServerBootstrap {
    private NioSelectorRunnablePool selectorRunnablePool;

    public ServerBootstrap(NioSelectorRunnablePool selectorRunnablePool) {
        this.selectorRunnablePool = selectorRunnablePool;
    }

    /**
     * 绑定端口
     *
     * @param localAddress
     */
    public void bind(final SocketAddress localAddress) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(localAddress);
            Boss boss = selectorRunnablePool.nextBoss();
            //注册 OP_ACCEPT事件
            boss.registerAcceptChannelTask(serverSocketChannel);
        }catch (Exception e) {

        }
    }

}
