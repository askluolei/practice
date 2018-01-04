package com.luolei.template.web.rest;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.repository.AccessPermissionRepository;
import com.luolei.template.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 19:58
 */
@RestController
@RequestMapping("/api")
public class AccessPermissionResources {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AccessPermissionRepository accessPermissionRepository;

    private final AuthorityRepository authorityRepository;

    public AccessPermissionResources(AccessPermissionRepository accessPermissionRepository, AuthorityRepository authorityRepository) {
        this.accessPermissionRepository = accessPermissionRepository;
        this.authorityRepository = authorityRepository;
    }

    public ResponseEntity<AccessPermission> createPermission() {
        return null;
    }

    public ResponseEntity<AccessPermission> updatePermission() {
        return null;
    }

    public ResponseEntity<List<AccessPermission>> getAllPermission() {
        return null;
    }

    public ResponseEntity<AccessPermission> getPermission(Long id) {
        return null;
    }

    public ResponseEntity<Void> deletePermission(Long id) {
        return null;
    }
}
