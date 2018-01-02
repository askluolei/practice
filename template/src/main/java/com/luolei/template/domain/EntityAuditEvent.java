package com.luolei.template.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * 实体类审计表
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:38
 */
@Entity
@Table(name = "jhi_entity_audit_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EntityAuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "gid")
    @GenericGenerator(name = "gid", strategy = "com.luolei.template.domain.IDGenerator")
    private Long id;

    @NotNull
    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @NotNull
    @Size(max = 255)
    @Column(name = "entity_type", length = 255, nullable = false)
    private String entityType;

    @NotNull
    @Size(max=20)
    @Column(name = "action", length = 20, nullable = false)
    private String action;

    @Lob
    @Column(name = "entity_value")
    private String entityValue;

    @Column(name = "commit_version")
    private Integer commitVersion;

    @Size(max = 100)
    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @NotNull
    @Column(name = "modified_date", nullable = false)
    private Instant modifiedDate;

}
