package com.zwh.ej.api;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("------>>>>>>Thread ID: %s, %s,任务总片数: %s, " +
                        "当前分片项: %s.当前参数: %s," +
                        "当前任务名称: %s.当前任务参数 %s",
                Thread.currentThread().getId(),
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()
        ));


//        try {
//            TimeUnit.SECONDS.sleep(10);
//        }catch (Exception e){
//            System.out.println("---");
//        }

        switch (shardingContext.getShardingItem()){
            case 0:
                System.out.println(00000000);
                break;

            case 1:
                System.out.println(11111111);
                break;
            case 2:
                System.out.println(22222222);
                break;
            default:
                System.out.println(99999999);
        }

        switch (shardingContext.getShardingParameter()){
            case "ONE":
                System.out.println("ONE");
                break;

            case "TWO":
                System.out.println("TWO");
                break;
            case "THREE":
                System.out.println("THREE");
                break;
            default:
                System.out.println("FOUR");
        }

    }
}
