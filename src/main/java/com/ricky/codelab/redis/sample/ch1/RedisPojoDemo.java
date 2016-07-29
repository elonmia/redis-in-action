package com.ricky.codelab.redis.sample.ch1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.ricky.codelab.redis.sample.pool.JedisPoolManager;
import redis.clients.jedis.Jedis;

public class RedisPojoDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		Jedis jedis = null;
		try {
			jedis = JedisPoolManager.getMgr().getResource();
	        
	        Person person =  new Person("Ricky", 27);
	        //序列化
	        byte[] byteArray = serialize(person);
	        
	        //set
	        jedis.set("Ricky".getBytes(), byteArray);

	        //get
	        byteArray = jedis.get("Ricky".getBytes());
	        //反序列化
	        person = deserialize(byteArray);
	        
	        System.out.println(person);
	        
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		/// ... when closing your application:
		JedisPoolManager.getMgr().destroy();
	}

	public static Person deserialize(byte[] byteArray) throws ClassNotFoundException, IOException{
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			ois = new ObjectInputStream(bais);
			return (Person) ois.readObject();
		} finally {
			ois.close();
		}
	}
	
	public static byte[] serialize(Person person) throws IOException{
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(person);
			oos.flush();
			
			return baos.toByteArray();
			
		} finally {
			oos.close();
			baos.close();
		}
	}
}

class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int age;

	public Person() {

	}
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
}
