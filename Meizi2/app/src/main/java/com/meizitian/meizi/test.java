package com.meizitian.meizi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//用来测试在musicview list中直接调用webview显示云音乐播放的 但是不会支持frame框架
public class test extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        webView= (WebView) findViewById(R.id.test_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        String html="<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 height=86 src=\"//music.163.com/outchain/player?type=2&id=435996517&auto=1&height=66\"></iframe>";
        String html2="http://music.163.com/#/outchain/2/435996517/m/use";
        String html3="http://www.baidu.com";
        String html4="http://weixin.sogou.com/";
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOnKeyListener(kl);
        webView.loadUrl(html4);
    }
    View.OnKeyListener kl=new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
            }
            return false;
        }
    };
}
