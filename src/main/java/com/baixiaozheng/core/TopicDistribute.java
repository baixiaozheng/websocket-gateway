package com.baixiaozheng.core;

import com.baixiaozheng.core.topic.TopicService;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Order
@Slf4j
@Component
public class TopicDistribute implements ApplicationContextAware {

    private ApplicationContext context;

    private List<TopicService> clients;


    @PostConstruct
    private void init() {
        clients = new ArrayList<>();
        Map<String, TopicService> map = this.context.getBeansOfType(TopicService.class);
        map.values().forEach(c -> {
            clients.add(c);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public Set<String> topics() {
        Set<String> topics = new HashSet<>();
        for (TopicService topicService : clients) {
            topics.addAll(topicService.topics());
        }
        return topics;
    }

    public void addChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        for (TopicService topicService : clients) {
            if (topicService.match(channelStr)) {
                topicService.addChannel(session, msgShardNo, msgJson, channelStr);
                return;
            }
        }
        log.error("add topic not be found:{}",channelStr);
        session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
    }

    public void removeChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        for (TopicService topicService : clients) {
            if (topicService.match(channelStr)) {
                topicService.removeChannel(session, msgShardNo, msgJson, channelStr);
                return;
            }
        }
        log.error("remove topic not be found:{}",channelStr);
        session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
    }

    public void reqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        for (TopicService topicService : clients) {
            if (topicService.match(channelStr)) {
                topicService.reqChannel(session, msgShardNo, msgJson, channelStr);
            }
        }
        log.error("req topic not be found:{}",channelStr);
        session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
    }
}
