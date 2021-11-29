package com.example.tefs_springboot.service.impl;

import com.example.tefs_springboot.dao.UserDao;
import com.example.tefs_springboot.pojo.entity.User;
import com.example.tefs_springboot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    @Override
    public List<User> findAllUser(){
        List<User> list=userDao.selectAllUser();
        return list;
    }
}
