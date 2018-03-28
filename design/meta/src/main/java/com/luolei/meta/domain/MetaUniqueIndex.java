package com.luolei.meta.domain;

import lombok.Data;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 15:02
 */
@Data
public class MetaUniqueIndex extends AuditDomain {

    private Long id;

    /**
     * 对象id
     */
    private Long objectId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 字段在哪列
     */
    private Integer fieldNum;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 下面是各种实际类型的 value
     * 唯一索引 一般不可能是复杂对象
     */

    private String stringValue;

    private Integer integerValue;

    private Long longValue;
}
