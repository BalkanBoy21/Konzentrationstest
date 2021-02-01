package com.example.konzentrationstest;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class represents the main menu.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // buttons for difficulty
    static Button easy, moderate, hard;

    // temporary variable used for saving the state of the last clicked button
    private static Button[] btns;

    // saves last clicked button to remember the difficulty of the game
    static String lastdisabledButton = "Leicht";


    /**
     * A method which is called when the program is run
     * @param savedInstanceState reference to a bundle object that is passed into the onCreate method of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        easy = findViewById(R.id.easy);
        moderate = findViewById(R.id.moderate);
        hard = findViewById(R.id.hard);
        btns = new Button[] {easy, moderate, hard};

        ImageView iv = findViewById(R.id.brainIcon);
        iv.setImageResource(R.drawable.braining_finalicon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // saves button when player enters start menu after loosing the game
        if (lastdisabledButton.equals("Leicht".trim())) {
            easy.setBackgroundResource(android.R.drawable.btn_default);
            easy.setEnabled(false);
        } else if (lastdisabledButton.equals("Mittel".trim())) {
            moderate.setBackgroundResource(android.R.drawable.btn_default);
            moderate.setEnabled(false);
        } else if (lastdisabledButton.equals("Schwer".trim())) {
            hard.setBackgroundResource(android.R.drawable.btn_default);
            hard.setEnabled(false);
        }

    }

    /**
     * Method gets chosen difficulty
     * @return text of the current chosen difficulty
     */
    public static String[] getCurrentDifficultyText() {
        String[] difficultyParts = new String[2];
        for (Button difficulty: btns) {
            if (!difficulty.isEnabled()) {
                String[] temp = difficulty.getText().toString().split("\\(");
                // difficulty
                difficultyParts[0] = temp[0].trim();
                // get rest of the string
                difficultyParts[1] = temp[1].trim().split("s")[0].trim();
            }
        }
        return difficultyParts;
    }

    // variable to track event time
    private static long mLastClickTime = 0;

    /**
     * This method sets the new difficulty depending on the clicked button
     * @param view view of the method and the class' xml file.
     */
    public static void terminateDfficulty (View view) {
        // time difference to prevent clicking on two buttons at the same time
        int difference = 100;
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < difference) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        easy.setEnabled(true);
        easy.setBackgroundResource(android.R.drawable.btn_default);
        moderate.setEnabled(true);
        moderate.setBackgroundResource(android.R.drawable.btn_default);
        hard.setEnabled(true);
        hard.setBackgroundResource(android.R.drawable.btn_default);

        // checks which button is clicked and enable it
        for (Button btn: btns) {
            if (view.getId() == btn.getId()) {
                btn.setEnabled(false);
                break;
            }
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
    
    /**
     * Opens module menu when back button is clicked
     * @param view view of the method and the class' xml file.
     */
    public void goToModuleMenu(View view) {
        Intent myIntent = new Intent(MainActivity.this, ModuleMenu.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}