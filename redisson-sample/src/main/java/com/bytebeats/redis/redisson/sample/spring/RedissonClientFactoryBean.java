package com.bytebeats.redis.redisson.sample.spring;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import java.io.File;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @date 2017-03-06 19:20
 */
public class RedissonClientFactoryBean implements FactoryBean<RedissonClient>, DisposableBean {

    private RedissonClient redisson;

    private String file;
    private String format;

    @Override
    public RedissonClient getObject() throws Exception {

        Config config = null;
        if("JSON".equalsIgnoreCase(format)){
            config = Config.fromJSON(new File(file));
        } else if("YAML".equalsIgnoreCase(format)){
            config = Config.fromYAML(new File(file));
        } else {
            throw new IllegalArgumentException("unsupported format:"+format);
        }
        redisson = Redisson.create(config);
        return redisson;
    }

    @Override
    public Class<?> getObjectType() {
        return RedissonClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if(redisson!=null){
            redisson.shutdown();
            redisson = null;
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
