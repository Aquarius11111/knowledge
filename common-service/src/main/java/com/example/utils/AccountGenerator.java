package com.example.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AccountGenerator {
    // 格式化时间戳为yyMMdd格式
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd");
    // 每日计数器，用于生成唯一序号
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * 生成8位账号（基于时间戳+计数器）
     * 格式：yyMMdd + 两位计数器（00-99）
     */
    public static String generateAccount() {
        // 获取当前日期部分（6位）
        String datePart = DATE_FORMAT.format(new Date());

        // 获取计数器部分（2位，范围00-99）
        int count = COUNTER.getAndIncrement() % 100;
        String countPart = String.format("%02d", count);

        return datePart + countPart;
    }

    /**
     * 重置计数器（建议每天零点调用）
     */
    public static void resetCounter() {
        COUNTER.set(0);
    }
}