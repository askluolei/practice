package com.luolei.pattern.command.ch1;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * @author luolei
 * @date 2017-03-30 15:58
 */
public class XMLUtil {
    //该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象，可以通过参数的不同返回不同类名节点所对应的实例
    public static Object getBean(int i) {
        try {
            //创建文档对象
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            Document doc;
            doc = builder.parse(XMLUtil.class.getResourceAsStream("/command/config.xml"));

            //获取包含类名的文本节点
            NodeList nl = doc.getElementsByTagName("className");
            Node classNode = null;
            if (0 == i) {
                classNode = nl.item(0).getFirstChild();
            } else {
                classNode = nl.item(1).getFirstChild();
            }

            String cName = classNode.getNodeValue();

            //通过类名生成实例对象并将其返回
            Class c = Class.forName(cName);
            Object obj = c.newInstance();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
