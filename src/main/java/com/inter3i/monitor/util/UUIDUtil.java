package com.inter3i.monitor.util;

import org.bson.types.ObjectId;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/5/10 19:48
 */
public class UUIDUtil {

    public static String getUUID(){
        return new ObjectId().toString();
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
