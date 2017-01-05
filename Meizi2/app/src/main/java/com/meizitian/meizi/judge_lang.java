package com.meizitian.meizi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/12.
 */

public class judge_lang {
    public boolean is_zh(String string){
        boolean s=false;
        Pattern p= Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(string);
        if(m.find()){
            s=true;
        }else {
            s=false;
        }
        return s;
    }
}
