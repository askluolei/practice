package com.luolei.template.config;

import com.luolei.template.aop.logging.LoggingAspect;
import io.github.jhipster.config.JHipsterConstants;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * AOP 日志配置
 *
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 17:47
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
