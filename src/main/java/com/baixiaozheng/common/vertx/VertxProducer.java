package com.baixiaozheng.common.vertx;

import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A component managing the lifecycle of the clustered Vert.x instance.
 * <p>
 * This bean {@link Vertx} is created <strong>after</strong> and destroyed <strong>before</strong> the {@link VertxConfig}.
 */
@Slf4j
@Configuration
public class VertxProducer {

  @Autowired
  VertxConfig vertxConfig;
  @Getter
  private Vertx vertx;
//  @Value("${websocket-gateway.vertx.init-timeout}")
//  private Integer vertxInitTimeout;
  @Getter
  @Autowired
  private SpringVerticleFactory springVerticleFactory;


  /**
   * Exposes the clustered Vert.x instance. We must disable destroy method inference, otherwise
   * Spring will call the {@link Vertx#close()} automatically.
   */
  @Bean(name = "vertx", destroyMethod = "")
  Vertx vertx() {
    return vertx;
  }

  @PostConstruct
  void init() {
    System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
//    final CompletableFuture<Vertx> future = new CompletableFuture<>();

//    try {
//      Vertx.clusteredVertx(vertxConfig.getVertxOptions(), ar -> {
//        if (ar.succeeded()) {
//          log.debug("initial vertx instance success", ar.result());
//          future.complete(ar.result());
//        } else {
//          log.debug("initial vertx instance failure", ar.cause());
//          future.completeExceptionally(ar.cause());
//        }
//      });
      vertx = Vertx.vertx(vertxConfig.getVertxOptions());
      //注册springVerticleFactory
      vertx.registerVerticleFactory(springVerticleFactory);
//    } catch (InterruptedException | ExecutionException | TimeoutException e) {
//      log.error("initial vertx instance failure", e);
//      throw new VertxException("initial vertx instance failure", e);
//    }
  }

  @PreDestroy
  void close() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future = new CompletableFuture<>();
    vertx.close(ar -> future.complete(null));

    future.get();
  }
}
