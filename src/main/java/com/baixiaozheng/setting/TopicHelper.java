package com.baixiaozheng.setting;

import com.baixiaozheng.setting.manager.TopicManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class TopicHelper {

  @Autowired
  private TopicManager topicManager;


  public Set<String> getSubTopics() {
    return topicManager.getSubTopics();
  }
}
