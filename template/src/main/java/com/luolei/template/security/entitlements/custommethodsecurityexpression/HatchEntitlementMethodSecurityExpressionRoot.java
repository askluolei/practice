package com.luolei.template.security.entitlements.custommethodsecurityexpression;

import com.luolei.template.security.entitlements.HatchEntitlementProvider;
import com.luolei.template.security.entitlements.HatchPermission;
import org.slf4j.Logger;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HatchEntitlementMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final Logger log = getLogger(HatchEntitlementMethodSecurityExpressionRoot.class);
    private final HatchEntitlementProvider entitlementProvider;
    private Object filterObject;
    private Object returnObject;

    public HatchEntitlementMethodSecurityExpressionRoot(Authentication authentication, HatchEntitlementProvider entitlementProvider) {
        super(authentication);
        this.entitlementProvider = entitlementProvider;
    }

    public boolean isEntitled(String protectedResource, String permission) {
        log.debug("evaluating permissions for user '{}' ({}) on targetObject '{}', {}", authentication.getName(),
            authentication.getClass().getSimpleName(), protectedResource, permission);
        if (!authentication.isAuthenticated() || isAnonymousAuthentication(authentication)) {
            return false;
        }
        HatchPermission hatchPermission = HatchPermission.valueOf(permission.toUpperCase());
        return hasPermissionForObject(authentication, hatchPermission, protectedResource);
    }

    private static boolean isAnonymousAuthentication(Authentication authentication) {
        return (authentication instanceof AnonymousAuthenticationToken);
    }

    private boolean hasPermissionForObject(Authentication authentication, HatchPermission entityPermission, String protectedResource) {
        List<String> allowedRoles = entitlementProvider.getAllowedRoles(protectedResource, entityPermission);
        return hasAccessorOneOfRequiredRoles(authentication, allowedRoles);
    }

    private boolean hasAccessorOneOfRequiredRoles(Authentication authentication, List<String> requiredRoles) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(requiredRoles::contains);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
