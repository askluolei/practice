package com.luolei.template.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 18:02
 */
@Entity
@Table(name = "_oss_image")
@Getter
@Setter
public class OssImage extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上传后 外链链接
     */
    @Column(nullable = false)
    private String link;
}
