package com.chen.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author liu
 * @Date 2019-06-07 20:26
 */
public class BioClient2 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(9999));
        OutputStream os = null;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            os = socket.getOutputStream();
            String line = scanner.next();
            if (line.contains("end")) break;
            for (int i = 0; i < 10; i++) {
                os.write(line.getBytes());
            }
        }
        if (os != null) os.close();
        if (socket != null) socket.close();
    }
}
