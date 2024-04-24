package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Start extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        EditText pin = findViewById(R.id.piinst);
        EditText cnfmpin = findViewById(R.id.piincon);
        Button pinbtn = findViewById(R.id.piinset);
        pinbtn.setOnClickListener(v -> funcpin(pin, cnfmpin));
        cnfmpin.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                funcpin(pin, cnfmpin);
                return true;
            }
            return false;
        });
    }
    private void funcpin(EditText text, EditText text1) {
        String pin = text.getText().toString();
        String pincnfm = text1.getText().toString();
        if (!pin.isEmpty() && pin.equals(pincnfm)) {
            SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("PIN", pin);
            editor.apply();

            Toast.makeText(this, "PIN has been set!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please fill in both sections with matching pin!", Toast.LENGTH_LONG).show();
        }
    }

}
