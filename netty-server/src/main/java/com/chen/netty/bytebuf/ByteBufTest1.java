package com.chen.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @Author liu
 * @Date 2019-03-14 20:55
 */
public class ByteBufTest1 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.copiedBuffer("hello world", Charset.forName("utf-8"));
        //堆上的缓存底层是一个数组,堆上的缓冲底层就是用一个数组存储的
        //而非堆上(直接缓冲区)的缓冲底层并是一个字节数组存储的，所以不能通过数组的形式调用
        if (buffer.hasArray()) {
            byte[] content = buffer.array();
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println(buffer.arrayOffset());
            System.out.println(buffer.readerIndex());
            System.out.println(buffer.writerIndex());
            System.out.println(buffer.capacity());
            System.out.println(buffer.readableBytes());

            for (int i = 0; i < buffer.readableBytes(); i++) {
                System.out.println((char)buffer.getByte(i));
            }
            System.out.println(buffer.getCharSequence(0,4,Charset.forName("utf-8")));
        }
        System.out.println(buffer);

    }
}
