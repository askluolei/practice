package com.luolei.template.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.domain.ScheduleLog;
import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskResult;
import com.luolei.template.repository.ScheduleLogRepository;
import com.luolei.template.utils.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:39
 */
public class ScheduleJob extends QuartzJobBean {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
        ScheduleTask scheduleTask = null;
        try {
            scheduleTask = objectMapper.readValue(jobExecutionContext.getMergedJobDataMap().getString(ScheduleTask.JOB_PARAM_KEY), ScheduleTask.class);
        } catch (IOException e) {
            log.error("json 解析异常", e);
            return;
        }

        ScheduleLog scheduleLog = new ScheduleLog();
        scheduleLog.setTask(scheduleTask);
        Object bean = SpringContextUtils.getBean(scheduleTask.getBeanName());
        ScheduleLogRepository scheduleLogRepository = SpringContextUtils.getBean(ScheduleLogRepository.class);
        String error = null;
        ScheduleTaskResult result = ScheduleTaskResult.FAIL;
        if (Objects.nonNull(bean) && bean instanceof ScheduleJobExecutor) {
            ScheduleJobExecutor jobExecutor = (ScheduleJobExecutor) bean;
            long startTime = System.currentTimeMillis();
            log.debug("准备执行任务: {}", jobExecutor.getClass().getName());
            try {
                jobExecutor.execute(jobExecutionContext, scheduleTask.getParams());
                result = ScheduleTaskResult.SUCCESS;
                log.debug("任务实现完毕: {}", jobExecutor.getClass().getName());
            } catch (Throwable t) {
                log.error("定时任务执行异常", t);
                error = t.getMessage();
                result = ScheduleTaskResult.FAIL;
            } finally {
                long times = System.currentTimeMillis() - startTime;
                scheduleLog.setTimes(times);
                scheduleLog.setError(error);
                scheduleLog.setResult(result);
                scheduleLogRepository.save(scheduleLog);
            }
        } else {
            log.warn("不存在名为：{} 的 Bean");
        }

    }
}
