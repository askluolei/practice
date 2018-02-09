package com.luolei.template.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 16:20
 */
@Entity
@Table(name = "_saved_sql")
@Getter
@Setter
public class SavedSql extends AbstractAuditingEntity {

    @Column(length = 50)
    private String title;

    /**
     * 执行的sql
     */
    @Column(nullable = false)
    private String sql;

    /**
     * 原始 sql
     */
    @Column
    private String originSql;

    @Column
    private String explanation;

    @Column(length = 16)
    private String type;

    /**
     * 是否校验过（执行无异常）
     */
    @Column
    private Boolean validate;
}
