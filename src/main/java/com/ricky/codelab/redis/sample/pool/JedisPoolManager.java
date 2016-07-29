package com.ricky.codelab.redis.sample.pool;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.ricky.codelab.redis.sample.util.PropertyUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolManager {
	private volatile static JedisPoolManager manager;
	private final JedisPool pool;

	private JedisPoolManager() {

		try {
			//加载redis配置
			Properties props = PropertyUtils.load("redis.properties");
			
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
				throw new IllegalArgumentException("JedisPool redis.server is empty!");
			}

			String[] server_arr = server.split(",");
			if(server_arr.length>1){
				throw new IllegalArgumentException("JedisPool redis.server length > 1");
			}

			String[] arr = server_arr[0].split(":");
			String host = arr[0];
			int port = Integer.parseInt(arr[1]);
			int timeout = Integer.parseInt(props.getProperty("redis.timeout"));
			String password = props.getProperty("redis.password");

			System.out.println("host->"+host+",port->"+port+",timeout="+timeout+",password:"+password);

			pool = new JedisPool(config, host, port, timeout, password);
			
		} catch (IOException e) {
			throw new IllegalArgumentException("init JedisPool error", e);
		}
		
	}

	public static JedisPoolManager getMgr() {
		if (manager == null) {
			synchronized (JedisPoolManager.class) {
				if (manager == null) {
					manager = new JedisPoolManager();
				}
			}
		}
		return manager;
	}

	public Jedis getResource() {
		
		return pool.getResource();
	}

	public void destroy() {
		// when closing your application:
		pool.destroy();
	}
	
	public void close() {
		pool.close();
	}
}
