package com.zwh.ej.boot.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.zwh.ej.api.MySimpleJob;
import com.zwh.ej.boot.job.MySimpleBootJob;
import com.zwh.ej.boot.listener.DistributeOnceEJobListener;
import com.zwh.ej.boot.listener.MybootJobListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EJobConfig {

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(
            @Value("${regCenter.serverAddr}") final String serverList,
            @Value("${regCenter.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }


    @Bean(initMethod = "init")
    @ConditionalOnBean(name = "regCenter")
    public JobScheduler simpleJobScheduler(ZookeeperRegistryCenter regCenter,
                                           final MySimpleBootJob simpleJob,
                                           @Value("${job.mySimpleJob.cron}") final String cron,
                                           @Value("${job.mySimpleJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${job.mySimpleJob.shardingItemParameters}") final String shardingItemParameters,
                                           @Value("${job.mySimpleJob.jobParameter}") final String jobParameter) {
        return new SpringJobScheduler(simpleJob,
                regCenter,
                getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters,jobParameter),
                new DistributeOnceEJobListener(50000,50000),
                new MybootJobListener());
    }




    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters,
                                                         final String jobParameter) {
        JobCoreConfiguration coreConfiguration = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .shardingItemParameters(shardingItemParameters).jobParameter(jobParameter).build();

        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(coreConfiguration , jobClass.getCanonicalName());

        return LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
//        return LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).monitorPort(9888).build();
    }
}
