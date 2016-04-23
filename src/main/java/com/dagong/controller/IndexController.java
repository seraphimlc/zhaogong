package com.dagong.controller;

import com.dagong.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuchang on 16/3/16.
 */
@Controller
public class IndexController {
    @Resource
    UserService userService;
    @RequestMapping("/")
    public String index(HttpServletResponse response){
        Cookie cookieId = new Cookie("userToken",userService.generateCookieForUser("1") );
        cookieId.setPath("/");
        Cookie userId = new Cookie("userId","1" );
        userId.setPath("/");
        Cookie userName = new Cookie("userName","lc" );
        userName.setPath("/");

        response.addCookie(cookieId);
        response.addCookie(userId);
        response.addCookie(userName);

        return "index";
    }


    @RequestMapping("/send")
    public String index1(HttpServletRequest request){
      Cookie[] cookies=  request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("cookie = " + cookie);
        }
        return "index";
    }

}
