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

    //private PopUpFenster pop;

    public Zeit (ProgressBar counter, int punkte) {
        this.counter = counter;
        this.punkte = punkte;
    }

    // startet Zeitleiste
    public void laufen(PopUpFenster popUp) {
//        pop = popUp;
        diff = MainActivity.getCurrentDifficultyText();
        milliSec = Integer.parseInt(String.valueOf(Double.parseDouble(diff[1]) * 1000).split("\\.")[0]);

        // Jedes Mal neu resetten, um bei richtiger Antwort die letzte Anzeige der Zeitleiste zu loeschen und die neue Liste wieder voll zu machen
        this.running = true;

        Log.d("---", "Punkte davor " + popUp.punkte);
        CountDownTimer countDownTimer = new CountDownTimer(milliSec, 10) {
            public void onTick(long millisUntilFinished) {
                Zeit.this.counter.setProgress(((int) millisUntilFinished*9) / ((milliSec / 100) / 5));     // mathematisches Umrechnen, im Kopf etwas schwerer zu machen

                if (Zeit.this.counter.getProgress() < 80) {     // die 80 hat nichts besonderes zu bedeuten
                    Log.d("---", "Punkte " + punkte);
                    popUp.showPopUpWindow();
                    //popUp.punkte = 0;
                }

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
            }
        };
        countDownTimer.start();
    }

}
