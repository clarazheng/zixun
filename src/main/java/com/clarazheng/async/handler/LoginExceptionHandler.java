package com.clarazheng.async.handler;


import com.clarazheng.async.EventHandler;
import com.clarazheng.async.EventModel;
import com.clarazheng.async.EventType;
import com.clarazheng.model.Message;
import com.clarazheng.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by clara on 2017/4/28.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Override
    public void doHandle(EventModel model) {
        //判断是否有异常登录
        Message message=new Message();
        message.setToId(model.getActorId());
        message.setContent("登陆ip异常");
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
