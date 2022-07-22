package com.toland.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toland.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
public interface UserMapper extends BaseMapper<User>
{

//MyBatis方法
//    @Select("SELECT * FROM user_info")
//    List<User> findAllUser();
//
//    @Insert("INSERT INTO user_info(username,password,nickname,email,phone,address) VALUES (#{username}, #{password}, #{nickname},#{email}, #{phone}, #{address})")
//    int insert(User user);
//
//    @Delete("DELETE FROM user_info WHERE id = #{id}")
//        //这个id与下一行的id一一对应
//    Integer deleteById(@Param("id") Integer id);
//
//    int update(User user);
//
//    @Select("SELECT * FROM user_info WHERE username like #{username} LIMIT #{pageNumber},#{pageSize}")
//    List<User> selectPage(Integer pageNumber, Integer pageSize,String username);
//
//    @Select("SELECT COUNT(*) FROM user_info")
//    Integer selectTotal(String username);

}