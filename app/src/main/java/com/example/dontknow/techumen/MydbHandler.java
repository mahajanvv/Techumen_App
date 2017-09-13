package com.example.dontknow.techumen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dont know on 27-08-2017.
 */

public class MydbHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="Techumen.db";
    public static final String TABLE_NAME="OfflineEntry";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_NAME="_name";
    public static final String COLUMN_EMAIL="_email";
    public static final String COLUMN_PHONE="_phone";
    public static final String COLUMN_COLLEGE="_college";
    public static final String COLUMN_YEAR="_year";
    public static final String COLUMN_IMAGE="_image";
    public static final String COLUMN_REMAINIG="_remaining";
    public static final String COLUMN_TRANSACTIONID="_transaction";
    public static final String COLUMN_EVENTNAME="_eventname";
    public static final String COLUMN_STATUS="_status";


    public MydbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String str= "CREATE TABLE " + TABLE_NAME + " (  "+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COLUMN_NAME +" TEXT ,"+
                COLUMN_EMAIL +" TEXT ,"+
                COLUMN_PHONE+ " TEXT ,"+
                COLUMN_COLLEGE +" TEXT ,"+
                COLUMN_YEAR + " TEXT ,"+
                COLUMN_IMAGE +" TEXT ,"+
                COLUMN_REMAINIG + " TEXT ,"+
                COLUMN_TRANSACTIONID+ " TEXT ,"+
                COLUMN_EVENTNAME +" TEXT ,"+
                COLUMN_STATUS + " TEXT );";
        db.execSQL(str);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP "+TABLE_NAME+ ";");
        onCreate(db);
    }

    public void add(TeDatabase cwDatabse)
    {
        ContentValues values =new ContentValues();
        values.put(COLUMN_NAME,cwDatabse.get_name());
        values.put(COLUMN_EMAIL,cwDatabse.get_email());
        values.put(COLUMN_PHONE,cwDatabse.get_phone());
        values.put(COLUMN_COLLEGE,cwDatabse.get_college());
        values.put(COLUMN_YEAR,cwDatabse.get_year());
        values.put(COLUMN_IMAGE,cwDatabse.get_image());
        values.put(COLUMN_REMAINIG,cwDatabse.get_remaining());
        values.put(COLUMN_TRANSACTIONID,cwDatabse.get_transaction());
        values.put(COLUMN_EVENTNAME,cwDatabse.get_eventname());
        values.put(COLUMN_STATUS,cwDatabse.get_status());


        SQLiteDatabase db =getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public ArrayList<TeDatabase> getDatabase()
    {
        ArrayList<TeDatabase > data = new ArrayList<TeDatabase>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME +" WHERE  1 ;";
        Cursor cursor = db.rawQuery(query , null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            data.add(new TeDatabase(cursor.getString(cursor.getColumnIndex("_name")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_transaction")),cursor.getString(cursor.getColumnIndex("_eventname")),cursor.getString(cursor.getColumnIndex("_status"))));
            cursor.moveToNext();
        }
        db.close();
        Collections.reverse(data);
        return data;

    }
    public TeDatabase getTedatabase(String mail,String phone,String remaining)
    {
        SQLiteDatabase db=getWritableDatabase();
        TeDatabase cwDatabase = null;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_EMAIL+" = \'"+mail+"\' AND "+COLUMN_PHONE+" = \'"+phone+"\' AND "+COLUMN_REMAINIG+" = \'"+remaining+"\' AND "+COLUMN_STATUS+" = "+"\"Not Uploaded\""+" ;";
        Cursor cursor = db.rawQuery(query, null );
        cursor.moveToFirst();
        if(cursor != null)
        {
            try {
                cwDatabase = new TeDatabase(cursor.getString(cursor.getColumnIndex("_name")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_transaction")),cursor.getString(cursor.getColumnIndex("_eventname")),cursor.getString(cursor.getColumnIndex("_status")));
            }
            catch (Exception e){}
        }
        return cwDatabase;
    }

    public ArrayList<TeDatabase> getUploadDatabase()
    {
        ArrayList<TeDatabase > data = new ArrayList<TeDatabase>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME +" WHERE  "+COLUMN_STATUS+" = "+"\"Not Uploaded\""+" ;";
        Cursor cursor = db.rawQuery(query , null );
        cursor.moveToFirst();
        if(cursor != null)
        {
            try {
                data.add(new TeDatabase(cursor.getString(cursor.getColumnIndex("_name")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_transaction")),cursor.getString(cursor.getColumnIndex("_eventname")),cursor.getString(cursor.getColumnIndex("_status"))));
            }
            catch (Exception e){
            }
        }
        /*while (!cursor.isAfterLast())
        {

            cursor.moveToNext();
        }*/
        db.close();
        return data;

    }
    public void setUploaded(TeDatabase cwDatabase)
    {
        String query = " UPDATE "+TABLE_NAME+ " SET " + COLUMN_STATUS +" = "+"\"UPLOADED\""+" WHERE "+COLUMN_NAME +" = '"+cwDatabase.get_name()+"' AND "+COLUMN_PHONE+" = '"+cwDatabase.get_phone()+"' AND "+COLUMN_EVENTNAME+" = '"+cwDatabase.get_eventname()+"' ; ";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
}
