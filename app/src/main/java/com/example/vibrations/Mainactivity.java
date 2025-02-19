package com.example.vibrations;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Mainactivity extends AppCompatActivity {

    private Vibrator vibrator;
    private EditText editText;
    private Handler handler = new Handler();
    private Runnable randomVibrationRunnable;
    private long endTime;
    private boolean isVibrating = false; // Track if we're currently in a vibration session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        editText = findViewById(R.id.multiText);

        if (vibrator != null && vibrator.hasVibrator()) {
            Log.d("Check motor", "Phone has a vibrator motor");
            editText.setText("Phone has a vibrator motor\n");

            // Set up "Start" button
            Button start_button = findViewById(R.id.start);
            start_button.setOnClickListener(v -> {
                if (!isVibrating) {
                    startRandomVibrationsFor3Minutes();
                } else {
                    Log.d("start_button", "Already vibrating.");
                }
            });

            // Set up "Stop" button
            Button stop_button = findViewById(R.id.stop);
            stop_button.setOnClickListener(v -> {
                Log.d("stop_button", "User tapped stop");
                stopRandomVibrations();
            });

        } else {
            // No vibrator found on the device
            Log.d("Check motor", "No vibrator motor found");
            editText.setText("No vibrator motor found\n");
        }
    }

    /**
     * Starts random vibrations for 3 minutes.
     */
    private void startRandomVibrationsFor3Minutes() {
        Log.d("start_button", "User tapped start");
        editText.setText("Starting random vibrations for 3 minutes...\n");
        isVibrating = true;

        // Calculate the time at which we should stop (3 minutes from now)
        endTime = System.currentTimeMillis() + (3 * 60 * 1000);

        // Create the Runnable that performs the random vibrations
        randomVibrationRunnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                if (currentTime < endTime && isVibrating) {
                    // Generate a random vibration duration between 50ms and 300ms (example range)
                    int vibrationDuration = getRandomIntInRange(50, 300);

                    // Vibration can be done differently depending on API level
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        VibrationEffect effect = VibrationEffect.createOneShot(vibrationDuration, VibrationEffect.DEFAULT_AMPLITUDE);
                        vibrator.vibrate(effect);
                    } else {
                        vibrator.vibrate(vibrationDuration);
                    }

                    // Generate a random delay before the next vibration between 1s and 3s
                    int delayBetweenVibrations = getRandomIntInRange(1000, 3000);

                    // Schedule the next vibration
                    handler.postDelayed(this, delayBetweenVibrations);

                } else {
                    // Time is up or user stopped vibrations
                    stopRandomVibrations();
                }
            }
        };

        // Start the first random vibration right away
        handler.post(randomVibrationRunnable);
    }

    /**
     * Stops the scheduled random vibrations.
     */
    private void stopRandomVibrations() {
        isVibrating = false;
        handler.removeCallbacksAndMessages(null);
        vibrator.cancel();
        editText.setText("Vibrations stopped.\n");
        Log.d("stop_button", "Vibrations stopped manually or time ended.");
    }

    /**
     * Utility method to get a random integer in a specified range.
     */
    private int getRandomIntInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
