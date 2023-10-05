package com.example.connectsport.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.connectsport.R;
import com.example.connectsport.termsofservice.Fragment1;
import com.example.connectsport.termsofservice.Fragment2;

public class MainActivity extends AppCompatActivity {

    TextView siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        siguiente = findViewById(R.id.siguiente);

        siguiente.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, Fragment1.class);
            startActivity(i);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        });
    }
}