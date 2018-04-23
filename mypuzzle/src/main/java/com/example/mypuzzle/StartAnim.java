package com.example.mypuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by WCY on 2017/7/12.
 */

public class StartAnim extends Activity {
    private ImageView gif_show;
    private AnimationDrawable anim;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_anim);
        mediaPlayer = MediaPlayer.create(this,R.raw.anim);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);
//        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
//        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        tv.setTypeface(Typefaces.get(this, "cute.ttf"));
        new Titanic().start(tv);
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent i = new Intent(StartAnim.this,FirstView.class);
                //通过Intent打开最终真正的主界面Main这个Activity
                StartAnim.this.startActivity(i);
                StartAnim.this.finish();    //关闭自己这个开场屏
            }
        }, 6000);   //这个时间根据实际需要更改。


    }
}
