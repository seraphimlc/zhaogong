package com.dagong.mapper;

import com.dagong.pojo.FollowJob;

import java.util.List;

public interface FollowJobMapper {
    int deleteByPrimaryKey(String id);

    int insert(FollowJob record);

    int insertSelective(FollowJob record);

    FollowJob selectByPrimaryKey(String id);

    List<FollowJob> getListSelective(FollowJob followJob);

    int updateByPrimaryKeySelective(FollowJob record);

    int updateByPrimaryKey(FollowJob record);
}