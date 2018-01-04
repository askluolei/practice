package com.luolei.template.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 14:45
 */
@RestController
@RequestMapping("/api")
public class PermissionTestController {

    @GetMapping("/user")
    @Secured(AuthoritiesConstants.USER)
    public void user() {
    }

    @GetMapping("/admin")
    @Secured(AuthoritiesConstants.ADMIN)
    public void admin() {
    }

    @GetMapping("/p1")
    @PreAuthorize("isEntitled('TEST', 'READ')")
    public void p1() {
    }

    @GetMapping("/p2")
    @PreAuthorize("isEntitled('TEST', 'DELETE')")
    public void p2() {
    }

    @GetMapping("/p3")
    @PreAuthorize("hasAuthority('TEST:READ')")
    public void p3() {
    }
}
