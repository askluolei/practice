package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luolei.template.config.audit.EntityAuditEventListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * 需要审计的实体类继承这个
 * 主键也在这里定义了
 * 如果不想要，就全部重新自定义
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 13:36
 */
@MappedSuperclass
@Audited
@EntityListeners({AuditingEntityListener.class, EntityAuditEventListener.class})
@Getter
@Setter
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 这里是主键
     * 默认所有表的主键都是 Long 类型的
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "gid")
    @GenericGenerator(name = "gid", strategy = "com.luolei.template.domain.IDGenerator")
    private Long id;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractAuditingEntity auditingEntity = (AbstractAuditingEntity) obj;
        if(auditingEntity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), auditingEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
