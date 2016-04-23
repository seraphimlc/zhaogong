package com.dagong.mapper;

import com.dagong.pojo.UserEvaluation;

import java.util.List;

public interface UserEvaluationMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserEvaluation record);

    int insertSelective(UserEvaluation record);

    UserEvaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserEvaluation record);

    List getListSelective(UserEvaluation record);

    int updateByPrimaryKey(UserEvaluation record);
}