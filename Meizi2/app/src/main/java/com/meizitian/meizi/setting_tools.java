package com.meizitian.meizi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class setting_tools extends AppCompatActivity {
    private ListView setting_tools_listview;
    private setting_tools_list_adpter settingToolsListAdpter;
    private List<settint_tools_single_list> settintToolsSingleLists;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_tools);
        init_view();
        init_datas();

        settingToolsListAdpter=new setting_tools_list_adpter(getApplicationContext(),R.layout.setting_tools_single_list,settintToolsSingleLists);
        setting_tools_listview.setAdapter(settingToolsListAdpter);
        init_event();
    }
    public void init_view(){
        setting_tools_listview= (ListView) findViewById(R.id.setting_tools_listview);
        settintToolsSingleLists=new ArrayList<>();
        imageView= (ImageView) findViewById(R.id.top_setting_imageview);
        imageView.setImageResource(R.drawable.back_1);
    }
    public void init_datas(){
        settint_tools_single_list d=new settint_tools_single_list(R.mipmap.ic_launcher,"快速翻译","开");
        settintToolsSingleLists.add(d);
        settint_tools_single_list d2=new settint_tools_single_list(R.mipmap.ic_launcher,"展示所有消息","关");
        settintToolsSingleLists.add(d2);
    }
    public void init_event(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setting_tools_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ss=settintToolsSingleLists.get(i).getText2();
                System.out.println("功能中的text2的值"+ss);
                //settintToolsSingleLists.add(i,new settint_tools_single_list(R.mipmap.ic_launcher,"dsf",ss));
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class settint_tools_single_list{
        int imageid;
        String text;
        String text2;
        boolean ischeck;
        public settint_tools_single_list(int imageid,String text,String text2){
            this.imageid=imageid;
            this.text=text;
            this.text2=text2;
            //this.ischeck=ischeck;
        }
        public int getImageid(){
            System.out.println(imageid);
            return imageid;
        }
        public String getText(){
            return text;
        }
        public String getText2(){
            return text2;
        }
    }
    // private static boolean ggle=true;
    class setting_tools_list_adpter extends ArrayAdapter<settint_tools_single_list> {
        private int viewlayoutid;
        public setting_tools_list_adpter(Context context, int resourcelayoutid, List<settint_tools_single_list> objects) {
            super(context,resourcelayoutid,objects);
            viewlayoutid=resourcelayoutid;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           settint_tools_single_list settintToolsSingleList=getItem(position);
            View v= LayoutInflater.from(getContext()).inflate(viewlayoutid,null);
            ImageView imageView= (ImageView) v.findViewById(R.id.setting_tools_single_imageview);
            TextView textView= (TextView) v.findViewById(R.id.setting_tools_single_textview);
            TextView textView2= (TextView) v.findViewById(R.id.setting_tools_single_textview2);
            System.out.println(settintToolsSingleList.getImageid());
            imageView.setImageResource(settintToolsSingleList.getImageid());
            System.out.println(settintToolsSingleList.getText());
            textView.setText(settintToolsSingleList.getText());
            System.out.println(settintToolsSingleList.getText2());
            textView2.setText(settintToolsSingleList.getText2());
            /**
            ToggleButton toggleButton0= (ToggleButton) v.findViewById(R.id.setting_tools_single_toggleButton);
            toggleButton0.setChecked(ggle);
            toggleButton0.setOnCheckedChangeListener(checkedChangeListener);
             **/
            return v;
        }
        //本来 功能 选卡里面的listview最右端是由一个togglebutton组成的现改为textview
        /**
        CompoundButton.OnCheckedChangeListener checkedChangeListener=new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getApplicationContext(),b?"open":"close",Toast.LENGTH_SHORT).show();
                ggle=b?true:false;
            }
        };
         **/
    }

}
