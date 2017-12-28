package com.luolei.meta.domain;

/**
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 14:17
 */

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * 大文本存储
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 13:37
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "object"})
@Entity
@Table(name = "t_meta_clob_data10")
/**
 * 这里的字段太多，而且大多是空字段，因此需要动态插入,动态更新（不管为null的字段）
 * 动态更新的意思是，只更新改变的字段值，
 * 注意，不是更新非null字段
 */
@DynamicInsert
@DynamicUpdate
public class MetaClobData10 {

    /**
     * 全局唯一的id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guid")
    private Long guid;

    /**
     * 租户id
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * 对象id
     */
    @ManyToOne
    @JoinColumn(name = "object_id", nullable = false)
    private MetaObject object;

    /**
     * 对象名
     */
    @Column(name = "object_name")
    private String name;

    public void setValue(int num, String value) {
        switch(num) {
            case 0 : setValue0(value);break;
            case 1 : setValue1(value);break;
            case 2 : setValue2(value);break;
            case 3 : setValue3(value);break;
            case 4 : setValue4(value);break;
            case 5 : setValue5(value);break;
            case 6 : setValue6(value);break;
            case 7 : setValue7(value);break;
            case 8 : setValue8(value);break;
            case 9 : setValue9(value);break;
            case 10 : setValue10(value);break;
            default: throw new IllegalArgumentException("invalid index num:" + num);
        }
    }

    public String getValue(int num) {
        switch(num) {
            case 0 : return getValue0();
            case 1 : return getValue1();
            case 2 : return getValue2();
            case 3 : return getValue3();
            case 4 : return getValue4();
            case 5 : return getValue5();
            case 6 : return getValue6();
            case 7 : return getValue7();
            case 8 : return getValue8();
            case 9 : return getValue9();
            case 10 : return getValue10();
            default: throw new IllegalArgumentException("invalid index num:" + num);
        }
    }

    @Lob
    @Type(type = "text")
    private String value0;

    @Lob
    @Type(type = "text")
    private String value1;

    @Lob
    @Type(type = "text")
    private String value2;

    @Lob
    @Type(type = "text")
    private String value3;

    @Lob
    @Type(type = "text")
    private String value4;

    @Lob
    @Type(type = "text")
    private String value5;

    @Lob
    @Type(type = "text")
    private String value6;

    @Lob
    @Type(type = "text")
    private String value7;

    @Lob
    @Type(type = "text")
    private String value8;

    @Lob
    @Type(type = "text")
    private String value9;

    @Lob
    @Type(type = "text")
    private String value10;
}
