package com.luolei.metadata.tables;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储对象信息
 *
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 16:01
 */
@Getter
@Setter
public class MetaObject {

    /**
     * 对象id
     */
    private Long objId;

    /**
     * 租户id
     */
    private Long orgId;

    /**
     * 对象名
     */
    private String objName;
}
