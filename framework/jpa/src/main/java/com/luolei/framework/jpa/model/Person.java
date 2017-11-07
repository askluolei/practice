package com.luolei.framework.jpa.model;

import com.luolei.framework.jpa.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 15:30
 */
@Getter
@Setter
@Entity
@Table(name = "t_person")
public class Person extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "detail_id", foreignKey = @ForeignKey(name = "FK_PERSON_DETAIL"))
    private PersonDetail detail;
}
