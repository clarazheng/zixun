package com.clarazheng;

import com.alibaba.fastjson.JSONObject;
import com.clarazheng.dao.CommentDAO;
import com.clarazheng.model.Comment;
import com.clarazheng.model.EntityType;
import com.clarazheng.model.News;
import com.clarazheng.model.User;
import com.clarazheng.service.CommentService;
import com.clarazheng.service.NewsService;
import com.clarazheng.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by clara on 2017/5/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZixunApplication.class)
@Sql("/init-schema.sql")
public class initDatabaseTest {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    CommentService commentService;

    @Test
    public void testUser(){
        for(int i=0;i<10;i++){
            User user=new User();
            user.setName(String.format("User%d",i+1));
            user.setPassword("aaaa");
            user.setSalt("bbbb");
            user.setHeadUrl(String.format("http://opj52kf1i.bkt.clouddn.com/%d.png?imageView2/1/w/50/h/50", new Random().nextInt(100)));
            userService.addUser(user);
            user.setPassword("eeee");
            userService.updatePassword(user);

            News news=new News();
            news.setUserId(user.getId());
            news.setTitle(String.format("News%d",i+1));
            news.setCreatedDate(new Date());
            news.setCommentCount(i+1);
            news.setLikeCount(i+1);
            news.setImage(String.format("http://opj52kf1i.bkt.clouddn.com/%d.png", new Random().nextInt(100)));
            news.setLink(String.format("http://127.0.0.1:8080/news/%d", i));
            newsService.addNews(news);

            for(int j=1;j<5;j++) {
                Comment comment = new Comment();
                comment.setUserId(j);
                comment.setStatus(0);
                comment.setContent(String.format("Comment%d",j));
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.Entity_News);
                comment.setCreatedDate(new Date());
                commentService.addComment(comment);
            }
        }
        System.out.println(JSONObject.toJSONString(userService.selectById(1)));
        System.out.println(JSONObject.toJSONString(userService.selectByName("User2")));
        //userService.deleteById(1);
        //Assert.assertNull(userService.selectById(1));
        newsService.updateCommentCount(1,100);
        newsService.updateLikeCount(1,100);
        System.out.println(JSONObject.toJSONString(newsService.selectByUserIdAndOffset(0,0,10)));
        List<Comment> comments=commentService.getByEntity(3,EntityType.Entity_News);
        for(Comment comment:comments){
            System.out.println(JSONObject.toJSONString(comment));
        }
    }
}
