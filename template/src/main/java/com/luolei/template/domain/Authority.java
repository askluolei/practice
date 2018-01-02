package com.luolei.template.domain;

import lombok.EqualsAndHashCode;
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
@Table(name = "_authority")
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Authority implements Serializable  {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;
}
