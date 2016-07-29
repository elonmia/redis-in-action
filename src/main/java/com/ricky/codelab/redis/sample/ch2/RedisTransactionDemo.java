package com.ricky.codelab.redis.sample.ch2;

import java.io.IOException;

import com.ricky.codelab.redis.sample.pool.JedisPoolManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisTransactionDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Jedis jedis = null;
		try {
			jedis = JedisPoolManager.getMgr().getResource();
			jedis.auth("password");
	        
			Transaction t = jedis.multi();
			t.set("foo", "bar");
			t.exec();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		/// ... when closing your application:
		JedisPoolManager.getMgr().destroy();
	}

}
