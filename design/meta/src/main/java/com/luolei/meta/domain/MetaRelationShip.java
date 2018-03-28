package com.luolei.meta.domain;

import lombok.Data;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 15:04
 */
@Data
public class MetaRelationShip extends AuditDomain {

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
     * 数据id
     */
    private Long dataId;

    /**
     * 这个是什么还得思考一下
     */
    private RelationType relationType;

    /**
     * 目标对象id
     */
    private Long targetObjId;
}
