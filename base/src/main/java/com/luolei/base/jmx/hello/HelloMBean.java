package com.luolei.base.jmx.hello;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public interface HelloMBean {

    public String getName();

    public void setName(String name);

    public String getAge();

    public void setAge(String age);

    public void helloWorld();

    public void helloWorld(String str);

    public void getTelephone();
}
