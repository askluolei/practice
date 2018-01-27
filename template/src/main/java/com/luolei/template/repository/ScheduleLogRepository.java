package com.luolei.template.repository;

import com.luolei.template.domain.ScheduleLog;
import com.luolei.template.domain.ScheduleTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:37
 */
public interface ScheduleLogRepository extends BaseRepository<ScheduleLog, Long> {

    //@Query("select log from ScheduleLog log where log.task.id = :id")
    Page<ScheduleLog> findByTask(ScheduleTask task, Pageable pageable);
}
