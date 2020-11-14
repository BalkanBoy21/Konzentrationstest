package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Zeit extends AppCompatActivity {

    private final ProgressBar counter;

    boolean running = true;
    int punkte = 0;

    String[] diff;
    int milliSec;

    private PopUpFenster pop;
    static boolean active = true;

    public Zeit (ProgressBar counter, int punkte) {
        this.counter = counter;
        this.punkte = punkte;
    }

    // startet Zeitleiste
    public void laufen(PopUpFenster popUp) {
        pop = popUp;        // loest wohl irgendwie das Problem mit dem Zurückgehen und dem Stoppen der Acitivity
        diff = MainActivity.getCurrentDifficultyText();
        milliSec = Integer.parseInt(String.valueOf(Double.parseDouble(diff[1]) * 1000).split("\\.")[0]);

        // Jedes Mal neu resetten, um bei richtiger Antwort die letzte Anzeige der Zeitleiste zu loeschen und die neue Liste wieder voll zu machen
        this.running = true;

        Zeit.active = false;

        CountDownTimer countDownTimer = new CountDownTimer(milliSec, 10) {
            public void onTick(long millisUntilFinished) {
                Zeit.this.counter.setProgress(((int) millisUntilFinished*9) / ((milliSec / 100) / 5));     // mathematisches Umrechnen, im Kopf etwas schwerer zu machen

                if (!Zeit.this.running) {
                    this.cancel();
                    //Zeit.this.running = false;
                    Log.d("","---Bazinga");
                    Zeit.this.counter.setProgress(Zeit.this.counter.getMax());
                    return;
                }
            }

            public void onFinish() {
                // sorgt dafuer dass Aktivität stoppt sobald man beim Laufen der Aktivität ins Hauptmenu zurueckmoechte
                if (!isFinishing()) {
                    Zeit.this.running = false;
                }
                Zeit.this.counter.setProgress(Zeit.this.counter.getMax());

                // nun ist der Backbutton wieder aktiv
                Zeit.active = true;

                int hs = 0;
                switch (pop.getKEY()) {
                    case "speicherPreferences_Rechnen":
                        TopScore.highscore_rechnen = pop.punkte;
                        hs = TopScore.highscore_rechnen; break;
                    case "speicherPreferences_Farben":
                        TopScore.highscore_farben = pop.punkte;
                        hs = TopScore.highscore_farben;
                        break;
                    case "speicherPreferences_Formen":
                        TopScore.highscore_formen = pop.punkte;
                        hs = TopScore.highscore_formen;
                        break;
                    case "speicherPreferences_waehleUnpassendeFarbe":
                        TopScore.highscore_waehleUnpassendeFarbe = pop.punkte;
                        hs = TopScore.highscore_waehleUnpassendeFarbe;
                        break;
                }

                Log.e("----", pop.getPreferences().getInt(pop.getKEY(), 0) + " ///// " + hs);
                if (pop.getPreferences().getInt(pop.getKEY(), 0) < hs) {
                    pop.getPreferencesEditor().putInt(pop.getKEY(), hs);
                    pop.setNeuerHighScore(true);
                    pop.neuerHighScore = true;
                }

                pop.getPreferencesEditor().putInt("key", hs);
                pop.getPreferencesEditor().commit();
                pop.showPopUpWindow();
            }
        };
        countDownTimer.start();
    }

}
