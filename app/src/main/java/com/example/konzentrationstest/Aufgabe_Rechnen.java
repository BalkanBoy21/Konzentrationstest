package com.example.konzentrationstest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

    int [] quadratzahlen = {1, 4, 9, 16, 25, 36, 49, 64, 81, 100};
    int [] summand1 = new int[100 + quadratzahlen.length];     // vielleicht besser new int[100 + quadratzahlen.length] und dann irgendwie verteilen, sind viel zu viele Variablen
    int [] summand2 = new int[summand1.length];
    int [] summen = new int[summand1.length];
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

        int [] temp_shuffle = new int[quadratzahlen.length];      // dient als Index nur fuer die Wurzel-Zahlen

        // nur fuer alle Wurzeln
        for (int j = 0; j < quadratzahlen.length; j++) {
            temp_shuffle[j] = (int) (Math.random() * 110);  // random-Index fuer die Wurzel-Zahlen
            summand1[temp_shuffle[j]] = quadratzahlen[j];
            summand2[temp_shuffle[j]] = 0;        // Merkmal einer Wurzelzahl einfach, dass 2.Summand eine 0 ist (um sie von einfachen Zahlen zu unterscheiden), siehe check Methode
            summen[temp_shuffle[j]] = generiereErgebnis_Wurzel(quadratzahlen[j]);    // bzw. selbst oben einfach eintragen
        }

        // zufällige Werte zuweisen nur für alle Summen
        for (int i = 0; i < summand1.length; i++) {
            if (summand1[i] == 0) {         // besser das hier als summand2[i], da in Java bei einem leeren Int-Array alle Werte per default gleich 0 sind.
                summand1[i] = (int) (Math.random() * 20) + 1;
                summand2[i] = (int) (Math.random() * 20) + 1;
                summen[i] = generiereErgebnis(summand1[i], summand2[i]);
            }
        }

        textFeld = findViewById(R.id.aufgabenFeld);
        timer = findViewById(R.id.countDownBar);

        // vielleicht überflüssig
        int a = 13 + (int) (Math.random() * 12);            // [13, 24]
        int b = 5 + (int) (Math.random() * 28);             // [5, 32]
        int c = (a + b - 3) + (int) (Math.random() * 3);    // [a+b-3; a+b+2]  -> spaeter nach belieben Aendern
        summand1[0] = a;
        summand2[0] = b;
        summen[0] = c;
        String s = a + " " + operator + " " + b + " = " + c;
        textFeld.setText(s); // default für den ersten Wert

        schwierigkeitslevel = String.valueOf(Page2.diff.getSelectedItem()).split(" ")[0];      // gibt entweder Easy, Moderate oder Hard aus

        //timer.setProgress(timer.getMax());      // nur fuer die erste Seite, also den ersten Wert

        int m = 10000;  // dummy, fuer den Anfang
        z = new Zeit(timer, m);
    }

    // Index der Stufe * 2 + 1 (= 2n+1), um die Anzahl an Sekunden für den jeweiligen Schwierigkeitsgrad zu ermitteln. Reicht als Anfangsalgorithmus
    public int level_in_sekunden(String level_index) {
        return 2 * (Arrays.asList(stufen).indexOf(level_index)+1) + 2;  // +1 sehr wichtig, da für Hard ansonsten Wert von 0 angenommen wird
    }

    public void showExitContinueWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Falsche Antwort");
        builder.setMessage("Ins Startmenü zurück oder eine neue Runde starten?");
        // add the buttons
        builder.setPositiveButton("Continue", (DialogInterface dialog, int which) -> punkte = 0);
        builder.setNegativeButton("Exit", (DialogInterface dialog, int which) -> {
                Intent myIntent = new Intent(Aufgabe_Rechnen.this, MainActivity.class);
                Aufgabe_Rechnen.this.startActivity(myIntent);
            });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ProgressBar getTimer() {
        return this.timer;
    }

    // vielleicht nicht nur Plus-, sondern auch Mal Aufgaben oder Wurzel-Aufgaben
    public static int generiereErgebnis(int wert1, int wert2) {
        int genauigkeitsWert = 3;
        return (wert1 + wert2) + (int) (Math.random() * genauigkeitsWert);     // Intervall [wert1+wert2; wert1+wert2+(3-1)], z.B. 8 + 13 -> [21, 23]. Je höher der genauigkeitswert, umso weiter entfernt ist das Ergebnis (spaeter selbst festlegem)
    }

    public static int generiereErgebnis_Wurzel(int wert) {
        return zufallsGenerator((int) (Math.sqrt(wert)), (int) (Math.sqrt(wert)) + (int) (Math.random() * 2));     // Intervall [wurzel(wert); wurzel(wert)+1]     // spaeter verfeinern, sodass die Wahrscheinlichkeit hoeher ist, dass das Ergebnis korrekt ist, zB mit neuer Methode und Modulo
    }

    // Wahrscheinlichkeit, dass wert1 ausgewaehlt wird, soll 70% sein, und fuer wert2 30%
    public static int zufallsGenerator(int wert1, int wert2) {
        int random = (int) (Math.random() * 100);
        if (random < 70) {
            return wert1;
        }
        return wert2;
    }

    // besser nur eine Methode statt 2, und dann zwischen 2 Fällen unterscheiden
    public void check(View view) {
        z.setMillisec(10000);       // neu auf 10000 setzen, wie oben bei Variable m

        ImageButton th_down = findViewById(R.id.unwahr);
        ImageButton th_up = findViewById(R.id.wahr);
        textFeld = findViewById(R.id.aufgabenFeld);

        boolean korrektesErgebnis;
        if (summand2[nth_activity] == 0) {  // Quadrat
            korrektesErgebnis = Math.sqrt(summand1[nth_activity]) == summen[nth_activity];
        } else {        // Summe
            korrektesErgebnis = summand1[nth_activity] + summand2[nth_activity] == summen[nth_activity];        // allein deshalb lieber andere Möglichkeit finden um Quadratzahlen einzubringen. Am besten in einem einzigen Array
        }
        int sek = level_in_sekunden(schwierigkeitslevel);
        int mil = sek * 1000;

        z.setMillisec(mil);
        timer.setProgress(timer.getMax());
        z.run();

        String s;

        if (view.getId() == R.id.unwahr) {
            if (!korrektesErgebnis) {
                Log.d("---", "Deine Antwort ist korrekt");
                ++punkte;

                // fuer die naechste Activity
                ++nth_activity;     // spaeter umbenennen
                if (summand2[nth_activity] != 0) {  // gib Summe aus
                    s = summand1[nth_activity] + " " + operator + " " + summand2[nth_activity] + " = " + summen[nth_activity];
                    textFeld.setText(s);
                } else if (summand2[nth_activity] == 0) {   // gib Text aus
                    //textFeld.setText(getResources().getString(R.string.sqr_root));
                    textFeld.setText(Html.fromHtml("&#x221a;" + summand1[nth_activity] + " = " + summen[nth_activity]));
                }

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

                if (summand2[nth_activity] != 0) {  // gib Summe aus
                    s = summand1[nth_activity] + " " + operator + " " + summand2[nth_activity] + " = " + summen[nth_activity];
                    textFeld.setText(s);
                } else if (summand2[nth_activity] == 0) {   // gib Text aus
                    //textFeld.setText(getResources().getString(R.string.sqr_root));
                    textFeld.setText(Html.fromHtml("&#x221a;" + summand1[nth_activity] + " = " + summen[nth_activity]));
                }
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