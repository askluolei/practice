package com.luolei.myplugin.config;

import cn.hutool.core.io.IoUtil;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 14:42
 */
public class ConfigTest {

    @Test
    public void test() {
        Yaml yaml = new Yaml();
        GenProperties properties = yaml.loadAs(getClass().getResourceAsStream("/gen.yml"), GenProperties.class);

        String read = IoUtil.read(getClass().getResourceAsStream("/entity.template"), StandardCharsets.UTF_8);
        Template template = Engine.use().getTemplateByString(read);
        Map<String, Object> map = new HashMap<>();
        for (EntityProperties entityProperties : properties.getEntities()) {
            map.put("entity", entityProperties);
            map.put("settings", properties.getSettings());
            String result = template.renderToString(map);
            System.out.println(result);
        }
    }
}
