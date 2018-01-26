package com.luolei.template.schedule;

import org.quartz.JobExecutionContext;

/**
 * 调度任务
 * @author 罗雷
 * @date 2018/1/26 0026
 * @time 13:37
 */
public interface ScheduleJobExecutor {
    /**
     * 调度执行的方法
     * @param jobExecutionContext
     * @param jsonStr
     */
    void execute(JobExecutionContext jobExecutionContext, String jsonStr);

    /**
     * 作者
     * @return
     */
    String author();

    /**
     * 调度说明
     * @return
     */
    String explain();
}
