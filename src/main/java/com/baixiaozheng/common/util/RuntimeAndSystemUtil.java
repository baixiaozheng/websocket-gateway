package com.baixiaozheng.common.util;

import io.vertx.core.impl.cpu.CpuCoreSensor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuntimeAndSystemUtil {
  public static void getCpuAndEnvAndProperties() {
    log.info("cpu availableProcessors is {}, vertx's CpuCoreSensor.availableProcessors is {},total memory is {} m,free memory is {} m,max memory is {} m",
            Runtime.getRuntime().availableProcessors(), CpuCoreSensor.availableProcessors(),
            Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024,
            Runtime.getRuntime().maxMemory() / 1024 / 1024);
    log.info("system env {},{}", System.getenv("APP_NAME"), System.getenv("ENV_NAME"));
    log.info("system all env {}", System.getenv());
    log.info("system property {},{}}", System.getProperty("APP_NAME"), System.getProperty("ENV_NAME"));
    log.info("system properties {}", System.getProperties());
  }
}
