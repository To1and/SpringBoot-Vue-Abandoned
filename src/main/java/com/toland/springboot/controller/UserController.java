package com.toland.springboot.controller;

import com.toland.springboot.entity.User;
import com.toland.springboot.mapper.UserMapper;
import com.toland.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public List<User> findAll()   //实现查询所有数据
    {
        return userMapper.findAll();     //返回全部数据
    }

    @GetMapping("/page")   //接口路径：/user/page?pageNumber=1&pageSize=10
    public Map<String, Object> findPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize)    //实现分页查询，接收页面数与页面大小两个数据
    {
        pageNumber = (pageNumber - 1) * pageSize;//limit的第一个参数 = (pageNumber - 1) * pageSize,其原理来源于MySQL语句

        List<User> data = userMapper.selectPage(pageNumber, pageSize);//获得查询信息

        Integer total = userMapper.selectTotal();//获得总条目数量

        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);

        return res;
    }


}
