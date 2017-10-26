package com.luolei.pattern.interpreter.ch1;

/**
 * 方向解释：终结符表达式
 *
 * @author luolei
 * @date 2017-03-30 16:13
 */
public class DirectionNode extends AbstractNode {
    private String direction;

    public DirectionNode(String direction) {
        this.direction = direction;
    }

    //方向表达式的解释操作
    public String interpret() {
        if (direction.equalsIgnoreCase("up")) {
            return "向上";
        } else if (direction.equalsIgnoreCase("down")) {
            return "向下";
        } else if (direction.equalsIgnoreCase("left")) {
            return "向左";
        } else if (direction.equalsIgnoreCase("right")) {
            return "向右";
        } else {
            return "无效指令";
        }
    }
}
