package com.luolei.template.web.rest;

import com.luolei.template.domain.ScheduleTask;
import com.luolei.template.domain.ScheduleTaskStatus;
import com.luolei.template.schedule.ScheduleJobExecutor;
import com.luolei.template.service.ScheduleService;
import com.luolei.template.web.rest.errors.BizError;
import com.luolei.template.web.rest.util.HeaderUtil;
import com.luolei.template.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author 罗雷
 * @date 2018/1/26 0026
 * @time 14:43
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScheduleController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ScheduleService scheduleService;
    private final ApplicationContext applicationContext;
    private final List<ScheduleJobExecutor> jobExecutors;

    public ScheduleController(ScheduleService scheduleService, ApplicationContext applicationContext, @Autowired(required = false) List<ScheduleJobExecutor> jobExecutors) {
        this.scheduleService = scheduleService;
        this.applicationContext = applicationContext;
        if (Objects.nonNull(jobExecutors)) {
            this.jobExecutors = jobExecutors;
        } else {
            this.jobExecutors = Collections.emptyList();
        }
    }

    @PostMapping("/schedule/task")
    public ResponseEntity<ScheduleTask> createScheduleTask(@RequestBody ScheduleTask scheduleTask) throws URISyntaxException {
        log.debug("request to create schedule task : {}", scheduleTask);
        if (!applicationContext.containsBean(scheduleTask.getBeanName())) {
            BizError.REQUEST_PARAM_CHECK_ERROR.exception("不合法法参数:" + scheduleTask.getBeanName() + ", 不存在该名称的 bean");
        }
        if (!(applicationContext.getBean(scheduleTask.getBeanName()) instanceof ScheduleJobExecutor)) {
            BizError.REQUEST_PARAM_CHECK_ERROR.exception("不合法法参数:" + scheduleTask.getBeanName() + ", 对应的bean 类型错误");
        }
        ScheduleTask result = scheduleService.save(scheduleTask);
        return ResponseEntity.created(new URI("/api/shcedule/task/" + result.getId()))
                .headers(HeaderUtil.createAlert("schedule task created", result.getBeanName()))
                .body(result);
    }

    @DeleteMapping("/schedule/task/{id}")
    public ResponseEntity<Void> deleteScheduleTask(@PathVariable Long id) {
        log.debug("request to delete schedule task: {}", id);
        scheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("schedule task deleted", String.valueOf(id))).build();
    }

    @PutMapping("/schedule/task")
    public ResponseEntity<ScheduleTask> updateScheduleTask(@RequestBody ScheduleTask scheduleTask) throws URISyntaxException {
        log.debug("request to update schedule task : {}", scheduleTask);
        if (!applicationContext.containsBean(scheduleTask.getBeanName())) {
            BizError.REQUEST_PARAM_CHECK_ERROR.exception("不合法法参数:" + scheduleTask.getBeanName() + ", 不存在该名称的 bean");
        }
        if (!(applicationContext.getBean(scheduleTask.getBeanName()) instanceof ScheduleJobExecutor)) {
            BizError.REQUEST_PARAM_CHECK_ERROR.exception("不合法法参数:" + scheduleTask.getBeanName() + ", 对应的bean 类型错误");
        }
        ScheduleTask result = scheduleService.update(scheduleTask);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("schedule task updated", String.valueOf(scheduleTask.getId()))).body(result);
    }

    @GetMapping("/schedule/task")
    public ResponseEntity<List<ScheduleTask>> query(Pageable pageable, @RequestParam(required = false) String beanName,
                                                    @RequestParam(required = false) ScheduleTaskStatus status) {
        log.debug("request to query schedule task by condition. pageable:{}, beanName:{}, status:{}", pageable, beanName, status);
        Page<ScheduleTask> page;
        if (Objects.nonNull(beanName) && Objects.nonNull(status)) {
            page = scheduleService.queryByBeanNameAndStatue(pageable, beanName, status);
        } else if (Objects.nonNull(beanName)) {
            page = scheduleService.queryByBeanName(pageable, beanName);
        } else if (Objects.nonNull(status)) {
            page = scheduleService.queryByStatus(pageable, status);
        } else {
            page = scheduleService.queryAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedule/task");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/schedule/task/{id}")
    public ResponseEntity<ScheduleTask> getScheduleTask(@PathVariable Long id) {
        log.debug("request to get schedule task by id : {}", id);
        return ResponseUtil.wrapOrNotFound(scheduleService.getById(id));
    }

    @GetMapping("/schedule/beans")
    public ResponseEntity<List<Map<String, Object>>> getAllSchedule() {
        log.debug("request to get all schedule bean");
        List<Map<String, Object>> list = new ArrayList<>();
        jobExecutors.forEach(scheduleJobExecutor -> {
            Map<String, Object> map = new HashMap<>();
            map.put("beanName", applicationContext.getBeanNamesForType(scheduleJobExecutor.getClass())[0]);
            map.put("author", scheduleJobExecutor.author());
            map.put("explain", scheduleJobExecutor.explain());
            list.add(map);
        });
        return ResponseEntity.ok(list);
    }
}
