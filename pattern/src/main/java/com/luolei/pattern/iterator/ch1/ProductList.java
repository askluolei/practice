package com.luolei.pattern.iterator.ch1;

import java.util.List;

/**
 * 商品数据类：具体聚合类
 *
 * @author luolei
 * @date 2017-03-30 19:03
 */
public class ProductList extends AbstractObjectList {
    public ProductList(List products) {
        super(products);
    }

    //实现创建迭代器对象的具体工厂方法
    public AbstractIterator createIterator() {
        return new ProductIterator(this);
    }
}
