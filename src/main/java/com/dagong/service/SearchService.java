package com.dagong.service;

import com.alibaba.fastjson.JSON;
import com.dagong.pojo.Job;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchang on 16/4/5.
 */
@Service
public class SearchService {

    private TransportClient transportClient = null;

    public SearchService(){
        try {
            transportClient = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.54.144"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public List<Map> searchJobByType(int jobType){
        List<Map> jobs = new ArrayList<>();
        SearchResponse searchResponse = transportClient.prepareSearch("test").setTypes("job")
                .setQuery(QueryBuilders.termQuery("jobType", jobType)).execute().actionGet();
        System.out.println("searchResponse = " + searchResponse);
        for (SearchHit searchHitFields : searchResponse.getHits().getHits()) {
            jobs.add(searchHitFields.getSource());
        }
        return jobs;
    }


    public Map getJob(String jobId){
        GetResponse response = transportClient.prepareGet("test", "job", jobId).execute().actionGet();
        if(response.isExists()){
            return response.getSource();
        }
        return null;
    }

    public boolean addUserFollowJobToIndex(){
        return true;
    }

    public boolean addJobToIndex(List<Job> jobList){
        if(jobList==null||jobList.isEmpty()){
            return false;
        }

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
        jobList.forEach(job -> {
            bulkRequestBuilder.add(transportClient.prepareIndex("test", "job", job.getId()).setSource(JSON.toJSONString(job)));
        });
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        return true;
    }
}
