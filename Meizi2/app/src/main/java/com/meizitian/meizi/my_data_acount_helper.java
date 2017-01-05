package com.meizitian.meizi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/24.
 */

public class my_data_acount_helper extends SQLiteOpenHelper {

    public static final String CREATE_ACOUNT_DATA=
            "create table acount_data ("+"id integer primary key autoincrement,"+"code text,"+"userid text,"+"token text,"+"name text )";

    private Context data_acount_context;

    public my_data_acount_helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        data_acount_context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACOUNT_DATA);
        Toast.makeText(data_acount_context,"create chat_data success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists acount_data");
        onCreate(sqLiteDatabase);
    }
}
