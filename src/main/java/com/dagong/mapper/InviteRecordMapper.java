package com.dagong.mapper;

import com.dagong.pojo.InviteRecord;

public interface InviteRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(InviteRecord record);

    int insertSelective(InviteRecord record);

    InviteRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InviteRecord record);

    int updateByPrimaryKey(InviteRecord record);
}