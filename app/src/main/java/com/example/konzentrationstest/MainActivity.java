package com.example.konzentrationstest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static Spinner diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diff = findViewById(R.id.difficulties);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diff.setAdapter(adapter);
        diff.setOnItemSelectedListener(this);

        Log.d("---",this.getWindow().getDecorView().getHeight() + "");
        Log.d("---",this.getWindow().getDecorView().getWidth() + "");

    }

    public void btnOnClick(View view) {
        



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void changeSoundIcon(View view) {
        //ImageView sound = findViewById(R.id.sound_icon);


    }

    public Spinner getDiff() {
        return diff;
    }

    public void goToPage2(View view) {
        Intent myIntent = new Intent(MainActivity.this, Page2.class);
        MainActivity.this.startActivity(myIntent);
    }

}