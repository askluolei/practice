package com.luolei.template.repository;

import com.luolei.template.TemplateApp;
import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskStatus;
import com.luolei.template.schedule.ScheduleJobExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/1/26 0026
 * @time 14:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class ScheduleTaskRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private String beanName;

    @Before
    public void init() {
        String[] beanNames = applicationContext.getBeanNamesForType(ScheduleJobExecutor.class);
        log.info("beanNames:{}", Arrays.toString(beanNames));
        this.beanName = beanNames[0];
        ScheduleTask task = new ScheduleTask();
        task.setBeanName(beanName);
        task.setStatus(ScheduleTaskStatus.NORMAL);
        task.setCronExpression("0 0/1 * * * ? ");
        task.setRemark("测试用的111");
        task.setParams("测试参数111");
        scheduleTaskRepository.save(task);

        task = new ScheduleTask();
        task.setBeanName(beanName);
        task.setStatus(ScheduleTaskStatus.NORMAL);
        task.setCronExpression("0 0/1 * * * ? ");
        task.setRemark("测试用的222");
        task.setParams("测试参数222");
        scheduleTaskRepository.save(task);

        task = new ScheduleTask();
        task.setBeanName(beanName);
        task.setStatus(ScheduleTaskStatus.PAUSE);
        task.setCronExpression("0 0/1 * * * ? ");
        task.setRemark("测试用的333");
        task.setParams("测试参数333");
        scheduleTaskRepository.save(task);
    }
    @Test
    public void findByBeanName() throws Exception {
        Page<ScheduleTask> tasks = scheduleTaskRepository.findByBeanName(beanName, new PageRequest(0, 10));
        assertThat(tasks).isNotNull();
        assertThat(tasks.getContent().size()).isEqualTo(3);
        assertThat(tasks.getTotalPages()).isEqualTo(1);
        assertThat(tasks.getTotalElements()).isEqualTo(3L);
    }

    @Test
    public void findByStatus() throws Exception {
        Page<ScheduleTask> tasks = scheduleTaskRepository.findByStatus(ScheduleTaskStatus.NORMAL, new PageRequest(0, 10));
        assertThat(tasks).isNotNull();
        assertThat(tasks.getContent().size()).isEqualTo(2);
        assertThat(tasks.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findByBeanNameAndStatus() throws Exception {
        Page<ScheduleTask> tasks = scheduleTaskRepository.findByBeanNameAndStatus(beanName, ScheduleTaskStatus.NORMAL, new PageRequest(0, 10));
        assertThat(tasks).isNotNull();
        assertThat(tasks.getContent().size()).isEqualTo(2);
        assertThat(tasks.getTotalPages()).isEqualTo(1);
    }

}
