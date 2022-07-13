package com.google.common.util.concurrent;


import com.google.common.util.concurrent.RateLimiter.SleepingStopwatch;
import com.google.common.util.concurrent.SmoothRateLimiter.SmoothBursty;

public class RichmanSmoothRateLimiter {
    public static RateLimiter create(double permitsPerSecond) {
        RateLimiter rateLimiter = new SmoothBursty(SleepingStopwatch.createFromSystemTimer(), 3 /* maxBurstSeconds */);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    public static RateLimiter create(double permitsPerSecond, Integer maxBurstSeconds) {
        RateLimiter rateLimiter = new SmoothBursty(SleepingStopwatch.createFromSystemTimer(), maxBurstSeconds /* maxBurstSeconds */);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }
}
