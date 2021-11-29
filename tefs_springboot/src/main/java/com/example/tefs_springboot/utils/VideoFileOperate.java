package com.example.tefs_springboot.utils;

import com.example.tefs_springboot.pojo.entity.Audio;
import com.example.tefs_springboot.pojo.entity.Video;
import com.example.tefs_springboot.pojo.vo.CutVideoVo;
import com.example.tefs_springboot.pojo.vo.ReplaceVideoVo;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leo on 2017/2/10.
 * 视频文件分割操作类
 */
public class VideoFileOperate {

    private static long  blockSize = 1 * 1024 * 1024;
    private loadingListener mListener;
    private boolean ffmpegWorkingFlag = false;

    /**
     * 获取视频文件时长
     *
     * @param file 文件
     * @return 时长 格式hh:MM:ss
     * @throws FileNotFoundException 视频不存在抛出此异常
     */
    private static String getVideoTime(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        List<String> commands = new ArrayList<String>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(file.getAbsolutePath());
        CmdResult result = runCommand(commands);
        String msg = result.getMsg();
        if (result.isSuccess()) {
            Pattern pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
            Matcher matcher = pattern.matcher(msg);
            String time = "";
            while (matcher.find()) {
                time = matcher.group();
            }
            return time;
        } else {
            return "";
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 去的文件长度，单位为字节b
     * @return 文件长度的字节数
     * @throws FileNotFoundException 文件未找到异常
     */
    private static long getVideoFileLength(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "不存在");
        }
        return file.length();
    }

