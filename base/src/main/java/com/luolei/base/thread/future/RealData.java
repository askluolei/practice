package com.luolei.base.thread.future;

import java.util.concurrent.Callable;

/**
 * @author luolei
 * @date 2017-03-13 16:30
 */
public class RealData implements Callable<String>{

    private String para;

    public RealData(String para) {
        this.para = para;
    }

    public String call() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(para);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
