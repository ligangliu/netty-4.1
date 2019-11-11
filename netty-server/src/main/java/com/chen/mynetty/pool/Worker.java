package com.chen.mynetty.pool;

import java.nio.channels.SocketChannel;

/**
 * worker接口
 * @Author liu
 * @Date 2019-11-10 11:18
 */
public interface Worker {
    /**
     * 加入一个新的客户端会话
     * @param channel
     */
    public void registerNewChannelTask(SocketChannel channel);
}
