package com.luolei.pattern.interpreter.ch1;

/**
 * And解释：非终结符表达式
 *
 * @author luolei
 * @date 2017-03-30 16:12
 */
public class AndNode extends AbstractNode {
    private AbstractNode left; //And的左表达式
    private AbstractNode right; //And的右表达式

    public AndNode(AbstractNode left, AbstractNode right) {
        this.left = left;
        this.right = right;
    }

    //And表达式解释操作
    public String interpret() {
        return left.interpret() + "再" + right.interpret();
    }
}
