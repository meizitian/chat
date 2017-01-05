package com.meizitian.meizi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

import static android.app.Activity.RESULT_OK;


public class fragment_main extends android.support.v4.app.Fragment {
    private View view;
    private ListView main_listView;
    private main_list_adapter mainListAdapter;
    private List<main_single_list> mainSingleLists =new ArrayList<>();
    private Button send_button;
    private Button more_button;
    private EditText input_text;
    private TextView buttom_more_pic_textview;
    private LinearLayout buttom_more_ll;
    private String input_content;
    private String chat_pic_content;

    private ImageView music_imageview;
    private LinearLayout music_ll;
    private Context main_context;
    private mydata my_data;
    private NotificationManager manager;
    private bind_save_userid bindSaveUserid;
    private AlertDialog.Builder alertDialog;
    private Activity main_activity;
    private ContactsContract.Data data;

    private TextMessage textMessage;
    private ImageMessage imageMessage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main_activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=LayoutInflater.from(getContext()).inflate(R.layout.main,null);
        main_context=getContext();

        manager=(NotificationManager)main_context.getSystemService(Context.NOTIFICATION_SERVICE);//通知栏通知
        my_data=new mydata();//初始化数据库
        my_data.create_data(main_context);//创建数据库
        bindSaveUserid=new bind_save_userid();
        bindSaveUserid.create(main_context);//创建绑定聊天对象的数据库
        init_view();//初始化控件
        init_datas();//初始化数据
        init_event();
        mainListAdapter=new main_list_adapter(getContext(),R.layout.main_single_list_layout,mainSingleLists);//绑定adpter
        main_listView.setAdapter(mainListAdapter);//绑定adpter

