package com.chen.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

/**
 * @Author liu
 * @Date 2019-03-14 21:20
 */
public class ByteBufTest2 {
    public static void main(String[] args) {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

        ByteBuf buffer1 = Unpooled.buffer(10);
        ByteBuf buffer2 = Unpooled.directBuffer(10);
        compositeByteBuf.addComponents(buffer1,buffer2);
//        compositeByteBuf.removeComponent(0);

        Iterator<ByteBuf> iterator = compositeByteBuf.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("=============");
        compositeByteBuf.forEach(System.out::println);

    }
}
