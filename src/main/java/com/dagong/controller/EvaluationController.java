package com.dagong.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dagong.evaluation.EvaluationClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by liuchang on 16/5/21.
 */
@RestController
public class EvaluationController {

    private static int DEFAULT_PAGE_SIZE=10;

    @Reference(version = "1.0.0")
    private EvaluationClient evaluationClient;
    @RequestMapping("/evaluateUser")
    public String evaluateUser(@CookieValue("userId") String userId,
                               @CookieValue("companyId") String companyId,
                               @RequestParam("userId") String evaedUserId,
                               @RequestParam("comment") String comment,
                               @RequestParam("rank") int rank) {
        evaluationClient.evaluateUser(userId,companyId,evaedUserId,rank,comment);
        return "ok";
    }


    public String getMyEvaluation(@CookieValue("userId") String userId,
                               @CookieValue("companyId") String companyId,
                                @RequestParam(value = "page",defaultValue = "1")int page) {
        evaluationClient.getUserEvaluationForCompany(companyId,page,DEFAULT_PAGE_SIZE);
        return "ok";
    }

    public String getUserEvaluation(@CookieValue("userId") String userId,
                                    @CookieValue("companyId") String companyId,
                                    @RequestParam("userId") String evaedUserId,
                                    @RequestParam(value = "page",defaultValue = "1")int page){
        List<Map> userEvaluations = evaluationClient.getUserEvaluation(evaedUserId, page, DEFAULT_PAGE_SIZE);
        return JSON.toJSONString(userEvaluations);
    }
    public String getJobEvaluation(@CookieValue("userId") String userId,
                                    @CookieValue("companyId") String companyId,
                                    @RequestParam("jobId") String jobId,
                                    @RequestParam(value = "page",defaultValue = "1")int page){
        List<Map> userEvaluations = evaluationClient.getJobEvaluation(jobId, page, DEFAULT_PAGE_SIZE);
        return JSON.toJSONString(userEvaluations);
    }
    public String getCompanyEvaluation(@CookieValue("userId") String userId,
                                    @CookieValue("companyId") String companyId,
                                    @RequestParam("userId") String evaedUserId,
                                    @RequestParam(value = "page",defaultValue = "1")int page){
        List<Map> userEvaluations = evaluationClient.getCompanyEvaluation(companyId, page, DEFAULT_PAGE_SIZE);
        return JSON.toJSONString(userEvaluations);
    }


}
