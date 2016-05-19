package com.dagong.mapper;

import com.dagong.pojo.Invitation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvitationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Invitation record);

    int insertSelective(Invitation record);

    Invitation selectByPrimaryKey(String id);

    List<Invitation> selectByUserId(@Param("userId") String userId, @Param("companyId") String companyId, int status);

    int updateByPrimaryKeySelective(Invitation record);

    int updateByPrimaryKey(Invitation record);
}