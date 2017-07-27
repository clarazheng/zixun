package com.clarazheng.dao;

import com.clarazheng.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by clara on 2017/5/8.
 */
@Mapper
public interface MessageDAO {

    String TABLE_NAME=" message ";
    String INSERT_FIELDS=" to_id, from_id, conversation_id, content, created_date, has_read ";
    String SELECT_FIELDS=" id, "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "( ",INSERT_FIELDS,") values (#{toId}, #{fromId}, #{conversationId}, #{content}, #{createdDate}, #{hasRead})"})
    int addMessage(Message message);

    @Update({"update ",TABLE_NAME," set has_read=1 where conversation_id=#{conversationId}"})
    void updateHasRead(String conversationId);

    @Select({"select count(id) from ",TABLE_NAME, " where conversation_id=#{conversationId} and to_id=#{userId} and has_read=0"})
    int countUnread(@Param("conversationId") String conversationId,
                    @Param("userId")int userId);

    @Select({"select",SELECT_FIELDS," from ",TABLE_NAME, " where  conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ",INSERT_FIELDS," , count(id) as id from (select * from ",TABLE_NAME,
            " where to_id=#{userId} or from_id=#{userId} order by id desc ) as tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

}
