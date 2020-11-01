package com.example.konzentrationstest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
            showExitContinueWindow();
        } else if (view.getId() == R.id.wahr2 && !ergebnisIstRichtig) { // wenn auf Richtig geklickt wird, das Ergebnis aber falsch ist
            showExitContinueWindow();
        } else {    // Ergebnis ist richtig
            int randomNumber = (int) (Math.random() * farben.length);
            String randomText = "";
            int randomFarbe = 0;    // dummy
            // Implementierung, sodass nur die benachbarten Farben ausgewählt werden können um die Häufigkeit zu erhöhen

            if ((randomNumber >= 1) && (randomNumber <= farben.length-2)) { // fuer alle Zahlen bis auf die aeußersten des Arrays
                randomText = farben[randomNumber];
                randomFarbe = farbCodes[(randomNumber-1) + (int) (Math.random() * 3)];
            } else if (randomNumber == 0) {
                int[] random_array = new int[] {farben.length-1, 0, 1};
                randomText = farben[randomNumber];
                randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
            } else if (randomNumber == farben.length-1) {
                int[] random_array = new int[]{farben.length - 2, farben.length - 1, 0};
                randomText = farben[randomNumber];
                randomFarbe = farbCodes[random_array[(int) (Math.random() * random_array.length)]];
            }

            farbText.setText(randomText);
            farbText.setTextColor(randomFarbe);
            //farbText.setBackgroundColor(randomFarbe);
        }
    }

    // Überlegen, eine eigene abstrakte Klasse bzw. ein Interface dafür zu erstellen, da fast in jeder Klasse diese Methode auftaucht
    // zeigt Pop-Up-Fenster, falls Spoel verloren.
    public void showExitContinueWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Falsche Antwort");
        builder.setMessage("Ins Startmenü zurück oder eine neue Runde starten?");
        // add the buttons
        builder.setPositiveButton("Continue", (DialogInterface dialog, int which) -> this.punkte = 0);
        builder.setNegativeButton("Exit", (DialogInterface dialog, int which) -> {
            Intent myIntent = new Intent(Aufgabe_Farben.this, MainActivity.class);
            Aufgabe_Farben.this.startActivity(myIntent);
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}