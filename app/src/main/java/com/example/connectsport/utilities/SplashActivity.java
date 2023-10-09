package com.example.connectsport.utilities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.connectsport.R;
import com.example.connectsport.termsofservice.Fragment1;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Se establece un retraso de 2 segundos
        Handler handler = new Handler();
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