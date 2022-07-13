package com.baixiaozheng.config;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@Data
@ConfigurationProperties(prefix = "websocket-gateway.vertx.verticles.gateway")
public class VerticleConfig {

  @Getter
  DeploymentOptions deploymentOptions;

  private int instances;

  private boolean worker;

  private String workerPoolName;

  private int workerPoolSize;

  private boolean ha;

  private long maxWorkerExecuteTime;

  private boolean multiThreaded;

  @PostConstruct
  void init() {
    //如果沒有配置instances的话取cpu核数*2
    if (instances == 0) {
      instances = 2 * CpuCoreSensor.availableProcessors();
    }

    deploymentOptions = new DeploymentOptions()
            .setInstances(instances)
            .setWorker(worker)
            .setWorkerPoolName(workerPoolName)
            .setWorkerPoolSize(workerPoolSize)
            .setHa(ha)
            .setMaxWorkerExecuteTime(maxWorkerExecuteTime)
            .setMultiThreaded(multiThreaded);

    log.debug("init verticle configuration, {}", deploymentOptions.toJson());
  }
}
