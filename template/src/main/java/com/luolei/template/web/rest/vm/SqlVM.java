package com.luolei.template.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 14:33
 */
@Getter
@Setter
public class SqlVM {

    /**
     * sql 语句
     */
    @NotBlank
    private String sql;

    /**
     * sql 语句的类型
     * select *
     * insert *
     * delete *
     * update *
     */
    private String type;

    private Boolean save;

    private String title;

    private String explanation;

    /**
     * 这个不需要前端传
     * 实际执行的sql，排除了注释行的
     */
    private String executeSql;
}
