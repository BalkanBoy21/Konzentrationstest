package com.example.konzentrationstest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Aufgabe_Farben extends AppCompatActivity {

    private TextView farbText;
    private ImageButton th_down, th_up;
    private ProgressBar timer;
    private View ansicht;

    private String [] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Weiß", "Pink"};
    private int [] farbCodes = new int[farben.length];

    static int punkte;
    Zeit z;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    final String KEY = "speicherPreferences2";
    PopUpFenster pop;

    Dialog epicDialog;
    Button leave, stay;
    TextView text, text2;

    boolean neuerHighScore = true;
    int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__farben);

        epicDialog = new Dialog(this);


        timer = findViewById(R.id.timer_Farben);
        th_down = findViewById(R.id.unwahr2);
        th_up = findViewById(R.id.wahr2);
        farbText = findViewById(R.id.textFarbe);
        ansicht = findViewById(R.id.screen);

        // durchsucht alle Farben in colors.xml (und weitere) und filtert alle Farben heraus, die im Array "farben" enthalten sind
        try {
            Field[] fields = Class.forName(getPackageName() + ".R$color").getDeclaredFields();
            String colorName;
            for (Field farbe: fields) {
                colorName = farbe.getName();
                if (Arrays.asList(farben).contains(colorName)) {
                    int colorId = farbe.getInt(null);
                    int color = getResources().getColor(colorId);
                    farbCodes[Arrays.asList(farben).indexOf(colorName)] = color;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fuer erste Seite
        farbText.setText(farben[(int) (Math.random() * farben.length)]);
        farbText.setTextColor(farbCodes[(int) (Math.random() * farbCodes.length)]);
//        ansicht.setBackgroundColor(farbCodes[(int) (Math.random() * farbCodes.length)]);

        punkte = 0;       // sehr wichtig, da man ins Menue zurueckgehen kann

        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public void ShowPopUp() {
        epicDialog.setContentView(R.layout.activity_popupfenster);
        leave = (Button) epicDialog.findViewById(R.id.verlassen);
        stay = (Button) epicDialog.findViewById(R.id.weiter);

        text = (TextView) epicDialog.findViewById(R.id.anzeigeScore);
        text2 = (TextView) epicDialog.findViewById(R.id.anzeigeHighscore);

        String punkteText = "\n\tPunkte: " + punkte;

        text.setText(punkteText);

        if (neuerHighScore) {
            text2.setText("Neuer Highscore " + preferences.getInt(KEY, 0));
        } else {
            text2.setText("\t\t\t\t\tHighscore: " + preferences.getInt(KEY, 0));
        }

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent myIntent = new Intent(Aufgabe_Farben.this, MainActivity.class);
                Aufgabe_Farben.this.startActivity(myIntent);
            }
        });

        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                epicDialog.dismiss();
            }
        });

        epicDialog.setCancelable(false);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void check(View view) {
        String currentText = farbText.getText().toString();
        int currentColor = farbText.getCurrentTextColor();
        //int currentColor = ((ColorDrawable) ansicht.getBackground()).getColor();
        boolean ergebnisIstRichtig = false;

        if (farbCodes[Arrays.asList(farben).indexOf(currentText)] == farbText.getCurrentTextColor()) {      // "... == currentColor" fuer Background-Color
            ergebnisIstRichtig = true;
        }

        if (((view.getId() == R.id.unwahr2) && ergebnisIstRichtig) || ((view.getId() == R.id.wahr2) && !ergebnisIstRichtig)){   // wenn auf Falsch geklickt wird, das Ergebnis aber richtig ist
            // Managen des HighScores
            neuerHighScore = false;
            // Neuen Highscore setzen
            TopScore.highscore_farben = punkte;

            Log.d("---", preferences.getInt(KEY, 0) + "");
            if (preferences.getInt(KEY, 0) < TopScore.highscore_farben) {
                preferencesEditor.putInt(KEY, TopScore.highscore_farben);
                TopScore.highscore_farben = punkte;
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_farben);
            preferencesEditor.commit();


            ShowPopUp();
            //pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore);
            //pop.showExitContinueWindow();

            //Intent myIntent = new Intent(Aufgabe_Farben.this, PopUpFenster.class);
            //Aufgabe_Farben.this.startActivity(myIntent);

            punkte = 0;

        } else {    // Ergebnis ist richtig
            ++punkte;
            int randomNumber;
            String randomText;
            int randomFarbe;

            // Implementierung, sodass nur die benachbarten Farben ausgewählt werden können um die Häufigkeit zu erhöhen
            do {
                randomNumber = (int) (Math.random() * farben.length);
                if (randomNumber == 0) {
                    //int[] random_array = new int[]{farben.length - 1, 0, 1};
                    int [] random_array = new int[]{0, (int) (Math.random() * farben.length)};
                    randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
                } else if (randomNumber == farben.length - 1) {
                    //int[] random_array = new int[]{farben.length - 2, farben.length - 1, 0};
                    int [] random_array = new int[]{farben.length-1, (int) (Math.random() * farben.length)};
                    randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
                } else {    // fuer alle Zahlen bis auf die aeußersten des Arrays
                    randomFarbe = farbCodes[(randomNumber - 1) + (int) (Math.random() * 3)];
                }
            } while (farben[randomNumber].equals(currentText) || (randomFarbe == currentColor));      // nach jedem Klick eine andere Farbe und ein anderer Text

            randomText = farben[randomNumber];
            farbText.setText(randomText);
            farbText.setTextColor(randomFarbe);
            //ansicht.setBackgroundColor(randomFarbe);
        }
    }

}