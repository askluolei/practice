package com.luolei.meta.domain;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:07
 */
public class MetaObject extends AuditDomain {

    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 对象名
     */
    private String objectName;

    /**
     * 对象自然名
     */
    private String objectNaturalName;
}
