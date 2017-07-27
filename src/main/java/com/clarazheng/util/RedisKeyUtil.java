package com.clarazheng.util;

/**
 * Created by clara on 2017/5/9.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    public static final String BIZ_LIKE="LIKE";
    public static final String BIZ_DISLIKE="DISLIKE";
    public static final String EVENTQUEUE="EVENTQUEUE";

    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    public static String getDislikeKey(int entityId,int entityType){
        return BIZ_DISLIKE+SPLIT+entityType+SPLIT+entityId;
    }

    public static String getEventQueueKey(){
        return EVENTQUEUE;
    }
}
