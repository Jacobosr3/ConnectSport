package com.example.connectsport.utilities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.connectsport.R;
import com.example.connectsport.termsofservice.Fragment1;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Se establece un retraso de 2 segundos
        Handler handler = new Handler();
        ImageView s = findViewById(R.id.s);
        Animation myanim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_s);
        s.startAnimation(myanim);
        ImageView c = findViewById(R.id.c);
        Animation myanim2 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_c);
        c.startAnimation(myanim2);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Se inicia la actividad principal
                startActivity(new Intent(SplashActivity.this, Fragment1.class));
                finish();
            }
        }, 5000);
    }
}