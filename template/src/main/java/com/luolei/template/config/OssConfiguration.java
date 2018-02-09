package com.luolei.template.config;

import com.luolei.template.oss.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 16:59
 */
@Configuration
public class OssConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public CloudStorageService cloudStorageService(ApplicationProperties properties) {
        boolean enableOss = properties.getOss().getEnable();
        if (!enableOss) {
            return null;
        }
        OssType ossType = properties.getOss().getType();
        CloudStorageService cloudStorageService = null;
        if (Objects.nonNull(ossType)) {
            switch (ossType) {
                case Qiniu:
                    cloudStorageService = new QiniuCloudStorageService(properties.getOss().getQiniu());
                    break;
                case Aliyun:
                    cloudStorageService = new AliyunCloudStorageService(properties.getOss().getAliyun());
                    break;
                case QCloud:
                    cloudStorageService = new QcloudCloudStorageService(properties.getOss().getQCloud());
                    break;
                default:
                    log.warn("unknown oss type:{}", ossType);
            }
        }
        return cloudStorageService;
    }
}
