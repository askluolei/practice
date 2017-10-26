package com.luolei.pattern.command.ch2;

/**
 * 计算器界面类：请求发送者
 *
 * @author luolei
 * @date 2017-03-30 16:03
 */
public class CalculatorForm {
    private AbstractCommand command;

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }

    //调用命令对象的execute()方法执行运算
    public void compute(int value) {
        int i = command.execute(value);
        System.out.println("执行运算，运算结果为：" + i);
    }

    //调用命令对象的undo()方法执行撤销
    public void undo() {
        int i = command.undo();
        System.out.println("执行撤销，运算结果为：" + i);
    }
}
