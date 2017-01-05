package com.meizitian.meizi;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.webkit.MimeTypeMap;

public class music_play_service extends Service {

    private music_play music_play =new music_play();
    //final MediaPlayer d=new MediaPlayer();
    private MediaPlayer d;
    class  music_play extends Binder{

        public void start_1(){
            String mus="http://218.76.94.41/m10.music.126.net/20161215112147/9b2b6cb0ac29f6ba3571e452684eeaa9/ymusic/1f73/59d1/5c45/85b045dc4c72381e960f58f252321377.mp3?wshc_tag=1&wsts_tag=58520670&wsid_tag=76fbb5a2&wsiphost=ipdbm";
            String extension = MimeTypeMap.getFileExtensionFromUrl(mus);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
            mediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mediaIntent.setDataAndType(Uri.parse(mus), mimeType);
            startActivity(mediaIntent);
        }
        public void start_2(Context context){
            d=MediaPlayer.create(context,Uri.parse("http://218.76.94.42/m10.music.126.net/20161215225629/51d3b281084b48ea90a0890e447cc696/ymusic/aaf5/0dce/0e0a/f86145ecdea37f05391d34ed32684bcd.mp3?wshc_tag=1&wsts_tag=5852a942&wsid_tag=76fbb5a2&wsiphost=ipdbm"));
            if (!d.isPlaying()){
                try{
                    //d.setDataSource("http://218.76.94.41/m10.music.126.net/20161215131222/a87c6f36ef3f423605d22677334b7123/ymusic/1f73/59d1/5c45/85b045dc4c72381e960f58f252321377.mp3?wshc_tag=1&wsts_tag=5852205c&wsid_tag=76fbb5a2&wsiphost=ipdbm");
                    //d.prepareAsync();
                    d.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            d.start();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        public void stop(){
            d.stop();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onbind");
        return music_play;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("onunbind");
        if (d.isPlaying()){
            d.stop();
            d.release();
        }
        //stopSelf();
        return super.onUnbind(intent);
    }
    @Override
    public void onCreate() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        //d.stop();
        System.out.println("ondestory");
        //stopSelf();
        super.onDestroy();
    }
}
