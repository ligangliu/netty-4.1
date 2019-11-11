package com.chen.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author liu
 * @Date 2019-06-07 20:19
 */
public class BioServer2 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9999));
        byte[] buff = new byte[1024];
        while (true) {
            System.out.println("========等待连接=======");
            Socket socket = serverSocket.accept();
            System.out.println("========连接成功========");
            int read = 0;
            System.out.println("========等待客户端输入内容=====");
            while ((read = socket.getInputStream().read(buff)) != -1) {
                System.out.println("read: " + read);
                System.out.println(new String(buff,0,read, Charset.forName("utf-8")));
            }
        }

    }
}
