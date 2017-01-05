package com.meizitian.meizi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class fragment_setting extends android.support.v4.app.Fragment {
    private ListView setting_listView;
    private View view;
    private List<setting_single_list> settingSingleLists;
    private setting_list_adapter settingListAdapter;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=LayoutInflater.from(getContext()).inflate(R.layout.setting,null);
        init_view();
        init_datas();
        settingListAdapter=new setting_list_adapter(getContext(),R.layout.setting_single_list_layout,settingSingleLists);
        setting_listView.setAdapter(settingListAdapter);
        setting_listView.setOnItemClickListener(itemClickListener);

        return view;
    }

    protected void init_view(){
        setting_listView= (ListView) view.findViewById(R.id.setting_listview);
        settingSingleLists=new ArrayList<>();
        imageView= (ImageView) view.findViewById(R.id.setting_imageview);
    }

    protected void init_datas(){
        setting_single_list list1=new setting_single_list(R.mipmap.ic_launcher,"我");
        settingSingleLists.add(list1);
        setting_single_list list2=new setting_single_list(R.mipmap.ic_launcher,"内测设置");
        settingSingleLists.add(list2);
        setting_single_list list3=new setting_single_list(R.mipmap.ic_launcher,"功能");
        settingSingleLists.add(list3);
        /**
        setting_single_list list4=new setting_single_list(R.mipmap.ic_launcher,"list4");
        settingSingleLists.add(list4);
        setting_single_list list5=new setting_single_list(R.mipmap.ic_launcher,"list5");
        settingSingleLists.add(list5);
        setting_single_list list6=new setting_single_list(R.mipmap.ic_launcher,"list6");
        settingSingleLists.add(list6);
        setting_single_list list7=new setting_single_list(R.mipmap.ic_launcher,"list7");
        settingSingleLists.add(list7);
         **/
        setting_single_list list8=new setting_single_list(R.mipmap.ic_launcher,"关于");
        settingSingleLists.add(list8);
        setting_single_list list9=new setting_single_list(R.mipmap.ic_launcher,"test");
        settingSingleLists.add(list9);
    }

    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //setting_single_list j=settingSingleLists.get(i);
            int id=view.getId();
            System.out.println(id);
            System.out.println(i);
            System.out.println(view);
            //以上id全部为-1 i为listview中的位置 view为linearlayout类型
            switch (i){
                case 0:
                    Intent w=new Intent(getContext(),setting_me.class);
                    getContext().startActivity(w);
                    break;
                case 1:
                    //内侧设置
                    Intent n=new Intent(getContext(),bind_chat.class);
                    getContext().startActivity(n);
                    break;
                case 2:
                    Intent s=new Intent(getContext(),setting_tools.class);
                    getContext().startActivity(s);
                    break;
                case 3:
                    Intent about=new Intent(getContext(),setting_about.class);
                    getContext().startActivity(about);
                    break;
                case 4:
                    String d= Environment.getRootDirectory().getAbsolutePath();
                    System.out.println(d);
                    break;
                default:
                    break;
            }
        }
    };
}
