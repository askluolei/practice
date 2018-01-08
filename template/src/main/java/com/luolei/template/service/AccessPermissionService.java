package com.luolei.template.service;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.repository.AccessPermissionRepository;
import com.luolei.template.repository.AuthorityRepository;
import com.luolei.template.security.entitlements.HatchPermission;
import com.luolei.template.web.rest.errors.BizError;
import com.luolei.template.web.rest.vm.AccessPermissionVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 罗雷
 * @date 2018/1/5 0005
 * @time 9:46
 */
@Service
public class AccessPermissionService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AccessPermissionRepository accessPermissionRepository;
    private final AuthorityRepository authorityRepository;

    public AccessPermissionService(AccessPermissionRepository accessPermissionRepository, AuthorityRepository authorityRepository) {
        this.accessPermissionRepository = accessPermissionRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * 新增权限，如果已经存在则直接返回
     * @param accessPermissionVM
     * @return
     */
    public AccessPermission createPermission(AccessPermissionVM accessPermissionVM) {
        log.debug("create permission : {}", accessPermissionVM.toString());
        if (Objects.isNull(authorityRepository.findByName(accessPermissionVM.getRoleName()))) {
            throw BizError.RESOURCE_NOT_EXIST.exception("role not exist:" + accessPermissionVM.getRoleName());
        }
        return accessPermissionRepository
                .findByRoleNameAndProtectedResourceAndHatchPermission(accessPermissionVM.getRoleName(),
                        accessPermissionVM.getProtectedResource(),
                        HatchPermission.valueOf(accessPermissionVM.getHatchPermission()))
                .orElseGet(() -> {
                    AccessPermission accessPermission = new AccessPermission();
                    accessPermission.setRoleName(accessPermissionVM.getRoleName());
                    accessPermission.setProtectedResource(accessPermissionVM.getProtectedResource());
                    accessPermission.setHatchPermission(HatchPermission.valueOf(accessPermissionVM.getHatchPermission()));
                    return accessPermissionRepository.save(accessPermission);
                });
    }

    /**
     * 根据主键删除
     * @param id
     */
    public void deletePermission(Long id) {
        log.debug("delete by id : {}", id);
        accessPermissionRepository.delete(id);
    }

    /**
     * 更新权限
     * 如果Id 不存在 或者 根据id查询不到那就新增
     * @param accessPermissionVM
     * @return
     */
    public AccessPermission updatePermission(AccessPermissionVM accessPermissionVM) {
        log.debug("update or create : {}", accessPermissionVM.toString());
        if (Objects.isNull(accessPermissionVM.getId())) {
            return createPermission(accessPermissionVM);
        } else {
            AccessPermission accessPermission = accessPermissionRepository.findOne(accessPermissionVM.getId());
            if (Objects.isNull(accessPermission)) {
                return createPermission(accessPermissionVM);
            } else {
                accessPermission.setRoleName(accessPermissionVM.getRoleName());
                accessPermission.setProtectedResource(accessPermissionVM.getProtectedResource());
                accessPermission.setHatchPermission(HatchPermission.valueOf(accessPermissionVM.getHatchPermission()));
                return accessPermissionRepository.save(accessPermission);
            }
        }
    }

    /**
     * 根据主键获取权限信息
     * @param id
     * @return
     */
    public Optional<AccessPermission> getPermission(Long id) {
        log.debug("get by id : {}", id);
        return Optional.ofNullable(accessPermissionRepository.findOne(id));
    }

    /**
     * 分页全部查询
     * @param pageable
     * @return
     */
    public Page<AccessPermission> findPermissions(Pageable pageable) {
        log.debug("page query : {}", pageable);
        return accessPermissionRepository.findAll(pageable);
    }

    /**
     * 根据角色分页
     * @param roleName
     * @param pageable
     * @return
     */
    public Page<AccessPermission> findPermissions(String roleName, Pageable pageable) {
        log.debug("query for roleName:{}, page:{}", roleName, pageable);
        return accessPermissionRepository.findByRoleName(roleName, pageable);
    }
}
