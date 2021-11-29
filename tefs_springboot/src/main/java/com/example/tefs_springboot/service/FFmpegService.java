package com.example.tefs_springboot.service;

import com.example.tefs_springboot.pojo.entity.Audio;
import com.example.tefs_springboot.pojo.entity.Video;
import com.example.tefs_springboot.pojo.vo.CutVideoVo;
import com.example.tefs_springboot.pojo.vo.ReplaceVideoVo;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.Time;
import java.util.List;

public interface FFmpegService {
//     void cutVideo(String videoFile,String startTime, int timeLength);
       List<CutVideoVo> cutVideo(String videoFile);
       List<Audio> convertVideo(List<CutVideoVo> list);
       ReplaceVideoVo download(String audioBase64, String videoPath);
       List<Video> composeVideo(List<CutVideoVo> list);
}
