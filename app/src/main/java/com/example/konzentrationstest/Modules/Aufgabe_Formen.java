package com.example.konzentrationstest.Modules;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.konzentrationstest.MainActivity;
import com.example.konzentrationstest.PopUpFenster;
import com.example.konzentrationstest.R;
import com.example.konzentrationstest.TopScore;
import com.example.konzentrationstest.Zeit;

import java.util.Arrays;

/**
 * This class handles the shapes module
 */
public class Aufgabe_Formen extends AppCompatActivity {

    // wichtig: formenText und symbolDateien muessen 1:1 in der gleichen Reihenfolge sein
    private final String[] formText = {"Kreis", "Quadrat", "Stern", "Herz", "Dreieck"};
    private final int []symbolfiles = {R.drawable.kreis, R.drawable.quadrat, R.drawable.stern, R.drawable.herz, R.drawable.dreieck};

    private ImageView form;
    private int randomSymbol;
    int symbol;

    private int punkte = 0;
    int temp = 0;

    private ProgressBar timer;
    private Zeit z;
    private TextView textView;

    // pop up variables
    private PopUpFenster pop;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private final String KEY = "speicherPreferences_Formen";

    public static ImageButton down, up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe_formen);

        textView = findViewById(R.id.formText);
        form = findViewById(R.id.formSymbol);
        timer = findViewById(R.id.timer_Formen);
        timer.setProgressTintList(ColorStateList.valueOf(Color.rgb(0,0, 139)));

        down = findViewById(R.id.unwahr3);
        up = findViewById(R.id.wahr3);

        boolean newHighscore = false;

        // presents pop up window
        Dialog epicDialog = new Dialog(this);

        // sets number of seconds for each difficulty
        String[] diff = MainActivity.getCurrentDifficultyText();
        int milliSec;
        Log.d("---", "Diff:" + diff[1]);
        if (diff[1].equals("Leicht")) {
            milliSec = 2000;
        } else if (diff[1].equals("Mittel")) {
            milliSec = 1500;
        } else {
            milliSec = 1000;
        }

        // set maximum for time counter
        timer.setMax((milliSec*9) / ((milliSec / 100) / 5));

        // fill first time counter
        timer.setProgress(timer.getMax());
        z = new Zeit(timer);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply();

        // set values for the first page before the actual game begins
        randomSymbol = (int) (Math.random() * symbolfiles.length);
        temp = randomSymbol;        // sehr wichtig fuer erste If-Anweisung in Methode check (nur fuer den Uebergang vom ersten zum zweiten Bild)
        symbol = symbolfiles[randomSymbol];

        // create new start text and new shape
        textView.setText(formText[(int) (Math.random() * formText.length)]);
        form.setImageResource(symbolfiles[randomSymbol]);

        // reset points to prevent application from saving points by leaving another game
        punkte = 0;

        pop = new PopUpFenster(this, punkte, newHighscore, epicDialog, preferences, preferencesEditor, KEY);

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
    public void check (View view) {
        // time difference to prevent clicking on two buttons at the same time
        int difference = 150;
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < difference) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        String lastText = textView.getText().toString();
        int lastSymbol = symbol;
        //int currentSymbol = symbolDateien[randomSymbol];

        // alter Zaehler wird gestoppt
        // stop current time counter
        z.running = false;

        // new highscore is new score now
        pop.setNewHighscore(false);

        int index = Arrays.asList(formText).indexOf(lastText);
        boolean antwortIstKorrekt = false;

        // Ueberpruefung ob Index des Textes mit dem der Form uebereinstimmt
        if (index == temp) {
            antwortIstKorrekt = true;
        }

        // selected answer isn't correct
        if (((view.getId() == R.id.unwahr3) && antwortIstKorrekt) || ((view.getId() == R.id.wahr3) && !antwortIstKorrekt)) {
            // sets new highscore
            TopScore.highscore_formen = pop.getPunkte();

            if (preferences.getInt(KEY, 0) < TopScore.highscore_formen) {
                preferencesEditor.putInt(KEY, TopScore.highscore_formen);
                pop.setNewHighscore(true);
            }
            preferencesEditor.putInt("key", TopScore.highscore_formen);
            preferencesEditor.commit();

            pop.showPopUpWindow();

        } else {
            // increase score
            pop.increaseScore();

            // timer
            z = new Zeit(timer);

            // running timer
            z.laufen(pop);

            // symbol and text are different from before
            do {
                randomSymbol = (int) (Math.random() * symbolfiles.length);

                if (randomSymbol == 0) {
                    //int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    int [] random_array = new int[]{0, (int) (Math.random() * symbolfiles.length)};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                } else if (randomSymbol == formText.length - 1) {
                    //int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    int [] random_array = new int[]{symbolfiles.length - 1, (int) (Math.random() * symbolfiles.length)};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                } else {        // Aeußere sind ausgeschlossen
                    temp = (randomSymbol - 1) + (int) (Math.random() * 3);
                }
                symbol = symbolfiles[temp];
            } while (formText[randomSymbol].equals(lastText) || (lastSymbol == symbol));

            // set new text and new shape
            textView.setText(formText[randomSymbol]);
            form.setImageResource(symbol);

        }

    }

}
