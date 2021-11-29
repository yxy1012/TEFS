package com.example.tefs_springboot.service.impl;

import com.example.tefs_springboot.dao.VideoDao;
import com.example.tefs_springboot.pojo.entity.Video;
import com.example.tefs_springboot.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    VideoDao videoDao;

    @Override
    public List<Video> findAllVideos(int userId){
        List<Video> list = videoDao.selectAllVideos(userId);
        return list;
    }

    @Override
    public int insert(Video video){
        int result =videoDao.insert(video);
        return result;
    }

    @Override
    public int delete(int videoId){
        int result=videoDao.delete(videoId);
        return result;
    }

    @Override
    public int update(Video video){
        int result=videoDao.update(video);
        return result;
    }

    @Override
    public int updateUrl(List<Video> list){
        for(int i=0;i<list.size();i++){
            videoDao.updateUrl(list.get(i));
        }
        int result=1;
        return result;
    }
}
