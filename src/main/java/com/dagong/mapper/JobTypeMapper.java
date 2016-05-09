package com.dagong.mapper;

import com.dagong.pojo.JobType;

public interface JobTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JobType record);

    int insertSelective(JobType record);

    JobType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobType record);

    int updateByPrimaryKey(JobType record);
}