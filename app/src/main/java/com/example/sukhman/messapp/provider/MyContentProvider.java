package com.example.sukhman.messapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.sukhman.messapp.Util;
import com.example.sukhman.messapp.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class MyContentProvider extends ContentProvider {
    public DBHelper dbHelper;
     public static SQLiteDatabase sqLiteDatabase;
  //  private  Context mContext;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tabName = uri.getLastPathSegment();
        int i=sqLiteDatabase.delete(tabName,selection,null);
        return i;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tabName = uri.getLastPathSegment();
        long id = sqLiteDatabase.insert(tabName,null,values);
        Uri returnUri = Uri.parse("nothing://any/"+id);
        return returnUri;

    }

    @Override
    public boolean onCreate() {
        dbHelper=new DBHelper(getContext(),Util.DB_NAME,null,Util.DB_VERSION);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        return false;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tabName = uri.getLastPathSegment();
        Log.i("test",tabName);
        Cursor cursor = sqLiteDatabase.query(tabName, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tabName=uri.getLastPathSegment();
        int id=sqLiteDatabase.update(tabName,values,selection,null);
        return id;

    }
   public class  DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);//create DB (users.db) and Version 1
            //mContext = context;
              //  sqLiteDatabase = this.getWritableDatabase();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Util.CREATE_TAB_QUERY);
            db.execSQL(Util.CREATE_TAB_QUERY1);//can have multiple queries

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            if(oldVersion!=newVersion){

            }
        }
    }


}
