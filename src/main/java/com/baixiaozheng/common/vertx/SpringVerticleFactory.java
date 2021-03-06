package com.baixiaozheng.common.vertx;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringVerticleFactory implements VerticleFactory, ApplicationContextAware {

  private ApplicationContext context;

  @Override
  public boolean blockingCreate() {
    return true;
  }

  @Override
  public String prefix() {
    return "spring";
  }

  @Override
  public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
    String clazz = VerticleFactory.removePrefix(verticleName);
    return (Verticle) context.getBean(Class.forName(clazz));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
