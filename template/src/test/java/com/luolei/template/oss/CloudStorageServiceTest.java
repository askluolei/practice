package com.luolei.template.oss;

import cn.hutool.core.io.FileUtil;
import com.luolei.template.TemplateApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 17:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class CloudStorageServiceTest {

    @Autowired
    private CloudStorageService cloudStorageService;

    @Test
    public void testUpload() {
        byte[] bytes = FileUtil.readBytes("E:\\临时文件\\test.jpg");
        String link = cloudStorageService.upload(bytes, "example.jpg");
        assertThat(link).isNotBlank();
    }
}
