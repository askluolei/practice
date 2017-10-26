package com.luolei.pattern.factory.ch2;

/**
 * @author luolei
 * @date 2017-03-29 16:14
 */
public class DatabaseLoggerFactory implements LoggerFactory {
    public Logger createLogger() {
        //连接数据库，代码省略
        //创建数据库日志记录器对象
        Logger logger = new DatabaseLogger();
        //初始化数据库日志记录器，代码省略
        return logger;
    }
}
