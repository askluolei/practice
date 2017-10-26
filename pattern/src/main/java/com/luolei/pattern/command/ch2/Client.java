package com.luolei.pattern.command.ch2;

/**
 * @author luolei
 * @date 2017-03-30 16:04
 */
public class Client {
    public static void main(String args[]) {
        CalculatorForm form = new CalculatorForm();
        AbstractCommand command;
        command = new ConcreteCommand();
        form.setCommand(command); //向发送者注入命令对象

        form.compute(10);
        form.compute(5);
        form.compute(10);
        form.undo();
    }
}
