package com.luolei.pattern.command.ch1;

/**
 * 帮助命令类：具体命令类
 *
 * @author luolei
 * @date 2017-03-30 15:56
 */
public class HelpCommand extends Command {
    private HelpHandler hhObj; //维持对请求接收者的引用

    public HelpCommand() {
        hhObj = new HelpHandler();
    }

    //命令执行方法，将调用请求接收者的业务方法
    public void execute() {
        hhObj.display();
    }
}
