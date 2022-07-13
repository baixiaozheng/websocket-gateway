package com.baixiaozheng.core.topic;

import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.jwt.JwtHelper;
import com.baixiaozheng.session.Session;
import io.jsonwebtoken.Claims;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractTopicService implements TopicService {

    protected String regexChannel;

    @Autowired
    Vertx vertx;

    @Getter
    Set<String> topics = new HashSet<>();

    @Override
    public Boolean match(String channelStr) {
        return Pattern.matches(regexChannel, channelStr);
    }

    @Override
    public Set<String> topics() {
        return expressionFormat();
    }

    @Override
    public void addChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        if (!session.sub(channelStr)) {
            log.error("add channel to sessionMap error:{}", channelStr);
            session.writeErrorMessage(WSErrorCodeEnum.SERVER_INTERNAL_ERROR);
        } else {
            doAddChannel(session, msgShardNo, msgJson, channelStr);
        }
    }

    public void doAddChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        if (!session.unsub(channelStr)) {
            session.writeErrorMessage(WSErrorCodeEnum.SERVER_INTERNAL_ERROR);
        } else {
            doRemoveChannel(session, msgShardNo, msgJson, channelStr);
        }
    }

    public void doRemoveChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        doReqChannel(session, msgShardNo, msgJson, channelStr);
    }

    public void doReqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        throw new UnsupportedOperationException();
    }

    public Set<String> expressionFormat() {
        return new HashSet<>();
    }


    String getUserId(String token){
        String[] params = token.split("_");
        String userId = params[1];
        return userId;
    }

}
