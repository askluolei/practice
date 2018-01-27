package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

/**
 * @author 罗雷
 * @date 2018/1/11 0011
 * @time 19:31
 */
@Entity
@Table(name = "_schedule_task_log")
@Getter
@Setter
@ToString(exclude = "task")
public class ScheduleLog extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    /**
     *  执行结果
     */
    @Column(name = "task_result")
    private ScheduleTaskResult result;

    /**
     * 失败信息
     */
    @Column(name = "error_message", length = 2000)
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    @Column(name = "tf_times")
    private Long times;

    /**
     * 任务
     */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private ScheduleTask task;

}
