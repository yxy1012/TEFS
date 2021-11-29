package com.example.tefs_springboot.pojo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Video implements Serializable {
  private Integer videoId;
  private String  videoName;
  private String  videoUrl;
  private String  videoSize;
  private String  videoAddtime;
  private String  videoStatus;
  private String  videoLength;
  private Integer videoDuration;
  private String  videoPoster;
  private Integer userId;
  private String  videoType;
}
