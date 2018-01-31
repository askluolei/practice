package com.luolei.template.web.rest;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.service.AccessPermissionService;
import com.luolei.template.web.rest.util.HeaderUtil;
import com.luolei.template.web.rest.util.PaginationUtil;
import com.luolei.template.web.rest.vm.AccessPermissionVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限的 Rest Controller
 *
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 19:58
 */
@RestController
@RequestMapping("/api")
public class AccessPermissionResources {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AccessPermissionService accessPermissionService;

    public AccessPermissionResources(AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }

    /**
     * add access permission
     * @param accessPermissionVM
     * @return
     */
//    @PostMapping("/access-permission")
    public ResponseEntity<AccessPermission> createPermission(@RequestBody AccessPermissionVM accessPermissionVM) {
        log.debug("Request to create a permission");
        return ResponseEntity.ok(accessPermissionService.createPermission(accessPermissionVM));
    }

    /**
     * update a access permission. if not exist, it will create
     * @param accessPermissionVM
     * @return
     */
//    @PutMapping("/access-permission")
    public ResponseEntity<AccessPermission> updatePermission(@RequestBody AccessPermissionVM accessPermissionVM) {
        log.debug("Request to update a permission");
        return ResponseEntity.ok(accessPermissionService.updatePermission(accessPermissionVM));
    }

    /**
     * get all access permission with page
     * @param pageable
     * @return
     */
    @GetMapping("/access-permission")
    public ResponseEntity<List<AccessPermission>> getAllPermission(Pageable pageable) {
        log.debug("Request all permission by page");
        final Page<AccessPermission> page = accessPermissionService.findPermissions(pageable);
        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "/api/access-permission");
        return new ResponseEntity<>(page.getContent(), httpHeaders, HttpStatus.OK);
    }

    /**
     * get access permission by id
     * @param id
     * @return
     */
    @GetMapping("/access-permission/{id}")
    public ResponseEntity<AccessPermission> getPermission(@PathVariable Long id) {
        log.debug("Request to get a permission by id:{}", id);
        return ResponseUtil.wrapOrNotFound(accessPermissionService.getPermission(id));
    }

    /**
     * delete access permission by id
     * @param id
     * @return
     */
//    @DeleteMapping("/access-permission/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        log.debug("request to delete a permission by id:{}", id);
        accessPermissionService.deletePermission(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "AccessPermission.deleted", String.valueOf(id))).build();
    }
}
