package com.baixiaozheng.verticle;


import com.baixiaozheng.common.vertx.VertxProducer;
import com.baixiaozheng.endpoint.WsEndpoint;
import com.baixiaozheng.endpoint.base.EndpointRegister;
import com.baixiaozheng.handler.HttpRequestExceptionHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;


@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class GatewayVerticle extends AbstractVerticle {

  @Autowired
  Vertx vertx;
  @Autowired
  ApplicationContext applicationContext;
  @Autowired
  VertxProducer vertxProducer;
  @Resource
  private EndpointRegister wshandler;
  private HttpServer httpServer;
  @Value("${websocket-gateway.vertx.ws.host}")
  private String host;
  @Value("${websocket-gateway.vertx.ws.port}")
  private Integer port;
  @Getter
  @Setter
  private int msgShardNo;


  @Override
  public void start() {
    HttpServerOptions opts = new HttpServerOptions().setTcpFastOpen(true)
            .setTcpKeepAlive(true)
            .setSendBufferSize(1024 * 1024)
            .setTcpNoDelay(true)
            .setHost(host)
            .setPort(port)
            //支持可压缩
            //.setCompressionSupported(true)
            ;
    JsonObject config = config();
    msgShardNo = config.getInteger("msgShardNo");

    WsEndpoint wsEndpoint = (WsEndpoint) wshandler.get(WsEndpoint.class);
    wsEndpoint.init(msgShardNo);

    vertx.setPeriodic(1000, timerId -> {
      if (wsEndpoint.getPublishedOrdinaryCount() > 10_0000_0000) {
        wsEndpoint.setPublishedOrdinaryCount(0L);
      }
      if (wsEndpoint.getPublishedProprietaryCount() > 10_0000_0000) {
        wsEndpoint.setPublishedProprietaryCount(0L);
      }
    });

    Router router = Router.router(vertx);
    httpServer = vertx.createHttpServer(opts)
            .websocketHandler(conn -> wshandler.dispatch(conn))
            .requestHandler(router::handle)
            .exceptionHandler(new HttpRequestExceptionHandler())
            .listen();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    httpServer.close();
  }
}
