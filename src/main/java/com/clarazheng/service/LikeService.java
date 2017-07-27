package com.clarazheng.service;

import com.clarazheng.util.JedisAdapter;
import com.clarazheng.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by clara on 2017/5/9.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long like(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId,int entityId,int entityType){
        String likeKey= RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityId, entityType);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(userId))?-1:0;
    }
}
