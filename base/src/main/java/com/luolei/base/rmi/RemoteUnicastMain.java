package com.luolei.base.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * 由于我们使用LocateRegistry创建了一个“本地RMI注册表”，所以不需要使用rmic命令生成Stub了（注意是“不需要手工生成”而不是“不需要”了），这是因为RMI Sever真实服务的JVM和RMI 注册表的JVM是同一个JVM。
 * 那么RMI Sever真实服务的JVM和RMI注册表的JVM可以是两个不同的JVM吗？当然可以。而且这才是RMI框架灵活性、健壮性的提现。
 * 请注意RemoteUnicastServiceImpl的定义，它继承了UnicastRemoteObject。一般来说RMI Server的实现可以继承两种父类：UnicastRemoteObject和Activatable
 * 前者的意义是，RMI Server真实的服务提供者将工作在“本地JVM”上；后者的意义是，RMI Server的真是的服务提供者，不是在“本地JVM”上运行，而是可以通过“RMI Remote Server 激活”技术，被序列化到“远程JVM”（即远程RMI注册表所在的JVM上），并适时被“远程JVM”加载运行。
 * 再注意一下“Naming.rebind”和“Naming.bind”的区别。前置是指“重绑定”，如果“重绑定”时“RMI 注册表”已经有了这个服务name的存在，则之前所绑定的Remote Object将会被替换；而后者在执行时如果“绑定”时“RMI注册表”已经有这个服务name的存在，则系统会抛出错误。所以除非您有特别的业务要求，那么建议使用rebind方法进行Remote Object绑定。
 * 还要注意registry.rebind和Naming.rebind绑定的区别。前者是使用RMI注册表绑定，所以不需要写完整的RMI URL了；后者是通过java的名称服务进行绑定，由于名称服务不止为RMI框架提供查询服务，所以在绑定是要书写完成的RMI URL。
 *
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class RemoteUnicastMain {

    public static void main(String[] args) throws Exception {
		/*
		 * Locate registry，您可以理解成RMI服务注册表，或者是RMI服务位置仓库。
		 * 主要的作用是维护一个“可以正常提供RMI具体服务的所在位置”。 每一个具体的RMI服务提供者，都会讲自己的Stub注册到Locate
		 * registry中，以表示自己“可以提供服务”
		 *
		 * 有两种方式可以管理Locate registry，一种是通过操作系统的命令行启动注册表；
		 * 另一种是在代码中使用LocateRegistry类。
		 *
		 * LocateRegistry类中有一个createRegistry方法，可以在这台物理机上创建一个“本地RMI注册表”
		 */
        LocateRegistry.createRegistry(1099);

        // 以下是向LocateRegistry注册（绑定/重绑定）RMI Server实现。
        RemoteUnicasServiceImpl remoteService = new RemoteUnicasServiceImpl();
        // 通过java 名字服务技术，可以讲具体的RMI Server实现绑定一个访问路径。注册到LocateRegistry中
        Naming.rebind("rmi://127.0.0.1:1099/queryAllUserinfo", remoteService);

		/*
		 * 在“已经拥有某个可访问的远程RMI注册表”的情况下。 下面这句代码就是向远程注册表注册RMI Server，
		 * 当然远程RMI注册表的JVM-classpath中一定要有这个Server的Stub存在
		 *
		 * （运行在另外一个JVM上的RMI注册表，可能是同一台物理机也可能不是同一台物理机）
		 * Naming.rebind("rmi://192.168.61.1:1099/queryAllUserinfo",
		 * remoteService);
		 */
    }
}
