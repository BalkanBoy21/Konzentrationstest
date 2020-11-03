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
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__formen);

        textView = findViewById(R.id.formText);
        form = findViewById(R.id.formSymbol);

        // fuer die erste Seite
        form.setImageResource(symbolDateien[(int) (Math.random() * symbolDateien.length)]);
        textView.setText(formenText[(int) (Math.random() * formenText.length)]);
    }

    public void check (View view) {

        boolean ergebnisIstRichtig = false;

        String text = textView.getText().toString();
        String dateiName = "";

        // irgendwas mit indexof, am besten, dann benötigt man nicht 10 If-Anweisungen
        int index = Arrays.asList(formenText).indexOf(textView.getText().toString());
        if (index == temp) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
            Log.d("---", "Korrekt");
            ergebnisIstRichtig = true;
        }

        if (view.getId() == R.id.unwahr3 && ergebnisIstRichtig) {        // wenn auf Falsch geklickt wird, das Ergebnis aber richtig ist
            Log.d("---", "Deine Antwort ist nicht korrekt");
            PopUpFenster pop = new PopUpFenster(this, punkte);
            pop.showExitContinueWindow();
            punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
            return;
        } else if (view.getId() == R.id.wahr3 && !ergebnisIstRichtig) {  // wenn auf Richtig geklickt wird, das Ergebnis aber falsch ist
            Log.d("---", "Deine Antwort ist nicht korrekt");
            PopUpFenster pop = new PopUpFenster(this, punkte);
            pop.showExitContinueWindow();
            punkte = 0;
            return;
        } else {
            // Antwort ist richtig
            ++punkte;
            temp = (int) (Math.random() * symbolDateien.length);
            form.setImageResource(symbolDateien[temp]);
            textView.setText(formenText[(int) (Math.random() * formenText.length)]);
        }

    }
}