package com.meizitian.meizi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class setting_about extends AppCompatActivity {
    private ImageView imageView;
    private TextView send_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_about);
        init_view();
        init_event();
        imageView.setImageResource(R.drawable.back_1);

    }
    private void init_view(){
        imageView= (ImageView) findViewById(R.id.top_setting_imageview);
        send_email= (TextView) findViewById(R.id.setting_zbout_email);
    }
    private void init_event(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send_email.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("1571539007@qq.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
