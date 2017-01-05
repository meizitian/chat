package com.meizitian.meizi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static java.security.AccessController.getContext;


public class bind_chat extends AppCompatActivity {
    private Context bind_context;
    private EditText bind_edittext;
    private Button bind_button;
    private TextView bind_top_back_textview;
    private TextView bind_top_more_textview;
    private TextView show_imei_textview;
    private String userid;

    private IntentFilter intentFilter;
    private IntentFilter intentFilter2;
    private shutdown down;
    private shutdown2 down2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_chat);
        bind_context=getApplicationContext();
        init_view();
        userid=bind_edittext.getText().toString();
        init_lis();
        intentFilter.addAction("com.meizitian.meizi.YES");
        intentFilter2.addAction("com.meizitian.meizi.NOP");
        registerReceiver(down,intentFilter);
        registerReceiver(down2,intentFilter2);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(down);
        unregisterReceiver(down2);
    }

    private void init_view(){
        bind_edittext= (EditText) findViewById(R.id.bind_edittext);
        bind_button= (Button) findViewById(R.id.bind_button);
        bind_top_back_textview= (TextView) findViewById(R.id.bind_top_back_textview);
        bind_top_more_textview= (TextView) findViewById(R.id.bind_top_more_textview);
        show_imei_textview= (TextView) findViewById(R.id.bind_imei_textview);
        intentFilter=new IntentFilter();
        intentFilter2=new IntentFilter();
        down=new shutdown();
        down2=new shutdown2();
    }
    private void init_lis(){
        bind_button.setOnClickListener(button_listener);
        bind_top_back_textview.setOnClickListener(top_back);
        bind_top_more_textview.setOnClickListener(top_more);
    }

    View.OnClickListener button_listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userid=bind_edittext.getText().toString();
            if (userid.isEmpty()){
                System.out.println("userid"+userid);
                Toast.makeText(bind_context,"没有输入userID",Toast.LENGTH_SHORT).show();
            }else {
                send_bind_message();
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    View.OnClickListener top_back=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };
    View.OnClickListener top_more=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(bind_context,"更多tips请看'关于'页面",Toast.LENGTH_SHORT).show();
            TelephonyManager mTm = (TelephonyManager) bind_context.getSystemService(TELEPHONY_SERVICE);
            String imei = mTm.getDeviceId();
            System.out.println(imei);
            show_imei_textview.setText(imei);
        }
    };


    private void send_bind_message(){
        String userid_self=RongIMClient.getInstance().getCurrentUserId();
        TextMessage textMessage=TextMessage.obtain("meizitian357753357753"+userid_self);
        io.rong.imlib.model.Message message= io.rong.imlib.model.Message.obtain(userid, Conversation.ConversationType.PRIVATE,textMessage);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
            }
            @Override
            public void onSuccess(Message message){
                Toast.makeText(bind_context,getResources().getString(R.string.dd),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    class shutdown extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, final Intent intent){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("对方的看法");
            builder.setMessage("同意啦~");
            builder.setCancelable(false);
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
    }
    class shutdown2 extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, final Intent intent){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("对方的看法");
            builder.setMessage("不同意啦~");
            builder.setCancelable(false);
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
    }
}
/**
 AlertDialog.Builder builder=new AlertDialog.Builder(bind_context);
 builder.setTitle("关于绑定");
 builder.setMessage("需要注意的是绑定用的是对方的userID而不是注册时的name");
 builder.setCancelable(false);
 builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {

}
});
 builder.show();
 */