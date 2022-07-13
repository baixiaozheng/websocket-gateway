package com.baixiaozheng.common.constant;

public class SessionConstant {
  /**
   * URI 访问地址 v1
   */
  public static final String URI_V1 = "/websocket/api";


  public static final String EVENTBUS_TOPIC_PREFIX = "eventbus-topic-";

  public static final String EVENTBUS_CUSTOMER_ID = "userId";

  public static final String EVENTBUS_MESSAGE_TYPE = "type";

  public static final String EVENTBUS_MESSAGE_TYPE_BALANCE = "balance";

  public static final String EVENTBUS_MESSAGE_TYPE_QUOTATION = "quotation";

  /**
   * 普通推送，推全部订阅用户
   */
  public static final String EVENTBUS_MESSAGE_TYPE_ORDINARY = "ordinary";

  /**
   * 专有推送，针对具体用户
   */
  public static final String EVENTBUS_MESSAGE_TYPE_PROPRIETARY = "proprietary";


  public static final String MQ_EXCHANGE_NAME = "websocket-topic";

  public static final String MQ_QUOTATION_ROUTINGKEY = "websocket.toAll";

  public static final String MQ_BALANCE_ROUTINGKEY = "balance.toAll";

  public static final String MQ_ORDINARY_ROUTINGKEY = "ordinary.toAll";

  public static final String MQ_PROPRIETARY_ROUTINGKEY = "proprietary.toAll";

  public static final String SOCKET_USER_IDS = "socket:userIds";


}
