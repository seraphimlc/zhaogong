package com.dagong.mq.apply;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.dagong.mq.MessageProcessor;
import com.dagong.service.ApplyService;
import com.dagong.service.ExcutorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuchang on 16/4/24.
 */
@Service
public class ApplyJobMessageProcessor extends MessageProcessor {

    @Resource
    private ExcutorService excutorService;

    @Resource
    private ApplyService applyService;


    public ApplyJobMessageProcessor() {
        this.setTopic("apply");
        this.setTag("userApplyJob");
    }

    @Override
    protected void process(List<MessageExt> list) {
        List<HashMap> mapList = new ArrayList<>();
        list.forEach(messageExt -> {
            System.out.println("messageExt = " + messageExt);
            try {
                String msg = new String(messageExt.getBody(), "utf-8");
                HashMap msgMap = (HashMap) JSON.parseObject(msg,HashMap.class);
                mapList.add(msgMap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        processMessageByThread(mapList);
    }

    private void processMessageByThread(List<HashMap> maps) {
        List<Runnable> runnableList = new ArrayList<>();
        if (isMultiThread && maps.size() > 1) {
            maps.forEach(hashMap -> {
                runnableList.add(() -> {
                    saveApplyRecord(hashMap);

                });
            });
            if (!runnableList.isEmpty()) {
                excutorService.excute(runnableList);
            }
        } else {
            maps.forEach(hashMap -> {
                saveApplyRecord(hashMap);


            });
        }


    }

    private void saveApplyRecord(HashMap hashMap) {
        String employeeId = (String) hashMap.get("userId");
        String jobId = (String) hashMap.get("jobId");
        long applyTime = (long) hashMap.get("applyTime");
        String companyId = (String) hashMap.get("companyId");
        String applyId = (String) hashMap.get("applyId");
        System.out.println("employeeId = " + employeeId);
        System.out.println("jobId = " + jobId);
        System.out.println("applyTime = " + applyTime);
        System.out.println("companyId = " + companyId);
        System.out.println("applyId = " + applyId);
        applyService.apply(applyId,employeeId, jobId, companyId, applyTime);
    }
}