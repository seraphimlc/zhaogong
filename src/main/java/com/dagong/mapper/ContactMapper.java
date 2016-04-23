package com.dagong.mapper;

import com.dagong.pojo.Contact;

public interface ContactMapper {
    int deleteByPrimaryKey(String id);

    int insert(Contact record);

    int insertSelective(Contact record);

    Contact selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Contact record);

    int updateByPrimaryKey(Contact record);
}