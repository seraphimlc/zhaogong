package com.dagong.mapper;

import com.dagong.pojo.FollowCompany;

import java.util.List;

public interface FollowCompanyMapper {
    int deleteByPrimaryKey(String id);

    int insert(FollowCompany record);

    int insertSelective(FollowCompany record);

    FollowCompany selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FollowCompany record);

    int updateByPrimaryKey(FollowCompany record);

    List<FollowCompany> getListSelective(FollowCompany followCompany);
}