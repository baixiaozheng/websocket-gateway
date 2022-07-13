package com.baixiaozheng.session;

import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.common.util.ApiSignature;
import com.baixiaozheng.common.util.ExceptionUtil;
import com.baixiaozheng.common.util.ZipUtil;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Session {

  private static ApiSignature sig = new ApiSignature();
  private static ZipUtil zipUtil = new ZipUtil();
  private ServerWebSocket webSocket;
  private Set<String> subedTopics;
//  @Getter
//  private boolean balance;
  @Getter
  private boolean proprietary;
  @Getter
  private String userId;
  @Getter
  private String serverName;
  private Vertx vertx;

  //  private RateLimiter rateLimiter;
  public Session(@NonNull ServerWebSocket webSocket, @NonNull Vertx vertx, @NonNull String serverName) {
    this.vertx = vertx;
    this.webSocket = webSocket;
    this.subedTopics = new HashSet<>();
    this.serverName = serverName;
//    this.rateLimiter = GWSmoothRateLimiter.create(2, 3);
  }

  /**
   * 处理订阅
   *
   * @param topic
   */
  public boolean sub(String topic) {
    this.subedTopics.add(topic);
    return true;
  }

  /**
   * 处理订阅
   *
   * @param topic
   */
  public boolean unsub(String topic) {
    this.subedTopics.remove(topic);
    if (!containsProprietary()) {
      clearProprietary();
    }
    return true;
  }

  public void setProprietary(boolean proprietary, String userId) {
    this.proprietary = proprietary;
    this.userId = userId;
  }

  public void clearProprietary() {
    this.proprietary = false;
    this.userId = "0";
  }

  /**
   * 是否还包含balance订阅
   *
   * @return
   */
  public boolean containsProprietary() {
    for (String topic : subedTopics) {
      if (StringUtils.endsWithIgnoreCase(topic, ApiParamConstant.MARKET_PROPRIETARY)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否还包含topic订阅
   *
   * @return
   */
  public boolean containsTopic(String topic) {
    return subedTopics.contains(topic);
  }

  /**
   * 统一的回复用户的方法
   *
   * @param msg
   */
  private void writeBinaryMsgToClient(String msg) {
    webSocket.writeTextMessage(msg);
  }

  /**
   * 关闭当前session
   */
  public void close() {
    webSocket.close();
  }

  /**
   * 写入正常返回的信息
   * ping
   * @param msgJson
   */
  public void writeRespMessage(JsonObject msgJson) {
    writeBinaryMsgToClient(msgJson.encode());
  }

  /**
   * 写入异步通知信息
   *
   */
  public void writeNotifyMessage(String msgStr) {
    writeBinaryMsgToClient(msgStr);
  }

  /**
   * sub订阅后的统一性响应
   * =
   *
   * @param channel
   * @param data
   * @param reqId
   */
  public void responseSuccessData(String channel, Object data, String reqId) {
    try {
      writeBinaryMsgToClient(new JsonObject()
              .put("success", true)
              .put("channel", channel)
              .put("reqId", reqId)
              .put("data", (data instanceof List) ? data : JsonObject.mapFrom(data)).encode()
      );
    } catch (Exception e) {
        log.error("req write error: {}, {}", e.getMessage(), ExceptionUtil.getLimitedStackTrace(e));
    }
  }

  /**
   * 写入错误响应
   */
  public void writeErrorMessage(WSErrorCodeEnum errorCode) {
    try {
      writeBinaryMsgToClient(new JsonObject()
              .put("success", false)
              .put("time", Instant.now().toEpochMilli())
              .put("error_code", errorCode.getErrorCode())
              .put("error_msg", errorCode.getErrorMsg()).encode());
    } catch (Exception e) {
        log.error("req write error: {}, {}", e.getMessage(), ExceptionUtil.getLimitedStackTrace(e));
    }
  }
}
