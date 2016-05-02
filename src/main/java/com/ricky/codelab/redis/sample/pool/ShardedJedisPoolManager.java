package com.ricky.codelab.redis.sample.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.ricky.codelab.redis.sample.util.PropertyUtils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardedJedisPoolManager {
	private volatile static ShardedJedisPoolManager manager;
	private final ShardedJedisPool shardedJedisPool;

	private ShardedJedisPoolManager() {

		try {
			Properties props = PropertyUtils.load("redis_cluster.properties");
			
			// 创建jedis池配置实例
			JedisPoolConfig config = new JedisPoolConfig();
			
			// 设置池配置项值
			String maxTotal = props.getProperty("redis.pool.maxTotal", "4");
			config.setMaxTotal(Integer.parseInt(maxTotal));
			
			String maxIdle = props.getProperty("redis.pool.maxIdle", "4");
			config.setMaxIdle(Integer.parseInt(maxIdle));
			
			String minIdle = props.getProperty("redis.pool.minIdle", "1");
			config.setMinIdle(Integer.parseInt(minIdle));
			
			String maxWaitMillis = props.getProperty("redis.pool.maxWaitMillis", "1024");
			config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
			
			String testOnBorrow = props.getProperty("redis.pool.testOnBorrow", "true");
			config.setTestOnBorrow("true".equals(testOnBorrow));
			
			String testOnReturn = props.getProperty("redis.pool.testOnReturn", "true");
			config.setTestOnReturn("true".equals(testOnReturn));

			String server = props.getProperty("redis.server");
			if(StringUtils.isEmpty(server)){
				throw new IllegalArgumentException("ShardedJedisPool redis.server is empty!");
			}
			
			String[] host_arr = server.split(",");
			List<JedisShardInfo> list = new ArrayList<JedisShardInfo>(host_arr.length);    
			for(String host : host_arr){
				
				String[] arr = host.split(":");
				System.out.println("init ShardedJedisPool host->"+arr[0]+",port->"+arr[1]);
		        JedisShardInfo jedisShardInfo = new JedisShardInfo(    
		        		arr[0], Integer.parseInt(arr[1]));
		        
		        jedisShardInfo.setPassword("password");
		        
		        list.add(jedisShardInfo);   
			}
	    
	        //根据配置文件,创建shared池实例
			System.out.println("***********init ShardedJedisPool***********");
	        shardedJedisPool = new ShardedJedisPool(config, list);  
			
		} catch (IOException e) {
			throw new IllegalArgumentException("init ShardedJedisPool error", e);
		}
		
	}

	public static ShardedJedisPoolManager getMgr() {
		if (manager == null) {
			synchronized (ShardedJedisPoolManager.class) {
				if (manager == null) {
					manager = new ShardedJedisPoolManager();
				}
			}
		}
		return manager;
	}

	public ShardedJedis getResource() {

		return shardedJedisPool.getResource();
	}

	public void destroy() {
		// when closing your application:
		shardedJedisPool.destroy();
	}
	
	public void close() {
		shardedJedisPool.close();
	}
}
