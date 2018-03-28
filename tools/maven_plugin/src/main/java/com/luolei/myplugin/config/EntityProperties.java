package com.luolei.myplugin.config;

import lombok.Data;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 14:31
 */
@Data
public class EntityProperties {

    private String name;

    private String packageName;

    private List<FieldProperties> fields;
}
