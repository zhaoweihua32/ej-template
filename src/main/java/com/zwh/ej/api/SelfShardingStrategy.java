package com.zwh.ej.api;

import com.dangdang.ddframe.job.lite.api.strategy.JobInstance;
import com.dangdang.ddframe.job.lite.api.strategy.JobShardingStrategy;
import com.google.common.collect.ImmutableList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelfShardingStrategy implements JobShardingStrategy {
    @Override
    public Map<JobInstance, List<Integer>> sharding(List<JobInstance> jobInstances, String jobName, int shardingTotalCount) {
        Map<JobInstance, List<Integer>> result = new LinkedHashMap<>(shardingTotalCount, 1);
        int i  = 0;
        for (JobInstance each : jobInstances){
            result.put(each, ImmutableList.of(i++));
        }

        return result;
    }
}
