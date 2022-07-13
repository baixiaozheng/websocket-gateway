package com.baixiaozheng.handler.upstream;

import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.common.util.ExceptionUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.rabbitmq.RabbitMQConsumer;
import io.vertx.rabbitmq.RabbitMQMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqProprietaryHandler implements Handler<AsyncResult<RabbitMQConsumer>> {
  @Autowired
  private Vertx vertx;

  @Getter
  @Setter
  private long receivedCount = 0L;
  @Getter
  @Setter
  private long sendedCount = 0L;

  @Override
  public void handle(AsyncResult<RabbitMQConsumer> r) {
    if (r.succeeded()) {
      log.info("RabbitMQ consumer proprietary created !");
      RabbitMQConsumer mqConsumer = r.result();
      mqConsumer.handler(new RabbitMqMsgBodyHandler());
    } else {
        log.error("proprietary onTextMessage exception, {} {}", r.cause().getMessage(), ExceptionUtil.getLimitedStackTrace(r.cause()));
    }
  }

  /**
   * 消息的具体处理类
   */
  private class RabbitMqMsgBodyHandler implements Handler<RabbitMQMessage> {
    @Override
    public void handle(RabbitMQMessage message) {
      receivedCount++;
//      log.info("Got balance message: " + message.body().toString());
      if (message != null) {
        ChannelNotifyVo vo = message.body().toJsonObject().mapTo(ChannelNotifyVo.class);
//        String[] topicArray = vo.getChannel().split("\\.");
//        symbolHelper.getTradeEnableSymbols().keySet().contains(topicArray[1]) &&
//        if ("market.order.income".equals(vo.getChannel())) {
//          System.out.println("=========RabbitMqBalanceHandler========" + vo.getChannel());
//          System.out.println("=========RabbitMqBalanceHandler========" + vo.getMessage());
//        }
        if (null != vo.getUserId()) {
          sendedCount++;
 //          log.info("send balance msg to eventbus :{}", message.body().toString());
          vertx.eventBus().publish(SessionConstant.EVENTBUS_TOPIC_PREFIX + vo.getChannel(), vo.getMessage(), new DeliveryOptions().addHeader(SessionConstant.EVENTBUS_MESSAGE_TYPE, SessionConstant.EVENTBUS_MESSAGE_TYPE_PROPRIETARY).addHeader(SessionConstant.EVENTBUS_CUSTOMER_ID, String.valueOf(vo.getUserId())));
        }
      }
    }
  }
}