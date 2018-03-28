package com.luolei.myplugin.config;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 17:45
 */
public class JsonTest {

    @Test
    public void test() throws IOException {
        GenProperties properties = JSON.parseObject(getClass().getResourceAsStream("/gen.json"), GenProperties.class);

        String read = IoUtil.read(getClass().getResourceAsStream("/entity.template"), StandardCharsets.UTF_8);
        Template template = Engine.use().getTemplateByString(read);
        Map<String, Object> map = new HashMap<>();
        for (EntityProperties entityProperties : properties.getEntities()) {
            map.put("entity", entityProperties);
            map.put("settings", properties.getSettings());
            String result = template.renderToString(map);
            System.out.println(result);
        }
        StringWriter stringWriter = new StringWriter();
        JSON.writeJSONString(stringWriter, properties, SerializerFeature.PrettyFormat);
        System.out.println(stringWriter.toString());
    }
}
