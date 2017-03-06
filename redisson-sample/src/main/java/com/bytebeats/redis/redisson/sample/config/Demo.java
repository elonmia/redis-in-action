package com.bytebeats.redis.redisson.sample.config;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

/**
 * https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95
 *
 */
public class Demo {

    public static void main( String[] args ) {

    }

    public void configYAML() throws IOException {

        //文件中的字段名称必须与Config对象里的字段名称相符
        Config config = Config.fromYAML(new File("redisson-config.yaml"));
        RedissonClient redisson = Redisson.create(config);

        redisson.shutdown();
    }

    public void configJSON() throws IOException {

        //文件中的字段名称必须与Config对象里的字段名称相符
        Config config = Config.fromJSON(new File("redisson-config.json"));
        RedissonClient redisson = Redisson.create(config);

        redisson.shutdown();
    }

    public void configCluster(){
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
                .addNodeAddress("127.0.0.1:7000", "127.0.0.1:7001")
                .addNodeAddress("127.0.0.1:7002");

        RedissonClient redisson = Redisson.create(config);

        redisson.shutdown();
    }

    public void configSingle(){

        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");

        // 2. Create Redisson instance
        RedissonClient redisson = Redisson.create(config);

        // 3. Get object you need
        RMap<String, String> map = redisson.getMap("myMap");

        redisson.shutdown();
    }
}
