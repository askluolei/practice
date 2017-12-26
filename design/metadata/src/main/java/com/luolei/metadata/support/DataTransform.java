package com.luolei.metadata.support;

import com.luolei.metadata.repository.MetaObjectRepository;
import com.luolei.metadata.tables.MetaData;
import com.luolei.metadata.tables.MetaField;
import com.luolei.metadata.tables.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 这个只是测试用的，后面在进行封装
 * @author 罗雷
 * @date 2017/12/26 0026
 * @time 17:51
 */
@Component
public class DataTransform {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MetaObjectRepository objectRepository;

    public DataTransform(MetaObjectRepository objectRepository) {
        this.objectRepository = objectRepository;
    }

    /**
     * 这里先不考虑嵌套的json对象
     * 假设是简单的 key：value 对象，value可以当成字符串
     * @param map
     * @param objectId
     * @return
     */
    public MetaData transform(Map<String, String> map, Long objectId) {
        MetaObject metaObject = objectRepository.findOne(objectId);
        MetaData metaData = null;
        if (Objects.nonNull(metaObject)) {
            metaData = new MetaData();
            metaData.setTenant(metaObject.getTenant());
            metaData.setObject(metaObject);
            Set<MetaField> fields = metaObject.getFields();
            for (MetaField field: fields) {
                String fieldName = field.getFieldName();
                if (map.containsKey(fieldName)) {
                    String valueStr = map.get(fieldName);
//                    String fieldType = field.getDataType();

                    /**
                     * 这里的字段类型，先暂时用 if else
                     * 这里要做的事情
                     *
                     * 1. 判断类型是否正确
                     * 2. 数据检查，自定义数据检查（这个如果动态扩展，是个问题）
                     * 3.将原始字符串设置到 MetaData 中
                     *
                     * 4. 后面还有加速查询的表，待续
                     *
                     * 这里直接做第三步
                     */
                    int fieldNum = field.getFieldNum();
                    metaData.setValue(fieldNum, valueStr);
                }
            }

            /**
             * 处理 MetaData 自己特有的字段
             */
            final String GUID_KEY = "guid";
            if (map.containsKey(GUID_KEY)) {
                metaData.setGuid(Long.parseLong(map.get(GUID_KEY)));
            }
        }
        return metaData;
    }

    public Map<String, String> transform(MetaData data) {
        MetaObject metaObject = data.getObject();
        Set<MetaField> fields = metaObject.getFields();
        Map<String, String> result = new HashMap<>();
        for (MetaField field: fields) {
            String value = data.getValue(field.getFieldNum());
            if (value != null && value.length() > 0) {
                result.put(field.getFieldName(), value);
            }
        }
        return result;
    }

    /**
     * 把参数里面的key 替换成 value0 - value500
     * @param map
     * @param objectId
     * @return
     */
    public Map<String, String> repalceFieldName(Map<String, String> map, Long objectId) {
        Map<String, String> result = new HashMap<>();
        MetaObject metaObject = objectRepository.findOne(objectId);
        if (Objects.nonNull(metaObject)) {
            Set<MetaField> fields = metaObject.getFields();
            for (MetaField field : fields) {
                if (map.containsKey(field.getFieldName())) {
                    result.put("value" + field.getFieldNum(), map.get(field.getFieldName()));
                }
            }
        }
        return result;
    }
}
