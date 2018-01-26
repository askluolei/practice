package com.luolei.template.schedule;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author 罗雷
 * @date 2018/1/26 0026
 * @time 15:32
 */
@Component
public class ExampleScheduleJobExecutor implements ScheduleJobExecutor {

    @Override
    public void execute(JobExecutionContext jobExecutionContext, String jsonStr) {
        System.out.println("hello:" + jsonStr);
    }

    @Override
    public String author() {
        return "luo lei";
    }

    @Override
    public String explain() {
        return "调度示例";
    }
}
