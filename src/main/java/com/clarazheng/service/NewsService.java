package com.clarazheng.service;

import com.clarazheng.dao.NewsDAO;
import com.clarazheng.model.News;
import com.clarazheng.util.ZixunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by clara on 2017/5/4.
 */
@Service
public class NewsService {
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addNews(News news){
        news.setTitle(sensitiveService.filter(news.getTitle()));
        news.setLink(sensitiveService.filter(news.getLink()));
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public void updateCommentCount(int id,int commentCount){
        newsDAO.updateCommentCount(id, commentCount);
    }

    public void updateLikeCount(int id,int likeCount){
        newsDAO.updateLikeCount(id, likeCount);
    }

    public List<News> selectByUserIdAndOffset(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String addImage(MultipartFile file) throws IOException {
        int dotPos=file.getOriginalFilename().lastIndexOf(".");
        String ext=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if(!ZixunUtil.isFileAllowed(ext)){
            return null;
        }
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+ext;
        Files.copy(file.getInputStream(),new File(ZixunUtil.imageDir+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ZixunUtil.ZinxunDomain+"image?name="+fileName;
    }
}
