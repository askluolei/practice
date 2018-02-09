package com.luolei.template.config;

import com.luolei.template.oss.OssType;
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

    private Oss oss;

    @Getter
    @Setter
    public static class Sequence {
        private Long workerId = 0L;
        private Long datacenterId = 0L;
    }

    @Getter
    @Setter
    public static class Oss {

        /**
         * 是否开启配置
         */
        private Boolean enable;
        /**
         * 选择的配置
         */
        private OssType type;

        private Qiniu qiniu;
        private Aliyun aliyun;
        private QCloud qCloud;

        /**
         * 七牛云配置
         */
        @Getter
        @Setter
        public static class Qiniu {
            private String domain;
            private String prefix;
            private String accessKey;
            private String secretKey;
            private String bucketName;
        }

        /**
         * 阿里云配置
         */
        @Getter
        @Setter
        public static class Aliyun {
            private String domain;
            private String prefix;
            private String endPoint;
            private String accessKeyId;
            private String accessKeySecret;
            private String bucketName;
        }

        /**
         * 腾讯云配置
         */
        @Getter
        @Setter
        public static class QCloud {
            private String domain;
            private String prefix;
            private Long appId;
            private String secretId;
            private String secretKey;
            private String bucketName;
            private String region;
        }
    }
}
