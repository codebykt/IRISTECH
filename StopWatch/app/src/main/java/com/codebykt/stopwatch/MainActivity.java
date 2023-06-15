package com.codebykt.stopwatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView stopwatchTextView;
    private Button startButton;
    private Button stopButton;
    private Button holdButton;

    private Handler handler;
    private Runnable runnable;
    private long startTime;
    private long elapsedTime;
    private boolean running = false;
    private boolean held = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchTextView = findViewById(R.id.stopwatch_textview);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        holdButton = findViewById(R.id.hold_button);

        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    startTime = System.currentTimeMillis();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            elapsedTime = System.currentTimeMillis() - startTime;
                            int milliseconds = (int) (elapsedTime % 1000);
                            int seconds = (int) (elapsedTime / 1000) % 60;
                            int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
                            int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

                            String time = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milliseconds);
                            stopwatchTextView.setText(time);

                            if (running) {
                                handler.postDelayed(this, 10);
                            }
                        }
                    };

                    handler.postDelayed(runnable, 0);
                    running = true;
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    holdButton.setEnabled(true);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                holdButton.setEnabled(false);
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (held) {
                    handler.postDelayed(runnable, 0);
                    holdButton.setText("Hold");
                } else {
                    handler.removeCallbacks(runnable);
                    holdButton.setText("Resume");
                }

                held = !held;
            }
        });
    }

    public void openGitHubPage(View view) {
        String url = "https://github.com/codebykt";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}

