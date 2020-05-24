package com.zwh.ej.boot.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

/**
 * 每个作业节点都执行
 */
public class MybootJobListener implements ElasticJobListener {
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {

        System.out.println("-----开始任务-----");
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        System.out.println("-----结束任务-----");
    }
}
