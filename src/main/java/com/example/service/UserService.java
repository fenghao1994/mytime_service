package com.example.service;

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
    public boolean login(User user){
        String sql1 = "SELECT * FROM user WHERE phoneNumber = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user1;
        try{
            user1 = jdbcTemplate.queryForObject(sql1, rowMapper, user.getPhoneNumber());
        }catch (Exception e){
            user1 = null;
        }
        if (user1 != null && user1.getPassword().equals(user.getPassword())){
            return true;
        }
        return false;
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
        String path = jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, String.class);
        return path;
    }
}
