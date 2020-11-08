package com.example.konzentrationstest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

// 2 Möglichkeiten, dies zu gestalten.
// 1.) Formen wie Rechteck, Kreis etc. hinzeichnen und in die Form das Wort schreiben
// 2.) Identisches Spiel wie Schlag den Raab. 2 Muster sind gegeben, 5 Antwortmöglichkeiten: 2 zeigen die Form und Farbe an, dabei bleibt eins übrig und das muss angeklickt werden
public class Aufgabe_Formen extends AppCompatActivity {

    String [] formenText = {"Kreis", "Quadrat", "Stern", "Herz", "Dreieck"};
    int []symbolDateien = {R.drawable.kreis, R.drawable.quadrat, R.drawable.stern, R.drawable.herz, R.drawable.dreieck};
    TextView textView;
    ImageView form;

    static int punkte = 0;
    int randomSymbol;
    String randomText;
    Zeit z;
    ProgressBar timer;

    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    final String KEY = "speicherPreferences3";
    PopUpFenster pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__formen);

        textView = findViewById(R.id.formText);
        form = findViewById(R.id.formSymbol);
        timer = findViewById(R.id.timer_Formen);

        // fuer die erste Seite
        randomSymbol = (int) (Math.random() * symbolDateien.length);

        textView.setX(5.0f);
        if (symbolDateien[randomSymbol] == R.drawable.kreis) {
            textView.setY(20.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.quadrat) {
            textView.setY(20.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.stern) {
            textView.setY(10.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.herz) {
            textView.setY(0.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.dreieck) {        // Entfernen, da jedes Rechteck auch ein Quadrat ist.
            textView.setY(180.0f);
        }

        textView.setText(formenText[(int) (Math.random() * formenText.length)]);
        form.setImageResource(symbolDateien[randomSymbol]);

        int m = 10000;  // dummy, fuer den Anfang
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public void setSymbolPosition (TextView view, int symbolCode) {
        view.setX(330.0f);
        if (symbolCode == R.drawable.kreis) {
            view.setY(708.0f);
        } else if (symbolCode == R.drawable.quadrat) {
            view.setY(709.0f);
        } else if (symbolCode == R.drawable.stern) {
            view.setY(708.0f);
        } else if (symbolCode == R.drawable.herz) {
            view.setY(708.0f);
        } else if (symbolCode == R.drawable.dreieck) {
            view.setY(850.0f);
        }

    }

    public void check (View view) {

        boolean ergebnisIstRichtig = false;

        String currentText = textView.getText().toString();
        int currentSymbol = randomSymbol;

        // irgendwas mit indexof, am besten, dann benötigt man nicht 10 If-Anweisungen
        int index = Arrays.asList(formenText).indexOf(textView.getText().toString());
        if (index == randomSymbol) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
            Log.d("---", "Korrekt");
            ergebnisIstRichtig = true;
        }

        if ((view.getId() == R.id.unwahr3 && ergebnisIstRichtig) || (view.getId() == R.id.wahr3 && !ergebnisIstRichtig)) {
            Log.d("---", "Deine Antwort ist nicht korrekt");
            // Managen des HighScores
            TopScore.highscore_formen = punkte;
            if (preferences.getInt(KEY, 0) < TopScore.highscore_formen) {
                preferencesEditor.putInt(KEY, TopScore.highscore_formen);
            }
            preferencesEditor.putInt("key", TopScore.highscore_formen);
            preferencesEditor.commit();

            pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0));            pop.showExitContinueWindow();
            punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
            return;
        } else {
            // Antwort ist richtig
            ++punkte;

            // damit Symbol und Text nicht 2 Mal hintereinander gleich sind
            do {
                randomSymbol = (int) (Math.random() * symbolDateien.length);
                randomText = formenText[(int) (Math.random() * formenText.length)];
            } while (currentText.equals(randomText) || (randomSymbol == currentSymbol));

            // fuer Position des Textes im Symbol
            if (symbolDateien[randomSymbol] == R.drawable.kreis) {
                setSymbolPosition(textView, R.drawable.kreis);
            } else if (symbolDateien[randomSymbol] == R.drawable.quadrat) {
                setSymbolPosition(textView, R.drawable.quadrat);
            } else if (symbolDateien[randomSymbol] == R.drawable.stern) {
                setSymbolPosition(textView, R.drawable.stern);
            } else if (symbolDateien[randomSymbol] == R.drawable.herz) {
                setSymbolPosition(textView, R.drawable.herz);
            } else if (symbolDateien[randomSymbol] == R.drawable.dreieck) {
                setSymbolPosition(textView, R.drawable.dreieck);
            }
            textView.setText(randomText);
            form.setImageResource(symbolDateien[randomSymbol]);

        }

    }
}