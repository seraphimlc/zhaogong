package com.dagong.mapper;

import com.dagong.pojo.JobEvaluation;

import java.util.List;

public interface JobEvaluationMapper {
    int deleteByPrimaryKey(String id);

    int insert(JobEvaluation record);

    List getListSelective(JobEvaluation jobEvaluation);

    int insertSelective(JobEvaluation record);

    JobEvaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JobEvaluation record);

    int updateByPrimaryKey(JobEvaluation record);
}