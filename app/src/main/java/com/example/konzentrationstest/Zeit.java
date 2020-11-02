package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

public class Zeit {

    int millisec;
    private ProgressBar counter;
    CountDownTimer countDownTimer;
    boolean running = true;


    public Zeit (ProgressBar counter, int millisec) {
        this.counter = counter;
        this.millisec = millisec;
    }

    public CountDownTimer getCountDownTimer() { return this.countDownTimer; }

    public void setMillisec(int millisec) {
        this.millisec = millisec;
    }

    public void run() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {  // Zeit.this.millisec
                Zeit.this.counter.setProgress((int) millisUntilFinished / 1000);
                Log.d("---", "MMM: " + millisUntilFinished);
                if (millisUntilFinished < 1200) {
                //if (Zeit.this.millisec == 0) {
                    // Dialogfenster einfügen
                    return;
                }

                if (!Zeit.this.running) {
                    Log.d("---", "Abbruch.");
                    this.cancel();
                    Zeit.this.running = false;
                    return;
                }
                //millisUntilFinished -= 1000;
//                Zeit.this.millisec -= 1000;
            }

            public void onFinish() {
                Zeit.this.running = false;
                Log.d("---", "Done");
//                textView.setText("Done.");        // loeschen
            }
        };
        countDownTimer.start();
        //Zeit.this.running = true;
    }

}
