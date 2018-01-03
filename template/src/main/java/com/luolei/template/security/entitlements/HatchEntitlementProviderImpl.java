package com.luolei.template.security.entitlements;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.repository.AccessPermissionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限提供者
 *
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:13
 */
@Component
public class HatchEntitlementProviderImpl implements HatchEntitlementProvider {

    private final AccessPermissionRepository accessPermissionRepository;

    public HatchEntitlementProviderImpl(AccessPermissionRepository accessPermissionRepository) {
        this.accessPermissionRepository = accessPermissionRepository;
    }

    @Override
    public List<String> getAllowedRoles(String accessedEntityClass, HatchPermission entityPermission) {
        return accessPermissionRepository.findByHatchPermissionAndProtectedResource(entityPermission, accessedEntityClass)
                .stream().map(AccessPermission::getRoleName).distinct().collect(Collectors.toList());
    }

    @Override
    public List<HatchEntitlement> getForRole(String role) {
        return accessPermissionRepository.findByRoleName(role).stream()
                .map(accessPermission -> new HatchEntitlement(accessPermission.getRoleName(), accessPermission.getProtectedResource(), accessPermission.getHatchPermission())).collect(Collectors.toList());
    }
}
