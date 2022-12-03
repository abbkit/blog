package com.abbkit.project;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BfTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    public Object bfReserve(String key, float errorRate, long capacity, boolean expansion){
        byte[][] valueBytes = new byte[5][];
        valueBytes[0]= String.valueOf(key).getBytes();
        valueBytes[1]= String.valueOf(errorRate).getBytes();
        valueBytes[2] = String.valueOf(capacity).getBytes();
        if (expansion){
            valueBytes[3] = "EXPANSION".getBytes();
            valueBytes[4] = String.valueOf(2).getBytes();
        }else{
            valueBytes[3] = "NONSCALING".getBytes();
        }
        return redisTemplate.execute((RedisCallback) connection -> connection.execute("BF.RESERVE", valueBytes));
    }


    @Test
    public void test(){

        String bfName="bf001";

        // 配置错误率和存储空间
//        bfReserve(bfName, (float) 0.001, 5000,false);

        RBloomFilter<String> bloom = redissonClient.getBloomFilter(bfName);
        // 初始化布隆过滤器；  大小:100000，误判率:0.01
        bloom.tryInit(5000l, 0.01);
        // 新增10万条数据
        for(int i=0;i<10;i++) {
            bloom.add("name" + i);
        }
        // 判断不存在于布隆过滤器中的元素
        List<String> notExistList = new ArrayList<>();
        for(int i=0;i<10;i++) {
            String str = "name" + i;
            boolean notExist = bloom.contains(str);
            if (notExist) {
                notExistList.add(str);
            }
        }
        if (notExistList!=null && notExistList.size() > 0 ) {
            System.out.println("误判次数:"+notExistList.size());
        }
        log.info("OK");

    }


}
