package com.chen.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liu
 * @Date 2019-02-16 23:10
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("from server:  " + ctx.channel().remoteAddress() + ": " + msg);
//        ctx.channel().writeAndFlush("from clinet： 刘丽刚，我好爱你" + LocalDateTime.now());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush("来自于客户端的问候！");
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        list.add("aaa");
        for (String l: list) {
            ctx.channel().writeAndFlush(l);
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("xxx");
    }
}
