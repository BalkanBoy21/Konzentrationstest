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
public class ModuleMenu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Spinner mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulemenu);

        Button b1 = findViewById(R.id.button_rechnen);
        Button b2 = findViewById(R.id.button_farbe);
        Button b3 = findViewById(R.id.button_formen);
        Button b4 = findViewById(R.id.button_uebersetzen);

        b1.setBackgroundColor(getResources().getColor(R.color.limegreen));
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
    public void onNothingSelected(AdapterView<?> parent) { }

    public void startModule(View view) {
        Class nextModule = null;
        int chosenModule = view.getId();

        if (chosenModule == R.id.button_rechnen) {
            nextModule = Aufgabe_Rechnen.class;
        } else if (chosenModule == R.id.button_farbe) {
            nextModule = Aufgabe_Farben.class;
        } else if (chosenModule == R.id.button_formen) {
            nextModule = Aufgabe_Formen.class;
        } else if (chosenModule == R.id.button_uebersetzen) {
            nextModule = Aufgabe_Uebersetzen.class;
        }

        Intent myIntent = new Intent(ModuleMenu.this, nextModule);
        ModuleMenu.this.startActivity(myIntent);
    }

}