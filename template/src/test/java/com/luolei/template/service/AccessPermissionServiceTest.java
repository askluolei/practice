package com.luolei.template.service;

import com.luolei.template.TemplateApp;
import com.luolei.template.domain.AccessPermission;
import com.luolei.template.security.entitlements.HatchPermission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

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

    public void init() {
        accessPermission = new AccessPermission();
        accessPermission.setRoleName("ROLE_USER");
        accessPermission.setProtectedResource("unit_test");
        accessPermission.setHatchPermission(HatchPermission.CREATE);
    }

    @Test
    public void createPermission() throws Exception {
    }

    @Test
    public void deletePermission() throws Exception {
    }

    @Test
    public void updatePermission() throws Exception {
    }

    @Test
    public void getPermission() throws Exception {
    }

    @Test
    public void findPermissions() throws Exception {
    }

    @Test
    public void findPermissions1() throws Exception {
    }

}