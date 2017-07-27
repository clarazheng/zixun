package com.clarazheng.async;

import java.util.List;

/**
 * Created by clara on 2017/4/28.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
