package com.demo.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class RedisConfig2{

    @Autowired
    private RedisProperties2 redisProperties;

    @Bean(name = "cacheManager2")
    public CacheManager cacheManager2() {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(jedisConnectionFactory2());
        return redisCacheManager;
    }

    @Bean(name = "redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate2() {
        //设置序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory2());
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer); // Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "jedisConnectionFactory2")
    public JedisConnectionFactory jedisConnectionFactory2() {
        boolean isCluster = redisProperties.getCluster() != null && redisProperties.getCluster().getNodes() != null && !redisProperties.getCluster().getNodes().isEmpty();
        Duration connectionTimeOut = redisProperties.getTimeout();
        int timeOut = 1000;
        if (connectionTimeOut != null) {
            timeOut = (int) connectionTimeOut.toMillis();
        }
        if (isCluster) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, setPoolConfig(redisProperties));
            jedisConnectionFactory.setTimeout(timeOut);
            jedisConnectionFactory.setPassword(redisProperties.getPassword());
            return jedisConnectionFactory;
        }
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.setTimeout(timeOut);
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        jedisConnectionFactory.setPoolConfig(setPoolConfig(redisProperties));
        return jedisConnectionFactory;
    }

    public JedisPoolConfig setPoolConfig(RedisProperties2 redisProperties2) {
        JedisPoolConfig config = new JedisPoolConfig();
        // 1.1 最大连接数
        config.setMaxTotal(redisProperties2.getMaxTotal());
        config.setMaxWaitMillis(redisProperties2.getJedis().getPool().getMaxWait().toMillis());
        //1.2 最大空闲连接数
        config.setMaxIdle(redisProperties2.getJedis().getPool().getMaxIdle());
        config.setMinIdle(redisProperties2.getJedis().getPool().getMinIdle());
        config.setTestOnBorrow(redisProperties2.isTestOnBorrow());
        config.setTestOnReturn(redisProperties2.isTestOnReturn());
        config.setTestWhileIdle(redisProperties2.isTestWhileIdle());
        config.setTestOnCreate(redisProperties2.isTestOnCreate());
        config.setNumTestsPerEvictionRun(redisProperties2.getNumTestsPerEvictionRun());
        config.setTimeBetweenEvictionRunsMillis(redisProperties2.getTimeBetweenEvictionRunsMillis());
        config.setBlockWhenExhausted(redisProperties2.isBlockWhenExhausted());
        return config;
    }
}