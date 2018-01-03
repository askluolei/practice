package com.luolei.template.security.entitlements;

/**
 * Created by cra on 11.07.2017.
 */
public class HatchEntitlement {

    private final String roleName;
    private final String protectedResource;
    private final HatchPermission entityPermission;

    public HatchEntitlement(String roleName, String protectedResource, HatchPermission entityPermission) {

        this.roleName = roleName;
        this.protectedResource = protectedResource;
        this.entityPermission = entityPermission;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getProtectedResource() {
        return protectedResource;
    }

    public HatchPermission getEntityPermission() {
        return entityPermission;
    }
}
