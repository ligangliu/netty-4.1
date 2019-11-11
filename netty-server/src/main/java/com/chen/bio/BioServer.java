package com.chen.bio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author liu
 * @Date 2019-05-14 14:05
 */
public class BioServer {
    public static void main(String[] args) throws Exception {
        byte[] bs = new byte[1024];
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9999));
        while (true) {
            System.out.println("等待连接。。。。");
            Socket socket = serverSocket.accept(); //阻塞---程序释放CPU资源,
            /**
             * 现在假设我们有一个方法可以使得该accept不阻塞(小张)
             * 这样的socket.getInputStream().read(bs)) == 0表示没有数据发送
             * 有数据就读取。
             * 这个时候。另一个来连了(小李)，它发送了数据，读出来处理
             * 这时候小张也发送了，那么就会将小张的丢失。
             * 这个时候我们可以用一个ArrayList<>保存所有的socket
             * 然后每次循环所有的socket，看其是否发送了数据。这就可以类似与Selector了
             * 只不过操作系统底层帮我们把有事件的socket返回给我们
             */
            //socket.setNB(不阻塞)
            // 这里我们理解socket在底层就是一个文件

            System.out.println("连接成功。。。。");
            System.out.println("start data...");
            int read = 0;
            while ((read = socket.getInputStream().read(bs)) != -1)
                System.out.printf("receive data: %s%n", new String(bs, 0,
                        read, Charset.forName("utf-8")));
        }
    }
}
