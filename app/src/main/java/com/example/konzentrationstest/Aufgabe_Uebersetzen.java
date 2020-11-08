package com.example.konzentrationstest;

import android.content.Context;
import android.content.SharedPreferences;
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
    int punkte = 0;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    final String KEY = "speicherPreferences3";
    PopUpFenster pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__uebersetzen);

        timer = findViewById(R.id.countDownBar4);

        int m = 10000;
        z = new Zeit(timer, m);

        punkte = 0;

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public void check(View view) {
        punkte = 5000;
        TopScore.highscore_formen = punkte;
        boolean neuerHighScore = false;
        if (preferences.getInt(KEY, 0) < TopScore.highscore_formen) {
            preferencesEditor.putInt(KEY, TopScore.highscore_formen);
            neuerHighScore = true;
        }
        preferencesEditor.putInt("key", TopScore.highscore_formen);
        preferencesEditor.commit();

        punkte = 1000;
        pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore);
        pop.showExitContinueWindow();
        punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
        return;
    }
}