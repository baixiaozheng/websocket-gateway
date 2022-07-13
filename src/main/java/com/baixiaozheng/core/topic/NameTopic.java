package com.baixiaozheng.core.topic;

import com.alibaba.fastjson.JSONObject;
import com.baixiaozheng.common.constant.ApiParamConstant;
import com.baixiaozheng.common.constant.SessionConstant;
import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.common.setting.manager.bean.User;
import com.baixiaozheng.common.util.ExceptionUtil;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;


@Slf4j
@Order(100)
@Component
public class NameTopic extends AbstractTopicService{

    @Autowired
    private RedisClient redisClient;


    @PostConstruct
    private void init() {
        /**
         * {"channel":"socket.name","event":"addChannel","token":"2_1_2"}
         */
        this.regexChannel = "(socket)\\.(name)$";
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
        Object authorization = msgJson.getValue(ApiParamConstant.TOKEN);
        log.info("authorization {}",authorization.toString());
        vertx.executeBlocking(futureForId -> {
            String uid = getUserId(authorization.toString());
            if(StringUtils.isBlank(uid)){
                session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
                futureForId.fail("identity is null error:"+String.valueOf(authorization));
            } else {
                futureForId.complete(uid);
            }
        }, resForId -> {
            if (resForId.succeeded()) {
                session.setProprietary(true, resForId.result().toString());
                if (!session.sub(channelStr)) {
                    log.error("add channel to sessionMap error:{}", channelStr);
                    session.writeErrorMessage(WSErrorCodeEnum.SERVER_INTERNAL_ERROR);
                } else {
                    //添加customerId到redis
//                    redisClient.sSet(SessionConstant.SOCKET_USER_IDS, session.getUserId());
                    vertx.executeBlocking(futureForData -> {
                        Object o = redisClient.getDataFromCacheMap(SessionConstant.SOCKET_USER_IDS, session.getUserId());
                        User u = JSONObject.parseObject(o.toString(), User.class);
                        futureForData.complete(u);
                    }, resForData -> {
                        if (resForData.succeeded()) {
                            session.responseSuccessData(channelStr, resForData.result(), "");
                        } else {
                            log.error("getCustomerData error:{}, {}", resForData.cause().getMessage(), ExceptionUtil.getLimitedStackTrace(resForData.cause()));
                            session.writeErrorMessage(WSErrorCodeEnum.TOKEN_ERROR);
                        }
                    });
                }
            } else {
                log.error("res is fail");
                session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
            }

        });
    }




    @Override
    public void doRemoveChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        redisClient.sRemove(SessionConstant.SOCKET_USER_IDS , session.getUserId());
        log.info(channelStr + "在remove：" + msgJson);
    }

    @Override
    public Set<String> expressionFormat() {

        this.topics.add("socket.name");
        log.info("add socket.name");
        log.info(JSONObject.toJSONString(topics));
        return topics;
    }


}
