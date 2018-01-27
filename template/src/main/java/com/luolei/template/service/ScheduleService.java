package com.luolei.template.service;

import com.luolei.template.domain.ScheduleLog;
import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskStatus;
import com.luolei.template.repository.ScheduleLogRepository;
import com.luolei.template.repository.ScheduleTaskRepository;
import com.luolei.template.schedule.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/14 13:33
 */
@Service
@Transactional
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleTaskRepository jobDao;

    @Autowired
    private ScheduleLogRepository jobLogDao;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<ScheduleTask> scheduleJobList = jobDao.findAll();
        for (ScheduleTask scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            if (Objects.isNull(cronTrigger)) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    public Page<ScheduleLog> getTaskLogsById(Long id, Pageable pageable) {
        log.debug("get schedule task logs by id:{}", id);
        return jobLogDao.findByTask(ScheduleTask.fromId(id), pageable);
    }

    public Optional<ScheduleTask> getById(Long id) {
        log.debug("get ScheduleTask by id:{}", id);
        return Optional.ofNullable(jobDao.findOne(id));
    }

    public Page<ScheduleTask> queryAll(Pageable pageable) {
        log.debug("query all ScheduleTask. pageable:{}", pageable);
        return jobDao.findAll(pageable);
    }

    public Page<ScheduleTask> queryByBeanName(Pageable pageable, String beanName) {
        log.debug("query ScheduleTask by beanName:{}, pageable:{}", beanName, pageable);
        return jobDao.findByBeanName(beanName, pageable);
    }

    public Page<ScheduleTask> queryByStatus(Pageable pageable, ScheduleTaskStatus status) {
        log.debug("query ScheduleTask by status:{}, pageable:{}", status, pageable);
        return jobDao.findByStatus(status, pageable);
    }

    public Page<ScheduleTask> queryByBeanNameAndStatue(Pageable pageable, String beanName, ScheduleTaskStatus status) {
        log.debug("query ScheduleTask by status:{}, beanName:{}, pageable:{}", status, beanName, pageable);
        return jobDao.findByBeanNameAndStatus(beanName, status, pageable);
    }

    public ScheduleTask save(ScheduleTask scheduleJob) {
        log.debug("save ScheduleTask:{}", scheduleJob);
        scheduleJob.setStatus(ScheduleTaskStatus.NORMAL);
        scheduleJob = jobDao.save(scheduleJob);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        return scheduleJob;
    }

    public ScheduleTask update(ScheduleTask scheduleJob) {
        log.debug("update ScheduleTask:{}", scheduleJob);
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        scheduleJob = jobDao.save(scheduleJob);
        return scheduleJob;
    }

    public void delete(Long id) {
        log.debug("delete ScheduleTask by id:{}", id);
        ScheduleUtils.deleteScheduleJob(scheduler, id);
        ScheduleTask task = jobDao.findOne(id);
        if (Objects.nonNull(task)) {
            jobDao.delete(task);
        }
    }

    public void deleteBatch(List<Long> ids) {
        log.debug("delete ScheduleTask by ids:{}", ids);
        for (Long id : ids) {
            ScheduleUtils.deleteScheduleJob(scheduler, id);
        }
        jobDao.delete(jobDao.findAll(ids));
    }

    public List<ScheduleTask> updateBatch(List<ScheduleTask> scheduleJobs) {
        log.debug("update batch");
        return scheduleJobs.stream()
            .map(this::update)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void run(List<Long> ids) {
        log.debug("run schedule task : {}", ids);
        List<ScheduleTask> scheduleJobs = jobDao.findAll(ids);
        for(ScheduleTask job : scheduleJobs){
            ScheduleUtils.run(scheduler, jobDao.findOne(job.getId()));
        }
    }

    public void pause(List<Long> ids) {
        log.debug("pause schedule task : {}", ids);
        List<ScheduleTask> scheduleJobs = jobDao.findAll(ids);
        for (ScheduleTask job : scheduleJobs) {
            job.setStatus(ScheduleTaskStatus.PAUSE);
            ScheduleUtils.pauseJob(scheduler, job.getId());
        }
        updateBatch(scheduleJobs);
    }

    public void resume(List<Long> ids) {
        log.debug("resume schedule task : {}", ids);
        List<ScheduleTask> scheduleJobs = jobDao.findAll(ids);
        for (ScheduleTask job : scheduleJobs) {
            job.setStatus(ScheduleTaskStatus.NORMAL);
            ScheduleUtils.resumeJob(scheduler, job.getId());
        }
        updateBatch(scheduleJobs);
    }
}
