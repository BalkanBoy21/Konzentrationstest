package com.example.konzentrationstest;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PopUpFenster extends AppCompatActivity {

    PopUpFenster p;
    int punkte;
    Object obj;
    int highscore;

    public PopUpFenster(Object obj, int punkte, int highscore) {
        this.obj = obj;
        this.punkte = punkte;
        this.highscore = highscore;
    }

    public void showExitContinueWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity) obj);
            builder.setTitle("Erreichte Punktzahl: " + punkte + " (Highscore: " + highscore + ")");

        // unbedingt noch sowas wie durchschnittliche Zeit pro Aufgabe im Format (Sekunden, Hundertstel-Millisekunden), z.B. 1,74 Sekunden
            builder.setMessage("\t\t\t\t\t\t\t\tDeine Antwort ist falsch.\n\t\t\t\t\t\t\t\tBeenden oder fortsetzen?");
        // add the buttons
            builder.setNegativeButton("Beenden", (DialogInterface dialog, int which) -> {
            Intent myIntent = new Intent((AppCompatActivity) obj, MainActivity.class);
                ((AppCompatActivity) obj).startActivity(myIntent);
        });
            builder.setPositiveButton("Fortsetzen", (DialogInterface dialog, int which) -> {});

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);    // sorgt dafür, dass entweder Beenden oder Fortsetzen gedrückt werden muss
        dialog.show();
    }
}
