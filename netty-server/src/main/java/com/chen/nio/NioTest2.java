package com.chen.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liu
 * @Date 2019-03-04 18:22
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("G:\\圣思园精通并发与netty视频\\a.txt");
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        channel.read(buffer);

        buffer.flip();
        while (buffer.remaining() > 0 ){
            byte b = buffer.get();
            System.out.println((char)b);
        }
    }
}
