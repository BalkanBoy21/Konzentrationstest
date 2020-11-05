package com.example.konzentrationstest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

// 2 Möglichkeiten, dies zu gestalten.
// 1.) Formen wie Rechteck, Kreis etc. hinzeichnen und in die Form das Wort schreiben
// 2.) Identisches Spiel wie Schlag den Raab. 2 Muster sind gegeben, 5 Antwortmöglichkeiten: 2 zeigen die Form und Farbe an, dabei bleibt eins übrig und das muss angeklickt werden
public class Aufgabe_Formen extends AppCompatActivity {

    String [] formenText = {"Kreis", "Quadrat", "Stern", "Herz"};
    int []symbolDateien = {R.drawable.kreis, R.drawable.quadrat, R.drawable.stern, R.drawable.herz};
    //String [] symbolDateien = {"kreis.xml", "quadrat.xml", "stern.xml", "herz.xml"};
    TextView textView;
    ImageView form;

    int punkte = 0;
    int randomSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__formen);

        textView = findViewById(R.id.formText);
        form = findViewById(R.id.formSymbol);

        // fuer die erste Seite
        randomSymbol = (int) (Math.random() * symbolDateien.length);
        form.setImageResource(symbolDateien[randomSymbol]);

        if (symbolDateien[randomSymbol] == R.drawable.kreis) {
            textView.setX(5.0f);
            textView.setY(20.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.quadrat) {
            textView.setX(5.0f);
            textView.setY(20.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.stern) {
            textView.setX(5.0f);
            textView.setY(20.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.herz) {
            textView.setX(5.0f);
            textView.setY(20.0f);
        } /*else if (symbolDateien[randomSymbol] == R.drawable.rechteck) {        // Entfernen, da jedes Rechteck auch ein Quadrat ist.
            textView.setX(5.0f);
            textView.setY(20.f);
        }*/

        textView.setText(formenText[(int) (Math.random() * formenText.length)]);
    }

    public void setSymbolPosition (TextView view, int symbolCode) {
        if (symbolCode == R.drawable.kreis) {
            view.setX(330.0f);
            view.setY(510.0f);
        } else if (symbolCode == R.drawable.quadrat) {
            view.setX(330.0f);
            view.setY(510.0f);
        } else if (symbolCode == R.drawable.stern) {
            view.setX(330.0f);
            view.setY(476.0f);
        } else if (symbolCode == R.drawable.herz) {
            view.setX(330.0f);
            view.setY(480.0f);
        }

    }

    public void check (View view) {

        boolean ergebnisIstRichtig = false;

        String text = textView.getText().toString();
        String dateiName = "";

        // irgendwas mit indexof, am besten, dann benötigt man nicht 10 If-Anweisungen
        int index = Arrays.asList(formenText).indexOf(textView.getText().toString());
        if (index == randomSymbol) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
            Log.d("---", "Korrekt");
            ergebnisIstRichtig = true;
        }

        if ((view.getId() == R.id.unwahr3 && ergebnisIstRichtig) || (view.getId() == R.id.wahr3 && !ergebnisIstRichtig)) {
            Log.d("---", "Deine Antwort ist nicht korrekt");
            PopUpFenster pop = new PopUpFenster(this, punkte);
            pop.showExitContinueWindow();
            punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
            return;
        } else {
            // Antwort ist richtig
            ++punkte;
            randomSymbol = (int) (Math.random() * symbolDateien.length);
            form.setImageResource(symbolDateien[randomSymbol]);

            //setSymbolPosition(textView, symbolDateien[randomSymbol]);
            // fuer Position des Textes im Symbol
            if (symbolDateien[randomSymbol] == R.drawable.kreis) {
                setSymbolPosition(textView, R.drawable.kreis);
            } else if (symbolDateien[randomSymbol] == R.drawable.quadrat) {
                setSymbolPosition(textView, R.drawable.quadrat);
            } else if (symbolDateien[randomSymbol] == R.drawable.stern) {
                setSymbolPosition(textView, R.drawable.stern);
            } else if (symbolDateien[randomSymbol] == R.drawable.herz) {
                setSymbolPosition(textView, R.drawable.herz);
            } else if (symbolDateien[randomSymbol] == R.drawable.rechteck) {
                setSymbolPosition(textView, R.drawable.rechteck);
            }
            textView.setText(formenText[(int) (Math.random() * formenText.length)]);
        }

    }
}