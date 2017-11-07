package com.luolei.ignite.start;

import org.apache.ignite.IgniteException;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 17:30
 */
public class MyLifecycleBean implements LifecycleBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
        /**
         * 这里的 slf4j 绑定不到具体的log 实现，后面研究解决
         */
        switch (evt) {
            case BEFORE_NODE_START:
                logger.info("=== 节点启动前 ===");
                break;
            case AFTER_NODE_START:
                logger.info("=== 节点启动后 ===");
                break;
            case BEFORE_NODE_STOP:
                logger.info("=== 节点关闭前 ===");
                break;
            case AFTER_NODE_STOP:
                logger.info("=== 节点关闭后 ===");
                break;
            default:
        }
    }
}
