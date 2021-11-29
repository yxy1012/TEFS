package com.example.tefs_springboot.dao;

import com.example.tefs_springboot.pojo.entity.Video;

import java.util.List;

public interface VideoDao {
    List<Video> selectAllVideos(int userId);
    int insert(Video video);
    Video selectByPrimaryKey(Integer videoId);
    int delete(int videoId);
    int update(Video video);
    int updateUrl(Video video);
}
