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
 * 存储字节对象的表
 *
 * @author 罗雷
 * @date 2017/12/28 0028
 * @time 15:18
 */
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tenant", "object"})
@Entity
@Table(name = "t_meta_blob_data10")
/**
 * 这里的字段太多，而且大多是空字段，因此需要动态插入,动态更新（不管为null的字段）
 * 动态更新的意思是，只更新改变的字段值，
 * 注意，不是更新非null字段
 */
@DynamicInsert
@DynamicUpdate
public class MetaBlobData10 implements Serializable {

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

    public void setValue(int num, byte[] value) {
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

    public byte[] getValue(int num) {
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
    @Column(columnDefinition = "BLOB")
    private byte[] value0;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value1;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value2;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value3;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value4;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value5;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value6;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value7;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value8;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value9;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] value10;
}