package com.clarazheng.dao;


import com.clarazheng.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by clara on 2017/5/4.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME=" user ";
    String INSERT_FIELDS=" name, password, salt, head_url ";
    String SELECT_FIELDS=" id, "+INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, " ( ",INSERT_FIELDS, ") values (#{name}, #{password},#{salt},#{headUrl})"})
    int addUser(User user);


    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    User selectById(int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
    User selectByName(String name);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteById(int id);


    @Update({"update ",TABLE_NAME," set head_url=#{headUrl} where id=#{id}"})
    void updateHeadUrl(User user);

}
