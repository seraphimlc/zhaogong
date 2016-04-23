package com.dagong.service;

import com.dagong.mapper.CompanyEvaluationMapper;
import com.dagong.mapper.JobEvaluationMapper;
import com.dagong.mapper.UserEvaluationMapper;
import com.dagong.pojo.CompanyEvaluation;
import com.dagong.pojo.JobEvaluation;
import com.dagong.pojo.UserEvaluation;
import com.dagong.util.IdGenerator;
import com.dagong.util.ListUtil;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class EvaluationService {

    @Resource
    private JobEvaluationMapper jobEvaluationMapper;

    @Resource
    private CompanyEvaluationMapper companyEvaluationMapper;

    @Resource
    private UserEvaluationMapper userEvaluationMapper;

    @Resource
    private IdGenerator idGenerator;

    public boolean evaluateJob(String userId, String jobId, int rank, String comment) {
        if (ListUtil.isEmpty(getJobEvaluation(jobId, userId))) {
            JobEvaluation jobEvaluation = createJobEvaluation(userId, jobId, rank, comment);
            jobEvaluationMapper.insert(jobEvaluation);
            return true;
        }
        return false;
    }

    private JobEvaluation createJobEvaluation(String userId, String jobId, int rank, String comment) {
        JobEvaluation jobEvaluation = new JobEvaluation();
        jobEvaluation.setUserId(userId);
        jobEvaluation.setJobId(jobId);
        jobEvaluation.setRank(rank);
        jobEvaluation.setComment(comment);
        jobEvaluation.setId(idGenerator.generate(JobEvaluation.class.getSimpleName()));
        return jobEvaluation;
    }

    public List<JobEvaluation> getJobEvaluation(String jobId, String userId) {
        JobEvaluation jobEvaluation = new JobEvaluation();
        jobEvaluation.setJobId(jobId);
        jobEvaluation.setUserId(userId);
        return jobEvaluationMapper.getListSelective(jobEvaluation);
    }

    public boolean deleteJobEvaluation(@NonNull String evaluationId, @NonNull String jobId, @NonNull String userId) {
        JobEvaluation jobEvaluation = jobEvaluationMapper.selectByPrimaryKey(evaluationId);
        if (jobEvaluation != null &&
                jobId.equals(jobEvaluation.getJobId()) &&
                userId.equals(jobEvaluation.getUserId())) {
            jobEvaluationMapper.deleteByPrimaryKey(evaluationId);
            return true;
        }
        return false;
    }

    public boolean evaluateCompany(String userId, String companyId, int rank, String comment) {

        if (ListUtil.isEmpty(getCompanyEvaluation(companyId, userId))) {
            CompanyEvaluation companyEvaluation = createCompanyEvaluation(userId, companyId, rank, comment);
            companyEvaluationMapper.insert(companyEvaluation);
            return true;
        }
        return false;
    }

    private CompanyEvaluation createCompanyEvaluation(String userId, String companyId, int rank, String comment) {
        CompanyEvaluation companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setUserId(userId);
        companyEvaluation.setCompanyId(companyId);
        companyEvaluation.setRank(rank);
        companyEvaluation.setComment(comment);
        companyEvaluation.setId(idGenerator.generate(CompanyEvaluation.class.getSimpleName()));
        return companyEvaluation;
    }

    public List<CompanyEvaluation> getCompanyEvaluation(String companyId,String userId) {
        CompanyEvaluation companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setCompanyId(companyId);
        companyEvaluation.setUserId(userId);
        return companyEvaluationMapper.getListSelective(companyEvaluation);
    }

    public boolean deleteCompanyEvaluation(@NonNull String evaluationId, @NonNull String companyId, @NonNull String userId) {
        CompanyEvaluation companyEvaluation = companyEvaluationMapper.selectByPrimaryKey(evaluationId);
        if (companyEvaluation != null &&
                companyId.equals(companyEvaluation.getCompanyId()) &&
                userId.equals(companyEvaluation.getUserId())) {
            companyEvaluationMapper.deleteByPrimaryKey(evaluationId);
            return true;
        }
        return false;
    }

    public boolean evaluateUser(String companyUserId, String userId, int rank, String comment) {
        if (ListUtil.isEmpty(getUserEvaluation(companyUserId, userId))) {
            UserEvaluation userEvaluation = createUserEvaluation(companyUserId, userId, rank, comment);
            userEvaluationMapper.insert(userEvaluation);
            return true;
        }
        return false;
    }

    private UserEvaluation createUserEvaluation(String companyUserId, String userId, int rank, String comment) {
        UserEvaluation userEvaluation = new UserEvaluation();
        userEvaluation.setCompanyUser(companyUserId);
        userEvaluation.setUserId(userId);
        userEvaluation.setRank(rank);
        userEvaluation.setComment(comment);
        userEvaluation.setId(idGenerator.generate(UserEvaluation.class.getSimpleName()));
        return userEvaluation;
    }

    public List<UserEvaluation> getUserEvaluation(String companyUserId,String userId) {
        UserEvaluation userEvaluation = new UserEvaluation();
        userEvaluation.setCompanyUser(companyUserId);
        userEvaluation.setUserId(userId);
        return userEvaluationMapper.getListSelective(userEvaluation);
    }

}
