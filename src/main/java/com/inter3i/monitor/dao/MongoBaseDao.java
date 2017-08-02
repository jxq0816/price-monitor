package com.inter3i.monitor.dao;

import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/6/6 15:36
 */
@Component
public class MongoBaseDao{

    private MongoOperations mongoTemplate;


    public <T> T getEntityById(Class<T> classes, String _id){
        Query query = new Query(Criteria.where("_id").is(new ObjectId(_id)));
        return this.mongoTemplate.findOne(query,classes);
    }

    public <T> void removeEntityById(Class<T> classes, String _id){
        Query query = new Query(Criteria.where("_id").is(new ObjectId(_id)));
        this.mongoTemplate.remove(query,classes);
    }


    public <T> Page queryPage(Class<T> classes, PageBean pageBean, Query query){
        Integer page = pageBean.getPageNo();
        Integer pageSize = pageBean.getPageSize();
        Long totalNum = this.mongoTemplate.count(query,classes);
        totalNum = (totalNum == null) ? 0 : totalNum;
        pageBean = new PageBean(page,pageSize,totalNum);
        if (pageBean.getTotalNum() == 0) {
            return new Page(new ArrayList(), pageBean);
        }
        query.skip(Integer.valueOf(pageBean.getPageBeginNum().toString()));// skip相当于从那条记录开始
        query.limit(pageBean.getPageSize());// 从skip开始,取多少条记录
        List<T> data = this.mongoTemplate.find(query,classes);
        return new Page(data, pageBean);
    }

    public <T> Page queryPage(Class<T> classes, PageBean pageBean, Query query, String collectionName){
        Integer page = pageBean.getPageNo();
        Integer pageSize = pageBean.getPageSize();
        Long totalNum = this.mongoTemplate.count(query,collectionName);
        totalNum = (totalNum == null) ? 0 : totalNum;
        pageBean = new PageBean(page,pageSize,totalNum);
        if (pageBean.getTotalNum() == 0) {
            return new Page(new ArrayList(), pageBean);
        }
        query.skip(Integer.valueOf(pageBean.getPageBeginNum().toString()));// skip相当于从那条记录开始
        query.limit(pageBean.getPageSize());// 从skip开始,取多少条记录
        List<T> data = this.mongoTemplate.find(query,classes,collectionName);
        return new Page(data, pageBean);
    }


    public MongoOperations getMongoTemplate() {
        return mongoTemplate;
    }
    //@Autowired
    public void setMongoTemplate(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
