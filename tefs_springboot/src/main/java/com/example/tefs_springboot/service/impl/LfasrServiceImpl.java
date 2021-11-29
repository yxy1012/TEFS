package com.example.tefs_springboot.service.impl;

import com.example.tefs_springboot.pojo.entity.Audio;
import com.example.tefs_springboot.service.LfasrService;
import com.example.tefs_springboot.utils.LfasrSDK;
import com.iflytek.msp.lfasr.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LfasrServiceImpl implements LfasrService {

    @Override
    public List<Message> extraParams(List<Audio> audiolist){
        List<Message> list=new ArrayList<>();
        try{
            list=LfasrSDK.businessExtraParams(audiolist);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return  list;
    }
}
