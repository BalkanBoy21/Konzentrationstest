package com.example.konzentrationstest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static Button easy, moderate, hard;
    private static Button[] btns;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        easy = findViewById(R.id.easy);
        moderate = findViewById(R.id.moderate);
        hard = findViewById(R.id.hard);
        btns = new Button[] {easy, moderate, hard};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //icon = findViewById(R.id.finalAppIcon);
        //icon.setImageResource(null);
        // Schwierigkeitsgrad "easy" als default-Wert
        easy.setEnabled(false);

    }

    public static String getCurrentDifficulty() {
/*        if (!easy.isEnabled()) {
            return "Easy";
        } else if (!moderate.isEnabled()) {
            return "Moderate";
        } else if (!hard.isEnabled()) {
            return "Hard";
        }*/
        for (Button difficulty: btns) {
            if (!difficulty.isEnabled()) {
                return difficulty.getText().toString().split("\\s+")[0];
            }
        }
        return "";
    }

    public static void terminateDfficulty(View view) {
        if (view.getId() == R.id.easy) {
            moderate.setEnabled(true);
            moderate.setBackgroundResource(android.R.drawable.btn_default);
            hard.setEnabled(true);
            hard.setBackgroundResource(android.R.drawable.btn_default);
            easy.setEnabled(false);
            easy.setBackgroundResource(R.color.Rot);
        } else if (view.getId() == R.id.moderate) {
            easy.setEnabled(true);
            easy.setBackgroundResource(android.R.drawable.btn_default);
            hard.setEnabled(true);
            hard.setBackgroundResource(android.R.drawable.btn_default);
            moderate.setEnabled(false);
            moderate.setBackgroundResource(R.color.Rot);
        } else if (view.getId() == R.id.hard) {
            easy.setEnabled(true);
            easy.setBackgroundResource(android.R.drawable.btn_default);
            moderate.setEnabled(true);
            moderate.setBackgroundResource(android.R.drawable.btn_default);
            hard.setEnabled(false);
            hard.setBackgroundResource(R.color.Rot);
        }
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

    public void goToPage2(View view) {
        Log.d("----", "Enabled: " + easy.isEnabled());
        Intent myIntent = new Intent(MainActivity.this, ModuleMenu.class);
        MainActivity.this.startActivity(myIntent);
    }

}