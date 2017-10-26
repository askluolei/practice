package com.luolei.base.thread.threadlocal;

import java.util.Date;

/**
 * @author luolei
 * @date 2017-03-13 16:44
 */
public class InheritableThreadLocalExt extends InheritableThreadLocal<Long>{

    @Override
    protected Long initialValue() {
        return new Date().getTime();
    }
}
