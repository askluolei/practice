package com.luolei.metadata.tables;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储字段信息
 *
 * @author 罗雷
 * @date 2017/11/9 0009
 * @time 16:03
 */
@Getter
@Setter
public class MetaField {

    /**
     * 字段id
     */
    private Long fieldId;

    /**
     * 租户id
     */
    private Long orgId;

    /**
     * 对象id
     */
    private Long objId;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段数据类型
     * 应该是一个java 类型
     */
    private String dataType;

    /**
     * 是否需要被索引
     */
    private Boolean isIndexed;

    /**
     * 字段号
     * 猜测，因为一行记录现在会插入到 Data 表，里面有 0 - 500 个可用字段，这里标记字段被存在哪个字段里面
     */
    private Integer fieldNum;

}
