package com.luolei.template.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author 罗雷
 * @date 2018/1/4 0004
 * @time 20:04
 */
@Getter
@Setter
@ToString
public class AccessPermissionVM {

    private Long id;

    @NotNull
    private String roleName;

    @NotNull
    private String protectedResource;

    @NotNull
    private String hatchPermission;
}
