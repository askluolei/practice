package com.luolei.template.web.rest.vm;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 20:04
 */
@Getter
@Setter
public class AccessPermissionVM {

    private Long id;
    private String roleName;
    private String protectedResource;
    private String hatchPermission;
}
