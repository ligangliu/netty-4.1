package com.chen.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author liu
 * @Date 2019-03-05 10:05
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));

            while (true){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey: selectionKeys){
                    if (selectionKey.isConnectable()){
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        if (client.isConnectionPending()){
                            client.finishConnect();
                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
                            writeBuffer.flip();
                            client.write(writeBuffer);

                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            executorService.submit(() -> {
                                while (true) {
                                    try {
                                        writeBuffer.clear();
                                        InputStreamReader inputStream = new InputStreamReader(System.in);
                                        BufferedReader br = new BufferedReader(inputStream);
                                        String sendMessage = br.readLine();

                                        writeBuffer.put(sendMessage.getBytes());
                                        writeBuffer.flip();
                                        client.write(writeBuffer);
                                    }catch (Exception e){

                                    }
                                }
                            });
                        }
                        client.register(selector, SelectionKey.OP_READ);
                    }else if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int count = client.read(readBuffer);
                        if (count > 0){
                            String receiveMessage = new String(readBuffer.array(),0,count);
                            System.out.println("receiveMessage: " + receiveMessage);
                        }
                    }
                }
                selectionKeys.clear();
            }
        }catch (Exception e){

        }
    }
}
