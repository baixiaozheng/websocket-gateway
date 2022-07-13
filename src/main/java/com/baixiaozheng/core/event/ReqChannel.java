package com.baixiaozheng.core.event;

import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.common.util.ChannelUtil;
import com.baixiaozheng.core.TopicDistribute;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(100)
@Component
public class ReqChannel implements EventService {


  @Autowired
  private TopicDistribute topicDistribute;

  @Override
  public Boolean match(Object event) {
    return ApiParamConstant.REQ.equals(event);
  }

  @Override
  public void event(Session session, int msgShardNo, JsonObject msgJson) {
    Object channel = msgJson.getValue(ApiParamConstant.CHANNEL);
    if (ChannelUtil.vaildChannel(channel)) {
        log.error("reqchannel vaild failed:{}", channel);
      session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
    }else{
      String channelStr = String.valueOf(channel);
      topicDistribute.reqChannel(session, msgShardNo, msgJson, channelStr);
    }
  }


}