        //IM
        receivemessage();//监听接受消息
        sendmessage();//发送消息
        manager.cancel(1); //取消通知
        buttom_more();//聊天更多功能布局
        System.out.println("oncreateview!!!");
        return view;
    }

    //重写下面两个方法用来关闭通知栏的通知的
    @Override
    public void onResume() {
        manager.cancel(1);
        super.onResume();
    }
    @Override
    public void onPause() {
        manager.cancel(1);
        super.onPause();
    }

    //初始化控件
    protected void  init_view(){
        main_listView= (ListView) view.findViewById(R.id.main_listview);
        music_imageview= (ImageView) view.findViewById(R.id.main_single_list_music_imageview);
        music_ll= (LinearLayout) view.findViewById(R.id.main_single_list_music_ll);
        send_button= (Button) view.findViewById(R.id.buttom_send_button);
        more_button= (Button) view.findViewById(R.id.buttom_more_button);
        input_text= (EditText)view.findViewById(R.id.buttom_input_edittext);
        buttom_more_pic_textview= (TextView) view.findViewById(R.id.buttom_more_pic_textview);
        buttom_more_ll= (LinearLayout) view.findViewById(R.id.buttom_more_ll);
        alertDialog=new AlertDialog.Builder(main_context);
    }
    //初始化数据包括历史数据
    protected void init_datas(){
        System.out.println("init_data!!!");
        /**
        main_single_list mainSingleList1=new main_single_list("hello",0,main_single_list.TYPECHAT_IN);
        mainSingleLists.add(mainSingleList1);
        main_single_list mainSingleList2=new main_single_list("hello",0,main_single_list.TYPECHAT_OUT);
        mainSingleLists.add(mainSingleList2);
        main_single_list mainSingleList3=new main_single_list("预制的第一次对话界面",0,main_single_list.TYPECHAT_IN);
        mainSingleLists.add(mainSingleList3);
        main_single_list mainSingleList4=new main_single_list("did you receive ？",0,main_single_list.TYPECHAT_IN);
        mainSingleLists.add(mainSingleList4);
        main_single_list mainSingleList5=new main_single_list("i see",0,main_single_list.TYPECHAT_OUT);
        mainSingleLists.add(mainSingleList5);
         **/
        getmessagefrom_mydata();
    }
    //list操作
    protected void init_event(){
        main_listView.setOnItemClickListener(ItemClickListener);
        main_listView.setOnItemLongClickListener(longClickListener);
    }
    //IM
    //监听接受消息
    public void receivemessage(){
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(io.rong.imlib.model.Message message, int i) {
                MessageContent messageContent = message.getContent();
                String send_userid=message.getSenderUserId();//获取发送消息的人userid

                long d=message.getSentTime();//发送时间
                String sengtime=long_to_time(d);
                System.out.println("接收的时间"+sengtime);

                //String bind_query_result=bindSaveUserid.query(main_context);
                System.out.println("发送者的名字："+send_userid);
                //接收文本消息
                rec_text_message(messageContent,send_userid,sengtime);
                rec_pic_message(messageContent,send_userid,sengtime);
                return false;
            }
        });
    }
    //通知
    private void notif(){
        //通知
        //主要是添加通知栏的跳转 因为pendingIntent.getactivity不能用 采用TaskStackBuilder的方式
        Intent intent=new Intent(main_context,MainActivity.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(main_context);
        stackBuilder.addParentStack(main_activity);// 此处activity不能用getactivity因为fragment退出后此方法返回的是一个null 出错！
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notification=new Notification.Builder(main_context);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle("啦啦啦");
        notification.setContentText("有消息啦");
        notification.setContentIntent(pendingIntent);
        Notification notif= notification.build();
        manager.notify(1,notif);
    }
    //展示消息
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    System.out.println("其实已经进入handle");
                    mainListAdapter.notifyDataSetChanged();
                    main_listView.smoothScrollToPosition(main_listView.getBottom());
                    break;
            }
        }
    };
    //发送消息
    public void sendmessage(){
        //发送文字消息
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bind_query_result=bindSaveUserid.query(main_context);
                if (bind_query_result=="meizitian357753357753"){
                    //没有绑定
                    System.out.println("未绑定："+bind_query_result);
                    Intent intent=new Intent(main_context,bind_chat.class);
                    startActivity(intent);
                }else {
                    //已经绑定 直接发信息
                    send_text_message();
                }
                input_text.setText("");
            }
        });
        //发送图片消息
        buttom_more_pic_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bind_query_result=bindSaveUserid.query(main_context);
                if (bind_query_result=="meizitian357753357753"){
                    //没有绑定
                    System.out.println("未绑定："+bind_query_result);
                    Intent intent=new Intent(main_context,bind_chat.class);
                    startActivity(intent);
                }else {
                    //已经绑定 直接发信息
                    System.out.println("发送前准备一下："+chat_pic_content);
                    get_pic();
                }
            }
        });
    }
    //发送确认绑定回复
    public void bind_answer(final String s, String name){
        String userid_self=RongIMClient.getInstance().getCurrentUserId();//直接通过sdk获取userid
        TextMessage textMessage=TextMessage.obtain("zezier159951159951"+s+userid_self);
        io.rong.imlib.model.Message message= io.rong.imlib.model.Message.obtain(name, Conversation.ConversationType.PRIVATE,textMessage);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(io.rong.imlib.model.Message message) {
            }
            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                System.out.println("确认回复:"+s);
            }
            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }
    //通过自己的数据库查询数据 展示历史消息
    public void getmessagefrom_mydata(){
        System.out.println("进入getmessagefrom_mydata");
        List<String> list;
        list=my_data.query_data();//查询数据 在自己的数据库里面
        int i=4;//一条数据有四个内容
        int list_size=list.size();
        if (!list.isEmpty()){
            i=list_size/i-1; //有i条数据
            int n=0;
            if (i>10){
                n=i-10;
            }
            messageshow d=new messageshow(list,i,n);//从第n条开始展示
            d.show_i_msg();
        }
    }
    //实际展示历时消息过程
    protected class messageshow{
        private List<String> list;
        private int i;
        private int kk;
        public messageshow(List<String> list,int i,int k){
            this.list=list;
            this.i=i;
            this.kk=k;
        }
        public void show_i_msg(){
            for (int k=kk;k<=i;k++){
                for (int j=0;j<=3;j++){
                    System.out.println(list.get(k*4+j)); //测试list的内容 即便是为null也可以
                }
                // name content type
                String name=list.get(k*4+0);
                String content=list.get(k*4+1);
                String type=list.get(k*4+2);
                String sendtime=list.get(k*4+3);
                if (content==null){
                    my_data.delate_data(name);
                }else if (type.equals("TYPECHAT_OUT")){
                    //此处放type=="TYPECHAT_OUT"不行！！！
                    main_single_list mainSingleList9=new main_single_list(content,0,main_single_list.TYPECHAT_OUT,sendtime);
                    mainSingleLists.add(mainSingleList9);
                }else if (type.equals("TYPECHAT_IN")){
                    main_single_list mainSingleList10=new main_single_list(content,0,main_single_list.TYPECHAT_IN,sendtime);
                    mainSingleLists.add(mainSingleList10);
                }else if (type.equals("TYPECHAT_PIC_OUT")){
                    main_single_list mainSingleList11=new main_single_list(content,0,main_single_list.TYPECHAT_PIC_OUT,sendtime);
                    mainSingleLists.add(mainSingleList11);
                }else if (type.equals("TYPECHAT_PIC_IN")){
                    main_single_list mainSingleList11=new main_single_list(content,0,main_single_list.TYPECHAT_PIC_IN,sendtime);
                    mainSingleLists.add(mainSingleList11);
                }
                Message message1=new Message();
                message1.what=1;
                handler.sendMessage(message1);
            }
        }
    }
    //添加图片发送功能
    private void buttom_more(){
        //more button的点击事件
        more_button.setOnClickListener(new View.OnClickListener() {
            int i=3;
            @Override
            public void onClick(View view) {
                if (i%2==1){
                    buttom_more_ll.setVisibility(View.VISIBLE);
                }else {
                    buttom_more_ll.setVisibility(View.GONE);
                }
                i++;
                while (i==7){
                    i=3;
                }
                System.out.println("buttom_more计算值"+i);
            }
        });

    }
    //发送文本信息
    protected void send_text_message(){
        String bind_query_result=bindSaveUserid.query(main_context);
        input_content=input_text.getText().toString();
        TextMessage textMessage=TextMessage.obtain(input_content);
        io.rong.imlib.model.Message message= io.rong.imlib.model.Message.obtain(bind_query_result, Conversation.ConversationType.PRIVATE,textMessage);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(io.rong.imlib.model.Message message) {
            }
            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                String sendtime=long_to_time(message.getSentTime());
                main_single_list mainSingleList9=new main_single_list(input_content,0,main_single_list.TYPECHAT_OUT,sendtime);
                mainSingleLists.add(mainSingleList9);
                Message message1=new Message();
                message1.what=1;
                handler.sendMessage(message1);

                String userid_self=RongIMClient.getInstance().getCurrentUserId();//直接通过sdk获取userid
                long sendtime_self=message.getSentTime();
                String sendtime_self_string=long_to_time(sendtime_self);
                my_data.add_data(userid_self,input_content,"TYPECHAT_OUT",sendtime_self_string); //添加到自己的数据库
                Log.d("ddd", "发送文本信息成功");
            }
            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
            }
        });
    }
    //接收文本信息
    protected  void rec_text_message(MessageContent messageContent,String send_userid,String sendtime){
        if (messageContent instanceof TextMessage) {
            textMessage = (TextMessage) messageContent;
            String receiveMessage=textMessage.getContent();
            System.out.println("这个？"+receiveMessage);
            if (receiveMessage.contains("meizitian357753357753")){
                final String name=receiveMessage.substring(21);
                //来确认绑定的消息
                notif();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(Message msg){
                                alertDialog.setTitle(name+"打算暗中绑定你");
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("绑定？不绑定？");
                                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        bind_answer("NOP",name);
                                    }
                                });
                                alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        bind_answer("YES",name);
                                        bindSaveUserid.save(main_context,name);
                                    }
                                });
                                alertDialog.show();
                            }
                        }.obtainMessage().sendToTarget();
                    }
                }).start();

            }else if (receiveMessage.contains("zezier159951159951")){
                //对方有回答
                String answer=receiveMessage.substring(18,21);
                String name=receiveMessage.substring(21);
                System.out.println("回调："+answer+"回调："+name);
                if (answer.equals("YES")){
                    System.out.println("answer:YES");
                    bindSaveUserid.save(main_context,name);
                    Intent intent=new Intent("com.meizitian.meizi.YES");
                    main_context.sendBroadcast(intent);
                }
                if (answer.equals("NOP")){
                    System.out.println("answer:NOP");
                    Intent intent=new Intent("com.meizitian.meizi.NOP");
                    main_context.sendBroadcast(intent);
                }
            }else{
                notif();
                main_single_list mainSingleList8=new main_single_list(receiveMessage,0,main_single_list.TYPECHAT_IN,sendtime);
                mainSingleLists.add(mainSingleList8);
                my_data.add_data(send_userid,receiveMessage,"TYPECHAT_IN",sendtime); //添加到自己的数据库
                Message message1=new Message();
                message1.what=1;
                handler.sendMessage(message1);
            }
        } else {
        }
    }
    //发送图片消息
    protected void send_pic_message(){
        String bind_query_result=bindSaveUserid.query(main_context);
        System.out.println("进入发送图片程序chat_pic_content:"+chat_pic_content);
        ImageMessage image_message=ImageMessage.obtain(Uri.parse(chat_pic_content),Uri.parse(chat_pic_content),true);
        io.rong.imlib.model.Message message= io.rong.imlib.model.Message.obtain(bind_query_result, Conversation.ConversationType.PRIVATE,image_message);
        RongIMClient.getInstance().sendImageMessage(message, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(io.rong.imlib.model.Message message) {
                System.out.println("发送图片进入onattached");
            }
            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
                System.out.println("发送图片出错："+errorCode);
            }
            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                String userid_self=RongIMClient.getInstance().getCurrentUserId();//直接通过sdk获取userid
                long sendtime_self=message.getSentTime();
                String sendtime_string=long_to_time(sendtime_self);

                main_single_list mainSingleList6=new main_single_list(chat_pic_content,0,main_single_list.TYPECHAT_PIC_OUT,sendtime_string);
                mainSingleLists.add(mainSingleList6);
                Message message1=new Message();
                message1.what=1;
                handler.sendMessage(message1);
                my_data.add_data(userid_self,chat_pic_content,"TYPECHAT_PIC_OUT",sendtime_string); //添加到自己的数据库
                System.out.println("添加到数据库的图片地址："+chat_pic_content);
                Log.d("ddd", "发送图片消息成功");

            }
            @Override
            public void onProgress(io.rong.imlib.model.Message message, int i) {
                System.out.println("正在发送进度"+i);
            }
        });

    }
    //接收图片消息
    protected void rec_pic_message(MessageContent messageContent, final String send_userid_2, final String sendtime){
        if (messageContent instanceof ImageMessage) {
            imageMessage = (ImageMessage) messageContent;
            final String receiveMessage = imageMessage.getRemoteUri().toString();//得到图片网址
            System.out.println("receiveMessage:"+receiveMessage);
            get_chat_pic getpic=new get_chat_pic();//下载并保存图片返回保存好的图片地址
            getpic.retuan_a_pic(receiveMessage, new get_chat_pic_callback() {
                @Override
                public void success(String pic_path) {
                    main_single_list mainSingleList8=new main_single_list(pic_path,0,main_single_list.TYPECHAT_PIC_IN,sendtime);
                    mainSingleLists.add(mainSingleList8);
                    System.out.println("添加到数据库的路径"+pic_path);
                    my_data.add_data(send_userid_2,pic_path,"TYPECHAT_PIC_IN",sendtime); //添加到自己的数据库
                    Message message1=new Message();//展示
                    message1.what=1;
                    handler.sendMessage(message1);
                }
            });
            /**
            RongIMClient.getInstance().downloadMedia(Conversation.ConversationType.PRIVATE, send_userid_2, RongIMClient.MediaType.IMAGE, receiveMessage, new RongIMClient.DownloadMediaCallback() {
                @Override
                public void onProgress(int i) {
                }
                @Override
                public void onSuccess(String s) {
                    System.out.println("下载后的数据："+s);

                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
             **/
            // http://7i7gc6.com1.z0.glb.clouddn.com/emoji.png 官网调试的url
            /**
             * 不知道此处的本地地址什么意思
            String receiveMessage2 = imageMessage.getLocalUri().toString();
            System.out.println("receiveMessage2:"+receiveMessage2);
             **/
            notif();
            /**
            main_single_list mainSingleList8=new main_single_list(receiveMessage,0,main_single_list.TYPECHAT_PIC_IN);
            mainSingleLists.add(mainSingleList8);
            my_data.add_data(send_userid,receiveMessage,"TYPECHAT_PIC_IN"); //添加到自己的数据库
            Message message1=new Message();
            message1.what=1;
            handler.sendMessage(message1);
             **/
        }
    }
    //获取本地相册图片的路径
    public void get_pic(){
        Intent getpic=new Intent(Intent.ACTION_GET_CONTENT);
        getpic.setType("image/*");
        startActivityForResult(getpic,123);
    }
    //获取本地相册图片的路径并赋值给chat_pic_content
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.e("get_pic", "ActivityResult resultCode error，获取图片URL的过程中出错");
            return;
        }
        if (requestCode==123){
            chat_pic_content=null;
            System.out.println("清除chat_pic_content:"+chat_pic_content);
            try{
                Uri originalUri = data.getData();  //获得图片的uri
                System.out.println("originalUri:"+originalUri);
                String uri_string=originalUri.toString();
                System.out.println("uri.tostring:"+uri_string);

                Bitmap bitmap=MediaStore.Images.Media.getBitmap(main_context.getContentResolver(),originalUri);
                int width=bitmap.getWidth();
                int height=bitmap.getHeight();
                System.out.println("图片大小："+width+"*"+height);
                if (width>4096||height>4096){
                    Toast.makeText(main_context,"图片太大",Toast.LENGTH_SHORT).show();
                }else {
                    chat_pic_content=uri_string;
                    System.out.println("chat_pic_content:"+chat_pic_content);
                    send_pic_message();
                    /**
                    main_single_list mainSingleList6=new main_single_list(uri_string,0,main_single_list.TYPECHAT_PIC_OUT);
                    mainSingleLists.add(mainSingleList6);
                    Message message1=new Message();
                    message1.what=1;
                    handler.sendMessage(message1);
                     **/
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //list_event 点击
    AdapterView.OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            System.out.println("长按了list位置："+i);
            String list_gettype= mainSingleLists.get(i).getType();
            String list_getsentime=mainSingleLists.get(i).getSendTime();
            System.out.println(list_gettype);
            if (list_gettype.equals("TYPECHAT_OUT")||list_gettype.equals("TYPECHAT_IN")){
                System.out.println(list_getsentime);
                Toast.makeText(main_context,"发送时间："+list_getsentime,Toast.LENGTH_LONG).show();
            }
            return false;
        }
    };
    AdapterView.OnItemClickListener ItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String list_gettype= mainSingleLists.get(i).getType();
            System.out.println(list_gettype);
            if (list_gettype.equals("TYPECHAT_PIC_OUT")||list_gettype.equals("TYPECHAT_PIC_IN")){
                System.out.println("点击了mainlist位置;"+i);
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse(mainSingleLists.get(i).getContent()), "image/*");
                startActivity(it);
            }
        }
    };

    //时间转换
    protected String long_to_time(long longs){
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//格式：  "HH:mm:ss"也可以
        java.util.Date dt = new Date(longs);
        String sendtime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
        return sendtime;
    }
    //返回按键
    public void backpressed(){
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i==KeyEvent.KEYCODE_BACK||keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    System.out.println("进入key监听事件");
                    manager.cancel(1);
                }
                if (i==KeyEvent.KEYCODE_HOME&&keyEvent.getAction()==KeyEvent.ACTION_UP){
                    System.out.println("进入key监听事件");
                    manager.cancel(1);
                }
                return false;
            }
        });
    }
    //未读消息
    public void unread(){
        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                System.out.println("进入未读消息service");
                if (integer>0){
                    NotificationManager manager= (NotificationManager) main_context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder notification=new Notification.Builder(main_context);
                    notification.setContentText("有"+integer+"消息");
                    Notification notif= notification.build();
                    manager.notify(1,notif);
                }
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
    //IM本地端查询聊天记录
    public void getmessage(){
        System.out.println("getmessage!!!");
        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, "meizi2",-1,-1, new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
            @Override
            public void onSuccess(List<io.rong.imlib.model.Message> messages) {

                System.out.println(messages.size()); //本地化消息的数量
                int i=messages.size();
                for (int h=i-1;h>=0;h--){
                    MessageContent messageContent=messages.get(h).getContent();
                    TextMessage textMessage= (TextMessage) messageContent;
                    String receiveMessage=textMessage.getContent();
                    String send_time=long_to_time(messages.get(h).getSentTime());
                    if (receiveMessage.contains("from id:meizi2 ")){
                        receiveMessage=receiveMessage.substring(14);
                        main_single_list mainSingleList10=new main_single_list(receiveMessage,0,main_single_list.TYPECHAT_IN,send_time);
                        mainSingleLists.add(mainSingleList10);
                    }
                    if (receiveMessage.contains("from id:meizi ")){
                        receiveMessage=receiveMessage.substring(13);
                        main_single_list mainSingleList9=new main_single_list(receiveMessage,0,main_single_list.TYPECHAT_OUT,send_time);
                        mainSingleLists.add(mainSingleList9);
                    }
                }
                /**
                 MessageContent messageContent=messages.get(2).getContent();
                 TextMessage textMessage= (TextMessage) messageContent;
                 main_single_list mainSingleList10=new main_single_list(textMessage.getContent(),0,main_single_list.TYPECHAT_IN);
                 mainSingleLists.add(mainSingleList10);
                 **/
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        Message message1=new Message();
        message1.what=1;
        handler.sendMessage(message1);
    }
}
/**
 *
 *
 main_listView.setOnItemClickListener(itemClickListener);
 设置itemonclicklistener
 AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
mainSingleLists.get(i);
System.out.println(view.getLayerType());
System.out.println(view);
System.out.println(view.getId());
}
};
**/

/**
 * 调用音乐播放器
 System.out.println(url);
 String mus="http://218.76.94.43/m10.music.126.net/20161209115054/04bffd6a28b069a2e0f199107ccbdb79/ymusic/a19d/0542/3f4a/22f668de5d922babf9720154b6de09bb.mp3?wshc_tag=0&wsts_tag=584a2442&wsid_tag=76fbaab5&wsiphost=ipdbm";
 String extension = MimeTypeMap.getFileExtensionFromUrl(mus);
 String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
 Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
 mediaIntent.setDataAndType(Uri.parse(mus), mimeType);
 startActivity(mediaIntent);
 **/