package com.example.tefs_springboot.service;

import com.example.tefs_springboot.pojo.dto.LoginMsg;

public interface LoginService {
    LoginMsg CheckLogin(int identity, String username, String password);
}
