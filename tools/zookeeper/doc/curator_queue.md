# 队列

转载来源 [并发编程网 – ifeve.com](http://ifeve.com/)

使用Curator也可以简化Ephemeral Node (临时节点)的操作。Curator也提供ZK Recipe的分布式队列实现。 利用ZK的 PERSISTENTSEQUENTIAL节点， 可以保证放入到队列中的项目是按照顺序排队的。 如果单一的消费者从队列中取数据， 那么它是先入先出的，这也是队列的特点。 如果你严格要求顺序，你就的使用单一的消费者，可以使用leader选举只让leader作为唯一的消费者。

但是， 根据Netflix的Curator作者所说， ZooKeeper真心不适合做Queue，或者说ZK没有实现一个好的Queue， 原因有五：
1. ZK有1MB 的传输限制。 实践中ZNode必须相对较小，而队列包含成千上万的消息，非常的大。
2. 如果有很多节点，ZK启动时相当的慢。 而使用queue会导致好多ZNode. 你需要显著增大 initLimit 和 syncLimit.
3. ZNode很大的时候很难清理。Netflix不得不创建了一个专门的程序做这事。
4. 当很大量的包含成千上万的子节点的ZNode时， ZK的性能变得不好
5. ZK的数据库完全放在内存中。 大量的Queue意味着会占用很多的内存空间。

尽管如此， Curator还是创建了各种Queue的实现。 如果Queue的数据量不太多，数据量不太大的情况下，酌情考虑，还是可以使用的。

## DistributedQueue
DistributedQueue是最普通的一种队列。 它设计以下四个类：
* QueueBuilder
* QueueConsumer
* QueueSerializer
* DistributedQueue

创建队列使用QueueBuilder,它也是其它队列的创建类。

```java
public static <T> QueueBuilder<T> builder(CuratorFramework client,
                                          QueueConsumer<T> consumer,
                                          QueueSerializer<T> serializer,
                                          java.lang.String queuePath)
```

```java
QueueBuilder<MessageType>    builder = QueueBuilder.builder(client, consumer, serializer, path);
... more builder method calls as needed ...
DistributedQueue<MessageType queue = builder.build();
```

创建好queue就可以往里面放入数据了：
```java
queue.put(aMessage);
```

QueueSerializer提供了对队列中的对象的序列化和反序列化。
QueueConsumer是消费者， 它可以接收队列的数据。 处理队列中的数据的代码逻辑可以放在QueueConsumer.consumeMessage()中。

正常情况下先将消息从队列中移除，再交给消费者消费。 但这是两个步骤，不是原子的。 可以调用Builder的lockPath()消费者加锁， 当消费者消费数据时持有锁，这样其它消费者不能消费此消息。 如果消费失败或者进程死掉，消息可以交给其它进程。这会带来一点性能的损失。 最好还是单消费者模式使用队列。

栗子
```java
public class DistributedQueueExample {

    private static final String PATH = "/example/queue";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        DistributedQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener((client1, event) -> {
                System.out.println("CuratorEvent: " + event.getType().name());
            });

            client.start();

            QueueConsumer<String> consumer = createQueueConsumer();
            //QueueBuilder 需要指定消费者，序列化类，path
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildQueue();

            queue.start();
            for (int i = 0; i < 10; i++) {
                queue.put(" test-" + i);
                Thread.sleep((long)(1000 * Math.random()));
            }

            Thread.sleep(20000);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    //数据序列化
    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
    }

    //消费者
    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {
            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }
        };
    }
}
```

## DistributedIdQueue
DistributedIdQueue和上面的队列类似， 但是可以为队列中的每一个元素设置一个ID。 可以通过ID把队列中任意的元素移除。 它涉及几个类：
* QueueBuilder
* QueueConsumer
* QueueSerializer
* DistributedQueue

通过下面方法创建：
```java
builder.buildIdQueue()
```

放入元素时：
```java
queue.put(aMessage, messageId);
```

移除元素时：
```java
int numberRemoved = queue.remove(messageId);
```

在这个例子中， 有些元素还没有被消费者消费时就移除了，这样消费者不会收到删除的消息。
```java
public class DistributedIdQueueExample {

    private static final String PATH = "/example/queue";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        DistributedIdQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener((client1, event) -> {
                System.out.println("CuratorEvent: " + event.getType().name());
            });
            client.start();

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildIdQueue();
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put(" test-" + i, "Id" + i);
                Thread.sleep((long)(100 * Math.random()));
                queue.remove("Id" + i);
            }

            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {

        return new QueueConsumer<String>(){

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

        };
    }
}
```

## DistributedPriorityQueue
优先级队列对队列中的元素按照优先级进行排序。 Priority越小， 元素月靠前， 越先被消费掉。 它涉及下面几个类：
* QueueBuilder
* QueueConsumer
* QueueSerializer
* DistributedPriorityQueue

通过builder.buildPriorityQueue(minItemsBeforeRefresh)方法创建。 当优先级队列得到元素增删消息时，它会暂停处理当前的元素队列，然后刷新队列。minItemsBeforeRefresh指定刷新前当前活动的队列的最小数量。 主要设置你的程序可以容忍的不排序的最小值。
放入队列时需要指定优先级：
```java
queue.put(aMessage, priority);
```
例子：

```java
public class DistributedPriorityQueueExample {

    private static final String PATH = "/example/queue";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        DistributedPriorityQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener((client1, event) -> {
                System.out.println("CuratorEvent: " + event.getType().name());
            });
            client.start();

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildPriorityQueue(0);
            queue.start();

            for (int i = 0; i < 10; i++) {
                int priority = (int)(Math.random() * 100);
                System.out.println("test-" + i + " priority:" + priority);
                queue.put("test-" + i, priority);
                Thread.sleep((long)(50 * Math.random()));
            }

            Thread.sleep(20000);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {

        return new QueueConsumer<String>(){

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

        };
    }
}
```

## DistributedDelayQueue
JDK中也有DelayQueue，不知道你是否熟悉。 DistributedDelayQueue也提供了类似的功能， 元素有个delay值， 消费者隔一段时间才能收到元素。 涉及到下面四个类。
* QueueBuilder
* QueueConsumer
* QueueSerializer
* DistributedDelayQueue

通过下面的语句创建：
```java
QueueBuilder<MessageType>    builder = QueueBuilder.builder(client, consumer, serializer, path);
... more builder method calls as needed ...
DistributedDelayQueue<MessageType> queue = builder.buildDelayQueue();
```

放入元素时可以指定delayUntilEpoch：
```java
queue.put(aMessage, delayUntilEpoch);
```

注意delayUntilEpoch不是离现在的一个时间间隔， 比如20毫秒，而是未来的一个时间戳，如 System.currentTimeMillis() + 10秒。 如果delayUntilEpoch的时间已经过去，消息会立刻被消费者接收。

例子：
```java
public class DistributedDelayQueueExample {

    private static final String PATH = "/example/queue";

    private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    public static void main(String[] args) {
        CuratorFramework client = null;
        DistributedDelayQueue<String> queue = null;

        try {
            client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
            client.getCuratorListenable().addListener((client1, event) -> {
                System.out.println("CuratorEvent: " + event.getType().name());
            });
            client.start();

            QueueConsumer<String> consumer = createQueueConsumer();
            QueueBuilder<String> builder = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH);
            queue = builder.buildDelayQueue();
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put("test-" + i, System.currentTimeMillis() + 10000);
            }
            System.out.println(System.currentTimeMillis() + ": already put all items");

            Thread.sleep(20000);
        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>(){

            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {

        return new QueueConsumer<String>(){

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("connection new state: " + newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                System.out.println("consume one message: " + message);
            }

        };
    }
}
```

## SimpleDistributedQueue
前面虽然实现了各种队列，但是你注意到没有，这些队列并没有实现类似JDK一样的接口。 SimpleDistributedQueue提供了和JDK一致性的接口(但是没有实现Queue接口)。 创建很简单：
```java
public SimpleDistributedQueue(CuratorFramework client,
                              String path)
```
增加元素：
```java
public boolean offer(byte[] data) throws Exception
```
删除元素：
```java
public byte[] take() throws Exception
```
另外还提供了其它方法：
```java
public byte[] peek() throws Exception
public byte[] poll(long timeout, TimeUnit unit) throws Exception
public byte[] poll() throws Exception
public byte[] remove() throws Exception
public byte[] element() throws Exception
```

没有add方法， 多了take方法。
take方法在成功返回之前会被阻塞。 而poll在队列为空时直接返回null。