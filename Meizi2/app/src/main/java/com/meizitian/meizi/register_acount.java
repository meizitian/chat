package com.meizitian.meizi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class register_acount extends AppCompatActivity {

    private EditText register_userid;
    private EditText register_name;
    private Button register_button;
    private TextView register_userid_textview;
    private TextView register_top_back;
    private get_token getToken;
    private Context register_context;
    private mydataacount dataacount;
    private Intent intent;

    String name;
    String userid;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_acount);
        register_context=getApplicationContext();
        dataacount=new mydataacount();
        dataacount.create_acount_data(register_context);
        intent=new Intent(register_context,MainActivity.class);
        init_view();
        init_lis();
    }
    private void init_view(){
        register_userid= (EditText) findViewById(R.id.register_editText_userid);
        register_userid_textview= (TextView) findViewById(R.id.register_Textview_userid);
        register_name= (EditText) findViewById(R.id.register_editText_name);
        register_button= (Button) findViewById(R.id.register_button);
        register_top_back= (TextView) findViewById(R.id.register_top_back_textview);

        register_userid.setVisibility(View.GONE);
        register_userid_textview.setVisibility(View.GONE);
    }
    private void init_lis(){
        register_button.setOnClickListener(register);
        register_top_back.setOnClickListener(top_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    View.OnClickListener register=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name=register_name.getText().toString();
            userid=register_userid.getText().toString();
            if (name.isEmpty()){
                Toast.makeText(getApplicationContext(),"信息不完整",Toast.LENGTH_SHORT).show();
            }else if (dataacount.query_acount_data().size()!=0) {
                Toast.makeText(register_context,"already registered",Toast.LENGTH_SHORT).show();
            }else {
                //注册
                getToken=new get_token();
                TelephonyManager mTm = (TelephonyManager) register_context.getSystemService(TELEPHONY_SERVICE);
                String momo_userid = mTm.getDeviceId();//用手机ID进行注册
                getToken.return_a_token(momo_userid, name, new get_token_callback() {
                    @Override
                    public void success(List<String> list) {
                        dataacount.add_acount_data(list);
                        if (list.get(0).equals("200")){
                            startActivity(intent);
                        }else {
                            Toast.makeText(register_context,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                        System.out.println("register:"+list);
                    }
                });
            }
        }
    };
    View.OnClickListener top_back=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
            onDestroy();
        }
    };
}
