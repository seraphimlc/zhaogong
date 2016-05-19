package com.dagong.mapper;

import com.dagong.pojo.CompanyUserLog;

public interface CompanyUserLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(CompanyUserLog record);

    int insertSelective(CompanyUserLog record);

    CompanyUserLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CompanyUserLog record);

    int updateByPrimaryKey(CompanyUserLog record);
}