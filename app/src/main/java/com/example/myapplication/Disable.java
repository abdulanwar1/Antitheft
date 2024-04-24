package com.example.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Disable extends Activity {
    private SharedPreferences preferences;
    private EditText pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable);
        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        pin = findViewById(R.id.entpin);
        Button submit = findViewById(R.id.subpin);
        pin.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        pin.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitPin();
                return true;
            }
            return false;
        });
        submit.setOnClickListener(v -> submitPin());
    }

    private void submitPin() {
        String entered = pin.getText().toString();
        String saved = preferences.getString("PIN", "0000");
        if (entered.equals(saved)) {
            Toast.makeText(this, "Alarm disabled.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Enter the correct pin", Toast.LENGTH_SHORT).show();
        }
    }

}


