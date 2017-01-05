package com.meizitian.meizi;


public class setting_single_list {
    private int image_id;
    private String setting_list_text;
    public setting_single_list(int image_id,String setting_list_text){
        this.image_id=image_id;
        this.setting_list_text=setting_list_text;
    }
    public int getImage_id(){
        return image_id;
    }
    public String getSetting_list_text(){
        return setting_list_text;
    }
}
