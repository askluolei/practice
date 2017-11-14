package com.luolei.framework.jpa.model;

import com.luolei.framework.jpa.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author 罗雷
 * @date 2017/11/8 0008
 * @time 19:19
 */
@Entity
@Table(name = "t_wife")
@Getter
@Setter
public class Wife extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column
    private String name;

    @OneToOne(mappedBy = "wife", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Husband husband;

    public void setHusband(Husband husband) {
        husband.setWife(this);
        this.husband = husband;
    }

}
