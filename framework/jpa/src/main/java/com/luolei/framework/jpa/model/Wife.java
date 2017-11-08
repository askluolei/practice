package com.luolei.framework.jpa.model;

import com.luolei.framework.jpa.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

    private String name;

    @OneToOne(mappedBy = "wife", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Husband husband;
}
