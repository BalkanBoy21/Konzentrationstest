package com.example.konzentrationstest;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PopUpFenster extends AppCompatActivity {

    PopUpFenster p;
    int punkte;
    Object obj;
    int highscore;
    boolean neuerHighScore;

    public PopUpFenster(Object obj, int punkte, int highscore, boolean neuerHighScore) {
        this.obj = obj;
        this.punkte = punkte;
        this.highscore = highscore;
        this.neuerHighScore = neuerHighScore;
    }

    public void showExitContinueWindow() {

        AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity) obj);
        builder.setTitle("\t\t\t\t\t\t\t\t\t\t\tPunktzahl: " + punkte + "\n" + (neuerHighScore ? "\t\t\t\t\t\t": "\t\t\t\t\t\t\t\t\t\t") + "(" + (neuerHighScore ? "Neuer ": "") + "Highscore: " + highscore + ")");

        // unbedingt noch sowas wie durchschnittliche Zeit pro Aufgabe im Format (Sekunden, Hundertstel-Millisekunden), z.B. 1,74 Sekunden
        builder.setMessage("\t\t\t\t\t\t\t\tDeine Antwort ist falsch.\n\t\t\t\t\t\t\t\tBeenden oder fortsetzen?");
        // add the buttons
        builder.setNegativeButton("Beenden", (DialogInterface dialog, int which) -> {
            Intent myIntent = new Intent((AppCompatActivity) obj, MainActivity.class);
            ((AppCompatActivity) obj).startActivity(myIntent);
        });
        builder.setPositiveButton("Fortsetzen", (DialogInterface dialog, int which) -> {});

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);    // sorgt dafür, dass entweder Beenden oder Fortsetzen gedrückt werden muss
        dialog.show();
    }
}
/*

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

import com.example.konzentrationstest.PopUpFenster;
import com.example.konzentrationstest.R;
import com.example.konzentrationstest.Zeit;

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

    int randomForm = 0;
    int lastSymbol;
    int temp = 0;

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
        temp = randomSymbol;        // nur fuer den Uebergang vom ersten zum zweiten Bild
        lastSymbol = symbolDateien[randomSymbol];

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
        punkte = 0;       // sehr wichtig, da man ins Menue zurueckgehen kann


        int m = 10000;  // dummy, fuer den Anfang
        z = new Zeit(timer, punkte);

        //setting preferences
        this.preferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }


    public void check (View view) {

        boolean ergebnisIstRichtig = false;

        String currentText = textView.getText().toString();
        int currentSymbol = lastSymbol;
        //int currentSymbol = symbolDateien[randomSymbol];
        Log.d("----", "Text: " + currentText);
        Log.d("----", "Symbol: " + currentSymbol);

        // irgendwas mit indexof, am besten, dann benötigt man nicht 10 If-Anweisungen
        int index = Arrays.asList(formenText).indexOf(currentText);
        Log.d("-----", "Index: " + index);
        if (index == temp) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
            Log.d("---", "Korrekt");
            ergebnisIstRichtig = true;
        }

        if (((view.getId() == R.id.unwahr3) && ergebnisIstRichtig) || ((view.getId() == R.id.wahr3) && !ergebnisIstRichtig)) {
            Log.d("---", "Deine Antwort ist nicht korrekt");
            // Managen des HighScores
            TopScore.highscore_formen = punkte;
            boolean neuerHighScore = false;
            if (preferences.getInt(KEY, 0) < TopScore.highscore_formen) {
                preferencesEditor.putInt(KEY, TopScore.highscore_formen);
                neuerHighScore = true;
            }
            preferencesEditor.putInt("key", TopScore.highscore_formen);
            preferencesEditor.commit();

            pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), neuerHighScore);
            pop.showExitContinueWindow();
            punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
            return;
        } else {
            // Antwort ist richtig
            ++punkte;

            // damit Symbol und Text nicht 2 Mal hintereinander gleich sind
            do {
                randomSymbol = (int) (Math.random() * symbolDateien.length);

                if (randomSymbol == 0) {
                    int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolA: " + randomSymbol);
                } else if (randomSymbol == formenText.length - 1) {
                    int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolB: " + randomSymbol);
                } else {        // Aeußere sind ausgeschlossen
                    temp = (randomSymbol - 1) + (int) (Math.random() * 3);
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolC: " + randomSymbol);
                }
            } while (formenText[randomSymbol].equals(currentText) || (currentSymbol == lastSymbol));

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
            textView.setText(formenText[randomSymbol]);
            if (symbolDateien[randomSymbol] == R.drawable.dreieck) {
                //form.setY(230.0f);
            }
            form.setImageResource(lastSymbol);

        }

    }

}
*/
/*
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.konzentrationstest.PopUpFenster;
import com.example.konzentrationstest.R;
import com.example.konzentrationstest.TopScore;
import com.example.konzentrationstest.Zeit;

import java.util.Arrays;

import android.content.Context;

package com.example.konzentrationstest;

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

    int randomForm = 0;
    int lastSymbol;
    int temp = 0;

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
        temp = randomSymbol;        // nur fuer den Uebergang vom ersten zum zweiten Bild
        lastSymbol = symbolDateien[randomSymbol];

        textView.setX(5.0f);
        if (symbolDateien[randomSymbol] == R.drawable.kreis) {
            textView.setY(15.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.quadrat) {
            textView.setY(15.0f);
        } else if (symbolDateien[randomSymbol] == R.drawable.stern) {
            textView.setY(5);
        } else if (symbolDateien[randomSymbol] == R.drawable.herz) {
            textView.setY(8);
        } else if (symbolDateien[randomSymbol] == R.drawable.dreieck) {        // Entfernen, da jedes Rechteck auch ein Quadrat ist.
            textView.setY(25);
        }

        textView.setText(formenText[(int) (Math.random() * formenText.length)]);
        form.setImageResource(symbolDateien[randomSymbol]);
        punkte = 0;       // sehr wichtig, da man ins Menue zurueckgehen kann

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
        int currentSymbol = lastSymbol;
        //int currentSymbol = symbolDateien[randomSymbol];
        Log.d("----", "Text: " + currentText);
        Log.d("----", "Symbol: " + currentSymbol);

        // irgendwas mit indexof, am besten, dann benötigt man nicht 10 If-Anweisungen
        int index = Arrays.asList(formenText).indexOf(currentText);
        Log.d("-----", "Index: " + index);
        if (index == temp) {    // man ueberprueft, ob der Index des Textes mit dem Index der Form uebereinstimmt, Voraussetzung: Array formenText und symbolDateien stimmen in der Reihenfolge ueberein
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

            pop = new PopUpFenster(this, punkte, preferences.getInt(KEY, 0), true);
            pop.showExitContinueWindow();
            punkte = 0;     // besser als in der Methode selbst, da nicht auf "Exit" bzw. Continue geklickt werden muss, die Punktzahl aber trotzdem zurückgesetzt werden soll.
            return;
        } else {
            // Antwort ist richtig
            ++punkte;

            // damit Symbol und Text nicht 2 Mal hintereinander gleich sind
            do {
                randomSymbol = (int) (Math.random() * symbolDateien.length);

                if (randomSymbol == 0) {
                    int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolA: " + randomSymbol);
                } else if (randomSymbol == formenText.length - 1) {
                    int[] random_array = new int[]{symbolDateien.length - 1, 0, 1};
                    temp = random_array[(int) (Math.random() * random_array.length)];
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolB: " + randomSymbol);
                } else {        // Aeußere sind ausgeschlossen
                    temp = (randomSymbol - 1) + (int) (Math.random() * 3);
                    lastSymbol = symbolDateien[temp];
                    Log.d("----", "RandomsymbolC: " + randomSymbol);
                }
            } while (formenText[randomSymbol].equals(currentText) || (currentSymbol == lastSymbol));

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
            textView.setText(formenText[randomSymbol]);
            form.setImageResource(symbolDateien[randomSymbol]);

        }

    }
*/