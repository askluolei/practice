package com.luolei.metadata.tables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 罗雷
 * @date 2017/12/26 0026
 * @time 16:16
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "t_meta_tenant")
public class MetaTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private String name;
}
