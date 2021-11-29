package com.example.tefs_springboot.controller;

import com.example.tefs_springboot.pojo.entity.User;
import com.example.tefs_springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {
    @Resource
    UserService userService;

    //查看所有用户信息
    @GetMapping("/findAllUser")
    @ResponseBody
    public List<User> findAllUser(){
        List<User> list=userService.findAllUser();
        return list;
    }
}
