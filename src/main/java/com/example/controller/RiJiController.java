package com.example.controller;

import com.example.bean.RiJi;
import com.example.service.RiJiService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fenghao on 2017/5/29.
 */

@RestController
@RequestMapping("/riji")
public class RiJiController {

    @Autowired
    private RiJiService riJiService;

    @RequestMapping(value = "/save_riji", method = RequestMethod.POST)
    public ResponseEntity<?> saveRiJi(HttpServletRequest request){
        String phoneNumber = request.getParameter("phoneNumber");
        int id = Integer.parseInt(request.getParameter("id"));
        String content = request.getParameter("content");
        String weather = request.getParameter("weather");
        long createTime = Long.parseLong(request.getParameter("createTime"));

        RiJi riJi = new RiJi();
        riJi.setId(id);
        riJi.setCreateTime(createTime);
        riJi.setContent(content);
        riJi.setPhoneNumber(phoneNumber);
        riJi.setWeather(weather);

        boolean flag = riJiService.saveRiJi(riJi);
        if (flag){
            return new ResponseEntity<String>("{\"msg\":\"保存成功\"}", HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("{\"msg\":\"保存失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getAllRiji", method = RequestMethod.POST)
    public ResponseEntity<List<RiJi>> getAllRiji(HttpServletRequest request){
        String phoneNumber = request.getParameter("phoneNumber");
        List<RiJi> list = new ArrayList<>();
        if (phoneNumber.equals("undefined") || phoneNumber.equals("")) {
            return getRootAllRiji();
        }

        list.clear();
        list.addAll(riJiService.getAllRiji(phoneNumber));
        return new ResponseEntity<List<RiJi>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/root/getAllRiji", method = RequestMethod.POST)
    public ResponseEntity<List<RiJi>> getRootAllRiji(){
        List<RiJi> list = new ArrayList<>();
        list.clear();
        list.addAll(riJiService.getRootAllRiji());
        return new ResponseEntity<List<RiJi>>(list, HttpStatus.OK);
    }

    /**
     * 管理员删除日记
     * @return
     */
    @RequestMapping(value = "/root/deleteRijiFromDisk", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRijiFromDisk(@RequestParam("idd")String idd){
        boolean flag = riJiService.deleteRijiFromDisk(idd);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"删除成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"删除失败\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
