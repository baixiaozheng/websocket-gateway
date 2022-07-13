package com.baixiaozheng.core;

import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.core.event.EventService;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order
@Slf4j
@Component
public class EventDistribute implements ApplicationContextAware {

    private ApplicationContext context;

    private List<EventService> clients;


    @PostConstruct
    private void init() {
        clients = new ArrayList<>();
        Map<String, EventService> map = this.context.getBeansOfType(EventService.class);
        map.values().forEach(c -> {
            clients.add(c);
        });
    }

    public void eventDistribute(Session session, int msgShardNo, JsonObject msgJson) {
        Object event = msgJson.getValue(ApiParamConstant.EVENT);
        for (EventService eventService : clients) {
            if (eventService.match(event)) {
                eventService.event(session, msgShardNo, msgJson);
                return;
            }
        }
        log.error("event error:{}", event);
        session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
