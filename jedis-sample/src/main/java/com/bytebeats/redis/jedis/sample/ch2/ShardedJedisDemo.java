package com.bytebeats.redis.jedis.sample.ch2;

import com.bytebeats.redis.jedis.sample.pool.ShardedJedisPoolManager;
import redis.clients.jedis.ShardedJedis;

public class ShardedJedisDemo {

	public static void main(String[] args) {
		
		ShardedJedis jedis = null;
		try {
			jedis = ShardedJedisPoolManager.getMgr().getResource();
			jedis.set("z", "bar");
			
		} finally {
			jedis.close();
		}
		ShardedJedisPoolManager.getMgr().destroy();
	}

}
