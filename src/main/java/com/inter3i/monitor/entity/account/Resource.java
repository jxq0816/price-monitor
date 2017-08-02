package com.inter3i.monitor.entity.account;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/2 17:52
 */
public class Resource {

    private String resourceid;
    private String applicationid;
    private String menuid;
    private String name;
    private String resource_no;
    private String createtime;

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource_no() {
        return resource_no;
    }

    public void setResource_no(String resource_no) {
        this.resource_no = resource_no;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
