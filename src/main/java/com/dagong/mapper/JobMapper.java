package com.dagong.mapper;

import com.dagong.pojo.Job;
import com.dagong.pojo.JobExample;
import java.util.List;

public interface JobMapper {
    int deleteByPrimaryKey(String id);

    int insert(Job record);

    int insertSelective(Job record);

    List<Job> selectByExampleWithBLOBs(JobExample example);

    List<Job> selectByExample(JobExample example);

    Job selectByPrimaryKey(String id);

    Job selectByPrimaryKeyWithBLOBs(String id);

    Job selectByPrimaryKeyOnlyBLOBs(String id);

    int updateByPrimaryKeySelective(Job record);

}