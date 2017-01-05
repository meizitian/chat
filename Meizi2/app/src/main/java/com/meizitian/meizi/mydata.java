package com.meizitian.meizi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class mydata {

    private my_data_helper mydata_all;
    private SQLiteDatabase database_all;

    public void create_data(Context context){
        my_data_helper mydata;
        SQLiteDatabase database;
        mydata=new my_data_helper(context,"chat_data.db",null,10);
        mydata.getWritableDatabase();
        database=mydata.getWritableDatabase();

        mydata_all=mydata;
        database_all=database;
        }
    public List<String>  query_data(){
        String name;
        String content;
        String type;
        String sendtime;
        List<String> list=new ArrayList<>();
        Cursor cursor=database_all.query("chat_data",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                name=cursor.getString(cursor.getColumnIndex("name"));
                //System.out.println(name);
                content=cursor.getString(cursor.getColumnIndex("content"));
                //System.out.println(content);
                type=cursor.getString(cursor.getColumnIndex("type"));
                //System.out.println(type);
                sendtime=cursor.getString(cursor.getColumnIndex("sendtime"));
                if (!name.isEmpty()){
                    list.add(name);
                    list.add(content);
                    list.add(type);
                    list.add(sendtime);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        //System.out.println("list："+list);
        //System.out.println("list大小："+list.size());
        return list;
    }
    public void add_data(String name,String content,String type,String sendtime){
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("content",content);
        values.put("type",type);
        values.put("sendtime",sendtime);
        database_all.insert("chat_data",null,values);
        values.clear();
    }
    public void delate_data(String delate_name){
        database_all.delete("chat_data","content==?",new String[]{delate_name});
        System.out.println("delate了一个数据");
    }
    public void updata_data(){

    }

}
