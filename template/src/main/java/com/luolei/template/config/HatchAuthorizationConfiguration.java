package com.luolei.template.config;

import com.luolei.template.security.entitlements.HatchEntitlementProvider;
import com.luolei.template.security.entitlements.custommethodsecurityexpression.HatchEntitlementMethodSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;

/**
 * 基于权限的安全配置
 *
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:43
 */
@Configuration
public class HatchAuthorizationConfiguration {

    @Autowired
    private HatchEntitlementProvider hatchEntitlementProvider;

    @Bean
    public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(){
        return new HatchEntitlementMethodSecurityExpressionHandler(hatchEntitlementProvider);
    }
}
