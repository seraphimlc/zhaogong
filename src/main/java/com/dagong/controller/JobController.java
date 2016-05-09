package com.dagong.controller;

import com.dagong.service.ApplyService;
import com.dagong.service.JobService;
import com.dagong.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by liuchang on 16/1/28.
 */
@Controller
@RequestMapping("/job/")
public class JobController {
    @Resource
    private JobService jobService;
    @Resource
    private ApplyService applyService;
    @Resource
    private UserService userService;

    @RequestMapping("create.do")
    public String createJob(@CookieValue("userId") String userId,
                            @RequestParam("jobName") String jobName,
                            @RequestParam("jobType") int jobType,
                            @RequestParam("salary") String salary,
                            @RequestParam("needNumber") int needNumber,
                            @RequestParam("degree") int degree,
                            @RequestParam("workyear") int workyear,
                            @RequestParam int[] environment,
                            @RequestParam("address") String address,
                            @RequestParam("contactor") String contactor,
                            @RequestParam("contact") String contact,
                            Map<String, Object> model) {

        return "/view/searchJobList";
    }

    @RequestMapping("addDetail.do")
    public String addDetail(@CookieValue("userId") String userId,
                            @RequestParam("jobId") String jobId,
                            @RequestParam("detail") String detail) {
        return null;
    }

    @RequestMapping("deploy.do")
    public String deploy(@CookieValue("userId") String userId,
                         @RequestParam("jobId") String jobId) {
        return null;
    }

    @RequestMapping("delay.do")
    public String delay(@CookieValue("userId") String userId,
                        @RequestParam("jobId") String jobId) {
        return null;
    }

    @RequestMapping("stop.do")
    public String stop(@CookieValue("userId") String userId,
                       @RequestParam("jobId") String jobId) {
        return null;
    }


    @RequestMapping("detail.do")
    public String detail(@CookieValue("userId") String userId,
                         @RequestParam("jobId") String jobId,
                         Map<String, Object> model) {
//        Map job = jobService.getJob(jobId);
//        if (job != null) {
//            model.put("job", job);
//
//        }
        return "/view/jobDetail";
    }

    @RequestMapping("list.do")
    public String list(@CookieValue("userId") String userId,
                       @CookieValue("companyId") String companyId,
                       @RequestParam("keyword") String keyword){
        return null;
    }


}
