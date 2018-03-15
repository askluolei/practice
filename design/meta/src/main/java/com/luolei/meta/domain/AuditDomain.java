package com.luolei.meta.domain;

import lombok.Data;

import java.time.Instant;

/**
 * 审计字段
 * @author 罗雷
 * @date 2018/3/14 0014
 * @time 11:24
 */
@Data
public class AuditDomain {
    private String createBy;
    private Instant createTime;
    private String lastModifiedBy;
    private Instant lastModifiedTime;
}
