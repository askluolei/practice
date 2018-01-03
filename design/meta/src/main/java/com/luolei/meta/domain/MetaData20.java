package com.luolei.meta.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 数据表
 * 字段数 <= 20 的对象存储的地方
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 13:37
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "object"})
@ToString(exclude = {"tenant", "object"})
@Entity
@Table(name = "t_meta_data20")
/**
 * 这里的字段太多，而且大多是空字段，因此需要动态插入,动态更新（不管为null的字段）
 * 动态更新的意思是，只更新改变的字段值，
 * 注意，不是更新非null字段
 */
@DynamicInsert
@DynamicUpdate
public class MetaData20 implements Serializable {

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
            case 11 : setValue11(value);break;
            case 12 : setValue12(value);break;
            case 13 : setValue13(value);break;
            case 14 : setValue14(value);break;
            case 15 : setValue15(value);break;
            case 16 : setValue16(value);break;
            case 17 : setValue17(value);break;
            case 18 : setValue18(value);break;
            case 19 : setValue19(value);break;
            case 20 : setValue20(value);break;
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
            case 11 : return getValue11();
            case 12 : return getValue12();
            case 13 : return getValue13();
            case 14 : return getValue14();
            case 15 : return getValue15();
            case 16 : return getValue16();
            case 17 : return getValue17();
            case 18 : return getValue18();
            case 19 : return getValue19();
            case 20 : return getValue20();
            default: throw new IllegalArgumentException("invalid index num:" + num);
        }
    }

    private String value0;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String value6;
    private String value7;
    private String value8;
    private String value9;
    private String value10;
    private String value11;
    private String value12;
    private String value13;
    private String value14;
    private String value15;
    private String value16;
    private String value17;
    private String value18;
    private String value19;
    private String value20;
}