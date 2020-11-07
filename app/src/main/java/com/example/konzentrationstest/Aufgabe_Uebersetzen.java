package com.example.konzentrationstest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

// wie soll das aussehen? Hat eher weniger mit Konzentration zu tun als vielmehr mit Vokabular, vielleicht zum Schluss machen als Extra
public class Aufgabe_Uebersetzen extends AppCompatActivity {

//    String [][] woerter_englisch = new String[][]{{"house", "car", "keyboard"}, {}, {}};

//    String [][] woerter_deutsch = {{"Haus", "Auto", "Tastatur"}, {}, {}};

    String [] woerter_englisch = new String[] {"house", "car", "keyboard", ""};

    String [] woerter_deutsch = new String [] {"Haus", "Auto", "Tastatur"};

    Zeit z;
    ProgressBar timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__uebersetzen);

        timer = findViewById(R.id.countDownBar4);

        int m = 10000;
        z = new Zeit(timer, m);
    }

    public void check(View view) {

    }
}