package com.example.konzentrationstest;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopUpFenster extends AppCompatActivity {

    PopUpFenster p;
    int punkte;
    Object obj;

    int highscore;
    boolean neuerHighScore;

    Button leave, stay;
    Dialog epicDialog;

    TextView text, text2;
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    private final String KEY;      // fuer jede Klasse anderen Key fuer jeweils einen anderen Highscore

    public PopUpFenster(Object obj, int punkte, int highscore, boolean neuerHighScore, Dialog epicDialog, SharedPreferences preferences, SharedPreferences.Editor preferencesEditor, String key) {
        this.obj = obj;
        this.punkte = punkte;
        this.highscore = highscore;
        this.neuerHighScore = neuerHighScore;

        this.epicDialog = epicDialog;
        this.preferences = preferences;
        this.preferencesEditor = preferencesEditor;

        KEY = key;
    }

    public boolean getNeuerHighScore() {
        return this.neuerHighScore;
    }

    public void setNeuerHighScore(boolean neuerHighScore) {
        this.neuerHighScore = neuerHighScore;
    }

    public SharedPreferences getPreferences() {
        return this.preferences;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        return this.preferencesEditor;
    }

    public String getKEY() {
        return this.KEY;
    }

    public void setPunkte() {
        punkte = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("----", "Event: " + Zeit.active);
        if ((keyCode == KeyEvent.KEYCODE_BACK)  && (!Zeit.active)) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showPopUpWindow() {
        epicDialog.setContentView(R.layout.activity_popupfenster);

        leave = epicDialog.findViewById(R.id.verlassen);
        stay = epicDialog.findViewById(R.id.weiter);

        text = epicDialog.findViewById(R.id.anzeigeScore);
        text2 = epicDialog.findViewById(R.id.anzeigeHighscore);

        String punkteText = "\n\tPunkte: " + this.punkte;
        text.setText(punkteText);

        // Text fuer Highscore
        String displayedText = "\t\t\t\t\tHighscore: " + preferences.getInt(KEY, 0);
        if (neuerHighScore) {
            displayedText = "Neuer Highscore: " + preferences.getInt(KEY, 0);
        }
        text2.setText(displayedText);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String difficulty = MainActivity.getCurrentDifficultyText()[0];
                if (difficulty.equals("Easy")) {
                    MainActivity.lastdisabledButton = "Easy";
                } else if (difficulty.equals("Moderate")) {
                    MainActivity.lastdisabledButton = "Moderate";
                } else if (difficulty.equals("Hard")) {
                    MainActivity.lastdisabledButton = "Hard";
                }

                Intent myIntent = new Intent( (AppCompatActivity) obj, MainActivity.class);
                ((AppCompatActivity) obj).startActivity(myIntent);
            }
        });

        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                switch (KEY) {
                    case "speicherPreferences_Rechnen":
                        // macht Buttons wieder frei sobald das Pop-Up-Fenster verlassen wurde
                        Aufgabe_Rechnen.down.setEnabled(true);
                        Aufgabe_Rechnen.up.setEnabled(true);
                        break;
                    case "speicherPreferences_Farben":
                        Aufgabe_Farben.down.setEnabled(true);
                        Aufgabe_Farben.up.setEnabled(true);
                        break;
                    case "speicherPreferences_Formen":
                        Aufgabe_Formen.down.setEnabled(true);
                        Aufgabe_Formen.up.setEnabled(true);
                        break;
                    case "speicherPreferences_waehleUnpassendeFarbe":
                        // enablen der Buttons
                        for (ImageButton ib: Aufgabe_waehleUnpassendeFarbe.getButtons()) {
                            ib.setEnabled(true);
                        }
                        break;
                }
                Zeit.active = true;
                epicDialog.dismiss();
                PopUpFenster.this.punkte = 0;
            }
        });

        // sorgt dafuer dass Aktivität stoppt sobald man beim Laufen der Aktivität ins Hauptmenu zurueckmoechte
        //if (isFinishing()) {
            Log.e("---", "Juckt");
            epicDialog.setCancelable(false);
            epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            epicDialog.show();
            //punkte = 0;
        //}

        //punkte = 0;
    }

}