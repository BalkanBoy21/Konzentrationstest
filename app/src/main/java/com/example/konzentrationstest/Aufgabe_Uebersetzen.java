
package com.example.konzentrationstest;

import android.app.Dialog;
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

    private Zeit z;
    private ProgressBar timer;
    int punkte;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    String KEY = "speicherPreferences4";

    private TextView farbText;

    private final String[] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Pink"};

    private final int[] farbCodes = new int[farben.length];

    ImageButton btn1, btn2, btn3;
    ImageButton[] btns;

    private Dialog epicDialog;

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

        // initialisiere das button-array das alle farb-buttons beinhaltet, die geshufflet werden
        btns = new ImageButton[] {btn1, btn2, btn3};

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

        // Setze erste Farbe aus im Array farben angegebenen Farben
        int random1 = (int) (Math.random() * farben.length);
        farbText.setText(farben[random1]);

        // Setze zweite Farbe, die unterschiedlich ist von erster Farbe
        int random2;
        do {
            random2 = (int) (Math.random() * farbCodes.length);
        } while (random1 == random2);
        farbText.setTextColor(farbCodes[random2]);

        int startColor_Btn1 = farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())];
        int startColor_Btn2 = farbText.getCurrentTextColor();

        btn1.setBackgroundColor(startColor_Btn1);
        btn2.setBackgroundColor(startColor_Btn2);

        // Dritte Farbe unterscheidet sich von den ersten beiden Farben und ist die korrekte Antwort auf die Frage
        int newColor;
        do {
            newColor = (int) (Math.random() * farbCodes.length);
        } while ((startColor_Btn1 == farbCodes[newColor]) || (farbCodes[newColor] == startColor_Btn2));

        btn3.setBackgroundColor(farbCodes[newColor]);


        // PopUp-Fenster
        epicDialog = new Dialog(this);

        // Zeitleiste erstellen
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        // Punktestand wird jedes Mal zurueckgesetzt, wenn die Seite neu betreten wird (besonders wenn auf "Beenden" geklickt wird)
        punkte = 0;
    }

    public void check(View view) {
        String lastText = farbText.getText().toString();
        int lastColor = farbText.getCurrentTextColor();

        // Prueft ob die angegebene Loesung korrekt ist
        boolean ergebnisIstRichtig = true;
        boolean neuerHighScore = false;


        ImageButton clickedButton;
        switch (view.getId()) {
            case R.id.farbe1: clickedButton = btn1; break;
            case R.id.farbe2: clickedButton = btn2; break;
            case R.id.farbe3: clickedButton = btn3; break;
            default: clickedButton = btn1;
        }

        int clickedButtonColor = ((ColorDrawable) clickedButton.getBackground()).getColor();

        if ((lastColor == clickedButtonColor) || (farbCodes[Arrays.asList(farben).indexOf(lastText)] == clickedButtonColor)) {     // wenn die Antwort also falsch ist
            ergebnisIstRichtig = false;
        }

        if (!ergebnisIstRichtig) {
            // Setzen des neuen Highscores
            TopScore.highscore_uebersetzen = punkte;

            if (preferences.getInt(KEY, 0) < TopScore.highscore_uebersetzen) {
                preferencesEditor.putInt(KEY, TopScore.highscore_uebersetzen);
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_uebersetzen);
            preferencesEditor.commit();

            PopUpFenster pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore, epicDialog, preferences, preferencesEditor, KEY);
            pop.showPopUpWindow();

            punkte = 0; // Nach jedem Schließen eines Pop-Up-Fensters die Punktzahl zurücksetzen

        } else {
            ++punkte;
            int randomNumber, randomFarbe;

                // Implementierung, sodass Text und Farbe jedes Mal unterschiedlich zur vorherigen Activity sind + Farbe niemals dem Text entspricht
                do {
                    randomNumber = (int) (Math.random() * farben.length);
                    randomFarbe = (int) (Math.random() * farbCodes.length);
                } while (farben[randomNumber].equals(lastText) || (farbCodes[randomFarbe] == lastColor) || (randomNumber == randomFarbe));

                farbText.setText(farben[randomNumber]);
                farbText.setTextColor(farbCodes[randomFarbe]);

            // nach jedem Klick die Buttons shufflen, damit richtige Antwort nicht immer an der selben (an der dritten) Stelle ist
            List<ImageButton> intList = Arrays.asList(btns);
            Collections.shuffle(intList);
            intList.toArray(btns);

            // Setzen der Farben
            btns[0].setBackgroundColor(farbText.getCurrentTextColor());
            btns[1].setBackgroundColor(farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())]);

            // die letzte Farbe unterscheidet sich von den anderen beiden und ist somit richtig
            int newColor;
            do {
                newColor = (int) (Math.random() * farbCodes.length);
            } while ((farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())] == farbCodes[newColor]) || (farbCodes[newColor] == farbText.getCurrentTextColor()));

            btns[2].setBackgroundColor(farbCodes[newColor]);

        }
    }
}
