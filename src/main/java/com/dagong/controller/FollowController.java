package com.dagong.controller;

import com.dagong.service.FollowService;
import com.dagong.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/28.
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    private static final int PAGE_SIZE = 10;
    @Resource
    private FollowService followService;
    @Resource
    private UserService userService;


    @RequestMapping("/followUser.do")
    public String followJob(@CookieValue("userId") String userId,
                            @RequestParam("userId") String employeeId) {
        System.out.println("userId = " + userId);
        System.out.println("employeeId = " + employeeId);
        return "true";
    }

    @RequestMapping("/invitUser.do")
    public String invitUser(@CookieValue("userId") String userId,
                            @RequestParam("jobId") String jobId,
                            @RequestParam("userId") String employeeId) {
        return "true";
    }

    @RequestMapping("/myFollowUser.do")
    public String list(@CookieValue("userId") String userId,
                                  @RequestParam("keyword") String keyword,
                                  @RequestParam("page") int page
                                  ){
        return null;
    }

    @RequestMapping("/followMe.do")
    public String listFollowMe(@CookieValue("userId") String userId,
                               @RequestParam("keyword") String keyword,
                               @RequestParam("page") int page){
        return null;
    }

    @RequestMapping("/relieveFollowUser.do")
    public String relieve(@CookieValue("userId") String userId,
                          @RequestParam("followId") String followId
                          ){
        return null;
    }

}
