package com.example.user.utils;

import java.util.*;

public class UsernameGenerator {

    // 可以自定义的一组名字，支持中英文
    private static final String[] NAME_POOL = {
            // 中文风格
            "晚风温柔", "山川与海", "碎星辰", "倾城月", "漫山花开", "夏末未央",
            "小熊软糖", "猫猫冲鸭", "糖心布丁", "芋泥啵啵", "橘猫不想动", "喜剧小狗",
            "独行浪客", "冰封战意", "北境孤狼", "狂暴之影", "烈酒孤舟", "战意如火",

            // 英文风格
            "Free", "Echo", "Blaze", "Nova", "Storm", "Zero", "DarkWolf",
            "DreamCatcher", "SoulEcho", "StarryEyes", "MoonWalker", "OceanWhisper", "LightSeeker"
    };

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    /**
     * 随机从名字池中选取一个名字
     */
    public static String getRandomName() {
        int index = RANDOM.nextInt(NAME_POOL.length);
        return NAME_POOL[index];
    }

    /**
     * 生成一个用户名：名字 + 随机字符串
     */
    public static String generateUsername() {
        int randomLength = (int)Math.random()*100;
        String name = getRandomName();
        StringBuilder sb = new StringBuilder(name);
        for (int i = 0; i < randomLength; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }

    /**
     * 生成多个不重复用户名
     */
//    public static Set<String> generateUsernames(int randomLength, int count) {
//        Set<String> usernames = new HashSet<>();
//        while (usernames.size() < count) {
//            usernames.add(generateUsername(randomLength));
//        }
//        return usernames;
//    }


}
