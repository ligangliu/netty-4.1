package com.chen.nio;

import java.nio.ByteBuffer;

/**
 * @Author liu
 * @Date 2019-03-04 19:03
 */
public class NioTest7 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();



    }
}
