package com.example.tefs_springboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.tefs_springboot.pojo.entity.Audio;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title : SDK 调用实例</p>
 * <p>Description : </p>
 * <p>Date : 2020/4/20 </p>
 *
 * @author : hejie
 */
public class LfasrSDK {
    private static final String APP_ID = "32e84ed8";
    private static final String SECRET_KEY = "34520c983adaa7549599d84e5ab2d536";

    /**
     * 带有业务参数，调用样例
     *
     * @throws InterruptedException e
     */
    public static List<Message> businessExtraParams(List<Audio> audiolist) throws InterruptedException {
        //1、创建客户端实例
        LfasrClient lfasrClient = LfasrClient.getInstance(APP_ID, SECRET_KEY);

        //2、上传
        //2.1、设置业务参数
        Map<String, String> param = new HashMap<>(16);
        //是否开启分词：默认 false
        //param.put("has_participle","true");
        //转写结果中最大的候选词个数：默认：0，最大不超过5
        //param.put("max_alternatives","2");

        //是否开启角色分离：默认为false
        //param.put("has_seperate","true");
        //发音人个数，可选值：0-10，0表示盲分：默认 2
        //param.put("speaker_number","3");
        //角色分离类型 1-通用角色分离；2-电话信道角色分离：默认 1
        //param.put("role_type","1");

        //语种： cn-中文（默认）;en-英文（英文不支持热词）
        param.put("language", "cn");
        //垂直领域个性化：法院-court；教育-edu；金融-finance；医疗-medical；科技-tech
        param.put("pd","edu");

        List<Message> textlist=new ArrayList<>();

        for(int i=0;i<audiolist.size();i++)
        {
            Message task = lfasrClient.upload(
                    audiolist.get(i).getAudioUrl()
                    , param);
            String taskId = task.getData();
            System.out.println("转写任务 taskId：" + taskId);

            //3、查看转写进度
            int status = 0;
            while (status != 9) {
                Message message = lfasrClient.getProgress(taskId);
                JSONObject object = JSON.parseObject(message.getData());
                status = object.getInteger("status");
                System.out.println(message.getData());
                TimeUnit.SECONDS.sleep(2);
            }
            //4、获取结果
            Message result = lfasrClient.getResult(taskId);
            System.out.println("转写结果: \n" + result.getData());
            textlist.add(result);
        }

        //退出程序，关闭线程资源，仅在测试main方法时使用。
        //System.exit(0);
        return textlist;
    }

}
