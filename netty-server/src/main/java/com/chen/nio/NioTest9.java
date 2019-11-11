package com.chen.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liu
 * @Date 2019-03-04 19:35
 */
public class NioTest9 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("G:\\圣思园精通并发与netty视频\\b.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,5);
        mappedByteBuffer.put(0, (byte)'t');
    }
}
