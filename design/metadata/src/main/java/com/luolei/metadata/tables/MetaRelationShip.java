package com.luolei.metadata.tables;

/**
 * 关联关系表
 *
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 20:03
 */
public class MetaRelationShip {

    /**
     * 对象id
     */
    private Long objId;

    /**
     * 租户id
     */
    private Long orgId;

    /**
     * 数据id
     */
    private Long guid;

    /**
     * 这个是什么还得思考一下
     */
    private Long relationId;

    /**
     * 目标对象id
     */
    private Long targetObjId;
}
