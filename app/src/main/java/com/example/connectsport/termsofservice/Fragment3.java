package com.example.connectsport.termsofservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connectsport.R;
import com.example.connectsport.login.MainActivity;
import com.example.connectsport.utilities.SharedPrefsUtil;

public class Fragment3 extends AppCompatActivity {

    Button aceptar;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_3);

        aceptar = findViewById(R.id.aceptar);
        checkBox = findViewById(R.id.checkBox);

        aceptar.setOnClickListener(view -> {

            if (checkBox.isChecked()) {
                SharedPrefsUtil.saveBoolean(getApplicationContext(), "ToS", true);
                Intent i = new Intent(Fragment3.this, MainActivity.class);
                startActivity(i);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                Toast.makeText(Fragment3.this, getString(R.string.acceptToS), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Do not do anything
    }

}