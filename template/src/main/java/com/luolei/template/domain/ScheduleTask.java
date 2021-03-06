package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:28
 */
@Entity
@Table(name = "_schedule_task")
@Getter
@Setter
@ToString(exclude = "logs")
public class ScheduleTask extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * new 对象 只设置id
     * @param id
     * @return
     */
    public static ScheduleTask fromId(Long id) {
        ScheduleTask task = new ScheduleTask();
        task.setId(id);
        return task;
    }

    /**
     * spring bean名称
     */
    @Column(name = "bean_name", length = 64, nullable = false)
    @NotNull
    private String beanName;

    /**
     * 方法名
     */
    @Column(name = "method_name", length = 64)
    private String methodName;

    /**
     * 参数
     */
    @Column(name = "params")
    private String params;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression", length = 16)
    @NotNull
    private String cronExpression;

    /**
     * 任务状态
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScheduleTaskStatus status = ScheduleTaskStatus.NORMAL;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 任务执行日志
     */
    @OneToMany(mappedBy = "task", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<ScheduleLog> logs;

    /**
     * 下次触发的时间
     */
    @Transient
    private LocalDateTime nextFireTime;
}
