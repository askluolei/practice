package com.luolei.template.repository;

import com.luolei.template.domain.PersistentAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:48
 */
public interface PersistenceAuditEventRepository extends BaseRepository<PersistentAuditEvent, Long> {

    List<PersistentAuditEvent> findByPrincipal(String principal);

    List<PersistentAuditEvent> findByAuditEventDateAfter(Instant after);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, Instant after);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(String principle, Instant after, String type);

    Page<PersistentAuditEvent> findAllByAuditEventDateBetween(Instant fromDate, Instant toDate, Pageable pageable);
}
