package com.ricky.codelab.redis.sample.ch2;

import com.ricky.codelab.redis.sample.pool.ShardedJedisPoolManager;
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
