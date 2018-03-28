package com.luolei.meta.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 14:58
 */
@Data
public class MetaIndex extends AuditDomain {

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
