package com.chen.mynetty.pool;

import java.nio.channels.ServerSocketChannel;

/**
 * boss接口
 * @Author liu
 * @Date 2019-11-10 11:18
 */
public interface Boss {
    /**
     * 加入一个新的ServerSocket
     * 类似NIO中serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
     * @param serverSocketChannel
     */
    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel);
}
