package com.luolei.tools.zookeeper.curator.start;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;

import java.util.Collection;
import java.util.List;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 10:37
 */
public class TransactionExample {

    public static void main(String[] args) {

    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        Collection<CuratorTransactionResult> results = client.inTransaction()
                .create().forPath("/a/path", "some data".getBytes())
                .and()
                .setData().forPath("/another/path", "other data".getBytes())
                .and()
                .delete().forPath("/yet/another/path")
                .and()
                .commit();

        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        return results;
    }

}
