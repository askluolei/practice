package com.luolei.meta.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 租户
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 13:35
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "t_tenant")
public class Tenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private String name;
}
