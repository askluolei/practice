package com.luolei.template.service;

import com.luolei.template.TemplateApp;
import com.luolei.template.domain.AccessPermission;
import com.luolei.template.security.entitlements.HatchPermission;
import com.luolei.template.web.rest.vm.AccessPermissionVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/1/8 0008
 * @time 9:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class AccessPermissionServiceTest {
    
    private static final Logger log = LoggerFactory.getLogger(AccessPermissionServiceTest.class);

    @Autowired
    private AccessPermissionService accessPermissionService;

    private AccessPermission accessPermission;

    private AccessPermissionVM accessPermissionVM;

    @Before
    public void init() {
        accessPermission = new AccessPermission();
        accessPermission.setRoleName("ROLE_USER");
        accessPermission.setProtectedResource("unit_test");
        accessPermission.setHatchPermission(HatchPermission.CREATE);

        accessPermissionVM = new AccessPermissionVM();
        accessPermissionVM.setRoleName("ROLE_USER");
        accessPermissionVM.setProtectedResource("unit_test");
        accessPermissionVM.setHatchPermission(HatchPermission.READ.name());
    }

    @Test
    public void createPermission() throws Exception {
        AccessPermission result = accessPermissionService.createPermission(accessPermissionVM);
        assertThat(result).isNotNull();
        assertThat(result.getRoleName()).isEqualTo("ROLE_USER");
        assertThat(result.getProtectedResource()).isEqualTo("unit_test");
        assertThat(result.getHatchPermission()).isEqualTo(HatchPermission.READ);
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void updatePermission() throws Exception {
        Optional<AccessPermission> optional = accessPermissionService.getPermission(1L);
        assertThat(optional.isPresent()).isTrue();
        AccessPermission accessPermission = optional.get();
        accessPermission.setRoleName("ROLE_TEST");
        accessPermission.setProtectedResource("my_protect");

        AccessPermissionVM accessPermissionVM = new AccessPermissionVM();
        accessPermissionVM.setId(accessPermission.getId());
        accessPermissionVM.setRoleName("ROLE_TEST");
        accessPermissionVM.setProtectedResource("my_protect");
        accessPermissionVM.setHatchPermission(accessPermission.getHatchPermission().name());
        AccessPermission result = accessPermissionService.updatePermission(accessPermissionVM);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isEqualTo(accessPermission.getId());
        assertThat(result.getRoleName()).isEqualTo("ROLE_TEST");
        assertThat(result.getProtectedResource()).isEqualTo("my_protect");
        assertThat(result.getHatchPermission()).isEqualTo(accessPermission.getHatchPermission());
    }

    @Test
    public void getPermission() throws Exception {
        Optional<AccessPermission> permissionOptional = accessPermissionService.getPermission(1L);
        assertThat(permissionOptional.isPresent()).isTrue();
    }

    @Test
    public void findPermissions() throws Exception {
        Page<AccessPermission> page = accessPermissionService.findPermissions(new PageRequest(0, 10));
        assertThat(page.getContent().size()).isGreaterThan(1);
        assertThat(page.getContent().stream().map(AccessPermission::getRoleName).distinct().collect(Collectors.toList()).size()).isGreaterThan(1);
    }

    @Test
    public void findPermissions1() throws Exception {
        Page<AccessPermission> page = accessPermissionService.findPermissions("ROLE_USER", new PageRequest(0, 10));
        assertThat(page.getContent().size()).isGreaterThan(0);
        assertThat(page.getContent().stream().map(AccessPermission::getRoleName).distinct().collect(Collectors.toList()).size()).isEqualTo(1);
    }

}