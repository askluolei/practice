package com.luolei.template.web.rest.errors;

/**
 * 业务异常错误码
 *
 * @author 罗雷
 * @date 2018/1/5 0005
 * @time 10:03
 */
public enum BizError {

    RESOURCE_EXIST("resource_exist", "资源已经存在"),
    RESOURCE_NOT_EXIST("resource_not_exist", "资源不存在")

    ;
    private String code;
    private String msg;

    BizError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException exception() {
        return new BizException(this);
    }

    public BizException exception(String data) {
        return new BizException(this, data);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
