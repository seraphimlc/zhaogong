package com.dagong.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dagong.job.JobClient;
import com.dagong.job.vo.JobVO;
import com.dagong.mapper.JobLogMapper;
import com.dagong.mapper.JobMapper;
import com.dagong.mapper.JobTypeMapper;
import com.dagong.mapper.WantJobMapper;
import com.dagong.mq.SendMessageService;
import com.dagong.pojo.Job;
import com.dagong.pojo.JobLog;
import com.dagong.util.IdGenerator;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class JobService {

    private static int STATUS_INIT = 1;
    private static int STATUS_DEPLOY = 2;
    private static int STATUS_EXPIRED = 3;
    private static int STATUS_STOP = 4;
    private static int STATUS_DELETE = 5;

    private static String TOPIC_JOB = "job";
    private static String TOPIC_UPDATE_STATUS = "updateStatus";

    private int pageSize = 10;


    @Resource
    private JobTypeMapper jobTypeMapper;

    @Resource
    private JobMapper jobMapper;
    //
//
    @Resource
    private JobLogMapper jobLogMapper;

    @Resource
    WantJobMapper wantJobMapper;

    @Resource
    UserService userService;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private SendMessageService sendMessageService;


    public Job getJob(String jobId) {
        return jobMapper.selectByPrimaryKey(jobId);
//        return jobClient.getJobByJobId(jobId);
    }

    public Job getJobDetail(String jobId) {
        return jobMapper.selectByPrimaryKeyWithBLOBs(jobId);
    }

    public boolean createJob(String companyUserId, String jobName,
                             Integer needNumber, Integer jobType,
                             String detail, Integer startSalary,
                             Integer endSalary, Integer royalty,
                             Integer bonus, Integer discuss,
                             String address, String welfare,
                             String workedYear, Integer degree,
                             String phoneNumber, String contractor,
                             long startTime, long endTime,
                             boolean isDeploy) {
        Job job = generateJob(companyUserId, jobName, needNumber, jobType, detail, startSalary, endSalary, royalty, bonus, discuss, address, welfare, workedYear, degree, phoneNumber, contractor, startTime, endTime, isDeploy);
        jobMapper.insertSelective(job);
        log(job);
        return true;
    }

    public List<Job> searchJob(String companyId, int status,int page) {
        if(page<=0){
            page=1;
        }
        PageHelper.startPage(page,pageSize);
        return jobMapper.searchJob(companyId, status);
    }

    private void log(Job job) {
        JobLog jobLog = new JobLog();
        jobLog.setId(idGenerator.generate(JobLog.class.getSimpleName()));
        jobLog.setJobId(job.getId());
        jobLog.setModifyUser(job.getModifyUser());
        jobLog.setDetail(job.toString());
        jobLog.setVersion(job.getVersion());
        jobLogMapper.insert(jobLog);
    }

    public Job generateJob(String companyUserId, String jobName,
                           Integer needNumber, Integer jobType,
                           String detail, Integer startSalary,
                           Integer endSalary, Integer royalty,
                           Integer bonus, Integer discuss,
                           String address, String welfare,
                           String workedYear, Integer degree,
                           String phoneNumber, String contractor,
                           long startTime, long endTime,
                           boolean isDeploy) {


        Job job = new Job();
        job.setId(idGenerator.generate(Job.class.getSimpleName()));
        job.setJobName(jobName);
        job.setCompanyId(companyUserId);
        job.setModifyUser(companyUserId);
        job.setJobType(jobType);
        job.setCreateTime(System.currentTimeMillis());
        job.setModifyTime(System.currentTimeMillis());
        job.setStartTime(startTime);
        job.setEndTime(endTime);
        job.setNeedNumber(needNumber);
        job.setDetail(detail);
        job.setStartSalary(startSalary);
        job.setEndSalary(endSalary);
        job.setAddress(address);
        job.setContractor(contractor);
        job.setPhoneNumber(phoneNumber);
        job.setDegree(degree);
        job.setWorkedYear(workedYear);
        job.setWelfare(welfare);


        job.setRoyalty(royalty);
        job.setBonus(bonus);
        job.setDiscuss(discuss);
        job.setStatus(isDeploy ? STATUS_DEPLOY : STATUS_INIT);
        job.setVersion(1);
        return job;
    }

    public boolean modifyJob(String jobId, String companyUserId, String jobName,
                             Integer needNumber, Integer jobType,
                             String detail, Integer startSalary,
                             Integer endSalary, Integer royalty,
                             Integer bonus, Integer discuss,
                             String address, String welfare,
                             String workedYear, Integer degree,
                             String phoneNumber, String contractor,
                             long startTime, long endTime,int version) {

        Job job = new Job();
        job.setId(jobId);
        job.setJobName(StringUtils.isBlank(jobName) ? null : jobName);
        job.setVersion(version);
        job.setDetail(StringUtils.isBlank(detail) ? null : detail);
        job.setWelfare(StringUtils.isBlank(welfare) ? null : welfare);
        job.setWorkedYear(StringUtils.isBlank(workedYear) ? null : workedYear);
        job.setContractor(StringUtils.isBlank(contractor) ? null : contractor);
        job.setDegree(degree);
        job.setAddress(StringUtils.isBlank(address) ? null : address);
        job.setBonus(bonus);
        job.setDiscuss(discuss);
        job.setNeedNumber(needNumber);
        job.setStartSalary(startSalary);
        job.setEndSalary(endSalary);
        job.setPhoneNumber(phoneNumber);
        job.setStartTime(startTime);
        job.setEndTime(endTime);
        job.setRoyalty(royalty);
        int result = jobMapper.updateByPrimaryKeySelective(job);
        if (result == 0) {
            return false;
        }
        log(job);
        return true;
    }


    public boolean addDetail(String jobId, String companyUserId, String detail,int version) {

        Job job = new Job();
        job.setId(jobId);
        job.setVersion(version);
        job.setDetail(detail);
        job.setModifyUser(companyUserId);
        job.setModifyTime(System.currentTimeMillis());
        job.setVersion(version);
        jobMapper.updateByPrimaryKeySelective(job);
        jobMapper.insertSelective(job);
        log(job);
        return true;

    }


    public boolean deployJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_DEPLOY);

    }

    public boolean stopJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_STOP);
    }

    public boolean deleteJob(String companyUserId, String jobId) {


        return modifyStatus(companyUserId, jobId, STATUS_DELETE);
    }

    public boolean expireJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_EXPIRED);
    }

    private boolean modifyStatus(String companyUserId, String jobId, Integer status) {
        Job oldJob = jobMapper.selectByPrimaryKey(jobId);
        if(oldJob==null){
            return false;
        }
        Job job = new Job();
        job.setId(jobId);
        job.setModifyTime(System.currentTimeMillis());
        job.setModifyUser(companyUserId);
        job.setStatus(status);
        job.setVersion(oldJob.getVersion());

        int result = jobMapper.updateByPrimaryKeySelective(job);
        if (result == 0) {
            return false;
        }
       log(job);
        try {
            sendMessage(job.getId());
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

    private void sendMessage(String jobId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        sendMessageService.sendMessage(TOPIC_JOB, TOPIC_UPDATE_STATUS, jobId);
    }


}
