package com.example.connectsport.utilities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.connectsport.R;
import com.example.connectsport.login.MainActivity;
import com.example.connectsport.main.MainBn;
import com.example.connectsport.termsofservice.Fragment1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    boolean ToS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ToS = SharedPrefsUtil.getBoolean(this, "ToS");
        openApp(ToS);

        ImageView s = findViewById(R.id.s);
        Animation myanim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_s);
        s.startAnimation(myanim);
        ImageView c = findViewById(R.id.c);
        Animation myanim2 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.move_c);
        c.startAnimation(myanim2);

    }

    private void openApp(boolean b) {

        // Se establece un retraso de 5 segundos
        Handler handler = new Handler();
        handler.postDelayed(() -> {

            Intent intent;

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                Intent mainIntent = new Intent(SplashActivity.this, MainBn.class);//MainBn
                startActivity(mainIntent);
                finish();
            } else {
                if (b) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, Fragment1.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        }, 5000);
    }
}