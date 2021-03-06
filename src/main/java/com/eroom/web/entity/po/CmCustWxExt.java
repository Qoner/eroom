package com.eroom.web.entity.po;
// Generated 2017-5-24 16:38:21 by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CmCustWxExt generated by hbm2java
 */
@Entity
@Table(name = "cm_cust_wx_ext", catalog = "eroom")
public class CmCustWxExt implements java.io.Serializable {

    private long custId;

    private String assetLevel;

    private Date birthday;

    private String cityCode;

    private String contactQq;

    private String contactTel;

    private String contactTel2;

    private String contactTel3;

    private String contactWechat;

    private Date createTime;

    private String custName;

    private String email;

    private Integer investAge;

    private String provinceCode;

    private String sex;

    private String tenantNo;

    private Date updateTime;

    public CmCustWxExt() {
    }

    public CmCustWxExt(long custId, Date createTime, String tenantNo) {
        this.custId = custId;
        this.createTime = createTime;
        this.tenantNo = tenantNo;
    }

    public CmCustWxExt(long custId, String assetLevel, Date birthday, String cityCode,
            String contactQq, String contactTel, String contactTel2, String contactTel3,
            String contactWechat, Date createTime, String custName, String email, Integer investAge,
            String provinceCode, String sex, String tenantNo, Date updateTime) {
        this.custId = custId;
        this.assetLevel = assetLevel;
        this.birthday = birthday;
        this.cityCode = cityCode;
        this.contactQq = contactQq;
        this.contactTel = contactTel;
        this.contactTel2 = contactTel2;
        this.contactTel3 = contactTel3;
        this.contactWechat = contactWechat;
        this.createTime = createTime;
        this.custName = custName;
        this.email = email;
        this.investAge = investAge;
        this.provinceCode = provinceCode;
        this.sex = sex;
        this.tenantNo = tenantNo;
        this.updateTime = updateTime;
    }

    @Id
    @Column(name = "cust_id", unique = true, nullable = false)
    public long getCustId() {
        return this.custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    @Column(name = "asset_level", length = 10)
    public String getAssetLevel() {
        return this.assetLevel;
    }

    public void setAssetLevel(String assetLevel) {
        this.assetLevel = assetLevel;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birthday", length = 19)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "city_code", length = 16)
    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Column(name = "contact_qq", length = 16)
    public String getContactQq() {
        return this.contactQq;
    }

    public void setContactQq(String contactQq) {
        this.contactQq = contactQq;
    }

    @Column(name = "contact_tel", length = 11)
    public String getContactTel() {
        return this.contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    @Column(name = "contact_tel2", length = 11)
    public String getContactTel2() {
        return this.contactTel2;
    }

    public void setContactTel2(String contactTel2) {
        this.contactTel2 = contactTel2;
    }

    @Column(name = "contact_tel3", length = 16)
    public String getContactTel3() {
        return this.contactTel3;
    }

    public void setContactTel3(String contactTel3) {
        this.contactTel3 = contactTel3;
    }

    @Column(name = "contact_wechat", length = 32)
    public String getContactWechat() {
        return this.contactWechat;
    }

    public void setContactWechat(String contactWechat) {
        this.contactWechat = contactWechat;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "cust_name", length = 32)
    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Column(name = "email", length = 64)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "invest_age")
    public Integer getInvestAge() {
        return this.investAge;
    }

    public void setInvestAge(Integer investAge) {
        this.investAge = investAge;
    }

    @Column(name = "province_code", length = 16)
    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Column(name = "sex", length = 4)
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "tenant_no", nullable = false, length = 32)
    public String getTenantNo() {
        return this.tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", length = 19)
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
