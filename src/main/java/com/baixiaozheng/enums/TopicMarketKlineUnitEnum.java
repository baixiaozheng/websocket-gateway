package com.baixiaozheng.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TopicMarketKlineUnitEnum {

  TOPIC_MARKET_KLINE_UNIT_1MIN("1min", 60 * 1000L, 60L),
  TOPIC_MARKET_KLINE_UNIT_5MIN("5min", 5 * 60 * 1000L, 5 * 60L),
  TOPIC_MARKET_KLINE_UNIT_15MIN("15min", 15 * 60 * 1000L, 15 * 60L),
  TOPIC_MARKET_KLINE_UNIT_30MIN("30min", 30 * 60 * 1000L, 30 * 60L),
  TOPIC_MARKET_KLINE_UNIT_60MIN("60min", 60 * 60 * 1000L, 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_4HOUR("4h", 4 * 60 * 60 * 1000L, 4 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_8HOUR("8h", 8 * 60 * 60 * 1000L, 8 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_12HOUR("12h", 12 * 60 * 60 * 1000L, 12 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_1DAY("1day", 24 * 60 * 60 * 1000L, 24 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_1MONTH("1mon", 30 * 24 * 60 * 60 * 1000L, 30 * 24 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_1WEEK("1week", 7 * 24 * 60 * 60 * 1000L, 7 * 24 * 60 * 60L),
  TOPIC_MARKET_KLINE_UNIT_1YEAR("1year", 365 * 24 * 60 * 60 * 1000L, 365 * 24 * 60 * 60L);

  private String name;
  private Long ms;
  private Long second;
  private static Set<String> names = Arrays.asList(TopicMarketKlineUnitEnum.values()).stream().map(e -> e.name).collect(Collectors.toSet());
  TopicMarketKlineUnitEnum(String name, Long ms, Long second) {
    this.name = name;
    this.ms = ms;
    this.second = second;
  }

  public String getName() {
    return name;
  }

  public Long getMs() {
    return ms;
  }


  public Long getSecond() {
    return second;
  }

  public static Set<String> names() {
    return names;
  }
}
