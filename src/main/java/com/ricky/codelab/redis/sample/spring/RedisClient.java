package com.ricky.codelab.redis.sample.spring;

import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis Client
 *
 * @author Ricky Fung
 * @create 2016-07-29 17:23
 */
public class RedisClient {

    private JedisPool pool;

    public RedisClient(JedisPool pool){
        this.pool = pool;
    }

    public Jedis getResource(){

        return pool.getResource();
    }

    public void destroy(){
        pool.destroy();
    }

    public void shutdown(){
        pool.close();
    }

    public static class RedisClientFacotry implements FactoryBean<RedisClient>{
        private String ip;
        private int port;
        private String password;
        private int timeout;
        private int maxTotal = 50;
        private int maxWaitMillis = 1000*6;
        private int maxIdle = 5;
        private boolean testOnReturn;
        private boolean testOnBorrow;

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public void setMaxWaitMillis(int maxWaitMillis) {
            this.maxWaitMillis = maxWaitMillis;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public void setTestOnReturn(boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        @Override
        public RedisClient getObject() throws Exception {

            //config
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxTotal);
            config.setMaxWaitMillis(maxWaitMillis);
            config.setMaxIdle(maxIdle);
            config.setTestOnReturn(testOnReturn);
            config.setTestOnBorrow(testOnBorrow);

            System.out.println("redis服务器IP:" + ip + ", 端口:" + port);

            JedisPool pool = new JedisPool(config, ip, port, timeout, password);

            return new RedisClient(pool);
        }

        @Override
        public Class<?> getObjectType() {

            return RedisClient.class;
        }

        @Override
        public boolean isSingleton() {

            return true;
        }
    }
}
