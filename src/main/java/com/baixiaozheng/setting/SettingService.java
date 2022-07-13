package com.baixiaozheng.setting;

import com.baixiaozheng.common.setting.manager.*;
import com.baixiaozheng.setting.manager.TopicManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
/**
 * 对接外部系统的服务相当于防腐层
 */
public class SettingService {

  // 如果启动时连不上symbolserver是否直接crash：
  @Value("${websocket-gateway.failFast:true}")
  boolean failFast;

  @Autowired
  private CityManager cityManager;
  @Autowired
  private TopicManager topicManager;

  @PostConstruct
  public void init() {
    if (!refreshSettings()) {
      // FIXME: 启动时读取symbolserver失败是否直接crash？
      if (failFast) {
        log.error("start websocket-gateway failed for read symbolserver settings failed!!!");
        throw new Error("cannot get settings from symbolserver.");
      }
    }
  }

  @Scheduled(fixedDelay = 60_000, initialDelay = 10)
  public boolean refreshSettings() {
    log.info("*******************start refreshSettings******************");
    if (!doRefreshSettings()) {
      return false;
    }
    return true;
  }

  public boolean doRefreshSettings() {
    log.info("*******************start doRefreshSettings******************");
//    log.warn("refresh settings from symbols...");
    try {
//      log.info("get tradeInfo settings...");
////      financeCoinTradeInfoManager.setFinanceCoinTradeInfoSet();
//      log.info("got tradeInfo settings.");
//      log.info("get tradeDistrict settings.");
////      tradeDistrictManager.setDistrictSet();
//      log.info("got tradeDistrict settings.");
//      log.info("get payCoin settings.");
////      payCoinManager.setPayCoinSet();
//      log.info("got payCoin settings.");

      cityManager.setCitySet();
      log.info("refresh city settings ");
      topicManager.reload();
      log.info("got topicHelper settings.");
      log.info("refresh settings from topicHelper ok.");
      return true;
    } catch (Throwable e) {
      log.error("Get settings failed.", e);
      return false;
    }
  }
}

