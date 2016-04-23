package com.dagong.service;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dagong.mapper.JobLogMapper;
import com.dagong.mapper.JobMapper;
import com.dagong.mapper.JobTypeMapper;
import com.dagong.mapper.WantJobMapper;
import com.dagong.mq.SendMessageWrapper;
import com.dagong.pojo.Job;
import com.dagong.pojo.JobLog;
import com.dagong.pojo.WantJob;
import com.dagong.util.IdGenerator;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by liuchang on 16/1/17.
 */
@Service

public class JobService {

    private static int STATUS_INIT = 1;
    private static int STATUS_DEPLOY = 2;
    private static int STATUS_EXPIRED = 3;
    private static int STATUS_CANCEL = 4;
    private static int STATUS_DELETE = 5;

    private int pageSize = 10;

    @Resource
    private JobTypeMapper jobTypeMapper;

    @Resource
    private JobMapper jobMapper;


    @Resource
    private JobLogMapper jobLogMapper;

    @Resource
    WantJobMapper wantJobMapper;

    @Resource
    UserService userService;

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private SearchService searchService;

    @Resource(name = "jobMessageSender")
    private SendMessageWrapper sendMessageWrapper;

    public List<Job> searchJob(String userId) {

        List<WantJob> wantJobs = wantJobMapper.selectByUserId(userId);
        return searchJob(wantJobs);
    }

    public List<Map> searchJobFromIndex(String userId) {
        List<WantJob> wantJobs = wantJobMapper.selectByUserId(userId);
        return searchJobFromIndex(wantJobs);
    }

    private List<Map> searchJobFromIndex(List<WantJob> wantJobs) {
        List<Map> jobs = new ArrayList<>();
        wantJobs.forEach(wantJob -> {
            jobs.addAll(searchService.searchJobByType(wantJob.getJobType()));
        });
        return jobs;
    }

    private List<Job> searchJob(List<WantJob> wantJobs) {
        List<Job> jobList = new ArrayList();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(wantJobs.size() + 1);
        for (WantJob wantJob : wantJobs) {
            new Thread(new GetJobTask(jobMapper, wantJob.getJobType(), jobList, cyclicBarrier)).start();
        }
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("jobList = " + jobList.size());
        return jobList;

    }

    public boolean createJob(String companyUserId, String companyId, String jobName, Integer needNumber, Integer jobType, String detail, Integer startSalary, Integer endSalary, Integer royalty, Integer bonus, Integer discuss, Date startTime, Date endTime, boolean isDeploy) {
        Job job = createJob(companyId, jobName, needNumber, jobType, detail, startSalary, endSalary, royalty, bonus, discuss, startTime, endTime, isDeploy);
        createJob(job,companyUserId);

        return true;
    }

    public boolean createJob(Job job,String companyUserId) {
        JobLog jobLog = createJobLog(companyUserId, job);
        jobMapper.insert(job);
        jobLogMapper.insert(jobLog);
        try {
            sendMessageWrapper.sendMessage("createJob", job.getId());
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

    private JobLog createJobLog(String companyUserId, Job job) {
        JobLog jobLog = new JobLog();
        jobLog.setId(idGenerator.generate(JobLog.class.getSimpleName()));
        jobLog.setJobId(job.getId());
        jobLog.setModifyUser(companyUserId);
        jobLog.setDetail(job.toString());
        return jobLog;
    }


    private Job createJob(String companyUserId, String jobName, Integer needNumber, Integer jobType, String detail, Integer startSalary, Integer endSalary, Integer royalty, Integer bonus, Integer discuss, Date startTime, Date endTime, boolean isDeploy) {
        Job job = new Job();
        job.setId(idGenerator.generate(Job.class.getSimpleName()));
        job.setJobName(jobName);
        job.setNeedNumber(needNumber);
        job.setCreateTime(new Date());
        job.setJobType(jobType);
        job.setDetail(detail);
        job.setStartSalary(startSalary);
        job.setEndSalary(endSalary);
        job.setRoyalty(royalty);
        job.setBonus(bonus);
        job.setDiscuss(discuss);
        job.setCompanyId(companyUserId);
        job.setStatus(isDeploy ? STATUS_DEPLOY : STATUS_INIT);
        job.setStartTime(startTime);
        job.setEndTime(endTime);
        job.setModifyUser(companyUserId);
        job.setModifyTime(new Date());
        return job;
    }

    public boolean modifyJob(String companyUserId, String jobId, String jobName, String detail, Integer needNumber, Integer jobType, Integer discuss, Integer startSalary, Integer endSalary, Integer royalty, Integer bonus, Date startTime, Date endTime, Integer status) {
        Job job = jobMapper.selectByPrimaryKey(jobId);
        job.setJobName(jobName);
        job.setDetail(detail);
        job.setNeedNumber(needNumber);
        job.setJobType(jobType);
        job.setDiscuss(discuss);
        job.setStartSalary(startSalary);
        job.setEndSalary(endSalary);
        job.setRoyalty(royalty);
        job.setBonus(bonus);
        job.setModifyTime(new Date());
        job.setModifyUser(companyUserId);
        job.setStartTime(startTime);
        job.setEndTime(endTime);
        job.setStatus(status);
        JobLog jobLog = createJobLog(companyUserId, job);
        jobMapper.updateByPrimaryKeySelective(job);
        jobLogMapper.insert(jobLog);
        try {
            sendMessageWrapper.sendMessage("modifyJob",job.getId());
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

    public List<Job> listJob(String companyId, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        Job job = new Job();
        if (StringUtils.isNotBlank(companyId)) {
            job.setCompanyId(companyId);
        }
        return jobMapper.listJob(job);
    }

    public Map getJob(String jobId) {
        return searchService.getJob(jobId);
    }

    public Job getJobFromDB(String jobId){
        return jobMapper.selectByPrimaryKey(jobId);
    }

    public boolean deployJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_DEPLOY);

    }

    public boolean cancelJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_CANCEL);
    }

    public boolean deleteJob(String companyUserId, String jobId) {


        return modifyStatus(companyUserId, jobId, STATUS_DELETE);
    }

    public boolean expireJob(String companyUserId, String jobId) {

        return modifyStatus(companyUserId, jobId, STATUS_EXPIRED);
    }

    private boolean modifyStatus(String companyUserId, String jobId, Integer status) {
        Job job = new Job();
        job.setId(jobId);
        job.setModifyTime(new Date());
        job.setModifyUser(companyUserId);
        job.setStatus(status);
        jobMapper.updateByPrimaryKeySelective(job);
        jobLogMapper.insert(createJobLog(companyUserId, job));
        return true;
    }


}

class GetJobTask implements Runnable {
    private CyclicBarrier barrier;
    private Integer jobType;
    private JobMapper jobMapper;
    private List jobList;

    public GetJobTask(JobMapper jobMapper, Integer jobType, List jobList, CyclicBarrier barrier) {
        this.jobMapper = jobMapper;
        this.jobType = jobType;
        this.barrier = barrier;
        this.jobList = jobList;
    }


    public void run() {
        try {
            Job job = new Job();
            job.setJobType(jobType);
            List list = jobMapper.listJob(job);

            jobList.addAll(list);
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }
}
