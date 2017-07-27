package com.clarazheng.model;

import java.util.Date;

/**
 * Created by clara on 2017/5/8.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private Date createdDate;
    private int hasRead;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        }
        return String.format("%d_%d", toId, fromId);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date creadtedDate) {
        this.createdDate = creadtedDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
