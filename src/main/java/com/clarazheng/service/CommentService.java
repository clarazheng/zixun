package com.clarazheng.service;

import com.clarazheng.dao.CommentDAO;
import com.clarazheng.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by clara on 2017/5/8.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;


    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public List<Comment> getByEntity(int entityId,int entityType){
        return commentDAO.getByEntity(entityId, entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public List<Comment> getByUserId(int userId){
        return commentDAO.getByUserId(userId);
    }

    public void deleteComment(int entityId,int entityType){
        commentDAO.updataStatus(entityId,entityType,1);
    }
}
