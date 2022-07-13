package com.baixiaozheng.common.util;

import org.apache.commons.lang3.StringUtils;

public class ChannelUtil {
  /**
   * 验证channel
   *
   * @param channel
   */
  public static boolean vaildChannel(Object channel) {
    if (!(channel instanceof Character[])) {
      return false;
    }
    String channelStr = String.valueOf(channel);
    if (StringUtils.isBlank(channelStr)) {
      return false;
    }
    String[] topicArray = channelStr.split("\\.");
    if (topicArray == null) {
      return false;
    }
    return true;
  }

  /**
   * 验证from and to
   */
  public static boolean vaildFromAndTo(Object from, Object to) {
    if (from != null && !(from instanceof Integer)) {
      return false;
    }
    if (to != null && !(to instanceof Integer)) {
      return false;
    }
    return true;
  }

  /**
   * 验证token
   */
  public static boolean vaildToken(Object token) {
    if (!(token instanceof Character[]) && !(token instanceof String)) {
      return false;
    }
    if (StringUtils.isBlank(String.valueOf(token))) {
      return false;
    }
    return true;
  }

  /**
   * 验证event
   *
   * @param event
   */
  public static boolean vaildEvent(Object event) {
    if (!(event instanceof Character[])) {
      return false;
    }
    if (StringUtils.isBlank(String.valueOf(event))) {
      return false;
    }
    return true;
  }
}
