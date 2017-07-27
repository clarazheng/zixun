package com.clarazheng.controller;

import com.clarazheng.model.*;
import com.clarazheng.service.*;
import com.clarazheng.util.ZixunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by clara on 2017/5/6.
 */
@Controller
public class NewsController {
    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/addImage"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String saveImage(@RequestParam("file")MultipartFile file){
        try{
            String imageDir= newsService.addImage(file);
            return ZixunUtil.getJSONString(0,imageDir);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"上传图片失败");
        }
    }

    @RequestMapping(path = {"/image"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String name, HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(ZixunUtil.imageDir + name), response.getOutputStream());
        }catch (Exception e){
            logger.error("显示图片失败"+e.getMessage());
        }
    }

    @RequestMapping(path="/news/{newsId}",method = {RequestMethod.POST,RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId")int newsId, Model model){
        News news=newsService.getById(newsId);
        User owner=userService.selectById(news.getUserId());
        model.addAttribute("news",news);
        model.addAttribute("owner",owner);
        if(hostHolder.getUser()==null){
            model.addAttribute("like",0);
        }else {
            model.addAttribute("like", likeService.getLikeStatus(hostHolder.getUser().getId(),news.getId(), EntityType.Entity_News));
        }
        List<Comment> commentList=commentService.getByEntity(newsId, EntityType.Entity_News);
        List<ViewObject> comments=new ArrayList<ViewObject>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.selectById(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        return "detail";
    }

    @RequestMapping(path="/addNews",method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try {
            News news =new News();
            if (hostHolder.getUser() == null) {
                news.setUserId(ZixunUtil.ANONYMOUS_USER);
            }else{
                news.setUserId(hostHolder.getUser().getId());
            }
            news.setImage(image);
            news.setTitle(sensitiveService.filter(title));
            news.setLink(sensitiveService.filter(link));
            news.setCreatedDate(new Date());
            news.setLikeCount(0);
            news.setCommentCount(0);
            newsService.addNews(news);
            return ZixunUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("增加资讯失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"failed");
        }
    }


    @RequestMapping(path="/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId")int newsId,@RequestParam("content")String content){
        try {
            Comment comment=new Comment();
            if (hostHolder.getUser() == null) {
                return "redirect:/?pop=1";
            } else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            comment.setEntityType(EntityType.Entity_News);
            comment.setEntityId(newsId);
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
        }catch (Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+newsId;
    }
}
