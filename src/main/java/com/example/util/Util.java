package com.example.util;

import java.text.SimpleDateFormat;

/**
 * Created by fenghao on 2017/5/29.
 */
public class Util {
    public static String dateYMD(long time){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd");
        String s = sdf.format( time);
        return s;
    }
}
