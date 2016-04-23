package com.dagong.service;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dagong.mapper.*;
import com.dagong.pojo.*;
import com.dagong.util.BeanValidator;
import com.dagong.util.IdGenerator;
import com.dagong.util.ListUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuchang on 16/1/17.
 */
@Service

public class UserService {
    private static int MAX_WANT_JOB = 5;
    private static int MAX_WANT_ENVIRONMENT = 5;
    private static int VALIDATE_CODE_LENGTH = 4;
    private static int VALIDATE_CODE_EXPIRED_TIME = 1000 * 60;
    private static Jedis jedisClient = new Jedis("172.16.54.144", 6379);
    private static String MESSAGE_TOPIC="user";

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserExperienceMapper userExperienceMapper;

    @Resource
    private UserSkillMapper userSkillMapper;

    @Resource
    private WantJobMapper wantJobMapper;

    @Resource
    private WantEnvironmentMapper wantEnvironmentMapper;

    @Resource
    private WantInformationMapper wantInformationMapper;

//    @Resource(name = "userMessageSender")
//    private SendMessageService sendMessageService;

    @Resource
    private IdGenerator idGenerator;

    public User register(User user) {
        user.setId(idGenerator.generate(User.class.getSimpleName()));

        BeanValidator.validate(user);
        userMapper.insert(user);
        return user;
    }

    public User getUserByUserName(String userName) {
        return null;
    }

    public boolean saveUserSkill(String userId, String skill) {
        UserSkill userSkill = userSkillMapper.selectByUserId(userId);
        if (userSkill == null) {
            userSkill = new UserSkill();
            userSkill.setId(idGenerator.generate(UserSkill.class.getSimpleName()));
            userSkill.setUserId(userId);
            userSkill.setSkill(skill);
            userSkillMapper.insert(userSkill);
        } else {
            userSkill.setSkill(skill);
            userSkillMapper.updateByPrimaryKey(userSkill);
        }
        return true;
    }

    public boolean saveUserExperience(String userId, String experience) {
        UserExperience userExperience = userExperienceMapper.selectByUserId(userId);
        if (userExperience == null) {
            userExperience = new UserExperience();
            userExperience.setId(idGenerator.generate(UserExperience.class.getSimpleName()));
            userExperience.setUserId(userId);
            userExperience.setExperience(experience);
            userExperienceMapper.insertSelective(userExperience);
        } else {
            userExperience.setExperience(experience);
            userExperienceMapper.updateByPrimaryKey(userExperience);
        }
        return true;
    }

    public boolean addWantJob(String userId, int[] jobIds) {
        List<WantJob> wantJobs = wantJobMapper.selectByUserId(userId);
        int existsSize = ListUtil.isEmpty(wantJobs) ? 0 : wantJobs.size();
        for (int i = 0; i < Math.min(MAX_WANT_JOB - existsSize, jobIds.length); i++) {
            WantJob wantJob = new WantJob();
            wantJob.setId(idGenerator.generate(WantJob.class.getSimpleName()));
            wantJob.setUserId(userId);
            wantJob.setJobType(jobIds[i]);
            wantJobMapper.insertSelective(wantJob);
        }
        return true;
    }

    public boolean clearWantJob(String userId) {
        wantJobMapper.deleteByUserId(userId);
        return true;
    }

    public boolean addWantEnvironment(String userId, int[] envIds) {
        List<WantEnvironment> wantEnvironments = wantEnvironmentMapper.selectByUserId(userId);
        int existsSize = ListUtil.isEmpty(wantEnvironments) ? 0 : wantEnvironments.size();
        for (int i = 0; i < Math.min(MAX_WANT_ENVIRONMENT - existsSize, envIds.length); i++) {
            WantEnvironment wantEnvironment = new WantEnvironment();
            wantEnvironment.setId(idGenerator.generate(WantEnvironment.class.getSimpleName()));
            wantEnvironment.setUserId(userId);
            wantEnvironment.setEnvId(envIds[i]);
            wantEnvironmentMapper.insertSelective(wantEnvironment);
//            WantJob wantJob = new WantJob();
//            wantJob.setId(idGenerator.generate(WantJob.class.getSimpleName()));
//            wantJob.setUserId(userId);
//            wantJob.setJobType(jobIds[i]);
//            wantJobMapper.insertSelective(wantJob);
        }
        return true;
    }

    public boolean clearWantJobs(String userId) {
        wantEnvironmentMapper.deleteByUserId(userId);
        return true;
    }

    public boolean saveWantInformation(String userId, UserInformationKey userInformationKey, String value) {
        WantInformation wantInformation = wantInformationMapper.selectByUserIdAndKey(userId, userInformationKey.getName());
        if (wantInformation == null) {
            wantInformation = new WantInformation();
            wantInformation.setId(idGenerator.generate(WantInformation.class.getSimpleName()));
            wantInformation.setUserId(userId);
            wantInformation.setKey(userInformationKey.getName());
            wantInformation.setValue(value);
            wantInformationMapper.insert(wantInformation);
        } else {
            wantInformation.setValue(value);
            wantInformationMapper.updateByPrimaryKeySelective(wantInformation);
        }
        return true;
    }

    public boolean validateCode(String phoneNumber, String validateCode) {
        String cacheCode = jedisClient.get(phoneNumber);
        if (StringUtils.isNotBlank(cacheCode)) {
            jedisClient.del(phoneNumber);
            if (validateCode.equals(cacheCode)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String generateCookieForUser(String userId) {
        String cookieValue = idGenerator.generateRandom("user");
        jedisClient.setex(cookieValue, 24 * 60 * 60, userId);
        return cookieValue;
    }

    public String getUserIdFromCookie(String cookieValue) {
        return jedisClient.get(cookieValue);
    }

    public boolean sendValidatePhoneNumberCode(String phoneNumber) {
        String validateCode = jedisClient.get(phoneNumber);
        if (StringUtils.isNotEmpty(validateCode)) {
            return true;

        }
        validateCode = IdGenerator.generateValidateCode(VALIDATE_CODE_LENGTH);
        jedisClient.setex(phoneNumber, VALIDATE_CODE_EXPIRED_TIME, validateCode);
        try {
            sendMessage(phoneNumber, getMessageFromTemplate(validateCode));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void sendMessage(String phoneNumber, String message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        HashMap<String, String> msg = new HashMap<>();
        msg.put(phoneNumber, message);
//        sendMessageService.sendMessage(MESSAGE_TOPIC,"validateCode", msg);
    }

    private String getMessageFromTemplate(String message) {
        return "验证码:[" + message + "];打工宝.";
    }


}
