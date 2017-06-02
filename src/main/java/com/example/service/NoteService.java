package com.example.service;

import com.example.bean.Note;
import com.example.bean.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
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
 * Created by fenghao on 2017/5/10.
 */
@Service
@Repository
public class NoteService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private List<Note> allNote;

    //保存note
    public boolean saveNote(Note note, List<MultipartFile> files){
        boolean flag = saveFile(note, files);
        if ( !flag){
            return flag;
        }

        String sql = "INSERT INTO note (id, title, content, createTime, editTime, isEdit, isDelete, phoneNumber) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{note.getId(), note.getTitle(), note.getContent(), note.getCreateTime(),
        note.getEditTime(), note.isEdit(), note.isDelete(), note.getPhoneNumber()});

        if (note.getAddress() == null){
            return true;
        }
        for (int i = 0 ; i < note.getAddress().size(); i++){
            String sql1 = "INSERT INTO photo(id, objectType, objectId, address, phoneNumber, createTime) VALUES(? ,? ,? ,?, ?, ?)";
            jdbcTemplate.update(sql1, new Object[]{note.getAddress().get(i).getId(), note.getAddress().get(i).getObjectType(),
                    note.getAddress().get(i).getObjectId(), note.getAddress().get(i).getAddress(), note.getPhoneNumber(), note.getCreateTime()});
        }
        return true;

    }
    //更新note
    public boolean updateNote(Note note, List<MultipartFile> files){
        boolean flag = saveFile(note, files);
        if (!flag){
            return flag;
        }
        String sql = "UPDATE note SET title = ?, content = ?, editTime = ?, isEdit = ?," +
                " isDelete = ? WHERE phoneNumber = ? AND createTime = ?" ;
        jdbcTemplate.update(sql, new Object[]{note.getTitle(), note.getContent(),
                note.getEditTime(), note.isEdit(), note.isDelete(), note.getPhoneNumber(), note.getCreateTime()});

        String sqlDelete = "DELETE FROM photo WHERE phoneNumber = ? AND objectType = ? AND " +
                "createTime = ?";
        jdbcTemplate.update(sqlDelete, new Object[]{note.getPhoneNumber(), "2", note.getCreateTime()});

        if (note.getAddress() == null){
            return true;
        }
        for (int i = 0 ; i < note.getAddress().size(); i++){
            String sql1 = "INSERT INTO photo(id, objectType, objectId, address, phoneNumber, createTime) VALUES(? ,? ,? ,?, ?, ?)";
            jdbcTemplate.update(sql1, new Object[]{note.getAddress().get(i).getId(), note.getAddress().get(i).getObjectType(),
                    note.getAddress().get(i).getObjectId(), note.getAddress().get(i).getAddress(), note.getPhoneNumber(), note.getCreateTime()});
        }
        return true;
    }


    //获取note
    public List<Note> getNote(String phoneNumber){
        ArrayList<Note> list = new ArrayList<>();
        String sql = "SELECT * FROM note WHERE phoneNumber = ?";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber});
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0 ; i< mapArrayList.size(); i++){
                Note note = new Note();
                note.setIdd((Integer) mapArrayList.get(i).get("idd"));
                note.setId((Integer) mapArrayList.get(i).get("id"));
                note.setTitle((String) mapArrayList.get(i).get("title"));
                note.setContent((String) mapArrayList.get(i).get("content"));
                note.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                note.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                note.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b1[0] == 0){
                    note.setEdit(false);
                }else {
                    note.setEdit(true);
                }
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isDelete");
                if (b2[0] == 0){
                    note.setDelete(false);
                }else {
                    note.setDelete(true);
                }
                list.add(note);
            }
        }
        for (int i = 0; i< list.size() ;i++){
            ArrayList<Photo> photos = new ArrayList<>();
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND createTime = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{list.get(i).getPhoneNumber(), list.get(i).getCreateTime(), "2"});
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

    public boolean deleteNote(String phoneNumber, String id){
        String sql = "UPDATE note SET isDelete = 1 WHERE phoneNumber = ? AND id = ?";
        jdbcTemplate.update(sql, new Object[]{phoneNumber, id});
        return true;
    }

    public boolean saveFile(Note note, List<MultipartFile> files){
        if (note.getAddress() != null){
            for (int i = 0 ; i < note.getAddress().size(); i++){
                note.getAddress().get(i).setAddress("");
            }
            if (files != null && files.size() > 0){
                for (int i = 0 ;i < files.size(); i++){
                    String path = "D:\\pics\\" + note.getPhoneNumber() + System.currentTimeMillis()+".png";
                    InputStream inputStrem = null;
                    try {
                        inputStrem = files.get(i).getInputStream();
                        int index = 0;
                        byte[] bytes = new byte[1024];
                        FileOutputStream downloanFile = new FileOutputStream(path);
                        while ((index = inputStrem.read(bytes)) != -1){
                            downloanFile.write(bytes, 0, index);
                            downloanFile.flush();
                        }
                        note.getAddress().get(i).setAddress(path);
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

    public List<Note> getAllNote() {
        ArrayList<Note> list = new ArrayList<>();
        String sql = "SELECT * FROM note";
        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        mapArrayList = jdbcTemplate.queryForList(sql);
        if (mapArrayList != null && mapArrayList.size() > 0){
            for (int i = 0 ; i< mapArrayList.size(); i++){
                Note note = new Note();
                note.setIdd((Integer) mapArrayList.get(i).get("idd"));
                note.setId((Integer) mapArrayList.get(i).get("id"));
                note.setTitle((String) mapArrayList.get(i).get("title"));
                note.setContent((String) mapArrayList.get(i).get("content"));
                note.setCreateTime((Long) mapArrayList.get(i).get("createTime"));
                note.setEditTime((Long) mapArrayList.get(i).get("editTime"));
                note.setPhoneNumber((String) mapArrayList.get(i).get("phoneNumber"));
                byte[] b1 = (byte[]) mapArrayList.get(i).get("isEdit");
                if (b1[0] == 0){
                    note.setEdit(false);
                }else {
                    note.setEdit(true);
                }
                byte[] b2 = (byte[]) mapArrayList.get(i).get("isDelete");
                if (b2[0] == 0){
                    note.setDelete(false);
                }else {
                    note.setDelete(true);
                }
                list.add(note);
            }
        }
        for (int i = 0; i< list.size() ;i++){
            ArrayList<Photo> photos = new ArrayList<>();
            String sql1 = "SELECT * FROM photo WHERE phoneNumber = ? AND objectId = ? AND objectType = ?";
            List<Map<String, Object>> photoMap = new ArrayList<>();
            photoMap = jdbcTemplate.queryForList(sql1, new Object[]{list.get(i).getPhoneNumber(), list.get(i).getId(), "2"});
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

    //物理删除
    public boolean deleteFromDisk(int idd) {
        String sql = "DELETE FROM note WHERE idd = ?";
        jdbcTemplate.update(sql, new Object[]{idd});
        return true;
    }

    public List<Photo> getPhotos(String phoneNumber, String objectId, String objectType) {
        ArrayList<Photo> photos = new ArrayList<>();
        String sql = "SELECT * FROM photo WHERE phoneNumber = ? AND objectId = ? AND objectType = ?";
        List<Map<String, Object>> photoMap = new ArrayList<>();
        photoMap = jdbcTemplate.queryForList(sql, new Object[]{phoneNumber, objectId, objectType});
        if (photoMap != null && photoMap.size() > 0){
            for (int j = 0 ; j < photoMap.size(); j++){
                Photo photo = new Photo();
                photo.setPhoneNumber((String) photoMap.get(j).get("phoneNumber"));
                photo.setIdd((Integer) photoMap.get(j).get("idd"));
                photo.setId((Integer) photoMap.get(j).get("id"));
                photo.setAddress((String) photoMap.get(j).get("address"));
                photo.setObjectId((Integer) photoMap.get(j).get("objectId"));
                photo.setObjectType((Integer) photoMap.get(j).get("objectType"));
                photo.setCreateTime((Long) photoMap.get(j).get("createTime"));
                photos.add(photo);
            }
        }
        return photos;
    }

    public boolean deletePhoto(String idd) {
        String sql = "DELETE FROM photo WHERE idd = ?";
        jdbcTemplate.update(sql, new Object[]{idd});
        return true;
    }
}
