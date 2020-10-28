package com.example.konzentrationstest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Rechenaufgaben extends AppCompatActivity {

    int [] werte1 = new int[100];
    int [] werte2 = new int[100];
    int [] ergebnisse = new int[100];
    static int punkte = 0;
    static int nth_activity = 0;

    String operator = "+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechenaufgaben);

        // zufällige Werte zuweisen
        for (int i = 0; i < werte1.length; i++) {
            werte1[i] = (int) (Math.random() * 20) + 1;
            werte2[i] = (int) (Math.random() * 20) + 1;
            ergebnisse[i] = generiereErgebnis(werte1[i], werte2[i]);
        }

        TextView textFeld = findViewById(R.id.aufgabenFeld);
        textFeld.setText(werte1[0] + " " + operator + " " + werte2[0] + " = " + ergebnisse[0]); // default für den ersten Wert

    }

    // vielleicht nicht nur Plus-, sondern auch Mal Aufgaben
    public static int generiereErgebnis(int wert1, int wert2) {
        int genauigkeitsWert = 3;
        int ergebnis = (wert1 + wert2) + (int) (Math.random() * genauigkeitsWert);     // Intervall [wert1+wert2; wert1+wert2+(3-1)], z.B. 8 + 13 -> [21, 23]. Je höher der genauigkeitswert, umso weiter entfernt ist das Ergebnis (spaeter selbst festlegem)
        return ergebnis;
    }

    // besser nur eine Methode statt 2, und dann zwischen 2 Fällen unterscheiden
    public void check(View view) {
        ImageButton th_down = findViewById(R.id.unwahr);
        ImageButton th_up = findViewById(R.id.wahr);

        boolean korrektesErgebnis = werte1[nth_activity] + werte2[nth_activity] == ergebnisse[nth_activity];
        TextView textFeld = findViewById(R.id.aufgabenFeld);

        if (view.getId() == R.id.unwahr) {
            Log.d("--", "Thumbs Down");
            if (!korrektesErgebnis) {
                Log.d("---", "Deine Antwort ist korrekt");
                ++punkte;
                ++nth_activity;
                textFeld.setText(werte1[nth_activity] + " " + operator + " " + werte2[nth_activity] + " = " + ergebnisse[nth_activity]);

            } else {
                Log.d("---", "Deine Antwort ist nicht korrekt");
                // Pop Up Fenster mit Nachricht, um ins Menue zu gelangen
            }
        } else if (view.getId() == R.id.wahr) {
            Log.d("--", "Thumbs Up");
            if (korrektesErgebnis) {
                Log.d("---", "Deine Antwort ist korrekt");
                ++punkte;
                ++nth_activity;
                textFeld.setText(werte1[nth_activity] + " " + operator + " " + werte2[nth_activity] + " = " + ergebnisse[nth_activity]);

                // naechste Activity, also neue Zahl mit while Schleife am besten (solange bis falsch getippt)
                // bzw. keine naechste Activity, sondern einfach den Text resetten
            } else {
                Log.d("---", "Deine Antwort ist nicht korrekt");
                // Pop Up Fenster mit Nachricht, um ins Menue zu gelangen
            }
        } else {
            Log.d("---", "Irgendwas stimmt hier nicht. Irgendein anderer Button muss gedrückt worden sein.");
        }

    }


}