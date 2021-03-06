package com.luolei.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * main 方法起一个服务
 *
 * @author 罗雷
 * @date 2018/1/17 0017
 * @time 9:46
 */
public final class DiscardServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8009"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        /**
         * 这里主要是用来测试 SSL 的
         * 如果开启了 则生成自签名证书，配置SSL
         */
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        /**
         * bossGroup 用来接受请求
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        /**
         * workerGroup 用来处理数据（是数据转换，复杂业务通常不在这里处理，心跳之类的可以，反正不要阻塞这里的线程）
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /**
             * 启动辅助类
             */
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // NIO 的模式
                    .channel(NioServerSocketChannel.class)
                    //处理请求时候的 handle
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //channel 里面的 handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            //一直 addLast 那 sslCtx 就是第一个
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            p.addLast(new DiscardServerHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            /**
             * 绑定端口 同步等待
             */
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
