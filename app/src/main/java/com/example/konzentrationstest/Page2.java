package com.example.konzentrationstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


// Settings for playing the game
public class Page2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Spinner diff;
    public Spinner mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);


        mod = findViewById(R.id.modules);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.module_items, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mod.setAdapter(adapter2);
        mod.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public Spinner getDiff() {
        return this.diff;
    }


    public void backToStartMenu(View view) {
        Intent myIntent = new Intent(Page2.this, MainActivity.class);
        Page2.this.startActivity(myIntent);
    }

    public void nextPage(View view) {
        //Intent myIntent = new Intent(Page2.this, MainActivity.class);
        //Page2.this.startActivity(myIntent);

        Class nextAct = null;
        switch (mod.getSelectedItem().toString()) {
            case "Rechnen": nextAct = Aufgabe_Rechnen.class; break;
            case "Farben": nextAct = Aufgabe_Farben.class; break;
            case "Sprachen": nextAct = Aufgabe_Uebersetzen.class; break;
            case "Formen": nextAct = Aufgabe_Formen.class; break;
        }

        Intent myIntent2 = new Intent(Page2.this, nextAct);
        Page2.this.startActivity(myIntent2);

    }
    
    // zu Sprachen: Sowas wie englisch = deutsch, also ob ein englisches Wort im deutschen auch so heißt, zB car = auto, house = haus, oder ähnliches. Irgendwas einfaches mit verwirrenden Lösungen

}