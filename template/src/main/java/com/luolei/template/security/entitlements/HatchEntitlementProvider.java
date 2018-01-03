package com.luolei.template.security.entitlements;

import java.util.List;

public interface HatchEntitlementProvider {

    List<String> getAllowedRoles(String accessedEntityClass, HatchPermission entityPermission);

    List<HatchEntitlement> getForRole(String role);
}
