package com.luolei.template.config.audit;

/**
 * 操作
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:57
 */
public enum EntityAuditAction {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private String value;

    EntityAuditAction(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return this.value();
    }
}
