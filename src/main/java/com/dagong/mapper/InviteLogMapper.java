package com.dagong.mapper;

import com.dagong.pojo.InviteLog;

public interface InviteLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(InviteLog record);

    int insertSelective(InviteLog record);

    InviteLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InviteLog record);

    int updateByPrimaryKey(InviteLog record);
}