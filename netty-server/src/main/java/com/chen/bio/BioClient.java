package com.chen.bio;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author liu
 * @Date 2019-05-14 14:11
 */
public class BioClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(9999));
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入内容。。。");
        OutputStream os = null;
        while (true) {
            String next = scanner.next();
            os = socket.getOutputStream();
            for (int i = 0; i < 10; i++) {
                os.write(next.getBytes());
            }
//            if (os != null) os.close();
//            if (socket != null) socket.close();
        }
    }
}
