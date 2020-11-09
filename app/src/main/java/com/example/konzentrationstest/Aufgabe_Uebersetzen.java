package com.example.konzentrationstest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Aufgabe_Uebersetzen extends AppCompatActivity {


    //    String [][] woerter_englisch = new String[][]{{"house", "car", "keyboard"}, {}, {}};

    //    String [][] woerter_deutsch = {{"Haus", "Auto", "Tastatur"}, {}, {}};

    String[] woerter_englisch = new String[]{"house", "car", "keyboard", ""};

    String[] woerter_deutsch = new String[]{"Haus", "Auto", "Tastatur"};

    Zeit z;
    ProgressBar timer;
    int punkte;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    final String KEY = "speicherPreferences4";
    PopUpFenster pop;

    private TextView farbText;

    private String[] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Weiß", "Rosa"};
    private int[] farbCodes = new int[farben.length];

    ImageButton btn1, btn2, btn3;

    ImageButton[] btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__uebersetzen);

        timer = findViewById(R.id.timer_Uebersetzen);
        farbText = findViewById(R.id.colorText);
        btn1 = findViewById(R.id.farbe1);
        btn2 = findViewById(R.id.farbe2);
        btn3 = findViewById(R.id.farbe3);

        // durchsucht alle Farben in colors.xml (und weitere) und filtert alle Farben heraus, die im Array "farben" enthalten sind
        try {
            Field[] fields = Class.forName(getPackageName() + ".R$color").getDeclaredFields();
            String colorName;
            for (Field farbe : fields) {
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
        int random1 = (int) (Math.random() * farben.length);
        farbText.setText(farben[random1]);

        int random2;
        do {        // damit der Text und die Farbe voneinander immer unterschiedlich sind
            random2 = (int) (Math.random() * farbCodes.length);
        } while (random1 == random2);
        farbText.setTextColor(farbCodes[random2]);

        // Punktestand wird jedes Mal zurueckgesetzt, wenn die Seite neu betreten wird (besonders wenn auf "Beenden" geklickt wird)
        punkte = 0;

        // initialisiere das button-array das alle farb-buttons beinhaltet, die geshufflet werden
        btns = new ImageButton[] {btn1, btn2, btn3};

        int startColor_Btn1 = farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())];
        int startColor_Btn2 = farbText.getCurrentTextColor();

        btn1.setBackgroundColor(startColor_Btn1);
        btn2.setBackgroundColor(startColor_Btn2);
        int newColor;
        do {
            newColor = (int) (Math.random() * farbCodes.length);
        } while ((startColor_Btn1 == farbCodes[newColor]) || (farbCodes[newColor] == startColor_Btn2));
        btn3.setBackgroundColor(farbCodes[newColor]);

        int m = 10000;
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public void check(View view) {
        String currentText = farbText.getText().toString();
        int currentColor = farbText.getCurrentTextColor();

        // Prueft ob die angegebene Loesung korrekt ist
        boolean ergebnisIstRichtig = true;

        ImageButton clickedButton;
        switch (view.getId()) {
            case R.id.farbe1: clickedButton = btn1; break;
            case R.id.farbe2: clickedButton = btn2; break;
            case R.id.farbe3: clickedButton = btn3; break;
            default: clickedButton = btn1;
        }

        int clickedButtonColor = ((ColorDrawable) clickedButton.getBackground()).getColor();

            if ((currentColor == clickedButtonColor) || (farbCodes[Arrays.asList(farben).indexOf(currentText)] == clickedButtonColor)) {     // wenn die Antwort also falsch ist
                ergebnisIstRichtig = false;
            }

        if (!ergebnisIstRichtig) {
            // Managen des HighScores
            boolean neuerHighScore = false;
            TopScore.highscore_farben = punkte;
            if (preferences.getInt(KEY, 0) < TopScore.highscore_farben) {
                preferencesEditor.putInt(KEY, TopScore.highscore_farben);
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_farben);
            preferencesEditor.commit();

            pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore);
            pop.showExitContinueWindow();
            punkte = 0; // Nach jedem Schließen eines Pop-Up-Fensters die Punktzahl zurücksetzen
        } else {
            ++punkte;
            int randomNumber;
            int randomFarbe;

                // Implementierung, sodass Text und Farbe jedes Mal unterschiedlich zur vorherigen Activity sind + Farbe niemals dem Text entspricht
                do {
                    randomNumber = (int) (Math.random() * farben.length);
                    randomFarbe = (int) (Math.random() * farbCodes.length);
                } while (farben[randomNumber].equals(currentText) || (farbCodes[randomFarbe] == currentColor) || (randomNumber == randomFarbe));

                farbText.setText(farben[randomNumber]);
                farbText.setTextColor(farbCodes[randomFarbe]);

            // nach jedem Klick die Buttons shufflen, damit richtige Antwort nicht immer an der selben Stelle ist
            List<ImageButton> intList = Arrays.asList(btns);
            Collections.shuffle(intList);
            intList.toArray(btns);

            // Veraendern der Farbbuttons
            btns[0].setBackgroundColor(farbText.getCurrentTextColor());
            btns[1].setBackgroundColor(farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())]);

            // die letzte Farbe sollte auf jeden Fall anders sein als die beiden anderen und somit immer die richtige
            int newColor;
            do {
                newColor = (int) (Math.random() * farbCodes.length);
            } while ((farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())] == farbCodes[newColor]) || (farbCodes[newColor] == farbText.getCurrentTextColor()));
            btns[2].setBackgroundColor(farbCodes[newColor]);

        }
    }
}