package com.luolei.template.schedule;

import com.alibaba.fastjson.JSON;
import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskStatus;
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
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "getCronTrigger异常，请检查qrtz开头的表，是否有脏数据");
        }
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleTask scheduleJob) {
        try {
            //构建job
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getId())).build();

            //构建cron
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()).withMisfireHandlingInstructionDoNothing();

            //根据cron，构建一个CronTrigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getId())).withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取, 这里注意的是 JSON 序列化的时候触发延时加载了，但是session已经关闭 设置为null 不需要logs
            scheduleJob.setLogs(null);
            jobDetail.getJobDataMap().put(ScheduleTask.JOB_PARAM_KEY, JSON.toJSONString(scheduleJob));

            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (scheduleJob.getStatus() == ScheduleTaskStatus.PAUSE) {
                pauseJob(scheduler, scheduleJob.getId());
            }
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "创建定时任务失败");
        }

    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleTask scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getId());

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getId());

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            //参数
            trigger.getJobDataMap().put(ScheduleTask.JOB_PARAM_KEY, JSON.toJSONString(scheduleJob));

            scheduler.rescheduleJob(triggerKey, trigger);

            //暂停任务
            if(scheduleJob.getStatus() == ScheduleTaskStatus.PAUSE){
                pauseJob(scheduler, scheduleJob.getId());
            }

        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "更新定时任务失败");
        }
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, ScheduleTask scheduleJob) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleTask.JOB_PARAM_KEY, JSON.toJSONString(scheduleJob));

            scheduler.triggerJob(getJobKey(scheduleJob.getId()), dataMap);
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "立即执行定时任务失败");
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "暂停定时任务失败");
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw BizError.SCHEDULE_CONFIG_ERROR.exception(e, "删除定时任务失败");
        }
    }
}
