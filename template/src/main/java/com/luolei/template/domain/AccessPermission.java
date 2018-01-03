package com.luolei.template.domain;

import com.luolei.template.security.entitlements.HatchPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 操作权限
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:01
 */
@Entity
@Table(name = "_access_permission")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AccessPermission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "role_name", nullable = false, length = 64)
    private String roleName;

    @Column(name = "protected_resource", nullable = false, length = 64)
    private String protectedResource;

    @Column(name = "hatch_permission", nullable = false)
    @Enumerated(EnumType.STRING)
    private HatchPermission hatchPermission;
}
