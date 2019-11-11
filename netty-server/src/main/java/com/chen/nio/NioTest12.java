package com.chen.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author liu
 * @Date 2019-03-04 20:10
 */
public class NioTest12 {
    public static void main(String[] args) throws Exception {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(ports[i]));

            //注册，将channel于selector相关联起来
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口：" + ports[i]);
        }

        while (true){
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector,SelectionKey.OP_READ);
                    iterator.remove();
                    System.out.println("获得客户端连接：" + socketChannel);
                }else if (selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int bytesRead = 0;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                    while (true){
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0){
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        bytesRead += read;
                    }
                }
            }
        }
    }
}