    /**
     * @param filePath 要处理的文件路径
     * @return 分割后的文件路径
     * @throws Exception 文件
     */
    public static List<CutVideoVo> cutVideo(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + "文件不存在");
        }
        if (!filePath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }
        //从ffmpeg获得的时间长度00:00:00格式
        String videoTimeString = getVideoTime(file);
        System.out.println(videoTimeString);
        //将时长转换为秒数
        int videoSecond = parseTimeToSecond(videoTimeString);
        //视频文件的大小
        long fileLength = getVideoFileLength(file);
        List<CutVideoVo> cutedVideoPaths = new ArrayList<CutVideoVo>();
        int eachPartTime = 10;
        if (videoSecond <= eachPartTime) {//如果视频文件大小不大于预设值，则直接返回原视频文件
            CutVideoVo cutVideoVo=new CutVideoVo();
            cutVideoVo.setCutUrl(filePath);
            cutedVideoPaths.add(cutVideoVo);
        } else {
//            //如果超过预设大小，则需要切割
//            int partNum = (int) (fileLength / blockSize);//文件大小除以分块大小的商
//            long remainSize = fileLength % blockSize;//余数
//            int cutNum;
//            if (remainSize > 0) {
//                cutNum = partNum + 1;
//            } else {
//                cutNum = partNum;
//            }
            int cutNum=videoSecond / eachPartTime;
            if(videoSecond % eachPartTime>0) cutNum++;
            List<String> commands = new ArrayList<String>();
            String fileFolder = "D:\\upload";
            String fileName[] = file.getName().split("\\.");
            for (int i = 0; i < cutNum; i++) {
                commands.add("ffmpeg");
                commands.add("-ss");
                commands.add(parseTimeToString(eachPartTime * i));
                if (i != cutNum - 1) {
                    commands.add("-t");
                    commands.add(parseTimeToString(eachPartTime));
                }
//                commands.add("-acodec");
//                commands.add("copy");
//                commands.add("-vcodec");
//                commands.add("copy");
                commands.add("-i");
                commands.add(filePath);
                commands.add("-codec");
                commands.add("copy");
                commands.add(fileFolder + File.separator + fileName[0] + "_part" + i + "." + fileName[1]);
                commands.add("-y");
                CutVideoVo cutVideoVo=new CutVideoVo();
                cutVideoVo.setCutUrl("http://localhost:8181/api/file/" + fileName[0] + "_part" + i + "." + fileName[1]);
                cutedVideoPaths.add(cutVideoVo);
                runCommand(commands);
                commands.clear();
            }
            //ffmpeg -i test.mp4 -f mp3 -vn test.mp3
            for (int i = 0; i < cutNum; i++) {
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fileFolder + File.separator + fileName[0] + "_part" + i + "." + fileName[1]);
                commands.add("-f");
                commands.add("mp3");
                commands.add("-vn");
                commands.add(fileFolder + File.separator + fileName[0] + "_part" + i + ".mp3");
                commands.add("-y");
                runCommand(commands);
                commands.clear();
            }
        }

        return cutedVideoPaths;
    }



    /**
     * @param list 要处理的文件路径列表
     * @return 转换后后的文件路径
     * @throws Exception 文件
     */
    public static List<Audio> convertVideo(List<CutVideoVo> list) throws Exception {
        List<Audio> audioPaths = new ArrayList<Audio>();
            List<String> commands = new ArrayList<String>();
            String fileFolder = "D:\\upload";
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getCutUrl().equals("boundary")) continue;
                String fn=fileFolder+File.separator+list.get(i).getCutUrl().replace("http://localhost:8181/api/file/","");
                File file = new File(fn);
                if (!file.exists()) {
                    throw new FileNotFoundException(list.get(i).getCutUrl() + "文件不存在");
                }
                if (!fn.endsWith(".mp4")) {
                    throw new Exception("文件格式错误");
                }
                String fileName[] = file.getName().split("\\.");
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fn);
                commands.add("-f");
                commands.add("wav");
                commands.add("-ar");
                commands.add("16000");
                commands.add(fileFolder + File.separator + fileName[0] + ".mp3");
                commands.add("-y");
                Audio audio=new Audio();
                audio.setAudioUrl(fileFolder + File.separator + fileName[0] + ".mp3");
                audioPaths.add(audio);
                runCommand(commands);
                commands.clear();
            }
        return audioPaths;
    }

    /**
     * @param audioPath 替换的音频
     * @param videoPath 原视频
     * @return 新视频文件路径
     * @throws Exception 文件
     */
    public static ReplaceVideoVo replaceAudio(String audioPath,String videoPath) throws Exception {
        File audioFile = new File(audioPath);
        if (!audioFile.exists()) {
            throw new FileNotFoundException(audioPath + "文件不存在");
        }
        if (!audioPath.endsWith(".mp3")) {
            throw new Exception("文件格式错误");
        }
        File videoFile = new File(videoPath);
        if (!videoFile.exists()) {
            throw new FileNotFoundException(videoPath + "文件不存在");
        }
        if (!videoPath.endsWith(".mp4")) {
            throw new Exception("文件格式错误");
        }

        List<String> commands = new ArrayList<String>();
        String fileFolder = "D:\\upload";
        String fName[] = videoFile.getName().split("\\.");
        String fileName[]= fName[0].split("_replaced");

        //提取视频流
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(videoPath);
        commands.add("-vcodec");
        commands.add("copy");
        commands.add("-an");
        commands.add(fileFolder + File.separator + fileName[0] + "_videoStreaming.mp4");
        commands.add("-y");

        String videoStreaming="http://localhost:8181/api/file/" + fileName[0] + "_videoStreaming.mp4";
        System.out.println(videoStreaming);

        runCommand(commands);
        commands.clear();

        //视频流和音频流合并
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(videoStreaming);
        commands.add("-i");
        commands.add(audioPath);
        commands.add("-vcodec");
        commands.add("mpeg4");
        commands.add("-acodec");
        commands.add("copy");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.mp4");
        commands.add("-y");


        runCommand(commands);
        commands.clear();

        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.mp4");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.wmv");
        commands.add("-y");


        runCommand(commands);
        commands.clear();

        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.wmv");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.mp4");
        commands.add("-y");


        runCommand(commands);
        commands.clear();

        ReplaceVideoVo newVideo=new ReplaceVideoVo();
        newVideo.setReplacedUrl("http://localhost:8181/api/file/" + fileName[0] + "_replaced.mp4");
        System.out.println(newVideo.getReplacedUrl());

        //ffmpeg -i 0.mp4 -f wav -ar 16000 0.mp3

        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(fileFolder + File.separator + fileName[0] + "_replaced.mp4");
        commands.add("-f");
        commands.add("wav");
        commands.add("-ar");
        commands.add("16000");
        commands.add(fileFolder + File.separator + fileName[0] + ".mp3");
        commands.add("-y");


        runCommand(commands);
        commands.clear();

        return newVideo;
    }

    /**
     * @param list 要合并的视频列表
     * @return 合成的视频路径
     * @throws Exception 文件
     */
    public static List<Video> composeVideo(List<CutVideoVo> list) throws Exception {
        List<Video> videoList = new ArrayList<>();
        List<String> commands = new ArrayList<String>();
        List<String> tsList = new ArrayList<>();
        String fileFolder = "D:\\upload";
        String outPutName=null;
        for (int i = 1; i <= list.size(); i++) {
            if(i!=list.size()&&!list.get(i).getCutUrl().equals("boundary")) {
                String fn=fileFolder+File.separator+list.get(i).getCutUrl().replace("http://localhost:8181/api/file/","");
                File file = new File(fn);
                if (!file.exists()) {
                    throw new FileNotFoundException(list.get(i).getCutUrl() + "文件不存在");
                }
                if (!fn.endsWith(".mp4")) {
                    throw new Exception("文件格式错误");
                }
                String fileName[] = file.getName().split("\\.");


                //提取视频流
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fn);
                commands.add("-vcodec");
                commands.add("copy");
                commands.add("-an");
                commands.add(fileFolder + File.separator + fileName[0] + "_videoStreaming.mp4");
                commands.add("-y");

                String videoStreaming="http://localhost:8181/api/file/" + fileName[0] + "_videoStreaming.mp4";
                System.out.println(videoStreaming);

                runCommand(commands);
                commands.clear();

                //mp4转ts
                //ffmpeg -i input1.flv -c copy -bsf:v h264_mp4toannexb -f mpegts input1.ts
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fileFolder + File.separator + fileName[0] + "_videoStreaming.mp4");
                commands.add("-c");
                commands.add("copy");
                commands.add("-bsf:v");
                commands.add("h264_mp4toannexb");
                commands.add("-f");
                commands.add("mpegts");
                commands.add(fileFolder + File.separator + fileName[0] + "_videoStreaming.ts");
                commands.add("-y");
                tsList.add(fileFolder + File.separator + fileName[0] + "_videoStreaming.ts");
                runCommand(commands);
                commands.clear();
                outPutName=fileName[0].split("_")[0];
            }
            else{
                Date date = new Date();
                String stamp = String.valueOf(date.getTime());
                //ts合并
                String param="\"concat:";

                for (int j = 0; j < tsList.size(); j++) {
                    String fn=tsList.get(j);
                    File file = new File(fn);
                    if (!file.exists()) {
                        throw new FileNotFoundException(list.get(j).getCutUrl() + "文件不存在");
                    }
                    if (!fn.endsWith(".ts")) {
                        throw new Exception("文件格式错误");
                    }
                    param+=fn;
                    if(j!=tsList.size()-1) param+="|";
                    else param+="\"";
                }

                //ffmpeg -i "concat:input1.ts|input2.ts|input3.ts" -c copy -bsf:a aac_adtstoasc -movflags +faststart output.mp4
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(param);
                commands.add("-c");
                commands.add("copy");
                commands.add("-bsf:a");
                commands.add("aac_adtstoasc");
                commands.add("-movflags");
                commands.add("+faststart");
                commands.add(fileFolder + File.separator + outPutName + "_videoStreaming.mp4");
                commands.add("-y");
                runCommand(commands);
                commands.clear();

                //ffmpeg -i input1.mp3 -i input2.mp3 -i input2.mp3  
                // -filter_complex "[1]adelay=4000|4000[del1],[2]adelay=6000|6000[del2],[0][del1]amix[out],[out][del2]amix" output.mp3
                int num=0;
                commands.add("ffmpeg");
                for (int j = i-tsList.size(); j < i; j++){
                    if(list.get(j).getCutUrl().equals("boundary")) continue;
                    String fn=fileFolder+File.separator+list.get(j).getCutUrl().replace("http://localhost:8181/api/file/","");
                    File file = new File(fn);
                    String fileName[] = file.getName().split("\\.");
                    String fName = fileName[0].replace("_replaced","");
                    commands.add("-i");
                    commands.add(fileFolder + File.separator + fName + ".mp3");
                    num++;
                }
                commands.add("-filter_complex");
                String a="\"";
                for (int j = 1; j < num; j++){
                    a+= "[" + j + "]adelay=" + j*10000 + "|" + j*10000 + "[del" + j + "],";
                }
                //[0][del1]amix[out],[out][del2]amix[out],[out][del3]amix
                for (int j = 1; j < num; j++){
                    if(j==1){
                        a+="[0][del" + j + "]amix";
                    }
                    else{
                        a+="[out],[out][del" + j + "]amix";
                    }
                }
                a+="\"";
                commands.add(a);
                commands.add(fileFolder + File.separator + outPutName+".mp3");
                commands.add("-y");
                runCommand(commands);
                commands.clear();

                //ffmpeg -i 视频流.avi -i 音频流.mp3 -vcodec mpeg4 -acodec copy 合并.mp4
                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fileFolder + File.separator + outPutName + "_videoStreaming.mp4");
                commands.add("-i");
                commands.add(fileFolder + File.separator + outPutName+".mp3");
                commands.add("-vcodec");
                commands.add("mpeg4");
                commands.add("-acodec");
                commands.add("copy");
                commands.add(fileFolder + File.separator + outPutName + "_" + stamp + ".mp4");
                commands.add("-y");
                runCommand(commands);
                commands.clear();


                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fileFolder + File.separator + outPutName + "_" + stamp + ".mp4");
                commands.add(fileFolder + File.separator + outPutName + "_" + stamp + ".wmv");
                commands.add("-y");


                runCommand(commands);
                commands.clear();

                commands.add("ffmpeg");
                commands.add("-i");
                commands.add(fileFolder + File.separator + outPutName + "_" + stamp + ".wmv");
                commands.add(fileFolder + File.separator + outPutName + "_" + stamp + ".mp4");
                commands.add("-y");


                runCommand(commands);
                commands.clear();

                Video video = new Video();
                video.setVideoUrl("http://localhost:8181/api/file/" + outPutName + "_" + stamp + ".mp4");
                videoList.add(video);
                tsList.clear();
            }
        }

        return videoList;
    }

    /**
     * 执行Cmd命令方法
     *
     * @param command 相关命令
     * @return 执行结果
     */
    private static synchronized CmdResult runCommand(List<String> command) {
        System.out.println(command);
        CmdResult cmdResult = new CmdResult(false, "");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            final StringBuilder stringBuilder = new StringBuilder();
            final InputStream inputStream = process.getInputStream();
            new Thread(new Runnable() {//启动新线程为异步读取缓冲器，防止线程阻塞

                @Override
                public void run() {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
//                          mListener.isLoading(true);
                        }
//                        mListener.isLoading(false);
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            process.waitFor();
            cmdResult.setSuccess(true);
            cmdResult.setMsg(stringBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException("ffmpeg执行异常" + e.getMessage());
        }
        return cmdResult;
    }

    /**
     * 将字符串时间格式转换为整型，以秒为单位
     *
     * @param timeString 字符串时间时长
     * @return 时间所对应的秒数
     */
    private static int parseTimeToSecond(String timeString) {
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(timeString);
        if (!matcher.matches()) {
            try {
                throw new Exception("时间格式不正确");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] time = timeString.split(":");
        return Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]);
    }

    /**
     * 将秒表示时长转为00:00:00格式
     *
     * @param second 秒数时长
     * @return 字符串格式时长
     */
    private static String parseTimeToString(int second) {
        int end = second % 60;
        int mid = second / 60;
        if (mid < 60) {
            return mid + ":" + end;
        } else if (mid == 60) {
            return "1:00:" + end;
        } else {
            int first = mid / 60;
            mid = mid % 60;
            return first + ":" + mid + ":" + end;
        }

    }

    interface loadingListener {
        void isLoading(boolean loading);
    }

//    /**
//     * 用于判断ffmpeg是否在工作
//     *
//     * @return true在工作 暂时无法验证是否准确
//     */
//    public boolean isFFmpegWorking() {
//
//        mListener = new loadingListener() {
//            @Override
//            public void isLoading(boolean loading) {
//                ffmpegWorkingFlag = loading;
//            }
//        };
//        return ffmpegWorkingFlag;
//    }
}
