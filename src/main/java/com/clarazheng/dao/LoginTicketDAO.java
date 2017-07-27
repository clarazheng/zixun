package com.clarazheng.dao;

import com.clarazheng.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by clara on 2017/5/4.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME=" login_ticket ";
    String INSERT_FIELDS=" user_id, ticket, expired, status";
    String SELECT_FIELDS=" id, "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, "( ",INSERT_FIELDS,") values (#{userId}, #{ticket}, #{expired}, #{status})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket getByTicket(String ticket);


    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStstus(@Param("ticket") String ticket,
                      @Param("status")int status);

}
