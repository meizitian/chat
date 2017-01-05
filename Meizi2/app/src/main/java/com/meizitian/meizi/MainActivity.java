package com.meizitian.meizi;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import io.rong.imlib.RongIMClient;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout top_main_ll;
    private LinearLayout top_setting_ll;
    private ImageView top_main_imageview;
    private ImageView top_setting_imageview;
    private List<Fragment> fragmentList;
    private ViewPager main_viewpager;
    private fragment_adapter fragmentAdapter;
    private Context mainactivity_context;
    private Intent intent;

    private mydataacount dataacount;
    private List<String> acount_list;
    private String TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainactivity_context=getApplicationContext();
        init_fragmentlist();
        init_view();
        init_event();
        fragmentAdapter=new fragment_adapter(getSupportFragmentManager(),fragmentList);
        main_viewpager.setAdapter(fragmentAdapter);
        main_viewpager.setCurrentItem(1);
        dataacount=new mydataacount();
        dataacount.create_acount_data(mainactivity_context);
        acount_list=dataacount.query_acount_data();
        intent=new Intent(mainactivity_context,register_acount.class);

        if (acount_list.size()!=0){
            //如果有数据
            TOKEN=acount_list.get(2);
            //IM
            /**
             * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
             * io.rong.push 为融云 push 进程名称，不可修改。
             */
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                    "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
                RongIMClient.init(this);
            }
            /**
            String meizi="cdIaV7qHWm/KJ36WFf18T+ZedBFUEP1+2/l050hpcuB2uVyD2ZJT0/xDIUAYoEkVxxuzwpuAscOQ48Di2JY8ag==";
            String meizi2="mjN3FvBK9DpMUBF25PL/neZedBFUEP1+2/l050hpcuB2uVyD2ZJT087881DMzzEpF594SlgnxjfHC76NrVCllQ==";
             **/
            connect(TOKEN);
        }else {
            startActivity(intent);
        }
    }
/**
    @Override
    protected void onResume() {
        super.onResume();
        if (acount_list.size()==0){
            startActivity(intent);
        }
    }
    **/

    protected void init_fragmentlist(){
        fragmentList=new ArrayList<>();
        fragmentList.add(new fragment_setting());
        fragmentList.add(new fragment_main());
    }
    protected void init_view(){
        top_main_ll= (LinearLayout) findViewById(R.id.top_main_ll);
        top_setting_ll= (LinearLayout) findViewById(R.id.top_setting_ll);
        top_main_imageview= (ImageView) findViewById(R.id.top_main_imageview);
        top_setting_imageview= (ImageView) findViewById(R.id.top_setting_imageview);
        main_viewpager= (ViewPager) findViewById(R.id.mainactivity_viewpager);
    }
    protected void init_event(){
        top_main_ll.setOnClickListener(this);
        top_setting_ll.setOnClickListener(this);
        main_viewpager.addOnPageChangeListener(pagerlistiner);
    }
    @Override
    public void onClick(View view) {
        init_ba();
        switch (view.getId()){
            case R.id.top_setting_ll:
                main_viewpager.setCurrentItem(0);
                top_setting_imageview.setImageResource(R.drawable.setting_0);
                break;
            case R.id.top_main_ll:
                main_viewpager.setCurrentItem(1);
                top_main_imageview.setImageResource(R.drawable.main_0);
                break;
            default:
                break;
        }
    }
    protected void init_ba(){
        top_setting_imageview.setImageResource(R.drawable.setting_1);
        top_main_imageview.setImageResource(R.drawable.main_1);
    }

    ViewPager.OnPageChangeListener pagerlistiner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            init_ba();
            int item=main_viewpager.getCurrentItem();
            switch (item){
                case 0:
                    top_setting_imageview.setImageResource(R.drawable.setting_0);
                    break;
                case 1:
                    top_main_imageview.setImageResource(R.drawable.main_0);
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    //IM
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public void connect(String token) {

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("LoginActivity", "--onSuccess---" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
}
