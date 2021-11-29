package com.example.tefs_springboot.service.impl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.example.tefs_springboot.dao.VideoDao;
import com.example.tefs_springboot.pojo.entity.Audio;
import com.example.tefs_springboot.pojo.entity.Video;
import com.example.tefs_springboot.pojo.vo.CutVideoVo;
import com.example.tefs_springboot.pojo.vo.ReplaceVideoVo;
import com.example.tefs_springboot.service.FFmpegService;
//import com.example.tefs_springboot.utils.MediaUtil;
import com.example.tefs_springboot.utils.VideoFileOperate;
import org.springframework.stereotype.Service;


@Service
public class FFmpegServiceImpl implements FFmpegService {


    @Override
    public List<CutVideoVo> cutVideo(String videoFile) {
//        File file=new File("D:\\upload\\1614012057818.mp4");
//        File outputFile=new File("D:\\test\\out6.mp4");
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        java.util.Date d = null;
        List<CutVideoVo> list=new ArrayList<>();
        try {
            System.out.println(videoFile);
            list=VideoFileOperate.cutVideo(videoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
//        Time time = new Time(d.getTime());
//        MediaUtil.cutVideo(file,outputFile,time,timeLength);
    }

    @Override
    public List<Audio> convertVideo(List<CutVideoVo> list){
        List<Audio> audioList=new ArrayList<>();
        try{
            audioList=VideoFileOperate.convertVideo(list);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return audioList;
    }

    @Override
    public ReplaceVideoVo download(String audioBase64,String videoPath){
            File file = null;
            //创建文件目录
            String filePath = "D:\\upload";
            String video = videoPath.replace("http://localhost:8181/api/file/","D:\\upload\\");
            String fileName[] = video.split("\\.");
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            String audioPath=fileName[0].split("_replaced")[0]+".mp3";
            System.out.println(audioPath);
            BufferedOutputStream bos = null;
            java.io.FileOutputStream fos = null;
            try {
                String base64= URLDecoder.decode(audioBase64, "utf-8");
                String audio=base64.replace("data:audio/mp3;base64,","");
                System.out.println(audio.replace("=",""));
                byte[] bytes = Base64.getDecoder().decode(audio.replace("=",""));
                file=new File(audioPath);
                if (file.exists() && file.isFile()) file.delete();
                fos = new java.io.FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        try {
            ReplaceVideoVo result = VideoFileOperate.replaceAudio(audioPath,video);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    public List<Video> composeVideo(List<CutVideoVo> list){
        List<Video> videoList = new ArrayList<>();
        try {
            videoList=VideoFileOperate.composeVideo(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoList;
    }

}
