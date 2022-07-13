package com.baixiaozheng.job;

import com.baixiaozheng.common.util.DateTools;
import com.baixiaozheng.handler.upstream.SocketNotifyVo;
import com.baixiaozheng.rabbitmq.producer.WebSocketProduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TimerJob {

    @Autowired
    private WebSocketProduce webSocketProduce;

    @Scheduled(fixedDelay = 1_000, initialDelay = 10_000)
    public void timer(){

        Date now = new Date();
        int second = DateTools.getSecond(now);
        String dateTimeString = DateTools.dateTime2String(now);

        if(second % 5 == 0){
            sendMessage(dateTimeString, "5s");
        }
        if(second % 10 == 0){
            sendMessage(dateTimeString, "10s");
        }
        if(second % 30 == 0){
            sendMessage(dateTimeString, "30s");
        }

    }

    private void sendMessage(String dateTimeString, String step){
        SocketNotifyVo vo = new SocketNotifyVo();
        vo.setChannel("socket.time."+step)
                .setTicker(dateTimeString);
        webSocketProduce.sendOrdinaryMessage(vo);
    }

}
