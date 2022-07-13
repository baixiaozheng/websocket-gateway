package com.baixiaozheng.core.topic;

import com.alibaba.fastjson.JSONObject;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.service.TimeService;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Order(100)
@Component
public class TimeTopic extends AbstractTopicService{

    @Autowired
    private TimeService timeService;

    @PostConstruct
    private void init() {
        /**
         * {"channel":"socket.time.5s","event":"addChannel"}
         */
        this.regexChannel = "[socket]+\\.[time]+\\.[a-zA-Z0-9·/]+$";
    }

    @Override
    public void doAddChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        handlerData(session, channelStr, msgJson);
    }

    @Override
    public void doReqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        handlerData(session, channelStr, msgJson);
    }

    private void handlerData(Session session, String channelStr, JsonObject msgJson) {
        String[] topicArray = StringUtils.split(channelStr, ".");
        String step = topicArray[2];
        vertx.executeBlocking(future -> {
            String timeString = timeService.getTimeString();
            String json = "{\"time\":\""+timeString+"\"}";
            future.complete(JSONObject.parseObject(json));
        }, res -> {
            if (res.succeeded()) {
                session.responseSuccessData(channelStr, res.result(), "");
            } else {
                log.error("res is fail");
                session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
            }

        });
    }

    @Override
    public void doRemoveChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        log.info(channelStr + "在remove：" + msgJson);
    }

    @Override
    public Set<String> expressionFormat() {
        String expression = "socket.time.%s";
        List<String> timeStep = Arrays.asList("5s","10s","30s");
        this.topics.addAll(timeStep.stream().map(format -> String.format(expression, format)).collect(Collectors.toSet()));
        return topics;
    }
}
