package com.example.konzentrationstest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnOnClick(View view) {
        



    }

    public void changeSoundIcon(View view) {
        //ImageView sound = findViewById(R.id.sound_icon);


    }

    public void goToPage2(View view) {
        Intent myIntent = new Intent(MainActivity.this, Page2.class);
        MainActivity.this.startActivity(myIntent);
    }

}