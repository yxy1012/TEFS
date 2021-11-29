package com.example.tefs_springboot.service;

import com.example.tefs_springboot.pojo.entity.Video;

import java.util.List;

public interface VideoService {
    List<Video> findAllVideos(int userId);
    int insert(Video video);
    int delete(int videoId);
    int update(Video video);
    int updateUrl(List<Video> list);
}
