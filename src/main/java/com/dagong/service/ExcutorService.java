package com.dagong.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuchang on 16/4/24.
 */
@Service
public class ExcutorService {

    private ExecutorService executorService;

    public ExcutorService() {
        executorService = Executors.newFixedThreadPool(20);
    }

    public void excute(Runnable runnable){
        executorService.execute(runnable);
    }

    public void excute(List<Runnable> list){
        list.forEach(runnable -> {
            excute(runnable);
        });
    }
}
