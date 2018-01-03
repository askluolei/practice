package com.luolei.template.security.entitlements.custommethodsecurityexpression;

import com.luolei.template.security.entitlements.HatchEntitlementProvider;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class HatchEntitlementMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private final HatchEntitlementProvider entitlementProvider;

    public HatchEntitlementMethodSecurityExpressionHandler(HatchEntitlementProvider entitlementProvider) {
        this.entitlementProvider = entitlementProvider;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
        Authentication authentication, MethodInvocation invocation) {
        HatchEntitlementMethodSecurityExpressionRoot root =
            new HatchEntitlementMethodSecurityExpressionRoot(authentication, entitlementProvider);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
