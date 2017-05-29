package com.example.service;

import com.example.bean.RiJi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/5/29.
 */
@Service
@Repository
public class RiJiService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean saveRiJi(RiJi riJi) {
        String sql = "INSERT INTO riji (id, content, createTime, weather, phoneNumber) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{riJi.getId(), riJi.getContent(), riJi.getCreateTime(), riJi.getWeather(), riJi.getPhoneNumber()});
        return true;
    }

    public Collection<? extends RiJi> getAllRiji(String phoneNumber) {
        ArrayList<RiJi> list = new ArrayList<>();
        String sql = "SELECT * FROM riji WHERE phoneNumber = ?";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber});
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0 ; i< mapArrayList.size(); i++){
                RiJi riji = new RiJi();
                riji.setIdd((Integer) mapArrayList.get(i).get("idd"));
                riji.setId((Integer) mapArrayList.get(i).get("id"));
                riji.setContent((String) mapArrayList.get(i).get("content"));
                riji.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                riji.setWeather((String) mapArrayList.get(i).get("weather"));
                riji.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                list.add(riji);
            }
        }
        return list;
    }

    public Collection<? extends RiJi> getRootAllRiji() {
        ArrayList<RiJi> list = new ArrayList<>();
        String sql = "SELECT * FROM riji";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0 ; i< mapArrayList.size(); i++){
                RiJi riji = new RiJi();
                riji.setIdd((Integer) mapArrayList.get(i).get("idd"));
                riji.setId((Integer) mapArrayList.get(i).get("id"));
                riji.setContent((String) mapArrayList.get(i).get("content"));
                riji.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                riji.setWeather((String) mapArrayList.get(i).get("weather"));
                riji.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                list.add(riji);
            }
        }
        return list;
    }

    public boolean deleteRijiFromDisk(String idd) {
        String sql = "DELETE FROM riji WHERE idd = ?";
        jdbcTemplate.update(sql, new Object[]{idd});
        return true;
    }
}
