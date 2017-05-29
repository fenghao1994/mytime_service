package com.example.controller;

import com.example.bean.Note;
import com.example.bean.Photo;
import com.example.service.NoteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/5/8.
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    //保存note
    @RequestMapping("/saveNote")
    public ResponseEntity<String> saveNote(HttpServletRequest request, @RequestParam(name = "addressFiles",required = false)MultipartFile[] files){
        String phoneNumber = request.getParameter("phoneNumber");
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        String address = request.getParameter("address");
        List<MultipartFile> files1 = new ArrayList<>();
        if (files != null && files.length > 0){
            for (int i = 0 ; i< files.length; i++){
                files1.add(files[i]);
            }
        }
        Note note = new Note();
        note.setPhoneNumber(phoneNumber);
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setCreateTime(createTime);
        note.setEditTime(editTime);
        note.setEdit(isEdit);
        note.setDelete(isDelete);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<Photo> list = null;
        try {
            list = objectMapper.readValue(address, new TypeReference<ArrayList<Photo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        note.setAddress(list);
        boolean flag = noteService.saveNote(note, files1);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"保存成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"保存失败\"}", HttpStatus.BAD_REQUEST);
        }
    }
    //更新note
    @RequestMapping("/updateNote")
    public ResponseEntity<String> updateNote(HttpServletRequest request, @RequestParam(name = "addressFiles",required = false)MultipartFile[] files){
        String phoneNumber = request.getParameter("phoneNumber");
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        long createTime = Long.parseLong(request.getParameter("createTime"));
        long editTime = Long.parseLong(request.getParameter("editTime"));
        boolean isEdit = Boolean.parseBoolean(request.getParameter("isEdit"));
        boolean isDelete = Boolean.parseBoolean(request.getParameter("isDelete"));
        String address = request.getParameter("address");
        List<MultipartFile> files1 = new ArrayList<>();
        if (files != null && files.length > 0){
            for (int i = 0 ; i< files.length; i++){
                files1.add(files[i]);
            }
        }

        Note note = new Note();
        note.setPhoneNumber(phoneNumber);
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setCreateTime(createTime);
        note.setEditTime(editTime);
        note.setEdit(isEdit);
        note.setDelete(isDelete);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<Photo> list = null;
        try {
            list = objectMapper.readValue(address, new TypeReference<ArrayList<Photo>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        note.setAddress(list);
        boolean flag = noteService.updateNote(note, files1);
        if (flag) {
            return new ResponseEntity<String>("{\"msg\":\"更新成功\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("{\"msg\":\"更新失败\"}", HttpStatus.BAD_REQUEST);
        }
    }
    //获取note
    @RequestMapping("/getNote")
    public ResponseEntity<List<Note>> getNote(@RequestParam("phoneNumber") String phoneNumber){
        if (phoneNumber.equals("undefined") || phoneNumber.equals("")) {
            return getAllNote();
        }
        List<Note> list = noteService.getNote(phoneNumber);
        return new ResponseEntity<List<Note>>(list, HttpStatus.OK);
    }

    @RequestMapping("/deleteNote")
    @ResponseBody
    public String deleteNote(@RequestParam("phoneNumber")String phoneNumber, @RequestParam("id") String id){
        boolean flag = noteService.deleteNote(phoneNumber, id);
        if (flag){
            return "删除成功";
        }else {
            return "删除失败";
        }
    }

    @RequestMapping(value = "/getAllNote", method = RequestMethod.POST)
    public ResponseEntity<List<Note>> getAllNote(){
        List<Note> list = noteService.getAllNote();
        return new ResponseEntity<List<Note>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/root/deleteNoteFromDisk", method = RequestMethod.POST)
    public ResponseEntity<?> deleteFromDisk(@RequestParam("idd") int idd){
        boolean flag = noteService.deleteFromDisk(idd);
        if (flag){
            return new ResponseEntity<>("{\"msg\":\"删除成功\"}", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("{\"msg\":\"删除失败\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getPhotos", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> getPhotos(@RequestParam("phoneNumber")String phoneNumber, @RequestParam("objectId") String objectId,
    @RequestParam("objectType")String objectType){
        List<Photo> list = noteService.getPhotos(phoneNumber, objectId, objectType);
        if (list != null && list.size() > 0){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("{\"msg\":\"没有存储照片\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deletePhoto", method = RequestMethod.POST)
    public ResponseEntity<?> deletePhoto(@RequestParam("idd") String idd){
        boolean flag = noteService.deletePhoto(idd);
        if (flag){
            return new ResponseEntity<>("{\"msg\":\"删除成功\"}", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("{\"msg\":\"删除失败\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
