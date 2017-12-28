package com.luolei.meta.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 字段
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 13:36
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "obj"})
@ToString(exclude = {"tenant", "obj"})
@Entity
@Table(name = "t_meta_field")
public class MetaField implements Serializable {

    /**
     * 字段id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long fieldId;

    /**
     * 租户id
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * 对象id
     */
    @ManyToOne
    @JoinColumn(name = "object_id", nullable = false)
    private MetaObject obj;

    /**
     * 字段名
     */
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    /**
     * 字段自然名
     */
    @Column(name = "field_natural_name")
    private String fieldNaturalName;

    /**
     * 字段数据类型
     * 应该是一个java 类型
     */
    @Column(name = "data_type", nullable = false)
    private String dataType;

    /**
     * 字段号
     * 一行记录现在会插入到 Data 表，里面有 0 - 500 个可用字段，这里标记字段被存在哪个字段里面
     */
    @Column(name = "field_num", nullable = false)
    private Integer fieldNum;

    /**
     * 是否需要被索引
     */
    @Column(name = "is_indexed")
    private Boolean isIndexed;

    /**
     * 是否是主键
     */
    @Column(name = "is_primary")
    private Boolean isPrimary;

    /**
     * 是否唯一
     */
    @Column(name = "is_unique")
    private Boolean isUnique;

    /**
     * 是否可空
     */
    @Column(name = "is_nullable")
    private Boolean isNullable;

    /**
     * 是否是被关联字段
     */
    @Column(name = "is_relation_ship")
    private Boolean isRelationShip;
}
