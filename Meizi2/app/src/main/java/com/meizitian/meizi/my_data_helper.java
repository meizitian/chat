package com.meizitian.meizi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class my_data_helper extends SQLiteOpenHelper {
    public static final String CREATE_CHAT_DATA=
            "create table chat_data ("+"id integer primary key autoincrement,"+"name text,"+"content text,"+"type text,"+"sendtime text )";
    //name:聊天人id
    // content:内容 目前支持文字
    // type:消息类型
    private Context data_context;
    public my_data_helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        data_context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CHAT_DATA);
        Toast.makeText(data_context,"create chat_data success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists chat_data");
        onCreate(sqLiteDatabase);
    }
}
