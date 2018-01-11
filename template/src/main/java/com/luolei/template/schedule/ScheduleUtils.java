package com.luolei.template.schedule;

import com.luolei.template.web.rest.errors.BizError;
import org.quartz.*;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:41
 */
public class ScheduleUtils {

    private static final String JOB_NAME = "TASK_";

    /**
     * 获取触发器key
     */
    private static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    private static JobKey getJobKey(Long jodIb) {
        return JobKey.jobKey(JOB_NAME + jodIb);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_ERROR.exception(e, "getCronTrigger异常，请检查qrtz开头的表，是否有脏数据");
        }
    }
}
