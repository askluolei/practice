package com.luolei.template.social.qq;

import org.springframework.social.ApiBinding;

/**
 * @author 罗雷
 * @date 2018/1/30 0030
 * @time 19:44
 */
public interface QQ extends ApiBinding {

    UserOperations getUserOperations();

    String getAccessToken();
}
