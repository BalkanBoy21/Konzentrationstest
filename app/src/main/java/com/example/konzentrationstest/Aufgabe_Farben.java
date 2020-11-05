package com.example.konzentrationstest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Aufgabe_Farben extends AppCompatActivity {

    private TextView farbText;
    private ImageButton th_down, th_up;

    private String [] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Braun", "Rosa"};
    private int [] farbCodes = new int[farben.length];

    private int punkte = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__farben);

        th_down = findViewById(R.id.unwahr2);
        th_up = findViewById(R.id.wahr2);
        farbText = findViewById(R.id.textFarbe);
        farbText.setTextColor(getResources().getColor(R.color.Grün));

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

    }

    int randomFarbe = 0;    // dummy
    public void check(View view) {
        String currentText = farbText.getText().toString();
        int currentColor = farbText.getCurrentTextColor();

        // Noch zu erledigen:
        // dafuer sorgen, dass nicht 2 mal der selbe Farbtext hintereinander ausgewaehlt wird


        boolean ergebnisIstRichtig = false;

        try {
            Field[] fields = Class.forName(getPackageName() + ".R$color").getDeclaredFields();
            for (Field farbe: fields) {
                if ((getResources().getColor(farbe.getInt(null)) == currentColor) && (farbe.getName().equals(currentText))) {  // sowohl Name als auch Farbe müssen übereinstimmen
                    ergebnisIstRichtig = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view.getId() == R.id.unwahr2 && ergebnisIstRichtig) {   // wenn auf Falsch geklickt wird, das Ergebnis aber richtig ist
            PopUpFenster pop = new PopUpFenster(this, punkte);
            pop.showExitContinueWindow();
            punkte = 0;
            return;
        } else if (view.getId() == R.id.wahr2 && !ergebnisIstRichtig) { // wenn auf Richtig geklickt wird, das Ergebnis aber falsch ist
            PopUpFenster pop = new PopUpFenster(this, punkte);
            pop.showExitContinueWindow();
            punkte = 0;
            return;
        } else {    // Ergebnis ist richtig
            ++punkte;
            int randomNumber;
            String randomText;

            // Implementierung, sodass nur die benachbarten Farben ausgewählt werden können um die Häufigkeit zu erhöhen
            do {
                randomNumber = (int) (Math.random() * farben.length);
                randomText = farben[randomNumber];
                if (randomNumber == 0) {
                    int[] random_array = new int[]{farben.length - 1, 0, 1};
                    randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
                } else if (randomNumber == farben.length - 1) {
                    int[] random_array = new int[]{farben.length - 2, farben.length - 1, 0};
                    randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
                } else {    // fuer alle Zahlen bis auf die aeußersten des Arrays
                    randomFarbe = farbCodes[(randomNumber - 1) + (int) (Math.random() * 3)];
                }
            } while (farben[randomNumber].equals(currentText) || (randomFarbe == currentColor));      // nach jedem Klick eine andere Farbe und ein anderer Text

            farbText.setText(randomText);
            farbText.setTextColor(randomFarbe);
            //farbText.setBackgroundColor(randomFarbe);
        }
    }

}