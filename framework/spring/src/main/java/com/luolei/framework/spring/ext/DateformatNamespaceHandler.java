package com.luolei.framework.spring.ext;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * spring 自定义标签
 * 譬如 <dubbo:provider></dubbo:provider>
 * 在spring 解析的时候如果知道这个自定义标签呢？
 * 首先，你要规定 xml的格式 xsd文件 ，就是你自定义的标签支持哪些 元素 属性，哪些是必须的
 * 这些都不复杂，去网上搜一下看一下就知道了，
 * 这里定义了  META-INF/sdf.xsd
 * 支持的标签名为 dateformat 其中支持的属性为 pattern ，且是必填项
 *
 * 定义好了后定义解析过程
 *
 * 通常是 定义一个 NamespaceHandler 继承 NamespaceHandlerSupport
 * 重写其中的init 方法，里面添加具体的解析类
 * 这里的是 DateformatDefinitionParser
 *
 * 这些都定义好了 如果让spring 知道你的定义呢？
 *
 * 在 META-INF/spring.schemas 文件中指定你的 xsd文件
 * 在 META-INF/spring.handlers 文件中指定你的 NamespaceHandler
 *
 * @author 罗雷
 * @date 2017/11/6 0006
 * @time 16:36
 */
public class DateformatNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("dateformat", new DateformatDefinitionParser());
    }
}
