package com.dagong.controller;

import com.dagong.service.ApplyService;
import com.dagong.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/28.
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Resource
    private ApplyService applyService;
    @Resource
    private UserService userService;



    @RequestMapping("/listApplyForNew.do")
    public String showApplyForNew(@CookieValue("userId") String userId,
                                  @RequestParam("jobId") String jobId,
                                  @RequestParam("page") int page){
        return null;
    }

    @RequestMapping("/listApplyForRead.do")
    public String showApplyForRead(@CookieValue("userId") String userId,
                                   @RequestParam("jobId") String jobId,
                                   @RequestParam("page") int page){
        return null;
    }


    @RequestMapping("/showResume.do")
    public String showResume(@CookieValue("userId") String userId,
                             @RequestParam("applyId") String applyId){
        return null;
    }




}
