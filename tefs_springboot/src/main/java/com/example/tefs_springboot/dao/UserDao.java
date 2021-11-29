package com.example.tefs_springboot.dao;

import com.example.tefs_springboot.pojo.entity.User;

import java.util.List;

public interface UserDao {
    List<User> selectAllUser();
    User selectByUserName(String username);
    int insert(User record);
}
