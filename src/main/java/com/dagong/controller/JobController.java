package com.dagong.controller;

import com.dagong.pojo.Job;
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
                            @RequestParam("startSalary") int startSalary,
                            @RequestParam("endSalary") int endSalary,
                            @RequestParam("needNumber") int needNumber,
                            @RequestParam("degree") int degree,
                            @RequestParam int[] environment,
                            @RequestParam("address") String address,
                            @RequestParam("contactor") String contactor,
                            @RequestParam("royalty")  int royalty,
                            @RequestParam("startTime") long startTime,
                            @RequestParam("endTime") long endTime,
                            @RequestParam("phoneNumber") String phoneNumber,
                            @RequestParam("workedYear") String workedYear,
                            @RequestParam("welfare") String welfare,
                            @RequestParam("discuss") int discuss,
                            @RequestParam("bonus") int bonus,
                            Map<String, Object> model) {

        jobService.createJob(userId,jobName,needNumber,jobType,null,startSalary,endSalary,royalty,
                bonus,discuss,address,welfare,workedYear,degree,
                phoneNumber,contactor,
                startTime,endTime,false);

        return "/view/searchJobList";
    }

    @RequestMapping("addDetail.do")
    public String addDetail(@CookieValue("userId") String userId,
                            @RequestParam("jobId") String jobId,
                            @RequestParam("detail") String detail,
                            @RequestParam("version") int version) {
        jobService.addDetail(jobId,userId,detail,version);
        return null;
    }


    @RequestMapping("modify.do")
    public String modifyJob(@CookieValue("userId") String userId,
                            @RequestParam("jobId") String jobId,
                            @RequestParam("jobName") String jobName,
                            @RequestParam("jobType") int jobType,
                            @RequestParam("startSalary") int startSalary,
                            @RequestParam("endSalary") int endSalary,
                            @RequestParam("needNumber") int needNumber,
                            @RequestParam("degree") int degree,
                            @RequestParam int[] environment,
                            @RequestParam("address") String address,
                            @RequestParam("contactor") String contactor,
                            @RequestParam("royalty")  int royalty,
                            @RequestParam("startTime") long startTime,
                            @RequestParam("endTime") long endTime,
                            @RequestParam("phoneNumber") String phoneNumber,
                            @RequestParam("workedYear") String workedYear,
                            @RequestParam("welfare") String welfare,
                            @RequestParam("discuss") int discuss,
                            @RequestParam("bonus") int bonus,
                            @RequestParam("version") int version,
                            Map<String, Object> model) {

        jobService.modifyJob(jobId,userId,jobName,needNumber,jobType,null,startSalary,endSalary,royalty,bonus,discuss,address,welfare,workedYear,degree,phoneNumber,contactor,startTime,endTime,version);

        return "/view/searchJobList";
    }

    @RequestMapping("modifyDetail.do")
    public String modifyDetail(@CookieValue("userId") String userId,
                            @RequestParam("jobId") String jobId,
                            @RequestParam("detail") String detail,@RequestParam("version")int version) {
        jobService.addDetail(jobId,userId,detail,version);
        return null;
    }




    @RequestMapping("deploy.do")
    public String deploy(@CookieValue("userId") String userId,
                         @RequestParam("jobId") String jobId) {
        jobService.deployJob(userId, jobId);
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
        jobService.stopJob(userId, jobId);
        return null;
    }


    @RequestMapping("getJob.do")
    public String getJob(@CookieValue("userId") String userId,
                         @RequestParam("jobId") String jobId,
                         Map<String, Object> model) {
        Job job = jobService.getJob(jobId);
        if (job != null) {
            model.put("job", job);

        }
        return "/view/jobDetail";
    }
    @RequestMapping("detail.do")
    public String detail(@CookieValue("userId") String userId,
                         @RequestParam("jobId") String jobId,
                         Map<String, Object> model) {
        Job job = jobService.getJobDetail(jobId);
        if (job != null) {
            model.put("job", job);

        }
        return "/view/jobDetail";
    }

    @RequestMapping("list.do")
    public String list(@CookieValue("userId") String userId,
                       @CookieValue("companyId") String companyId,
                       @RequestParam("status") int status){



        return null;
    }


}
