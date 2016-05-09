package com.dagong.controller;

import com.alibaba.fastjson.JSON;
import com.dagong.pojo.ApplyRecord;
import com.dagong.service.ApplyService;
import com.dagong.service.JobService;
import com.dagong.service.UserService;
import com.dagong.user.vo.UserVO;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/28.
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {


    @Resource
    private UserService userService;

    @Resource
    private ApplyService applyService;

    @Resource
    private JobService jobService;

    private static String OK="ok";
    private static String FAIL="fail";


    @RequestMapping("/listApplyForNew.do")
    public String showApplyForNew(@CookieValue("userId") String userId,
                                  @CookieValue("companyId") String companyId,
                                  @RequestParam("jobId") String jobId,
                                  @RequestParam("page") int page) {

        return JSON.toJSONString(applyService.getApplyRecordForNew(userId, jobId, page));

    }

    @RequestMapping("/listApplyForRead.do")
    public String showApplyForRead(@CookieValue("userId") String userId,
                                   @CookieValue("companyId") String companyId,
                                   @RequestParam("jobId") String jobId,
                                   @RequestParam("page") int page) {
        return JSON.toJSONString(applyService.getApplyRecordForRead(userId, jobId, page));

    }


    @RequestMapping("/showResume.do")
    public String showResume(@CookieValue("userId") String userId,
                             @CookieValue("companyId") String companyId,
                             @RequestParam("applyId") String applyId) {
        ApplyRecord applyRecord = applyService.getApplyById(applyId);
        if (applyRecord != null) {
            UserVO userVO = userService.getUserById(applyRecord.getUserId());
            if (userVO != null) {

                return JSON.toJSONString(userVO);
            }
        }
        return null;
    }


    @RequestMapping("/pass.do")
    public String pass(@CookieValue("userId") String userId,
                       @CookieValue("companyId") String companyId,
                       @RequestParam("applyId") String applyId) {
        if(applyService.pass(userId, companyId, applyId)){
            return FAIL;
        }

        return OK;
    }

    @RequestMapping("/refuse.do")
    public String refuse(@CookieValue("userId") String userId,
                         @CookieValue("companyId") String companyId,
                         @RequestParam("applyId") String applyId) {
        if(applyService.refuse(userId, companyId, applyId)){
            return FAIL;
        }

        return OK;
    }

    @RequestMapping("/interview.do")
    public String interview(@CookieValue("userId") String userId,
                            @CookieValue("companyId") String companyId,
                            @RequestParam("applyId") String applyId) {
        if(applyService.interview(userId, companyId, applyId)){
            return FAIL;
        }

        return OK;
    }


}
