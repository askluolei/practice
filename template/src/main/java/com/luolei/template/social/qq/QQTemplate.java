package com.luolei.template.social.qq;

import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

/**
 * @author 罗雷
 * @date 2018/1/30 0030
 * @time 19:45
 */
public class QQTemplate extends AbstractOAuth2ApiBinding implements QQ {

    private String accessToken;
    private UserOperations userOperations;

    public QQTemplate() {
        initialize();
    }

    private void initialize() {
        userOperations = new UserTemplate(getRestTemplate(), isAuthorized());
    }

    public QQTemplate(String accessToken) {
        super(accessToken);
        this.accessToken = accessToken;
        initialize();
    }

    @Override
    public UserOperations getUserOperations() {
        return userOperations;
    }

    @Override
    public String getAccessToken() {
        return null;
    }
}
