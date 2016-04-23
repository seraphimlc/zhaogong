package com.dagong.mapper;

import com.dagong.pojo.ApplyLog;

public interface ApplyLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApplyLog record);

    int insertSelective(ApplyLog record);

    ApplyLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApplyLog record);

    int updateByPrimaryKey(ApplyLog record);
}