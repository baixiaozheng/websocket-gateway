package com.baixiaozheng.common.setting.bean;

import com.google.common.collect.ImmutableSet;
import com.baixiaozheng.common.setting.eunms.TopicEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Topic {

  private String expression;

  private Boolean isFormat;

  private Boolean isLocal;

  private TopicEnum topicEnum;

  private ImmutableSet<String> localSet;

}
