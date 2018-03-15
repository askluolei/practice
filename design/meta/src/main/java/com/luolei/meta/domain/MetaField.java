package com.luolei.meta.domain;

import lombok.Data;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:08
 */
@Data
public class MetaField extends AuditDomain {

    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 对象id
     */
    private Long objectId;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段自然名
     */
    private String fieldNaturalName;

    /**
     * 字段数据类型
     * 应该是一个java 类型
     */
    private String dataType;

    /**
     * 字段号
     * 一行记录现在会插入到 Data 表，里面有 0 - 500 个可用字段，这里标记字段被存在哪个字段里面
     */
    private Integer fieldNum;

    /**
     * 是否需要被索引
     */
    private Boolean isIndexed;

    /**
     * 是否是主键
     */
    private Boolean isPrimary;

    /**
     * 是否唯一
     */
    private Boolean isUnique;

    /**
     * 是否可空
     */
    private Boolean isNullable;

    /**
     * 是否是被关联字段
     */
    private Boolean isRelationShip;
}
