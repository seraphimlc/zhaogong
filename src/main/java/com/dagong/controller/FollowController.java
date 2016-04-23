package com.dagong.controller;

import com.dagong.pojo.FollowJob;
import com.dagong.service.FollowService;
import com.dagong.service.UserService;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/28.
 */
@RestController
//@RequestMapping("/follow")
public class FollowController {
    private static final int PAGE_SIZE=10;
    @Resource
    private FollowService followService;
    @Resource
    private UserService userService;


    @RequestMapping("/followJob.do")
    @ResponseBody
    public String followJob(@CookieValue("user") String user,@RequestParam("jobIds") String[] jobIds) {
        String userId = userService.getUserIdFromCookie(user);
        followService.followJob(userId, jobIds);
        return "true";
    }

    @RequestMapping("/followCompany.do")
    @ResponseBody
    public String followCompany(@CookieValue("user") String user,@RequestParam("companyId") String companyId) {
        String userId = userService.getUserIdFromCookie(user);
        followService.followCompany(userId, companyId);
        return "true";
    }

    @RequestMapping("/getFollowJob.do")
    public Page getFollowJob(@CookieValue("user") String user,@RequestParam(name="page",defaultValue = "1")int page){
        String userId = userService.getUserIdFromCookie(user);
        Page<FollowJob> followJobForUser = followService.getFollowJobForUser(userId, page, PAGE_SIZE);
        return followJobForUser;
    }



}
