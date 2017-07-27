package com.clarazheng.model;

import org.springframework.stereotype.Component;

/**
 * Created by clara on 2017/5/5.
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users=new ThreadLocal<User>();

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
