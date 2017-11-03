package com.luolei.tools.zookeeper.my;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;

/**
 * @author 罗雷
 * @date 2017/11/3 0003
 * @time 16:02
 */
public class NodeCreateTry {

    private static final String path = "/path/pp1";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();
        server.start();

        CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3) );
        client.start();

        BackgroundCallback callback = (curatorFramework, curatorEvent) -> {
            CuratorEventType type = curatorEvent.getType();
            switch (type) {
                case CREATE:
                    System.out.println("------ 创建成功 --------");
                    break;
                case EXISTS:
                    System.out.println("------ 节点已经存在 --------");
                    break;
                default:
                    System.out.println("其他类型的事件:" + type);
            }
            System.out.println("resultCode:" + curatorEvent.getResultCode());
            System.out.println("watchedEvent:" + curatorEvent.getWatchedEvent());
        };

//        client.create().withMode(CreateMode.EPHEMERAL).inBackground(callback).forPath(path, "path".getBytes());
//        client.create().withMode(CreateMode.EPHEMERAL).inBackground(callback).forPath(path, "path".getBytes());

        String result = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        System.out.println(ZKPaths.getNodeFromPath(result));

        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------异常啦--------");
        }

        Thread.sleep(2000);
        CloseableUtils.closeQuietly(client);
        CloseableUtils.closeQuietly(server);
    }
}
