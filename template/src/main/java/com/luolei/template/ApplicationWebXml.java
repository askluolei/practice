package com.luolei.template;

import com.luolei.template.config.DefaultProfileUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * WAR 包支持
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 11:09
 */
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        DefaultProfileUtil.addDefaultProfile(application.application());
        return application.sources(TemplateApp.class);
    }
}
