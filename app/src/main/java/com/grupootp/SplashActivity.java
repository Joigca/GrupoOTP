package com.grupootp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.grupootp.grupootp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class SplashActivity extends Activity{
    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {



                    // Start the next activity
                    Intent mainIntent = new Intent().setClass(SplashActivity.this, Inicio.class);
                    startActivity(mainIntent);

                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();

                }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}

