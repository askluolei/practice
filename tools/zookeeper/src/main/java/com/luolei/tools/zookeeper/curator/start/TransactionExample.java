package com.luolei.tools.zookeeper.curator.start;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;

import java.util.List;

/**
 * @author 罗雷
 * @date 2017/11/1 0001
 * @time 10:37
 */
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
