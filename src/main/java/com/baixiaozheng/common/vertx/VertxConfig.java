package com.baixiaozheng.common.vertx;

import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Vertx Configurations See {@link VertxOptions} for detail
 */
@Slf4j
@Configuration
public class VertxConfig {

  @Getter
  private VertxOptions vertxOptions;

//  @Value("${websocket-gateway.vertx.clustered}")
//  private boolean clustered;
//
//  @Value("${websocket-gateway.vertx.cluster-host}")
//  private String clusterHost;
//
//  @Value("${websocket-gateway.vertx.cluster-port}") // 0 meaning assign a random port
//  private int clusterPort;
//
//  @Value("${websocket-gateway.vertx.cluster-ping-interval}") // in ms, default 20s
//  private long clusterPingInterval;
//
//  @Value("${websocket-gateway.vertx.cluster-ping-reply-interval}")  // in ms, default 20s
//  private long clusterPingReplyInterval;
//
//  @Value("${websocket-gateway.vertx.ha-enabled}")
//  private boolean haEnabled;
//
//  @Value("${websocket-gateway.vertx.ha-group}")
//  private String haGroup;
//
//  @Value("${websocket-gateway.vertx.internal-blocking-pool-size}")
//  private int internalBlockingPoolSize;
//
  @Value("${websocket-gateway.vertx.blocked-thread-check-interval}") // in ms, default 1s
  private long blockedThreadCheckInterval;
//
//  @Value("${websocket-gateway.vertx.event-loop-pool-size}")
//  // default is 2 * number of cores on the machine
//  private int eventLoopPoolSize;
//
//  @Value("${websocket-gateway.vertx.max-event-loop-execute-time}") // in ns, default 20s
//  private long maxEventLoopExecuteTime;
//
//  @Value("${websocket-gateway.vertx.worker-pool-size}")
//  private int workerPoolSize;
//
//  @Value("${websocket-gateway.vertx.worker.execute-time}") // in ns, default 60s
//  private long maxWorkerExecuteTime;

//  @Value("${websocket-gateway.zookeeper.zookeeper-hosts}")
//  private String zkHosts;
//
//  @Value("${websocket-gateway.zookeeper.root-path}")
//  private String zkRootPath;
//
//  @Value("${websocket-gateway.zookeeper.connection-timeout}") // in ms, default 15s
//  private int zkConnectionTimeoutMs;
//
//  @Value("${websocket-gateway.zookeeper.session-timeout}") // in ms, default 60s
//  private int zkSessionTimeout;
//
//  @Value("${websocket-gateway.zookeeper.retry.initial-sleep-time}") // in ms, default 10s
//  private int zkRetryInitialSleepTime;
//
//  @Value("${websocket-gateway.zookeeper.retry.interval-times}") // in ms, default 10s
//  private int zkRetryIntervalTimes;
//
//  @Value("${websocket-gateway.zookeeper.retry.max-times}")
//  private int zkRetryMaxTimes;
//
  @Value("${websocket-gateway.vertx.warning-exception-time}")
  private int vertxWarningExceptionTime;

  @Value("${websocket-gateway.vertx.cache-max-time-to-live}")
  private int vertxCacheMaxTimeToLive;

  @PostConstruct
  void init() {
    /**
     * configuring to use zookeeper as our cluster manager
     * see {@link org.apache.curator.framework.CuratorFrameworkFactory} for detail
     */
//    JsonObject zkConfig = new JsonObject()
//            .put("zookeeperHosts", zkHosts)
//            .put("rootPath", zkRootPath)
//            .put("sessionTimeout", zkSessionTimeout)
//            .put("connectTimeout", zkConnectionTimeoutMs)
//            .put("retry",
//                    new JsonObject()
//                            .put("initialSleepTime", zkRetryInitialSleepTime)
//                            .put("intervalTimes", zkRetryIntervalTimes)
//                            .put("maxTimes", zkRetryMaxTimes)
//            );
//    ClusterManager clusterManager = new ZookeeperClusterManager(zkConfig);
//    log.info("initial vertx cluster configuration, {}", zkConfig.toString());

//    Set<String> ips = LocalIpAddressUtil.resolveLocalIps();
//    if (StringUtils.isBlank(clusterHost) || "NOT_SET".equals(clusterHost)) {
//      int total = ips.size();
//      int remain = total;
//      for (String addr : ips) {
//        if (remain == 1) {
//          clusterHost = addr;
//          break;
//        }
//        //remain > 1
//        if (!addr.endsWith(".0.1")) {
//          clusterHost = addr;
//          break;
//        }
//        remain--;
//      }
//    }
//    Set<InetAddress> addresses = LocalIpAddressUtil.resolveLocalAddresses();
//    log.info("get all ips is {},get all addresses is {},choose clusterHost is {}", ips, addresses, clusterHost);
    vertxOptions = new VertxOptions()
//            .setClustered(clustered)
//            .setClusterHost(clusterHost)
//            .setClusterPort(clusterPort)
//            .setClusterPingInterval(clusterPingInterval)
//            .setClusterPingReplyInterval(clusterPingReplyInterval)
//            .setHAEnabled(haEnabled)
//            .setHAGroup(haGroup)
//            .setInternalBlockingPoolSize(internalBlockingPoolSize)
            .setBlockedThreadCheckInterval(blockedThreadCheckInterval)
//            .setEventLoopPoolSize(eventLoopPoolSize)
//            .setMaxEventLoopExecuteTime(maxEventLoopExecuteTime)
//            .setWorkerPoolSize(workerPoolSize)
//            .setMaxWorkerExecuteTime(maxWorkerExecuteTime)
//            .setClusterManager(clusterManager)
            .setWarningExceptionTime(vertxWarningExceptionTime)
            .setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
    //设置dns最长cache时间为3600秒
    vertxOptions.getAddressResolverOptions().setCacheMaxTimeToLive(vertxCacheMaxTimeToLive);

    log.info("initial vertx configuration, {}", vertxOptions.toString());
  }
}
