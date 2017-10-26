package com.luolei.pattern.factory.ch2;

/**
 * @author luolei
 * @date 2017-03-29 16:14
 */
public class FileLoggerFactory implements LoggerFactory {
    public Logger createLogger() {
        //创建文件日志记录器对象
        Logger logger = new FileLogger();
        //创建文件，代码省略
        return logger;
    }
}

