package com.baixiaozheng.core.topic;

import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;

import java.util.Set;

public interface TopicService {

  Set<String> topics();

  Boolean match(String channelStr);

  void addChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr);

  void removeChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr);

  void reqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr);
}
