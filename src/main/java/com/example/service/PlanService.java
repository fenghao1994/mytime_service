package com.example.service;

import com.example.bean.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/5/10.
 */
@Service
public class PlanService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void savePlan(Plan plan) {
        String sql = "INSERT INTO plan (id, planId, title, isExpired, isComplete, createTime, editTime, isEdit, phoneNumber) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{plan.getId(), plan.getPlanId(), plan.getTitle(),
                plan.isExpired(), plan.isComplete(), plan.getCreateTime(), plan.getEditTime(),
                plan.isEdit(), plan.getPhoneNumber()});
    }

    public void updatePlan(Plan plan) {
        String sql = "UPDATE plan SET title = ?, isExpired = ?, isComplete = ?," +
                "editTime = ?, isEdit = ? WHERE  phoneNumber = ? AND id = ?";
        jdbcTemplate.update(sql, new Object[]{plan.getTitle(), plan.isExpired(), plan.isComplete(), plan.getEditTime(),
                plan.isEdit(), plan.getPhoneNumber(), plan.getId()});
    }

    public List<Plan> getPlan(String phoneNumber) {
        ArrayList<Plan> arrayList = new ArrayList<>();
        String sql = "SELECT * FROM plan WHERE phoneNumber = ?";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber});

        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                Plan plan = new Plan();
                plan.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                plan.setId((Integer) mapArrayList.get(i).get("id"));
                plan.setPlanId((Long) mapArrayList.get(i).get("planId"));
                plan.setTitle((String) mapArrayList.get(i).get("title"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isExpired");
                if (b1[0] == 0) {
                    plan.setExpired(false);
                } else {
                    plan.setExpired(true);
                }
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isComplete");
                if (b2[0] == 0) {
                    plan.setComplete(false);
                } else {
                    plan.setComplete(true);
                }
                plan.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                plan.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                byte[] b3 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b3[0] == 0) {
                    plan.setEdit(false);
                } else {
                    plan.setEdit(true);
                }
                arrayList.add(plan);
            }
        }
        return arrayList;

    }


    public List<Plan> getAllPlan() {
        ArrayList<Plan> arrayList = new ArrayList<>();
        String sql = "SELECT * FROM plan";

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);

        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                Plan plan = new Plan();
                plan.setIdd((Integer) mapArrayList.get(i).get("idd"));
                plan.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                plan.setId((Integer) mapArrayList.get(i).get("id"));
                plan.setPlanId((Long) mapArrayList.get(i).get("planId"));
                plan.setTitle((String) mapArrayList.get(i).get("title"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isExpired");
                if (b1[0] == 0) {
                    plan.setExpired(false);
                } else {
                    plan.setExpired(true);
                }
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isComplete");
                if (b2[0] == 0) {
                    plan.setComplete(false);
                } else {
                    plan.setComplete(true);
                }
                plan.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                plan.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                byte[] b3 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b3[0] == 0) {
                    plan.setEdit(false);
                } else {
                    plan.setEdit(true);
                }
                arrayList.add(plan);
            }
        }
        return arrayList;
    }

    public boolean deleteFromDisk(int idd) {
        String sql = "DELETE FROM plan WHERE idd = ?";
        jdbcTemplate.update(sql, new Object[]{idd});
        return true;
    }
}
