package com.example.controller;

import com.example.bean.Plan;
import com.example.service.PlanService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/5/8.
 */
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    //保存plan
    @RequestMapping("/savePlan")
    public void savePlan(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        long planId = Long.parseLong(request.getParameter("planId"));
        String title = request.getParameter("title");
        boolean isExpired = Boolean.parseBoolean(request.getParameter("isExpired"));
        boolean isComplete = Boolean.parseBoolean(request.getParameter("isComplete"));
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        String phoneNumber = request.getParameter("phoneNumber");

        Plan plan = new Plan();
        plan.setId(id);
        plan.setPlanId(planId);
        plan.setTitle(title);
        plan.setExpired(isExpired);
        plan.setComplete(isComplete);
        plan.setCreateTime(createTime);
        plan.setEditTime(editTime);
        plan.setEdit(isEdit);
        plan.setPhoneNumber(phoneNumber);
        planService.savePlan(plan);

    }

    //更新plan
    @RequestMapping("/updatePlan")
    public void updatePlan(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        long planId = Long.parseLong(request.getParameter("planId"));
        String title = request.getParameter("title");
        boolean isExpired = Boolean.parseBoolean(request.getParameter("isExpired"));
        boolean isComplete = Boolean.parseBoolean(request.getParameter("isComplete"));
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        String phoneNumber = request.getParameter("phoneNumber");

        Plan plan = new Plan();
        plan.setId(id);
        plan.setPlanId(planId);
        plan.setTitle(title);
        plan.setExpired(isExpired);
        plan.setComplete(isComplete);
        plan.setCreateTime(createTime);
        plan.setEditTime(editTime);
        plan.setEdit(isEdit);
        plan.setPhoneNumber(phoneNumber);
        planService.updatePlan(plan);
    }
    //获取plan
    @RequestMapping("/getPlan")
    public ResponseEntity<List<Plan>> getPlan(@RequestParam("phoneNumber") String phoneNumber){
        ArrayList<Plan> list = (ArrayList<Plan>) planService.getPlan(phoneNumber);
        return new ResponseEntity<List<Plan>>(list, HttpStatus.OK);
    }
}
