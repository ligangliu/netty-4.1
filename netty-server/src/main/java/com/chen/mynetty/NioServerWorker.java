package com.chen.mynetty;


import com.chen.mynetty.pool.NioSelectorRunnablePool;
import com.chen.mynetty.pool.Worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @Author liu
 * @Date 2019-11-10 11:19
 */
public class NioServerWorker extends AbstractNioSelector implements Worker {

    public NioServerWorker(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor,threadName,selectorRunnablePool);
    }

    /**
     * 加入一个新的socket客户端，也就是为一个socket客户端注入OP_READ事件
     * @param socketChannel
     */
    @Override
    public void registerNewChannelTask(SocketChannel socketChannel) {
        final Selector selector = this.selector;
        registerTask(() -> {
            try {
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                System.out.println("注册失败");
            }
        });
    }

    @Override
    protected int select(Selector selector) throws IOException {
        return selector.select(500);
    }

    /**
     * 也即是当客户但发送消息进行处理的逻辑
     * @param selector
     * @throws IOException
     */
    @Override
    protected void process(Selector selector) throws IOException {
        Set<SelectionKey> keys = selector.selectedKeys();
        if (keys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            //处理接收到的数据
            // 数据总长度
            int ret = 0;
            boolean failure = true;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读取数据
            try {
                ret = socketChannel.read(buffer);
                failure = false;
            } catch (Exception e) {
            }
            //判断是否连接已断开
            if (ret <= 0 || failure) {
                key.cancel();
                System.out.println("客户端断开连接");
            }else{
                System.out.println("收到数据:" + new String(buffer.array()));
                //回写数据
                ByteBuffer outBuffer = ByteBuffer.wrap("收到\n".getBytes());
                socketChannel.write(outBuffer);// 将消息回送给客户端
            }
        }
    }
}

