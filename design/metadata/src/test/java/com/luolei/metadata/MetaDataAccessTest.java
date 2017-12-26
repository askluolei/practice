package com.luolei.metadata;

import com.luolei.metadata.repository.MetaDataRepository;
import com.luolei.metadata.repository.MetaFieldRepository;
import com.luolei.metadata.repository.MetaObjectRepository;
import com.luolei.metadata.repository.MetaTenantRepository;
import com.luolei.metadata.support.DataTransform;
import com.luolei.metadata.tables.MetaData;
import com.luolei.metadata.tables.MetaField;
import com.luolei.metadata.tables.MetaObject;
import com.luolei.metadata.tables.MetaTenant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2017/12/26 0026
 * @time 17:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MetaDataAccessTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MetaTenantRepository tenantRepository;

    @Autowired
    private MetaObjectRepository objectRepository;

    @Autowired
    private MetaFieldRepository fieldRepository;

    @Autowired
    private MetaDataRepository dataRepository;

    @Autowired
    private DataTransform dataTransform;

    private MetaTenant tenant;

    private MetaObject metaObject;


    @Before
    public void init() {
        /**
         * 添加一个租户
         */
        tenant = new MetaTenant();
        tenant.setName("测试客户1");
        tenant = tenantRepository.save(tenant);

        /**
         * 添加一个租户表
         */
        metaObject = new MetaObject();
        metaObject.setTenant(tenant);
        metaObject.setObjectName("user");
        metaObject.setObjectNaturalName("用户表");
        metaObject = objectRepository.save(metaObject);
    }

    @Test
    public void test() {
        List<MetaField> fields = new ArrayList<>();

        /**
         * 给 用户表 添加 主键
         */
        MetaField idField = new MetaField();
        idField.setTenant(tenant);
        idField.setObj(metaObject);
        idField.setFieldName("userId");
        idField.setFieldNaturalName("主键");
        idField.setFieldNum(0);
        idField.setIsPrimary(true);
        idField.setDataType("Long");
        idField = fieldRepository.save(idField);
        fields.add(idField);

        /**
         * 给 用户表 添加 firstName 字段
         */
        MetaField firstNameField = new MetaField();
        firstNameField.setTenant(tenant);
        firstNameField.setObj(metaObject);
        firstNameField.setFieldName("firstName");
        firstNameField.setFieldNaturalName("名");
        firstNameField.setFieldNum(1);
        firstNameField.setDataType("String");
        firstNameField = fieldRepository.save(firstNameField);
        fields.add(firstNameField);

        /**
         * 给 用户表 添加 lastName 字段
         */
        MetaField lastNameField = new MetaField();
        lastNameField.setTenant(tenant);
        lastNameField.setObj(metaObject);
        lastNameField.setFieldName("lastName");
        lastNameField.setFieldNaturalName("姓");
        lastNameField.setFieldNum(2);
        lastNameField.setDataType("String");
        lastNameField = fieldRepository.save(lastNameField);
        fields.add(lastNameField);

        assertThat(tenantRepository.count()).isEqualTo(1L);
        assertThat(objectRepository.count()).isEqualTo(1L);
        assertThat(fieldRepository.count()).isEqualTo(3L);

        /**
         * 待插入数据
         */
        Map<String, String> map = new HashMap<>();
        map.put("firstName", "lei");
        map.put("lastName", "luo");

        /**
         * 转换成通用数据
         */
        MetaData data = dataTransform.transform(map, metaObject.getObjId());

        /**
         * 插入数据表
         */
        data = dataRepository.save(data);
        log.info("value0:{}, value1:{},value2:{}", data.getValue0(), data.getValue1(), data.getValue2());
        assertThat(dataRepository.count()).isEqualTo(1L);

        /**
         * 删除数据就不需要了，根据id删除，
         * data的id是全局唯一的
         * 唯一需要考虑的是，要不要逻辑删除
         */

        /**
         * 这里更新数据
         */
        Map<String, String> toUpdate = new HashMap<>();
        toUpdate.put("guid", data.getGuid().toString());
        toUpdate.put("userId", data.getValue0());
        toUpdate.put("lastName", "hello");
        MetaData toUpdateData = dataTransform.transform(toUpdate, metaObject.getObjId());
        toUpdateData = dataRepository.save(toUpdateData);
        log.info("value0:{}, value1:{},value2:{}", toUpdateData.getValue0(), toUpdateData.getValue1(), toUpdateData.getValue2());
        assertThat(dataRepository.count()).isEqualTo(1L);

        /**
         * 最主要的就是查询了，查询的性能相比建物理表会下降
         * 在数据少的时候感觉不明显
         * 还有就是如何应对复杂查询
         *
         * 这里先考虑单表查询
         */

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firstName", "lei");

        Map<String, String> params = dataTransform.repalceFieldName(queryParams, metaObject.getObjId());
        Specification<MetaData> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Predicate predicate = cb.equal(root.get(key), value);
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        List<MetaData> dataList = dataRepository.findAll(specification);
        assertThat(dataList.size()).isEqualTo(1);
        log.info("value0:{}, value1:{},value2:{}", dataList.get(0).getValue0(), dataList.get(0).getValue1(), dataList.get(0).getValue2());
        assertThat(dataTransform.transform(dataList.get(0)).get("firstName")).isEqualTo("lei");
    }
}
