package com.luolei.template.social.qq;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.web.client.RestTemplate;

/**
 * @author 罗雷
 * @date 2018/1/30 0030
 * @time 19:53
 */
public class AbstractQQApiOperations {

    protected final RestTemplate restTemplate;
    protected final boolean isAuthorized;

    protected AbstractQQApiOperations(RestTemplate restTemplate, boolean isAuthorized) {
        this.restTemplate = restTemplate;
        this.isAuthorized = isAuthorized;
    }

    protected void requireAuthorization() {
        if (!isAuthorized) {
            throw new MissingAuthorizationException("qq");
        }
    }

    protected <T> T getEntity(String url, Class<T> type) {
        return restTemplate.getForObject(url, type);
    }

    protected <T> T saveEntity(String url, T entity) {
        return (T) restTemplate.postForObject(url, entity, entity.getClass());
    }
}
