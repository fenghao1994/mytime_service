package com.example.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fenghao on 2017/5/8.
 */
public class PlanItem implements Serializable{
    int idd;
    int id;
    /**
     * 计划表的唯一值
     */
    long planId;
    String title;
    String content;
    long createTime;
    long editTime;
    boolean isEdit;
    String phoneNumber;
    String messageContent;
    String messagePhoneNumber;
    String location;
    String phoneNumberLianXi;
    boolean isEveryDay;
    boolean isManyDays;
    boolean isExpired;
    boolean isComplete;
    boolean isDelete;

    ArrayList<Photo> address;
    int years;
    int months;
    int days;
    int hours;
    int minutes;
    int alarmWay;//0为默认 只提醒一次，1为每日，2为每周，3为每月

    //目前用于到达目的后通知，显示一次后不再显示，默认为null 到达目的地通知后变为 LOCATED
    String describes;

    String open;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessagePhoneNumber() {
        return messagePhoneNumber;
    }

    public void setMessagePhoneNumber(String messagePhoneNumber) {
        this.messagePhoneNumber = messagePhoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumberLianXi() {
        return phoneNumberLianXi;
    }

    public void setPhoneNumberLianXi(String phoneNumberLianXi) {
        this.phoneNumberLianXi = phoneNumberLianXi;
    }

    public boolean isEveryDay() {
        return isEveryDay;
    }

    public void setEveryDay(boolean everyDay) {
        isEveryDay = everyDay;
    }

    public boolean isManyDays() {
        return isManyDays;
    }

    public void setManyDays(boolean manyDays) {
        isManyDays = manyDays;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public ArrayList<Photo> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Photo> address) {
        this.address = address;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(int alarmWay) {
        this.alarmWay = alarmWay;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }
}
