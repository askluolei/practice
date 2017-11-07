package com.luolei.ignite.start;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;

import java.util.concurrent.locks.Lock;

/**
 * @author 罗雷
 * @date 2017/11/7 0007
 * @time 17:11
 */
public class FirstDataApplication {

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCacheName");
            /**
             * 这里测试 put get 操作
             */
            for (int i = 0; i < 10; i++) {
                cache.put(i, Integer.toString(i));
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
            }

            /**
             * 这里测试 原子化操作
             */
            IgniteCache<String, Integer> atomicCache = ignite.getOrCreateCache("atomicCacheName");
            Integer oldVal = atomicCache.getAndPutIfAbsent("Hello", 11);
            boolean success = atomicCache.putIfAbsent("World", 22);
            oldVal = atomicCache.getAndReplace("Hello", 11);
            success = atomicCache.replace("World", 22);
            success = atomicCache.replace("World", 2, 22);
            success = atomicCache.remove("Hello", 1);


            /**
             * 编程式事务
             */
            try (Transaction tx = ignite.transactions().txStart()) {
                Integer hello = atomicCache.get("Hello");
                if (hello == 1) {
                    atomicCache.put("Hello", 11);
                }
                atomicCache.put("World", 22);
                tx.commit();
            }

            /**
             * 给 key 加锁进行操作
             * 这里注意一下 CacheAtomicityMode.ATOMIC 不支持 锁, 需要配置成 CacheAtomicityMode.TRANSACTIONAL
             * 在配置文件里面是
             * <property name="atomicityMode" value="TRANSACTIONAL"/>
             * 但是这里还是不能改
             * 因为，服务端配置要跟这里一样才行
             */
//            Lock lock = atomicCache.lock("Hello");
//            lock.lock();
//            try {
//                atomicCache.put("Hello", 11);
//                atomicCache.put("World", 22);
//            }
//            finally {
//                lock.unlock();
//            }

        }
    }
}
