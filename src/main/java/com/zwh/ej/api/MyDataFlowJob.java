package com.zwh.ej.api;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class MyDataFlowJob implements DataflowJob<String> {

    private static ConcurrentLinkedQueue<String> dataQueue = new ConcurrentLinkedQueue<String>(){{
        add("a");
        add("b");
        add("c");
        add("d");
        add("e");
    }};


    @Override
    public List<String> fetchData(ShardingContext shardingContext) {
        if(dataQueue.size() == 0){
            System.out.println("---------拉取数据为空！");
            return null;
        }
        return Arrays.asList(dataQueue.poll());

    }

    @Override
    public void processData(ShardingContext shardingContext, List<String> list)  {

        System.out.println(list);

//        try {
//            // do biz...
//            TimeUnit.SECONDS.sleep(3);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        shareList.removeAll(list);
//        System.out.println(shareList);

    }
}
