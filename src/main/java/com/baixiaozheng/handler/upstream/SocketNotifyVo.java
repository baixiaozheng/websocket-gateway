package com.baixiaozheng.handler.upstream;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SocketNotifyVo {

	/**
	 * 订阅的channel
	 */
	private String channel;

	private Long date;

	/**
	 * 传送的数据
	 */
	private Object ticker;

}
