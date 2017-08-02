package com.inter3i.monitor.common;

import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/4/26 19:21
 */
public class Page<T> {

    private List<T> result;
    private PageBean pageBean;

    public Page()
    {
    }

    public Page(List<T> lstResult, PageBean pageBean)
    {
        this.result = lstResult;
        this.pageBean = pageBean;
    }

    public List getResult()
    {
        return this.result;
    }

    public void setResult(List lstResult)
    {
        this.result = lstResult;
    }

    public PageBean getPageBean()
    {
        return this.pageBean;
    }

    public void setPageBean(PageBean pageBean)
    {
        this.pageBean = pageBean;
    }
}
