package com.toland.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toland.springboot.entity.User;
import com.toland.springboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User>
{

}
