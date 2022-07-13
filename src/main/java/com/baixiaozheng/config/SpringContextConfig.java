package com.baixiaozheng.config;


import com.rabbitmq.client.Address;
import io.vertx.core.Vertx;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
@Slf4j
public class SpringContextConfig {

  @Bean("mqOptions")
  public RabbitMQOptions getMqOptions(@Value("${websocket-gateway.mq.host}") String host,
                                      @Value("${websocket-gateway.mq.port}") int port,
                                      @Value("${websocket-gateway.mq.username}") String username,
                                      @Value("${websocket-gateway.mq.password}") String password,
                                      @Value("${websocket-gateway.mq.heart-beat}") int heartbeat,
                                      @Value("${websocket-gateway.mq.connection-timeout}") int connectiontimeout) {
    return new RabbitMQOptions().setHost(host).setPort(port).setUser(username).setPassword(password).setRequestedHeartbeat(heartbeat).setConnectionTimeout(connectiontimeout);
    //return new RabbitMQOptions().setAddresses(addresses).setUser(username).setPassword(password).setRequestedHeartbeat(heartbeat).setConnectionTimeout(connectiontimeout);
  }

  @Bean("mqClient")
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public RabbitMQClient getMq(@Autowired Vertx vertx, @Autowired RabbitMQOptions mqOptions) {
    return RabbitMQClient.create(vertx, mqOptions);
  }

//  @Bean("rOptions")
//  public RedisOptions getROptions(@Value("${websocket-gateway.redis.host}") String host,
//                                      @Value("${websocket-gateway.redis.port}") int port,
//                                      @Value("${websocket-gateway.redis.database}") int database,
//                                      @Value("${websocket-gateway.redis.password}") String password) {
//    RedisOptions o = new RedisOptions().setHost(host).setPort(port).setSelect(database);
//    return StringUtils.isNotBlank(password)?o.setAuth(password):o;
//  }
//
//  @Bean("rClient")
//  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
//  public RedisClient getRClient(@Autowired Vertx vertx, @Autowired RedisOptions rOptions) {
//    return RedisClient.create(vertx, rOptions);
//  }

//  @Bean
//  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
//  public HttpClient getHttpClient(@Autowired Vertx vertx,
//                                  @Value("${websocket-gateway.vertx.httpclient.pool.size}") int poolSize,
//                                  @Value("${websocket-gateway.vertx.httpclient.ssl}") boolean ssl,
//                                  @Value("${websocket-gateway.vertx.httpclient.connection-timeout}") int connectionTimeout) {
//    HttpClientOptions options = new HttpClientOptions();
//    if (poolSize != 0) {
//      options.setMaxPoolSize(poolSize);
//    }
//    options.setSsl(ssl)
//            .setConnectTimeout(connectionTimeout);
//    return vertx.createHttpClient(options);
//  }
}
