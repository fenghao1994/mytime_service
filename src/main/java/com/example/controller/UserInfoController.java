package com.example.controller;

import com.example.annotations.PermissionAnno;
import com.example.bean.FeedBack;
import com.example.bean.User;
import com.example.configurer.Permission;
import com.example.configurer.UserManager;
import com.example.service.UserService;
import org.apache.catalina.mapper.Mapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.security.pkcs11.P11Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/5/8.
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final ResourceLoader resourceLoader;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    private static final String ROOT = "pics";


    @Autowired
    public UserInfoController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //客户端登陆
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @PermissionAnno
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
    @PermissionAnno
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

    //忘记密码
    @RequestMapping(value = "/forget/password", method = RequestMethod.POST)
    @PermissionAnno
    public ResponseEntity<String> forgetPassword(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("password") String password) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        boolean flag = userService.forgetPassword(user);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"修改成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"修改失败，请确认账户和原密码\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //重置密码
    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    @PermissionAnno
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
    @PermissionAnno
    public ResponseEntity<String> uploadHeadImg(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("file") MultipartFile file) {
        boolean flag = userService.updateHeadImg(phoneNumber, file);
        if (flag) {
            return new ResponseEntity<>("{\"msg\":\"修改头像成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"上传头像失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    //获取头像
    @RequestMapping(value = "/get/headimg", method = RequestMethod.POST)
    @PermissionAnno
    public String getHeadImg(@RequestParam("phoneNumber") String phoneNumber) {
        String path = userService.getHeadImg(phoneNumber);
        return path;
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
    public ResponseEntity<List<User>> getAllUser() {
        ArrayList<User> list = (ArrayList<User>) userService.getAllUser();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("id")int id) {
        boolean flag = userService.deleteUser(id);
        if (flag) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    @RequestMapping(value = "/root/resetPassword", method = RequestMethod.POST)
    public String resetPasswordFromRoot(@RequestParam("id") int id, @RequestParam("password")String password) {
        boolean flag = userService.resetPasswordFromRoot(id, password);
        if (flag) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    @RequestMapping(value = "/root/getUser", method = RequestMethod.POST)
    public  ResponseEntity<List<User>> rootGetUser(@RequestParam("phoneNumber") String phoneNumber) {
        if (phoneNumber.equals("undefined") || phoneNumber.equals("")) {
            return getAllUser();
        }

        User user = userService.rootGetUser(phoneNumber);
        List<User> list = new ArrayList<>();
        list.add(user);

        Boolean isLogin = (Boolean) request.getSession().getAttribute("isLogin");
        if (isLogin != null && isLogin){
            return new ResponseEntity<List<User>>(list, HttpStatus.OK);
        }else {
            return new ResponseEntity<List<User>>(list, HttpStatus.BAD_REQUEST);
        }


    }


    //管理员
    @RequestMapping(value = "/root/login", method = RequestMethod.POST)
    @PermissionAnno
    public ResponseEntity<?> rootLogin(){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username.equals("admin") && password.equals("admin")){
            request.getSession().setAttribute("isLogin", true);
            return new ResponseEntity<Object>(HttpStatus.OK);
        }else {
            request.getSession().setAttribute("isLogin", false);
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户上传反馈
     * @return
     */
    @RequestMapping(value = "/feed_back" ,method = RequestMethod.POST)
    @PermissionAnno
    public ResponseEntity<?> feedBack(){
        String phoneNumber = request.getParameter("phoneNumber");
        long createTime = Long.parseLong(request.getParameter("createTime"));
        String feedback = request.getParameter("feedback");
        userService.feedBack(phoneNumber, createTime, feedback);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    /**
     * 服务管理端获取所有用户反馈
     * @return
     */
    @RequestMapping(value = "/root/get/feed_back", method = RequestMethod.POST)
    public List<FeedBack> getAllFeedBack(){
        List<FeedBack> list = new ArrayList<>();
        list = userService.getAllFeedBack();
        return list;
    }

    /**
     * 删除用户反馈
     */
    @RequestMapping(value = "/root/delete/feedback", method = RequestMethod.POST)
    public ResponseEntity<?> deleteFeedBack(@RequestParam("id")int id){
        boolean flag = userService.deleteFeedBack(id);
        if (flag) {
            return new ResponseEntity<Object>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 服务端管理通过手机号获取反馈
     * @param phoneNumber
     * @return
     */
    @RequestMapping(value = "/getfeedbackwithPhoneNumber", method = RequestMethod.POST)
    public  ResponseEntity<List<FeedBack>> getFeedBackWithPhoneNumber(@RequestParam("phoneNumber")String phoneNumber){
        List<FeedBack> list = new ArrayList<>();
        if (phoneNumber.equals("undefined") || phoneNumber.equals("")){
            list = getAllFeedBack();
        }else {
            list = userService.getFeedBackWithPhoneNumber(phoneNumber);
        }
        return new ResponseEntity<List<FeedBack>>(list, HttpStatus.OK);
    }
}
