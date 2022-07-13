package com.baixiaozheng.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TopicMarketDepthUnitEnum {

  TOPIC_MARKET_DEPTH_UNIT_STEP0("step0"),
  TOPIC_MARKET_KLINE_UNIT_STEP1("step1"),
  TOPIC_MARKET_KLINE_UNIT_STEP2("step2"),
  TOPIC_MARKET_KLINE_UNIT_STEP3("step3"),
  TOPIC_MARKET_KLINE_UNIT_STEP4("step4"),
  TOPIC_MARKET_KLINE_UNIT_STEP5("step5"),
  TOPIC_MARKET_KLINE_UNIT_PERCENT10("percent10");

  private String name;
  private static Set<String> names = Arrays.asList(TopicMarketDepthUnitEnum.values()).stream().map(e -> e.name).collect(Collectors.toSet());

  TopicMarketDepthUnitEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Set<String> names() {
    return names;
  }
}
