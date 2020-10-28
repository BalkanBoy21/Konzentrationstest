package com.example.konzentrationstest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Rechenaufgaben extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechenaufgaben);

        ImageButton btn1 = findViewById(R.id.unwahr);

    }

    public void falsch(View view) {
        System.out.println("Falsch");

    }

    public void richtig(View view) {

    }
}