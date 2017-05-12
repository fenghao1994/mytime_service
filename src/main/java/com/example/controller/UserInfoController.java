package com.example.controller;

import com.example.bean.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

/**
 * Created by fenghao on 2017/5/8.
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final ResourceLoader resourceLoader;

    @Autowired
    private UserService userService;

    private static final String ROOT = "pics";


    @Autowired
    public UserInfoController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //登陆
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("password") String password) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        boolean flag = userService.login(user);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"登陆成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"登陆失败，账户错误或者密码错误\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("password") String password) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        boolean flag = userService.register(user);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"注册成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"注册失败，账号已存在\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //重置密码
    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    public ResponseEntity<String> resetPassword(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        boolean flag = userService.resetPassword(user, newPassword);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"修改成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"修改失败，请确认账户和原密码\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //上传头像
    @RequestMapping(value = "/upload/headimg", method = RequestMethod.POST)
    public ResponseEntity<String> uploadHeadImg(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("file") MultipartFile file) {
        boolean flag = userService.updateHeadImg(phoneNumber, file);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"修改头像成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"上传头像失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //获取头像
    @RequestMapping(value = "get/headimg", method = RequestMethod.POST)
    public String getHeadImg(@RequestParam("phoneNumber") String phoneNumber) {
        String path = userService.getHeadImg(phoneNumber);
        return path;
    }
}
