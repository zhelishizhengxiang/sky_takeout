package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.config
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/30
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建redis模板对象");
        RedisTemplate redisTemplate = new RedisTemplate();

        //设置redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //Spring Data Redis 默认使用JdkSerializationRedisSerializer
        //虽然能够正确存储在redis中，但是在客户端查看是乱码，无法直接查看缓存数据
        //设置redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置redis value的序列化器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 设置哈希键的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 设置哈希值的序列化方式
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
