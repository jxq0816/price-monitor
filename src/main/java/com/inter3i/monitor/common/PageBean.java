package com.inter3i.monitor.common;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/4/26 19:22
 */
public class PageBean {

    public final static Integer DEFAULT_PAGE_SIZE = 10;

    //当前页码
    private Integer pageNo;
    //页面数据条数
    private Integer pageSize;
    //总共数据条数
    private Long totalNum;
    //总共的页数
    private Integer totalPage;
    //当前页面的开始条数
    private Long pageBeginNum;
    //是否有上一页
    private Boolean hasPreviousPage;
    //是否有下一页
    private Boolean hasNextPage;

    public PageBean(){
        pageNo=1;
        pageSize=DEFAULT_PAGE_SIZE;
    }

    public PageBean(Integer pageNo, Integer pageSize, Long totalNum) {
        Integer constructorPageNum = (pageNo == null || pageNo < 0) ? 1 : pageNo;
        Integer constructorPageSize = (pageSize == null || pageSize < 0) ? DEFAULT_PAGE_SIZE : pageSize;
        Long constructorTotalNum = (totalNum == null || totalNum < 0) ? 0 : totalNum;
        Integer constructorTotalPage = (constructorTotalNum > 0 && constructorTotalNum.intValue() < constructorPageSize) ? 1 : (constructorTotalNum.intValue() % constructorPageSize.intValue() == 0) ? (constructorTotalNum.intValue() /constructorPageSize.intValue()) : (constructorTotalNum.intValue() /constructorPageSize.intValue() + 1);
        Long constructorPageBeginNum = Long.valueOf((constructorPageNum - 1) * constructorPageSize);
        Boolean constructorHasPreviousPage = !(constructorPageNum == 1);
        Boolean constructorHasNextPage = (constructorPageNum < constructorTotalPage);

        this.pageNo = constructorPageNum;
        this.pageSize = constructorPageSize;
        this.totalNum = constructorTotalNum;
        this.totalPage = constructorTotalPage;
        this.pageBeginNum = constructorPageBeginNum;
        this.hasPreviousPage = constructorHasPreviousPage;
        this.hasNextPage = constructorHasNextPage;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getPageBeginNum() {
        return pageBeginNum;
    }

    public void setPageBeginNum(Long pageBeginNum) {
        this.pageBeginNum = pageBeginNum;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

}
