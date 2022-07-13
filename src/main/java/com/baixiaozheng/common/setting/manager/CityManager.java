package com.baixiaozheng.common.setting.manager;

import com.alibaba.fastjson.JSONObject;
import com.baixiaozheng.common.setting.client.RedisClient;
import com.baixiaozheng.common.setting.manager.bean.City;
import com.baixiaozheng.common.util.CollectionUtils;
import com.baixiaozheng.common.vo.KlineTicker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class CityManager {

    private Set<String> citySet = new HashSet<>();

    @Autowired
    private RedisClient redisClient;

    public static final String WEATHER_CITY_KEY = "weather:city";

    public List<City> getCities(){
        List<City> cities = new ArrayList<>();
        List<Object> objects =  redisClient.getListValue(WEATHER_CITY_KEY);
        if(CollectionUtils.isNotEmpty(objects)) {
            for (Object o : objects) {
                City c = JSONObject.parseObject(o.toString(), City.class);
                cities.add(c);
            }
        }
        return cities;
    }

    public void setCitySet(){
        List<City> cities = getCities();
        for(City c : cities){
            citySet.add(c.getCity());
        }
        log.info(JSONObject.toJSONString(citySet));
    }

}
