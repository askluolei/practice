package com.luolei.template.repository;

import com.luolei.template.schedule.ScheduleJobExecutor;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author 罗雷
 * @date 2018/1/26 0026
 * @time 14:26
 */
@Component
public class TestScheduleExecutor implements ScheduleJobExecutor {

    @Override
    public void execute(JobExecutionContext jobExecutionContext, String jsonStr) {
        System.out.println("hello:" + jsonStr);
    }

    @Override
    public String author() {
        return "lei luo";
    }

    @Override
    public String explain() {
        return "调度测试";
    }
}
