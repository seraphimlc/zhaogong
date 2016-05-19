package com.dagong.mapper;

import com.dagong.pojo.CompanyLog;

public interface CompanyLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(CompanyLog record);

    int insertSelective(CompanyLog record);

    CompanyLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CompanyLog record);

    int updateByPrimaryKey(CompanyLog record);
}