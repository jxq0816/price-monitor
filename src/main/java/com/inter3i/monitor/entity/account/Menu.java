package com.inter3i.monitor.entity.account;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/2 17:45
 */
public class Menu {

    private String menuid;
    private String applicationid;
    private String name;
    private String menu_no;
    private String url;
    private String createtime;
    private String parent_menu_id;
    private String menu_level;

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
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

    public String getMenu_no() {
        return menu_no;
    }

    public void setMenu_no(String menu_no) {
        this.menu_no = menu_no;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getParent_menu_id() {
        return parent_menu_id;
    }

    public void setParent_menu_id(String parent_menu_id) {
        this.parent_menu_id = parent_menu_id;
    }

    public String getMenu_level() {
        return menu_level;
    }

    public void setMenu_level(String menu_level) {
        this.menu_level = menu_level;
    }
}
