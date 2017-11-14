package com.luolei.metadata.tables;

import lombok.Getter;
import lombok.Setter;

/**
 * 跟 Index 一样 ，不过这里是唯一索引
 *
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 20:01
 */
@Getter
@Setter
public class MetaUniqueIndex {

    /**
     * 对象id
     */
    private Long objId;

    /**
     * 租户id
     */
    private Long orgId;

    /**
     * 字段在哪列
     */
    private Integer fieldNum;

    /**
     * 数据id
     */
    private Long guid;

    /**
     * 下面是各种实际类型的 value
     * 唯一索引 一般不可能是复杂对象
     */

    private String stringValue;

    private Integer integerValue;

    private Long longValue;

}
