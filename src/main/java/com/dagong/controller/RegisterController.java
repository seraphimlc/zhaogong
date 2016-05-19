package com.dagong.controller;

import com.dagong.pojo.Company;
import com.dagong.service.CompanyService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/4/23.
 */
@RestController

public class RegisterController {
    @Resource
    private CompanyService companyService;

    public String register(@CookieValue("userId") String userId,
                           @RequestParam("companyName") String companyName,
                           @RequestParam("companyType") int companyType,
                           @RequestParam("industry") int industry,
                           @RequestParam("scale") int scale,
                           @RequestParam("area") String area,
                           @RequestParam("address") String address,
                           @RequestParam("telephone") String telephone,
                           @RequestParam("contact") String contact) {
        companyService.createCompany(userId, companyName, companyType, industry, scale,
                area, address, telephone, contact);
        return "ok";

    }

    public String addDescription(@CookieValue("userId") String userId,
                                 @RequestParam("companyId") String companyId,
                                 @RequestParam("description") String description,
                                 @RequestParam("version") int version) {
        companyService.addDescription(userId,companyId,description,version);
        return "ok";
    }

    public String getDescription(@CookieValue("userId") String userId,
                                 @CookieValue("companyId") String companyId) {
        return null;
    }

    public String modifyDescription(@CookieValue("userId") String userId,
                                    @CookieValue("companyId") String companyId,
                                    @RequestParam("description") String description,
                                    @RequestParam("version") int version) {
       companyService.addDescription(userId,companyId,description,version);
        return null;
    }

}
