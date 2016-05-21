package com.dagong.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dagong.mapper.ApplyLogMapper;
import com.dagong.mapper.ApplyRecordMapper;
import com.dagong.mq.SendMessageService;
import com.dagong.pojo.ApplyLog;
import com.dagong.pojo.ApplyRecord;
import com.dagong.pojo.Job;
import com.dagong.user.vo.UserVO;
import com.dagong.util.IdGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
    private static long APPLY_INTERVAL = 1000 * 60 * 60 * 24 * 30;//one month
    private static String TOPIC_APPLY = "apply";
    private static String TOPIC_UPDATE_STATUS = "updateStatus";

    private static final int PAGE_SIZE = 10;

    @Resource
    private UserService userService;
    @Resource
    private ApplyRecordMapper applyRecordMapper;
    @Resource
    private ApplyLogMapper applyLogMapper;

    @Resource
    private JobService jobService;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private SendMessageService sendMessageService;

    public ApplyRecord getApplyById(String applyId){
        return applyRecordMapper.selectByPrimaryKey(applyId);
    }

    public boolean apply(String applyId, String userId, String jobId, String companyId, long applyTime) {
        if (!checkApplied(userId, jobId, applyId, applyTime)) {
            return false;
        }
        UserVO userVO = userService.getUserById(userId);
        Job job = jobService.getJob(jobId);
        if (userVO == null || job == null) {
            return false;
        }
        if (!job.getCompanyId().equals(companyId)) {
            return false;
        }
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setApplyTime(applyTime);
        applyRecord.setId(idGenerator.generate(ApplyRecord.class.getSimpleName()));
        applyRecord.setUserId(userId);
        applyRecord.setJobId(jobId);
        applyRecord.setCompanyId(job.getCompanyId());
        applyRecord.setCompanyUser(job.getModifyUser());
        applyRecord.setUserInfo(JSON.toJSONString(userVO));
        applyRecord.setStatus(STATUS_INIT);
        applyRecordMapper.insert(applyRecord);
        applyLogMapper.insert(createApplyLog(job.getModifyUser(), applyRecord.getId(), STATUS_INIT, STATUS_INIT, null));
        return true;
    }

    private boolean checkApplied(String userId, String jobId, String applyId, long applyTime) {
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setUserId(userId);
        applyRecord.setJobId(jobId);
        List<ApplyRecord> recordList = applyRecordMapper.getApplyRecord(applyRecord);
        boolean isValid = true;
        if (recordList != null && !recordList.isEmpty()) {
            for (ApplyRecord applyRecord1 : recordList) {
                if (applyTime - APPLY_INTERVAL < applyRecord1.getApplyTime()
                        || applyRecord1.getApplyId().equals(applyId)) {
                    isValid = false;
                }

            }
        }
        return isValid;
    }

    public List<ApplyRecord> getApplyRecordForNew(String companyUserId, String jobId, int page) {
        return getApplyRecord(companyUserId, jobId, STATUS_INIT, page);
    }

    public List<ApplyRecord> getApplyRecordForRead(String companyUserId, String jobId, int page) {
        return getApplyRecord(companyUserId, jobId, STATUS_READ, page);
    }

    public boolean read(String userId, String applyId) {
        return update(userId, applyId, STATUS_READ, null);
    }

    public boolean interview(String userId, String applyId, String description) {
        return update(userId, applyId, STATUS_INTERVIEW, description);
    }


    public boolean success(String userId, String applyId, String description) {
        return update(userId, applyId, STATUS_SUCCESS, description);
    }

    public boolean fail(String userId, String applyId, String description) {
        return update(userId, applyId, STATUS_FAILED, description);
    }


    public boolean refuse(String userId, String applyId, String description) {
        return update(userId, applyId, STATUS_REFUSE, description);
    }

    public boolean pass(String userId, String applyId, String description) {
        return update(userId, applyId, STATUS_ACCEPT, description);
    }

    private boolean update(String companyUserId, String applyId, int status, String description) {

        ApplyRecord oldApplyRecord = applyRecordMapper.selectByPrimaryKey(applyId);
        if(oldApplyRecord.getStatus()==status){
            return false;
        }

        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setId(applyId);
        applyRecord.setStatus(status);
        applyRecord.setCompanyUser(companyUserId);
        applyRecord.setDescription(description);
        applyRecord.setUpdateTime(System.currentTimeMillis());
        applyRecordMapper.updateByPrimaryKeySelective(applyRecord);
        applyLogMapper.insert(createApplyLog(companyUserId, applyId, oldApplyRecord.getStatus(), status, description));
        applyRecord.setApplyId(oldApplyRecord.getApplyId());
        try {
            sendMessage(applyRecord);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void sendMessage(ApplyRecord applyRecord) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        sendMessageService.sendMessage(TOPIC_APPLY, TOPIC_UPDATE_STATUS, applyRecord);
    }


    private List<ApplyRecord> getApplyRecord(String companyUserId, String jobId, int status, int page) {
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


    private ApplyLog createApplyLog(String companyUserId, String applyId, int oldStatus, int newStatus, String description) {
        ApplyLog applyLog = new ApplyLog();
        applyLog.setId(idGenerator.generate(ApplyLog.class.getSimpleName()));
        applyLog.setLogTime(System.currentTimeMillis());
        applyLog.setApplyId(applyId);
        applyLog.setModifyUser(companyUserId);
        applyLog.setNewStatus(newStatus);
        applyLog.setOldStatus(oldStatus);
        applyLog.setDescription(description);


        return applyLog;
    }


}
