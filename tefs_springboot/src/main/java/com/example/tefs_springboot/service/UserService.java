package com.example.tefs_springboot.service;

import com.example.tefs_springboot.pojo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();
}
