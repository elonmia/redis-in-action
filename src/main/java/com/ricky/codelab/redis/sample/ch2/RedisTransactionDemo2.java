package com.ricky.codelab.redis.sample.ch2;

import com.ricky.codelab.redis.sample.pool.JedisPoolManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;

public class RedisTransactionDemo2 {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Jedis jedis = null;
		try {
			//首先用redis-cli连接redis服务器设置foo=bar,you=you
			jedis = JedisPoolManager.getMgr().getResource();
			jedis.auth("password");
			jedis.watch("foo", "you");
			Transaction t = jedis.multi();
			t.set("foo", "bar");//断点到这里，先不执行。然后用redis-cli连接redis，修改foo的值为444，修改完后，该断点往下执行
			t.set("you", "you1");
			t.exec();//能执行，不会抛异常，但是foo=444，you=you。说明该事务被打断，不执行，设置you=you1失败了
		} catch (Exception e){
			System.out.println(e);
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		/// ... when closing your application:
		JedisPoolManager.getMgr().destroy();
	}

}
