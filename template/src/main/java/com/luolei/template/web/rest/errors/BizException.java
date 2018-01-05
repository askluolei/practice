package com.luolei.template.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;

/**
 * @author 罗雷
 * @date 2018/1/5 0005
 * @time 10:15
 */
public class BizException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final BizError bizError;

    private String data;

    public BizException(BizError bizError) {
        this.bizError = bizError;
    }

    public BizException(BizError bizError, String data) {
        this.bizError = bizError;
        this.data = data;
    }

    public BizError getBizError() {
        return bizError;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
