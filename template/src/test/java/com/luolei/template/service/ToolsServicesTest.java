package com.luolei.template.service;

import com.alibaba.fastjson.JSON;
import com.luolei.template.TemplateApp;
import com.luolei.template.web.rest.vm.SqlVM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 14:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApp.class)
@Transactional
public class ToolsServicesTest {

    @Autowired
    private ToolsService toolsService;

    @Test
    public void test() {
        toolsService.getDatabaseInfo();
        String sql = "select * from _user";
        SqlVM sqlVM = new SqlVM();
        sqlVM.setSql(sql);
        List<Map<String, Object>> list = toolsService.executeSelect(sqlVM);
        for (Map<String, Object> map : list) {
            System.out.println(JSON.toJSONString(map));
        }
    }
}
