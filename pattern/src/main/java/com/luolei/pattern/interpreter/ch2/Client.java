package com.luolei.pattern.interpreter.ch2;

/**
 * 其中关键词LOOP表示“循环”，后面的数字表示循环次数；PRINT表示“打印”，后面的字符串表示打印的内容；SPACE表示“空格”；BREAK表示“换行”；END表示“循环结束”。
 * 每一个关键词对应一条命令，计算机程序将根据关键词执行相应的处理操作。
 * @author luolei
 * @date 2017-03-30 16:23
 */
public class Client {
    public static void main(String[] args) {
        String text = "LOOP 2 PRINT 杨过 SPACE SPACE PRINT 小龙女 BREAK END PRINT 郭靖 SPACE SPACE PRINT 黄蓉";
        Context context = new Context(text);

        Node node = new ExpressionNode();
        node.interpret(context);
        node.execute();
    }
}
