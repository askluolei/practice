package com.luolei.base.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchrousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            asynchrousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchrousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port : " + this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept() {
        asynchrousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }

}
