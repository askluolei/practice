package com.luolei.base.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI适用与服务端和客户端都是使用java语言，并且不考虑其他语言使用发布的接口
 * 可以说RMI是一种特殊的RPC实现，只适用于java
 *
 * 定义远程服务接口
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public interface RemoteServiceInterface extends Remote {

    /**
     * 这个RMI接口负责查询目前已经注册的所有用户信息
     * @return
     * @throws RemoteException
     */
    List<UserInfo> queryAllUserinfo() throws RemoteException;
}
