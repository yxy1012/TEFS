package com.example.tefs_springboot.service.impl;

import com.example.tefs_springboot.dao.AdminDao;
import com.example.tefs_springboot.dao.UserDao;
import com.example.tefs_springboot.pojo.dto.LoginMsg;
import com.example.tefs_springboot.pojo.entity.Admin;
import com.example.tefs_springboot.pojo.entity.User;
import com.example.tefs_springboot.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    UserDao userDao;
    @Resource
    AdminDao adminDao;

    @Resource
    LoginMsg msg;
    //检查登录(业务逻辑：
    // 1.用户根据输入的用户名与密码去数据库中查询有无相关记录，若输入正确则返回login为true，并且返回userid和username给前端
    // 2.派送员根据输入的用户名与密码去数据库中进行查询，同时查询其启用信息，若是启用为F，意味着管理员还未审核，若输入正确并且启用为T，怎返回login为true
    // 3.管理员根据输入的用户名与密码去数据库中进行查询，若输入正确在返回true，返回adminid和adminname给前端)
    @Override
    public LoginMsg CheckLogin(int identity, String username, String password) {
        if(identity==1){
            User user=userDao.selectByUserName(username);
            if(user!=null&&user.getPassword().equals(password)){
                msg.setId(user.getUserId());
                msg.setIdentity(identity);
                msg.setName(user.getUserName());
                msg.setLogin(true);
            }
            else {
                msg.setLogin(false);
            }
        }
        else if(identity==2) {
            Admin admin=adminDao.selectByUserName(username);
            if(admin!=null&&admin.getPassword().equals(password)){
                msg.setIdentity(identity);
                msg.setId(admin.getAdminId());
                msg.setName(admin.getUserName());
                msg.setLogin(true);
            }else {
                msg.setLogin(false);
            }
        }
        return msg;
    }
}

