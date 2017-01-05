package com.meizitian.meizi;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;


/**
 * Created by Administrator on 2016/12/12.
 */

public class main_list_adapter extends ArrayAdapter<main_single_list> {
    private int viewresourceid;
    private int f=2;
    private music_play_service.music_play musicPlay;

    public main_list_adapter(Context context, int textViewResourceId, List<main_single_list> objects) {
        super(context,textViewResourceId, objects);
        viewresourceid=textViewResourceId;
    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        main_single_list mainSingleList=getItem(position);
        View view;
        final Viewholder viewholder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(viewresourceid,null);
            viewholder=new Viewholder();
            viewholder.left_chat_ll= (LinearLayout) view.findViewById(R.id.main_single_list_leftchat_ll);
            viewholder.right_chat_ll= (LinearLayout) view.findViewById(R.id.main_single_list_rightchat_ll);
            viewholder.left_chat_textview= (TextView) view.findViewById(R.id.main_single_list_leftchat_textview);
            viewholder.right_chat_textview= (TextView) view.findViewById(R.id.main_single_list_rightchat_textview);
            viewholder.left_chat_pic_ll= (LinearLayout) view.findViewById(R.id.main_single_list_leftchat_pic_ll);
            viewholder.right_chat_pic_ll= (LinearLayout) view.findViewById(R.id.main_single_list_rightchat_pic_ll);
            viewholder.left_chat_pic_imageview= (ImageView) view.findViewById(R.id.main_single_list_leftchat_pic_imageview);
            viewholder.right_chat_pic_imageview= (ImageView) view.findViewById(R.id.main_single_list_rightchat_pic_imageview);

            viewholder.pic_ll= (LinearLayout) view.findViewById(R.id.main_single_list_pic_ll);
            viewholder.music_ll= (LinearLayout) view.findViewById(R.id.main_single_list_music_ll);
            viewholder.music_imageview= (ImageView) view.findViewById(R.id.main_single_list_music_imageview);
            viewholder.musictext_view= (TextView) view.findViewById(R.id.main_single_list_music_textview);

            view.setTag(viewholder);
        }else {
            view=convertView;
            viewholder= (Viewholder) view.getTag();
        }
       if (mainSingleList.getType() == main_single_list.TYPECHAT_IN){
           viewholder.left_chat_ll.setVisibility(View.VISIBLE);
           viewholder.right_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_ll.setVisibility(View.GONE);
           viewholder.right_chat_pic_ll.setVisibility(View.GONE);
           viewholder.pic_ll.setVisibility(View.GONE);
           viewholder.music_ll.setVisibility(View.GONE);
           viewholder.left_chat_textview.setText(mainSingleList.getContent());
           }else if(mainSingleList.getType() == main_single_list.TYPECHAT_OUT) {
           viewholder.right_chat_ll.setVisibility(View.VISIBLE);
           viewholder.left_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_ll.setVisibility(View.GONE);
           viewholder.right_chat_pic_ll.setVisibility(View.GONE);
           viewholder.pic_ll.setVisibility(View.GONE);
           viewholder.music_ll.setVisibility(View.GONE);
           viewholder.right_chat_textview.setText(mainSingleList.getContent());
           } else if (mainSingleList.getType()==main_single_list.TYPECHAT_PIC_IN) {
           viewholder.right_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_ll.setVisibility(View.VISIBLE);
           viewholder.right_chat_pic_ll.setVisibility(View.GONE);
           viewholder.pic_ll.setVisibility(View.GONE);
           viewholder.music_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_imageview.setImageURI(Uri.parse(mainSingleList.getContent()));
           } else if (mainSingleList.getType()==main_single_list.TYPECHAT_PIC_OUT) {
           viewholder.right_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_ll.setVisibility(View.GONE);
           viewholder.right_chat_pic_ll.setVisibility(View.VISIBLE);
           viewholder.pic_ll.setVisibility(View.GONE);
           viewholder.music_ll.setVisibility(View.GONE);
           viewholder.right_chat_pic_imageview.setImageURI(Uri.parse(mainSingleList.getContent()));
           }else if (mainSingleList.getType() == main_single_list.TYPECHAT_MUSIC) {
           viewholder.right_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_ll.setVisibility(View.GONE);
           viewholder.left_chat_pic_ll.setVisibility(View.GONE);
           viewholder.right_chat_pic_ll.setVisibility(View.GONE);
           viewholder.pic_ll.setVisibility(View.GONE);
           viewholder.music_ll.setVisibility(View.VISIBLE);
           viewholder.musictext_view.setText(mainSingleList.getContent());
           viewholder.music_imageview.setImageResource(mainSingleList.getMusic_bg());
       }
        viewholder.music_imageview.setOnClickListener(music_imageview_onClickListener);
        viewholder.music_ll.setOnClickListener(musictext_view_onclickListener);
        //以上两项监听目前已经被注释掉
        //此处的button监听没反应
                /**
                Intent intent=new Intent(getContext(),music_play_service.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (f%2==0){
                    getContext().startService(intent);
                    System.out.println(f);
                    f++;
                }else {

                    getContext().stopService(intent);//无法停止服务
                    System.out.println(f);
                    f++;
                }
                 **/

