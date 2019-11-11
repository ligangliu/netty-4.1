package com.chen.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author liu
 * @Date 2019-02-16 23:10
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("from client: " + ctx.channel().remoteAddress() + ": " + msg);
        //会从最后的那个Handler一直往上执行每一个Handler直到抵达目的地
        ctx.channel().writeAndFlush("陈立啟，你就是一个傻逼");

        //会从当前的那个Handler的下一个Handler往下传输直到抵达目的地
//        ctx.writeAndFlush("xxx");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

}
