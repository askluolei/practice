# Curator框架应用
`Curator`框架提供了一套高级的API， 简化了ZooKeeper的操作。 它增加了很多使用ZooKeeper开发的特性，可以处理ZooKeeper集群复杂的连接管理和重试机制。 这些特性包括：

* 自动化的连接管理: 重新建立到ZooKeeper的连接和重试机制存在一些潜在的错误case。 Curator帮助你处理这些事情，对你来说是透明的。
* 清理API:
    * 简化了原生的ZooKeeper的方法，事件等
    * 提供了一个现代的流式接口
* 提供了Recipes实现： 基于这些Recipes可以创建很多复杂的分布式应用

`Curator`框架通过`CuratorFrameworkFactory`以工厂模式和`builder`模式创建`CuratorFramework`实 例。 `CuratorFramework`实例都是线程安全的，你应该在你的应用中共享同一个`CuratorFramework`实例.

工厂方法newClient()提供了一个简单方式创建实例。 而Builder提供了更多的参数控制。一旦你创建了一个CuratorFramework实例，你必须调用它的start()启动，在应用退出时调用close()方法关闭.

```java
package com.luolei.tools.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * @author 罗雷
 * @date 2017/11/1 9:08
 */
public class CreateClientExample {

    private static final String PATH = "/example/basic";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        try {
            client = createSimple(connectString);
            client.start();
            client.create().creatingParentsIfNeeded().forPath(PATH, "test".getBytes());
            CloseableUtils.closeQuietly(client);

            client = createWithOptions(connectString, new ExponentialBackoffRetry(1000, 3), 1000, 1000);
            client.start();
            System.out.println(new String(client.getData().forPath(PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    public static CuratorFramework createSimple(String connectionString) {
        // these are reasonable arguments for the ExponentialBackoffRetry.
        // The first retry will wait 1 second - the second will wait up to 2 seconds - the
        // third will wait up to 4 seconds.
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // The simplest way to get a CuratorFramework instance. This will use default values.
        // The only required arguments are the connection string and the retry policy
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc details
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                // etc. etc.
                .build();
    }

}

```

Curator框架提供了一种流式接口。 操作通过builder串联起来， 这样方法调用类似语句一样。

```java
client.create().forPath("/head", new byte[0]);
client.delete().inBackground().forPath("/head");
client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child", new byte[0]);
client.getData().watched().inBackground().forPath("/test");
```

`CuratorFramework`提供的方法：

|方法名|描述|
|------|---|
|create()|开始创建操作， 可以调用额外的方法(比如方式mode 或者后台执行background) 并在最后调用forPath()指定要操作的ZNode|
|delete()|开始删除操作. 可以调用额外的方法(版本或者后台处理version or background)并在最后调用forPath()指定要操作的ZNode|
|checkExists()|开始检查ZNode是否存在的操作. 可以调用额外的方法(监控或者后台处理)并在最后调用forPath()指定要操作的ZNode|
|getData()|开始获得ZNode节点数据的操作. 可以调用额外的方法(监控、后台处理或者获取状态watch, background or get stat) 并在最后调用forPath()指定要操作的ZNode|
|setData()|开始设置ZNode节点数据的操作. 可以调用额外的方法(版本或者后台处理) 并在最后调用forPath()指定要操作的ZNode|
|getChildren()|开始获得ZNode的子节点列表。 以调用额外的方法(监控、后台处理或者获取状态watch, background or get stat) 并在最后调用forPath()指定要操作的ZNode|
|inTransaction()|开始是原子ZooKeeper事务. 可以复合create, setData, check, and/or delete 等操作然后调用commit()作为一个原子操作提交|


后台操作的通知和监控可以通过`ClientListener`接口发布. 你可以在`CuratorFramework`实例上通过addListener()注册listener, Listener实现了下面的方法:
* eventReceived() 一个后台操作完成或者一个监控被触发

事件类型以及事件的方法如下：

