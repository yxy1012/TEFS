package com.example.tefs_springboot.controller;


import com.example.tefs_springboot.pojo.entity.Audio;
import com.example.tefs_springboot.pojo.entity.Replacement;
import com.example.tefs_springboot.pojo.entity.Video;
import com.example.tefs_springboot.pojo.vo.CutVideoVo;
import com.example.tefs_springboot.pojo.vo.ReplaceVideoVo;
import com.example.tefs_springboot.service.FFmpegService;
import com.example.tefs_springboot.service.LfasrService;
import com.example.tefs_springboot.service.VideoService;
import com.iflytek.msp.lfasr.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VideoController {

    @Resource
    VideoService videoService;

    @Resource
    FFmpegService ffmpegService;

    @Resource
    LfasrService lfasrService;

    @GetMapping("/allVideo/{id}")
    @ResponseBody
    public List<Video> findAllVideos(@PathVariable("id") int userId){
        List<Video> result =videoService.findAllVideos(userId);
        return result;
    }

    @PostMapping("/insertVideo")
    @ResponseBody
    public int insert(@RequestBody Video video){
        int result=videoService.insert(video);
        return  result;
    }

    @PostMapping("/deleteVideo/{id}")
    @ResponseBody
    public int delete(@PathVariable("id") int videoId){
        int result = videoService.delete(videoId);
        return result;
    }

    @PostMapping("/updateVideo")
    @ResponseBody
    public int update(@RequestBody Video video){
        int result = videoService.update(video);
        return result;
    }

    @PostMapping("/cutVideo")
    @ResponseBody
    public List<CutVideoVo> cut(@RequestBody List<Video> selectedList){
//        ffmpegService.cutVideo(video,"00:00:00",10);
        List<CutVideoVo> videolist=new ArrayList<>();
        List<Audio> audiolist=new ArrayList<>();
        List<Message> messagelist=new ArrayList<>();
        for(int i=0;i<selectedList.size();i++)
        {
            System.out.println(selectedList.get(i).getVideoUrl());
            String videoFile = selectedList.get(i).getVideoUrl().replace("http://localhost:8181/api/file/","D:\\upload\\");
            System.out.println(videoFile);
            CutVideoVo cutVideoVo=new CutVideoVo();
            cutVideoVo.setCutUrl("boundary");
            videolist.add(cutVideoVo);
            videolist.addAll(ffmpegService.cutVideo(videoFile));
        }
//        audiolist.addAll(ffmpegService.convertVideo(videolist));
//        System.out.println(audiolist);
//        messagelist=lfasrService.extraParams(audiolist);
//        System.out.println(messagelist);
//        int j=0;
//        for(int i=0;i<videolist.size();i++){
//            if(videolist.get(i).getCutUrl()!="boundary")
//            {
//                videolist.get(i).setCutParam(messagelist.get(j));
//                j++;
//            }
//        }
//        System.out.println(videolist);
        return videolist;
    }

    @PostMapping("/extractText")
    @ResponseBody
    public CutVideoVo extractText(@RequestBody CutVideoVo cutvideo){
        List<CutVideoVo> videolist=new ArrayList<>();
        List<Audio> audiolist=new ArrayList<>();
        List<Message> messagelist;
        System.out.println(cutvideo.getCutUrl());
        videolist.add(cutvideo);
        audiolist.addAll(ffmpegService.convertVideo(videolist));
        System.out.println(audiolist);
        messagelist=lfasrService.extraParams(audiolist);
        //System.out.println(messagelist);

        String str = messagelist.toString();
        String[] message=str.split("onebest\":\"");
        str="";
        for(int i=1;i<message.length;i++){
            str+=message[i].split("\",\"speaker")[0];
        }
        System.out.println(str);

        cutvideo.setCutParam(str);
        return cutvideo;
    }

    @PostMapping("/download")
    @ResponseBody
    public ReplaceVideoVo Download(@RequestBody Replacement replacement){
        System.out.println(replacement.getVideoPath());
        return ffmpegService.download(replacement.getAudioBase64(),replacement.getVideoPath());
    }

    @PostMapping("/composeVideo")
    @ResponseBody
    public List<Video> composeVideo(@RequestBody List<CutVideoVo> list){
        return ffmpegService.composeVideo(list);
    }

    @PostMapping("/updateUrl")
    @ResponseBody
    public int updateUrl(@RequestBody List<Video> list){
        return videoService.updateUrl(list);
    }
}
