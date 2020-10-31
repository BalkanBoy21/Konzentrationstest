package com.example.konzentrationstest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.Arrays;
public class Aufgabe_Farben extends AppCompatActivity {

    String [] farben = {"Grün", "Gelb", "Blau", "Rot", "Türkis", "Orange", "Braun", "Rosa"};
    int [] farben_hexcode = new int[farben.length];
    TextView farbText;
    ImageButton th_down, th_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aufgabe__farben);

        th_down = findViewById(R.id.unwahr2);
        th_up = findViewById(R.id.wahr2);
        farbText = findViewById(R.id.textFarbe);
        farbText.setTextColor(getResources().getColor(R.color.Grün));

        String [] temp_col = getResources().getStringArray(R.array.colors);
        int [] temp_hex = getResources().getIntArray(R.array.colors);

        for (int i = 0; i < temp_col.length; i++) {
            if (Arrays.asList(farben).contains(temp_col[i])) {
                farben_hexcode[i] = temp_hex[i];
            }
        }

        try {
            Field[] fields = Class.forName(getPackageName() + ".R$color").getDeclaredFields();
            String [] temp = new String[farben.length];
            String colorName = "";
            for (int k = 0; k < fields.length; k++) {
                colorName = fields[k].getName();
                int colorId = fields[k].getInt(null);
                int color = getResources().getColor(colorId);
                if (Arrays.asList(farben).contains(colorName)) {
                    farben_hexcode[k] = color;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void check(View view) {
        boolean korrektesErgebnis = false;
        if (view.getId() == R.id.unwahr2) {

        } else {

        }

        String randomText = farben[(int) (Math.random() * farben.length)];
        int randomFarbe = farben_hexcode[(int) (Math.random() * farben_hexcode.length)];

        farbText.setText(randomText);
        farbText.setTextColor(randomFarbe);
        //farbText.setBackgroundColor(randomFarbe);
    }


}