package com.baixiaozheng.handler;

import com.baixiaozheng.common.util.ExceptionUtil;
import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class HttpRequestExceptionHandler implements Handler<Throwable> {

  @Override
  public void handle(Throwable event) {
      log.error("http request error message : {}, {}", event.getMessage(), ExceptionUtil.getLimitedStackTrace(event));
  }
}
