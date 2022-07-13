package com.baixiaozheng.job;

import com.alibaba.fastjson.JSONObject;
import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.common.setting.manager.bean.User;
import com.baixiaozheng.common.util.CollectionUtils;
import com.baixiaozheng.common.util.DateTools;
import com.baixiaozheng.handler.upstream.SocketNotifyVo;
import com.baixiaozheng.rabbitmq.producer.WebSocketProduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Component
public class NameJob {
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private WebSocketProduce webSocketProduce;

    @Scheduled(fixedDelay = 10_000, initialDelay = 10_000)
    public void timer(){

        Map<String, Object> userMap = redisClient.getCacheMap(SessionConstant.SOCKET_USER_IDS);
        Date now = new Date();
        if(CollectionUtils.isNotEmpty(userMap)){
            userMap.forEach((key, value)->{
                User user = JSONObject.parseObject(value.toString(), User.class);
                SocketNotifyVo vo = new SocketNotifyVo();
                vo.setChannel("socket.name")
                        .setTicker(user.getAge()+"岁的"+user.getName()+"，你好，现在是"+ DateTools.dateTime2String(now));
                webSocketProduce.sendProprietaryMessage(vo,key);
                    });
        }

    }
}
