package com.meizitian.meizi;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Random;


public class translate_baidu {
    public void transer(final String q, final String from, final String to,final translate_callback translateCallback){
        new AsyncTask<Void,Integer,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection connection = null;
                String text = null;
                try {
                    String appId = "20161021000030523";
                    String token = "wpaw29KtT1HfEddnkHIz";
                    String url_1 = "http://api.fanyi.baidu.com/api/trans/vip/translate";
                    //String from = "en";
                    //String to = "zh";
                    Random random = new Random();
                    int salt = random.nextInt(10000);
                    StringBuilder md5String = new StringBuilder();
                    md5String.append(appId).append(q).append(salt).append(token);
                    String md5 = MD5Encoder.encode(md5String.toString());
                    URL url = new URL(url_1 + "?" + "q=" + URLEncoder.encode(q, "utf-8") + "&from=" + from + "&to=" + to + "&appid=" + appId + "&salt=" + salt + "&sign=" + md5);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject resultJson = new JSONObject(response.toString());
                    //获取返回翻译结果
                    JSONArray array = (JSONArray) resultJson.get("trans_result");
                    JSONObject dst = (JSONObject) array.get(0);
                    String showtext = dst.getString("dst");
                    text = URLDecoder.decode(showtext, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return text;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (Objects.equals(s,"")){
                    translateCallback.fail(s);
                }else {
                    translateCallback.success(s);
                }
            }
        }.execute();
    }
}
