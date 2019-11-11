package com.chen.nio;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @Author liu
 * @Date 2019-03-05 10:39
 */
public class NioTest13 {
    public static void main(String[] args) {
        Charset charset = Charset.forName("utf-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        String inputFile = "";
        String outputFile = "";


    }
}
