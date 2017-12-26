package com.luolei.metadata.tables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * 存储对象信息
 *
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 16:01
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "fields"})
@ToString(exclude = {"tenant", "fields"})
@Entity
@Table(name = "t_meta_object")
public class MetaObject {

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
    private MetaTenant tenant;

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
