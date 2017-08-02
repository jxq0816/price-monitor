package com.inter3i.monitor.business.impl;

import com.inter3i.monitor.business.IndexService;
import com.inter3i.monitor.dao.JDBCBaseDao;
import com.inter3i.monitor.entity.IndexData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/*
 * DESCRIPTION : 电商价格监测-首页ServiceImpl
 * USER : liuxiaolei
 * DATE : 2017/6/21 11:40
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private JDBCBaseDao jdbcBaseDao;


    /**
     * 定时计算首页面显示的的数据(预警次数、监测产品、注册用户、监测活动)
     *
     */
    @Override
    public void calculationIndexData() {
        String sql = "select id,number,min_data,max_data from index_data";
        List<IndexData> indexDatas = jdbcBaseDao.queryList(IndexData.class,sql);
        for (IndexData indexData: indexDatas){
            updateIndexData(indexData);
        }
    }

    /**
     * 定时计算首页面显示的的数据(预警次数、监测产品、注册用户、监测活动)
     *@return List<IndexData> 首页数据集合
     */
    @Override
    public List<IndexData> getIndexData() {
        String sql = "select english_name,chinese_name,number from index_data";
        return jdbcBaseDao.queryList(IndexData.class,sql);
    }

    /**
     * 通过基数、最大数、最小数换算新基数值
     *
     */
    public void updateIndexData(IndexData indexData){
        int max = indexData.getMaxData();//得到最大范围的数
        int min = indexData.getMinData();//得到最小范围的数

        Random random = new Random();
        int number = random.nextInt(max)%(max-min+1)+min; //得到最小值到最大值的区间整数
        String sql = "update index_data set number = ? where id = ?";
        jdbcBaseDao.executeUpdate(sql,number+indexData.getNumber(),indexData.getId());
    }
}
