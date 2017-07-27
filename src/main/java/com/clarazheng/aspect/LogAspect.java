package com.clarazheng.aspect;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.ir.CallNode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by clara on 2017/5/4.
 */
@Aspect
@Component
public class LogAspect {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.clarazheng.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg: joinPoint.getArgs()){
            sb.append("arg:" + arg.toString()+"| ");
        }
        logger.info("before method:"+sb.toString());
    }

    @After("execution(* com.clarazheng.controller.*Controller.*(..))")
    public void afterMethod(){
    }
}
