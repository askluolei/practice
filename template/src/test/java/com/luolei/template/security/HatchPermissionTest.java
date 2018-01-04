package com.luolei.template.security;

import com.luolei.template.TemplateApp;
import com.luolei.template.repository.AccessPermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 权限测试
 *
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 14:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class HatchPermissionTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccessPermissionRepository accessPermissionRepository;

    private MockMvc restMvc;

    @Before
    public void setUp() {
        this.restMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testRoleUserFail() throws Exception {
        this.restMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testRoleUser() throws Exception {
        this.restMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoleAdminFail() throws Exception {
        this.restMvc.perform(get("/api/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testRoleAdminFail2() throws Exception {
        this.restMvc.perform(get("/api/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testRoleAdmin() throws Exception {
        this.restMvc.perform(get("/api/admin"))
                .andExpect(status().isOk());
    }

    @Test
    public void testP1Fail() throws Exception {
        this.restMvc.perform(get("/api/p1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testP1SuccessWithAdmin() throws Exception {
        this.restMvc.perform(get("/api/p1"))
                .andExpect(status().isOk());
    }

    /**
     * spring-security 是根据 authority 来划分权限的
     * 其实可以可以区分role 和 permission 虽然比对是看 authority 字符串
     * 但是设置role 是有 ROLE_ 前缀的
     * 所以，当实现  接口的时候 将角色和权限都设置进去就可以用 hasRole  实现基于权限的控制了
     *
     * 这里是进行了扩展 定义了isEntitled 方法 第一个参数是要 保护的资源  第二个参数是权限 HatchPermission 枚举
     * 有一个 AccessPermission 表 将角色关联对应的权限也可以实现基于权限的控制
     *
     * hasRole 是看 authority 里面有没有 ROLE_参数 的字符串
     * hasAuthority 是看 authority 里面有没有 参数 的字符串
     *
     * 自定义方法 isEntitled 有两个参数 第一个是要保护的资源（字符串） 第二个是权限 READ,DELETE 等 根据当前登录用户的角色判断是否有该权限
     * 目前自定义的跟 hasAuthority 融合 可以随便用哪个都行 譬如 资源 TEST 权限 READ  可以使用 hasAuthority('TEST:READ') 或者 isEntitled('TEST', 'READ') 这两个等效
     * 但是注意 融合的时候使用了 角色默认的 ROLE_ 开头  还有分隔符为 :
     *
     * 为了测试 在权限表里面加了一些数据
     * 看config/permission.csv 里面
     */
    @Test
    @WithMockUser
    public void testP1Success() throws Exception {
        this.restMvc.perform(get("/api/p1"))
                .andExpect(status().isOk());
    }

    /**
     * 一个尴尬的问题是 从 userDetailsService 获取数据 在 before 前面，所以新增的权限在这里看不到~
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "user",  userDetailsServiceBeanName = "userDetailsService")
    public void testP3Success() throws Exception {
        this.restMvc.perform(get("/api/p3"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testP2Success() throws Exception {
        this.restMvc.perform(get("/api/p2"))
                .andExpect(status().isOk());
    }

}
