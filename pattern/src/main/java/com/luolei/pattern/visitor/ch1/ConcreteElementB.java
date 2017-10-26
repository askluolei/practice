package com.luolei.pattern.visitor.ch1;

/**
 * @author luolei
 * @date 2017-03-31 15:54
 */
public class ConcreteElementB implements Element
{
    public void accept(Visitor visitor)
    {
        visitor.visit(this);
    }

    public void operationA()
    {
        //业务方法
    }
}
