package com.example.konzentrationstest;

import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class Zeit {

    int millisec;
    private ProgressBar counter;

    public Zeit (ProgressBar counter, int millisec) {
        this.counter = counter;
        this.millisec = millisec;
    }

    public void setMillisec(int millisec) {
        this.millisec = millisec;
    }

    public void run() {
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                Zeit.this.counter.setProgress(Zeit.this.millisec / 1000);
                if (Zeit.this.millisec == 0) {
                    // Dialogfenster einfügen
                }
                Zeit.this.millisec -= 1000;
            }

            public void onFinish() {
//                textView.setText("Done.");        // loeschen
            }
        }.start();
    }

}
