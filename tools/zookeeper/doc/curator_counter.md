# 计数器
计数器是用来计数的, 利用ZooKeeper可以实现一个集群共享的计数器。 只要使用相同的path就可以得到最新的计数器值， 这是由ZooKeeper的一致性保证的。Curator有两个计数器， 一个是用int来计数，一个用long来计数。

## SharedCount
这个类使用int类型来计数。 主要涉及三个类。
* SharedCount
* SharedCountReader
* SharedCountListener

SharedCount代表计数器， 可以为它增加一个SharedCountListener，当计数器改变时此Listener可以监听到改变的事件，而SharedCountReader可以读取到最新的值， 包括字面值和带版本信息的值VersionedValue。

例子代码：
```java
public class SharedCounterExample {

    private static final String PATH = "/example/counter";
    private static final int QTY = 50;
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        final Random rand = new Random();
        CuratorFramework client = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.start();

            SharedCount sharedCount = new SharedCount(client, PATH, 0);
            sharedCount.addListener(new SharedCountListener() {
                @Override
                public void countHasChanged(SharedCountReader sharedCount, int newCount) throws Exception {
                    System.out.println("Counter's value is changed to " + newCount);
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    System.out.println("State changed: " + newState.toString());
                }
            });

            sharedCount.start();

            List<SharedCount> examples = Lists.newArrayList();
            ExecutorService service = Executors.newFixedThreadPool(QTY);

            for (int i = 0; i < QTY; ++i) {
                final SharedCount count = new SharedCount(client, PATH, 0);
                examples.add(count);
                Callable<Void> task = () -> {
                    count.start();
                    Thread.sleep(rand.nextInt(10000));
                    System.out.println("Increment:" + count.trySetCount(count.getVersionedValue(), count.getCount() + rand.nextInt(10)));
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);

            for (int i = 0; i < QTY; ++i) {
                examples.get(i).close();
            }
            sharedCount.close();

        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

```

在这个例子中，我们使用baseCount来监听计数值(addListener方法)。 任意的SharedCount， 只要使用相同的path，都可以得到这个计数值。 然后我们使用5个线程为计数值增加一个10以内的随机数。
```java
count.trySetCount(count.getVersionedValue(), count.getCount() + rand.nextInt(10))
```
这里我们使用trySetCount去设置计数器。 第一个参数提供当前的VersionedValue,如果期间其它client更新了此计数值， 你的更新可能不成功， 但是这时你的client更新了最新的值，所以失败了你可以尝试再更新一次。 而setCount是强制更新计数器的值。
注意计数器必须start,使用完之后必须调用close关闭它。
在这里再重复一遍前面讲到的， 强烈推荐你监控ConnectionStateListener， 尽管我们的有些例子没有监控它。 在本例中SharedCountListener扩展了ConnectionStateListener。 这一条针对所有的Curator recipes都适用，后面的文章中就不专门提示了。

## DistributedAtomicLong
再看一个Long类型的计数器。 除了计数的范围比SharedCount大了之外， 它首先尝试使用乐观锁的方式设置计数器， 如果不成功(比如期间计数器已经被其它client更新了)， 它使用InterProcessMutex方式来更新计数值。

此计数器有一系列的操作：
* get(): 获取当前值
* increment()： 加一
* decrement(): 减一
* add()： 增加特定的值
* subtract(): 减去特定的值
* trySet(): 尝试设置计数值
* forceSet(): 强制设置计数值

你必须检查返回结果的succeeded()， 它代表此操作是否成功。 如果操作成功， preValue()代表操作前的值， postValue()代表操作后的值。
我们下面的例子中使用5个线程对计数器进行加一操作，如果成功，将操作前后的值打印出来。
```java
public class DistributedAtomicLongExample {

    private static final int QTY = 5;
    private static final String PATH = "/examples/counter";
    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.start();

            List<DistributedAtomicLong> examples = Lists.newArrayList();
            ExecutorService service = Executors.newFixedThreadPool(QTY);

            for (int i = 0; i < QTY; ++i) {
                final DistributedAtomicLong count = new DistributedAtomicLong(client, PATH, new RetryNTimes(10, 10));

                examples.add(count);
                Callable<Void> task = () -> {
                    try {
                        //Thread.sleep(rand.nextInt(1000));
                        AtomicValue<Long> value = count.increment();
                        //AtomicValue<Long> value = count.decrement();
                        //AtomicValue<Long> value = count.add((long)rand.nextInt(20));
                        System.out.println("succeed: " + value.succeeded());
                        if (value.succeeded())
                            System.out.println("Increment: from " + value.preValue() + " to " + value.postValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}

```

