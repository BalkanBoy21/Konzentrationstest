package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Zeit extends AppCompatActivity {

    private ProgressBar counter;
    CountDownTimer countDownTimer;
    boolean running = true;
    int punkte = 0;
    String diff;
    int milliSec;

    public Zeit (ProgressBar counter, int punkte) {
        this.counter = counter;
        this.punkte = punkte;
    }

    public CountDownTimer getCountDownTimer() { return this.countDownTimer; }

    public void terminateDifficulty() {

    }

    public void laufen() {
        terminateDifficulty();
        diff = MainActivity.getCurrentDifficulty();
        Log.d("----", "Diff: " + diff);
        milliSec = diff.equals("Easy") ? 3000 : (diff.equals("Moderate") ? 2000 : (diff.equals("Hard") ? 1000 : 5000));       // ziemlich schlechter Code, reicht aber für den Anfang. Lieber den Button erhalten und dann checken ob der entsprechende Button gedrückt wurde
        Log.d("----", "Millisec: " + milliSec);

        counter.setProgress(counter.getMax());
        countDownTimer = new CountDownTimer(milliSec, 1000) {
            public void onTick(long millisUntilFinished) {  // Zeit.this.millisec
                Zeit.this.counter.setProgress((int) millisUntilFinished / 1000);
                Log.d("---", "MMM: " + millisUntilFinished);
                if (millisUntilFinished < 1000) {
                    return;
                }

                if (!Zeit.this.running) {
                    Log.d("---", "Abbruch.");
                    this.cancel();
                    Zeit.this.running = false;
                    return;
                }
            }

            public void onFinish() {
                Zeit.this.running = false;
                //PopUpFenster pop = new PopUpFenster(Zeit.this, punkte);
                //pop.showExitContinueWindow();
                Log.d("---", "Done");
//                textView.setText("Done.");        // loeschen
            }
        };
        countDownTimer.start();
        //Zeit.this.running = true;
    }

    public void run() {
        countDownTimer = new CountDownTimer(milliSec, 1000) {
            public void onTick(long millisUntilFinished) {  // Zeit.this.millisec
                Zeit.this.counter.setProgress((int) millisUntilFinished / 1000);
                Log.d("---", "MMM: " + millisUntilFinished);
                if (millisUntilFinished < 1000) {
                    return;
                }

                if (!Zeit.this.running) {
                    Log.d("---", "Abbruch.");
                    this.cancel();
                    Zeit.this.running = false;
                    return;
                }
            }

            public void onFinish() {
                Zeit.this.running = false;
                //PopUpFenster pop = new PopUpFenster(Zeit.this, punkte);
                //pop.showExitContinueWindow();
                Log.d("---", "Done");
//                textView.setText("Done.");        // loeschen
            }
        };
        countDownTimer.start();
        //Zeit.this.running = true;
    }

}