                /**
                String mus="http://124.228.90.37/m10.music.126.net/20161214204647/b1aa05b36d1ee0e13d06db74e1b089df/ymusic/0640/132e/5d84/f475cd0415cb65c6fb6b5b18ad5273b8.mp3?wshc_tag=1&wsts_tag=5851395c&wsid_tag=76fbb457&wsiphost=ipdbm";
                String extension = MimeTypeMap.getFileExtensionFromUrl(mus);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
                mediaIntent.setDataAndType(Uri.parse(mus), mimeType);
                getContext().startActivity(mediaIntent);
                 **/

        return view;
    }

    private class Viewholder{
        LinearLayout left_chat_ll;
        LinearLayout right_chat_ll;
        TextView left_chat_textview;
        TextView right_chat_textview;
        LinearLayout left_chat_pic_ll;
        LinearLayout right_chat_pic_ll;
        ImageView left_chat_pic_imageview;
        ImageView right_chat_pic_imageview;

        LinearLayout pic_ll;
        LinearLayout music_ll;
        ImageView music_imageview;
        TextView musictext_view;

    }

    //后台服务播放播放music play service
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicPlay= (music_play_service.music_play) iBinder;
            musicPlay.start_2(getContext());
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //musicPlay.stop();
        }
    };

    //绑定解绑music player
    View.OnClickListener music_imageview_onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent bintent=new Intent(getContext(),music_play_service.class);
            if (f%2==0){
                //getContext().bindService(bintent,connection,Context.BIND_AUTO_CREATE);
                System.out.println(f);
                f++;
            }else {
                //getContext().unbindService(connection);
                System.out.println(f);
                f++;
            }
        }
    };

    //用class的方式测试后台播放music
    View.OnClickListener musictext_view_onclickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(),test.class);
            //getContext().startActivity(intent);
        }
    };

    public void init(){
        View view_main;
        view_main=LayoutInflater.from(getContext()).inflate(R.layout.main,null);
    }

//// TODO: 2016/12/20
// 问题一：
// 完成：目前建立好了database 用来储存数据 实现了get到inputtext的内容用sendbutton来发送
// 待完善：但是不能显示出来 因为inputtext的数据在adapter里面，而显示的方法在fragment_main里面 在adapter里面的button监听没反应 试着调到fragment_main里面去
// 解决方案：1.预计，每次发送都先储存下来，然后统一和接受的消息一起从数据库里面展示出来
//           2.或者查看IM的文档 从服务器里面找到发出去的数据
//           3.或者直接通过intent、广播传递数据过去
// 实际解决：send_button的onclicklistener监听在adapter里面无法动作，改到fragment后可行，同时也解决了input_text数据的传递问题
//
//
// 问题二：
//       纯文字消息的保存（退出应用后list的数据更新会被释放）
// 已经解决，官方文档有本地记录的获取方式
//       sdk自带的本地化消息是全部储存在一起的 需要区分发出和接收
// 已经解决一半，用非常笨拙的方式，只能针对两个id
// 已经解决，通过注册与绑定
//       无法做到oncreate的时候直接显示上次的消息
// 已经解决一半，不过listview展示有缺陷 每次都得从上滚动到下
//       数据库增加图片的储存与操作 完成图片的发送接收
//       setting页面的setting选项 翻译等功能 监听复制事件即时翻译
//       setting页面tips
//       main页面上下滑动展示软键盘
//       main页面监听回车键
//       sendmessage的方式需要更改 过时了
// 已经解决
//
//

}
