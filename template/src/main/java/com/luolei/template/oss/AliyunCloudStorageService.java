package com.luolei.template.oss;

import com.aliyun.oss.OSSClient;
import com.luolei.template.config.ApplicationProperties;
import com.luolei.template.web.rest.errors.BizError;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 16:53
 */
public class AliyunCloudStorageService extends CloudStorageService {

    private OSSClient client;
    private ApplicationProperties.Oss.Aliyun aliyun;

    public AliyunCloudStorageService(ApplicationProperties.Oss.Aliyun aliyun){
        this.aliyun = aliyun;
        //初始化
        init();
    }

    private void init(){
        client = new OSSClient(aliyun.getEndPoint(), aliyun.getAccessKeyId(),
            aliyun.getAccessKeySecret());
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject(aliyun.getBucketName(), path, inputStream);
        } catch (Exception e){
            throw BizError.UPLOAD_ERROR.exception(e, "上传文件失败，请检查配置信息");
        }
        return aliyun.getDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(aliyun.getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(aliyun.getPrefix(), suffix));
    }
}
