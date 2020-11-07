package com.example.konzentrationstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


// Settings for playing the game
public class Page2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Spinner mod;
    private Button b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        b1 = findViewById(R.id.button_rechnen);
        b2 = findViewById(R.id.button_farbe);
        b3 = findViewById(R.id.button_formen);
        b4 = findViewById(R.id.button_uebersetzen);

        b1.setBackgroundColor(getResources().getColor(R.color.palegreen));
        b2.setBackgroundColor(getResources().getColor(R.color.lightgreen));
        b3.setBackgroundColor(getResources().getColor(R.color.mediumspringgreen));
        b4.setBackgroundColor(getResources().getColor(R.color.springgreen));

        b1.setTextColor(getResources().getColor(R.color.white));
        b2.setTextColor(getResources().getColor(R.color.white));
        b3.setTextColor(getResources().getColor(R.color.white));
        b4.setTextColor(getResources().getColor(R.color.white));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



    public void backToStartMenu(View view) {
        Intent myIntent = new Intent(Page2.this, MainActivity.class);
        Page2.this.startActivity(myIntent);
    }

    public void nextPage(View view) {
        //Intent myIntent = new Intent(Page2.this, MainActivity.class);
        //Page2.this.startActivity(myIntent);
/*
        Class nextAct = null;
        switch (mod.getSelectedItem().toString()) {
            case "Rechnen": nextAct = Aufgabe_Rechnen.class; break;
            case "Farben": nextAct = Aufgabe_Farben.class; break;
            case "Sprachen": nextAct = Aufgabe_Uebersetzen.class; break;
            case "Formen": nextAct = Aufgabe_Formen.class; break;
        }
*/
        Class nextAct = null;
        switch (view.getId()) {
            case R.id.button_rechnen: nextAct = Aufgabe_Rechnen.class; break;
            case R.id.button_farbe: nextAct = Aufgabe_Farben.class; break;
            case R.id.button_formen: nextAct = Aufgabe_Formen.class; break;
            case R.id.button_uebersetzen: nextAct = Aufgabe_Uebersetzen.class; break;
        }

        Intent myIntent2 = new Intent(Page2.this, nextAct);
        Page2.this.startActivity(myIntent2);


    }
    
    // zu Sprachen: Sowas wie englisch = deutsch, also ob ein englisches Wort im deutschen auch so heißt, zB car = auto, house = haus, oder ähnliches. Irgendwas einfaches mit verwirrenden Lösungen

}