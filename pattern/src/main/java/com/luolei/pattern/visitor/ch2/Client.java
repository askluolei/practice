package com.luolei.pattern.visitor.ch2;

/**
 * 如果要在系统中增加一种新的访问者，无须修改源代码，只要增加一个新的具体访问者类即可，在该具体访问者中封装了新的操作元素对象的方法。从增加新的访问者的角度来看，访问者模式符合“开闭原则”。
 如果要在系统中增加一种新的具体元素，例如增加一种新的员工类型为“退休人员”，由于原有系统并未提供相应的访问接口（在抽象访问者中没有声明任何访问“退休人员”的方法），因此必须对原有系统进行修改，在原有的抽象访问者类和具体访问者类中增加相应的访问方法。从增加新的元素的角度来看，访问者模式违背了“开闭原则”。
 综上所述，访问者模式与抽象工厂模式类似，对“开闭原则”的支持具有倾斜性，可以很方便地添加新的访问者，但是添加新的元素较为麻烦。

 * @author luolei
 * @date 2017-03-31 16:02
 */
public class Client {
    public static void main(String args[]) {
        EmployeeList list = new EmployeeList();
        Employee fte1, fte2, fte3, pte1, pte2;

        fte1 = new FulltimeEmployee("张无忌", 3200.00, 45);
        fte2 = new FulltimeEmployee("杨过", 2000.00, 40);
        fte3 = new FulltimeEmployee("段誉", 2400.00, 38);
        pte1 = new ParttimeEmployee("洪七公", 80.00, 20);
        pte2 = new ParttimeEmployee("郭靖", 60.00, 18);

        list.addEmployee(fte1);
        list.addEmployee(fte2);
        list.addEmployee(fte3);
        list.addEmployee(pte1);
        list.addEmployee(pte2);

        Department dep;
        dep = (Department) XMLUtil.getBean();
        list.accept(dep);

    }
}
