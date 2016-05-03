package com.dagong.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dagong.job.JobClient;
import com.dagong.job.vo.JobVO;
import com.dagong.mapper.ApplyRecordMapper;
import com.dagong.pojo.ApplyRecord;
import com.dagong.pojo.User;
import com.dagong.user.vo.UserVO;
import com.dagong.util.IdGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    private static final int PAGE_SIZE = 10;

    @Resource
    private UserService userService;
    @Resource
    private ApplyRecordMapper applyRecordMapper;

    @Resource
    private JobService jobService;

    @Resource
    private IdGenerator idGenerator;


    public boolean apply(String userId, String jobId,String companyId, long applyTime) {
        UserVO userVO = userService.getUserById(userId);
        JobVO jobVO = jobService.getJob(jobId);
        if (userVO == null || jobVO == null) {
            return false;
        }
        if(!jobVO.getCompanyId().equals(companyId)){
            return false;
        }
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setApplyTime(applyTime);
        applyRecord.setId(idGenerator.generate(ApplyRecord.class.getSimpleName()));
        applyRecord.setUserId(userId);
        applyRecord.setJobId(jobId);
        applyRecord.setCompanyId(jobVO.getCompanyId());
        applyRecord.setCompanyUser(jobVO.getCompanyUser());
        applyRecord.setUserInfo(JSON.toJSONString(userVO));
        applyRecord.setStatus(STATUS_INIT);
        applyRecordMapper.insert(applyRecord);
        return true;
    }

    public List<ApplyRecord> getApplyRecordForNew(String companyUserId,String jobId,int page){
        return getApplyRecord(companyUserId,jobId,STATUS_INIT,page);
    }
    public List<ApplyRecord> getApplyRecordForRead(String companyUserId,String jobId,int page){
        return getApplyRecord(companyUserId,jobId,STATUS_READ,page);
    }

    private List<ApplyRecord> getApplyRecord(String companyUserId,String jobId, int status, int page) {
        if (page <= 0) {
            page = 1;
        }
        PageHelper.startPage(page, PAGE_SIZE);
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setCompanyUser(companyUserId);
        applyRecord.setJobId(jobId);
        applyRecord.setStatus(status);

        Page<ApplyRecord> pages = (Page<ApplyRecord>) applyRecordMapper.getApplyRecord(applyRecord);
        return pages;

    }


}
