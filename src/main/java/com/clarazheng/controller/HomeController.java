package com.clarazheng.controller;

import com.clarazheng.model.*;
import com.clarazheng.service.LikeService;
import com.clarazheng.service.NewsService;
import com.clarazheng.service.SensitiveService;
import com.clarazheng.service.UserService;
import com.clarazheng.util.ZixunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clara on 2017/5/4.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model,
                        @RequestParam(value = "pop",defaultValue = "0")int pop){
        List<News> newsList=newsService.selectByUserIdAndOffset(0,0,10);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(News news : newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            User user=userService.selectById(news.getUserId());
            vo.set("user",user);
            if(hostHolder.getUser()==null){
                vo.set("like",0);
            }else {
                vo.set("like", likeService.getLikeStatus(hostHolder.getUser().getId(),news.getId(), EntityType.Entity_News));
            }
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        if(hostHolder.getUser()!=null){
            pop=0;
        }
        model.addAttribute("pop",pop);
        return "home";
    }


    @RequestMapping(path = {"/user/{userId}/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String user(Model model,
                       @PathVariable("userId")int userId){
        List<News> newsList=newsService.selectByUserIdAndOffset(userId,0,10);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(News news : newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            User user=userService.selectById(userId);
            vo.set("user",user);
            if(hostHolder.getUser()==null){
                vo.set("like",0);
            }else {
                vo.set("like", likeService.getLikeStatus(hostHolder.getUser().getId(),news.getId(), EntityType.Entity_News));
            }
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "home";
    }
}
