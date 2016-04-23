package com.dagong.controller;

import com.dagong.mapper.JobTypeMapper;
import com.dagong.pojo.User;
import com.dagong.service.FollowService;
import com.dagong.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuchang on 16/3/3.
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private FollowService followService;


    @Resource
    private JobTypeMapper jobTypeMapper;

    private static String REGEX = "^1[3|4|5|7|8]\\d{9}$";

    public String initFromWeChat() {
        return null;
    }

    @RequestMapping("/selectWantEnvironment.do")
    public String selectWantEnvironment(@CookieValue("user") String user, @RequestParam int[] environment) {
        String userId = userService.getUserIdFromCookie(user);
        userService.addWantEnvironment(userId, environment);
        return "ok";
    }

    @RequestMapping("/saveExperience.do")
    public String saveExperience(@CookieValue("user") String user, @RequestParam String experience, @RequestParam String specialSkill) {
        String userId = userService.getUserIdFromCookie(user);
        userService.saveUserExperience(userId, experience);
        userService.saveUserSkill(userId, specialSkill);
        return "ok";
    }

    @RequestMapping("/userProfile.do")
    public String profile(@CookieValue("user") String user) {
        String userId = userService.getUserIdFromCookie(user);
        System.out.println("userId = " + userId);
        return "/view/profile";
    }


    @RequestMapping("/selectJobType.do")
    public String selectJobType(@CookieValue("user") String user, @RequestParam int[] jobTypes) {
        String userId = userService.getUserIdFromCookie(user);
        if (StringUtils.isBlank(user)) {
            return "noUser";
        }
        userService.addWantJob(user, jobTypes);
        return "ok";
    }

    @RequestMapping("/register.do")
    public String register(@RequestParam String name,
                           @RequestParam Integer gender,
                           @RequestParam String birthday,
                           @RequestParam String phoneNumber,
                           @RequestParam String cardId,
                           @RequestParam String validateCode) {
        User user = new User();
        user.setName(name);
        user.setGender(gender);
        user.setBrithday(convertStringToDate(birthday));
        user.setCardId(cardId);
        user.setPhone(phoneNumber);
        if (!userService.validateCode(phoneNumber, validateCode)) {
            return "failValidateCode";
        }
        userService.register(user);
        return user.getId();
    }

    private static Date convertStringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @RequestMapping("/sendValidateCode.do")
    public String sendValidateCode(@RequestParam String phoneNumber) {
        if (!validatePhoneNumber(phoneNumber)) {
            return "fail";
        }
        userService.sendValidatePhoneNumberCode(phoneNumber);
        return "ok";
    }


    @RequestMapping("myFollowJob.do")
    public String followJob(@CookieValue("user") String user, @RequestParam(value = "page", defaultValue = "1") int page) {
        String userId = userService.getUserIdFromCookie(user);

        return null;
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches(REGEX);
    }


}
