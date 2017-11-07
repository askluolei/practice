package com.luolei.framework.jpa.model;

import com.luolei.framework.jpa.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 15:30
 */
@Getter
@Setter
@Entity
@Table(name = "t_person_detail")
public class PersonDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Integer age;
    private String phone;
}
