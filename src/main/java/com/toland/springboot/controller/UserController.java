package com.toland.springboot.controller;

import com.toland.springboot.entity.User;
import com.toland.springboot.mapper.UserMapper;
import com.toland.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping
    public Integer save(@RequestBody User user)     //实现新增或者更新数据
    {
        return userService.save(user);      //返回操作的条目数量
    }

    @DeleteMapping("/{id}") //这个id与下一行的id一一对应
    public Integer delete(@PathVariable Integer id) //   //实现删除数据
    {
        return userMapper.deleteById(id);   //返回删除的id
    }

    @GetMapping
    public List<User> index()   //实现查询所有数据
    {
        List<User> all = userMapper.findAll();
        return all;     //返回全部数据
    }


}
