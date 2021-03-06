package com.luolei.template.config.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.domain.AbstractAuditingEntity;
import com.luolei.template.domain.EntityAuditEvent;
import com.luolei.template.repository.EntityAuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 异步的实体审计写入
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:58
 */
@Component
public class AsyncEntityAuditEventWriter {

    private final Logger log = LoggerFactory.getLogger(AsyncEntityAuditEventWriter.class);

    private final EntityAuditEventRepository auditingEntityRepository;

    private final ObjectMapper objectMapper; //Jackson object mapper

    public AsyncEntityAuditEventWriter(EntityAuditEventRepository auditingEntityRepository, ObjectMapper objectMapper) {
        this.auditingEntityRepository = auditingEntityRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Writes audit events to DB asynchronously in a new thread
     */
    @Async
    public void writeAuditEvent(Object target, EntityAuditAction action) {
        log.debug("-------------- Post {} audit  --------------", action.value());
        try {
            EntityAuditEvent auditedEntity = prepareAuditEntity(target, action);
            if (auditedEntity != null) {
                auditingEntityRepository.save(auditedEntity);
            }
        } catch (Exception e) {
            log.error("Exception while persisting audit entity for {} error: {}", target, e);
        }
    }

    /**
     * Method to prepare auditing entity
     *
     * @param entity
     * @param action
     * @return
     */
    private EntityAuditEvent prepareAuditEntity(final Object entity, EntityAuditAction action) {
        EntityAuditEvent auditedEntity = new EntityAuditEvent();
        Class<?> entityClass = entity.getClass(); // Retrieve entity class with reflection
        auditedEntity.setAction(action.value());
        auditedEntity.setEntityType(entityClass.getName());
        Long entityId;
        String entityData;
        log.trace("Getting Entity Id and Content");
        try {
            AbstractAuditingEntity abstractAuditingEntity = (AbstractAuditingEntity) entity;
            entityId = abstractAuditingEntity.getId();
            entityData = objectMapper.writeValueAsString(entity);
        } catch (IOException e) {
            log.error("Exception while getting entity ID and content {}", e);
            return null;
        }
        auditedEntity.setEntityId(entityId);
        auditedEntity.setEntityValue(entityData);
        final AbstractAuditingEntity abstractAuditEntity = (AbstractAuditingEntity) entity;
        if (EntityAuditAction.CREATE.equals(action)) {
            auditedEntity.setModifiedBy(abstractAuditEntity.getCreatedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getCreatedDate());
            auditedEntity.setCommitVersion(1);
        } else {
            auditedEntity.setModifiedBy(abstractAuditEntity.getLastModifiedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getLastModifiedDate());
            calculateVersion(auditedEntity);
        }
        log.trace("Audit Entity --> {} ", auditedEntity.toString());
        return auditedEntity;
    }

    private void calculateVersion(EntityAuditEvent auditedEntity) {
        log.trace("Version calculation. for update/remove");
        Integer lastCommitVersion = auditingEntityRepository.findMaxCommitVersion(auditedEntity
                .getEntityType(), auditedEntity.getEntityId());
        log.trace("Last commit version of entity => {}", lastCommitVersion);
        if(lastCommitVersion!=null && lastCommitVersion != 0){
            log.trace("Present. Adding version..");
            auditedEntity.setCommitVersion(lastCommitVersion + 1);
        } else {
            log.trace("No entities.. Adding new version 1");
            auditedEntity.setCommitVersion(1);
        }
    }
}
