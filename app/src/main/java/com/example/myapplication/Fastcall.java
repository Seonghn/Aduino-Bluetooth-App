package com.example.myapplication;

import android.app.Activity;
import android.media.MediaPlayer;


public class Fastcall extends Activity {


    MediaPlayer mp;

    public void Fastcall(){


        mp = MediaPlayer.create(this, R.raw.gem);


    }

    public void start(){
        mp.start();
    }

    public void stop(){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });

    }

}