|Event Type|Event Methods|
|----------|-------------|
|CREATE|getResultCode() and getPath()|
|DELETE|getResultCode() and getPath()|
|EXISTS|getResultCode(), getPath() and getStat()|
|GETDATA|getResultCode(), getPath(), getStat() and getData()|
|SETDATA|getResultCode(), getPath() and getStat()|
|CHILDREN|getResultCode(), getPath(), getStat(), getChildren()|
|WATCHED|getWatchedEvent()|

还可以通过 `ConnectionStateListener` 接口监控连接的状态。 强烈推荐你增加这个监控器。

你可以使用命名空间Namespace避免多个应用的节点的名称冲突。 CuratorFramework提供了命名空间的概念，这样CuratorFramework会为它的API调用的path加上命名空间：
```java
CuratorFramework    client = CuratorFrameworkFactory.builder().namespace("MyApp") ... build();
 ...
client.create().forPath("/test", data);
// node was actually written to: "/MyApp/test"
```

Curator还提供了临时的CuratorFramework： CuratorTempFramework， 一定时间不活动后连接会被关闭
创建builder时不是调用build()而是调用buildTemp()。 3分钟不活动连接就被关闭，你也可以指定不活动的时间。 它只提供了下面几个方法：
```java
public void     close();
    public CuratorTransaction inTransaction() throws Exception;
    public TempGetDataBuilder getData() throws Exception;
```

## 操作方法
上面的表格列出了CuratorFramework可以用的操作。 下面就是一个例子：

```java
public class CrudExample {

    public static void main(String[] args) {

    }

    public static void create(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个持久节点，附带数据
        client.create().forPath(path, payload);
    }

    public static void createEphemeral(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个临时节点，附带数据
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
    }

    public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload) throws Exception {
        //创建一个临时有序节点，附带数据
        //使用 curator 的保护模式 ACL 认证
        return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
    }

    public static void setData(CuratorFramework client, String path, byte[] payload) throws Exception {
        // 设置数据
        client.setData().forPath(path, payload);
    }

    public static void setDataAsync(CuratorFramework client, String path, byte[] payload) throws Exception {
        //异步设置数据，并且设置监听
        CuratorListener listener = (client1, event) -> {
            //这里处理事件
        };
        client.getCuratorListenable().addListener(listener);
        client.setData().inBackground().forPath(path, payload);
    }

    public static void setDataAsyncWithCallback(CuratorFramework client, BackgroundCallback callback, String path, byte[] payload) throws Exception {
        //异步设置数据，并且添加回调方法
        client.setData().inBackground(callback).forPath(path, payload);
    }

    public static void delete(CuratorFramework client, String path) throws Exception {
        //删除 path
        client.delete().forPath(path);
    }

    public static void guaranteedDelete(CuratorFramework client, String path) throws Exception {
        //保证 删除给定的 path
        client.delete().guaranteed().forPath(path);
    }

    public static List<String> watchedGetChildren(CuratorFramework client, String path) throws Exception {
        //获取子节点，并且在给定path上设置一个 监听，监听通知将通过 CuratorListener 传回
        return client.getChildren().watched().forPath(path);
    }

    public static List<String> watchedGetChildren(CuratorFramework client, String path, Watcher watcher) throws Exception {
        //获取子节点，并且在给定path上设置一个给定  watcher
        return client.getChildren().usingWatcher(watcher).forPath(path);
    }
}
```


## 事务
上面也提到， CuratorFramework提供了事务的概念，可以将一组操作放在一个原子事务中。 什么叫事务？ 事务是原子的， 一组操作要么都成功，要么都失败。

下面的例子演示了事务的操作：

```java
public class TransactionExample {

    public static void main(String[] args) {

    }

    public static List<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        List<CuratorTransactionResult> transactionResults = client.transaction().forOperations(
                client.transactionOp().create().forPath("/a/path", "some data".getBytes()),
                client.transactionOp().setData().forPath("/another/path", "other data".getBytes()),
                client.transactionOp().delete().forPath("/yet/another/path")
        );
        for (CuratorTransactionResult result : transactionResults) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        return transactionResults;
    }

}
```
