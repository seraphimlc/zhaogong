package com.dagong.service;

import com.dagong.mapper.CompanyUserLogMapper;
import com.dagong.mapper.CompanyUserMapper;
import com.dagong.mapper.InvitationMapper;
import com.dagong.pojo.CompanyUser;
import com.dagong.pojo.CompanyUserLog;
import com.dagong.pojo.Invitation;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liuchang on 16/5/14.
 */
@Service
public class InvitationService {

    private Jedis jedisClient = null;
    private static int TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 7;//有效期七天

    private static final int STATUS_INIT = 1;
    private static final int STATUS_PASS = 2;
    private static final int STATUS_REJECT = 3;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private InvitationMapper invitationMapper;

    @Resource
    private CompanyUserMapper companyUserMapper;
    @Resource
    private CompanyUserLogMapper companyUserLogMapper;


    public InvitationService() {
        jedisClient = new Jedis("172.16.54.144", 6379);
        jedisClient.select(5);
    }

    public String inviteAdmin(String companyUserId, String companyId, int inviteCount) {
        if (inviteCount <= 0 || inviteCount > 5) {
            inviteCount = 5;
        }
        String token = UUID.randomUUID().toString();
        Map map = new HashMap<>();
        map.put("userId", companyUserId);
        map.put("companyId", companyId);
        map.put("inviteCount", inviteCount);
        map.put("applyCount", 0);
        map.put("status", 1);
        map.put("invateTime", System.currentTimeMillis());
        map.put("token", token);
        jedisClient.hmset(token, map);
        jedisClient.expire(token, TOKEN_EXPIRE_TIME);
        return token;
    }

    public int applyInvitation(String token, String userId, String applyInfo) {
        Map<String, String> map = jedisClient.hgetAll(token);
        if (map == null || map.isEmpty()) {
//            throw new Exception("can not find the token");
            return 5;
        }

        String status = map.get("status");
        if ("2".equals(status)) {
//            throw new Exception("the invitation is finish!");
            return 2;
        }
        long inviteCount = Long.parseLong(map.get("inviteCount"));
        long applyCount = Long.parseLong(map.get("applyCount"));

        if (applyCount >= inviteCount) {
//            throw new Exception("the invitation queque is full!");
            return 3;
        }

        if (map.get(userId) != null) {
//          throw new Exception("the user:"+userId+" already applied.");
            return 4;
        }

        applyCount = jedisClient.hincrBy(token, "applyCount", 1);
        if (applyCount > inviteCount) {
            jedisClient.hincrBy(token, "applyCount", -1);
//            throw new Exception("the invitation queque is full!");
            return 3;
        }

        String companyUserId = map.get("userId");
        String companyId = map.get("companyId");
        long inviteTime = Long.parseLong(map.get("inviteTime"));
        saveApply(companyUserId, companyId, userId, applyInfo, token, inviteTime);
        return 1;

    }

    public boolean pass(String companyUserId, String invitationId) {
        Invitation invitation = invitationMapper.selectByPrimaryKey(invitationId);
        if (invitation == null) {
            return false;
        }
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyId(invitation.getInviteCompanyId());
        companyUser.setId(idGenerator.generate(CompanyUser.class.getSimpleName()));
        companyUser.setVersion(1);
        companyUser.setStatus(1);
        companyUserMapper.insert(companyUser);
        log(companyUser,companyUserId);
        update(invitationId, STATUS_PASS);
        return true;
    }

    private void log(CompanyUser companyUser, String companyUserId) {
        CompanyUserLog companyUserLog = new CompanyUserLog();
        companyUserLog.setId(idGenerator.generate(CompanyUserLog.class.getSimpleName()));
        companyUserLog.setStatus(companyUser.getStatus());
        companyUserLog.setCreateTime(System.currentTimeMillis());
        companyUserLog.setUserId(companyUser.getUserId());
        companyUserLog.setModifyUser(companyUserId);
        companyUserLog.setVersion(companyUser.getVersion());
        companyUserLogMapper.insertSelective(companyUserLog);
    }

    public boolean reject(String companyUserId, String invitationId) {
        Invitation invitation = invitationMapper.selectByPrimaryKey(invitationId);
        if (invitation == null) {
            return false;
        }
        return update(invitationId, STATUS_REJECT);
    }

    private boolean update(String invitationId, int status) {

        Invitation updateInvitation = new Invitation();
        updateInvitation.setId(invitationId);
        updateInvitation.setStatus(status);
        updateInvitation.setComfirmTime(System.currentTimeMillis());
        invitationMapper.updateByPrimaryKeySelective(updateInvitation);
        return true;
    }

    public List<Invitation> getNewApplyInvitation(String companyId, String userId) {
        return invitationMapper.selectByUserId(userId, companyId, STATUS_INIT);
    }

    public List<Invitation> getPassApplyInvitation(String companyId, String userId) {
        return invitationMapper.selectByUserId(userId, companyId, STATUS_PASS);
    }

    public List<Invitation> getRejectApplyInvitation(String companyId, String userId) {
        return invitationMapper.selectByUserId(userId, companyId, STATUS_REJECT);
    }


    public void saveApply(String companyUserId, String companyId, String applyUserId, String applyInfo, String token, Long inviteTime) {
        Invitation invitation = new Invitation();
        invitation.setId(idGenerator.generate(Invitation.class.getSimpleName()));
        invitation.setInviteCompanyId(companyUserId);
        invitation.setInviteCompanyId(companyId);
        invitation.setApplyInfo(applyInfo);
        invitation.setApplyUserId(applyUserId);
        invitation.setToken(token);
        invitation.setInviteTime(inviteTime);
        invitation.setApplyTime(System.currentTimeMillis());
        invitation.setStatus(1);
        invitationMapper.insert(invitation);
    }
}
