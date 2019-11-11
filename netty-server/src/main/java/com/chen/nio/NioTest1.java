package com.chen.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @Author liu
 * @Date 2019-03-04 18:03
 */
public class NioTest1 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        for (int i = 0; i < 10; i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }
        buffer.flip();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
