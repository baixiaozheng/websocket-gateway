package com.baixiaozheng.common.setting.eunms;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TopicEnum {

  SYMBOL_TOPIC("symbol"),
  CURRENCY_TOPIC("currency");

  private String name;
  private static Set<String> names = Arrays.asList(TopicEnum.values()).stream().map(e -> e.name).collect(Collectors.toSet());

  TopicEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Set<String> names() {
    return names;
  }
}
