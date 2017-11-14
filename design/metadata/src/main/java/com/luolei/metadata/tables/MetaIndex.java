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
