package com.eroom.web.entity.po;
// Generated 2017-5-24 16:38:21 by Hibernate Tools 5.2.1.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CustRoomCollect generated by hbm2java
 */
@Entity
@Table(name = "t_cust_room_collect", catalog = "eroom")
public class CustRoomCollect implements java.io.Serializable {

    private Long collectId;

    private long custId;

    private long rentId;

    private String state;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public CustRoomCollect() {
    }

    public CustRoomCollect(long custId, long rentId, String state, Date createTime) {
        this.custId = custId;
        this.rentId = rentId;
        this.state = state;
        this.createTime = createTime;
    }

    public CustRoomCollect(long custId, long rentId, String state, Date createTime,
                           Date updateTime, String remark) {
        this.custId = custId;
        this.rentId = rentId;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "collect_id", unique = true, nullable = false)
    public Long getCollectId() {
        return this.collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }

    @Column(name = "cust_id", nullable = false)
    public long getCustId() {
        return this.custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    @Column(name = "rent_id", nullable = false)
    public long getRentId() {
        return this.rentId;
    }

    public void setRentId(long rentId) {
        this.rentId = rentId;
    }

    @Column(name = "state", nullable = false, length = 4)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, length = 19)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", length = 19)
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "remark")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}