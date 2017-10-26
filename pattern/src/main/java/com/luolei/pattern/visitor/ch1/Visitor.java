package com.luolei.pattern.visitor.ch1;

/**
 * @author luolei
 * @date 2017-03-31 15:53
 */
public abstract class Visitor
{
    public abstract void visit(ConcreteElementA elementA);
    public abstract void visit(ConcreteElementB elementB);
    public void visit(ConcreteElementC elementC)
    {
        //元素ConcreteElementC操作代码
    }
}
