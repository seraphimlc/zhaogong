package com.dagong.mapper;

import com.dagong.pojo.CompanyEvaluation;

import java.util.List;

public interface CompanyEvaluationMapper {
    int deleteByPrimaryKey(String id);

    int insert(CompanyEvaluation record);

    int insertSelective(CompanyEvaluation record);

    List getListSelective(CompanyEvaluation record);

    CompanyEvaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CompanyEvaluation record);

    int updateByPrimaryKey(CompanyEvaluation record);
}