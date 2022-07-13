package com.baixiaozheng.endpoint.base;

import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.common.util.ZipUtil;
import io.vertx.core.http.ServerWebSocket;

public class ErrorEndpoint extends WebsocketEndpoint {
  private ZipUtil zipUtil = new ZipUtil();

  @Override
  protected void onOpen(ServerWebSocket ws, String uri) {
    ws.writeBinaryMessage(zipUtil.compress("Websocket Error on " + ws.path(), ApiParamConstant.UTF8));
    ws.close();
  }
}
