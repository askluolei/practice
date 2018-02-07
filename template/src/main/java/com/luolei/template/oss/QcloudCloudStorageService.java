package com.luolei.template.oss;

import com.luolei.template.config.ApplicationProperties;
import com.luolei.template.web.rest.errors.BizError;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 16:55
 */
public class QcloudCloudStorageService extends CloudStorageService {

    private COSClient client;
    private ApplicationProperties.Oss.QCloud qCloud;

    public QcloudCloudStorageService(ApplicationProperties.Oss.QCloud qCloud){
        this.qCloud = qCloud;
        //初始化
        init();
    }

    private void init(){
        Credentials credentials = new Credentials(qCloud.getAppId(), qCloud.getSecretId(),
            qCloud.getSecretKey());

        //初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        clientConfig.setRegion(qCloud.getRegion());

        client = new COSClient(clientConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if(!path.startsWith("/")) {
            path = "/" + path;
        }

        //上传到腾讯云
        UploadFileRequest request = new UploadFileRequest(qCloud.getBucketName(), path, data);
        String response = client.uploadFile(request);

        JSONObject jsonObject = JSONObject.fromObject(response);
        if(jsonObject.getInt("code") != 0) {
            throw BizError.UPLOAD_ERROR.exception("文件上传失败，" + jsonObject.getString("message"));
        }

        return qCloud.getDomain() + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw BizError.UPLOAD_ERROR.exception(e, "上传文件失败");
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(qCloud.getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(qCloud.getPrefix(), suffix));
    }
}
