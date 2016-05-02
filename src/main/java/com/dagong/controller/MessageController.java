package com.dagong.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuchang on 16/1/28.
 */
@RestController
@RequestMapping("/message")
public class MessageController {


    @RequestMapping(".do")
    public String getMessageCount(@CookieValue("userId") String userId) {
        return null;
    }

    @RequestMapping("/getNewMessage.do")
    public String getNewMessage(@CookieValue("userId") String userId,
                                @RequestParam("page") String page) {
        return null;
    }

    @RequestMapping("/getReadMessage.do")
    public String getReadMessage(@CookieValue("userId") String userId,
                                 @RequestParam("page") String page) {
        return null;
    }

    @RequestMapping("/readMessage.do")
    public String readMessage(@CookieValue("userId") String userId) {
        return null;
    }


}
