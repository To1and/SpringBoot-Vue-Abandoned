package com.toland.springboot.mapper;

import com.toland.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper
{
    @Select("SELECT * FROM user_info")
    List<User> findAll();
}