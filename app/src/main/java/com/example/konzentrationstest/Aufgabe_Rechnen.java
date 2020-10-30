package com.example.konzentrationstest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

// vielleicht besser, die Zahlen rot zu markieren oder mit anderen Farben, sodass besser sichtbar und nicht nur grau.

public class Aufgabe_Rechnen extends AppCompatActivity {

    int [] werte1 = new int[100];
    int [] werte2 = new int[100];
    int [] ergebnisse = new int[100];
    static int punkte = 0;
    static int nth_activity = 0;

    String operator = "+";
    String [] stufen = {"Hard", "Moderate", "Easy"};

    private ProgressBar timer;
    private TextView textFeld;
    private String schwierigkeitslevel;

    private Zeit z;

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

        textFeld = findViewById(R.id.aufgabenFeld);
        timer = findViewById(R.id.countDownBar);

        textFeld.setText(werte1[0] + " " + operator + " " + werte2[0] + " = " + ergebnisse[0]); // default für den ersten Wert

        schwierigkeitslevel = String.valueOf(Page2.diff.getSelectedItem()).split(" ")[0];      // gibt entweder Easy, Moderate oder Hard aus

        //timer.setProgress(timer.getMax());      // nur fuer die erste Seite, also den ersten Wert
        timer.setMax(0);
        timer.setProgress(0);


        int m = 10000;  // dummy, fuer den Anfang
        z = new Zeit(timer, m);
    }

    // Index der Stufe * 2 + 1 (= 2n+1), um die Anzahl an Sekunden für den jeweiligen Schwierigkeitsgrad zu ermitteln. Reicht als Anfangsalgorithmus
    public int level_in_sekunden(String level_index) {
        Log.d("---", "Ergebnis: " + Arrays.asList(stufen).indexOf(level_index));
        return 2 * (Arrays.asList(stufen).indexOf(level_index)+1) + 2;  // +1 sehr wichtig, da für Hard ansonsten Wert von 0 angenommen wird
    }

    public void showExitContinueWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Falsche Antwort");
        builder.setMessage("Ins Startmenü zurück oder eine neue Runde starten?");
        // add the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                punkte = 0;
                // es geht einfach weiter, einfach nichts aendern
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Aufgabe_Rechnen.this, MainActivity.class);
                Aufgabe_Rechnen.this.startActivity(myIntent);
                //do stuff
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ProgressBar getTimer() {
        return this.timer;
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
        textFeld = findViewById(R.id.aufgabenFeld);

        boolean korrektesErgebnis = werte1[nth_activity] + werte2[nth_activity] == ergebnisse[nth_activity];

        int sek = level_in_sekunden(schwierigkeitslevel);
        int mil = sek * 1000;

        z.setMillisec(mil);
        timer.setProgress(timer.getMax());
        z.run();

        if (view.getId() == R.id.unwahr) {
            if (!korrektesErgebnis) {
                Log.d("---", "Deine Antwort ist korrekt");
                ++punkte;
                ++nth_activity;
                textFeld.setText(werte1[nth_activity] + " " + operator + " " + werte2[nth_activity] + " = " + ergebnisse[nth_activity]);

                // Code doppelt und drei-fach, Möglichkeit überlegen nicht nochmal schreiben zu müssen

            } else {
                Log.d("---", "Deine Antwort ist nicht korrekt");
                // Pop Up Fenster mit Nachricht, um ins Menue zu gelangen
                showExitContinueWindow();
            }
        } else if (view.getId() == R.id.wahr) {
            if (korrektesErgebnis) {
                ++punkte;
                ++nth_activity;
                textFeld.setText(werte1[nth_activity] + " " + operator + " " + werte2[nth_activity] + " = " + ergebnisse[nth_activity]);

                // Code doppelt und drei-fach, Möglichkeit überlegen nicht nochmal schreiben zu müssen

            } else {
                Log.d("---", "Deine Antwort ist nicht korrekt");
                showExitContinueWindow();
            }
        } else {
            Log.d("---", "Irgendwas stimmt hier nicht. Irgendein anderer Button muss gedrückt worden sein.");
        }

    }

}