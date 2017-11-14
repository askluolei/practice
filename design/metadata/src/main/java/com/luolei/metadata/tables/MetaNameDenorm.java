package com.luolei.metadata.tables;

import lombok.Getter;
import lombok.Setter;

/**
 * 是一个简单的数据表
 * 存储对象id 和 这个对象的实例的名字
 * 主要让一些仅需获取名字的查询调用
 * 从而让一些简单的查询无需查询规模庞大的 data 表
 * ？？？
 * 还需要思考
 *
 * @author 罗雷
 * @date 2017/11/14 0014
 * @time 20:07
 */
@Getter
@Setter
public class MetaNameDenorm {

    private Long objId;
}
