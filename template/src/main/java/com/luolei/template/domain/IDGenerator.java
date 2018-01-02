package com.luolei.template.domain;

import com.luolei.template.utils.SpringContextUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.AbstractUUIDGenerator;

import java.io.Serializable;
import java.util.Objects;

/**
 * 主键自动生成
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 13:49
 */
public class IDGenerator extends AbstractUUIDGenerator {

    private Sequence sequence;

    private synchronized void ensureInit() {
        sequence = SpringContextUtils.getBean(Sequence.class);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        if (Objects.isNull(sequence)) {
            ensureInit();
        }
        return sequence.nextId();
    }
}
