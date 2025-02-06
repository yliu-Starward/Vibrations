package com.example.vibrations;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Mainactivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        EditText editText = findViewById(R.id.multiText);

        if (vibrator != null && vibrator.hasVibrator()) {
            Log.d("Check motor","Phone has vibrator motor");
            editText.setText("Phone has vibrator motor\n");
            Button start_button = (Button) findViewById(R.id.start);
            start_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("start_button", "User tapped start");
                    editText.setText("Start motor for 16 minutes\n");
                    vibrator.vibrate(1000000);
                }
            });


            Button stop_button = (Button) findViewById(R.id.stop);
            stop_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("stop_button", "User tapped stop");
                    editText.setText("Stop motor\n");
                    vibrator.cancel();
                }
            });
        }
        else {
            Log.d("Check motor","has no vibrator motor ");
        }


    }

}
