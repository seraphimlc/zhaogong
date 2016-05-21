package com.dagong.service;

import com.dagong.mapper.CompanyLogMapper;
import com.dagong.mapper.CompanyMapper;
import com.dagong.mapper.CompanyUserLogMapper;
import com.dagong.mapper.CompanyUserMapper;
import com.dagong.pojo.Company;
import com.dagong.pojo.CompanyLog;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private CompanyUserMapper companyUserMapper;

    @Resource
    private CompanyLogMapper companyLogMapper;
    @Resource
    private CompanyUserLogMapper companyUserLogMapper;

    @Resource
    private IdGenerator idGenerator;

    private static int COMPANY_STATUS_INIT = 1;
    private static int COMPANY_STATUS_SUCCESS = 2;
    private static int COMPANY_STATUS_FAIL = 3;



    public int createCompany(String userId, String companyName,
                             int companyType,
                             int industry,
                             int scale,
                             String area,
                             String address,
                             String telephone, String contact) {
        List<Company> list = companyMapper.selectByUserId(userId);
        if (list != null && !list.isEmpty()) {
            return 2;
        }
        list = companyMapper.selectByCompanyName(companyName);
        if (list != null && !list.isEmpty()) {
            return 3;
        }

        Company company = new Company();
        company.setId(idGenerator.generate(Company.class.getSimpleName()));
        company.setCompanytype(companyType);
        company.setIndustry(industry);
        company.setScale(scale);
        company.setArea(area);
        company.setAddress(address);
        company.setTelephone(telephone);
        company.setContact(contact);
        company.setVersion(1);
        company.setCreateTime(System.currentTimeMillis());
        company.setStatus(COMPANY_STATUS_INIT);
        company.setCreateUser(userId);
        company.setModifyTime(System.currentTimeMillis());
        companyMapper.insertSelective(company);
        log(company);
        return 1;
    }


    public boolean modifyCompany(String companyId,String userId, String companyName,
                             int companyType,
                             int industry,
                             int scale,
                             String area,
                             String address,
                             String telephone, String contact,int version) {

        Company company = new Company();
        company.setId(companyId);
        company.setCompanytype(companyType);
        company.setIndustry(industry);
        company.setScale(scale);
        company.setArea(area);
        company.setAddress(address);
        company.setTelephone(telephone);
        company.setContact(contact);
        company.setVersion(version);
        company.setCreateUser(userId);
        company.setModifyTime(System.currentTimeMillis());
        companyMapper.updateByPrimaryKeySelective(company);
        log(company);
        return true;
    }


    public boolean addDescription(String userId,String companyId,String description,int version){

        Company newCompany  = new Company();
        newCompany.setId(companyId);
        newCompany.setDescription(description);
        newCompany.setModifyTime(System.currentTimeMillis());
        newCompany.setVersion(version);
        companyMapper.updateByPrimaryKeySelective(newCompany);
        log(newCompany);
        return true;
    }

    private void log(Company company) {
        CompanyLog companyLog = new CompanyLog();
        companyLog.setId(idGenerator.generate(CompanyLog.class.getSimpleName()));
        companyLog.setCompanyId(company.getId());
        companyLog.setCreateTime(System.currentTimeMillis());
        companyLog.setModifyUser(company.getCreateUser());
        companyLog.setVersion(company.getVersion());
        companyLog.setNewStatus(company.getStatus());
       companyLog.setDetail(company.getDescription());
       companyLogMapper.insert(companyLog);
    }
}
