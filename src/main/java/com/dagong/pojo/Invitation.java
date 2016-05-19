package com.dagong.pojo;

public class Invitation {
    private String id;

    private String inviteUserId;

    private String inviteCompanyId;

    private String applyUserId;

    private String applyInfo;

    private int status;

    private String token;

    private Long inviteTime;

    private Long applyTime;

    private Long comfirmTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId == null ? null : inviteUserId.trim();
    }

    public String getInviteCompanyId() {
        return inviteCompanyId;
    }

    public void setInviteCompanyId(String inviteCompanyId) {
        this.inviteCompanyId = inviteCompanyId == null ? null : inviteCompanyId.trim();
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId == null ? null : applyUserId.trim();
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo == null ? null : applyInfo.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Long getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Long inviteTime) {
        this.inviteTime = inviteTime;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Long getComfirmTime() {
        return comfirmTime;
    }

    public void setComfirmTime(Long comfirmTime) {
        this.comfirmTime = comfirmTime;
    }
}