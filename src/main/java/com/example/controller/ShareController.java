package com.example.controller;

import com.example.annotations.PermissionAnno;
import com.example.bean.Friend;
import com.example.bean.FriendShare;
import com.example.bean.Photo;
import com.example.bean.PlanItem;
import com.example.service.ShareService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/6/1.
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @RequestMapping(value = "/planitem", method = RequestMethod.GET)
    @PermissionAnno
    public String sharePlanItem(@RequestParam("createTime") String createTime, @RequestParam("phoneNumber")String phoneNumber, Model model){
        List<PlanItem> list = shareService.getPlanItemWithPlanIdAndPhoneNumber(phoneNumber, createTime);
        System.out.print(list);
        model.addAttribute("list", list);
        return "share";
    }

    /**
     * 获取朋友分享的计划
     * @param message
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/friend/open", method = RequestMethod.POST)
    @PermissionAnno
    public ResponseEntity<List<FriendShare>> getFriendOpen(@RequestParam("message") String message, @RequestParam("phoneNumber") String phoneNumber){
        List<Map<String, String>> maps = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            maps = objectMapper.readValue(message, new TypeReference<ArrayList<Map<String, String>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<FriendShare> list = shareService.getFriendOpen(maps, phoneNumber);
        return new ResponseEntity<List<FriendShare>>(list, HttpStatus.OK);
    }

    /**
     * 获取朋友列表
     * @param message
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/friends", method = RequestMethod.POST)
    @PermissionAnno
    public ResponseEntity<List<Friend>> getFriends(@RequestParam("message") String message, @RequestParam("phoneNumber") String phoneNumber){
        List<Map<String, String>> maps = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            maps = objectMapper.readValue(message, new TypeReference<ArrayList<Map<String, String>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Friend> list = shareService.getFriends(maps, phoneNumber);
        return new ResponseEntity<List<Friend>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/change/friend/relationship")
    @PermissionAnno
    public ResponseEntity<?> changeFriendsRelationship(@RequestParam("ownPhoneNumber")String ownPhoneNumber,
                                                       @RequestParam("otherPhoneNumber")String otherPhoneNumber,
                                                       @RequestParam("userActive")String userActive) {
        boolean flag = shareService.changeFriendsRelationship(ownPhoneNumber, otherPhoneNumber, userActive);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    /**
     * 获取某个人的公开计划
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/get/someone/share")
    @PermissionAnno
    public ResponseEntity<List<PlanItem>> getSomeoneShare(@RequestParam("phoneNumber") String phoneNumber){
        List<PlanItem> list = shareService.getSomeoneShare(phoneNumber);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
