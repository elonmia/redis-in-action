package com.ricky.codelab.redis.sample.ch1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ricky.codelab.redis.sample.pool.JedisPoolManager;

import redis.clients.jedis.Jedis;

public class RedisDemo {

	public static void main(String[] args) {
		
		Jedis jedis = null;
		try {
		  jedis = JedisPoolManager.getMgr().getResource();
//		  jedis.auth("hello");
		  
		  //simple key-value
	      jedis.set("redis", "myredis");
	      System.out.println(jedis.get("redis"));
	      
	      jedis.append("redis", "yourredis");   
	      jedis.append("mq", "RabbitMQ");
	      
	      //incr
	      String pv = jedis.set("pv", "0");
	      System.out.println("pv:"+pv);
	      jedis.incr("pv");
	      jedis.incrBy("pv", 10);
	      System.out.println("pv:"+pv);
	      
		  //mset
		  jedis.mset("firstName", "ricky", "lastName", "Fung");
	      System.out.println(jedis.mget("firstName", "lastName"));
		  
	      //map
	      Map<String,String> cityMap =  new HashMap<String,String>();
	      cityMap.put("beijing", "1");
	      cityMap.put("shanghai", "2");
	      
	      jedis.hmset("city", cityMap);
	      System.out.println(jedis.hget("city", "beijing"));
	      System.out.println(jedis.hlen("city"));
	      System.out.println(jedis.hmget("city", "beijing","shanghai"));
	      
	      //list
		  jedis.lpush("hobbies", "reading");
		  jedis.lpush("hobbies", "basketball");
		  jedis.lpush("hobbies", "shopping");
		  
		  List<String> hobbies = jedis.lrange("hobbies", 0, -1);
		  System.out.println("hobbies:"+hobbies);
		  
		  jedis.del("hobbies");
		  
		  //set
		  jedis.sadd("name", "ricky");
	      jedis.sadd("name", "kings");
	      jedis.sadd("name", "demon");
	      
	      System.out.println("size:"+jedis.scard("name"));
	      System.out.println("exists:"+jedis.sismember("name", "ricky"));
	      System.out.println(String.format("all members: %s", jedis.smembers("name")));
	      System.out.println(String.format("rand member: %s", jedis.srandmember("name")));
	      //remove
	      jedis.srem("name", "demon");
	      
		  //hset
		  jedis.hset("address", "country", "CN");
		  jedis.hset("address", "province", "BJ");
		  jedis.hset("address", "city", "Beijing");
		  jedis.hset("address", "district", "Chaoyang");
		  
		  System.out.println("city:"+jedis.hget("address", "city"));
		  System.out.println("keys:"+jedis.hkeys("address"));
		  System.out.println("values:"+jedis.hvals("address"));
	      
		  //zadd
		  jedis.zadd("gift", 0, "car"); 
		  jedis.zadd("gift", 0, "bike"); 
		  Set<String> gift = jedis.zrange("gift", 0, -1);
		  System.out.println("gift:"+gift);
		  
		} finally {
		  if (jedis != null) {
		    jedis.close();
		  }
		}
		/// ... when closing your application:
		JedisPoolManager.getMgr().destroy();
	}

}
