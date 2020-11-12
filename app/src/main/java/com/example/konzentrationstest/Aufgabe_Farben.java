package com.example.konzentrationstest;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Aufgabe_Farben extends AppCompatActivity {

    private TextView farbText;

    private final String [] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Weiß", "Pink"};
    private final int [] farbCodes = new int[farben.length];

    static int punkte;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;

    Dialog epicDialog;
    private final String KEY = "speicherPreferences_Farben";

    private ProgressBar timer;
    private Zeit z;
    String diff;

    int milliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__farben);

        timer = findViewById(R.id.timer_Farben);

        farbText = findViewById(R.id.textFarbe);

        epicDialog = new Dialog(this);

        // Setzen der max. Sekundenzahl durch ausgewaehlten Schwierigkeitsgrad
        diff = MainActivity.getCurrentDifficulty();
        milliSec = diff.equals("Easy") ? 3000 : (diff.equals("Moderate") ? 2000 : (diff.equals("Hard") ? 1000 : 5000));       // ziemlich schlechter Code, reicht aber für den Anfang. Lieber den Button erhalten und dann checken ob der entsprechende Button gedrückt wurde
        timer.setMax(milliSec / 10);

        // Die erste Timeline sollte aufgefuellt sein
        timer.setProgress(timer.getMax());
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply();

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

    }

    public void check(View view) {

        String currentText = farbText.getText().toString();
        int currentColor = farbText.getCurrentTextColor();
        //int currentColor = ((ColorDrawable) ansicht.getBackground()).getColor();

        z.running = false;  // alter Zaehler wird gestoppt
        z = new Zeit(timer, punkte);     // neues Objekt fuer naechste Seite
        z.laufen();     // neuer Zaehler geht los

        // Jedes Mal den HighScore neu auf falsch setzen, sonst wird jedes Mal angegeben, dass ein neuer HighScore erreicht wurde
        boolean neuerHighScore = false;
        boolean antwortIstKorrekt = false;

        if (farbCodes[Arrays.asList(farben).indexOf(currentText)] == farbText.getCurrentTextColor()) {      // "... == currentColor" fuer Background-Color
            antwortIstKorrekt = true;
        }

        if (((view.getId() == R.id.unwahr2) && antwortIstKorrekt) || ((view.getId() == R.id.wahr2) && !antwortIstKorrekt)){   // wenn auf Falsch geklickt wird, das Ergebnis aber richtig ist
            // Setzen des neuen Highscores
            TopScore.highscore_farben = punkte;

            if (preferences.getInt(KEY, 0) < TopScore.highscore_farben) {
                preferencesEditor.putInt(KEY, TopScore.highscore_farben);
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_farben);
            preferencesEditor.commit();

            // Stoppt die Zeit, damit diese nicht weiter geht wenn man bereits im Pop-Up-Fenster ist
            z.running = false;

            PopUpFenster pop = new PopUpFenster(Aufgabe_Farben.this, punkte, preferences.getInt(KEY, 0), neuerHighScore, epicDialog, preferences, preferencesEditor, KEY);
            pop.showPopUpWindow();

            punkte = 0;
        } else {    // Ergebnis ist richtig
            ++punkte;

            int randomNumber;
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

            farbText.setText(farben[randomNumber]);
            farbText.setTextColor(randomFarbe);
            //ansicht.setBackgroundColor(randomFarbe);

        }
    }

}