package com.luolei.meta.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 对象
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 13:35
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "fields"})
@ToString(exclude = {"tenant", "fields"})
@Entity
@Table(name = "t_meta_object")
public class MetaObject implements Serializable {

    /**
     * 对象id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "object_id")
    private Long objId;

    /**
     * 租户id
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * 对象名
     */
    @Column(name = "object_name", nullable = false)
    private String objectName;

    /**
     * 对象自然名
     */
    @Column(name = "object_natural_name")
    private String objectNaturalName;

    /**
     * 一对多关联，一个对象有很多字段，这里不使用懒加载
     */
    @OneToMany(mappedBy = "obj", fetch = FetchType.EAGER)
    private Set<MetaField> fields;
}
