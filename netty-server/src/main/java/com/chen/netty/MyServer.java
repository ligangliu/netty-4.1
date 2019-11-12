package com.chen.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executors;

/**
 * netty对于Socket的支持
 * @Author liu
 * @Date 2019-02-16 23:04
 */
public class MyServer {

    public static void main(String[] args) throws Exception {
        /**
         * NioEventLoopGroup -- 线程池
         * NioEventLoop -- Thread
         * bossGroup用于接收Tcp请求，它会将请求交给workGroup，workGroup获得真正的连接，然后和连接进行通信，比如读写解码等操作
         *
         */
        //第一步建立bossGroup 接受数据然后转发给workerGroup ，是一个死循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //第二部 workerGroup 完成实际数据的处理，也是一个死循环
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * ServerBootstap是一个引导类，用于启动服务器和引导整个程序的初始化
             *  group 方法将两个 group 放入了自己的字段中，用于后期引导使用。
             *  添加了一个 channel，其中参数一个Class对象，引导类将通过这个 Class 对象反射创建 Channel。
             * 然后添加了一些TCP的参数。
             * 添加了一个服务器专属的日志处理器 handler。
             * 添加一个 SocketChannel（不是 ServerSocketChannel，针对的客户端连接后的处理）的 handler。
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * group(bossGroup,workerGroup)
             * 会将我们的bossGroup和workerGroup赋值给ServerBootstrap以及他的父类的引用
             * 这样的话我们就可以通过引用使用它
             */
            serverBootstrap.group(bossGroup,workerGroup)
                    /**
                     * channel(NioServerSocketChannel.class)
                     * 完成了channel(NioServerSocketChannel.class)
                     * private volatile ChannelFactory<? extends C> channelFactory;的赋值
                     */
                    .channel(NioServerSocketChannel.class)
                    //对ServerBootstrap中childOptions赋值
                    .childOption(ChannelOption.SO_BACKLOG,100)
                    //对ServerBootstrap中private volatile ChannelHandler handler;赋值
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //对ServerBootstrap中private volatile ChannelHandler childHandler;赋值
                    .childHandler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //****************下面这些都是netty为我们定义好的handler*************
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LengthFieldBasedFrameDecoder
                                    (Integer.MAX_VALUE,0,4,0,4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            //****************下面自己编写handler*************
                            pipeline.addLast("myhandler",new MyServerHandler());
                        }
                    });
            /**
             * 绑定端口并阻塞至连接成功
             * main线程阻塞等待关闭。
             */
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();

            System.out.println("=================服务端开始工作================");
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
