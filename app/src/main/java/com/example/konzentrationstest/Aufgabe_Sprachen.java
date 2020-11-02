package com.example.konzentrationstest;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// wie soll das aussehen? Hat eher weniger mit Konzentration zu tun als vielmehr mit Vokabular, vielleicht zum Schluss machen als Extra
public class Aufgabe_Sprachen extends AppCompatActivity {

//    String [][] woerter_englisch = new String[][]{{"house", "car", "keyboard"}, {}, {}};

//    String [][] woerter_deutsch = {{"Haus", "Auto", "Tastatur"}, {}, {}};

    String [] woerter_englisch = new String[] {"house", "car", "keyboard", ""};

    String [] woerter_deutsch = new String [] {"Haus", "Auto", "Tastatur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__uebersetzen);
    }

    public void check(View view) {

    }
}