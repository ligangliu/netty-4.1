package com.chen.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Author liu
 * @Date 2019-03-04 19:39
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(9999);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocateDirect(2);
        buffers[1] = ByteBuffer.allocateDirect(3);
        buffers[2] = ByteBuffer.allocateDirect(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
           int bytesRead = 0;
           while (bytesRead < messageLength){
               long r = socketChannel.read(buffers);
               bytesRead += r;
               System.out.println("bytesRead: " + bytesRead);

               Arrays.asList(buffers).stream().map(buffer -> "position: " + buffer.position() + " ,limit: " + buffer.limit())
                       .forEach(System.out::println);
               Arrays.asList(buffers).forEach(buffer -> buffer.flip());
               long bytesWritten = 0;
               while (bytesWritten < messageLength){
                   long w = socketChannel.write(buffers);
                   bytesWritten += w;
               }
               Arrays.asList(buffers).forEach(buffer -> buffer.clear());

           }

        }

    }
}
