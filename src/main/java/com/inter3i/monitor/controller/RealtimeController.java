package com.inter3i.monitor.controller;

import com.alibaba.fastjson.JSONArray;
import com.inter3i.monitor.business.*;
import com.inter3i.monitor.common.ApplicationSessionFactory;
import com.inter3i.monitor.common.Page;
import com.inter3i.monitor.common.PageBean;
import com.inter3i.monitor.entity.ActiveContent;
import com.inter3i.monitor.entity.Category;
import com.inter3i.monitor.entity.MoreContent;
import com.inter3i.monitor.entity.WarningContent;
import com.inter3i.monitor.util.DateComparatorHelp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/*
 * DESCRIPTION : 
 * USER : jiangxingqi
 * DATE : 2017/5/22 16:16
 */
@Controller
@RequestMapping("realtime")
public class RealtimeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TaskConfigurationService taskConfigurationService;

    @Autowired
    private TaskConfigurationChildService taskConfigurationChildService;

    @Autowired
    private ActiveContentService activeContentService; //活动内容

    @Autowired
    private WarningContentService warningContentService;  //预警内容

    /**
     * 品类列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "category/list")
    @ResponseBody
    public Map list(HttpServletRequest request, HttpServletResponse response){
        Map rs = new HashMap();
        List<Category> list=categoryService.findAllList();
        String userId=ApplicationSessionFactory.getAccountId(request);

        List categoryList=new ArrayList();
        for(Category i:list){
            Map categoryMap=new HashMap();
            String categoryName=i.getCategoryNameExplain();
            categoryMap.put("categoryName",categoryName);

            List<Map<String,Object>> taskList=taskConfigurationService.findList(userId,categoryName);
            categoryMap.put("taskList",taskList);

            categoryList.add(categoryMap);
        }
        rs.put("categoryList",categoryList);
        return rs;
    }

    /**
     * 最近的主任务
     * @param request
     * @return
     */
    @RequestMapping(value = "last/task")
    public ModelAndView lastOne(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/report");
        String userId=ApplicationSessionFactory.getAccountId(request);
        Map<String,Object> lastTask=taskConfigurationService.findLastOne(userId);
        if(lastTask==null){
            //TODO 日志
        }else{
            String taskId=(String) lastTask.get("taskId");
            mv.addObject("lastTask",lastTask);
            PageBean pageBean = new PageBean();
            Page childList=taskConfigurationChildService.findPage(pageBean,taskId,"warningTime","desc");
            mv.addObject("page",childList);
        }
        return mv;
    }

    /**
     * 点击主任务名称
     * @param request
     * @return
     */
    @RequestMapping(value = "task",method= RequestMethod.POST)
    public ModelAndView findOne(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/report");
        String taskId=request.getParameter("taskId");
        String categoryName=request.getParameter("categoryName");
        String pageNoStr=request.getParameter("pageNo");
        String orderParam=request.getParameter("orderParam");
        String orderType=request.getParameter("orderType");
        if(StringUtils.isEmpty(pageNoStr)){
            pageNoStr="1";
        }
        Integer pageNo=Integer.parseInt(pageNoStr);
        if(StringUtils.isNotEmpty(taskId)&&StringUtils.isNotEmpty(categoryName)){
            Map<String,Object> lastTask=taskConfigurationService.findOne(taskId,categoryName);
            mv.addObject("lastTask",lastTask);
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(pageNo);
            Page childList=taskConfigurationChildService.findPage(pageBean,taskId,orderParam,orderType);
            mv.addObject("page",childList);
            mv.addObject("orderParam",orderParam);
        }
        return mv;
    }

    /**
     * 子任务分页
     * @param request
     * @return
     */
    @RequestMapping(value = "task/child")
    public ModelAndView taskChild(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/report_task_child");
        String taskId=request.getParameter("taskId");
        String pageNoStr=request.getParameter("pageNo");
        String orderParam=request.getParameter("orderParam");
        String orderType=request.getParameter("orderType");
        if(StringUtils.isEmpty(pageNoStr)){
            pageNoStr="1";
        }
        Integer pageNo=Integer.parseInt(pageNoStr);
        if(StringUtils.isNotEmpty(taskId)){
            PageBean pageBean = new PageBean();
            pageBean.setPageNo(pageNo);
            Page childList=taskConfigurationChildService.findPage(pageBean,taskId,orderParam,orderType);
            mv.addObject("page",childList);
            mv.addObject("orderParam",orderParam);
        }
        return mv;
    }

    /**
     * 子任务详情
     * @param request
     * @return
     */
    @RequestMapping(value = "task/child/detail",method= RequestMethod.POST)
    public ModelAndView childDetail(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/product-details");

        String taskId=request.getParameter("taskId");
        String categoryName=request.getParameter("categoryName");
        String childTaskId=request.getParameter("childTaskId");
        String pageNo=request.getParameter("pageNo");
        String orderParam=request.getParameter("orderParam");

        Map<String, Object> childTask = taskConfigurationChildService.findOneMap(childTaskId);
         List<Map<String,Object>> priceTrend=taskConfigurationChildService.priceTrend(childTaskId);
        priceTrend=taskConfigurationChildService.supplementPriceTrend(priceTrend,childTaskId);//补充缺失数据
        DateComparatorHelp comparator=new DateComparatorHelp();
        Collections.sort(priceTrend,comparator);//价格按时间排序
        String rs= JSONArray.toJSONString(priceTrend);
        List<ActiveContent> activeContents = activeContentService.getTaskActiveContent(childTaskId,3);
        List<WarningContent> warningContents = warningContentService.getTaskWarningContent(childTaskId,3);
        mv.addObject("childTask",childTask);
        mv.addObject("priceTrend",rs);
        mv.addObject("taskId",taskId);
        mv.addObject("categoryName",categoryName);
        mv.addObject("childTaskId",childTaskId);
        mv.addObject("pageNo",pageNo);
        mv.addObject("orderParam",orderParam);
        mv.addObject("activeContents",activeContents);
        mv.addObject("warningContents",warningContents);
        return mv;
    }

    /**
     * 子任务详情 更多预警 更多活动
     * @param request
     * @return
     */
    @RequestMapping(value = "task/child/detail/more",method= RequestMethod.POST)
    public ModelAndView detailMore(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/product-list");

        String taskId=request.getParameter("taskId");
        String categoryName=request.getParameter("categoryName");
        String childTaskId=request.getParameter("childTaskId");
        String pageNo=request.getParameter("pageNo");
        String orderParam=request.getParameter("orderParam");
        String moreType=request.getParameter("moreType");
        Map<String, Object> childTask = taskConfigurationChildService.findOneMap(childTaskId);
        mv.addObject("childTask",childTask);
        mv.addObject("taskId",taskId);
        mv.addObject("categoryName",categoryName);
        mv.addObject("pageNo",pageNo);
        mv.addObject("orderParam",orderParam);
        mv.addObject("moreType",moreType);
        return mv;
    }

    /**
     * 预警和活动列表 分页
     * @param request
     * @return
     */
    @RequestMapping(value = "task/child/detail/more/page",method= RequestMethod.POST)
    public ModelAndView detailMorePage(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("/monitorfile/realtimeReport/product_list_child");
        String childTaskId=request.getParameter("childTaskId");
        String moreType=request.getParameter("moreType");
        PageBean pageBean = new PageBean();
        String pageMoreNoStr=request.getParameter("pageNo");
        if(StringUtils.isEmpty(pageMoreNoStr)){
            pageMoreNoStr="1";
        }
        Integer pageMoreNo=Integer.parseInt(pageMoreNoStr);
        pageBean.setPageNo(pageMoreNo);
        Page page=null;
        if(MoreContent.WARNING_TYPE.equals(moreType)){
            //预警
            page=warningContentService.findPage(pageBean,childTaskId);
        }
        if(MoreContent.ACTIVE_TYPE.equals(moreType)){
            page=activeContentService.findPage(pageBean,childTaskId);
        }
        mv.addObject("page",page);
        mv.addObject("moreType",moreType);
        return mv;
    }

}
