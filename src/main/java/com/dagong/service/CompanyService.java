package com.dagong.service;

import com.dagong.mapper.CompanyMapper;
import com.dagong.mapper.CompanyUserMapper;
import com.dagong.mapper.ContactMapper;
import com.dagong.pojo.Company;
import com.dagong.pojo.CompanyUser;
import com.dagong.pojo.Contact;
import com.dagong.util.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liuchang on 16/1/17.
 */
@Service
public class CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private ContactMapper contactMapper;

    @Resource
    private CompanyUserMapper companyUserMapper;

    @Resource
    private IdGenerator idGenerator;

    private static int COMPANY_STATUS_INIT = 1;
    private static int COMPANY_STATUS_SUCCESS = 2;
    private static int COMPANY_STATUS_FAIL = 3;

    public Company createCompany(String name, String detail) {
        Company company = new Company();
        company.setId(idGenerator.generate(Company.class.getSimpleName()));
        company.setDetail(detail);
        company.setStatus(COMPANY_STATUS_INIT);
        company.setName(name);
        companyMapper.insert(company);
        return company;
    }

    public boolean modifyCompany(String companyId, String name, String detail) {
        updateCompany(companyId, name, detail, null);
        return true;
    }

    private void updateCompany(String companyId, String name, String detail, Integer status) {
        Company company = new Company();
        company.setId(companyId);
        company.setName(name);
        company.setDetail(detail);
        company.setStatus(status);
        companyMapper.updateByPrimaryKey(company);
    }

    public boolean allowCompany(String companyId) {
        updateCompany(companyId, null, null, COMPANY_STATUS_SUCCESS);
        return true;
    }

    public boolean refuseCompany(String companyId) {
        updateCompany(companyId, null, null, COMPANY_STATUS_FAIL);
        return true;
    }


    public boolean createContact(String companyId, String address, String contactName, String email, String phone, String wechat, String qq) {
        Contact contact = new Contact();
        contact.setId(idGenerator.generate(Contact.class.getSimpleName()));
        contact.setCompanyId(companyId);
        contact.setAddress(address);
        contact.setContactName(contactName);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setWechat(wechat);
        contact.setQq(qq);
        contactMapper.insert(contact);
        return true;
    }

    public boolean modifyContact(String contactId,String companyId, String address, String contactName, String email, String phone, String wechat, String qq) {
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setCompanyId(companyId);
        contact.setAddress(address);
        contact.setContactName(contactName);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setWechat(wechat);
        contact.setQq(qq);
        contactMapper.updateByPrimaryKey(contact);
        return true;
    }

    public boolean deleteContact(String contactId) {
        contactMapper.deleteByPrimaryKey(contactId);
        return true;
    }

    public Company getCompanyByName(String name){
        return companyMapper.selectByName(name);
    }

    public boolean createCompanyUser(String companyId, String username, String password, String phone) {
        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(idGenerator.generate(CompanyUser.class.getSimpleName()));
        companyUser.setPhone(phone);
        companyUser.setCompanyId(companyId);
        companyUser.setUsername(username);
        companyUser.setPassword(password);
        companyUserMapper.insert(companyUser);
        return true;
    }

    public boolean modifyCompanyUserPassword(String companyUserId, String password) {
        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(companyUserId);
        companyUser.setPassword(password);
        companyUserMapper.updateByPrimaryKeySelective(companyUser);
        return true;
    }

    public boolean modifyCompanyUserPhone(String companyUserId, String phone) {
        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(companyUserId);
        companyUser.setPhone(phone);
        return true;
    }

    public Company getCompanyById(String companyId){
        return companyMapper.selectByPrimaryKey(companyId);
    }


}
