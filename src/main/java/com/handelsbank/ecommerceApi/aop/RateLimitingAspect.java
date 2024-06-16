package com.handelsbank.ecommerceApi.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RateLimitingAspect {
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> timestamps = new ConcurrentHashMap<>();

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(rateLimit) && execution(public * *(..))")
    public void rateLimit(RateLimit rateLimit) throws Exception {
        String key = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new AtomicInteger(0));
        timestamps.putIfAbsent(key, currentTime);

        synchronized (requestCounts.get(key)) {
            if (currentTime - timestamps.get(key) > rateLimit.duration()) {
                timestamps.put(key, currentTime);
                requestCounts.get(key).set(0);
            }
            AtomicInteger count = requestCounts.get(key);
            if (count.incrementAndGet() > rateLimit.requests()) {
                throw new RuntimeException("Rate limit exceeded");
            }
        }
    }
}
