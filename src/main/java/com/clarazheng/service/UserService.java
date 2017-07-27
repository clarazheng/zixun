package com.clarazheng.service;

import com.clarazheng.dao.LoginTicketDAO;
import com.clarazheng.dao.UserDAO;
import com.clarazheng.model.LoginTicket;
import com.clarazheng.model.User;
import com.clarazheng.util.ZixunUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by clara on 2017/5/4.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addUser(User user){
        return userDAO.addUser(user)>0?1:0;
    }

    public User selectById(int id){
        return userDAO.selectById(id);
    }

    public User selectByName(String name){
        return userDAO.selectByName(name);
    }

    public void updatePassword(User user){
        userDAO.updatePassword(user);
    }

    public void deleteById(int id){
        userDAO.deleteById(id);
    }

    public Map<String,Object> reg(String name,String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(name)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        if(StringUtils.containsIgnoreCase(name,"admin")||StringUtils.contains(name,"管理员")||sensitiveService.isSeneitive(name)){
            map.put("msgname","用户名包含敏感词");
            return map;
        }
        User user=userDAO.selectByName(name);
        if(user!=null){
            map.put("msgname","用户名已被使用");
            return map;
        }
        user=new User();
        user.setName(name);
        user.setHeadUrl(String.format("http://opj52kf1i.bkt.clouddn.com/%d.png?imageView2/1/w/50/h/50", new Random().nextInt(100)));
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ZixunUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setStatus(0);
        Date date=new Date();
        date.setTime(date.getTime()+3600*24);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        map.put("ticket",ticket.getTicket());
        return map;
    }

    public Map<String,Object> login(String name,String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(name)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(name);
        if(user==null){
            map.put("msgname","用户名不存在");
            return map;
        }
        if(!user.getPassword().equals(ZixunUtil.MD5(password+user.getSalt()))){
            map.put("msgpwd","密码不正确");
            return map;
        }
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setStatus(0);
        Date date=new Date();
        date.setTime(date.getTime()+3600*24);
        ticket.setExpired(date);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        map.put("ticket",ticket.getTicket());
        return map;
    }

    public void logout(String ticket){
        loginTicketDAO.updateStstus(ticket,1);
    }

    public void updateHeadUrl(User user){
        userDAO.updateHeadUrl(user);
    }
}
