package com.zwh.ej.api;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class EJobBootstrap {
    public static void main(String[] args) {
        new JobScheduler(createRegistryCenter(), createSimpleJobConfiguration(), createRdbEventConfig()).init();
//        new JobScheduler(createRegistryCenter(), createSimpleJobConfiguration()).init();
//
//        new JobScheduler(createRegistryCenter(), createDataflowJobConfiguration()).init();
//
//        new JobScheduler(createRegistryCenter(), createScriptJobConfiguration()).init();
    }

    /**
     * 数据库的事件配置
     */
    public static JobEventConfiguration createRdbEventConfig(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/elastic_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("root");
        dataSource.setPassword("Zwh12345");
        return new JobEventRdbConfiguration(dataSource);
    }


    /**
     * 注册中心的信息
     */
    private static CoordinatorRegistryCenter createRegistryCenter(){
        CoordinatorRegistryCenter registryCenter =
                new ZookeeperRegistryCenter(new ZookeeperConfiguration("127.0.0.1:2181", "elastic-job"));
        registryCenter.init();
        return registryCenter;
    }


    /**
     * simple type
     *
     *
     * core -> type -> job
     */
    private static LiteJobConfiguration createSimpleJobConfiguration(){
        JobCoreConfiguration coreConfiguration =
                JobCoreConfiguration.newBuilder("mySimpleEJob","0/6 * * * * ?",3)
                        .shardingItemParameters("0=ONE, 1=TWO, 2=THREE, 3=FOUR").failover(true).build();

        SimpleJobConfiguration simpleJobConfiguration =
                new SimpleJobConfiguration(coreConfiguration, MySimpleJob.class.getCanonicalName());

        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).monitorPort(8899).build();
//        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
//                .overwrite(true)
//                .jobShardingStrategyClass(SelfShardingStrategy.class.getCanonicalName())
//                .build();

        return liteJobConfiguration;
    }


    /**
     * dataflow type
     */
    private static LiteJobConfiguration createDataflowJobConfiguration(){
        JobCoreConfiguration coreConfiguration =
                JobCoreConfiguration.newBuilder("dataflowEJob","0/5 * * * * ?", 2).failover(true).build();

        DataflowJobConfiguration dataflowJobConfiguration =
                new DataflowJobConfiguration(coreConfiguration, MyDataFlowJob.class.getCanonicalName(), true);

        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(dataflowJobConfiguration).overwrite(true).build();

        return liteJobConfiguration;
    }


    /**
     * script type
     */
    private static LiteJobConfiguration createScriptJobConfiguration(){
        JobCoreConfiguration coreConfiguration =
                JobCoreConfiguration.newBuilder("scriptEJob1","0/2 * * * * ?", 1).build();

        ScriptJobConfiguration scriptJobConfiguration =
                new ScriptJobConfiguration(coreConfiguration, "/Users/zhaoweihua/a_my_project/ej-template/src/main/resources/script_job.sh");

        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(scriptJobConfiguration).build();
        return liteJobConfiguration;
    }


}
