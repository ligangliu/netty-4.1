package com.chen.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Author liu
 * @Date 2019-03-05 14:10
 */
public class NioServer2 {

    private Selector selector;
    private int port = 8899;

    public void init() throws IOException {
        selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端启动，启动端口：" + this.port);
    }

    public void start() throws IOException {
        while (true){
            this.selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()){
                    accept(selectionKey);
                }else if (selectionKey.isReadable()){
                    read(selectionKey);
                }
            }
        }
    }

    /**
     * 处理连接请求
     * @param selectionKey
     * @throws IOException
     */
    private void accept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

    /**
     * 处理读请求
     * @param selectionKey
     * @throws IOException
     */
    private void read(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        socketChannel.read(buffer);
        buffer.flip();
        String receiveMessage = new String(buffer.array()).trim();
        System.out.println("客户端的请求内容： " + receiveMessage);

        String outString = "HTTP/1.1 200 OK\n"
                +"Content-Type:text/html; charset=UTF-8\n\n"
                +"<html>\n"
                +"<head>\n"
                +"<title>first page</title>\n"
                +"</head>\n"
                +"<body>\n"
                +"hello fomcat\n"
                +"</body>\n"
                +"</html>";

        ByteBuffer buffer1 = ByteBuffer.wrap(outString.getBytes());
        socketChannel.write(buffer1);
        socketChannel.close();
    }

    public static void main(String[] args) {
        NioServer2 server2 = new NioServer2();
        try {
            server2.init();
            server2.start();
        }catch (Exception e){

        }
    }
}
