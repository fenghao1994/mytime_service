package com.example.service;

import com.example.bean.Photo;
import com.example.bean.PingLun;
import com.example.bean.PlanItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/5/11.
 */
@Service
public class PlanItemService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean savePlanItem(PlanItem planItem, List<MultipartFile> files){
        boolean flag = saveFile(planItem, files);
        if ( !flag){
            return flag;
        }
        String sql = "INSERT INTO planitem (id, planId, title, content, createTime, editTime," +
                "isEdit, phoneNumber, messageContent, messagePhoneNumber, location, phoneNumberLianXi, " +
                "isEveryDay, isManyDays, isExpired, isComplete, isDelete, years, months," +
                "days, hours, minutes, alarmWay, describes, openPlanItem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                "?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{planItem.getId(), planItem.getPlanId(), planItem.getTitle(),
        planItem.getContent(), planItem.getCreateTime(), planItem.getEditTime(), planItem.isEdit(),
        planItem.getPhoneNumber(), planItem.getMessageContent(), planItem.getMessagePhoneNumber(), planItem.getLocation(),
        planItem.getPhoneNumberLianXi(), planItem.isEveryDay(), planItem.isManyDays(), planItem.isExpired(), planItem.isComplete(),
        planItem.isDelete(),planItem.getYears(), planItem.getMonths(), planItem.getDays(), planItem.getHours(), planItem.getMinutes(),
        planItem.getAlarmWay(), planItem.getDescribes(), planItem.getOpen()});

        if (planItem.getAddress() == null){
            return true;
        }
        for (int i = 0 ; i < planItem.getAddress().size(); i++){
            String sql1 = "INSERT INTO photo(id, objectType, objectId, address, phoneNumber, createTime) VALUES(? ,? ,? ,?, ?, ?)";
            jdbcTemplate.update(sql1, new Object[]{planItem.getAddress().get(i).getId(), planItem.getAddress().get(i).getObjectType(),
                    planItem.getAddress().get(i).getObjectId(), planItem.getAddress().get(i).getAddress(), planItem.getPhoneNumber(), planItem.getCreateTime()});
        }
        return true;
    }

    //更新planItem
    public boolean updatePlanItem(PlanItem planItem, List<MultipartFile> files){
        boolean flag = saveFile(planItem, files);
        if (!flag){
            return flag;
        }

        String sql = "UPDATE planitem SET title = ?, content = ?, editTime = ?," +
                "isEdit = ?, messageContent = ?, messagePhoneNumber = ?, location = ?, phoneNumberLianXi = ?, " +
                "isEveryDay = ?, isManyDays = ?, isExpired = ?, isComplete = ?, isDelete = ?, years = ?, months = ?," +
                "days = ?, hours = ?, minutes = ?, alarmWay = ?, describes = ?, openPlanItem = ? WHERE phoneNumber = ? AND createTime = ? ";
        int num = jdbcTemplate.update(sql, new Object[]{planItem.getTitle(),
                planItem.getContent(), planItem.getEditTime(), planItem.isEdit(),
                planItem.getMessageContent(), planItem.getMessagePhoneNumber(), planItem.getLocation(),
                planItem.getPhoneNumberLianXi(), planItem.isEveryDay(), planItem.isManyDays(), planItem.isExpired(), planItem.isComplete(),
                planItem.isDelete(),planItem.getYears(), planItem.getMonths(), planItem.getDays(), planItem.getHours(), planItem.getMinutes(),
                planItem.getAlarmWay(), planItem.getDescribes(), planItem.getOpen(), planItem.getPhoneNumber(), planItem.getCreateTime()});

        String sqlDelete = "DELETE FROM photo WHERE phoneNumber = ? AND objectType = ? AND " +
                "createTime = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{planItem.getPhoneNumber(), "1", planItem.getCreateTime()});

        if (planItem.getAddress() == null){
            return true;
        }
        for (int i = 0 ; i < planItem.getAddress().size(); i++){
            String sql1 = "INSERT INTO photo(id, objectType, objectId, address, phoneNumber, createTime) VALUES(? ,? ,? ,?, ?, ?)";
            jdbcTemplate.update(sql1, new Object[]{planItem.getAddress().get(i).getId(), planItem.getAddress().get(i).getObjectType(),
                    planItem.getAddress().get(i).getObjectId(), planItem.getAddress().get(i).getAddress(), planItem.getPhoneNumber(), planItem.getCreateTime()});
        }
        return true;
    }

    //获取planItem
    public List<PlanItem> getPlanItem(String phoneNumber){
        ArrayList<PlanItem> list = new ArrayList<>();
        String sql = "SELECT * FROM planitem WHERE phoneNumber = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber});
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

    public boolean saveFile(PlanItem planItem, List<MultipartFile> files) {
        if (planItem.getAddress() != null){
            for (int i = 0; i < planItem.getAddress().size(); i++) {
                planItem.getAddress().get(i).setAddress("");
            }
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    String path = "D:\\pics\\" + planItem.getPhoneNumber() + System.currentTimeMillis() + ".png";
                    InputStream inputStrem = null;
                    try {
                        inputStrem = files.get(i).getInputStream();
                        int index = 0;
                        byte[] bytes = new byte[1024];
                        FileOutputStream downloanFile = new FileOutputStream(path);
                        while ((index = inputStrem.read(bytes)) != -1) {
                            downloanFile.write(bytes, 0, index);
                            downloanFile.flush();
                        }
                        planItem.getAddress().get(i).setAddress(path);
                        downloanFile.close();
                        inputStrem.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deletePlanItem(String phoneNumber, long createTime) {
        String sql = "UPDATE planitem SET isDelete = 1 WHERE phoneNumber = ? AND createTime = ?";
        jdbcTemplate.update(sql, new Object[]{phoneNumber, createTime});
        return true;
    }

    public boolean completePlanItem(String phoneNumber, String createTime, String editTime) {
        String sql = "UPDATE planitem SET isComplete = 1, editTime = ?  WHERE phoneNumber = ? AND createTime = ?";
        jdbcTemplate.update(sql, new Object[]{editTime, phoneNumber, createTime});
        return true;
    }

    public List<PlanItem> getPlanItemWithPlanIdAndPhoneNumber(String phoneNumber, String planId) {
        ArrayList<PlanItem> list = new ArrayList<>();
        String sql = "SELECT * FROM planitem WHERE phoneNumber = ? AND planId = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber, planId});
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
                planItem.setOpen((String) mapArrayList.get(i).get("openPlanItem"));
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

    public boolean deletePlanItemFromDisk(String idd) {
        String sql = "DELETE FROM planitem WHERE idd = ?";
        jdbcTemplate.update(sql, new Object[]{idd});
        return true;

    }

    public boolean pingLun(String content, String phoneNumber, long createTime, long editTime, String deletePinglun) {
        String sql = "INSERT INTO pinglun(content, phoneNumber, createTime, editTime, deletePinglun) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{content, phoneNumber, createTime, editTime, deletePinglun});
        return true;
    }

    public List<PingLun> getPingLun(long createTime) {
        ArrayList<PingLun> list = new ArrayList<>();
        String sql = "SELECT * FROM pinglun WHERE createTime = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{createTime});
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0; i < mapArrayList.size(); i++){
                PingLun pingLun = new PingLun();
                pingLun.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                pingLun.setContent((String) mapArrayList.get(i).get("content"));
                pingLun.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                pingLun.setDeletePinglun((String) mapArrayList.get(i).get("deletePinglun"));
                pingLun.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                pingLun.setId((Integer) mapArrayList.get(i).get("id"));
                list.add(pingLun);
            }
        }
        return list;
    }

    /**
     * 管理员删除评论
     * @param id
     * @return
     */
    public boolean deletePinglun(int id) {
        String sql = "DELETE FROM pinglun WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{id});
        return true;
    }
}
