package com.licc.task.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;



/**
 * redis 配置
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {




  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate template = new StringRedisTemplate(factory);
    initDomainRedisTemplate(template,factory);
    template.afterPropertiesSet();
    return template;
  }
  /**
   * 设置数据存入 redis 的序列化方式
   *
   * @param redisTemplate
   * @param factory
   */
  private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
   //  redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setHashKeySerializer(new StringRedisSerializer());



//    redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
   // redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    redisTemplate.setConnectionFactory(factory);
  }
  /**
   * 实例化 listOperations 对象,可以使用 list 类型操作
   *
   * @param redisTemplate
   * @return
   */
  @Bean
  public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForList();
  }

  /**
   * 实例化 HashOperations 对象,可以使用 Hash 类型操作
   *
   * @param redisTemplate
   * @return
   */
  @Bean
  public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForHash();
  }

  /**
   * 实例化 ValueOperations 对象,可以使用 String 操作
   *
   * @param redisTemplate
   * @return
   */
  @Bean
  public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {

    return redisTemplate.opsForValue();
  }
  @Bean
  public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForSet();
  }
}