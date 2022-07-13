package com.baixiaozheng;

import com.baixiaozheng.common.util.RuntimeAndSystemUtil;
import com.baixiaozheng.common.vertx.SpringVerticleFactory;
import com.baixiaozheng.config.VerticleConfig;
import com.baixiaozheng.verticle.GatewayVerticle;
import com.baixiaozheng.verticle.SocketUpStreamVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class WebsocketApplication {

    @Autowired
    Vertx vertx;
    @Autowired
    SpringVerticleFactory springVerticleFactory;
    @Autowired
    VerticleConfig verticleConfig;

    @Value("${websocket-gateway.msg.shard-count:0}")
    int msgShardCount;

    public static void main(String[] args) {
        //DNS参数设置
//    System.setProperty("sun.net.inetaddr.ttl", "3600");
//    System.setProperty("sun.net.inetaddr.negative.ttl", "1");
//    java.security.Security.setProperty("networkaddress.cache.ttl", "3600");
        RuntimeAndSystemUtil.getCpuAndEnvAndProperties();
        SpringApplication.run(WebsocketApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            if (msgShardCount == 0) {
                //原来是2，现在改成4了
                msgShardCount = 1 * CpuCoreSensor.availableProcessors();
            }

            log.info("msgShardCount = {}", msgShardCount);
            for (int i = 0; i < msgShardCount; i++) {
                DeploymentOptions dOption = new DeploymentOptions(verticleConfig.getDeploymentOptions());
                dOption.setInstances(1);
                dOption.setConfig(new JsonObject().put("msgShardNo", i));
                deployVerticle(vertx, GatewayVerticle.class, dOption);
            }

//      MetricsService m = MetricsService.create(vertx);
//
//      vertx.setPeriodic(1000, h -> {
//        JsonObject metrics = m.getMetricsSnapshot(vertx);
//        VertxMetricsHandler.handlerMetrics(statsdClient, metrics);
//      });
            DeploymentOptions option = new DeploymentOptions(verticleConfig.getDeploymentOptions());
            option.setInstances(1);
            deployVerticle(vertx, SocketUpStreamVerticle.class, option);
        } catch (Throwable e) {
            log.error("gateway init exception: {} {}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Deploy a vertx-guice verticle on a vertx instance with deployment options
     */
    public String deployVerticle(Vertx vertx, Class<? extends Verticle> clz, DeploymentOptions opts) {
        Future<String> future = Future.future();
        String verticleName = springVerticleFactory.prefix() + ":" + clz.getName();
        vertx.deployVerticle(verticleName, opts, ar -> {
            if (ar.succeeded()) {
                JsonObject config = opts.getConfig();
                log.info("deployed verticle {}, result: {},msgShardNo: {} ", verticleName, ar.result(), config==null?0:config.getInteger("msgShardNo"));
                future.complete(ar.result());
            } else {
                log.error("failed to deploy verticle {}", verticleName, ar.cause());
                future.fail(ar.cause());
            }
        });
        return future.result();
    }
}
