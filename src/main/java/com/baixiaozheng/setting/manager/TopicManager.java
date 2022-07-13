package com.baixiaozheng.setting.manager;

import com.google.common.collect.ImmutableSet;
import com.baixiaozheng.common.setting.bean.Topic;
import com.baixiaozheng.core.TopicDistribute;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TopicManager {

  @Getter
  private Set<Topic> topics;

//  @Autowired
//  private SymbolManager symbolManager;
//
//  @Autowired
//  private CurrencyManager currencyManager;

  @Getter
  private Set<String> topicList;

  @Autowired
  private TopicDistribute topicDistribute;

//  @PostConstruct
//  void init() {
////    topicList = topicDistribute.topics();
//    topicList.forEach(System.out::println);
//  }


  public void reload() {
    topicList = topicDistribute.topics();
//    topicList.forEach(System.out::println);
  }


  public Set<String> getSubTopics() {
    return topicList;
  }


  private Set<String> expressionFormat(Topic topic, Set<String> strings, ImmutableSet<String> localSet) {
    Set<String> collect = new HashSet<>();
    if (topic.getIsFormat()) {
      collect.add(topic.getExpression());
      return collect;
    } else {
      if (topic.getIsLocal()) {
        topic.setLocalSet(localSet);
        collect = topic.getLocalSet().stream().map(format -> String.format(topic.getExpression(), format)).collect(Collectors.toSet());
      } else {
        collect = strings.stream().map(format -> String.format(topic.getExpression(), format)).collect(Collectors.toSet());
      }
    }
    return collect;
  }
}
