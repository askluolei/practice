package com.luolei.base.thread.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理提供诸如同步控制器，线程池等基本工具外，为了提高开发人员的效率，JDK还为大家准备了一大批好用的容器类，可以大大减少开发工作量。
 *
 * 一些常用的容器：
 * ConcurrentHashMap：这是一个高效的并发HashMap，可以理解为一个线程安全的HashMap
 * CopyOnWriteArrayList：这是一个List，在读多写少的场合，这个List的性能非常好，远远好于Vector
 * ConcurrentLinkedQueue：高效的并发队列，使用链表实现，可以看作一个线程安全的LinkedList
 * BlockingQueue：这是一个接口，JDK内部通过链表，数组等方式实现了这个接口，表示阻塞队列，非常适合用于作为数据共享的通道
 * ConcurrentSkipListMap：跳表的实现，这个一个Map，使用跳表的数据结构进行快速查找
 *
 * 除了以上并发包中的专有数据结构外，java.util下的Vector是线程安全的（虽然性能上和上述专用工具没得比），另外Collections工具类可以帮助我们将任意集合包装成线程安全的集合
 * @author luolei
 * @date 2017-03-13 16:27
 */
public class UnmodifyContainer {
    /**
     * 线程安全的HashMap，使用Collections.synchronizedMap方法包装我们的HashMap，产生的HashMap就是线程安全的
     * 该方法会生成一个名为SynchronizedMap的Map，它使用委托，将自己所有的Map相关功能交给传入的HashMap实现，而自己则主要负责保证线程安全
     *
     * public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m) {
     return new SynchronizedMap<>(m);
     }

     private static class SynchronizedMap<K,V>
     implements Map<K,V>, Serializable {
     private static final long serialVersionUID = 1978198479659022715L;

     private final Map<K,V> m;     // Backing Map
     final Object      mutex;        // Object on which to synchronize

     SynchronizedMap(Map<K,V> m) {
     this.m = Objects.requireNonNull(m);
     mutex = this;
     }

     SynchronizedMap(Map<K,V> m, Object mutex) {
     this.m = m;
     this.mutex = mutex;
     }

     public int size() {
     synchronized (mutex) {return m.size();}
     }

     所有的方法都是使用mutex这个对象进行同步，也就是所有的操作都需要获取mutex对象的锁，在并发级别不是很高的情况下，够用，但是在高并发下，这种做法不可取
     */
    public static Map<String, String> m = Collections.synchronizedMap(new HashMap<>());

    /**
     * 同样的，List也可以通过Collections.synchronizedList保证，来保证线程安全，其实现方式跟上面是一样的
     */
    public static List<String> l = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

    }
}
