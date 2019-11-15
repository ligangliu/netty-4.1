package com.chen.netty;

import java.io.FileInputStream;

/**
 ByteBuf
 对于我们业务消息的编码来说，推荐使用HeapBuf
 对于i/o通信线程读写缓冲区时，推荐使用DirectByteBuf
 ByteBuf 的实现类有两种纬度
 1。堆上(heap buffer)和非堆上(direct buffer),复合缓冲区(composite buffer)
 2。池化和非池化
    所有两两组合就有四种类型的实现类
 处理器


 * @Author liu
 * @Date 2019-11-12 22:31
 */
public class Summary {
    public static void main(String[] args) throws Exception {
        new FileInputStream("").getChannel();
    }
}
