package com.luolei.template.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 通过页面执行的sql记录
 *
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 16:16
 */
@Entity
@Table(name = "_execute_sql")
@Getter
@Setter
public class ExecuteSql extends AbstractAuditingEntity {

    @Column(length = 50)
    private String title;

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
}
