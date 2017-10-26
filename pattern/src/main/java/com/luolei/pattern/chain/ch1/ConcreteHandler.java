package com.luolei.pattern.chain.ch1;

/**
 * @author luolei
 * @date 2017-03-30 15:42
 */
public class ConcreteHandler extends Handler {
    public void handleRequest(String request) {
        if (request.contains("123")) {
            //处理请求
        } else {
            this.successor.handleRequest(request);  //转发请求
        }
    }
}
