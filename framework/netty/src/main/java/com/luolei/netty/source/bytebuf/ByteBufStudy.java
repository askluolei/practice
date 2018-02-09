package com.luolei.netty.source.bytebuf;

import io.netty.buffer.*;

/**
 * @author 罗雷
 * @date 2018/2/9 0009
 * @time 9:41
 */
public class ByteBufStudy {

    public static void test1() {
        ByteBuf byteBuf = Unpooled.buffer(8);
        System.out.println(byteBuf.capacity());
        System.out.println(byteBuf.refCnt());
        System.out.println("==== 我是分割线 === ");
        for (int i = 0; i < byteBuf.capacity(); i++) {
            byteBuf.writeByte(i);
        }
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
        System.out.println("==== 我是分割线 === ");
        for (byte b : byteBuf.array()) {
            System.out.println(b);
        }
        System.out.println("==== 我是分割线 === ");
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
        System.out.println(byteBuf.maxCapacity());
        byteBuf.writeByte(1);
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
        System.out.println(byteBuf.capacity());
    }

    public static void main(String[] args) {
        test1();
    }
}
