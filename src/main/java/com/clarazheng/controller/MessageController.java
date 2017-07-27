package com.clarazheng.controller;

import com.clarazheng.model.HostHolder;
import com.clarazheng.model.Message;
import com.clarazheng.model.User;
import com.clarazheng.model.ViewObject;
import com.clarazheng.service.MessageService;
import com.clarazheng.service.SensitiveService;
import com.clarazheng.service.UserService;
import com.clarazheng.util.ZixunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by clara on 2017/5/8.
 */
@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;


    @RequestMapping(path={"/msg/add"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){
        try{
            if(hostHolder.getUser()==null){
                return ZixunUtil.getJSONString(999);
            }
            User user=userService.selectByName(toName);
            if(user==null){
                return ZixunUtil.getJSONString(1,"收信人不存在");
            }
            int userId=hostHolder.getUser().getId();
            int toId=user.getId();
            Message message=new Message();
            message.setFromId(userId);
            message.setToId(toId);
            message.setContent(sensitiveService.filter(content));
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            messageService.addMessage(message);
            return ZixunUtil.getJSONString(message.getId());
        }catch (Exception e){
            logger.error("发信失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"发信失败");
        }
    }

    @RequestMapping(path={"/msg/list"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.getUser()==null){
            return "redirect:/?pop=1";
        }
        int localId=hostHolder.getUser().getId();
        List<Message> conversationList=messageService.getConversationList(localId,0,10);
        List<ViewObject> conversations=new ArrayList<ViewObject>();
        for(Message conversation : conversationList){
            ViewObject vo=new ViewObject();
            vo.set("conversation",conversation);
            int userId=conversation.getToId()==localId?conversation.getFromId():conversation.getToId();
            vo.set("user",userService.selectById(userId));
            vo.set("unread",messageService.countUnread(conversation.getConversationId(),localId));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }


    @RequestMapping(path={"/msg/detail"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getConversationDetail(@RequestParam("conversationId")String conversationId, Model model){
        if(hostHolder.getUser()==null){
            return "redirect:/?pop=1";
        }
        messageService.updateHasRead(conversationId);
        List<Message> conversation=messageService.getConversationDetail(conversationId,0,10);
        List<ViewObject> messages=new ArrayList<ViewObject>();
        for(Message message :conversation){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int userId=message.getFromId();
            String headUrl=userService.selectById(userId).getHeadUrl();
            vo.set("userId",userId);
            vo.set("headUrl",headUrl);
            messages.add(vo);
        }
        model.addAttribute("messages",messages);
        return "letterDetail";
    }
}
