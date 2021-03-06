package com.example.tefs_springboot.controller;

import com.example.tefs_springboot.pojo.dto.LoginMsg;
import com.example.tefs_springboot.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class LoginController {
    //用于控制登录
    @Resource
    LoginService loginService;

    //登录校验
    @GetMapping("/checklogin/{identity}/{username}/{password}")
    @ResponseBody
    public LoginMsg CheckLogin(@PathVariable("identity") int identity, @PathVariable("username") String username, @PathVariable("password") String password) {
        LoginMsg result = loginService.CheckLogin(identity,username,password);
        return result;
    }
}
