package com.example.tefs_springboot.service;

import com.example.tefs_springboot.pojo.entity.Audio;
import com.iflytek.msp.lfasr.model.Message;

import java.util.List;

public interface LfasrService {
    List<Message> extraParams(List<Audio> audiolist);
}
