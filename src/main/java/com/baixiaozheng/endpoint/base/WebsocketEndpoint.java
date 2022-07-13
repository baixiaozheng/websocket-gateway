package com.baixiaozheng.endpoint.base;

import com.baixiaozheng.common.util.ExceptionUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class WebsocketEndpoint {


  @Getter
  @Setter
  private int msgShardNo = -1;

  protected void onOpen(ServerWebSocket ws, String path) {
    //log.error("onOpen: {}", ws.textHandlerID());
  }

  protected void onClose(ServerWebSocket ws) {
      log.error("onClose: {}", ws.textHandlerID());
//    try {
//      ws.close();
//    } catch (Throwable t) {
//      log.error("onClose: close exception: {} {}", t.getMessage(), t.getStackTrace());
//    }
  }

  protected void onTextMessage(String msg, ServerWebSocket ws) {
    log.debug("onTextMessage: {}", ws.textHandlerID());
  }

  protected void onException(Throwable t, ServerWebSocket ws) {
      log.error("onException: {} {}", t.getMessage(), ExceptionUtil.getLimitedStackTrace(t));
  }

  protected void onBinaryMessage(Buffer buf, ServerWebSocket ws) {
    log.error("onBinaryMessage: {}", ws.textHandlerID());
  }

  protected void onPong(Buffer buf, ServerWebSocket ws) {
    log.error("onPong: {}", ws.textHandlerID());
  }

  protected void onEnd(ServerWebSocket ws) {
    log.error("onEnd: {}", ws.textHandlerID());
  }

  protected void onDrain(ServerWebSocket ws) {
    log.error("onDrain: {}", ws.textHandlerID());
  }

  protected void onFrame(WebSocketFrame f, ServerWebSocket ws) {
    log.error("onFrame: {}", ws.textHandlerID());
  }

  public void shutdown() {

  }

  public void accept(ServerWebSocket ws, String uri) {
//        if(this.msgShardNo==-1){
//            this.msgShardNo=msgShardNo;
//        }
    try {
      this.onOpen(ws, uri);
      ws.pongHandler(buf -> this.onPong(buf, ws))
              .binaryMessageHandler(buf -> this.onBinaryMessage(buf, ws))
              .textMessageHandler(msg -> this.onTextMessage(msg, ws))
              .endHandler(v -> this.onEnd(ws))
              .exceptionHandler(t -> this.onException(t, ws))
//            .drainHandler(v -> this.onDrain(ws))
//            .frameHandler(f -> this.onFrame(f, ws))
              .closeHandler(v -> this.onClose(ws));
    } catch (Exception e) {
        log.error("accept: {} {}", e.getMessage(), ExceptionUtil.getLimitedStackTrace(e));
    }

  }
}
