package com.luolei.template.repository;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.security.entitlements.HatchPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:04
 */
public interface AccessPermissionRepository extends BaseRepository<AccessPermission, Long> {

    List<AccessPermission> findByHatchPermissionAndProtectedResource(HatchPermission hatchPermission, String protectedResource);

    List<AccessPermission> findByRoleName(String roleName);

    Page<AccessPermission> findByRoleName(String roleName, Pageable pageable);

    Optional<AccessPermission> findByRoleNameAndProtectedResourceAndHatchPermission(String roleName, String protectedResource, HatchPermission hatchPermission);
}
