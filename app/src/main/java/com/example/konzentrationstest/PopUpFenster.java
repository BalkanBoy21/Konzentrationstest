package com.example.konzentrationstest;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

abstract public class PopUpFenster {
    public static void showExitContinueWindow(Class <?> c) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(null);
        builder.setTitle("Falsche Antwort");
        builder.setMessage("Ins Startmenü zurück oder eine neue Runde starten?");
        // add the buttons
        //builder.setPositiveButton("Continue", (DialogInterface dialog, int which) -> this.punkte = 0);
        builder.setNegativeButton("Exit", (DialogInterface dialog, int which) -> {
        //    Intent myIntent = new Intent(Aufgabe_Rechnen.this, MainActivity.class);
       //     Aufgabe_Rechnen.this.startActivity(myIntent);
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
