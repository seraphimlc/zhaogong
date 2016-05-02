package com.dagong.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dagong.job.JobClient;
import org.springframework.stereotype.Service;

/**
 * Created by liuchang on 16/1/17.
 */
@Service

public class JobService {

    @Reference(version = "1.0.0")
    private JobClient jobClient;

    public JobClient getJobClient() {
        return jobClient;
    }

    public void setJobClient(JobClient jobClient) {
        this.jobClient = jobClient;
    }
}
