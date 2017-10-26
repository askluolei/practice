package com.luolei.pattern.command.ch2;

/**
 * 抽象命令类
 *
 * @author luolei
 * @date 2017-03-30 16:03
 */
public abstract class AbstractCommand {
    public abstract int execute(int value); //声明命令执行方法execute()

    public abstract int undo(); //声明撤销方法undo()
}
