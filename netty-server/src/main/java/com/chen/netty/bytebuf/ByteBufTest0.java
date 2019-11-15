package com.chen.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @Author liu
 * @Date 2019-03-14 20:20
 */
public class ByteBufTest0 {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        //getByte(i)通过索引访问，并不会改变真实的读索引和写索引
        //我们可以通过ByteBuf的readIndex()与writeIdex()来修改
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
        System.out.println("===" + buffer.readableBytes());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
        System.out.println("====" + buffer.readableBytes());
    }

}
