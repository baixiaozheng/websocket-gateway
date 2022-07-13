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
public class RabbitMqOrdinaryHandler implements Handler<AsyncResult<RabbitMQConsumer>> {
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
      log.info("RabbitMQ consumer ordinary created !");
      RabbitMQConsumer mqConsumer = r.result();
      mqConsumer.handler(new RabbitMqMsgBodyHandler());
    } else {
        log.error("ordinary onTextMessage exception, {} {}", r.cause().getMessage(), ExceptionUtil.getLimitedStackTrace(r.cause()));
    }
  }

  /**
   * 消息的具体处理类
   */
  private class RabbitMqMsgBodyHandler implements Handler<RabbitMQMessage> {
    @Override
    public void handle(RabbitMQMessage message) {
      receivedCount++;
//        log.info("Got quotation message: " + message.body().toString());
      if (message != null) {
        try {
          ChannelNotifyVo vo = message.body().toJsonObject().mapTo(ChannelNotifyVo.class);
          sendedCount++;
//            log.info("send quotation msg to eventbus :{}", message.body().toString());
          vertx.eventBus().publish(SessionConstant.EVENTBUS_TOPIC_PREFIX + vo.getChannel(), vo.getMessage(), new DeliveryOptions().addHeader(SessionConstant.EVENTBUS_MESSAGE_TYPE, SessionConstant.EVENTBUS_MESSAGE_TYPE_ORDINARY));
        } catch (Throwable t) {
            log.error("error: {}, {}", t.getMessage(), ExceptionUtil.getLimitedStackTrace(t));
        }
      }
    }
  }
}
