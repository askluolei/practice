package com.luolei.template.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 11:14
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Getter
@Setter
public class ApplicationProperties {

    private Sequence sequence = new Sequence();

    @Getter
    @Setter
    public static class Sequence {
        private Long workerId = 0L;
        private Long datacenterId = 0L;
    }
}
