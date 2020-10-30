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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        diff = findViewById(R.id.difficulties);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diff.setAdapter(adapter);
        diff.setOnItemSelectedListener(this);

        Spinner mod = findViewById(R.id.modules);
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


    public void backToStartMenu(View view) {
        Intent myIntent = new Intent(Page2.this, MainActivity.class);
        Page2.this.startActivity(myIntent);
    }

    public void nextPage(View view) {
        //Intent myIntent = new Intent(Page2.this, MainActivity.class);
        //Page2.this.startActivity(myIntent);

        Intent myIntent2 = new Intent(Page2.this, Aufgabe_Rechnen.class);
        Page2.this.startActivity(myIntent2);

    }
    
    // zu Sprachen: Sowas wie englisch = deutsch, also ob ein englisches Wort im deutschen auch so heißt, zB car = auto, house = haus, oder ähnliches. Irgendwas einfaches mit verwirrenden Lösungen

}