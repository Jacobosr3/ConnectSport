package com.example.connectsport.termsofservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connectsport.R;

public class Fragment2 extends AppCompatActivity {

    TextView siguiente, atras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_2);

        siguiente = findViewById(R.id.siguiente);
        atras = findViewById(R.id.atras);

        siguiente.setOnClickListener(view -> {
            Intent i = new Intent(Fragment2.this, Fragment3.class);
            startActivity(i);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        });

        atras.setOnClickListener(view -> {
            Intent i = new Intent(Fragment2.this, Fragment1.class);
            startActivity(i);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        });
    }

    @Override
    public void onBackPressed() {
        //Do not do anything
    }

}