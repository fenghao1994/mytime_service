package com.example.service;

import com.example.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/6/1.
 */
@Service
@Repository
public class ShareService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    public List<PlanItem> getPlanItemWithPlanIdAndPhoneNumber(String phoneNumber, String createTime) {
        ArrayList<PlanItem> list = new ArrayList<>();
        String sql = "SELECT * FROM planitem WHERE phoneNumber = ? AND createTime = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber, createTime});
        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                PlanItem planItem = new PlanItem();
                planItem.setIdd((Integer) mapArrayList.get(i).get("idd"));
                planItem.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                planItem.setId((Integer) mapArrayList.get(i).get("id"));
                planItem.setPlanId((Long) mapArrayList.get(i).get("planId"));
                planItem.setTitle((String) mapArrayList.get(i).get("title"));
                planItem.setContent((String) mapArrayList.get(i).get("content"));
                planItem.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                planItem.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b1[0] == 0){
                    planItem.setEdit(false);
                }else {
                    planItem.setEdit(true);
                }
                planItem.setMessageContent((String) mapArrayList.get(i).get("messageContent"));
                planItem.setMessagePhoneNumber((String) mapArrayList.get(i).get("messagePhoneNumber"));
                planItem.setLocation((String) mapArrayList.get(i).get("location"));
                planItem.setPhoneNumberLianXi((String) mapArrayList.get(i).get("phoneNumberLianXi"));
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isEveryDay");
                if (b2[0] == 0){
                    planItem.setEveryDay(false);
                }else {
                    planItem.setEveryDay(true);
                }
                byte[] b3 = (byte[]) mapArrayList.get(i).get("isManyDays");
                if (b3[0] == 0){
                    planItem.setManyDays(false);
                }else {
                    planItem.setManyDays(true);
                }

                byte[] b4 = (byte[]) mapArrayList.get(i).get("isExpired");
                if (b4[0] == 0){
                    planItem.setExpired(false);
                }else {
                    planItem.setExpired(true);
                }

                byte[] b5 = (byte[]) mapArrayList.get(i).get("isComplete");
                if (b5[0] == 0){
                    planItem.setComplete(false);
                }else {
                    planItem.setComplete(true);
                }
                byte[] b6 = (byte[]) mapArrayList.get(i).get("isDelete");
                if (b6[0] == 0){
                    planItem.setDelete(false);
                }else {
                    planItem.setDelete(true);
                }
                planItem.setYears((Integer) mapArrayList.get(i).get("years"));
                planItem.setMonths((Integer) mapArrayList.get(i).get("months"));
                planItem.setDays((Integer) mapArrayList.get(i).get("days"));
                planItem.setHours((Integer) mapArrayList.get(i).get("hours"));
                planItem.setMinutes((Integer) mapArrayList.get(i).get("minutes"));
                planItem.setAlarmWay((Integer) mapArrayList.get(i).get("alarmWay"));
                planItem.setDescribes((String) mapArrayList.get(i).get("describes"));
                list.add(planItem);
            }
        }


        for (int i = 0; i< list.size() ;i++){
            ArrayList<Photo> photos = new ArrayList<>();
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND createTime = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{list.get(i).getPhoneNumber(), list.get(i).getCreateTime(), "1"});
            if (photoMap != null && photoMap.size() > 0){
                for (int j = 0 ; j < photoMap.size(); j++){
                    Photo photo = new Photo();
                    photo.setId((Integer) photoMap.get(j).get("id"));
                    photo.setAddress((String) photoMap.get(j).get("address"));
                    photo.setObjectId((Integer) photoMap.get(j).get("objectId"));
                    photo.setObjectType((Integer) photoMap.get(j).get("objectType"));
                    photo.setCreateTime((Long) photoMap.get(j).get("createTime"));
                    photos.add(photo);
                }
            }
            list.get(i).setAddress( photos);
        }
        return list;
    }

    public List<FriendShare> getFriendOpen(List<Map<String, String>> maps, String phoneNumber) {

        List<User> list = userService.getAllUser();
        List<String> phoneNumberList = new ArrayList<>();
        //将所有的用户的电话号放入list  contains比对时需要string类型
        for (int i = 0; i < maps.size(); i++){
            phoneNumberList.add(maps.get(i).get("phoneNumber"));
        }
        List<User> friendList = new ArrayList<>();
        //将所有的朋友放入friendList中
        for (int i = 0 ; i < list.size(); i++){
            if (phoneNumberList.contains(list.get(i).getPhoneNumber())){
                friendList.add(list.get(i));
            }
        }
        //如果friendlilst中有自己的号码 则删掉
        for (int i = 0; i < friendList.size(); i++){
            if (friendList.get(i).getPhoneNumber().equals(phoneNumber)){
                friendList.remove(i);
            }
        }
        List<FriendShare> listFriendShare = getFriendOpenPlanItem(friendList);
        for (int i = 0 ;i < listFriendShare.size(); i++){
            String sql = "SELECT * FROM userrelationshiip WHERE ownPhoneNumber = " + phoneNumber + "  AND otherPhoneNumber = " + listFriendShare.get(i).getPlanItem().getPhoneNumber();
            List<Map<String, Object>> mapArrayList = new ArrayList<>();
            mapArrayList = jdbcTemplate.queryForList(sql);
            if (mapArrayList != null && mapArrayList.size() > 0) {
                for (int j = 0; j< mapArrayList.size(); j++){
                    String str = (String) mapArrayList.get(j).get("userActive");
                    if (str.equals("HITE")){
                        listFriendShare.remove(i);
                        break;
                    }
                }
            }
        }

        return listFriendShare;
    }

    public List<Friend> getFriends(List<Map<String, String>> maps, String phoneNumber) {
        List<User> list = userService.getAllUser();
        List<String> phoneNumberList = new ArrayList<>();
        //将所有的用户的电话号放入list  contains比对时需要string类型
        for (int i = 0; i < maps.size(); i++){
            phoneNumberList.add(maps.get(i).get("phoneNumber"));
        }
        List<User> friendList = new ArrayList<>();
        //将所有的朋友放入friendList中
        for (int i = 0 ; i < list.size(); i++){
            if (phoneNumberList.contains(list.get(i).getPhoneNumber())){
                friendList.add(list.get(i));
            }
        }
        //如果friendlilst中有自己的号码 则删掉
        for (int i = 0; i < friendList.size(); i++){
            if (friendList.get(i).getPhoneNumber().equals(phoneNumber)){
                friendList.remove(i);
            }
        }
        List<Friend> friendRelationship = new ArrayList<>();
        //找出与朋友的关系  关注还是屏蔽还是默认
        for (int i = 0; i < friendList.size(); i++){
            String active = "WATCH";
            String sql = "SELECT * FROM userrelationshiip WHERE ownPhoneNumber = " + phoneNumber + "  AND otherPhoneNumber = " + friendList.get(i).getPhoneNumber();

            List<Map<String, Object>> mapArrayList = new ArrayList<>();
            mapArrayList = jdbcTemplate.queryForList(sql);

            if (mapArrayList != null && mapArrayList.size() > 0) {
                active = (String) mapArrayList.get(0).get("userActive");
            }

            Friend friend = new Friend();
            friend.setUser( friendList.get(i));
            friend.setUserActive(active);
            friendRelationship.add(friend);
        }


        return friendRelationship;
    }

    public List<FriendShare> getFriendOpenPlanItem(List<User> friendList) {
        List<FriendShare> friendShareList = new ArrayList<>();
        for (int j = 0; j < friendList.size(); j++) {
            String sql = "SELECT * FROM planitem WHERE phoneNumber = ? AND openPlanItem = ?";
            List<Map<String, Object>> mapArrayList = new ArrayList<>();
            mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{friendList.get(j).getPhoneNumber(), "OPEN"});
            if (mapArrayList != null && mapArrayList.size() > 0) {
                for (int i = 0; i < mapArrayList.size(); i++) {
                    FriendShare friendShare = new FriendShare();
                    PlanItem planItem = new PlanItem();
                    planItem.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                    planItem.setId((Integer) mapArrayList.get(i).get("id"));
                    planItem.setPlanId((Long) mapArrayList.get(i).get("planId"));
                    planItem.setTitle((String) mapArrayList.get(i).get("title"));
                    planItem.setContent((String) mapArrayList.get(i).get("content"));
                    planItem.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                    planItem.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                    planItem.setOpen((String) mapArrayList.get(i).get("openPlanItem"));
                    byte[] b1 = (byte[]) mapArrayList.get(i).get("isEdit");
                    if (b1[0] == 0) {
                        planItem.setEdit(false);
                    } else {
                        planItem.setEdit(true);
                    }
                    planItem.setMessageContent((String) mapArrayList.get(i).get("messageContent"));
                    planItem.setMessagePhoneNumber((String) mapArrayList.get(i).get("messagePhoneNumber"));
                    planItem.setLocation((String) mapArrayList.get(i).get("location"));
                    planItem.setPhoneNumberLianXi((String) mapArrayList.get(i).get("phoneNumberLianXi"));
                    byte[] b2 = (byte[]) mapArrayList.get(i).get("isEveryDay");
                    if (b2[0] == 0) {
                        planItem.setEveryDay(false);
                    } else {
                        planItem.setEveryDay(true);
                    }
                    byte[] b3 = (byte[]) mapArrayList.get(i).get("isManyDays");
                    if (b3[0] == 0) {
                        planItem.setManyDays(false);
                    } else {
                        planItem.setManyDays(true);
                    }

                    byte[] b4 = (byte[]) mapArrayList.get(i).get("isExpired");
                    if (b4[0] == 0) {
                        planItem.setExpired(false);
                    } else {
                        planItem.setExpired(true);
                    }

                    byte[] b5 = (byte[]) mapArrayList.get(i).get("isComplete");
                    if (b5[0] == 0) {
                        planItem.setComplete(false);
                    } else {
                        planItem.setComplete(true);
                    }
                    byte[] b6 = (byte[]) mapArrayList.get(i).get("isDelete");
                    if (b6[0] == 0) {
                        planItem.setDelete(false);
                    } else {
                        planItem.setDelete(true);
                    }
                    planItem.setYears((Integer) mapArrayList.get(i).get("years"));
                    planItem.setMonths((Integer) mapArrayList.get(i).get("months"));
                    planItem.setDays((Integer) mapArrayList.get(i).get("days"));
                    planItem.setHours((Integer) mapArrayList.get(i).get("hours"));
                    planItem.setMinutes((Integer) mapArrayList.get(i).get("minutes"));
                    planItem.setAlarmWay((Integer) mapArrayList.get(i).get("alarmWay"));
                    planItem.setDescribes((String) mapArrayList.get(i).get("describes"));

                    friendShare.setPlanItem(planItem);
                    friendShare.setUser(friendList.get(j));
                    friendShareList.add(friendShare);
                }
            }
        }

        for (int i = 0; i < friendShareList.size(); i++) {
            ArrayList<Photo> photos = new ArrayList<>();
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND createTime = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{friendShareList.get(i).getPlanItem().getPhoneNumber(), friendShareList.get(i).getPlanItem().getCreateTime(), "1"});
            if (photoMap != null && photoMap.size() > 0) {
                for (int j = 0; j < photoMap.size(); j++) {
                    Photo photo = new Photo();
                    photo.setId((Integer) photoMap.get(j).get("id"));
                    photo.setAddress((String) photoMap.get(j).get("address"));
                    photo.setObjectId((Integer) photoMap.get(j).get("objectId"));
                    photo.setObjectType((Integer) photoMap.get(j).get("objectType"));
                    photo.setCreateTime((Long) photoMap.get(j).get("createTime"));
                    photos.add(photo);
                }
            }
            friendShareList.get(i).getPlanItem().setAddress(photos);
        }
        return friendShareList;
    }


    public boolean changeFriendsRelationship(String ownPhoneNumber, String otherPhoneNumber, String userActive) {
        String sql = "SELECT * FROM userrelationshiip WHERE ownPhoneNumber = " + ownPhoneNumber + "  AND otherPhoneNumber = " + otherPhoneNumber;
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);
        if (mapArrayList != null && mapArrayList.size() > 0) {
            String sql1 = "UPDATE userrelationshiip SET userActive = ? WHERE ownPhoneNumber = ? AND otherPhoneNumber = ?";
            jdbcTemplate.update(sql1, new Object[]{userActive, ownPhoneNumber, otherPhoneNumber});
        }else {
            String sql1 = "INSERT INTO userrelationshiip(ownPhoneNumber, otherPhoneNumber, userActive) VALUES(?, ?, ?)";
            jdbcTemplate.update(sql1, new Object[]{ownPhoneNumber, otherPhoneNumber, userActive});
        }
        return true;
    }

    public List<PlanItem> getSomeoneShare(String phoneNumber) {
        List<PlanItem> list = new ArrayList<>();
        String sql = "SELECT * FROM planitem WHERE phoneNumber = ? AND openPlanItem = ?";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber, "OPEN"});
        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                PlanItem planItem = new PlanItem();
                planItem.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                planItem.setId((Integer) mapArrayList.get(i).get("id"));
                planItem.setPlanId((Long) mapArrayList.get(i).get("planId"));
                planItem.setTitle((String) mapArrayList.get(i).get("title"));
                planItem.setContent((String) mapArrayList.get(i).get("content"));
                planItem.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                planItem.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                planItem.setOpen((String) mapArrayList.get(i).get("openPlanItem"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b1[0] == 0) {
                    planItem.setEdit(false);
                } else {
                    planItem.setEdit(true);
                }
                planItem.setMessageContent((String) mapArrayList.get(i).get("messageContent"));
                planItem.setMessagePhoneNumber((String) mapArrayList.get(i).get("messagePhoneNumber"));
                planItem.setLocation((String) mapArrayList.get(i).get("location"));
                planItem.setPhoneNumberLianXi((String) mapArrayList.get(i).get("phoneNumberLianXi"));
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isEveryDay");
                if (b2[0] == 0) {
                    planItem.setEveryDay(false);
                } else {
                    planItem.setEveryDay(true);
                }
                byte[] b3 = (byte[]) mapArrayList.get(i).get("isManyDays");
                if (b3[0] == 0) {
                    planItem.setManyDays(false);
                } else {
                    planItem.setManyDays(true);
                }

                byte[] b4 = (byte[]) mapArrayList.get(i).get("isExpired");
                if (b4[0] == 0) {
                    planItem.setExpired(false);
                } else {
                    planItem.setExpired(true);
                }

                byte[] b5 = (byte[]) mapArrayList.get(i).get("isComplete");
                if (b5[0] == 0) {
                    planItem.setComplete(false);
                } else {
                    planItem.setComplete(true);
                }
                byte[] b6 = (byte[]) mapArrayList.get(i).get("isDelete");
                if (b6[0] == 0) {
                    planItem.setDelete(false);
                } else {
                    planItem.setDelete(true);
                }
                planItem.setYears((Integer) mapArrayList.get(i).get("years"));
                planItem.setMonths((Integer) mapArrayList.get(i).get("months"));
                planItem.setDays((Integer) mapArrayList.get(i).get("days"));
                planItem.setHours((Integer) mapArrayList.get(i).get("hours"));
                planItem.setMinutes((Integer) mapArrayList.get(i).get("minutes"));
                planItem.setAlarmWay((Integer) mapArrayList.get(i).get("alarmWay"));
                planItem.setDescribes((String) mapArrayList.get(i).get("describes"));

                list.add(planItem);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Photo> photos = new ArrayList<>();
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND createTime = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{list.get(i).getPhoneNumber(), list.get(i).getCreateTime(), "1"});
            if (photoMap != null && photoMap.size() > 0) {
                for (int j = 0; j < photoMap.size(); j++) {
                    Photo photo = new Photo();
                    photo.setId((Integer) photoMap.get(j).get("id"));
                    photo.setAddress((String) photoMap.get(j).get("address"));
                    photo.setObjectId((Integer) photoMap.get(j).get("objectId"));
                    photo.setObjectType((Integer) photoMap.get(j).get("objectType"));
                    photo.setCreateTime((Long) photoMap.get(j).get("createTime"));
                    photos.add(photo);
                }
            }
            list.get(i).setAddress(photos);
        }
        return list;
    }
}
