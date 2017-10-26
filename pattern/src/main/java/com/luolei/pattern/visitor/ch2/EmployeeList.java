package com.luolei.pattern.visitor.ch2;

import java.util.ArrayList;

/**
 * 员工列表类：对象结构
 *
 * @author luolei
 * @date 2017-03-31 16:00
 */
public class EmployeeList {
    //定义一个集合用于存储员工对象
    private ArrayList<Employee> list = new ArrayList<Employee>();

    public void addEmployee(Employee employee) {
        list.add(employee);
    }

    //遍历访问员工集合中的每一个员工对象
    public void accept(Department handler) {
        for (Employee obj : list) {
            obj.accept(handler);
        }
    }
}
