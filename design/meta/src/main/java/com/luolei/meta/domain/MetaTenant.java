package com.luolei.meta.domain;

import lombok.Data;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:06
 */
@Data
public class MetaTenant extends AuditDomain {

    private Long id;

    private String name;
}
