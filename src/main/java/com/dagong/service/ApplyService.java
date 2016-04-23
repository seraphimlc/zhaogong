package com.dagong.service;

import com.dagong.mapper.ApplyLogMapper;
import com.dagong.mapper.ApplyRecordMapper;
import com.dagong.mapper.JobMapper;
import com.dagong.pojo.ApplyLog;
import com.dagong.pojo.ApplyRecord;
import com.dagong.pojo.Job;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class ApplyService {
    private static int STATUS_INIT = 1;
    private static int STATUS_READ = 2;
    private static int STATUS_ACCEPT = 3;
    private static int STATUS_REFUSE = 4;
    private static int STATUS_INTERVIEW = 5;
    private static int STATUS_SUCCESS = 6;
    private static int STATUS_FAILED = 7;

    @Resource
    private ApplyLogMapper applyLogMapper;

    @Resource
    private ApplyRecordMapper applyRecordMapper;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private IdGenerator idGenerator;

    public boolean apply(String userId, String companyId, String jobId) {

        Job job = jobMapper.selectByPrimaryKey(jobId);
        if (job == null || !job.getCompanyId().equals(companyId)) {
            return false;
        }
        ApplyRecord applyRecord = createApplyRecord(userId, jobId);
        ApplyLog applyLog = createApplyLog(userId, null, applyRecord.getId(), applyRecord.getStatus(), applyRecord.getStatus());

        applyRecordMapper.insert(applyRecord);
        applyLogMapper.insert(applyLog);
        return true;
    }

    public boolean read(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_READ);
    }

    public boolean accept(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_ACCEPT);
    }
    public boolean refuse(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_REFUSE);
    }

    public boolean interview(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_INTERVIEW);
    }

    public boolean success(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_SUCCESS);
    }
    public boolean fail(String companyId,String applyId){
        return updateStatus(companyId,null,applyId,STATUS_FAILED);
    }

    private boolean updateStatus(String companyUserId,String companyUserName,String applyId,int status){
        ApplyRecord applyRecord = applyRecordMapper.selectByPrimaryKey(applyId);
        ApplyRecord newApplyRecord = new ApplyRecord();
        newApplyRecord.setId(applyId);
        newApplyRecord.setStatus(status);
        ApplyLog applyLog = createApplyLog(companyUserId,companyUserName,applyId,applyRecord.getStatus(),newApplyRecord.getStatus());
        applyRecordMapper.updateByPrimaryKeySelective(newApplyRecord);
        applyLogMapper.insert(applyLog);
        return true;
    }

    private ApplyLog createApplyLog(String userId, String userName, String applyId, int oldStatus, int newStatus) {
        ApplyLog applyLog = new ApplyLog();
        applyLog.setId(idGenerator.generate(ApplyLog.class.getSimpleName()));
        applyLog.setLogTime(new Date());
        applyLog.setApplyId(applyId);
        applyLog.setModifyUser(userId);
        applyLog.setModifyUser(userName);
        applyLog.setNewStatus(newStatus);
        applyLog.setOldStatus(oldStatus);
        return applyLog;
    }

    private ApplyRecord createApplyRecord(String userId, String jobId) {
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setStatus(STATUS_INIT);
        applyRecord.setUserId(userId);
        applyRecord.setJobId(jobId);
        applyRecord.setId(idGenerator.generate(ApplyRecord.class.getSimpleName()));
        return applyRecord;
    }


}
