package com.inter3i.monitor.entity;

/**
 * Created by dujun on 2017/6/21.
 */
public class IndexData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;  //首页数据id
    private String englishName;  //英文名
    private String chineseName;  //中文名
    private Integer number;  //基数
    private Integer minData;  //最小的范围数
    private Integer maxData;  //最大的范围数



    /**
     * 描述: 首页数据id
     */
    public Integer getId(){
        return this.id;
    }

    /**
     * 描述: 首页数据id
     */
    public void setId(Integer id){
        this.id = id;
    }

    /**
     * 描述: 英文名
     */
    public String getEnglishName(){
        return this.englishName;
    }

    /**
     * 描述: 英文名
     */
    public void setEnglishName(String englishName){
        this.englishName = englishName;
    }

    /**
     * 描述: 中文名
     */
    public String getChineseName(){
        return this.chineseName;
    }

    /**
     * 描述: 中文名
     */
    public void setChineseName(String chineseName){
        this.chineseName = chineseName;
    }

    /**
     * 描述: 基数
     */
    public Integer getNumber(){
        return this.number;
    }

    /**
     * 描述: 基数
     */
    public void setNumber(Integer number){
        this.number = number;
    }

    /**
     * 描述: 最小的范围数
     */
    public Integer getMinData(){
        return this.minData;
    }

    /**
     * 描述: 最小的范围数
     */
    public void setMinData(Integer minData){
        this.minData = minData;
    }

    /**
     * 描述: 最大的范围数
     */
    public Integer getMaxData(){
        return this.maxData;
    }

    /**
     * 描述: 最大的范围数
     */
    public void setMaxData(Integer maxData){
        this.maxData = maxData;
    }

}
