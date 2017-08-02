package com.inter3i.monitor.entity;

/*
 * DESCRIPTION : 电商价格监测-任务配置修改类不对应任务实体类
 * USER : liuxiaolei
 * DATE : 2017/6/20 12:24
 */
public class taskUpdate {


    private String chineseName;//中文名称
    private String englishName;//英文名称
    private String value;//值

    /**
     * 描述: 中文名称
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 描述: 中文名称
     */
    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    /**
     * 描述: 英文名称
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * 描述: 英文名称
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    /**
     * 描述: 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 描述: 值
     */
    public void setValue(String value) {
        this.value = value;
    }
}
