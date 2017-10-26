package com.luolei.pattern.chain.ch1;

/**
 * @author luolei
 * @date 2017-03-30 15:41
 */
public abstract class Handler {
    //维持对下家的引用
    protected Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest(String request);
}
