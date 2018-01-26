package com.luolei.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 角色名
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:27
 */
@Entity
@Table(name = "_role")
@Setter
@Getter
@ToString
public class Role extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 角色名
   */
  @NotNull
  @Size(max = 50)
  @Column(length = 50)
  private String name;

  /**
   * 角色介绍
   */
  @Column(length = 128)
  @Size(max = 128)
  private String introduce;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Role role = (Role) o;

    return !(name != null ? !name.equals(role.name) : role.name != null);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
