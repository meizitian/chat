package com.meizitian.meizi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class translate extends Activity {
    private String data;
    private Intent intent;
    @Override
    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //接收数据
        Intent getdata=getIntent();
        data=getdata.getStringExtra(Intent.EXTRA_TEXT);
        //传递数据并打开服务
        intent=new Intent(this,translate_baidu_service.class);
        intent.putExtra("data",data);
        startService(intent);
        System.out.println("oncreate");
        onDestroy();
    }
    @Override
    protected void onStart() {
        System.out.println("onstart");
        super.onStart();
    }
    @Override
    protected void onResume() {
        System.out.println("onresume");
        finish();
        super.onResume();
    }
    @Override
    protected void onStop() {
        System.out.println("onstop");
        super.onStop();
    }
    @Override
    protected void onRestart() {
        System.out.println("onrestart");
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        System.out.println("ondestroy");
        stopService(intent);
        super.onDestroy();
    }
}
