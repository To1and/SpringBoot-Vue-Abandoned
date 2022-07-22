package com.toland.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    private UserService userService;

    //实现新增或者更新数据
    @PostMapping
    public boolean saveOrUpdateUserInfo(@RequestBody User user)
    {
        return userService.saveOrUpdate(user);
    }


    //实现查询返回有数据
    @GetMapping
    public List<User> listAllUserInfo()
    {
        return userService.list();
    }


    //实现删除特定ID数据
    @DeleteMapping("/{id}")
    public boolean deleteUserInfoById(@PathVariable Integer id)
    {
        return userService.removeById(id);
    }


    //实现分页查询-MyBatisPlus实现
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNumber,
                                @RequestParam Integer pageSize,
                                @RequestParam(required = false, defaultValue = "") String username,
                                @RequestParam(required = false, defaultValue = "") String nickname,
                                @RequestParam(required = false, defaultValue = "") String address)
                                //在类参数表内加入默认值设置，以便当不对某项值进行限定时，即返回空值时也能正常依据其他传入参数搜索
    {
        IPage<User> page = new Page<>(pageNumber, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username))
        {
            queryWrapper.like("username", username);
        }
        if (!"".equals(nickname))
        {
            queryWrapper.like("nickname", nickname);
        }
        if (!"".equals(address))
        {
            queryWrapper.like("address", address);
        }
//      queryWrapper.or().like("address", address);

        return userService.page(page, queryWrapper);
    }

    //实现分页查询-MyBatis实现
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNumber,
//    @RequestParam Integer pageSize,
//    @RequestParam String username)
//    {
//        pageNumber = (pageNumber - 1) * pageSize;//limit的第一个参数 = (pageNumber - 1) * pageSize,其原理来源于MySQL语句
//
//        username="%"+username+"%";//实现模糊查询
//
//        List<User> data = userMapper.selectPage(pageNumber, pageSize,username);//获得查询信息
//
//        Integer total = userMapper.selectTotal(username);//获得总条目数量
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//
//        return res;
//    }

}
