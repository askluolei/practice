package com.luolei.template.oss;

import cn.hutool.core.io.IoUtil;
import com.luolei.template.config.ApplicationProperties;
import com.luolei.template.web.rest.errors.BizError;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.InputStream;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 16:44
 */
public class QiniuCloudStorageService extends CloudStorageService {

    private UploadManager uploadManager;
    private String token;
    private ApplicationProperties.Oss.Qiniu qiniu;

    public QiniuCloudStorageService(ApplicationProperties.Oss.Qiniu qiniu){
        this.qiniu = qiniu;
        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        token = Auth.create(qiniu.getAccessKey(), qiniu.getSecretKey()).
            uploadToken(qiniu.getBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw BizError.UPLOAD_ERROR.exception("上传七牛出错" + res.toString());
            }
        } catch (Exception e) {
            throw BizError.UPLOAD_ERROR.exception(e,"上传文件失败，请核对七牛配置信息");
        }

        return qiniu.getDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        byte[] data = IoUtil.readBytes(inputStream);
        return this.upload(data, path);
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(qiniu.getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(qiniu.getPrefix(), suffix));
    }
}
