package com.toland.springboot.mapper;

import com.toland.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper
{
    @Select("SELECT * FROM user_info")
    List<User> findAll();
    @Insert("INSERT INTO user_info(username,password,nickname,email,phone,address) VALUES (#{username}, #{password}, #{nickname},#{email}, #{phone}, #{address})")
    int insert(User user);

    @Delete("DELETE FROM user_info WHERE id = #{id}")   //这个id与下一行的id一一对应
    Integer deleteById(@Param("id") Integer id);

    int update(User user);

    @Select("SELECT * FROM user_info LIMIT #{pageNumber},#{pageSize}")
    List<User> selectPage(Integer pageNumber,Integer pageSize);

    @Select("SELECT COUNT(*) FROM user_info")
    Integer selectTotal();

}