package com.luolei.netty.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * handler 就是用来处理接收到和响应数据的
 * 获取数据走  inbound
 * 输出数据走  outbound
 *
 * @author 罗雷
 * @date 2018/1/17 0017
 * @time 9:44
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
        //不读取数据
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
