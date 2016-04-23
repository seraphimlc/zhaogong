package com.dagong.service;

import com.dagong.mapper.CompanyUserMapper;
import com.dagong.mapper.ContactMapper;
import com.dagong.mapper.InviteLogMapper;
import com.dagong.mapper.InviteRecordMapper;
import com.dagong.pojo.CompanyUser;
import com.dagong.pojo.Contact;
import com.dagong.pojo.InviteLog;
import com.dagong.pojo.InviteRecord;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class InviteService {


    private static int STATUS_INIT = 1;
    private static int STATUS_READ = 2;
    private static int STATUS_ACCEPT = 3;
    private static int STATUS_REFUSE = 4;
    private static int STATUS_INTERVIEW = 5;
    private static int STATUS_SUCCESS = 6;
    private static int STATUS_FAILED = 7;

    @Resource
    private InviteRecordMapper inviteRecordMapper;

    @Resource
    private InviteLogMapper inviteLogMapper;

    @Resource
    private CompanyUserMapper companyUserMapper;

    @Resource
    private ContactMapper contactMapper;

    @Resource
    private IdGenerator idGenerator;

    public boolean invite(String companyUserId, String userId, String jobId, String contactId, String comment, Date interviewTime) {
        CompanyUser companyUser = companyUserMapper.selectByPrimaryKey(companyUserId);
        if (companyUser == null) {
            return false;
        }
        Contact contact = contactMapper.selectByPrimaryKey(contactId);
        if (contact == null) {
            return false;
        }
        if (!companyUser.getCompanyId().equals(contact.getCompanyId())) {
            return false;
        }
        InviteRecord inviteRecord = createInviteRecord(userId, jobId, contactId, comment, interviewTime, companyUserId);
        InviteLog inviteLog = createInviteLog(companyUserId, companyUser.getUsername(), inviteRecord.getId(), STATUS_INIT, STATUS_INIT);
        inviteRecordMapper.insert(inviteRecord);
        inviteLogMapper.insert(inviteLog);
        return true;
    }

    public boolean read(String userId, String inviteId) {
        updateStatus(userId, null, inviteId, STATUS_READ);
        return true;
    }

    public boolean accept(String userId, String inviteId) {
        updateStatus(userId, null, inviteId, STATUS_ACCEPT);
        return true;
    }
    public boolean refuse(String userId,String invitedId){
        updateStatus(userId,null,invitedId,STATUS_REFUSE);
        return true;
    }

    public boolean interview(String userId,String invitedId){
        updateStatus(userId,null,invitedId,STATUS_INTERVIEW);
        return true;
    }

    public boolean success(String modifyUserId,String inviteId){
        updateStatus(modifyUserId,null,inviteId,STATUS_SUCCESS);
        return true;
    }
    public boolean fail(String modifyUserId,String inviteId){
        updateStatus(modifyUserId,null,inviteId,STATUS_FAILED);
        return true;
    }

    public void updateStatus(String modifyUserId, String modifyUserName, String inviteId, int status) {
        InviteRecord inviteRecord = inviteRecordMapper.selectByPrimaryKey(inviteId);
        InviteRecord newInviteRecord = new InviteRecord();
        newInviteRecord.setStatus(status);
        newInviteRecord.setId(inviteId);
        InviteLog inviteLog = createInviteLog(modifyUserId, modifyUserName, inviteId, inviteRecord.getStatus(), status);
        inviteRecordMapper.updateByPrimaryKeySelective(newInviteRecord);
        inviteLogMapper.insert(inviteLog);
    }

    private InviteLog createInviteLog(String modifyUserId, String modifyUserName, String inviteId, int oldStatus, int newStatus) {
        InviteLog inviteLog = new InviteLog();
        inviteLog.setInviteId(inviteId);
        inviteLog.setId(idGenerator.generate(InviteLog.class.getSimpleName()));
        inviteLog.setLogTime(new Date());
        inviteLog.setModifyUser(modifyUserId);
        inviteLog.setModifyUsername(modifyUserName);
        inviteLog.setOldStatus(oldStatus);
        inviteLog.setNewStatus(newStatus);
        return inviteLog;
    }

    private InviteRecord createInviteRecord(String userId, String jobId, String contactId, String comment, Date interviewTime, String companyUserId) {
        InviteRecord inviteRecord = new InviteRecord();
        inviteRecord.setComment(comment);
        inviteRecord.setUserId(userId);
        inviteRecord.setContactId(contactId);
        inviteRecord.setInterviewTime(interviewTime);
        inviteRecord.setJobId(jobId);
        inviteRecord.setCompanyId(companyUserId);
        inviteRecord.setId(idGenerator.generate(InviteRecord.class.getSimpleName()));
        inviteRecord.setStatus(STATUS_INIT);
        return inviteRecord;
    }


}
