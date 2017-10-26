package com.luolei.base.rmi;

import java.rmi.Naming;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/26 23:18
 */
public class RemoteClient {

    private static final Logger LOGGER = Logger.getLogger(RemoteClient.class.getName());

    public static void main(String[] args) throws Exception {
        // 您看，这里使用的是java名称服务技术进行的RMI接口查找。
        RemoteServiceInterface remoteServiceInterface = (RemoteServiceInterface)Naming.lookup("rmi://127.0.0.1:1099/queryAllUserinfo");
        List<UserInfo> users = remoteServiceInterface.queryAllUserinfo();

        RemoteClient.LOGGER.info("users.size() = " +users.size());
    }
}
