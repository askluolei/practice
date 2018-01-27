package com.luolei.template.repository;

import com.luolei.template.TemplateApp;
import com.luolei.template.domain.ScheduleLog;
import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskResult;
import com.luolei.template.domain.ScheduleTaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/1/27 0027
 * @time 11:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class ScheduleLogRepositoryTest {

    @Autowired
    private ScheduleLogRepository scheduleLogRepository;

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    private Long taskId;

    @Before
    public void init() {
        ScheduleTask task = new ScheduleTask();
        task.setBeanName("testBean");
        task.setCronExpression("1 1 1 1 1 1");
        task.setParams("测试");
        task.setStatus(ScheduleTaskStatus.NORMAL);
        task = scheduleTaskRepository.save(task);
        taskId = task.getId();

        ScheduleLog log1 = new ScheduleLog();
        log1.setResult(ScheduleTaskResult.SUCCESS);
        log1.setTimes(1L);
        log1.setTask(task);
        log1 = scheduleLogRepository.save(log1);

        ScheduleLog log2 = new ScheduleLog();
        log2.setResult(ScheduleTaskResult.FAIL);
        log2.setTimes(2L);
        log2.setTask(task);
        log2 = scheduleLogRepository.save(log2);
    }

    @Test
    public void findByTaskId() throws Exception {
        ScheduleTask task = new ScheduleTask();
        task.setId(taskId);
        Page<ScheduleLog> logs = scheduleLogRepository.findByTask(task, new PageRequest(0, 10));
        assertThat(logs.getContent().size()).isEqualTo(2);
    }

}
