package com.clarazheng.controller;

import com.clarazheng.model.HostHolder;
import com.clarazheng.model.User;
import com.clarazheng.service.SensitiveService;
import com.clarazheng.service.UserService;
import com.clarazheng.util.ZixunUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by clara on 2017/5/4.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/reg"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String reg(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      HttpServletResponse response){
        try{
            Map<String,Object> map =userService.reg(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                return ZixunUtil.getJSONString(0);
            }
            return ZixunUtil.getJSONString(1,map);
        }catch (Exception e){
            logger.error("注册失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"注册失败");
        }
    }

    @RequestMapping(path = {"/login"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String login(Model model,
                      @RequestParam("username")String name,
                      @RequestParam("password")String password,
                        HttpServletResponse response){
        try{
            Map<String,Object> map =userService.login(name, password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                return ZixunUtil.getJSONString(0);
            }
            return ZixunUtil.getJSONString(1,map);
        }catch (Exception e){
            logger.error("登陆失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"登陆失败");
        }
    }

    @RequestMapping(path = {"logout"},method = {RequestMethod.POST,RequestMethod.GET})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


    @RequestMapping(path={"/changeHead"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image){
        try {
            if (hostHolder.getUser() == null) {
                return ZixunUtil.getJSONString(1,"未登录");
            }
            User user=hostHolder.getUser();
            user.setHeadUrl(image);
            userService.updateHeadUrl(user);
            return ZixunUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("改变头像失败"+e.getMessage());
            return ZixunUtil.getJSONString(1,"failed");
        }
    }
}
