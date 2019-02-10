package com.example.sukhman.messapp;

import android.net.Uri;

public class Util {
    public static  final int DB_VERSION=1;
    public static  final String DB_NAME="StudentsRecord.db";

    public static  final String TAB_NAME="Student";

    public static  final String COL_ID="_ID";
    public static  final String COL_ACC_ID="AID";
    public static  final String COL_NAME="NAME";
    public static  final String COL_PHONE="PHONE";
    public static  final String COL_YEAR="YEAR";

    public static  final String CREATE_TAB_QUERY="create table Student("+
            "_ID integer primary key autoincrement," +
            "AID integer,"+
            "NAME varchar(256),"+
            "PHONE varchar(256),"+
            "YEAR integer" +
            ")";

    //authority name
    public static Uri USER_URI = Uri.parse("content://com.example.sukhman.messapp.cp/"+TAB_NAME);

    public static  final String TAB_NAME1="Bill";

    public static  final String COL_BILL_ID="_ID";
    public static  final String COL_SID="SID";
    public static  final String COL_STATUS="AC_STATUS";
    public static  final String COL_EXTRA="EXTRA";
    public static  final String COL_PRICE="PRICE";
    public static  final String COL_DATE="DATE";


    public static  final String CREATE_TAB_QUERY1="create table Bill("+
            "_ID INTEGER PRIMARY KEY autoincrement,"+
            "SID integer,"+
            "AC_STATUS integer DEFAULT 1,"+
            "EXTRA varchar(256),"+
            "PRICE integer,"+
            "DATE DATETIME DEFAULT CURRENT_DATE"+
            ")";

    public static Uri USER_URI1 = Uri.parse("content://com.example.sukhman.messapp.cp/"+TAB_NAME1);


}
