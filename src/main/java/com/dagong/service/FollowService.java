package com.dagong.service;

import com.dagong.mapper.CompanyUserMapper;
import com.dagong.mapper.FollowCompanyMapper;
import com.dagong.mapper.FollowJobMapper;
import com.dagong.mapper.FollowUserMapper;
import com.dagong.mq.SendMessageService;
import com.dagong.pojo.CompanyUser;
import com.dagong.pojo.FollowCompany;
import com.dagong.pojo.FollowJob;
import com.dagong.pojo.FollowUser;
import com.dagong.util.IdGenerator;
import com.dagong.util.ListUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuchang on 16/1/17.
 */
@Service

public class FollowService {
    @Resource
    private FollowCompanyMapper followCompanyMapper;
    @Resource
    private FollowJobMapper followJobMapper;
    @Resource
    private FollowUserMapper followUserMapper;
    @Resource
    private CompanyUserMapper companyUserMapper;
    @Resource
    private IdGenerator idGenerator;

    @Resource
    private SendMessageService sendMessageService;

    public boolean followCompany(String userId, String companyId) {
        List<FollowCompany> followCompanies = getFollowCompany(userId, companyId);
        if (ListUtil.isEmpty(followCompanies)) {
            FollowCompany followCompany = createFollowCompany(userId, companyId);
            followCompanyMapper.insert(followCompany);
            return true;
        }
        return false;
    }

    private FollowCompany createFollowCompany(String userId, String companyId) {
        FollowCompany followCompany = new FollowCompany();
        followCompany.setCompanyId(idGenerator.generate(FollowCompany.class.getSimpleName()));
        followCompany.setCompanyId(companyId);
        followCompany.setUserId(userId);
        return followCompany;
    }

    public List<FollowCompany> getFollowCompany(String userId, String companyId) {
        FollowCompany followCompany = new FollowCompany();
        followCompany.setUserId(userId);
        followCompany.setCompanyId(companyId);
        return followCompanyMapper.getListSelective(followCompany);

    }

    public boolean dismissFollowCompany(String followId, String userId, String companyId) {
        List<FollowCompany> followCompanies = getFollowCompany(userId, companyId);
        {
            if (!ListUtil.isEmpty(followCompanies)) {
                for (FollowCompany followCompany : followCompanies) {
                    if (followCompany.getId().equals(followId)) {
                        followCompanyMapper.deleteByPrimaryKey(followId);
                    }
                }
            }
        }
        return true;
    }

    public boolean followJob(String userId,String[] jobIds){
        for(String jobId:jobIds) {
            List<FollowJob> followJobs = getFollowJob(userId, jobId);
            if (ListUtil.isEmpty(followJobs)) {
                FollowJob followJob = createFollowJob(userId, jobId);
                followJobMapper.insert(followJob);
            }
        }
        return true;
    }

    public Page<FollowJob> getFollowJobForUser(String userId,int pageNo,int pageSize){
        PageHelper.startPage(pageNo, pageSize);
        FollowJob followJob = new FollowJob();
        followJob.setUserId(userId);
        Page<FollowJob> page = (Page<FollowJob>) followJobMapper.getListSelective(followJob);
        return page;
    }

    public boolean dismissFollowJob(String followId,String userId,String jobId){
        List<FollowJob> followJobs = getFollowJob(userId, jobId);
        if(!ListUtil.isEmpty(followJobs)){
            for(FollowJob followJob:followJobs){
                if(followJob.getId().equals(followId)){
                    followJobMapper.deleteByPrimaryKey(followId);
                }
            }
        }
        return true;
    }

    private FollowJob createFollowJob(String userId, String jobId) {
        FollowJob followJob = new FollowJob();
        followJob.setJobId(jobId);
        followJob.setUserId(userId);
        followJob.setId(idGenerator.generate(FollowJob.class.getSimpleName()));
        return followJob;
    }

    public List<FollowJob> getFollowJob(String userId,String jobId){
        FollowJob followJob = new FollowJob();
        followJob.setUserId(userId);
        followJob.setJobId(jobId);
        return followJobMapper.getListSelective(followJob);
    }

    public boolean followUser(String companyUserId,String userId){
        CompanyUser companyUser = companyUserMapper.selectByPrimaryKey(companyUserId);
        List<FollowUser> followUsers = getFollowUser(companyUserId, userId, companyUser.getCompanyId());
        if (ListUtil.isEmpty(followUsers)) {
            FollowUser followUser = createFollowUser(companyUserId, userId, companyUser);
            followUserMapper.insert(followUser);
            return true;
        }
        return false;
    }

    public boolean dismissFollowUser(String followId,String companyUserId,String userId){
        List<FollowUser> followUsers = getFollowUser(companyUserId, userId,null);

        if(!ListUtil.isEmpty(followUsers)){
            for(FollowUser followUser:followUsers){
                if(followUser.getId().equals(followId)){
                    followUserMapper.deleteByPrimaryKey(followId);
                }
            }
        }
        return true;
    }
    private FollowUser createFollowUser(String companyUserId, String userId, CompanyUser companyUser) {
        FollowUser followUser = new FollowUser();
        followUser.setCompanyUser(companyUserId);
        followUser.setCompanyId(companyUser.getCompanyId());
        followUser.setUserId(userId);
        return followUser;
    }

    public List<FollowUser> getFollowUser(String companyUserId,String userId, String companyId) {
        FollowUser followUser = new FollowUser();
        followUser.setUserId(userId);
        followUser.setCompanyId(companyId);
        followUser.setCompanyUser(companyUserId);
        return followUserMapper.getListSelective(followUser);

    }




}
