package com.example.service;

import com.example.bean.FeedBack;
import com.example.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/5/9.
 */
@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //注册
    public boolean register(User user){
        /**
         * true。表示注册成功
         * false。表示注册失败(phoneNumber已经存在)
         *
         * 1.先查找是否存在这个phonenumber
         * 2、存在则返回已经注册过
         * 3、不存在则写入数据库，注册
         */
        String sql1 = "SELECT * FROM user WHERE phoneNumber = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user1;
        try{
            user1 = jdbcTemplate.queryForObject(sql1, rowMapper, user.getPhoneNumber());
        }catch (Exception e){
            user1 = null;
        }

        if (user1 != null){
            return false;
        }
        String sql2 = "INSERT INTO user (phoneNumber, password, headImg) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql2, new Object[]{user.getPhoneNumber(), user.getPassword(), user.getHeadImg()});
        return true;
    }

    //登陆
    public User login(User user){
        String sql1 = "SELECT * FROM user WHERE phoneNumber = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user1;
        try{
            user1 = jdbcTemplate.queryForObject(sql1, rowMapper, user.getPhoneNumber());
        }catch (Exception e){
            user1 = null;
        }
        if (user1 != null && user1.getPassword().equals(user.getPassword())){
            List<String> listString = new ArrayList<>();
            String sql = "SELECT * FROM userlabel WHERE phoneNumber = ?";
            List<Map<String, Object>> mapArrayList = new ArrayList<>();
            mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{user1.getPhoneNumber()});
            if (mapArrayList != null && mapArrayList.size() > 0){
                for (int i = 0; i < mapArrayList.size(); i++){
                    String str = (String) mapArrayList.get(i).get("label");
                    listString.add(str);
                }
            }
            user1.setLabel(listString);
            return user1;
        }
        return null;
    }

    //重置密码
    public boolean resetPassword(User user, String newPassword){
        String sql1 = "SELECT * FROM user WHERE phoneNumber = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user1;
        try{
            user1 = jdbcTemplate.queryForObject(sql1, rowMapper, user.getPhoneNumber());
        }catch (Exception e){
            user1 = null;
        }
        if (user1 == null || !user1.getPassword().equals(user.getPassword())){
            return false;
        }
        String sql2 = "UPDATE user SET password = ? WHERE phoneNumber = ?";
        jdbcTemplate.update(sql2, new Object[]{newPassword, user.getPhoneNumber()});
        return true;
    }

    //上传头像
    public boolean updateHeadImg(String phoneNumber, MultipartFile file){
        String path = "D:\\pics\\" + phoneNumber + System.currentTimeMillis()+".png";
        try {
            InputStream inputStrem = file.getInputStream();
            int index = 0;
            byte[] bytes = new byte[1024];
            FileOutputStream downloanFile = new FileOutputStream(path);
            while ((index = inputStrem.read(bytes)) != -1){
                downloanFile.write(bytes, 0, index);
                downloanFile.flush();
            }
            downloanFile.close();
            inputStrem.close();
            String sql = "UPDATE user SET headImg = ? WHERE phoneNumber = ?";
            jdbcTemplate.update(sql, new Object[]{path, phoneNumber});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    //获取头像
    public String getHeadImg(String phoneNumber){
        String sql = "SELECT headImg FROM user WHERE phoneNumber = ?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{phoneNumber});
        String path = (String) map.get("headImg");
        return path;
    }

    public List<User> getAllUser() {
        ArrayList<User> allUser = new ArrayList<>();
        String sql = "SELECT * FROM user";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0 ; i< mapArrayList.size(); i++) {
                User user = new User();
                user.setId((Integer) mapArrayList.get(i).get("id"));
                user.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                user.setHeadImg((String) mapArrayList.get(i).get("headImg"));
                user.setUserName((String) mapArrayList.get(i).get("userName"));
                user.setSelfIntroduction((String) mapArrayList.get(i).get("selfIntroduction"));
                user.setPersonalizedSignature((String) mapArrayList.get(i).get("personalizedSignature"));
                allUser.add(user);
            }
        }

        //查询label
        for (int i = 0; i < allUser.size(); i++){
            String sql1 = "SELECT * FROM userlabel WHERE phoneNumber = ?";
            List<Map<String, Object>> mapArrayList1 = new ArrayList<>();
            mapArrayList1 = jdbcTemplate.queryForList(sql1, new Object[]{allUser.get(i).getPhoneNumber()});
            List<String> stringList = new ArrayList<>();
            if (mapArrayList1 != null && mapArrayList1.size() > 0){
                for (int j = 0; j < mapArrayList1.size(); j++){
                    String str2 = (String) mapArrayList1.get(j).get("label");
                    stringList.add(str2);
                }
            }
            allUser.get(i).setLabel(stringList);
        }

        return allUser;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{id});
        return true;
    }

    public boolean resetPasswordFromRoot(int id, String password) {
        String sql2 = "UPDATE user SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql2, new Object[]{password, id});
        return true;
    }

    public User rootGetUser(String phoneNumber) {
        String sql = "SELECT * FROM user WHERE phoneNumber = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user;
        try{
            user = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{phoneNumber});
        }catch (Exception e){
            user = null;
        }
        return user;
    }

    /**
     * 用户提交建议
     * @param phoneNumber
     * @param createTime
     * @param feedback
     */
    public void feedBack(String phoneNumber, long createTime, String feedback){
        String sql = "INSERT INTO feedback (phoneNumber, createTime, feedback) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{phoneNumber, createTime, feedback});
    }

    public List<FeedBack> getAllFeedBack(){
        ArrayList<FeedBack> arrayList = new ArrayList<>();
        String sql = "SELECT * FROM feedback";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);
        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                FeedBack feedBack = new FeedBack();
                feedBack.setId((Integer) mapArrayList.get(i).get("id"));
                feedBack.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                feedBack.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                feedBack.setFeedBack((String) mapArrayList.get(i).get("feedback"));
                arrayList.add(feedBack);
            }
        }
        return arrayList;
    }


    /**
     * 删除反馈
     * @param id
     * @return
     */
    public boolean deleteFeedBack(int id) {
        String sql = "DELETE FROM feedback WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{id});
        return true;
    }


    /**
     * 通过手机号获取用户反馈
     * @param phoneNumber
     * @return
     */
    public List<FeedBack> getFeedBackWithPhoneNumber(String phoneNumber) {
        ArrayList<FeedBack> arrayList = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE phoneNumber = ?";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber});
        if (mapArrayList != null && mapArrayList.size() > 0) {
            for (int i = 0; i < mapArrayList.size(); i++) {
                FeedBack feedBack = new FeedBack();
                feedBack.setId((Integer) mapArrayList.get(i).get("id"));
                feedBack.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                feedBack.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                feedBack.setFeedBack((String) mapArrayList.get(i).get("feedback"));
                arrayList.add(feedBack);
            }
        }
        return arrayList;
    }

    public boolean forgetPassword(User user) {
        String sql = "UPDATE user SET password = ? WHERE phoneNumber = ?";
        jdbcTemplate.update(sql, new Object[]{user.getPassword(), user.getPhoneNumber()});
        return true;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public boolean updateUserInfo(User user) {
        String sql = "UPDATE user SET personalizedSignature = ?, selfIntroduction = ?, userName = ? WHERE phoneNumber = ?";
        jdbcTemplate.update(sql, new Object[]{user.getPersonalizedSignature(),
                user.getSelfIntroduction(), user.getUserName(), user.getPhoneNumber()});
        return true;
    }

    /**
     * 保存用户的label
     * @param phoneNumber
     * @param label
     */
    public boolean saveUserLabel(String phoneNumber, String label) {
        String sql = "INSERT INTO userlabel(phoneNumber, label) VALUES (?, ?)";
        jdbcTemplate.update(sql, new Object[]{phoneNumber, label});
        return true;
    }


    /**
     * 删除用户的label
     * @param phoneNumber
     * @param label
     * @return
     */
    public boolean deleteUserLabel(String phoneNumber, String label) {
        String sql = "DELETE FROM userlabel WHERE phoneNumber = ? AND label = ?";
        jdbcTemplate.update(sql, new Object[]{phoneNumber, label});
        return true;
    }
}
