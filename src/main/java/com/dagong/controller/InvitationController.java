package com.dagong.controller;

import com.dagong.mapper.CompanyUserMapper;
import com.dagong.service.InvitationService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/4/25.
 */
@RestController
public class InvitationController {
    @Resource
    private InvitationService invitationService;


    public String inviteAdmin(@CookieValue("userId") String userId,
                              @CookieValue("companyId") String companyId, @RequestParam("inviteCount") int inviteCount) {
        String token = invitationService.inviteAdmin(userId, companyId, inviteCount);
        return token;
    }

    public String applyInvitation(@CookieValue("userId") String userId, @RequestParam("token") String token,
                                  @RequestParam("applyInfo") String applyInfo) {
        int result = invitationService.applyInvitation(token, userId, applyInfo);
        if (result == 1) {
            return "ok";
        } else {
            return "fail";
        }
    }




    public String pass(@CookieValue("userId") String userId, @RequestParam("invitationId") String invitationId) {
        if (invitationService.pass(userId, invitationId)) {
            return "ok";
        } else {
            return "fail";
        }

    }

    public String reject(@CookieValue("userId") String userId, @RequestParam("invitationId") String invitationId) {
        if (invitationService.reject(userId, invitationId)) {
            return "ok";
        } else {
            return "fail";
        }

    }


}
