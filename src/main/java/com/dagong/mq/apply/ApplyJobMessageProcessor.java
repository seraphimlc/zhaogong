package com.dagong.mq.apply;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.dagong.mq.MessageProcessor;
import com.dagong.service.ApplyService;
import com.dagong.service.ExcutorService;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuchang on 16/4/24.
 */
public class ApplyJobMessageProcessor extends MessageProcessor {

    @Resource
    private ExcutorService excutorService;

    @Resource
    private ApplyService applyService;

    public ApplyJobMessageProcessor() {
        this.setTopic("apply");
        this.setTag("applyJob");
    }

    @Override
    protected void process(List<MessageExt> list) {
        List<HashMap> mapList = new ArrayList<>();
        list.forEach(messageExt -> {
            try {
                String msg = new String(messageExt.getBody(), "utf-8");
                HashMap msgMap = (HashMap) JSON.parse(msg);
                mapList.add(msgMap);
                processMessageByThread(mapList);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    private void processMessageByThread(List<HashMap> maps) {
        List<Runnable> runnableList = new ArrayList<>();

        maps.forEach(hashMap -> {
            runnableList.add(() ->{
                    String employeeId = (String) hashMap.get("userId");
                    String jobId = (String) hashMap.get("jobId");
                    long applyTime = (long) hashMap.get("applyTime");
                    applyService.apply(employeeId, jobId, applyTime);

            });
        });
        if(!runnableList.isEmpty()){
            excutorService.excute(runnableList);
        }
    }


}
