package com.luolei.pattern.factory.ch2;

/**
 * @author luolei
 * @date 2017-03-29 16:14
 */
public class Client {
    public static void main(String args[]) {
        LoggerFactory factory;
        Logger logger;
        factory = new FileLoggerFactory(); //可引入配置文件实现
        logger = factory.createLogger();
        logger.writeLog();
    }
}
