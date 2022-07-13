package com.baixiaozheng.endpoint;


import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.RichmanSmoothRateLimiter;
import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.common.util.ChannelUtil;
import com.baixiaozheng.core.EventDistribute;
import com.baixiaozheng.endpoint.base.WS;
import com.baixiaozheng.endpoint.base.WebsocketEndpoint;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import com.baixiaozheng.setting.TopicHelper;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@WS({SessionConstant.URI_V1})
@Scope(SCOPE_PROTOTYPE)
public class WsEndpoint extends WebsocketEndpoint {

    @Autowired
    private TopicHelper topicHelper;
    @Autowired
    private EventDistribute eventDistribute;
    @Autowired
    RedisClient redisClient;
    @Autowired
    private Vertx vertx;

    private HashMap<String, Session> sessionMap = new HashMap<>();
    @Getter
    @Setter
    private long publishedOrdinaryCount = 0L;
    @Getter
    @Setter
    private long publishedProprietaryCount = 0L;

    private static ConcurrentHashMap<String, RateLimiter> websocketCountMap = new ConcurrentHashMap<>();

    public static Set<String> alreadyTopic = new HashSet<>();

    public void init(int msgShardNo) {
        this.setMsgShardNo(msgShardNo);
        if (getMsgShardNo() != -1) {
//      vertx.setPeriodic(1000, h -> {
//        statsdClient.gauge("gateway.ws.session", sessionMap.size(), 1,
//                "vId:" + getMsgShardNo());
//      });
        }

        initSubTopics();
    }

    @Override
    protected void onOpen(ServerWebSocket ws, String uri) {
        super.onOpen(ws, uri);

        String serverName = getServerName(ws);
        Session session = new Session(ws, vertx, serverName);
        websocketCountMap.putIfAbsent(serverName, RichmanSmoothRateLimiter.create(4, 6));
        sessionMap.put(ws.textHandlerID(), session);
    }

    //可以addchannel；
    private boolean canReq(RateLimiter rateLimiter) {
        if (rateLimiter.tryAcquire()) {
            return true;
        } else {
            return false;
        }
    }

    private String getServerName(ServerWebSocket ws) {
        MultiMap map = ws.headers();
        if (StringUtils.isBlank(map.get("X-Forwarded-For"))) {
            return "127.0.0.1";
        }
        return map.get("X-Forwarded-For");
    }

    /**
     * 1.ping-pong处理
     * 2.addChannel处理
     * 3.remoceChannel处理
     *
     * @param msg
     * @param ws
     */
    @Override
    protected void onTextMessage(String msg, ServerWebSocket ws) {
        //log.info("get message msg:{}", msg);
        super.onTextMessage(msg, ws);
        Session session = sessionMap.get(ws.textHandlerID());
        JsonObject msgJson = new JsonObject(msg);
//    String serverName = getServerName(ws);
        if (msgJson.containsKey(ApiParamConstant.PING)) {
            doPing(session, msgJson);
        } else if (msgJson.containsKey(ApiParamConstant.EVENT)) {
            Object event = msgJson.getValue(ApiParamConstant.EVENT);
            if (ChannelUtil.vaildEvent(event)) {
                log.error("event vaild failed:{}", event);
                session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
            } else {
                eventDistribute.eventDistribute(session, getMsgShardNo(), msgJson);
            }
        } else {
            log.error("event not be found");
            session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
        }
    }

    /**
     * ping请求的处理
     *
     * @param session
     * @param msgJson
     */
    private void doPing(Session session, JsonObject msgJson) {
        JsonObject result = new JsonObject();
//    Object pingObj = msgJson.getValue(ApiParamConstant.PING);
//    if (pingObj instanceof Long) {
        result.put(ApiParamConstant.PONG, msgJson.getString(ApiParamConstant.PING));
        session.writeRespMessage(result);
//    }
    }

    @Override
    public void onException(Throwable t, ServerWebSocket ws) {
        super.onException(t, ws);
    }

    @Override
    public void onClose(ServerWebSocket ws) {
        super.onClose(ws);
        Session session = sessionMap.get(ws.textHandlerID());

//    redisClient.sRemove(SessionConstant.CFD_CURRENCY_SOCKET_FAVOURITE_CUSTOMERIDS, session.getCustomerId());
//    redisClient.sRemove(SessionConstant.CFD_ORDER_INCOME_CUSTOMERIDS, session.getCustomerId());
        sessionMap.remove(ws.textHandlerID());
    }


    private void initSubTopics() {
        Set<String> topics = topicHelper.getSubTopics();
        topics.stream().forEach(topic -> {
//            if(!alreadyTopic.contains(topic)) {
//            log.info("*******add now topic {}"+topic);
            MessageConsumer<String> consumer = vertx.eventBus().localConsumer(SessionConstant.EVENTBUS_TOPIC_PREFIX + topic);
            consumer.handler(message -> {
                sendBySessionMap(message, topic);
            });
            //alreadyTopic.add(topic);
//            }
        });
    }

    // @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshSubTopics() {
        log.info("******* refresh topics ********");
        //initSubTopics();
        log.info("topics size {}", topicHelper.getSubTopics().size());
        log.info("alreadyTopic size {}",alreadyTopic.size());
    }

    /**
     * 每个线程遍历自己管理的session集合
     * 如果session订阅topic则发送，反之则不发
     */
    private void sendBySessionMap(Message<String> message, String topic) {
        //balance做特殊处理
        String type = message.headers().get(SessionConstant.EVENTBUS_MESSAGE_TYPE);
//        if (SessionConstant.EVENTBUS_MESSAGE_TYPE_BALANCE.equals(type)) {
//            String userId = message.headers().get(SessionConstant.EVENTBUS_CUSTOMER_ID);
//            sessionMap.values().stream().forEach(session -> {
//                if (session.isBalance() && userId.equals(String.valueOf(session.getUserId())) && session.containsTopic(topic)) {
//                    publishedBalanceCount++;
////                    log.info("publish balance msg to eventbus :{}", message.body().toString());
//                    session.writeNotifyMessage(message.body());
//                }
//            });
//        } else if (SessionConstant.EVENTBUS_MESSAGE_TYPE_QUOTATION.equals(type)) {
//            sessionMap.values().stream().forEach(session -> {
//                if (session.containsTopic(topic)) {
//                    publishedQuotationCount++;
//                       // log.info("publish quotation msg to eventbus :{}", message.body().toString());
//                    session.writeNotifyMessage(message.body());
//                }
//            });
//        }

        if(SessionConstant.EVENTBUS_MESSAGE_TYPE_PROPRIETARY.equals(type)){
            String userId = message.headers().get(SessionConstant.EVENTBUS_CUSTOMER_ID);
            sessionMap.values().stream().forEach(session -> {
                if (session.isProprietary() && userId.equals(String.valueOf(session.getUserId())) && session.containsTopic(topic)) {
                    publishedProprietaryCount++;
//                    log.info("publish balance msg to eventbus :{}", message.body().toString());
                    session.writeNotifyMessage(message.body());
                }
            });
        } else if(SessionConstant.EVENTBUS_MESSAGE_TYPE_ORDINARY.equals(type)){
            sessionMap.values().stream().forEach(session -> {
                if (session.containsTopic(topic)) {
                    publishedOrdinaryCount++;
                       // log.info("publish quotation msg to eventbus :{}", message.body().toString());
                    session.writeNotifyMessage(message.body());
                }
            });
        }

    }
}