package com.baixiaozheng.service.impl;

import com.baixiaozheng.common.util.DateTools;
import com.baixiaozheng.service.TimeService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeServiceImpl implements TimeService {
    @Override
    public String getTimeString() {
        Date now = new Date();
        String timeString = DateTools.dateTime2String(now);
        return timeString;
    }
}
