package com.meizitian.meizi;

/**
 * Created by Administrator on 2016/12/12.
 */

public class main_single_list {

    public static final String TYPECHAT_IN="TYPECHAT_IN";
    public static final String TYPECHAT_OUT="TYPECHAT_OUT";
    public static final String TYPECHAT_PIC_OUT="TYPECHAT_PIC_OUT";
    public static final String TYPECHAT_PIC_IN="TYPECHAT_PIC_IN";
    public static final String TYPECHAT_PIC="TYPECHAT_PIC";
    public static final String TYPECHAT_MUSIC="TYPECHAT_MUSIC";
    private String content;
    private String type;
    private int music_bg;
    private String sendtime;
    public main_single_list(String content,int music_bg,String type,String sendtime){
        this.content=content;
        this.music_bg=music_bg;
        this.type=type;
        this.sendtime=sendtime;
    }
    public String getContent(){
        return content;
    }
    public String getType(){
        return type;
    }
    public int getMusic_bg(){
        return music_bg;
    }
    public String getSendTime(){
        return sendtime;
    }
}
