package com.luolei.pattern.factory.ch2;

/**
 * 数据库日志记录器：具体产品
 * @author luolei
 * @date 2017-03-29 16:12
 */
public class DatabaseLogger implements Logger {
    public void writeLog() {
        System.out.println("数据库日志记录。");
    }
}
