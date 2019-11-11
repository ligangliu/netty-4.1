package com.chen.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liu
 * @Date 2019-03-04 18:27
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("G:\\圣思园精通并发与netty视频\\b.txt");
        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        byte[] message = "xxxxxxx".getBytes();
        for (int i = 0; i < message.length; i++) {
            buffer.put(message[i]);
        }

        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }
}
