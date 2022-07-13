package com.baixiaozheng.common.setting.client;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

@Configuration
@Slf4j
public class RedisConfiguration {

//  @Value("${websocket-gateway.redis.host:127.0.0.1}")
//  private String host;
//
//  @Value("${websocket-gateway.redis.port:6379}")
//  private int port;
//
//  @Value("${websocket-gateway.redis.database:0}")
//  private int database;
//
//  @Value("${websocket-gateway.redis.timeout:30000}")
//  private int timeout;
//
//  @Value("${websocket-gateway.redis.password:}")
//  private String password;
//
//  @Value("${websocket-gateway.redis.ssl:true}")
//  private boolean useSsl;

//  @Bean
//  public RedisConnectionFactory redisConnectionFactory() {
//    JedisConnectionFactory factory = new JedisConnectionFactory();
//    factory.setHostName(host);
//    factory.setPort(port);
//    if(StringUtils.isNotBlank(password)){
//      factory.setPassword(password);
//    }
//    factory.setTimeout(timeout);
//    factory.setDatabase(database);
//    factory.setUseSsl(useSsl);
//    return factory;
//  }

  @Bean
  public RedisSerializer fastJson2JsonRedisSerializer() {
    return new FastJsonRedisSerializer(Object.class);
  }

  @Bean
  public RedisTemplate<Serializable, Object> redisTemplate(@Autowired RedisConnectionFactory factory) {
    RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
    // 配置连接工厂
    template.setConnectionFactory(factory);

    //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
//    GenericJackson2JsonRedisSerializer jacksonSeial = new GenericJackson2JsonRedisSerializer();

//    ObjectMapper om = new ObjectMapper();
//    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//    jacksonSeial.setObjectMapper(om);
    // 值采用json序列化
    template.setValueSerializer(fastJson2JsonRedisSerializer());
    //使用StringRedisSerializer来序列化和反序列化redis的key值
    template.setKeySerializer(new StringRedisSerializer());
    // 设置hash key 和value序列化模式
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(fastJson2JsonRedisSerializer());
    template.afterPropertiesSet();
    return template;
  }
}
