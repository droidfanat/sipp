package org.silena.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import org.silena.R;


public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        new View(this).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FullscreenActivity.this, MainActivity.class);
                startActivity(intent);
                FullscreenActivity.this.finish();
            }
        }, 5000);

    }
}
