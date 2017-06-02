package com.example.service;

import com.example.bean.Photo;
import com.example.bean.PlanItem;
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

    public List<PlanItem> getPlanItemWithPlanIdAndPhoneNumber(String phoneNumber, String id) {
        ArrayList<PlanItem> list = new ArrayList<>();
        String sql = "SELECT * FROM planitem WHERE phoneNumber = ? AND id = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber, id});
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
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND objectId = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{list.get(i).getPhoneNumber(), list.get(i).getId(), "1"});
            if (photoMap != null && photoMap.size() > 0){
                for (int j = 0 ; j < photoMap.size(); j++){
                    Photo photo = new Photo();
                    photo.setId((Integer) photoMap.get(j).get("id"));
                    photo.setAddress((String) photoMap.get(j).get("address"));
                    photo.setObjectId((Integer) photoMap.get(j).get("objectId"));
                    photo.setObjectType((Integer) photoMap.get(j).get("objectType"));
                    photos.add(photo);
                }
            }
            list.get(i).setAddress( photos);
        }
        return list;
    }
}
