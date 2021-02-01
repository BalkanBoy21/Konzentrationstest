package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.konzentrationstest.Modules.Aufgabe_Farben;
import com.example.konzentrationstest.Modules.Aufgabe_Formen;
import com.example.konzentrationstest.Modules.Aufgabe_Rechnen;
import com.example.konzentrationstest.Modules.Aufgabe_waehleUnpassendeFarbe;

/**
 * handles all events for time counter
 */
public class Zeit extends AppCompatActivity {

    // time counter
    private final ProgressBar counter;

    // pop up window
    private PopUpFenster pop;

    // milliseconds until game stops
    int milliSec;

    // array that contains all difficulties
    String[] diff;

    public boolean running = true;
    public static boolean active = true;

    public Zeit (ProgressBar counter) {
        this.counter = counter;
    }

    /**
     * This method starts the time counter.
     * @param popUp pop up window appearing when game is over.
     */
    public void laufen(PopUpFenster popUp) {
        pop = popUp;
        diff = MainActivity.getCurrentDifficultyText();
        milliSec = Integer.parseInt(String.valueOf(Double.parseDouble(diff[1]) * 1000).split("\\.")[0]);

        this.running = true;

        // refill time counter
        Zeit.active = false;

        // time counter
        CountDownTimer countDownTimer = new CountDownTimer(milliSec, 10) {
            public void onTick(long millisUntilFinished) {
                Zeit.this.counter.setProgress(((int) millisUntilFinished*9) / ((milliSec / 100) / 5));

                // refills the time counter when clicked button is right answer
                if (!Zeit.this.running) {
                    this.cancel();
                    Zeit.this.counter.setProgress(Zeit.this.counter.getMax());
                }
            }

            // is called when counter is empty, i.e. when given time is over
            public void onFinish() {
                // prevents player from going back to menu while playing the game
                if (!isFinishing()) {
                    Zeit.this.running = false;
                }
                Zeit.this.counter.setProgress(Zeit.this.counter.getMax());

                // activate back buttons
                Zeit.active = true;

                // sets score depending on current module and disables both, true and false, buttons
                int hs = 0;
                switch (pop.getKEY()) {
                    case "speicherPreferences_Rechnen":
                        TopScore.highscore_rechnen = pop.getPunkte();
                        hs = TopScore.highscore_rechnen;
                        // verhindert Button-Klick nachdem die Zeitleiste vorbei ist (durch schnelles Klicken)
                        Aufgabe_Rechnen.down.setEnabled(false);
                        Aufgabe_Rechnen.up.setEnabled(false);
                        break;
                    case "speicherPreferences_Farben":
                        TopScore.highscore_farben = pop.getPunkte();
                        hs = TopScore.highscore_farben;
                        Aufgabe_Farben.down.setEnabled(false);
                        Aufgabe_Farben.up.setEnabled(false);
                        break;
                    case "speicherPreferences_Formen":
                        TopScore.highscore_formen = pop.getPunkte();
                        hs = TopScore.highscore_formen;
                        Aufgabe_Formen.down.setEnabled(false);
                        Aufgabe_Formen.up.setEnabled(false);
                        break;
                    case "speicherPreferences_waehleUnpassendeFarbe":
                        TopScore.highscore_waehleUnpassendeFarbe = pop.getPunkte();
                        hs = TopScore.highscore_waehleUnpassendeFarbe;
                        // verhindert Button-Klick nachdem die Zeitleiste vorbei ist (durch schnelles Klicken)
                        for (ImageButton iv: Aufgabe_waehleUnpassendeFarbe.btns) {
                            iv.setEnabled(false);
                        }
                        break;
                }

                // sets reached score
                if (pop.getPreferences().getInt(pop.getKEY(), 0) < hs) {
                    pop.getPreferencesEditor().putInt(pop.getKEY(), hs);
                    pop.setNewHighscore(true);
                }

                pop.getPreferencesEditor().putInt("key", hs);
                pop.getPreferencesEditor().commit();
                pop.showPopUpWindow();
                Zeit.active = true;
            }
        };
        countDownTimer.start();
    }

}
