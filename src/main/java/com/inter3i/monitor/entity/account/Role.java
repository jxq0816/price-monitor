package com.inter3i.monitor.entity.account;

import java.util.Date;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/2 17:45
 */
public class Role {

    private String roleid;
    private String applicationid;
    private String name;
    private String role_no;
    private Date createtime;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRole_no() {
        return role_no;
    }

    public void setRole_no(String role_no) {
        this.role_no = role_no;
    }
}
