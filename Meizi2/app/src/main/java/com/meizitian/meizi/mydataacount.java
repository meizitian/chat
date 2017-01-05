package com.meizitian.meizi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class mydataacount {

    private my_data_acount_helper myDataAcountHelper;
    private SQLiteDatabase sqLiteDatabase;

    public void create_acount_data(Context context){
        my_data_acount_helper m;
        SQLiteDatabase s;
        m=new my_data_acount_helper(context,"acount_data.db",null,5);
        m.getWritableDatabase();
        s=m.getWritableDatabase();
        myDataAcountHelper=m;
        sqLiteDatabase=s;
    }

    public void add_acount_data(List<String> list){
        System.out.println("add_acount_data:"+list);
        String CODE=list.get(0);
        String USERID=list.get(1);
        String TOKEN=list.get(2);
        String NAME=list.get(3);
        ContentValues values=new ContentValues();
        values.put("code",CODE);
        values.put("userid",USERID);
        values.put("token",TOKEN);
        values.put("name",NAME);
        sqLiteDatabase.insert("acount_data",null,values);
        values.clear();
    }

    public List<String> query_acount_data(){
        String CODE;
        String USERID;
        String TOKEN;
        String NAME;
        List<String> list=new ArrayList<>();
        Cursor cursor=sqLiteDatabase.query("acount_data",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                CODE=cursor.getString(cursor.getColumnIndex("code"));
                //System.out.println(CODE);
                USERID=cursor.getString(cursor.getColumnIndex("userid"));
                //System.out.println(USERID);
                TOKEN=cursor.getString(cursor.getColumnIndex("token"));
                NAME=cursor.getString(cursor.getColumnIndex("name"));
                //System.out.println(TOKEN);
                list.add(CODE);
                list.add(USERID);
                list.add(TOKEN);
                list.add(NAME);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
