package com.luolei.meta.domain;

import lombok.Data;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 15:08
 */
@Data
public class MetaBlobData extends AuditDomain {

    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 对象id
     */
    private Long objectId;

    /**
     * 数据名
     */
    private String name;

    private byte[] value0;
    private byte[] value1;
    private byte[] value2;
    private byte[] value3;
    private byte[] value4;
    private byte[] value5;
    private byte[] value6;
    private byte[] value7;
    private byte[] value8;
    private byte[] value9;
    private byte[] value10;

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
}
