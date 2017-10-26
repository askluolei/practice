package com.luolei.base.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于NIO 的时间服务器
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            stop = false;
            selector = Selector.open();//打开选择器
            servChannel = ServerSocketChannel.open();//打开服务端socket通道
            servChannel.configureBlocking(false);//设置为非阻塞
            servChannel.socket().bind(new InetSocketAddress(port), 1024);//监听端口，设置等待连接的最大数量
            servChannel.register(selector, SelectionKey.OP_ACCEPT);//注册到选择器上，感兴趣的事件为 accept
            System.out.println("The time server is start in port:" + port);//监听成功
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void run() {
        while(!stop) {
            try {
                selector.select(1000);//这是一个阻塞的选择操作
                Set<SelectionKey> selectionKeys = selector.selectedKeys();//返回选择的key，也就是由I/O操作的通道
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()) {
                    key = it.next();
                    it.remove();//处理过的key要remove掉，否则一直在选择器里面
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();//注意，出现异常了，就不再选择器中注册感兴趣的事件了
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {//key是否有效
            if (key.isAcceptable()) {//如果是accept
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();//可以接受连接的肯定是ServerSocketChannel
                SocketChannel sc = ssc.accept();//接受连接，得到一个SocketChannel
                sc.configureBlocking(false);//设置为非阻塞
                sc.register(selector, SelectionKey.OP_READ);//注册对read感兴趣事件
            }

            if (key.isReadable()) {//可读，在服务器，可读，肯定是已经完成连接的，有一个专门的SocketChannel来处理客户端I/O请求
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);//一个操作字节的容器
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {//说明有数据可读
                    readBuffer.flip();//变成读模式,limit = position  position = 0
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order : " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    sc.register(selector, SelectionKey.OP_WRITE);
                    doWrite(sc, currentTime);
                    key.cancel();
                } else if (readBytes < 0) {
                    //对链路关闭
                    key.cancel();
                    sc.close();
                } else {
                    //读到0字节，忽略
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();//写模式切换到读模式
            channel.write(writeBuffer);
            channel.close();
        }
    }
}
