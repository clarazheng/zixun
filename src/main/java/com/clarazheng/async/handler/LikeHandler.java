package com.clarazheng.async.handler;

import com.clarazheng.async.EventHandler;
import com.clarazheng.async.EventModel;
import com.clarazheng.async.EventType;
import com.clarazheng.model.Message;
import com.clarazheng.model.User;
import com.clarazheng.service.MessageService;
import com.clarazheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by clara on 2017/4/28.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFromId(3);
        message.setToId(model.getActorId());
        User user=userService.selectById(model.getEntityOwnerId());
        message.setContent("注意啦，用户"+model.getActorId()
                +"赞了你的资讯,http://127.0.0.1:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
            }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
