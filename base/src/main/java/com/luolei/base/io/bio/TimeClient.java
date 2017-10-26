package com.luolei.base.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * BIO 客户端
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class TimeClient {
    public static void main(String[] args) {

        int port = 8080;
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }

        }
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            out.print("QUERY TIME ORDER");
            out.flush();
            //发送输出关闭标记,为什么要发送这个呢？因为服务端使用的是 readLine，这个阻塞读取，没接收到关闭标记，就一直阻塞在那里
            //那可不可以关闭输出流呢？ 答案是不行的，因为关闭输出流，同时会关闭socket
            //如果不想发送关闭标记，那么就需要修改服务端读取数据的方式了
            socket.shutdownOutput();
            System.out.println("Send order 2 server succeed.");
            Thread.sleep(1000);
            String resp = in.readLine();
            System.out.println("Now is : " + resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
