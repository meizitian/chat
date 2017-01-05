package com.meizitian.meizi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/26.
 */

public class get_chat_pic {

    public void retuan_a_pic(final String uri_string, final get_chat_pic_callback callback){
        new AsyncTask<Void,Integer,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection connection=null;
                String b=null;
                try{
                    URL url=new URL(uri_string);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //设置请求方法
                    connection.setConnectTimeout(8000); //设置连接服务器超时时间
                    connection.setReadTimeout(8000);  //设置读取数据超时时间
                    connection.connect(); //开始连接
                    int responseCode = connection.getResponseCode(); //得到服务器的响应码
                    if (responseCode == 200) {
                        //访问成功
                        InputStream is = connection.getInputStream(); //获得服务器返回的流数据
                        Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
                        b=save_bitmap_to(bitmap);
                    } else {
                        //访问失败
                        System.out.println("访问失败，responsecode："+responseCode);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return b;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                callback.success(s);
            }
        }.execute();
    }
    public String save_bitmap_to(Bitmap bitmap){
        System.out.println("进入图片储存过程");
        String pic_name=String.valueOf(Math.random()*10000);
       // String d= Environment.getRootDirectory().getAbsolutePath();
       // System.out.println(d);
        String path="/storage/emulated/0/meizi_chat_pic/";
       // String path_test=d+"meizi_chat_pic/";
        String type=".jpg";
        String pic_path=path+pic_name+type;
        String return_path="file://"+pic_path;
        File gg=new File(path);
        if (!gg.exists()){
            gg.mkdirs();
        }
        File f=new File(pic_path);
        FileOutputStream out=null;
        try {
            out=new FileOutputStream(f);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return return_path;
    }
}
