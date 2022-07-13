package com.baixiaozheng.rabbitmq.producer;

import com.alibaba.fastjson.JSON;
import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.handler.upstream.ChannelNotifyVo;
import com.baixiaozheng.handler.upstream.SocketNotifyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class WebSocketProduce {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendOrdinaryMessage(SocketNotifyVo notifyVo) {
		ChannelNotifyVo channelNotifyVo = new ChannelNotifyVo();
		channelNotifyVo.setChannel(notifyVo.getChannel());
		channelNotifyVo.setMessage(JSON.toJSONString(notifyVo));
		if (log.isDebugEnabled()) {
			log.info("{} to send message:{}", SessionConstant.MQ_ORDINARY_ROUTINGKEY, channelNotifyVo);
		}
		rabbitTemplate.convertAndSend(SessionConstant.MQ_ORDINARY_ROUTINGKEY, JSON.toJSONString(channelNotifyVo));
	}

	public void sendProprietaryMessage(SocketNotifyVo notifyVo, String userId) {
		ChannelNotifyVo channelNotifyVo = new ChannelNotifyVo();
		channelNotifyVo.setChannel(notifyVo.getChannel());
		channelNotifyVo.setMessage(JSON.toJSONString(notifyVo));
		channelNotifyVo.setUserId(userId);
		if (log.isDebugEnabled()) {
			log.debug("{} to send message:{}", SessionConstant.MQ_PROPRIETARY_ROUTINGKEY, channelNotifyVo);
		}
		rabbitTemplate.convertAndSend(SessionConstant.MQ_PROPRIETARY_ROUTINGKEY, JSON.toJSONString(channelNotifyVo));
	}
}
