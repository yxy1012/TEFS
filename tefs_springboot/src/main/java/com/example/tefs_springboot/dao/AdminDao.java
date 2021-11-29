package com.example.tefs_springboot.dao;

import com.example.tefs_springboot.pojo.entity.Admin;

public interface AdminDao {
    Admin selectByUserName(String username);
}
