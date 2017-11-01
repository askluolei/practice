package com.luolei.tools.zookeeper.curator.cache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 14:07
 */
public class PathCacheExample {

    private static final String PATH = "/example/cache";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        PathChildrenCache childrenCache = null;
        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.start();
            childrenCache = new PathChildrenCache(client, PATH, true);

            //设置监听
            childrenCache.getListenable().addListener((client1, event) -> {
                System.out.println(event.getType() + " : " + event.getData());
            });

            childrenCache.start();

            for (int i = 0; i < 5; i++) {
                Thread.sleep(10000);
                //输出过程中，可以通过zkCli客户端命令 修改 /example/cache目录下面的子目录，数据等操作
                printCache(childrenCache);
            }

            Thread.sleep(20000);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(childrenCache);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static void printCache(PathChildrenCache cache) {
        for (ChildData data : cache.getCurrentData()) {
            System.out.println(data.getPath() + " = " + new String(data.getData()));
        }
    }
}
