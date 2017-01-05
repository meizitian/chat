package com.meizitian.meizi;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class translate_baidu_service extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data=intent.getStringExtra("data");
        usebaidu(data);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    protected void usebaidu(String string){
        translate_baidu translateBaidu=new translate_baidu();
        judge_lang is_zh=new judge_lang();
        if (is_zh.is_zh(string)){
            translateBaidu.transer(string,"zh","en",new translate_callback(){
                @Override
                public void success(final String string) {
                    //吐出结果
                    new Thread(){
                        @Override
                        public void run(){
                            // Looper.prepare();
                            new Handler(Looper.getMainLooper()){
                                @Override
                                public void handleMessage(Message msg){
                                    Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
                                    System.out.println("服务完成");
                                }
                            }.obtainMessage().sendToTarget();
                        }
                    }.start();
                }
                @Override
                public void fail(String string) {
                }
            });
        }else {
            translateBaidu.transer(string,"en","zh",new translate_callback(){
                @Override
                public void success(final String string) {
                    //吐出结果
                    new Thread(){
                        @Override
                        public void run(){
                            // Looper.prepare();
                            new Handler(Looper.getMainLooper()){
                                @Override
                                public void handleMessage(Message msg){
                                    Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
                                    System.out.println("服务完成");
                                }
                            }.obtainMessage().sendToTarget();
                        }
                    }.start();
                }
                @Override
                public void fail(String string) {
                }
            });
        }
    }
}
