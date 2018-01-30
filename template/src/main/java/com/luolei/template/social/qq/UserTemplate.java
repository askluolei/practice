package com.luolei.template.social.qq;

import org.springframework.web.client.RestTemplate;

/**
 * @author 罗雷
 * @date 2018/1/30 0030
 * @time 19:57
 */
public class UserTemplate extends AbstractQQApiOperations implements UserOperations {

    protected UserTemplate(RestTemplate restTemplate, boolean isAuthorized) {
        super(restTemplate, isAuthorized);
    }

    @Override
    public QQUserInfo getUserInfo() {
        return null;
    }
}
