package com.luolei.template.web.rest.errors;

import lombok.Getter;
import lombok.Setter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author 罗雷
 * @date 2018/1/5 0005
 * @time 10:15
 */
@Getter
@Setter
public class BizException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private static final String title = "known exception";
    private static final String detail = "已知的业务异常";


    private final BizError bizError;
    private Throwable throwable;
    private String message;


    public BizException(BizError bizError, Throwable throwable, String message) {
        super(ErrorConstants.BIZ_EXPECTED_EXCEPTION_TYPE, title, Status.OK, detail);
        this.bizError = bizError;
        this.throwable = throwable;
        this.message = message;
    }

    public BizException(BizError bizError, Throwable throwable) {
        this(bizError, throwable, null);
    }

    public BizException(BizError bizError, String message) {
        this(bizError, null, message);
    }

    public BizException(BizError bizError) {
        this(bizError, null, null);
    }

}
