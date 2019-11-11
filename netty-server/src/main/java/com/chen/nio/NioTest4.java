package com.chen.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liu
 * @Date 2019-03-04 18:40
 */
public class NioTest4 {
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("G:\\圣思园精通并发与netty视频\\a.txt");
        FileOutputStream outputStream = new FileOutputStream("G:\\圣思园精通并发与netty视频\\b.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer.allocateDirect(1024);

        while (true){
            buffer.clear(); //如果没有这行代码，程序会一直运行
            int read = inputChannel.read(buffer);
            System.out.println("read: " + read);
            if (-1 == read){
                break;
            }
            buffer.flip();
            outputChannel.write(buffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
