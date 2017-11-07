package com.luolei.framework.spring.ext;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;

/**
 * 这里是具体的解析类，
 * 将返回一个bean，类型为 SimpleDateFormat
 * 从配置的xml中获取 pattern 属性
 * 丢给 构造参数
 *
 * @author 罗雷
 * @date 2017/11/6 0006
 * @time 16:37
 */
public class DateformatDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return SimpleDateFormat.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String pattern = element.getAttribute("pattern");
        builder.addConstructorArgValue(pattern);
    }
}
