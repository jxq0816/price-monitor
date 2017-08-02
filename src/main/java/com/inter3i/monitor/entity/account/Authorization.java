package com.inter3i.monitor.entity.account;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/2 17:53
 */
public class Authorization {

    private Account account;
    private List<Resource> authorisedResourceList;
    private List<Menu> authorisedMenuList;
    private List<Menu> unauthorizedMenuList;
    private List<Role> roleList;
    private List<String> authorisedResourceNoList;
    private List<String> authorisedMenuNoList;
    private Set<String> unauthorizedMenuUrlSet;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Resource> getAuthorisedResourceList() {
        return authorisedResourceList;
    }

    public void setAuthorisedResourceList(List<Resource> authorisedResourceList) {
        this.authorisedResourceList = authorisedResourceList;
    }

    public List<Menu> getAuthorisedMenuList() {
        return authorisedMenuList;
    }

    public void setAuthorisedMenuList(List<Menu> authorisedMenuList) {
        this.authorisedMenuList = authorisedMenuList;
    }

    public List<Menu> getUnauthorizedMenuList() {
        return unauthorizedMenuList;
    }

    public void setUnauthorizedMenuList(List<Menu> unauthorizedMenuList) {
        this.unauthorizedMenuList = unauthorizedMenuList;
    }

    public List<String> getAuthorisedResourceNoList() {
        if(CollectionUtils.isNotEmpty(authorisedResourceList)){
            authorisedResourceNoList = new ArrayList<String>();
            for(Resource resource : authorisedResourceList){
                authorisedResourceNoList.add(resource.getResource_no());
            }
        }
        return authorisedResourceNoList;
    }

    public void setAuthorisedResourceNoList(List<String> authorisedResourceNoList) {
        this.authorisedResourceNoList = authorisedResourceNoList;
    }

    public List<String> getAuthorisedMenuNoList() {
        if(CollectionUtils.isNotEmpty(authorisedMenuList)){
            authorisedMenuNoList = new ArrayList<String>();
            for(Menu menu : authorisedMenuList){
                authorisedMenuNoList.add(menu.getMenu_no());
            }
        }
        return authorisedMenuNoList;
    }

    public void setAuthorisedMenuNoList(List<String> authorisedMenuNoList) {
        this.authorisedMenuNoList = authorisedMenuNoList;
    }

    public Set<String> getUnauthorizedMenuUrlSet() {
        if(CollectionUtils.isNotEmpty(unauthorizedMenuList)){
            unauthorizedMenuUrlSet = new HashSet<String>();
            for(Menu menu : unauthorizedMenuList){
                unauthorizedMenuUrlSet.add(menu.getUrl());
            }
        }
        return unauthorizedMenuUrlSet;
    }

    public void setUnauthorizedMenuUrlSet(Set<String> unauthorizedMenuUrlSet) {
        this.unauthorizedMenuUrlSet = unauthorizedMenuUrlSet;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
