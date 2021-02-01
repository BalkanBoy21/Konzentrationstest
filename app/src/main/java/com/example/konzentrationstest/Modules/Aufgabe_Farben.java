package com.example.konzentrationstest.Modules;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.konzentrationstest.MainActivity;
import com.example.konzentrationstest.PopUpFenster;
import com.example.konzentrationstest.R;
import com.example.konzentrationstest.TopScore;
import com.example.konzentrationstest.Zeit;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * This class handles the color module.
 */
public class Aufgabe_Farben extends AppCompatActivity {

    private TextView farbText;

    private final String [] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Weiß", "Rosa", "Schwarz"};
    //private String[] farben = {"Green", "Yellow", "Blue", "Red", "Orange", "White", "Pink", "Black"};
    private final int [] farbCodes = new int[farben.length];

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private final String KEY = "speicherPreferences_Farben";

    private ProgressBar timer;
    private Zeit z;

    private int punkte;
    private final boolean newHighscore = false;

    public static ImageButton down, up;
    private PopUpFenster pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_farben);

        down = findViewById(R.id.unwahr2);
        up = findViewById(R.id.wahr2);

        timer = findViewById(R.id.timer_Farben);
        timer.setProgressTintList(ColorStateList.valueOf(Color.rgb(0,0, 139)));

        farbText = findViewById(R.id.textFarbe);

        Dialog epicDialog = new Dialog(this);

        // Setzen der max. Sekundenzahl durch ausgewaehlten Schwierigkeitsgrad
        String[] diff = MainActivity.getCurrentDifficultyText();

        int milliSec;
        if (diff[1].equals("Leicht")) {
            milliSec = 2000;
        } else if (diff[1].equals("Mittel")) {
            milliSec = 1500;
        } else {
            milliSec = 1000;
        }

        // Das Maximum fuer die Zeitleiste setzen
        timer.setMax((milliSec*9) / ((milliSec / 100) / 5));

        // Die erste Timeline sollte aufgefuellt sein
        timer.setProgress(timer.getMax());
        z = new Zeit(timer);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply();

        // durchsucht alle Farben in colors.xml (und weitere) und filtert alle Farben heraus, die im Array "farben" enthalten sind
        try {
            Field[] fields = Class.forName("com.example.konzentrationstest" + ".R$color").getDeclaredFields();
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

        // first page
        farbText.setText(farben[(int) (Math.random() * farben.length)]);
        farbText.setTextColor(farbCodes[(int) (Math.random() * farbCodes.length)]);
//        ansicht.setBackgroundColor(farbCodes[(int) (Math.random() * farbCodes.length)]);

        this.punkte = 0;       // sehr wichtig, da man ins Menue zurueckgehen kann

        pop = new PopUpFenster(Aufgabe_Farben.this, punkte, newHighscore, epicDialog, preferences, preferencesEditor, KEY);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when time isn't running
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (!Zeit.active)) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // variable to track event time
    private long mLastClickTime = 0;

    /**
     * This method handles all button events of this page.
     * @param view view of the method and the class' xml file.
     */
    public void check(View view) {
        // time difference to prevent clicking on two buttons at the same time
        int difference = 150;
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < difference) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        // stop timer
        z.running = false;

        // new highscore is normal highscore now
        pop.setNewHighscore(false);

        String currentText = farbText.getText().toString();
        int currentColor = farbText.getCurrentTextColor();
        //int currentColor = ((ColorDrawable) ansicht.getBackground()).getColor();

        // determines if given answer is correct or not
        boolean antwortIstKorrekt = false;

        if (farbCodes[Arrays.asList(farben).indexOf(currentText)] == farbText.getCurrentTextColor()) {      // "... == currentColor" fuer Background-Color
            antwortIstKorrekt = true;
        }

        // task is correct but player clicked on wrong button
        if (((view.getId() == R.id.unwahr2) && antwortIstKorrekt) || ((view.getId() == R.id.wahr2) && !antwortIstKorrekt)){
            // Setzen des neuen Highscores
            TopScore.highscore_farben = pop.getPunkte();

            if (preferences.getInt(KEY, 0) < TopScore.highscore_farben) {
                preferencesEditor.putInt(KEY, TopScore.highscore_farben);
                pop.setNewHighscore(true);
            }
            preferencesEditor.putInt("key", TopScore.highscore_farben);
            preferencesEditor.commit();

            pop.showPopUpWindow();

        } else {
            // increases score
            pop.increaseScore();

            // timer
            z = new Zeit(timer);

            // starts new timer
            z.laufen(pop);

            // implementation to select random colors
            int randomNumber, randomFarbe;
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
            } while (farben[randomNumber].equals(currentText) || (randomFarbe == currentColor));

            farbText.setText(farben[randomNumber]);
            farbText.setTextColor(randomFarbe);
            //ansicht.setBackgroundColor(randomFarbe);

        }
    }

}