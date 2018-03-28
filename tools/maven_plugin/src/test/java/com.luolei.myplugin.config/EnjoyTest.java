package com.luolei.myplugin.config;

import cn.hutool.core.io.IoUtil;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 16:57
 */
public class EnjoyTest {

    @Test
    public void test() {
        String read = IoUtil.read(getClass().getResourceAsStream("/test.template"), StandardCharsets.UTF_8);
        Template template = Engine.use().getTemplateByString(read);
        Map<String, EntityProperties> map = new HashMap<>();
        EntityProperties properties = new EntityProperties();
        map.put("test", properties);
        properties.setName("hello");
        String result = template.renderToString(map);
        System.out.println(result);
    }
}
