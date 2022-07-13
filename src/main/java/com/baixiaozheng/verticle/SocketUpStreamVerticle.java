package com.baixiaozheng.verticle;

import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.common.util.ExceptionUtil;
import com.baixiaozheng.handler.upstream.RabbitMqOrdinaryHandler;
import com.baixiaozheng.handler.upstream.RabbitMqProprietaryHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.rabbitmq.RabbitMQClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SocketUpStreamVerticle extends AbstractVerticle {

//  @Value("${websocket-gateway.mq.quotation-queue}")
//  private String quotationQueue;
//  @Value("${websocket-gateway.mq.balance-queue}")
//  private String balanceQueue;
  @Value("${websocket-gateway.mq.proprietary-queue}")
  private String proprietaryQueue;
  @Value("${websocket-gateway.mq.ordinary-queue}")
  private String ordinaryQueue;
  @Value("${info.app.seq}")
  private String seq;

  @Autowired
  private Vertx vertx;
  @Resource
  private RabbitMQClient mqClient;

//  @Autowired
//  private RabbitMqQuotationHandler qHandler;
//  @Autowired
//  private RabbitMqBalanceHandler bHandler;

  @Autowired
  private RabbitMqProprietaryHandler pHandler;

  @Autowired
  private RabbitMqOrdinaryHandler oHandler;
  @Override
  public void start() throws Exception {
    super.start();
    mqClient.start(ar -> {
      if (ar.succeeded()) {

        mqClient.queueDeclare(ordinaryQueue + seq, true, true, true, queueResult -> {
          if (queueResult.succeeded()) {
            log.info("declare ordinaryQueue success");
            mqClient.queueBind(ordinaryQueue + seq, SessionConstant.MQ_EXCHANGE_NAME, SessionConstant.MQ_ORDINARY_ROUTINGKEY, bindResult -> {
              if (bindResult.succeeded()) {
                log.info("bind ordinaryQueue success");
                mqClient.basicConsumer(ordinaryQueue + seq, oHandler);
              } else {
                log.error("bind ordinaryQueue error : {}, message: {}", ExceptionUtil.getLimitedStackTrace(bindResult.cause()), bindResult.cause().getMessage());
              }
            });
          } else {
            log.error("declare ordinaryQueue error : {}, message: {}", ExceptionUtil.getLimitedStackTrace(queueResult.cause()), queueResult.cause().getMessage());
          }
        });

        mqClient.queueDeclare(proprietaryQueue + seq, true, true, true, queueResult -> {
          if (queueResult.succeeded()) {
            log.info("declare proprietaryQueue success");
            mqClient.queueBind(proprietaryQueue + seq, SessionConstant.MQ_EXCHANGE_NAME, SessionConstant.MQ_PROPRIETARY_ROUTINGKEY, bindResult -> {
              if (bindResult.succeeded()) {
                log.info("bind proprietaryQueue success");
                mqClient.basicConsumer(proprietaryQueue + seq, pHandler);
              } else {
                log.error("bind proprietaryQueue error : {}, message: {}", ExceptionUtil.getLimitedStackTrace(bindResult.cause()), bindResult.cause().getMessage());
              }
            });
          } else {
            log.error("declare proprietaryQueue error : {}, message: {}", ExceptionUtil.getLimitedStackTrace(queueResult.cause()), queueResult.cause().getMessage());
          }
        });



      } else {
          log.error("start mqclient error : {}, message: {}", ExceptionUtil.getLimitedStackTrace(ar.cause()), ar.cause().getMessage());
      }
    });
    vertx.setPeriodic(1000, timerId -> {
      if (oHandler.getReceivedCount() > 10_0000_0000) {
        oHandler.setReceivedCount(0L);
      }
      if (oHandler.getSendedCount() > 10_0000_0000) {
        oHandler.setSendedCount(0L);
      }
      if (pHandler.getReceivedCount() > 10_0000_0000) {
        pHandler.setReceivedCount(0L);
      }
      if (pHandler.getSendedCount() > 10_0000_0000) {
        pHandler.setSendedCount(0L);
      }
    });
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
