
package com.example.konzentrationstest;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

// 3 Möglichkeiten, dies zu gestalten.
// 1.) Formen wie Rechteck, Kreis etc. hinzeichnen und in die Form das Wort schreiben
// 2.) Identisches Spiel wie Schlag den Raab. 2 Muster sind gegeben, 5 Antwortmöglichkeiten: 2 zeigen die Form und Farbe an, dabei bleibt eins übrig und das muss angeklickt werden
// 3.) vermutlich beste und einfachste Idee:

public class Aufgabe_Formen extends AppCompatActivity {

    private final String [] formenText = {"Kreis", "Quadrat", "Stern", "Herz", "Dreieck"};
    private final int []symbolDateien = {R.drawable.kreis, R.drawable.quadrat, R.drawable.stern, R.drawable.herz, R.drawable.dreieck};
    private TextView textView;
    private ImageView form;

    int punkte = 0;
    int randomSymbol;
    String randomText;

    ProgressBar timer;
    Zeit z;
    String diff;

    // Pop-Up-Hilfsmittel
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    Dialog epicDialog;
    final String KEY = "speicherPreferences_Formen";

    int symbol;
    int temp = 0;
    int milliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__formen);

        textView = findViewById(R.id.formText);
        form = findViewById(R.id.formSymbol);
        timer = findViewById(R.id.timer_Formen);

        // PopUp-Fenster
        epicDialog = new Dialog(this);

        // Setzen der max. Sekundenzahl durch ausgewaehlten Schwierigkeitsgrad
        diff = MainActivity.getCurrentDifficulty();
        milliSec = diff.equals("Easy") ? 3000 : (diff.equals("Moderate") ? 2000 : (diff.equals("Hard") ? 1000 : 5000));       // ziemlich schlechter Code, reicht aber für den Anfang. Lieber den Button erhalten und dann checken ob der entsprechende Button gedrückt wurde
        timer.setMax(milliSec / ((milliSec / 100) / 3));

        // Die erste Timeline sollte aufgefuellt sein
        timer.setProgress(timer.getMax());
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        preferencesEditor.apply();

        // fuer die erste Seite
        randomSymbol = (int) (Math.random() * symbolDateien.length);
        temp = randomSymbol;        // sehr wichtig fuer erste If-Anweisung in Methode check (nur fuer den Uebergang vom ersten zum zweiten Bild)
        symbol = symbolDateien[randomSymbol];

        // fuer Position des Textes im Symbol
        textView.setX(5.0f);
        switch (symbol) {
            case R.drawable.kreis: textView.setY(20.0f); break;
            case R.drawable.quadrat: textView.setY(20.0f); break;
            case R.drawable.stern: textView.setY(2.0f); break;
            case R.drawable.herz: textView.setY(0.0f); break;
            case R.drawable.dreieck: textView.setY(180.0f); break;
        }

        // Erstellen des Starttextes und der Startform
        textView.setText(formenText[(int) (Math.random() * formenText.length)]);
        form.setImageResource(symbolDateien[randomSymbol]);
        punkte = 0;       // sehr wichtig, da man ins Menue zurueckgehen und das Spiel wieder oeffnen kann

    }

    // variable to track event time
    private long mLastClickTime = 0;

    public void check (View view) {
        // Zeitdifferenz, um zu verhindern, dass 2 Buttons auf einmal geklickt werden
        int difference = 100;
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < difference) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        String lastText = textView.getText().toString();
        int lastSymbol = symbol;
        //int currentSymbol = symbolDateien[randomSymbol];

        z.running = false;  // alter Zaehler wird gestoppt
        z = new Zeit(timer, punkte);     // neues Objekt fuer naechste Seite
        z.laufen();     // neuer Zaehler geht los

        boolean antwortIstKorrekt = false;
        boolean neuerHighScore = false;

        int index = Arrays.asList(formenText).indexOf(lastText);

        if (index == temp) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
            antwortIstKorrekt = true;
        }

        // Antwort ist nicht korrekt
        if (((view.getId() == R.id.unwahr3) && antwortIstKorrekt) || ((view.getId() == R.id.wahr3) && !antwortIstKorrekt)) {
            // Setzen des neuen Highscores
            TopScore.highscore_formen = punkte;

            if (preferences.getInt(KEY, 0) < TopScore.highscore_formen) {
                preferencesEditor.putInt(KEY, TopScore.highscore_formen);
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_formen);
            preferencesEditor.commit();

            // Stoppt die Zeit, damit diese nicht weiter geht wenn man bereits im Pop-Up-Fenster ist
            z.running = false;

            PopUpFenster pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore, epicDialog, preferences, preferencesEditor, KEY);
            pop.showPopUpWindow();

            punkte = 0;     // Punktestand zurücksetzen bei falscher Antwort (besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.)

        } else { // Antwort ist korrekt
            // +1 Punkt wenn Antwort richtig
            ++punkte;

            // Symbol und Text werden nicht 2 Mal hintereinander gleich sein
            do {
                randomSymbol = (int) (Math.random() * symbolDateien.length);

                if (randomSymbol == 0) {
                    //int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    int [] random_array = new int[]{0, (int) (Math.random() * symbolDateien.length)};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                } else if (randomSymbol == formenText.length - 1) {
                    //int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    int [] random_array = new int[]{symbolDateien.length - 1, (int) (Math.random() * symbolDateien.length)};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                } else {        // Aeußere sind ausgeschlossen
                    temp = (randomSymbol - 1) + (int) (Math.random() * 3);
                }
                symbol = symbolDateien[temp];
            } while (formenText[randomSymbol].equals(lastText) || (lastSymbol == symbol));

            // fuer Position des Textes im Symbol
            textView.setX(330.0f);
            switch (symbol) {
                case R.drawable.kreis: textView.setY(720.0f); break;
                case R.drawable.quadrat: textView.setY(732.0f); break;
                case R.drawable.stern: textView.setY(708.0f); break;
                case R.drawable.herz: textView.setY(708.0f); break;
                case R.drawable.dreieck: textView.setY(850.0f); break;
            }

            // Setzen des neuen Textes und der neuen Form
            textView.setText(formenText[randomSymbol]);
            form.setImageResource(symbol);

        }

    }

}
