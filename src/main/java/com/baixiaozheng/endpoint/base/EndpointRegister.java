package com.baixiaozheng.endpoint.base;

import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class EndpointRegister extends ApplicationObjectSupport {

  private Map<String, WebsocketEndpoint> endpoints = new HashMap<>();

  private Map<Class<? extends WebsocketEndpoint>, WebsocketEndpoint> endpointClasss = new HashMap<>();

  private ErrorEndpoint errorEndpoint = new ErrorEndpoint();


  @Override
  public void initApplicationContext(ApplicationContext context) throws BeansException {
    Map<String, Object> beans = context.getBeansWithAnnotation(WS.class);

    beans.values().stream()
            .filter(o -> o instanceof WebsocketEndpoint)
            .map(o -> (WebsocketEndpoint) o)
            .forEach(o -> {
              Arrays.asList(o.getClass().getDeclaredAnnotation(WS.class).value()).forEach(s -> {
                endpoints.put(validate(s), o);
              });
            });

    endpointClasss.putAll(beans.values().stream()
            .filter(o -> o instanceof WebsocketEndpoint)
            .map(o -> (WebsocketEndpoint) o)
            .collect(Collectors.toMap(o -> o.getClass(), o -> o)));
  }

  public WebsocketEndpoint get(Class<? extends WebsocketEndpoint> clazz) {
    return endpointClasss.get(clazz);
  }

  public void dispatch(ServerWebSocket ws) {
    endpoints.getOrDefault(validate(ws.path()), errorEndpoint).accept(ws, ws.path());
  }

  private String validate(String path) {
    StringBuilder sb = new StringBuilder("/");
    char[] cs = path.toCharArray();
    for (char c : cs) {
      if (c == '/') {
        if (sb.charAt(sb.length() - 1) != '/') {
          sb.append('/');
        }
      } else {
        sb.append(c);
      }
    }
    if (sb.charAt(sb.length() - 1) != '/') {
      sb.append('/');
    }
    return sb.toString();
  }
}
