package com.example.konzentrationstest;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PopUpFenster extends AppCompatActivity {

    PopUpFenster p;
    int punkte;
    static Object obj;
    int highscore;
    boolean neuerHighScore;
    TextView textView;
    Button leave, stay;

    PopupWindow popupWindow;
    View popupView;

    public PopUpFenster(Object obj, int punkte, int highscore, boolean neuerHighScore) {
        this.obj = obj;
        this.punkte = punkte;
        this.highscore = highscore;
        this.neuerHighScore = neuerHighScore;
//        this.onCreate(new Bundle());

        LayoutInflater layoutInflater = ((AppCompatActivity) obj).getLayoutInflater();
        popupView = layoutInflater.inflate(R.layout.activity_popupfenster, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);


        //textView = findViewById(R.id.punktAnzeigeText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupfenster);

        Log.d("----", "Leaveeeee");
        //leave = findViewById(R.id.verlassen);
        //stay = findViewById(R.id.weiter);
        showDialog();

        //Log.d("----", leave.getText().toString() + "");
/*
        stay = findViewById(R.id.weiter);
        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                showExitContinueWindow();
            }
        });
*/
    }

    void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(1);
//        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton("Fortsetzen",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    Intent myIntent = new Intent((PopUpFenster) obj, MainActivity.class);
                                    ((PopUpFenster) obj).startActivity(myIntent);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((PopUpFenster) getActivity())
                                            .doNegativeClick();
                                }
                            }).create();
        }
    }



    String punkteText = "";

    public void showExitContinueWindow() {
        //this.onCreate(null);
        AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity) obj, R.style.türkis);    // (AppCompatActivity) obj
        String punkteText = "\t\t\t\t\t\t\t\t\t\t\t\t\tPunktzahl: " + punkte + "\n" + (neuerHighScore ? "\t\t\t\t\t\t\t\t\t\t\t\t\t" : "\t\t\t\t\t\t\t\t\t\t") + "(" + (neuerHighScore ? "Neuer " : "") + "\t\t\t\tHighscore: " + highscore + ")";

        builder.setTitle(punkteText);
        //builder.setView(View.inflate(this, R.layout.activity_popupfenster, null));
        // unbedingt noch sowas wie durchschnittliche Zeit pro Aufgabe im Format (Sekunden, Hundertstel-Millisekunden), z.B. 1,74 Sekunden
        builder.setMessage("\t\t\t\t\tDeine Antwort ist falsch.\n\t\t\t\t\tBeenden oder fortsetzen?");

        // add the buttons
        builder.setNegativeButton("Beenden", (DialogInterface dialog, int which) -> {
            Intent myIntent = new Intent((AppCompatActivity) obj, MainActivity.class);
            ((AppCompatActivity) obj).startActivity(myIntent);
        });
        builder.setPositiveButton("Fortsetzen", (DialogInterface dialog, int which) -> {});

        LayoutInflater inflater = ((AppCompatActivity) obj).getLayoutInflater();
        View sign_in = inflater.inflate(R.layout.activity_popupfenster, null);

//        leave = (Button) sign_in.findViewById(R.id.verlassen);

        builder.setView(sign_in);
        builder.show();

        // create and show the alert dialog
        //AlertDialog dialog = builder.create();
        //dialog.setCancelable(false);    // sorgt dafür, dass entweder Beenden oder Fortsetzen gedrückt werden muss
        //dialog.show();
    }
}