package com.chen.mynetty;


import com.chen.mynetty.pool.Boss;
import com.chen.mynetty.pool.NioSelectorRunnablePool;
import com.chen.mynetty.pool.Worker;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @Author liu
 * @Date 2019-11-10 11:19
 */
public class NioServerBoss extends AbstractNioSelector implements Boss {

    public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor,threadName,selectorRunnablePool);
    }

    /**
     * 也就是当有新的客户端连接到来的时候
     * @param serverSocketChannel
     */
    @Override
    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel) {
        final Selector selector = this.selector;
        registerTask(() -> {
            try {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (ClosedChannelException e) {
                System.out.println("注册失败");
            }
        });
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select();
    }

    /**
     * 来了客户端连接的时候
     * @param selector
     * @throws IOException
     */
    @Override
    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()) {
            return;
        }
        //循环处理，所有返回的客户端连接，并注册OP_READ事件
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            iterator.remove();
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel socketChannel = server.accept();
            socketChannel.configureBlocking(false);
            /**
             * 获取一个worker线程，然后将该客户端交由该worker线程处理
             */
            Worker worker = getSelectorRunnablePool().nextWorker();
            worker.registerNewChannelTask(socketChannel);
            System.out.println("新客户端连接");
        }
    }

}
