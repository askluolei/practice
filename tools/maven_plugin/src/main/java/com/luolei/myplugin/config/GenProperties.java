package com.luolei.myplugin.config;

import lombok.Data;

import java.util.List;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 14:29
 */
@Data
public class GenProperties {

    private SettingProperties settings;
    private List<EntityProperties> entities;
}
