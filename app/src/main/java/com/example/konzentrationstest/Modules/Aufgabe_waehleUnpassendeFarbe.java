
package com.example.konzentrationstest.Modules;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Collections;
import java.util.List;

/**
 * This class handles the module about inapproriate colors.
 */
public class Aufgabe_waehleUnpassendeFarbe extends AppCompatActivity {

    private Zeit z;
    private ProgressBar timer;
    int punkte;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    String KEY = "speicherPreferences_waehleUnpassendeFarbe";

    private TextView farbText;

    private final String[] farben = {"Grün", "Gelb", "Blau", "Rot", "Orange", "Rosa", "Schwarz"};

    private final int[] farbCodes = new int[farben.length];

    static ImageButton btn1, btn2, btn3;
    public static ImageButton[] btns;

    boolean newHighscore = false;
    PopUpFenster pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_waehleunpassendefarbe);

        timer = findViewById(R.id.timer_waehleUnpassendeFarbe);
        timer.setProgressTintList(ColorStateList.valueOf(Color.rgb(0,0, 139)));
        farbText = findViewById(R.id.colorText);

        btn1 = findViewById(R.id.farbe1);
        btn2 = findViewById(R.id.farbe2);
        btn3 = findViewById(R.id.farbe3);

        // initialize color button for saving their current states
        btns = new ImageButton[] {btn1, btn2, btn3};

        // filtering all colors in colors.xml which appear in the array 'farben'
        try {
            Field[] fields = Class.forName("com.example.konzentrationstest" + ".R$color").getDeclaredFields();
            //Field[] fields = Class.forName();
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

        // sets color for the first field
        int random1 = (int) (Math.random() * farben.length);
        farbText.setText(farben[random1]);

        // sets 2nd color which is different from the first one
        int random2;
        do {
            random2 = (int) (Math.random() * farbCodes.length);
        } while (random1 == random2);
        farbText.setTextColor(farbCodes[random2]);

        int startColor_Btn1 = farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())];
        int startColor_Btn2 = farbText.getCurrentTextColor();

        btn1.setBackgroundColor(startColor_Btn1);
        btn2.setBackgroundColor(startColor_Btn2);

        // 3rd color which is different from the 1st and 2nd
        int newColor;
        do {
            newColor = (int) (Math.random() * farbCodes.length);
        } while ((startColor_Btn1 == farbCodes[newColor]) || (farbCodes[newColor] == startColor_Btn2));

        btn3.setBackgroundColor(farbCodes[newColor]);

        // pop up dialog
        Dialog epicDialog = new Dialog(this);

        String[] diff = MainActivity.getCurrentDifficultyText();
        // gets current milliseconds depending on selected difficulty
        int milliSec = Integer.parseInt(String.valueOf(Double.parseDouble(diff[1]) * 1000).split("\\.")[0]);

        // fills maximum of time counter
        timer.setMax((milliSec*9) / ((milliSec / 100) / 5));

        // fills time counter for the first page
        timer.setProgress(timer.getMax());
        z = new Zeit(timer);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply();

        pop = new PopUpFenster(this, punkte, newHighscore, epicDialog, preferences, preferencesEditor, KEY);

        // reset score when page is entered
        punkte = 0;
    }

    public static ImageButton[] getButtons() {
        return btns;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)  && (!Zeit.active)) {
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

        String lastText = farbText.getText().toString();
        int lastColor = farbText.getCurrentTextColor();

        // checks if clicked button is correct
        boolean ergebnisIstRichtig = true;

        // stops latest counter
        z.running = false;

        // resets new highscore because new score is old highscore now
        pop.setNewHighscore(false);

        ImageButton clickedButton;
        int id = view.getId();

        if (id == R.id.farbe1) {
            clickedButton = btn1;
        } else if (id == R.id.farbe2) {
            clickedButton = btn2;
        } else { //if (id == R.id.farbe3) {
            clickedButton = btn3;
        }

        int clickedButtonColor = ((ColorDrawable) clickedButton.getBackground()).getColor();

        // clicked button is false
        if ((lastColor == clickedButtonColor) || (farbCodes[Arrays.asList(farben).indexOf(lastText)] == clickedButtonColor)) {
            ergebnisIstRichtig = false;
        }

        if (!ergebnisIstRichtig) {
            // new highscore
            TopScore.highscore_waehleUnpassendeFarbe = pop.getPunkte();

            if (preferences.getInt(KEY, 0) < TopScore.highscore_waehleUnpassendeFarbe) {
                preferencesEditor.putInt(KEY, TopScore.highscore_waehleUnpassendeFarbe);
                pop.setNewHighscore(true);
            }
            preferencesEditor.putInt("key", TopScore.highscore_waehleUnpassendeFarbe);
            preferencesEditor.commit();

            pop.showPopUpWindow();

        } else {
            // increases score
            pop.increaseScore();

            // creates new object for new page
            z = new Zeit(timer);

            // start new counter
            z.laufen(pop);

            int randomNumber, randomFarbe;
            // chooses colors so that text and color are totally different from before
            do {
                randomNumber = (int) (Math.random() * farben.length);
                randomFarbe = (int) (Math.random() * farbCodes.length);
            } while (farben[randomNumber].equals(lastText) || (farbCodes[randomFarbe] == lastColor) || (randomNumber == randomFarbe));

            // set text and text color
            farbText.setText(farben[randomNumber]);
            farbText.setTextColor(farbCodes[randomFarbe]);

            // shuffle buttons after each click so that 3rd answer isn't always the correct answer
            List<ImageButton> intList = Arrays.asList(btns);
            Collections.shuffle(intList);
            intList.toArray(btns);

            // set color
            // tect color
            btns[0].setBackgroundColor(farbText.getCurrentTextColor());
            // text
            btns[1].setBackgroundColor(farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())]);

            // correct color which is always different from the first and second answer
            int newColor;
            do {
                newColor = (int) (Math.random() * farbCodes.length);
            } while ((farbCodes[Arrays.asList(farben).indexOf(farbText.getText().toString())] == farbCodes[newColor]) || (farbCodes[newColor] == farbText.getCurrentTextColor()));

            btns[2].setBackgroundColor(farbCodes[newColor]);
        }

    }
}
