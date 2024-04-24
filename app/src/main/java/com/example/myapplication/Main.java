package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Main extends Activity implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private boolean isArmed = false;
    private MediaPlayer media;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageButton enbtn = findViewById(R.id.enbttn);
        Button extbtn = findViewById(R.id.btnExit);
        ImageButton disbtn = findViewById(R.id.disbttn);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        media = MediaPlayer.create(this, R.raw.alarm_sound);
        media.setLooping(true);

        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        isArmed = prefs.getBoolean("isArmed", false);

        enbtn.setOnClickListener(v -> {
            manager.registerListener(Main.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            isArmed = true;
            prefs.edit().putBoolean("isArmed", isArmed).apply();
            Toast.makeText(Main.this, "Alarm is enabled!", Toast.LENGTH_SHORT).show();
        });
        extbtn.setOnClickListener(view -> {
            Intent intent = new Intent(Main.this, Start.class);
            startActivity(intent);
            finish();
        });
        disbtn.setOnClickListener(view -> {
            if (isArmed) {
                Intent intent = new Intent(Main.this, Disable.class);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(Main.this, "Alarm is already disabled!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            manager.unregisterListener(this);
            isArmed = false;
            prefs.edit().putBoolean("isArmed", isArmed).apply();
            if (media.isPlaying()) {
                media.stop();
            }
            Intent intent = new Intent(Main.this, Start.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isArmed) return;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double position = Math.sqrt(x * x + y * y + z * z);

        if (position > 10 && !media.isPlaying()) {
            Toast.makeText(this, "Movement detected! You have 5 seconds to disable the alarm.", Toast.LENGTH_LONG).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isArmed && !media.isPlaying()) {
                    media.start();
                }
            }, 5000);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (media != null) {
            if (media.isPlaying()) {
                media.stop();
            }
            media.release();
            media = null;
        }
    }
}
