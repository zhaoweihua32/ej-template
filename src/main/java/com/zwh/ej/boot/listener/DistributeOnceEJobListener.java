package com.zwh.ej.boot.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;


/**
 * 分布式监听器
 */
public class DistributeOnceEJobListener extends AbstractDistributeOnceElasticJobListener {
    public DistributeOnceEJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {

        System.out.println("---------分布式监听器执行开始--------");
    }

    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
        System.out.println("---------分布式监听器执行结束--------");

    }
}
