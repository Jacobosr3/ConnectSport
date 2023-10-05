package com.example.connectsport.termsofservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connectsport.R;

public class Fragment1 extends AppCompatActivity {

    TextView siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1);

        siguiente = findViewById(R.id.siguiente);

        siguiente.setOnClickListener(view -> {
            Intent i = new Intent(Fragment1.this, Fragment2.class);
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