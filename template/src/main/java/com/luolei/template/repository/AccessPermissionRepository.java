package com.luolei.template.repository;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.security.entitlements.HatchPermission;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:04
 */
public interface AccessPermissionRepository extends BaseRepository<AccessPermission, Long> {

    List<AccessPermission> findByRoleNameAndProtectedResource(String roleName, String protectedResource);

    List<AccessPermission> findByHatchPermissionAndProtectedResource(HatchPermission hatchPermission, String protectedResource);

    List<AccessPermission> findByRoleName(String roleName);
}
