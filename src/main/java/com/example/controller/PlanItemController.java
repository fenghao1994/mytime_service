package com.example.controller;

import com.example.annotations.PermissionAnno;
import com.example.bean.Photo;
import com.example.bean.PingLun;
import com.example.bean.PlanItem;
import com.example.service.PlanItemService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/5/8.
 */
@RestController
@RequestMapping("/planItem")
public class PlanItemController {

    @Autowired
    private PlanItemService planItemService;

    //保存planitem
    @RequestMapping("/savePlanItem")
    @PermissionAnno
    public ResponseEntity<String> savePlanItem(HttpServletRequest request, @RequestParam(name = "addressFiles",required = false
    )MultipartFile[] files){
        int id = Integer.parseInt(request.getParameter("id"));
        long planId = Long.parseLong(request.getParameter("planId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        String phoneNumber = request.getParameter("phoneNumber");
        String messageContent = request.getParameter("messageContent");
        String messagePhoneNumber = request.getParameter("messagePhoneNumber");
        String location = request.getParameter("location");
        String phoneNumberLianXi = request.getParameter("phoneNumberLianXi");
        boolean isEveryDay = Boolean.parseBoolean(request.getParameter("isEveryDay"));
        boolean isManyDays = Boolean.parseBoolean(request.getParameter("isManyDays"));
        boolean isExpired = Boolean.parseBoolean(request.getParameter("isExpired"));
        boolean isComplete = Boolean.parseBoolean(request.getParameter("isComplete"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        int hour = Integer.parseInt(request.getParameter("hour"));
        int minute = Integer.parseInt(request.getParameter("minute"));
        int alarmWay = Integer.parseInt(request.getParameter("alarmWay"));
        String describes = request.getParameter("describes");
        String open = request.getParameter("open");
        String address = request.getParameter("address");
        List<MultipartFile> files1 = new ArrayList<>();
        if (files != null && files.length > 0){
            for (int i = 0 ; i< files.length; i++){
                files1.add(files[i]);
            }
        }
        PlanItem planItem = new PlanItem();
        planItem.setId(id);
        planItem.setPlanId(planId);
        planItem.setTitle(title);
        planItem.setContent(content);
        planItem.setCreateTime(createTime);
        planItem.setEditTime(editTime);
        planItem.setEdit(isEdit);
        planItem.setPhoneNumber(phoneNumber);
        planItem.setMessageContent(messageContent);
        planItem.setMessagePhoneNumber(messagePhoneNumber);
        planItem.setLocation(location);
        planItem.setPhoneNumberLianXi(phoneNumberLianXi);
        planItem.setEveryDay(isEveryDay);
        planItem.setManyDays(isManyDays);
        planItem.setExpired(isExpired);
        planItem.setComplete(isComplete);
        planItem.setDelete(isDelete);
        planItem.setYears(year);
        planItem.setDays(day);
        planItem.setMonths(month);
        planItem.setHours(hour);
        planItem.setMinutes(minute);
        planItem.setAlarmWay(alarmWay);
        planItem.setDescribes(describes);
        planItem.setOpen(open);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<Photo> list = null;
        try {
            list = objectMapper.readValue(address, new TypeReference<ArrayList<Photo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        planItem.setAddress(list);
        boolean flag = planItemService.savePlanItem(planItem, files1);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"保存成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"保存失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //更新planItem
    @RequestMapping("/updatePlanItem")
    @PermissionAnno
    public ResponseEntity<String> updatePlanItem(HttpServletRequest request, @RequestParam(name = "addressFiles",required = false)MultipartFile[] files){
        int id = Integer.parseInt(request.getParameter("id"));
        long planId = Long.parseLong(request.getParameter("planId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        String phoneNumber = request.getParameter("phoneNumber");
        String messageContent = request.getParameter("messageContent");
        String messagePhoneNumber = request.getParameter("messagePhoneNumber");
        String location = request.getParameter("location");
        String phoneNumberLianXi = request.getParameter("phoneNumberLianXi");
        boolean isEveryDay = Boolean.parseBoolean(request.getParameter("isEveryDay"));
        boolean isManyDays = Boolean.parseBoolean(request.getParameter("isManyDays"));
        boolean isExpired = Boolean.parseBoolean(request.getParameter("isExpired"));
        boolean isComplete = Boolean.parseBoolean(request.getParameter("isComplete"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        int hour = Integer.parseInt(request.getParameter("hour"));
        int minute = Integer.parseInt(request.getParameter("minute"));
        int alarmWay = Integer.parseInt(request.getParameter("alarmWay"));
        String describes = request.getParameter("describes");
        String open = request.getParameter("open");
        String address = request.getParameter("address");
        List<MultipartFile> files1 = new ArrayList<>();
        if (files != null && files.length > 0){
            for (int i = 0 ; i< files.length; i++){
                files1.add(files[i]);
            }
        }

        PlanItem planItem = new PlanItem();
        planItem.setId(id);
        planItem.setPlanId(planId);
        planItem.setTitle(title);
        planItem.setContent(content);
        planItem.setCreateTime(createTime);
        planItem.setEditTime(editTime);
        planItem.setEdit(isEdit);
        planItem.setPhoneNumber(phoneNumber);
        planItem.setMessageContent(messageContent);
        planItem.setMessagePhoneNumber(messagePhoneNumber);
        planItem.setLocation(location);
        planItem.setPhoneNumberLianXi(phoneNumberLianXi);
        planItem.setEveryDay(isEveryDay);
        planItem.setManyDays(isManyDays);
        planItem.setExpired(isExpired);
        planItem.setComplete(isComplete);
        planItem.setDelete(isDelete);
        planItem.setYears(year);
        planItem.setDays(day);
        planItem.setMonths(month);
        planItem.setHours(hour);
        planItem.setMinutes(minute);
        planItem.setAlarmWay(alarmWay);
        planItem.setDescribes(describes);
        planItem.setOpen(open);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<Photo> list = null;
        try {
            list = objectMapper.readValue(address, new TypeReference<ArrayList<Photo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        planItem.setAddress(list);
        boolean flag = planItemService.updatePlanItem(planItem, files1);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"保存成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"保存失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //获取planItem
    @RequestMapping("/getPlanItem")
    @PermissionAnno
    public ResponseEntity<List<PlanItem>> getPlanItem(@RequestParam("phoneNumber") String phoneNumber){
        List<PlanItem> list = planItemService.getPlanItem(phoneNumber);
        return new ResponseEntity<List<PlanItem>>(list, HttpStatus.OK);
    }

    @RequestMapping("/deletePlanItem")
    @ResponseBody
    @PermissionAnno
    public String deletePlanItem(@RequestParam("phoneNumber")String phoneNumber, @RequestParam("createTime") long createTime){
        boolean flag = planItemService.deletePlanItem(phoneNumber, createTime);
        if (flag){
            return "删除成功";
        }else {
            return "删除失败";
        }
    }

    @RequestMapping("/completePlanItem")
    @ResponseBody
    @PermissionAnno
    public String completePlanItem(@RequestParam("phoneNumber")String phoneNumber, @RequestParam("createTime") String createTime, @RequestParam("editTime") String editTime){
        boolean flag = planItemService.completePlanItem(phoneNumber, createTime, editTime);
        if (flag){
            return "删除成功";
        }else {
            return "删除失败";
        }
    }

    @RequestMapping("/getPlanItemWithPlanIdAndPhoneNumber")
    public ResponseEntity<List<PlanItem>> getPlanItemWithPlanIdAndPhoneNumber(@RequestParam("phoneNumber")String phoneNumber,
                                                              @RequestParam("planId")String planId){
        List<PlanItem> list = new ArrayList<>();
        list = planItemService.getPlanItemWithPlanIdAndPhoneNumber(phoneNumber, planId);
        return new ResponseEntity<List<PlanItem>>(list, HttpStatus.OK);
    }

    @RequestMapping("/root/deletePlanItemFromDisk")
    public ResponseEntity<?> deletePlanItemFromDisk(@RequestParam("idd")String idd){
        boolean flag = planItemService.deletePlanItemFromDisk(idd);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"删除成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"删除失败\"}", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 增加评论
     * @param content
     * @param phoneNumber
     * @param createTime
     * @param editTime
     * @param deletePinglun
     * @return
     */
    @RequestMapping("/pinglun")
    @PermissionAnno
    public ResponseEntity<?> pingLun(@RequestParam("content") String content,
                                     @RequestParam("phoneNumber")String phoneNumber,
                                     @RequestParam("createTime")long createTime,
                                     @RequestParam("editTime")long editTime,
                                     @RequestParam("deletePinglun")String deletePinglun){
        boolean flag = planItemService.pingLun(content, phoneNumber, createTime, editTime, deletePinglun);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * 获取评论
     * @param createTime
     * @return
     */
    @RequestMapping("/get/pinglun")
    @PermissionAnno
    public ResponseEntity<List<PingLun>> getPingLun(@RequestParam("createTime")long createTime){
        List<PingLun> luns = planItemService.getPingLun(createTime);
        return new ResponseEntity<List<PingLun>>(luns,HttpStatus.OK);
    }

    /**
     * 管理员删除评论
     */
    @RequestMapping("/deletePinglun")
    public ResponseEntity<?> rootDeletePinglun(@RequestParam("id") int id){
        boolean flag = planItemService.deletePinglun(id);
        return new ResponseEntity<List<PingLun>>(HttpStatus.OK);
    }

    /**
     * 刷新定位字段
     */
    @RequestMapping("/update/location")
    @PermissionAnno
    public ResponseEntity<?> updateLoctation(@RequestParam("phoneNumber")String phoneNumber,
                                             @RequestParam("createTime") long createTime){
        planItemService.updateLocation(phoneNumber, createTime);
        return new ResponseEntity<List<PingLun>>(HttpStatus.OK);
    }
}
