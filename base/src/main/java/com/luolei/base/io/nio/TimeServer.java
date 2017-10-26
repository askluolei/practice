package com.luolei.base.io.nio;

import java.io.IOException;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class TimeServer {
    public static void main(String[] args) throws IOException{
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //默认
            }
        }
        MultiplexerTimeServer timeServer =new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
