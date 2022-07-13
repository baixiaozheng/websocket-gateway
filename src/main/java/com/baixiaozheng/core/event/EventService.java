package com.baixiaozheng.core.event;

import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;

public interface EventService {

  Boolean match(Object event);

  void event(Session session, int msgShardNo, JsonObject msgJson);
}
