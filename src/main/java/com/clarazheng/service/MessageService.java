package com.clarazheng.service;

import com.clarazheng.dao.MessageDAO;
import com.clarazheng.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by clara on 2017/5/8.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public void updateHasRead(String conversationId){
        messageDAO.updateHasRead(conversationId);
    }

    public int countUnread(String conversationId,int userId){
        return messageDAO.countUnread(conversationId, userId);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }
}
