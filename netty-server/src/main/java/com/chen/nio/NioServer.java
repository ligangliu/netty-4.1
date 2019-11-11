package com.chen.nio;


import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author liu
 * @Date 2019-03-05 9:23
 */
public class NioServer {

    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main(String[] args) throws  Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(9999));
        //注册至Selector上
        /*
         可以把Selector理解成一个ArrayList，其中ArrayList
         */
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //主线程进入一个死循环，一直监听
        while (true) {
            System.out.println("阻塞前======");
            int select = selector.select();
            System.out.println("阻塞后======");
            //我们还可以通过SelectionKey反过去获取到ServerSocketChannel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("select: " + select + " length: " + selectionKeys.size());
            selectionKeys.forEach(selectionKey -> {
                SocketChannel client = null;
                try {
                    if (selectionKey.isAcceptable()){
                        //这里为什么是ServerSocketChannel呢？
                        //因为当事件为isAcceptable的时候，是ServerSocketChannl注册的，所以只可能是ServerSocketChannel
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                        client =  serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        String key = "[" + UUID.randomUUID().toString() + "]";
                        clientMap.put(key, client);
                    } else if (selectionKey.isReadable()){
                        client = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int count = client.read(buffer);
                        if (count > 0){
                            buffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String receiveMessage = String.valueOf(charset.decode(buffer).array());

                            System.out.println(client + ": " + receiveMessage);
                            String senderKey = null;
                            for (Map.Entry<String, SocketChannel> entry: clientMap.entrySet()){
                                if (client == entry.getValue()){
                                    senderKey = entry.getKey();
                                    break;
                                }
                            }
                            for (Map.Entry<String, SocketChannel> entry: clientMap.entrySet()) {
                                SocketChannel value = entry.getValue();
                                ByteBuffer buffer1 = ByteBuffer.allocate(1024);
                                buffer1.put((senderKey + " : " + receiveMessage).getBytes());
                                buffer1.flip();
                                value.write(buffer1);
                            }
                        }
                    }
                }catch (Exception e){
                }
                selectionKeys.clear();
            });
        }

    }
}
