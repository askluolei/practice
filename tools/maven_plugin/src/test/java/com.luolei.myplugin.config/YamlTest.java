package com.luolei.myplugin.config;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 罗雷
 * @date 2018/3/27 0027
 * @time 17:36
 */
public class YamlTest {

    @Test
    public void test() {
        GenProperties genProperties = new GenProperties();
        SettingProperties settingProperties = new SettingProperties();
        settingProperties.setPackageName("com.luolei.template");
        settingProperties.setVm(true);
        genProperties.setSettings(settingProperties);
        List<EntityProperties> entityProperties = new ArrayList<>();
        EntityProperties e1 = new EntityProperties();
        e1.setName("Regin");
        List<FieldProperties> fields = new ArrayList<>();
        FieldProperties fieldProperties = new FieldProperties();
        fieldProperties.setName("regionName");
        fieldProperties.setType("String");
        fields.add(fieldProperties);
        e1.setFields(fields);
        entityProperties.add(e1);

        Yaml yaml = new Yaml();
        String result = yaml.dump(genProperties);
        System.out.println(result);

    }
}
