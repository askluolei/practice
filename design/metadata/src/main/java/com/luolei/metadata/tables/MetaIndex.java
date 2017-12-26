package com.luolei.metadata.tables;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 索引表
 * 很明显，data 表 虽然存储数据，但是没办法在其中加索引了，
 * 为了加快查询，新增索引表
 *
 * 系统在运行的时候 会从 data 表复制需要索引的数据到这个表
 *
 *  这个表的主键 和 索引如何加呢？
 *  objId, orgId, fieldNum, guid 4个作为联合主键
 *
 *  那一个表如果有很多索引，岂不是这个表很庞大
 *
 *
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 19:50
 */
@Getter
@Setter
public class MetaIndex {

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
     */

    private String stringValue;

    private Integer integerValue;

    private Long longValue;

    private Date dateValue;

    private BigDecimal decimalValue;

    private Boolean booleanValue;

    private LocalDate localDateValue;

    private LocalTime localTimeValue;

    private LocalDateTime localDateTimeValue;

}
