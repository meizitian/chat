package com.meizitian.meizi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class setting_me extends AppCompatActivity {
    private ImageView imageView;
    private TextView setting_me_userid;
    private TextView setting_me_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_me);
        init_view();
        init_event();

    }
    public void init_view(){
        imageView= (ImageView) findViewById(R.id.top_setting_imageview);
        setting_me_userid= (TextView) findViewById(R.id.setting_me_userid);
        setting_me_name= (TextView) findViewById(R.id.setting_me_name);
        imageView.setImageResource(R.drawable.back_1);
        setting_me_name.setText(quaryname().get(1));
        setting_me_userid.setText(quaryname().get(3));
    }
    public List<String> quaryname(){
        mydataacount d=new mydataacount();
        d.create_acount_data(getApplicationContext());
        List<String> ss=d.query_acount_data();
        return ss;
    }
    public void init_event(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
