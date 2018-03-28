package com.luolei.myplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 测试插件
 *
 * @author 罗雷
 * @date 2018/3/26 0026
 * @time 11:04
 */
@Mojo(name = "hello")
public class HelloMojo extends AbstractMojo {

    @Parameter(defaultValue = "world")
    private String greeting;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("hello " + greeting);
    }
}
