package com.luolei.template.config.audit;

import com.luolei.template.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 19:57
 */
public class EntityAuditEventListener extends AuditingEntityListener {

    private final Logger log = LoggerFactory.getLogger(EntityAuditEventListener.class);

    @PostPersist
    public void onPostCreate(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.CREATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting create audit entity {}", e);
        }
    }

    @PostUpdate
    public void onPostUpdate(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.UPDATE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting update audit entity {}", e);
        }
    }

    @PostRemove
    public void onPostRemove(Object target) {
        try {
            AsyncEntityAuditEventWriter asyncEntityAuditEventWriter = SpringContextUtils.getBean(AsyncEntityAuditEventWriter.class);
            asyncEntityAuditEventWriter.writeAuditEvent(target, EntityAuditAction.DELETE);
        } catch (NoSuchBeanDefinitionException e) {
            log.error("No bean found for AsyncEntityAuditEventWriter");
        } catch (Exception e) {
            log.error("Exception while persisting delete audit entity {}", e);
        }
    }
}
