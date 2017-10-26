package com.luolei.base.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * UnicastRemoteObject和Activatable  接口的实现是继承了UnicastRemoteObject
 * UnicastRemoteObject：实现是在服务提供者实现的
 * Activatable：通过RMI“活化”模式，将Remote Service的真实提供者移植到RMI Registry注册表所在的JVM上。
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class RemoteUnicasServiceImpl extends UnicastRemoteObject implements RemoteServiceInterface {

    private static final long serialVersionUID = 7896130329059374012L;

    protected RemoteUnicasServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<UserInfo> queryAllUserinfo() throws RemoteException {
        List<UserInfo> users = new ArrayList<UserInfo>();

        UserInfo user1 = new UserInfo();
        user1.setUserAge(21);
        user1.setUserDesc("userDesc1");
        user1.setUserName("userName1");
        user1.setUserSex(true);
        users.add(user1);

        UserInfo user2 = new UserInfo();
        user2.setUserAge(21);
        user2.setUserDesc("userDesc2");
        user2.setUserName("userName2");
        user2.setUserSex(false);
        users.add(user2);
        return users;
    }

}
