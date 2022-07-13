package com.baixiaozheng.handler.upstream;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
public class ChannelNotifyVo implements Serializable {
  private static final long serialVersionUID = 4493514088575871785L;
  /**
   * 发送的频道
   */
  @Getter @Setter
  private String channel;
  /**
   * 发送的消息
   */
  @Getter @Setter
  private String message;

  @Getter @Setter
  private String userId;



  @Override
  public String toString() {
    return "ChannelNotifyVo{" +
            "channel='" + channel + '\'' +
            ", message='" + message + '\'' +
            ", userId='" + userId + '\'' +
            '}';
  }
}