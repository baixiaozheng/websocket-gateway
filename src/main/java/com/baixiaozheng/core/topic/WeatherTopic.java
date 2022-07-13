package com.baixiaozheng.core.topic;

import com.alibaba.fastjson.JSONObject;
import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.common.setting.manager.CityManager;
import com.baixiaozheng.common.setting.manager.bean.City;
import com.baixiaozheng.common.util.HttpClientUtils;
import com.baixiaozheng.enums.WSErrorCodeEnum;
import com.baixiaozheng.session.Session;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Order(100)
@Component
public class WeatherTopic extends AbstractTopicService{

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private CityManager cityManager;
    private static String weatherUrl = "http://wthrcdn.etouch.cn/weather_mini?city=";
    @PostConstruct
    private void init() {
        /**
         * {"channel":"socket.weather.北京","event":"addChannel"}
         * redis数据格式：{"volume":3.840385,"high":9245.8,"low":9248.44,"time":1593657840,"close":9248.44,"open":9246.71}, {"volume":1.472079,"high":9246.11,"low":9247,"time":1593657780,"close":9246.41,"open":9247}
         */
        this.regexChannel = "[socket]+\\.[weather]+\\.[^\\x00-\\xff]+$";
    }

    @Override
    public void doAddChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        handlerData(session, channelStr, msgJson);
    }

    @Override
    public void doReqChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        handlerData(session, channelStr, msgJson);
    }

    private void handlerData(Session session, String channelStr, JsonObject msgJson) {
        String weatherKey = "socket:weather:";
        String[] topicArray = StringUtils.split(channelStr, ".");
        String city = topicArray[2];
        vertx.executeBlocking(future -> {
            if(!redisClient.exists(weatherKey+city)) {
                String w = HttpClientUtils.sendHttpGet(weatherUrl+city);

                redisClient.set(weatherKey+city,JSONObject.parseObject(w));
            }
            Object weather = redisClient.get(weatherKey + city);
            future.complete(weather);
        }, res -> {
            if (res.succeeded()) {
                session.responseSuccessData(channelStr, res.result(), "");
            } else {
                log.error("res is fail");
                session.writeErrorMessage(WSErrorCodeEnum.INVALID_PARAMETER);
            }

        });
    }

    @Override
    public void doRemoveChannel(Session session, int msgShardNo, JsonObject msgJson, String channelStr) {
        log.info(channelStr + "在remove：" + msgJson);
    }

    @Override
    public Set<String> expressionFormat() {

        String expression = "socket.weather.%s";

        List<City> cities = cityManager.getCities();
        List<String> cityNames= cities.stream().map(City::getCity).collect(Collectors.toList());
        this.topics.addAll(cityNames.stream().map(format -> String.format(expression, format)).collect(Collectors.toSet()));
        return topics;
    }

}
