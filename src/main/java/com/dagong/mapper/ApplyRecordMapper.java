package com.dagong.mapper;

import com.dagong.pojo.ApplyRecord;

import java.util.List;

public interface ApplyRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ApplyRecord record);

    int insertSelective(ApplyRecord record);

    ApplyRecord selectByPrimaryKey(String id);

    ApplyRecord getApplyRecordByApplyId(String applyId);

    int updateByPrimaryKeySelective(ApplyRecord record);

    int updateByPrimaryKey(ApplyRecord record);

    List<ApplyRecord> getApplyRecord(ApplyRecord applyRecord);
}