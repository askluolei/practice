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
@Table(name = "t_husband")
@Getter
@Setter
public class Husband extends BaseEntity {

    @Column(name = "t_name")
    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "wife_id", foreignKey = @ForeignKey(name = "FK_HUS_WIFE"))
    private Wife wife;
}
