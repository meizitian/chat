package com.meizitian.meizi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/12/24.
 */

public class bind_save_userid {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public void create(Context context){
        preferences=context.getSharedPreferences("bind_userid",Context.MODE_PRIVATE);
    }
    public void save(Context context,String userid){
        editor=context.getSharedPreferences("bind_userid",Context.MODE_PRIVATE).edit();
        editor.putString("userid",userid);
        System.out.println("save a userid:"+userid);
        editor.commit();
    }
    public String query(Context context){
        String userid=preferences.getString("userid","meizitian357753357753");
        System.out.println("查询的userID："+userid);
        return userid;
    }
}
