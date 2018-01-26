package com.luolei.template.repository;

import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:37
 */
public interface ScheduleTaskRepository extends BaseRepository<ScheduleTask, Long> {

    Page<ScheduleTask> findByBeanName(String beanName, Pageable pageable);

    Page<ScheduleTask> findByStatus(ScheduleTaskStatus status, Pageable pageable);

    Page<ScheduleTask> findByBeanNameAndStatus(String beanName, ScheduleTaskStatus status, Pageable pageable);
}
