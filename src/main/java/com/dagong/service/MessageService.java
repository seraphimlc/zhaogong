package com.dagong.service;

import com.dagong.pojo.InviteRecord;
import com.dagong.pojo.Job;
import org.apache.commons.collections.map.HashedMap;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by liuchang on 16/5/11.
 */
public class MessageService {
    @Resource
    private JobService jobService;
    @Resource
    private InviteService inviteService;

    private static Jedis jedisClient = new Jedis("172.16.54.144", 6379);

    public MessageService() {
        jedisClient.select(3);
    }

    public void increaseApplyMessage(String userId, String jobId) {
        jedisClient.hincrBy(userId + "_job", jobId, 1);
    }

    public void increaseInviteMessage(String userId, String inviteId) {
        jedisClient.hincrBy(userId + "_invite", inviteId, 1);

    }

    public void decreaseApplyMessage(String userId, String jobId) {
        long result = jedisClient.hincrBy(userId + "_job", jobId, -1);
        if (result < 0) {
            jedisClient.hincrBy(userId + "_job", jobId, 1);
        }
    }

    public void decreaseInviteMessage(String userId, String inviteId) {
        long result = jedisClient.hincrBy(userId + "_invite", inviteId, -1);
        if (result < 0) {
            jedisClient.hincrBy(userId + "_invite", inviteId, 1);
        }
    }

    public Map getMessageList(String userId) {
        Map<String, String> jobMessageMap = jedisClient.hgetAll(userId + "_job");
        Map<String, String> inviteMessageMap = jedisClient.hgetAll(userId + "_invite");
        Map<String, Map> messageMap = new HashedMap();
        Map<Job, Long> jobMap = new HashedMap();
        Map<InviteRecord,Long> inviteMap = new HashedMap();
        jobMessageMap.forEach((key, value) -> {
            Job job = jobService.getJob(key);
            jobMap.put(job, Long.parseLong(value));
        });

        inviteMessageMap.forEach((key,value)->{
            InviteRecord inviteRecord=inviteService.getInviteRecord(key);
            inviteMap.put(inviteRecord,Long.parseLong(value));
        });
        messageMap.put("job",jobMap);
        messageMap.put("invite",inviteMap);
        return messageMap;

    }


}
