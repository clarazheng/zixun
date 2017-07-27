package com.clarazheng.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clara on 2017/5/5.
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    private static final String DEFAULT_REPLACEMENT = "敏感词";

    private class TrieNode{
        private boolean end = false;
        private Map<Character,TrieNode> subNodes=new HashMap<Character,TrieNode>();
        void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);
        }
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }
        void setEnd(Boolean end){
            this.end=end;
        }
        boolean isEnd(){
            return end;
        }
    }
    private TrieNode root=new TrieNode();

    private boolean isSymble(Character c){
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2e80||ic>0x9fff);
    }

    private void addWord(String word){
        TrieNode tempNode=root;
        for(int i=0;i<word.length();i++){
            Character c=word.charAt(i);
            if(isSymble(c)){
                continue;
            }
            TrieNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if (i == word.length() - 1) {
                tempNode.setEnd(true);
            }
        }
    }
    public String filter(String text){
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();
        TrieNode tempNode=root;
        int begin=0;
        int end=0;
        while(begin<text.length()){
            Character c=text.charAt(end);
            if(isSymble(c)){
                if(begin==end){
                    result.append(c);
                    begin=begin+1;
                    end=begin;
                }else{
                    end++;
                }
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                result.append(text.charAt(begin));
                begin=begin+1;
                end=begin;
                tempNode=root;
            }else if(tempNode.isEnd()){
                begin=end+1;
                end=begin;
                tempNode=root;
                result.append(replacement);
            }else{
                end=end+1;
                {
                    if(end==text.length()){
                        end=begin+1;
                        result.append(text.charAt(begin));
                        begin=end;
                        tempNode=root;
                    }
                }
            }
        }
        return result.toString();
    }

    public boolean isSeneitive(String text){
        if (StringUtils.isBlank(text)) {
            return false;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();
        TrieNode tempNode=root;
        int begin=0;
        int end=0;
        while(begin<text.length()){
            Character c=text.charAt(end);
            if(isSymble(c)){
                if(begin==end){
                    result.append(c);
                    begin=begin+1;
                    end=begin;
                }else{
                    end++;
                }
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                result.append(text.charAt(begin));
                begin=begin+1;
                end=begin;
                tempNode=root;
            }else if(tempNode.isEnd()){
               return true;
            }else{
                end=end+1;
                {
                    if(end==text.length()){
                        end=begin+1;
                        result.append(text.charAt(begin));
                        begin=end;
                        tempNode=root;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            org.springframework.core.io.Resource fileRource = new ClassPathResource("SensitiveWords.txt");
            InputStream is = fileRource.getInputStream();
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            isReader.close();
        }catch (Exception e){
            logger.error("读取敏感词失败"+e.getMessage());
        }
    }
    public static void main(String[] argv){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("暴力");
        s.addWord("色暴力的");
        System.out.println(s.filter("@这是条色@情资讯色暴力"));
    }
}
