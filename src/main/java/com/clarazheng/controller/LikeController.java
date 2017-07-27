package com.clarazheng.controller;

import com.clarazheng.async.EventModel;
import com.clarazheng.async.EventProducer;
import com.clarazheng.async.EventType;
import com.clarazheng.model.EntityType;
import com.clarazheng.model.HostHolder;
import com.clarazheng.service.LikeService;
import com.clarazheng.service.NewsService;
import com.clarazheng.service.UserService;
import com.clarazheng.util.ZixunUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by clara on 2017/5/9.
 */
@Controller
public class LikeController {
    private static final Logger logger= LoggerFactory.getLogger(LikeController.class);
    @Autowired
    LikeService likeService;
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId")int newsId){
        try{
            long likeCount=likeService.like(hostHolder.getUser().getId(),newsId, EntityType.Entity_News);
            newsService.updateLikeCount(newsId,(int) likeCount);
            eventProducer.fireEvent(new EventModel(EventType.LIKE).setEntityId(newsId).
                    setEntityType(EntityType.Entity_News).setActorId(hostHolder.getUser().getId()).
                    setEntityOwnerId(newsService.getById(newsId).getUserId()));
            return ZixunUtil.getJSONString(0,String.valueOf(likeCount));
        }catch (Exception e){
            logger.error("赞失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"赞失败");
        }
    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId")int newsId){
        try{
            long likeCount=likeService.dislike(hostHolder.getUser().getId(),newsId, EntityType.Entity_News);
            newsService.updateLikeCount(newsId,(int) likeCount);
            return ZixunUtil.getJSONString(0,String.valueOf(likeCount));
        }catch (Exception e){
            logger.error("踩失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"踩失败");
        }
    }

}
