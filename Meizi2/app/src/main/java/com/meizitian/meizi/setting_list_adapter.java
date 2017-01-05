package com.meizitian.meizi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class setting_list_adapter extends ArrayAdapter<setting_single_list> {

    private int viewresourceid;
    public setting_list_adapter(Context context,int textViewResourceId, List<setting_single_list> objects) {
        super(context,textViewResourceId, objects);
        viewresourceid=textViewResourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        setting_single_list settingSingleList=getItem(position);
        View view1= LayoutInflater.from(getContext()).inflate(viewresourceid,null);
        ImageView imageView= (ImageView) view1.findViewById(R.id.setting_single_list_imageview);
        TextView textView= (TextView) view1.findViewById(R.id.setting_single_list_textview);
        imageView.setImageResource(settingSingleList.getImage_id());
        textView.setText(settingSingleList.getSetting_list_text());
        /**
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText()=="绑定测试"){
                    Intent n=new Intent(getContext(),bind_chat.class);
                    getContext().startActivity(n);
                }
                if (textView.getText()=="list5"){
                    System.out.println("dsfsdasgdagsdgasdgas");
                }
            }
        });
        **/
        return view1;
    }


}
