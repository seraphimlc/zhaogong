package com.dagong.mapper;

import com.dagong.pojo.FollowUser;

import java.util.List;

public interface FollowUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(FollowUser record);

    int insertSelective(FollowUser record);

    FollowUser selectByPrimaryKey(String id);


    List<FollowUser> getListSelective(FollowUser followUser);

    int updateByPrimaryKeySelective(FollowUser record);

    int updateByPrimaryKey(FollowUser record);
}