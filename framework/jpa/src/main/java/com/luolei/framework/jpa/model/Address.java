package com.luolei.framework.jpa.model;

import com.luolei.framework.jpa.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 14:05
 */
@Entity
@Table(name = "t_address")
@Getter
@Setter
public class Address extends BaseEntity {

    @Column(name = "t_province")
    private String province;

    @Column(name = "t_city")
    private String city;

    @Column(name = "t_street_code")
    private Integer streetCode;
}
