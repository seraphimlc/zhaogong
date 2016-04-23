package com.dagong.mapper;

import com.dagong.pojo.ApplyRecord;

public interface ApplyRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApplyRecord record);

    int insertSelective(ApplyRecord record);

    ApplyRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApplyRecord record);

    int updateByPrimaryKey(ApplyRecord record);
}