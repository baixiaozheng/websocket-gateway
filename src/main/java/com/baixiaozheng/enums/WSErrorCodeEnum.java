package com.baixiaozheng.enums;

public enum WSErrorCodeEnum {
  INVALID_PARAMETER("invalid-parameter", "参数错误"),
  BAD_REQUEST("bad-request", "错误请求"),
  INVALID_COMMAND("invalid-command", "无效指令"),

  BASE_SYMBOL_ERROR("base-symbol-error", "交易对不存在"),
  BASE_CURRENCY_ERROR("base-currency-error", "币种不存在"),
  BASE_DATE_ERROR("base-date-error", "错误的日期格式"),
  BAD_ARGUMENT("bad-argument", "无效参数"),
  TOKEN_ERROR("token-error", "token错误"),
  SERVER_INTERNAL_ERROR("server_internal_error", "服务器内部错误"),

  REQ_KLINE_429("req kline 429", "请求kline过于频繁，请稍候再试"),

  IP_ERROR("ip error", "IP 错误"),
  WEBSOCKET_COUNT_ERROR("websocket count error", "websocket 连接上限错误");
  private String errorCode;

  private String errorMsg;

  WSErrorCodeEnum(String errorCode, String errorMsg) {
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }
}