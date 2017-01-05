package com.meizitian.meizi;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class get_token {
    public void return_a_token(final String userid,final String name, final get_token_callback callback){
        new AsyncTask<Void,Integer,List<String>>(){
            @Override
            protected List<String> doInBackground(Void... voids) {
                HttpURLConnection connection=null;
                List<String> text = new ArrayList<String>();
                try{
                    String app_key="n19jmcy5nin29";
                    String app_secret="1InMmc2I59k";
                    String nonce=String.valueOf(Math.random()*1000000);
                    System.out.println(nonce);
                    String timestamp=String.valueOf(System.currentTimeMillis()/1000);
                    System.out.println(timestamp);
                    String sign=app_secret+nonce+timestamp;
                    String signature=SHA1coder.sha1(sign);
                    System.out.println(signature);

                    URL url=new URL("http://api.cn.ronghub.com/user/getToken.xml");
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");//默认是get
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    //头部信息
                    connection.setRequestProperty("App-Key",app_key);
                    connection.setRequestProperty("Nonce",nonce);
                    connection.setRequestProperty("Timestamp",timestamp);
                    connection.setRequestProperty("Signature",signature);
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.connect();//正式连接
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String content="userId="+ URLEncoder.encode(userid,"UTF-8")+"&name="+URLEncoder.encode(name,"UTF-8");
                    out.writeBytes(content);
                    out.flush();
                    out.close();
                    //开始读取数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    System.out.println(reader);
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null){
                        System.out.println("line:");
                        System.out.println(line);
                        response.append(line);
                    }
                    reader.close();
                    connection.disconnect();
                    String xml_orign=response.toString(); //整理成string类型
                    System.out.println("xml_orign:"+xml_orign);
                    text=xml_parse(xml_orign,name); //解析成一个list类型 依次为; code userid token
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null) {
                        connection.disconnect(); //断开连接  后面才加上的 之前没有加的时候没有出错
                    }
                }
                return text;
            }
            @Override
            protected void onPostExecute(List<String> s) {
                super.onPostExecute(s);
                callback.success(s);//借用回调出去
            }
        }.execute();
    }
    public List<String> xml_parse(String xml_string,String NAME){
        List<String> list=new ArrayList<>();
        try{
            Reader xml_change = new StringReader(xml_string);//把string类型转换成reader
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(xml_change));//最后得到文档结点 document
            System.out.println("documeent:"+document);
            //code
            NodeList code = document.getElementsByTagName("code");
            String CODE=code.item(0).getFirstChild().getNodeValue();
            System.out.println(CODE);
            //userId
            NodeList userid = document.getElementsByTagName("userId");
            String USERID=userid.item(0).getFirstChild().getNodeValue();
            System.out.println(USERID);
            //token
            NodeList Token = document.getElementsByTagName("token");
            String TOKEN=Token.item(0).getFirstChild().getNodeValue();
            System.out.println(TOKEN);

            list.add(CODE);
            list.add(USERID);
            list.add(TOKEN);
            list.add(NAME);
            /**
             * 实例：
             * <result>
             *   <code>200</code>
             *   <userId>meizi</userId>
             *   <token>6AfFe58zGYm4eX8Vd1DAtH08aOYa24KyBemjnd7ZhwMFkLn0Qdzqa3PfDJxR8wfQKxQNK0Su6dZJqPT3MbeFSw==</token>
             * </result>
             */
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
/**
 * 签名规则：
 String app_key="uwd1c0sxdlx2";
 String nonce="14314";
 String timestamp="1408706337";
 String sign=app_key+nonce+timestamp;
 String signature=SHA1coder.sha1(sign);

 System.out.println(signature);
 System.out.println("生成五位数的随机数："+Math.round(Math.random())*100000);
 System.out.println("45beb7cc7307889a8e711219a47b7cf6a5b000e8");
 */
