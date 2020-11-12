package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Zeit extends AppCompatActivity {

    private final ProgressBar counter;
    private CountDownTimer countDownTimer;

    boolean running = true;
    int punkte = 0;

    String diff;
    int milliSec;

    public Zeit (ProgressBar counter, int punkte) {
        this.counter = counter;
        this.punkte = punkte;
    }

    public CountDownTimer getCountDownTimer() { return this.countDownTimer; }

    int zehntel;
    public void laufen() {
        diff = MainActivity.getCurrentDifficulty();
        milliSec = diff.equals("Easy") ? 3000 : (diff.equals("Moderate") ? 2000 : (diff.equals("Hard") ? 1000 : 5000));       // ziemlich schlechter Code, reicht aber für den Anfang. Lieber den Button erhalten und dann checken ob der entsprechende Button gedrückt wurde

        // Jedes Mal neu resetten, um bei richtiger Antwort die letzte Anzeige der Zeitleiste zu loeschen und die neue Liste wieder voll zu machen
        this.running = true;
        counter.setMax(milliSec / ((milliSec / 100) / 3));
        counter.setProgress(milliSec);

        countDownTimer = new CountDownTimer(milliSec, 10) {
            public void onTick(long millisUntilFinished) {
                Zeit.this.counter.setProgress((int) millisUntilFinished / ((milliSec / 100) / 3));     // mathematisches Umrechnen, siehe Blatt

                if (!Zeit.this.running) {
                    Log.d("---", "Abbruch.");
                    this.cancel();
                    Zeit.this.running = false;
                    Zeit.this.counter.setProgress(Zeit.this.counter.getMax());
                    return;
                }
            }

            public void onFinish() {
                Zeit.this.running = false;
                // Pop-Up-Fenster erstellen
//                PopUpFenster pop = new PopUpFenster(Zeit.this, punkte);
//                pop.showPopUpWindow();

                Zeit.this.counter.setProgress(Zeit.this.counter.getMax());
            }
        };
        countDownTimer.start();
        //Zeit.this.running = true;

    }

}
